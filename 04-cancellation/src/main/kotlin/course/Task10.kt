package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.10. Отменяемая обработка. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.10") {
        val processed = AtomicInteger()
        event("Большой вычислительный цикл начат")
        // TODO: Начни длительный вычислительный цикл на вычислительном диспетчере.
        // Он должен замечать отмену и прекращать обработку.
        // Через 30 мс отмени вычисление, дождись его завершения и выведи processed.
        var stopped = false
        event("Обработано: ${processed.get()}, остановлено: $stopped")
        checkResult("Цикл остановлен", stopped)
    }
}
