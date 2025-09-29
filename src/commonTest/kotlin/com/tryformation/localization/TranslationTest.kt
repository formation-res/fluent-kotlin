package com.tryformation.localization

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.startWith
import kotlin.test.Test
import runTest

class TranslationTest {
    @Test
    fun shouldTest() = runTest {
        val bs = bundleSequenceProvider.loadBundleSequence(listOf("nl-NL"), "en-US") { key ->
            translations[key]
        }
        bs.format(MyLangStrings.Bye).let { tl ->
            tl.noTranslationFound shouldBe false
            tl.bestTranslationUsed shouldBe true
            tl.fallbackLocaleUsed shouldBe false
        }
    }

    @Test
    fun shouldSubstituteVariablesCorrectly() = runTest {
        val bs = bundleSequenceProvider.loadBundleSequence(listOf("nl-NL"), "en-US") { key ->
            translations[key]
        }

        val name = "Duderowski"
        val link = "https://tryformation.com"
        bs.format(
            MyLangStrings.LongerTextWithVars, args = mapOf(
                "name" to name,
                "link" to link
            )
        ).let { formatted ->
            println(formatted)
            formatted.message.lines().size shouldBe 5
            formatted.message shouldContain name
            formatted.message shouldContain link
            formatted.message.lines().first() shouldNot (startWith(" "))
        }
    }

    @Test
    fun shouldOverrideDefaultTranslation() = runTest {
        val bs = bundleSequenceProvider
            .loadBundleSequence(listOf("nl-NL"), "en-US") { key ->
                translations[key]
            }
        bs.format(MyLangStrings.TheMeaningOfLife).message shouldBe MyLangStrings.TheMeaningOfLife.localizedDefaultTranslation
        bs.format(MyLangStrings.TowelsAreUseful).message shouldContain "massively useful"
    }
}

enum class MyLangStrings(
    override val localizedDefaultTranslation: String? = null
) : TranslatableWithDefault {
    Hello,
    HowAreYou,
    Bye,
    LongerTextWithVars,
    TheMeaningOfLife("The meaning of life is 42."),
    TowelsAreUseful("Towels are useful"),

    ;

    override val prefix = "mylang"
}

expect val bundleSequenceProvider: LocalizedTranslationBundleSequenceProvider
val translations = mapOf(
    "nl-NL" to """
            ${MyLangStrings.Hello.messageId}= Haaaaaai!
            ${MyLangStrings.Bye.messageId} = Tot Ziens. Doei!
        """.trimIndent(),
    "en-US" to """
            ${MyLangStrings.Hello.messageId}= OhHai!
            ${MyLangStrings.Bye.messageId}= Ok Bye!
            ${MyLangStrings.HowAreYou.messageId}=Wazzzzaaaaaa!
            ${MyLangStrings.LongerTextWithVars.messageId} = 
                Hello { ${'$'}name },
                
                Click this link: {${'$'}link}
                
                And bye
            ${MyLangStrings.TowelsAreUseful.messageId} = A towel, it says, is about the most massively useful thing an interstellar hitchhiker can have.
        """.trimIndent(),
    "en-FR" to """
            ${MyLangStrings.Hello.messageId}  = 'ello!
            ${MyLangStrings.Bye.messageId} =Hasta la Vista!
        """.trimIndent(),

    )