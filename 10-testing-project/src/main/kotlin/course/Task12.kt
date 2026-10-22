package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.12. Итоговый сервис и виртуальный тест. См. TASKS.md. */
@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking {
    runExercise("10.12") {
        runTest {
            val ids = (1..100).toList()
            val active = AtomicInteger()
            val peak = AtomicInteger()
            suspend fun process(id: Int): Int {
                val count = active.incrementAndGet()
                peak.accumulateAndGet(count, ::maxOf)
                try {
                    delay(10)
                    if (id == 50) error("задание 50 повреждено")
                    return id * 2
                } finally {
                    active.decrementAndGet()
                }
            }
            event("100 заданий, буфер 10, три работника, ошибка у id=50")
            var results = emptyList<Pair<Int, Int>>()
            val progress = MutableStateFlow(0)
            val failures = java.util.concurrent.ConcurrentLinkedQueue<Int>()
            val startedAt = currentTime

            // TODO: Обработай все задания тремя работниками через очередь
            // ёмкостью 10. Сбой id=50 не должен прервать остальные.
            // Прогресс учитывает и успех, и ошибку; результатов должно быть 99.
            // Проверь поведение по виртуальному времени, не ожидая реально.

            val elapsed = currentTime - startedAt
            event("Результатов: ${results.size}, прогресс: ${progress.value}")
            event("Ошибки: $failures, пик: ${peak.get()}, время: $elapsed мс")
            checkResult("Все задания учтены",
                results.size == 99 &&
                    results.map { it.first }.toSet() == (ids - 50).toSet() &&
                    results.all { (id, value) -> value == id * 2 } &&
                    failures.toList() == listOf(50) && progress.value == 100)
            checkResult("Ограничение работников и завершение",
                peak.get() in 1..3 && active.get() == 0)
            checkResult("Прошло только виртуальное время", elapsed in 340L..1000L)
        }
    }
}
