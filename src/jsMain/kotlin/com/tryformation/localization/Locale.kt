package com.tryformation.localization

interface Locale : Translatable {
    val languageCode: String
    val countryCode: String?
    val aliases: Array<String>

    /**
     * Bcp 47 style locale id of the [Locale.languageCode] with an optional variant in the form of the [Locale.countryCode] separated by a dash.
     */
    val id get() = "$languageCode${if (countryCode.isNullOrBlank()) "" else "-$countryCode"}"
}

