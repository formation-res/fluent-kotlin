Fluent-kotlin is a library for kotlin-js that wraps the `fluent-js` npm library and allows you to use [project fluent](https://projectfluent.org/) translations in kotlin-js projects in a reactie way.

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
implementation("com.tryformation:fluent-kotlin:0.1.0")
```


## Background

We use this at FORMATION with our [Fritz2](https://www.fritz2.dev/) based ui. This is a reactive web framework and with this library, you can simply load a translation and the UI will update to use it.

When we were looking for a suitable localization solution, we came across Project Fluent. It currently has libraries for Javascript, Java and other languages. However there was no Kotlin implementation as people on Android apparently use the Java dependency. Howevever, there is a nice javascript version that we were able to integrate with some simple Kotlin type mappings.

## Future

Currently this only works on kotlin-js and we have only tested this with Fritz2; even though it should work with other frameworks. However, we may invest some time to make this a Kotlin multi platform library. Adding a jvm platform implementation should not be too hard using a Java implementation. Likewise, it may be possible to support native platforms given suitable implementations. While we may do this eventually; this is not a high priority for us currently. However, we'd welcome pull requests for either that or a pure Kotlin multi platform dependency.

## Usage

- Define `Translatable` objects via e.g. an enum.
- Create some `.flt` files with translations for different locales
- Create a `Localization` instance and register your locales.

Please refer to the unit tests for detailed examples. Also, check out our [Full Stack Kotlin](https://github.com/formation-res/kt-fullstack-demo) project, which integrates this library to add translations.
