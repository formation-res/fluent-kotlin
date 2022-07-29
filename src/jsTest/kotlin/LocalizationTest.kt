import BasicStrings.bye
import BasicStrings.nope
import com.tryformation.fluent.translate
import com.tryformation.localization.Localization
import com.tryformation.localization.Translatable
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

enum class BasicStrings : Translatable {
    hello,
    bye,
    nope
    ;

    override val prefix: String = "basic"
}
enum class LocaleStrings : Translatable {
    EN_GB,
    FR_FR,
    ;

    override val prefix: String = "locale"
}

class LocalizationTest {
    private enum class TestLocale(
        override val id: String,
        override val translatable: Translatable,
        override val aliases: Array<String>,
    ): com.tryformation.localization.Locale {
        EN_GB("en-GB", LocaleStrings.EN_GB, arrayOf("en")),
        FR_FR("fr-FR", LocaleStrings.FR_FR, arrayOf("fr")),
        ;
    }
    val ftl = mapOf("en-GB" to """
            basic-hello = Oh Hai
            basic-bye = OKAY BYE THEN!
            basic-wtf = What The Fuck
        """.trimIndent(),
        "fr-FR" to """
            basic-hello = Bonjour
            basic-bye = Au Revoir
        """.trimIndent()
    )

    private val localization = Localization(
        locales = TestLocale.values(),
        fetch = { locale ->
            console.asDynamic().debug("fetching locale", locale)
            ftl[locale.id] ?: error("failed too get locale for id ${locale.id}")
        }
    )


    @Test
    fun shouldTranslate() = runTest("shouldTranslate") {

        val english = localization.loadBundleSequence(listOf(TestLocale.EN_GB))
        val french = localization.loadBundleSequence(listOf(TestLocale.FR_FR), TestLocale.EN_GB)

        // six ways to get a translation :-)
        assertSoftly {
            english.translate(bye) shouldBe "OKAY BYE THEN!"

            french.translate(bye) shouldBe "Au Revoir"
            french.translate("basic-wtf") shouldBe "What The Fuck"
            french.translate(nope) shouldBe nope.messageId
        }
    }
}