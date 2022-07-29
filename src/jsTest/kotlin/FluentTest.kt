import com.tryformation.fluent.*
import io.kotest.matchers.shouldBe
import kotlin.js.json
import kotlin.test.Test

class FluentTest {
    private fun loadBundle(content: String): FluentBundle {
        val resource = FluentResource(content)
        val bundle = FluentBundle("en-US")
        val errors = bundle.addResource(resource)
        if (errors.isNotEmpty()) {
            // Syntax errors are per-message and don't break the whole resource
            errors.forEach {
                console.error(it)
            }
        }
        return bundle
    }
    private val mainBundle = loadBundle("""
        -brand-name = Foo 3000
        welcome = Welcome, {${'$'}name}, to {-brand-name}!
        helloWorld = Hello World
        """.trimIndent()
    )
    val fallbackBundle = loadBundle("""
        -brand-name = Foo 3000
        welcome = Welcome, {${'$'}name}, to {-brand-name}!
        helloWorld = Hello World
        foo = bar
        test = {${'$'}arg}
    """.trimIndent()
    )

    private val FSI = "\u2068" // FIRST-STRONG ISOLATE
    private val PDI = "\u2069" // POP DIRECTIONAL ISOLATE

    val bundles = arrayOf(
        mainBundle,
        fallbackBundle,
    )

    @Test
    fun testHelloWorld() {
        val bundle = mainBundle
        val welcome = bundle.translate("helloWorld", json())
        welcome shouldBe "Hello World"
    }

    @Test
    fun testWelcome() {
        val bundle = mainBundle
        val welcome = bundle.translate("welcome", json("name" to "Anna"))
        welcome shouldBe "Welcome, ${FSI}Anna${PDI}, to ${FSI}Foo 3000${PDI}!"
    }

    @Test
    fun testFallback() {
        val welcome = bundles.translate("welcome", json("name" to "Anna"))
        welcome shouldBe "Welcome, ${FSI}Anna${PDI}, to ${FSI}Foo 3000${PDI}!"
        val foo = bundles.translate("foo", null)
        foo shouldBe "bar"
    }
}