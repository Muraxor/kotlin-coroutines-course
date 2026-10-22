package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.09. Поиск с таймаутом. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.09") {
        suspend fun lookup(): String { event("Поиск начат"); delay(150); event("Поиск завершён"); return "найдено" }
        var short: String? = "не проверено"; var long: String? = "не проверено"
        // TODO: Ограничь ожидание lookup() сначала 100 мс, затем 300 мс. При
        // превышении лимита получи null.
        event("Лимит 100 мс: $short; лимит 300 мс: $long")
        checkResult("Таймаут и успех", short == null && long == "найдено")
    }
}
