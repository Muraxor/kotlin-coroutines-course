package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.02. Вычисление на Default. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.02") {
        val sum =  withContext(Dispatchers.Default) {
            (1..1000).sum()
        }
        event("Сумма: $sum")
    }
}
