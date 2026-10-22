package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.random.Random
import kotlin.random.nextLong
import kotlin.system.measureTimeMillis

/** 02.07. Порядок списка. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.07") {
        val input = (1..5).toList()
        // TODO: создай async на элемент и собери результаты

        val result = input.map {
            async {
                val delayTime = Random(100).nextLong(100,1000)
                delay(delayTime)
                it*it
            }
        }.awaitAll()
        event("Вход: $result")
    }
}
