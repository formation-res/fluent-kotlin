package com.tryformation.localization

import com.tryformation.fluent.BundleSequence
import com.tryformation.fluent.FluentBundle
import com.tryformation.fluent.FluentResource
import kotlinx.browser.window

/**
 * Service class that allows you to to look up the right translations for your locales.
 *
 * @param locales list of locales that you support
 * @param fetch function that produces the flt content given a locale id
 */
class LocalizationService(
    locales: Array<out Locale>,
    val fetch: suspend (locale: String) -> String?
) {
    private val locales: MutableList<Locale> = mutableListOf(*locales)

    fun registerLocale(locale: Locale) {
        locales += locale
    }

    fun getLocale(languageId: String): Locale? {
        val values = locales
        return values.firstOrNull { it.id == languageId }
            ?: values.firstOrNull { languageId in it.aliases }
    }

    suspend fun getDefault(): BundleSequence {
        val locales: List<Locale> = window.navigator.language.let { langId ->
            getLocale(langId)?.let { listOf(it) }
        } ?: window.navigator.languages.mapNotNull { langId ->
            getLocale(langId)
        }.takeIf { it.isNotEmpty() }
        ?: emptyList()

        return loadBundleSequence(locales.map { it.id }.distinct(), fetch = fetch)
    }

    companion object {
        suspend fun loadBundleSequence(
            locales: List<String>,
            fallbackLocale: String? = null,
            fetch: suspend (locale: String) -> String?
        ): BundleSequence {
            return if (fallbackLocale != null) {
                (locales + fallbackLocale).distinct().mapNotNull { locale ->
                    loadBundle(locale, fetch)
                }.toTypedArray()
            } else {
                locales.mapNotNull { loadBundle(it, fetch) }.toTypedArray()
            }
        }

        suspend fun loadBundle(locale: String, fetch: suspend (locale: String) -> String?): FluentBundle? {
            val ftlContent = fetch(locale)
            return ftlContent?.let {
                val resource = FluentResource(ftlContent)
                val bundle = FluentBundle(locale)
                val errors = bundle.addResource(resource)
                if (errors.isNotEmpty()) {
                    // Syntax errors are per-message and don't break the whole resource
                    errors.forEach {
                        console.error(it)
                    }
                }
                bundle
            }
        }
    }
}


