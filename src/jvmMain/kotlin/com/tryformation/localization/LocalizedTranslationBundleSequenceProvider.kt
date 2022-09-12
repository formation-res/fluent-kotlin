package com.tryformation.localization

import fluent.bundle.FluentBundle
import fluent.functions.cldr.CLDRFunctionFactory
import fluent.syntax.parser.FTLParser
import fluent.syntax.parser.FTLStream
import java.util.Locale

//class SimpleFluentBasedBundle(override val locale: List<String>, val translationMap: Map<String, String>) :
//    LocalizedTranslationBundle {
//    override fun translate(translatable: Translatable, args: Map<String, Any>?): String? {
//        return translationMap[translatable.messageId]
//    }
//
//    companion object {
//        fun fromString(locales: List<String>, source: String): SimpleFluentBasedBundle {
//            val translationMap = source.lines().filter {
//                val trimmed = it.trim()
//                trimmed.isNotBlank() && !trimmed.startsWith("#")
//            }.mapNotNull { line ->
//                val index = line.indexOf('=')
//                if (index > 0) {
//                    line.substring(0, index).trim() to line.substring(index).trim()
//                } else {
//                    null
//                }
//            }.toMap()
//            return SimpleFluentBasedBundle(locales, translationMap)
//        }
//    }
//}

class JavaFluentAdapter(override val locale: List<String>, private val ftlContent: String) :
    LocalizedTranslationBundle {
    private val bundle: FluentBundle

    override fun translate(translatable: Translatable, args: Map<String, Any>?): String? {
        return if(bundle.getMessage(translatable.messageId).isEmpty) {
            null
        } else {
            bundle.format(translatable.messageId, args?.toMutableMap() ?: mutableMapOf<String, Any>())
        }
    }

    init {
        val ftlResource = FTLParser.parse(FTLStream.of(ftlContent))
        val l = Locale.forLanguageTag(locale.first())
        bundle = FluentBundle
            .builder(l, CLDRFunctionFactory.INSTANCE).addResource(ftlResource)
            .build()
    }
}

actual class LocalizedTranslationBundleSequenceProvider {
    actual suspend fun loadBundleSequence(
        locales: List<String>,
        fallbackLocale: String,
        fetch: suspend (locale: String) -> String?
    ): LocalizedTranslationBundleSequence {
        val bundles = (locales + fallbackLocale).mapNotNull { locale ->
            fetch.invoke(locale)?.let { source ->
                JavaFluentAdapter(listOf(locale),source)
//                SimpleFluentBasedBundle.fromString(listOf(locale), source)
            }
        }
        return LocalizedTranslationBundleSequence(bundles)
    }
}