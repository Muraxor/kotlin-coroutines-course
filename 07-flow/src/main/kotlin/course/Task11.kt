package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.11. Отмена коллектора. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.11") {
        var cleaned = false
        val source = flow { try { var n = 0; while (true) { emit(n++); delay(20) } } finally { cleaned = true; event("Источник закрыт") } }
        event("Источник готов к сбору")
        // TODO: Начни получать события потока. Примерно через 70 мс останови
        // получение и дождись завершения обработки.
        event("Очистка источника: $cleaned")
        checkResult("Сбор отменён", cleaned)
    }
}
