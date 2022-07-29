import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking

actual fun runTest(name: String, block: suspend () -> Unit) {
    runBlocking(CoroutineName(name)) {
        block()
    }
}