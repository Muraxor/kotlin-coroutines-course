package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.12. Отчёт из четырёх источников. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.12") {
        val sources = listOf(
            ReportSource("users", 10, 40),
            ReportSource("orders", 20, 80),
            ReportSource("errors", 1, 60),
            ReportSource("income", 300, 100),
        )
        event("Собираем отчёт из ${sources.size} источников")

        val report = coroutineScope {
            // TODO: Начни чтение всех четырёх источников одновременно.
            val readSources = sources.map {
                async { readSource(it) }
            }.awaitAll()
                .toMap()
            // TODO: Когда все ответы получены, собери из них словарь «имя источника
            // — значение».
            //emptyMap<String, Int>()
            readSources
        }

        event("Итоговый отчёт: $report")
        checkResult("Все четыре источника получены", report == mapOf("users" to 10, "orders" to 20, "errors" to 1, "income" to 300))
    }
}

private data class ReportSource(val name: String, val value: Int, val waitMs: Long)

private suspend fun readSource(source: ReportSource): Pair<String, Int> {
    event("${source.name}: запрос начат")
    delay(source.waitMs)
    event("${source.name}: получено ${source.value}")
    return source.name to source.value
}
