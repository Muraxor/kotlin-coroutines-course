package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.03. Мягкий таймаут. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.03") {
        val result: String? = run { delay(500); "ready" } // TODO: ограничь 100 мс
         event("Результат: $result")
    }
}
