import com.tryformation.fluent.getMessage
import com.tryformation.localization.LocalizationService
import com.tryformation.localization.Translatable
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class LocaleTest {

    private val localesToTest = TestLocales.values()

    val ftl = mapOf(
        "en-GB" to """
            demo-title = Recipe Search
            demo-cheese = Cheese
            demo-query = Query
            demo-search-button = Search
            demo-empty-search = Nothing yet. Maybe click Search?
            demo-found-results = Found {${'$'}amount} results!
        """.trimIndent(),
        "nl-NL" to """
            demo-title = Recepten Zoeken
            demo-cheese = Kaas
            demo-query = Zoekvraag
            demo-search-button = Zoeken
            demo-empty-search = Nog niks gevonden. Klik op Zoeken?
            demo-found-results = {${'$'}amount} resultaten gevonden!
        """.trimIndent(),
        "fr-FR" to  "" // empty
    )

    val fetch: suspend (locale: String) -> String? = { locale ->
        console.asDynamic().debug("fetching locale", locale)
        ftl[locale] ?: error("missing locale ${locale}")
    }
    private val localizationService = LocalizationService(localesToTest, fetch)

    object TL {
        val translatables: List<Translatable> = listOf(
            *UiTexts.values()
        )
        enum class UiTexts : Translatable {
            Title,
            Cheese,
            Query,
            SearchButton,
            EmptySearch,
            FoundResults
            ;

            override val prefix: String = "demo"
        }
    }

    @Test
    fun verifyKeysExist() = runTest("verify message id exists") {
        assertSoftly {
            localesToTest.forEach { locale ->
                val bundleSequence = LocalizationService.loadBundleSequence(listOf(locale.id, TestLocales.EN_GB.id),fetch =fetch)
                val bundleSequenceWithoutFallback = LocalizationService.loadBundleSequence(listOf(locale.id), fallbackLocale = null, fetch =fetch)
                TL.translatables.forEach { translatable ->
                    withClue("$locale missing '${translatable.messageId}'") {
                        val message = bundleSequence.getMessage(translatable.messageId)
                        message shouldNotBe null
                    }
                    if(bundleSequenceWithoutFallback.getMessage(translatable.messageId) == null) {
                        console.error("WARN: $locale '${translatable.messageId}' is falling back to english \n")
                    }
                }
            }
        }
    }

    enum class TestTranslations : Translatable {
        SHOULD_FAIL
        ;

        override val prefix: String = "testfail-"
    }

    @Test
    fun verifyFail() = runTest("verify missing translation fails") {
        val translatable = TestTranslations.SHOULD_FAIL
        localesToTest.forEach { locale ->

            val bundle = LocalizationService.loadBundle(locale.id, fetch=fetch)
            bundle shouldNotBe null
            val bundleSequence = LocalizationService.loadBundleSequence(listOf(locale.id), fetch = fetch)

            withClue("error: message id '${translatable.messageId}' is not missing?") {
                val message = bundleSequence.getMessage(translatable.messageId)
                message shouldBe null
            }
            val hasMessage = bundle!!.hasMessage(translatable.messageId)
            if (hasMessage) {
                console.error("WARN: '${translatable.messageId}' should not be available in $locale")
            }
        }
    }
}