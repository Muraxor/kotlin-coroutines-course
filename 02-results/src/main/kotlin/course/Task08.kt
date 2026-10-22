package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.08. Пустой и одиночный вход. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.08") {
        suspend fun squares(input: List<Int>): List<Int> {
            return input.map {
                async { it * it }
            }
                .awaitAll()
        }
        for (input in listOf(emptyList(), listOf(3), listOf(1, 2, 3))) event("$input -> ${squares(input)}")
    }
}
