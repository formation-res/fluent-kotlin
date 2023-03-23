package com.tryformation.localization

/**
 * A `TranslatedValue` holds a translated [message] for a [translatable], and some metadata such as [localeUsed] and the index of the locale
 * in the `LocalizedTranslationBundleSequence` bundle list. This index is used to calculate the
 * [bestTranslationUsed] (0),  [fallbackLocaleUsed] (>0), and [noTranslationFound] (-1) properties.
 *
 * You may use this to log translation failures for missing translations or incomplete bundles (if the fallback is used).
 */
data class TranslatedValue(
    val translatable: Translatable? = null,
    val message: String,
    val localeUsed: String?,
    val localeIndex: Int,
    val messageId: String = translatable?.messageId ?: error("messageId must be provided"),
) {
    val bestTranslationUsed: Boolean = localeIndex == 0
    val fallbackLocaleUsed: Boolean = localeIndex > 0
    val noTranslationFound: Boolean = localeIndex < 0
}