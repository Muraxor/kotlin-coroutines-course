package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.01. Передать одно значение. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.01") {
        val channel = Channel<Int>()
        launch { /* TODO: отправь 42 */ channel.close() }
        // TODO: получи и выведи значение
    }
}
