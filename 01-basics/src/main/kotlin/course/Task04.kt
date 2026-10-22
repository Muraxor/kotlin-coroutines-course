package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.04. Дождаться конкретной задачи. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.04") {
        val job = launch { delay(150); event("Работа готова") }
        job.join()
        event("Можно использовать результат работы")
    }
}
