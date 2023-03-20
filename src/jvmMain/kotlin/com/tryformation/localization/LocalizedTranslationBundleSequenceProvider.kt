package com.tryformation.localization

import fluent.bundle.FluentBundle
import fluent.functions.cldr.CLDRFunctionFactory
import fluent.syntax.parser.FTLParser
import fluent.syntax.parser.FTLStream
import java.util.Locale

class JavaFluentAdapter(override val locale: List<String>, private val ftlContent: String) :
    LocalizedTranslationBundle {
    private val bundle: FluentBundle

    override fun translate(stringId: String, args: Map<String, Any>?): String? {
        return if(bundle.getMessage(stringId).isEmpty) {
            null
        } else {
            bundle.format(stringId, args?.toMutableMap() ?: mutableMapOf<String, Any>())
        }
    }

    override fun translate(translatable: Translatable, args: Map<String, Any>?): String? {
        return translate(translatable.messageId, args)
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
            }
        }
        return LocalizedTranslationBundleSequence(bundles)
    }
}