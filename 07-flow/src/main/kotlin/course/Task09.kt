package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.09. Прогресс загрузки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.09") {
        val milestones = listOf(0, 25, 50, 75, 100)
        event("Контрольные точки прогресса: $milestones")
        val received = mutableListOf<Int>()
        // TODO: Передавай milestones по одному с задержкой между событиями.
        // Записывай полученные значения в received.
        event("Получен прогресс: $received")
        checkResult("Все точки получены", received == milestones)
    }
}
