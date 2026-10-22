package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.01. Остановить тики. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.01") {
        val job = launch { repeat(20) { delay(100); event("Тик $it") } }
        delay(250)
        // TODO: отмени job и дождись его завершения
        event("Остановлено")
    }
}
