import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise

actual fun runTest(name: String, block: suspend () -> Unit) {
    CoroutineScope(CoroutineName(name)).promise {
        console.log("test $name starting\n")
        block()
    }.then {
        console.log("test $name finished \n")
    }
}

fun <T> loadSuspendingThen(block: suspend () -> T, then: (T) -> Unit): dynamic {
    return CoroutineScope(CoroutineName("run-blocking")).promise {
//        console.log("test $name starting\n")
        block()
    }.then {
        then(it)
    }
}
