package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.09. Отказ обязательного источника. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.09") {
        suspend fun profile(): String { delay(30); error("профиль недоступен") }
        suspend fun orders(): Int { try { delay(500); return 3 } finally { event("Запрос заказов завершён") } }
        event("Профиль обязателен, заказы должны отмениться при его отказе")
        // TODO: Начни две дочерние операции в общей группе. Ошибка одной должна
        // прервать всю группу; обработай её снаружи.
        var failed = false
        event("Экран не загружен: $failed")
        checkResult("Отказ обязательного источника", failed)
    }
}
