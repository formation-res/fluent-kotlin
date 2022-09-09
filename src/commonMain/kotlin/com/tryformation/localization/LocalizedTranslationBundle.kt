package com.tryformation.localization

interface LocalizedTranslationBundle {
    val locale: List<String>
    fun translate(translatable: Translatable, args: Map<String, Any>? = null): String?
}