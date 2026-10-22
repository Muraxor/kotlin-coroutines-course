package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.12. Поиск со сменой запроса и ошибкой. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.12") {
        val requests = flowOf("A", "B", "C")
            .onEach { delay(25); event("Запрос: $it") }
        val state = MutableStateFlow("Idle")
        val completed = mutableListOf<String>()
        val failed = mutableListOf<String>()
        suspend fun search(query: String): String {
            event("Поиск $query начат")
            delay(if (query == "B") 10 else 70)
            if (query == "B") error("поиск B недоступен")
            event("Поиск $query готов")
            return "Result $query"
        }
        event("Новый запрос прерывает старый; ошибка B локальна")
        val lateSeen = mutableListOf<String>()

        // TODO: При новом запросе прекращай прежний поиск.
        // Ошибку B покажи в состоянии, но продолжи принимать запросы.
        // Отмену старого поиска не считай ошибкой.
        // После завершения добавь в lateSeen текущее состояние
        // глазами нового подписчика.
        event("Конечное состояние: ${state.value}")
        event("Готовы: $completed, ошибки: $failed, поздний: $lateSeen")
        checkResult("Последний запрос", state.value == "Result C")
        checkResult("Отмена и ошибка различаются",
            completed == listOf("C") && failed == listOf("B"))
        checkResult("Новый подписчик видит итог", lateSeen == listOf("Result C"))
    }
}
