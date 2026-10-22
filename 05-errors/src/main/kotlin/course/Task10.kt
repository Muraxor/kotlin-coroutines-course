package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.10. Частичные данные. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.10") {
        val ids = listOf("A", "B", "C")
        suspend fun loadCard(id: String): String { delay(30); if (id == "B") error("Карточка B недоступна"); return "card$id" }
        event("Карточки: $ids")
        var result = emptyList<String>()
        // TODO: Запусти A, B и C одновременно так, чтобы ошибка B не прервала A и
        // C. Обработай сбой B отдельно и сохрани успешные результаты.
        event("Результат: $result")
        checkResult("Частичные данные", result == listOf("cardA", "errorB", "cardC"))
    }
}
