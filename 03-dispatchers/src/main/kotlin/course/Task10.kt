package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.10. Сервис с двумя диспетчерами. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.10") {
        val inputs = listOf(21, 13, 7)
        val io = Dispatchers.IO
        val cpu = Dispatchers.Default
        val readContexts = java.util.Collections.synchronizedList(
            mutableListOf<CoroutineDispatcher>())
        val computeContexts = java.util.Collections.synchronizedList(
            mutableListOf<CoroutineDispatcher>())
        suspend fun legacyRead(number: Int): Int {
            val context = currentCoroutineContext()[ContinuationInterceptor]
            readContexts += context as CoroutineDispatcher
            event("Чтение $number на ${Thread.currentThread().name}")
            Thread.sleep(50)
            return number
        }
        suspend fun double(number: Int): Int {
            val context = currentCoroutineContext()[ContinuationInterceptor]
            computeContexts += context as CoroutineDispatcher
            event("Вычисление $number × 2 на ${Thread.currentThread().name}")
            return number * 2
        }

        var answers = emptyList<Int>()
        // TODO: Создай сервис с внедрёнными контекстами io и cpu.
        // Его метод обрабатывает одно число: сначала читает, затем удваивает.
        // Обработай все три числа одновременно и сохрани исходный порядок.
        event("Ответы сервиса: $answers")
        checkResult("Все ответы", answers == listOf(42, 26, 14))
        checkResult("Стадии в заданных контекстах",
            readContexts.size == inputs.size && readContexts.all { it === io } &&
                computeContexts.size == inputs.size &&
                computeContexts.all { it === cpu })
    }
}
