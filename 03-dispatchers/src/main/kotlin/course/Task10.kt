package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.10. Собственный пул потоков. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.10") {
        val inputs = (1..6).toList()
        val active = AtomicInteger()
        val peak = AtomicInteger()
        val createdThreads = AtomicInteger()
        val usedThreads = java.util.Collections.synchronizedSet(mutableSetOf<String>())

        fun blockingWork(number: Int): Int {
            val now = active.incrementAndGet()
            peak.accumulateAndGet(now, ::maxOf)
            usedThreads += Thread.currentThread().name
            event("Работа $number: поток=${Thread.currentThread().name}, активно=$now")
            try {
                Thread.sleep(35L + (7 - number) * 5L)
                return number * number
            } finally {
                active.decrementAndGet()
            }
        }

        var results = emptyList<Int>()
        var executorWasClosed = false

        val pool = Executors.newFixedThreadPool(2) { runnable ->
            Thread(runnable, "course-worker-${createdThreads.incrementAndGet()}")
        }

        val dispatcher = pool.asCoroutineDispatcher()

        dispatcher.use {
            results = inputs.map {
                async { blockingWork(it) }
            }.awaitAll()
        }

        executorWasClosed = pool.isShutdown

        // course-worker-1 и course-worker-2. Используй его как контекст
        // выполнения, начни обработку всех inputs до ожидания результатов,
        // сохрани исходный порядок и обязательно освободи ресурс.
        // После закрытия сохрани состояние executor.isShutdown
        // в executorWasClosed.

        event("Результаты: $results")
        event("Потоки: $usedThreads, пик: ${peak.get()}")
        checkResult("Квадраты в исходном порядке",
            results == listOf(1, 4, 9, 16, 25, 36))
        checkResult("Работали ровно два собственных потока",
            peak.get() == 2 && usedThreads.size == 2 &&
                usedThreads.all { it.startsWith("course-worker-") })
        checkResult("Пул потоков закрыт",
            executorWasClosed && createdThreads.get() == 2)
    }
}
