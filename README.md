Fluent-kotlin is a multi platform library for kotlin that provides message formatting for [project fluent](https://projectfluent.org/fluent/guide/) style localizations.

Instead of re-implementing this for platforms, this library simply adapts existing libraries for different platforms.

- For javascript we use [fluent.js](https://github.com/projectfluent/fluent.js/)
- On the jvm we use [xyzsd/fluent](https://github.com/xyzsd/fluent). This is a functional but not fully complete implementation; so your mileage may vary.

## Fluent AI

Check out our AI powered translation tool: [Fluent AI](https://fluent-ai.jillesvangurp.com). We wrote this tool to manage our project fluent translation tools. And of course it's a kotlin-js applications that uses fluent-kotlin for its translations. So, it serves as a nice example of how to use fluent-kotlin as well. You can dive into the source code [here](https://github.com/jillesvangurp/fluent-ai).

- Edit language strings for different languages side by side
- Easily identify missing translations and use AI to add the missing ones.
- Add entire new languages with translations fully automatically
- Sort and organize your translation strings and eliminate copy pasted identical translations

## Gradle

Add the [tryformation](https://tryformation.com) maven repository:

```kotlin
repositories {
    mavenCentral()
    maven("https://maven.tryformation.com/releases") {
        content {
            includeGroup("com.tryformation")
        }
    }
}
```

Then add the latest version:

```kotlin
implementation("com.tryformation:fluent-kotlin:0.1.2")
```

Note, check the release tags for the latest version.


## Background

We use this library at FORMATION with our [Fritz2](https://www.fritz2.dev/) based ui as well as in our Spring Boot based server for sending e.g. localized emails. 

When we were looking for a suitable localization solution, we came across Mozilla's [Project Fluent](https://projectfluent.org/). It currently has libraries for Javascript, Java and other languages. But since we are using Kotlin everywhere in our stack, we decided to abstract the behavior so we could share code, and translations between our client and server.

## Usage

- Define `Translatable` objects via e.g. an enum. This will allow you to use enums instead of string literals to identify your language strings.
- Create some `.ftl` files with translations for different locales. Consider using [Fluent AI](https://fluent-ai.jillesvangurp.com) for creating your translations and managing localization strings.
- Instantiate `LocalizedTranslationBundleSequenceProvider`
- Use a custom fetchFtl function to fetch your ftl source by locale id. If you e.g. put ftl files in resources you can fetch them using the browser fetch API (with kotlin-js) or by opening a classpath resource (kotlin-jvm).
- Use it to load a `LocalizedTranslationBundleSequence`
- Use that to format your `Translatable` identifiers.
- Create some extension functions to make all this easy with your framework. We did not bundle this on purpose to keep the code base **framework neutral**.

## How we do it with Fritz2

```kotlin
private val translationStore by lazy {
    withKoin {
        get<TranslationStore>()
    }
}

fun HtmlTag<HTMLElement>.translate(translatable: Translatable, args: Map<String, Any>? = null) =
    translationStore[translatable, args ?: mapOf()].renderText()

fun HtmlTag<HTMLElement>.translate(translatable: Translatable, vararg args: Pair<String, Any>) =
    translationStore[translatable, mapOf(*args)].renderText()

fun Translatable?.getString(args: Map<String, Any>? = null) = this?.let {
    translationStore.getString(it, args ?: mapOf())
} ?: ""

fun Translatable?.getString(vararg args: Pair<String, Any>) = this?.let {
    translationStore.getString(it, mapOf(*args))
}

fun String.translate(args:Map<String, Any>? = null) = translationStore.getString(this,args)
fun String.translate(vararg args: Pair<String, Any>) = translationStore.getString(this,mapOf(*args))

```

The TranslationStore is the only non trivial bit of code:

```kotlin
class TranslationStore(
bundleSequence: LocalizedTranslationBundleSequence,
languageCodeSettingStore: Store<String?>,
) : RootStore<LocalizedTranslationBundleSequence>(
initialData = bundleSequence,
job = Job(),
) {

    operator fun get(translatable: Translatable): Flow<String> = data.map {
        it.format(
            translatable = translatable,
            args = null,
            fallback = translatable.humanReadable,
        ).value
    }

    operator fun get(translatable: Translatable, args: Map<String, Any>): Flow<String> = data.map {
        it.format(
            translatable = translatable,
            args = args,
            fallback = translatable.humanReadable,
        ).value
    }

    operator fun get(translatable: Translatable, args: Flow<Map<String, Any>>): Flow<String> =
        data.combine(args) { tl, json -> tl.format(translatable = translatable, args = json, fallback = translatable.humanReadable).value }


    fun get(stringId: String): Flow<String> = data.map { it.format(stringId, null).value }
    fun get(stringId: String, args: Map<String, Any>): Flow<String> = data.map { it.format(stringId, args).value }
    fun get(stringId: String, args: Flow<Map<String, Any>>): Flow<String> = data.combine(args) { tl, json -> tl.format(stringId, json).value }

    fun getString(translatable: Translatable, json: Map<String, Any>? = null): String = current.format(
        translatable = translatable,
        args = json,
        fallback = translatable.humanReadable
    ).value
    fun getString(stringId: String, json: Map<String, Any>? = null): String = current.format(
        messageId = stringId,
        args = json,
        fallback = stringId.humanReadable()
    ).value

    private val setLocale = handle<String?> { current, locale ->
        if (locale != null) {
            console.log("switching locale", locale)
            provider.loadBundleSequence(listOf(locale), fallbackLocale = Locales.EN_GB.id, ::fetchFtl)
        } else current
    }

    init {
        languageCodeSettingStore.data handledBy setLocale
    }

    companion object {
        private val provider = LocalizedTranslationBundleSequenceProvider()

        suspend fun load(languageCodeSettingStore: Store<String?>, localeStore: Store<Locale>): TranslationStore {
            return withKoin {

                val intialBundleSequence = provider.loadBundleSequence(
                    locales = listOfNotNull(
                        languageCodeSettingStore.current,
                        window.navigator.language,
                    ) + window.navigator.languages,
                    fallbackLocale = Locales.EN_GB.id,
                    fetch = ::fetchFtl,
                )
                intialBundleSequence.bundles.firstNotNullOfOrNull {
                    it.locale.first().let { localeCode ->
                        Locales.findByIdOrNull(localeCode)
                    }
                }?.let {

                    localeStore.update(it)
                }

                TranslationStore(intialBundleSequence, languageCodeSettingStore)
            }

        }
    }
}
```
This is lifted from our production code, so it probably needs some adjusting.

But the principle is very simple:

- Stores are a Fritz2 thing. You basically get a state flow of state; in this case a `LocalizedTranslationBundleSequence` that you can lookup translation strings.
- The helpers above, allow you to easily use `Translatable` texts in Fritz2 components. They just look up the text using the current bundle sequence. If you update the store with a new one, all the texts change in the UI.
- To do that, we use setLocale( ) which we can call from a settings screen.
- Note that load correctly uses window.navigator.language to pick the user's preferred locale.

Check out our [Full Stack Kotlin](https://github.com/formation-res/kt-fullstack-demo) project, which integrates this library to add translations and provides an example of how you could get started. [Fluent AI](https://fluent-ai.jillesvangurp.com) also uses this, obviously.

## Development status

We use this at FORMATION. Our choice of UI framework is a bit exotic, which puts us on the spot to DIY things like localization. This has been in use at FORMATION for the past four years or so and we treat it as stable. 

But there isn't a large user base. Project fluent however is a safe bet. So if you are looking for a localization file format, that's not a bad choice. And if you are doing Kotlin code, using this library is not the worst way to use it.

So, it works but expect to be a bit hands on with this is the honest advice. It's open source; so feel free to fork/adapt/whatever. We appreciate feedback, pull requests, etc.




