package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.05. Результат каждого источника. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.05") {
        val sources=listOf(10,20,30)
        // TODO: запусти три источника и представь каждый успех или ошибку как
        // Result<Int>
        event("Источники: $sources")
    }
}
