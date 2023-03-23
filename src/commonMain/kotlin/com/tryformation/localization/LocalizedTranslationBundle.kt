package com.tryformation.localization

interface LocalizedTranslationBundle {
    val locale: List<String>
    fun translate(translatable: Translatable, args: Map<String, Any>? = null): String?
    fun translate(messageId: String, args: Map<String, Any>? = null): String?
}