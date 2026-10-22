package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.08. Разделить холодный источник. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.08") {
        val source=flow { delay(30);emit(42) }
        // TODO: преврати source в StateFlow с initial=-1 и дождись значения 42
        event("Холодный источник готов: $source")
    }
}
