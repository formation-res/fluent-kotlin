package com.tryformation.localization

import com.tryformation.fluent.BundleSequence
import com.tryformation.fluent.FluentBundle
import com.tryformation.fluent.FluentResource
import kotlinx.browser.window

class Localization(
    locales: Array<out Locale>,
    val fetch: suspend (locale: Locale) -> String
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

        return loadBundleSequence(locales.distinct())
    }

    suspend fun loadBundleSequence(
        locales: List<Locale>,
        fallback: Locale? = null
    ): BundleSequence {
        return if (fallback != null) {
//            console.log("loading locales: $locales and fallback: $fallback")
            (locales + fallback).distinct().map { locale ->
                loadBundle(locale)
            }.toTypedArray()
        } else {
            locales.map { loadBundle(it) }.toTypedArray()
        }
    }

    suspend fun loadBundle(locale: Locale): FluentBundle {
//        val url = "$baseUrl/${locale.id}.ftl"
        val ftlContent = fetch(locale) //httpClient.get(url).bodyAsText()

        val resource = FluentResource(ftlContent)
        val bundle = FluentBundle(locale.id)
        val errors = bundle.addResource(resource)
        if (errors.isNotEmpty()) {
            // Syntax errors are per-message and don't break the whole resource
            errors.forEach {
                console.error(it)
            }
        }
        return bundle
    }
}


