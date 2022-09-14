[![Deploy to maven](https://github.com/formation-res/fluent-kotlin/actions/workflows/deploy.yaml/badge.svg)](https://github.com/formation-res/fluent-kotlin/actions/workflows/deploy.yaml)

Fluent-kotlin is a multi platform library for kotlin that provides message formatting for [project fluent](https://projectfluent.org/fluent/guide/) style localizations.

Instead of re-implementing this for platforms, this library simply adapts existing libraries for different platforms.

- For javascript we use [fluent.js](https://github.com/projectfluent/fluent.js/)
- On the jvm we use [xyzsd/fluent](https://github.com/xyzsd/fluent). This is a functional but not fully complete implementation; so your mileage may vary.

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

When we were looking for a suitable localization solution, we came across Project Fluent. It currently has libraries for Javascript, Java and other languages. But since we are using Kotlin everywhere in our stack, we decided to abstract the behavior so we could share code, and translations between our client and server.

## Usage

- Define `Translatable` objects via e.g. an enum. This will allow you to use enums instead of string literals to identify your language strings.
- Create some `.ftl` files with translations for different locales. 
- Instantiate `LocalizedTranslationBundleSequenceProvider`
- Use a custom fetchFtl function to fetch your ftl source by locale id. If you e.g. put ftl files in resources you can fetch them using the browser fetch API (with kotlin-js) or by opening a classpath resource (kotlin-jvm).
- Use it to load a `LocalizedTranslationBundleSequence`
- Use that to format your `Translatable` identifiers.

Please refer to the unit tests for detailed examples. Also, check out our [Full Stack Kotlin](https://github.com/formation-res/kt-fullstack-demo) project, which integrates this library to add translations.
