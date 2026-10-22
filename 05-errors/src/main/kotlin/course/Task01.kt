package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.01. Ошибка области. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.01") {
        // TODO: обработай ошибку всего scope снаружи
        coroutineScope { launch { delay(30); error("сбой") } }
        event("После scope")
    }
}
