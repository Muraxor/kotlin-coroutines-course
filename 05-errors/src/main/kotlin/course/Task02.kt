package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.02. Сосед отменён. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.02") {
        try { coroutineScope {
            launch { delay(30); error("сбой") }
            launch { delay(500); event("Сосед дошёл") } // TODO: покажи finally
        } } catch (_: IllegalStateException) { event("Родитель получил ошибку") }
    }
}
