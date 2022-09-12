package com.tryformation.localization

import com.tryformation.fluent.translate
import kotlin.js.json

fun Map<String, Any>?.toJson() = this?.let { args -> json(*args.entries.map { it.key to it.value }.toTypedArray()) }

actual class LocalizedTranslationBundleSequenceProvider {
    actual suspend fun loadBundleSequence(
        locales: List<String>,
        fallbackLocale: String,
        fetch: suspend (locale: String) -> String?
    ): LocalizedTranslationBundleSequence {
        return LocalizationService.loadBundleSequence(locales, fallbackLocale, fetch).let { bs ->
            bs.map { fluentBundle ->
                object : LocalizedTranslationBundle {
                    override val locale: List<String> get() = fluentBundle.locales.toList()

                    override fun translate(translatable: Translatable, args: Map<String, Any>?): String? {
                        val jsonArgs = args.toJson()
                        return try {
                            fluentBundle.translate(translatable.messageId, jsonArgs)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }.let { bundles ->
                LocalizedTranslationBundleSequence(bundles)
            }
        }
    }
}

