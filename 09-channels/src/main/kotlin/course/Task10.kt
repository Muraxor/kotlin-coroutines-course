package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.10. Две стадии. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.10") {
        val input = (1..5).toList(); val output = mutableListOf<Int>()
        event("Первая стадия ×2, вторая +1; вход: $input")
        // TODO: Организуй две последовательные стадии обработки с очередью ёмкостью
        // 2 между ними. Готовые значения добавь в output.
        event("Выход: $output")
        checkResult("Две стадии", output == listOf(3, 5, 7, 9, 11))
    }
}
