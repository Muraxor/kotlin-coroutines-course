package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.03. Исправить зависание. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.03") {
        val channel = Channel<Int>()
        // TODO: найди строку, которая завершает цикл
        launch { repeat(3) { channel.send(it) }; channel.close() }
        for (item in channel) event("$item")
    }
}
