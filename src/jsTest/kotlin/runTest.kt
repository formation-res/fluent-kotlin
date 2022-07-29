import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise

actual fun runTest(name: String, block: suspend () -> Unit): dynamic {
    return CoroutineScope(CoroutineName(name)).promise {
        console.log("test $name starting\n")
        block()
    }.then {
        console.log("test $name finished \n")
    }
}
