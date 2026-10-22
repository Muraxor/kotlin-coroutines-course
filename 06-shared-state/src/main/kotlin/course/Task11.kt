package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.11. Стресс-проверка склада. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.11") {
        var stock = 50; val mutex = Mutex()
        event("100 клиентов резервируют по 1 единице")
        // TODO: Реализуй reserve(): Boolean и запусти 100 одновременных попыток.
        // Число успешных броней не должно превысить остаток.
        val outcomes = emptyList<Boolean>()
        event("Успешных: ${outcomes.count { it }}, остаток: $stock")
        checkResult("Инвариант склада", outcomes.count { it } == 50 && stock == 0)
    }
}
