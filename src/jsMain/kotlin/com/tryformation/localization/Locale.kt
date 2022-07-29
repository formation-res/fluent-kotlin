package com.tryformation.localization

interface Locale {
    val id: String
    val translatable: Translatable
    val aliases: Array<String>
}
