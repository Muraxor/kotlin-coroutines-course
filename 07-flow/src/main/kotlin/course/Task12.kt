package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.12. Два потребителя телеметрии. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.12") {
        val sourceRuns = AtomicInteger()
        val total = AtomicInteger()
        val source = flow {
            val run = sourceRuns.incrementAndGet()
            event("Датчик: подписка $run")
            for (value in listOf(1, -3, 2, 2, 5)) {
                delay(15)
                event("Датчик $run: $value")
                emit(value)
            }
            error("датчик отключился")
        }
        event("Два потребителя начинают сбор телеметрии")
        var values = emptyList<List<Int>>()

        // TODO: Два потребителя должны начать работу одновременно.
        // Каждый пропускает отрицательные и повторные подряд значения,
        // удваивает остальные и после сбоя добавляет -1.
        // Сохрани списки обоих потребителей и безопасно посчитай общую сумму.
        event("Измерения: $values, общая сумма: ${total.get()}")
        checkResult("Два независимых сбора",
            sourceRuns.get() == 2 &&
                values == listOf(listOf(2, 4, 10, -1), listOf(2, 4, 10, -1)))
        checkResult("Общая сумма без потерь", total.get() == 30)
    }
}
