package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.11. Измерить две стратегии. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.11") {
        val input = listOf(1, 2, 3)
        event("Одинаковый вход для двух способов: $input")

        var sequential = emptyList<Int>()
        val sequentialMs = measureTimeMillis {
            // TODO: Для чисел 1, 2, 3 получи результаты по очереди, дожидаясь
            // каждого перед следующим.
            sequential = input.map {
                loadNumber(it)
            }
        }

        var concurrent = emptyList<Int>()
        val concurrentMs = measureTimeMillis {
            // TODO: Для тех же чисел начни все три загрузки одновременно и собери
            // результаты в исходном порядке.
            concurrent = input.map {
                async { loadNumber(it) }
            }.awaitAll()
        }

        event("Последовательно: $sequential за $sequentialMs мс")
        event("Конкурентно: $concurrent за $concurrentMs мс")
        checkResult("Оба результата [2, 4, 6]", sequential == listOf(2, 4, 6) && concurrent == sequential)
    }
}

private suspend fun loadNumber(number: Int): Int {
    event("Число $number: старт")
    delay(80)
    val result = number * 2
    event("Число $number: готово $result")
    return result
}
