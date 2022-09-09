@file:Suppress("unused")

package com.tryformation.localization

class LocalizedTranslationBundleSequence(val bundles: List<LocalizedTranslationBundle>) {
    /**
     * Use a LocalizedTranslationBundleSequence to tranlate a [translatable].
     * In case no translation can be found, the [fallback] is used. This defaults to using the `Translatable.name`
     *
     * If your `LocalizedTranslationBundle` implementation supports parameters, you can pass those using the [args].
     */
    fun translate(
        translatable: Translatable,
        fallback: String = translatable.name,
        args: Map<String, Any>? = null
    ): TranslatedValue {
        var localeUsed = 0
        return this.bundles.firstNotNullOfOrNull {
            it.translate(translatable, args)?.let { m ->
                TranslatedValue(
                    translatable = translatable,
                    message = m,
                    localeUsed = it.locale.takeIf { locales -> locales.isNotEmpty() }?.first(),
                    localeIndex = localeUsed
                )
            }.also {
                localeUsed++
            }
        } ?: TranslatedValue(translatable, fallback, null, -1)
    }
}


