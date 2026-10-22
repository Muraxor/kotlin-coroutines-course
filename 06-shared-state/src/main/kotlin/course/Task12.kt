package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.12. Бронирование мест. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.12") {
        val capacity = 4
        var available = capacity
        val requests = listOf("A" to 2, "B" to 2, "C" to 1,
            "D" to 1, "E" to 3)
        val mutex = Mutex()
        event("Свободно $capacity места; заявки: $requests")
        var sales = emptyList<Pair<String, Int>>()

        // TODO: Обработай все заявки одновременно.
        // Проверка остатка и списание мест должны быть неделимыми.
        // Для каждой заявки сохрани пару: имя и купленное число мест.
        // При отказе купленное число равно нулю.
        sales.forEach { (name, count) ->
            event("$name: ${if (count == 0) "отказ" else "куплено $count"}")
        }
        val sold = sales.sumOf { it.second }
        event("Продано: $sold, осталось: $available")
        checkResult("Каждая заявка обработана",
            sales.map { it.first } == requests.map { it.first })
        checkResult("Ни одного лишнего места",
            sold == capacity && available == 0 &&
                sales.all { (name, count) ->
                    count == 0 || count == requests.first { it.first == name }.second
                })
    }
}
