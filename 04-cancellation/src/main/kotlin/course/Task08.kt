package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.08. Приостанавливающая очистка. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.08") {
        // TODO: suspend-очистка
        val job=launch { try { delay(500) } finally { event("Начало очистки"); } }
        delay(30);job.cancelAndJoin();event("Остановка завершена")
    }
}
