package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.12. Конвейер файлов с ограничением нагрузки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.12") {
        val names = listOf("a", "bb", "ccc", "dddd", "eeeee", "ffffff")
        val activeReads = AtomicInteger()
        val peakReads = AtomicInteger()
        val finishedReads = AtomicInteger()
        val finishedAnalyses = AtomicInteger()
        val activeAnalyses = AtomicInteger()
        val peakAnalyses = AtomicInteger()
        val overlappedStages = AtomicInteger()
        val callerTicks = AtomicInteger()

        fun legacyRead(name: String): String {
            val active = activeReads.incrementAndGet()
            peakReads.accumulateAndGet(active, ::maxOf)
            event("Чтение $name: старт, одновременно=$active")
            try {
                Thread.sleep(110L - name.length * 10L)
                event("Чтение $name: готово")
                return "content:$name"
            } finally {
                activeReads.decrementAndGet()
                finishedReads.incrementAndGet()
            }
        }

        fun analyze(name: String, content: String): Pair<String, Int> {
            val active = activeAnalyses.incrementAndGet()
            peakAnalyses.accumulateAndGet(active, ::maxOf)
            if (activeReads.get() > 0) overlappedStages.incrementAndGet()
            event("Обработка $name: старт, одновременно=$active")
            try {
                Thread.sleep(25)
                event("Обработка $name: длина=${content.length}")
                return name to content.length
            } finally {
                activeAnalyses.decrementAndGet()
                finishedAnalyses.incrementAndGet()
            }
        }

        event("Файлы: $names")
        val ticker = launch {
            while (isActive) {
                delay(15)
                if (finishedAnalyses.get() < names.size) {
                    callerTicks.incrementAndGet()
                    event("Вызывающий поток свободен")
                }
            }
        }

        var result = emptyList<Pair<String, Int>>()
        // TODO: Получи результаты для всех файлов одновременно.
        // Блокирующее чтение выполняй вне вызывающего потока,
        // но одновременно читай не более двух файлов.
        // Анализ выполняй в вычислительном контексте по одному файлу.
        // Не жди всех чтений перед началом анализа готовых файлов.
        // Сохрани исходный порядок имён в result.
        ticker.cancelAndJoin()

        event("Результат: $result")
        event("Пики: чтение=${peakReads.get()}, анализ=${peakAnalyses.get()}")
        event("Перекрытий стадий: ${overlappedStages.get()}")
        event("Тиков вызывающего потока: ${callerTicks.get()}")
        checkResult("Порядок и размеры", result == names.map { it to it.length + 8 })
        checkResult("Все стадии завершены",
            finishedReads.get() == names.size &&
                finishedAnalyses.get() == names.size &&
                activeReads.get() == 0 && activeAnalyses.get() == 0)
        checkResult("Чтение и анализ ограничены",
            peakReads.get() == 2 && peakAnalyses.get() == 1)
        checkResult("Стадии перекрылись, вызывающий поток свободен",
            overlappedStages.get() > 0 && callerTicks.get() > 0)
    }
}
