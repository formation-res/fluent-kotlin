@file:Suppress("unused")

package com.tryformation.localization

class LocalizedTranslationBundleSequence(val bundles: List<LocalizedTranslationBundle>) {
    /**
     * Use a LocalizedTranslationBundleSequence to tranlate a [translatable].
     * In case no translation can be found, the [fallback] is used. This defaults to using the `Translatable.name`
     *
     * If your `LocalizedTranslationBundle` implementation supports parameters, you can pass those using the [args].
     */
    fun format(
        translatable: Translatable,
        args: Map<String, Any>? = null,
        fallback: String = translatable.name,
    ): TranslatedValue {
        var localeUsed = 0
        return this.bundles.firstNotNullOfOrNull {
            it.translate(translatable, args)?.let { m ->
                TranslatedValue(
                    translatable = translatable,
                    message = m,
                    localeUsed = it.locale.takeIf { locales -> locales.isNotEmpty() }?.first(),
                    localeIndex = localeUsed,
                    stringId = translatable.messageId
                )
            }.also {
                localeUsed++
            }
        } ?: TranslatedValue(translatable, fallback, null, -1)
    }

    fun format(
        stringId: String,
        args: Map<String, Any>? = null,
        fallback: String = stringId,
    ): TranslatedValue {
        var localeUsed = 0
        return this.bundles.firstNotNullOfOrNull {
            it.translate(stringId, args)?.let { m ->
                TranslatedValue(
                    message = m,
                    localeUsed = it.locale.takeIf { locales -> locales.isNotEmpty() }?.first(),
                    localeIndex = localeUsed,
                    stringId = stringId,
                )
            }.also {
                localeUsed++
            }
        } ?: TranslatedValue(message = fallback, localeUsed = null, localeIndex = -1, stringId = stringId)
    }
}


