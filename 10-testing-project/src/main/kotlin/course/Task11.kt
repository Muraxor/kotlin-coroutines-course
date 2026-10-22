package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.11. Набор проверок. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.11") {
        runTest {
            suspend fun fakeSuccess(): Int { delay(50); return 42 }
            suspend fun fakeFailure(): Int { delay(50); error("сбой") }
            event("Заглушки успеха и ошибки готовы")
            val checks = mutableListOf<Boolean>()
            // TODO: Добавь пять проверок: успешный результат, ошибка, превышение
            // времени, ограничение одновременной работы и отмена.
            event("Проверки: $checks")
            checkResult("Пять успешных проверок", checks.size == 5 && checks.all { it })
        }
    }
}
