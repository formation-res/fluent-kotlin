package com.tryformation.localization

import io.kotest.matchers.shouldBe
import runTest
import kotlin.test.Test

class TranslationTest {
    @Test
    fun shouldTest() = runTest {
        val bs = bundleSequenceProvider.loadBundleSequence(listOf("nl-NL"),"en-US") { key->
            translations[key]
        }
        bs.translate(MyLangStrings.Bye).let { tl ->
            tl.noTranslationFound shouldBe false
            tl.bestTranslationUsed shouldBe true
            tl.fallbackLocaleUsed shouldBe false
        }
    }
}

enum class MyLangStrings: Translatable {
    Hello,
    HowAreYou,
    Bye;

    override val prefix="mylang"
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
        """.trimIndent(),
"en-FR" to """
            ${MyLangStrings.Hello.messageId}  = 'ello!
            ${MyLangStrings.Bye.messageId} =Hasta la Vista!
        """.trimIndent(),

)