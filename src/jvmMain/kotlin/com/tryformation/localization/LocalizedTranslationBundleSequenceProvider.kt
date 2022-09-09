package com.tryformation.localization

class SimpleFluentBasedBundle(override val locale: List<String>, val translationMap: Map<String, String>) : LocalizedTranslationBundle {
    override fun translate(translatable: Translatable, args: Map<String, Any>?): String? {
        return translationMap[translatable.messageId]
    }

    companion object {
        fun fromString(locales: List<String>, source: String): SimpleFluentBasedBundle {
            val translationMap = source.lines().filter {
                val trimmed = it.trim()
                trimmed.isNotBlank() && !trimmed.startsWith("#")
            }.mapNotNull { line ->
                val index = line.indexOf('=')
                if(index>0) {
                    line.substring(0,index).trim() to line.substring(index).trim()
                } else {
                    null
                }
            }.toMap()
            return SimpleFluentBasedBundle(locales, translationMap)
        }
    }
}

actual class LocalizedTranslationBundleSequenceProvider {
    actual suspend fun loadBundleSequence(
        locales: List<String>,
        fallbackLocale: String,
        fetch: suspend (locale: String) -> String?
    ): LocalizedTranslationBundleSequence {
        val bundles = (locales + fallbackLocale).mapNotNull { locale ->
            fetch.invoke(locale)?.let {  source ->
                SimpleFluentBasedBundle.fromString(listOf(locale), source)
            }
        }
        return LocalizedTranslationBundleSequence(bundles)
    }
}