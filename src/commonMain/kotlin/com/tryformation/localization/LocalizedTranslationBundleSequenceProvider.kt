package com.tryformation.localization

/**
 * This provider class should load translation bundles based on the user's prefered locales.
 */
expect class LocalizedTranslationBundleSequenceProvider {
    /**
     * Loads a bundle sequence. You specify the [locales] and a [fallbackLocale]
     * that is used in case there are no matching bundles for any of the [locales].
     *
     * The [fetch] function is used to fetch the translations for a locale as a `String?`.
     * Use null in case no translation is available for the locale. Fetch may be implemented using
     * String literals, the browser fetch API, opening a file on the jvm, etc. It's up to you.
     */
    suspend fun loadBundleSequence(
        locales: List<String>,
        fallbackLocale: String,
        fetch: suspend (locale: String) -> String?
    ): LocalizedTranslationBundleSequence
}