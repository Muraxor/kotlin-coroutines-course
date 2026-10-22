package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.04. Не проглотить отмену. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.04") {
        val job = launch {
            try { delay(500) }
            // TODO: не проглатывай отмену
            catch (e: Exception) { event("Поймано: $e") }
        }
        delay(50); job.cancelAndJoin(); event("Остановка")
    }
}
