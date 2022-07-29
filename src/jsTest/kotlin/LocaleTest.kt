import com.tryformation.fluent.getMessage
import com.tryformation.localization.Locale
import com.tryformation.localization.Localization
import com.tryformation.localization.Translatable
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class LocaleTest {
    enum class LocaleNames : Translatable {
        EN_GB,
        NL_NL,
        ;

        override val prefix: String = "locale"
    }

    enum class TestLocale(
        override val id: String,
        override val translatable: Translatable,
        override val aliases: Array<String>,
    ) : Locale {
        EN_GB("en-GB", LocaleNames.EN_GB, arrayOf("en")),
        NL_NL("nl-NL", LocaleNames.NL_NL, arrayOf("nl")),
    }

    private val localesToTest = TestLocale.values()

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
        """.trimIndent()
    )

    private val localization = Localization(localesToTest) { locale ->
        console.asDynamic().debug("fetching locale", locale)
        ftl[locale.id] ?: error("missing locale ${locale.id}")
    }

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
                val bundleSequence = localization.loadBundleSequence(listOf(locale))
                val bundleSequenceWithoutFallback = localization.loadBundleSequence(listOf(locale), fallback = null)
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

            val bundle = localization.loadBundle(locale)
            val bundleSequence = localization.loadBundleSequence(listOf(locale))

            withClue("error: message id '${translatable.messageId}' is not missing?") {
                val message = bundleSequence.getMessage(translatable.messageId)
                message shouldBe null
            }
            val hasMessage = bundle.hasMessage(translatable.messageId)
            if (hasMessage) {
                console.error("WARN: '${translatable.messageId}' should not be available in $locale")
            }
        }
    }
}