package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.09. Минимальная цена. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.09") {
        val prices = listOf(120, 90, 110)
        val waitsMs = listOf(50L, 100L, 150L)
        event("Запрашиваем цены у ${prices.size} магазинов")
        val min = prices.mapIndexed { index, price ->
            async { fetchPrice(price, waitsMs[index]) }
        }.awaitAll()
            .min()

        event("Лучшая цена: $min")
        checkResult("Минимум 90", min == 90)
    }
}

suspend fun fetchPrice(price: Int, waitMs: Long): Int {
    event("Магазин с ценой $price: запрос начат")
    delay(waitMs)
    event("Магазин с ценой $price: ответ получен")
    return price
}
