import BasicStrings.*
import com.tryformation.fluent.translate
import com.tryformation.localization.LocalizationService
import com.tryformation.localization.Translatable
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

enum class BasicStrings : Translatable {
    hello,
    bye,
    nope,
    wtf
    ;

    override val prefix: String = "pre"
}

class LocalizationTest {
    val ftl = mapOf("en-GB" to """
            pre-hello = Oh Hai
            pre-bye = OKAY BYE THEN!
            pre-wtf = What The Fuck
        """.trimIndent(),
        "fr-FR" to """
            pre-hello = Bonjour
            pre-bye = Au Revoir
        """.trimIndent()
    )

    val fetch: suspend (locale: String) -> String? = { locale ->
        console.asDynamic().debug("fetching locale", locale)
        ftl[locale] ?: error("failed too get locale for id ${locale}")
    }
    private val localizationService = LocalizationService(
        locales = TestLocales.values(),
        fetch = fetch
    )


    @Test
    fun shouldTranslate() = runTest("shouldTranslate") {

        val english = LocalizationService.loadBundleSequence(listOf(TestLocales.EN_GB.id), fetch = fetch)
        val french = LocalizationService.loadBundleSequence(listOf(TestLocales.FR_FR.id), TestLocales.EN_GB.id, fetch = fetch)

        // six ways to get a translation :-)
        assertSoftly {
            english.translate(bye) shouldBe "OKAY BYE THEN!"

            french.translate(bye) shouldBe "Au Revoir"
            french.translate(wtf.messageId) shouldBe "What The Fuck"
            french.translate(nope) shouldBe nope.name
        }
    }
}