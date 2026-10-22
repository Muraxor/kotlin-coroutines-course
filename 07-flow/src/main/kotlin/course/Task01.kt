package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.01. Три значения. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.01") {
        flow<Int> { /* TODO: отправь 1, 2, 3 */ }.collect { event("Значение $it") }
    }
}
