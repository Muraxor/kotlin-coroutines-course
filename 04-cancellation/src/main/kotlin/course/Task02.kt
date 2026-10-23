package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.02. Очистка при отмене. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.02") {
        val job = launch {
            try {
                repeat(20) { delay(100) }
            } finally {
                event("Очистка")
            }
        }
        delay(50);
        job.cancelAndJoin();
        event("Остановка завершена")
    }
}
