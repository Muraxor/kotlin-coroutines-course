package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.09. Два независимых чтения. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.09") {
        val inputs = listOf("left" to "a,b,c", "right" to "d,e")
        val activeReads = AtomicInteger()
        val peakReads = AtomicInteger()

        fun legacyRead(input: Pair<String, String>): String {
            val active = activeReads.incrementAndGet()
            peakReads.accumulateAndGet(active, ::maxOf)
            event("${input.first}: чтение начато, одновременно=$active")
            try {
                Thread.sleep(if (input.first == "left") 90 else 60)
                event("${input.first}: чтение завершено")
                return input.second
            } finally {
                activeReads.decrementAndGet()
            }
        }

        fun countParts(text: String): Int {
            event("Обработка $text")
            return text.split(',').size
        }

        event("Два независимых источника: $inputs")
        var result = emptyList<Pair<String, Int>>()
        // TODO: Начни обе загрузки до ожидания первого ответа.

        suspend fun process(input: Pair<String, String>): Pair<String, Int> {
            val value = withContext(Dispatchers.IO) {
                legacyRead(input)
            }
            val amount = withContext(Dispatchers.Default) {
                countParts(value)
            }

            return input.first to amount
        }

        result = coroutineScope {
            inputs.map {
                async { process(it) }
            }.awaitAll()
        }


        // Каждое блокирующее чтение и последующую обработку
        // выполни в подходящих контекстах.
        // Сохрани результаты в порядке входных данных.
        event("Результат: $result, пик чтений: ${peakReads.get()}")
        checkResult(
            "Оба ответа в исходном порядке",
            result == listOf("left" to 3, "right" to 2)
        )
        checkResult("Чтения перекрылись", peakReads.get() == 2)
    }
}
