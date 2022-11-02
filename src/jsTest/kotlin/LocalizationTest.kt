import BasicStrings.*
import com.tryformation.fluent.*
import com.tryformation.localization.LocalizationService
import com.tryformation.localization.Translatable
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJSDate
import kotlin.js.json
import kotlin.test.Test

enum class BasicStrings : Translatable {
    hello,
    bye,
    nope,
    wtf,
    time,
    datetime
    ;

    override val prefix: String = "pre"
}

class LocalizationTest {
    val ftl = mapOf(
        "en-GB" to """
            pre-hello = Oh Hai
            pre-bye = OKAY BYE THEN!
            pre-wtf = What The Fuck
            pre-datetime = en-GB: { ${'$'}date }
            pre-time = Today is { DATETIME(
                ${'$'}date, 
                timezone: "${'$'}timezone", 
                month: "long",
                year: "numeric",
                day: "numeric",
                hour: "numeric",
                minute: "numeric",
                second: "numeric",
            ) }
            pre-other = Other stuff?
        """.trimIndent(),
        "de-DE" to """
            pre-datetime = de-DE: { ${'$'}date }
        """.trimIndent(),
        "en-US" to """
            pre-datetime = en-US: { ${'$'}date }
        """.trimIndent(),
        "fr-FR" to """
            pre-hello = Bonjour
            pre-bye = Au Revoir
            pre-datetime = fr-FR: { ${'$'}date }
            pre-time = Today is { DATETIME(
                ${'$'}date, 
                timezone: "${'$'}timezone", 
                month: "long",
                year: "numeric",
                day: "numeric",
                hour: "numeric",
                minute: "numeric",
                second: "numeric",
            ) }
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
        val french =
            LocalizationService.loadBundleSequence(listOf(TestLocales.FR_FR.id), TestLocales.EN_GB.id, fetch = fetch)

        // six ways to get a translation :-)
        assertSoftly {
            english.translate(bye) shouldBe "OKAY BYE THEN!"

            french.translate(bye) shouldBe "Au Revoir"
            french.translate(wtf.messageId) shouldBe "What The Fuck"
            french.translate(nope) shouldBe nope.name
        }
    }

    @Test
    fun shouldFormatDatetime() = loadSuspendingThen({
        LocalizationService.loadBundleSequence(listOf(TestLocales.EN_GB.id), fetch = fetch)
    }) { english ->
        js("""require("@js-joda/timezone")""")

        val datetime = Clock.System.now()
        val timezone = TimeZone.availableZoneIds.random()

        println("\n\n")
        println(datetime.toString() + "\n")
        println(timezone + "\n")

        val formatted = english.translate(
            time,
            args = json(
                "date" to datetime.toJSDate(),
                "timezone" to timezone
            )
        )
        println("\n\n\n")
        println(formatted + "\n")
        formatted shouldNotBe "time"



val formatted2 = english.translate(
    time,
    args = json(
        "date" to datetime.toJSDate().format(
            DateTimeFormatOption.Timezone(TimeZone.availableZoneIds.random()),
            DateTimeFormatOption.Weekday.long,
            DateTimeFormatOption.Year.two_digit,
            DateTimeFormatOption.Hour.two_digit
        ),
        "timezone" to timezone,
    )
)
        println("\n\n\n")
        println(formatted2 + "\n")
        formatted2 shouldNotBe "time"
    }
    @Test
    fun translateVariousDatetimeFormats() = loadSuspendingThen({
        listOf(
            LocalizationService.loadBundleSequence(listOf(TestLocales.EN_GB.id), fetch = fetch),
            LocalizationService.loadBundleSequence(listOf(TestLocales.EN_US.id), fetch = fetch),
            LocalizationService.loadBundleSequence(listOf(TestLocales.DE_DE.id), fetch = fetch),
            LocalizationService.loadBundleSequence(listOf(TestLocales.FR_FR.id), fetch = fetch),
        )
    }) { languages ->
        js("""require("@js-joda/timezone")""")

        val instant = Clock.System.now()
//        val timezone = TimeZone.availableZoneIds.random()

        println("\n\n")
        println(instant.toString() + "\n")
//        println(timezone + "\n")

        val date = instant.toJSDate()

        val formats = listOf(
            setOf(
                DateTimeFormatOption.DateStyle.full,
                DateTimeFormatOption.TimeStyle.full,
            ),
            setOf(
                DateTimeFormatOption.DateStyle.long,
                DateTimeFormatOption.TimeStyle.long,
            ),
            setOf(
                DateTimeFormatOption.DateStyle.medium,
                DateTimeFormatOption.TimeStyle.medium,
            ),
            setOf(
                DateTimeFormatOption.DateStyle.short,
                DateTimeFormatOption.TimeStyle.short,
            ),
            setOf(
                DateTimeFormatOption.DateStyle.short,
            ),
            setOf(
                DateTimeFormatOption.TimeStyle.short,
            ),
            setOf(
                DateTimeFormatOption.Month.short,
                DateTimeFormatOption.Day.numeric,
                DateTimeFormatOption.Year.numeric
            ),
            setOf(
                DateTimeFormatOption.Hour.numeric,
                DateTimeFormatOption.Minute.numeric,
                DateTimeFormatOption.Second.numeric
            ),
            setOf(
                DateTimeFormatOption.Hour.two_digit,
                DateTimeFormatOption.Minute.two_digit,
                DateTimeFormatOption.Second.two_digit
            ),
        ).forEach { formatOptions ->
            println("$formatOptions => \n")
            languages.forEach { bundle ->
                val formatted = bundle.translate(
                    datetime,
                    args = json(
                        "date" to instant.toJSDate().format(*formatOptions.toTypedArray()),
                    )
                )
                println("$formatted\n")
                formatted shouldNotBe datetime.name
            }
            println("\n\n")
        }
    }
}