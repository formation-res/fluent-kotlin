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
        fallback: String = translatable.defaultTranslation,
    ): TranslatedValue {
        var localeUsed = 0
        return this.bundles.firstNotNullOfOrNull {
            it.translate(translatable, args)?.let { m ->
                TranslatedValue(
                    translatable = translatable,
                    message = m,
                    localeUsed = it.locale.takeIf { locales -> locales.isNotEmpty() }?.first(),
                    localeIndex = localeUsed,
                    messageId = translatable.messageId
                )
            }.also {
                localeUsed++
            }
        } ?: TranslatedValue(translatable, fallback, null, -1)
    }

    fun format(
        messageId: String,
        args: Map<String, Any>? = null,
        fallback: String = messageId,
    ): TranslatedValue {
        var localeUsed = 0
        return this.bundles.firstNotNullOfOrNull {
            it.translate(messageId, args)?.let { m ->
                TranslatedValue(
                    message = m,
                    localeUsed = it.locale.takeIf { locales -> locales.isNotEmpty() }?.first(),
                    localeIndex = localeUsed,
                    messageId = messageId,
                )
            }.also {
                localeUsed++
            }
        } ?: TranslatedValue(message = fallback, localeUsed = null, localeIndex = -1, messageId = messageId)
    }
}


