package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.07. CPU-работа и ожидание. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.07") {
        fun cpuStep(seed: Int): Long {
            val deadline = System.nanoTime() + 50_000_000L
            var value = seed.toLong()
            while (System.nanoTime() < deadline) {
                value = (value * 1_664_525L + 1_013_904_223L) and 0x7fff_ffffL
            }
            return value
        }

        val ticks = AtomicInteger()
        val completedSteps = AtomicInteger()
        val ticker = launch {
            while (isActive) {
                delay(20)
                event("Тик ${ticks.incrementAndGet()}")
            }
        }
        var result = 0L

        withContext(Dispatchers.Main) {
            event(completedSteps.incrementAndGet())
            result = cpuStep(result.toInt())

            event(completedSteps.incrementAndGet())
            result = cpuStep(result.toInt())

            event(completedSteps.incrementAndGet())
            result = cpuStep(result.toInt())
        }

        ticker.cancelAndJoin()

        event("Итог вычисления: $result, тиков: ${ticks.get()}")
        checkResult("Выполнены три шага", completedSteps.get() == 3)
        checkResult("Вызывающий поток не был занят", ticks.get() > 0)
    }
}
