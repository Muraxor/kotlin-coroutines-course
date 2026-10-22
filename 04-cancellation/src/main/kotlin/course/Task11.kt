package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.11. Три исхода. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.11") {
        suspend fun work(ms: Long): String { delay(ms); event("Работа за $ms мс завершена"); return "успех" }
        event("Проверяем успех, таймаут и внешнюю отмену")
        // TODO: Сравни три случая для work(100): времени достаточно, времени не
        // хватает (30 мс), работа отменена извне.
        val outcomes = emptyList<String>()
        event("Исходы: $outcomes")
        checkResult("Три исхода", outcomes == listOf("успех", "таймаут", "отмена"))
    }
}
