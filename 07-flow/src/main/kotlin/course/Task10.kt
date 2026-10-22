package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.10. Поисковые запросы. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.10") {
        val queries = flowOf("ko", "ko", "kot", "kotlin").onEach { delay(20); event("Ввод: $it") }
        event("Быстрый поток запросов готов")
        val completed = mutableListOf<String>()
        // TODO: Повторяющиеся подряд запросы пропускай. Новому запросу разреши
        // прервать обработку предыдущего; одна обработка занимает 60 мс.
        event("Завершённые поиски: $completed")
        checkResult("Последний запрос", completed == listOf("kotlin"))
    }
}
