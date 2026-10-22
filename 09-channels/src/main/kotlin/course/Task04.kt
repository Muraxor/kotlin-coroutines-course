package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.04. Ограниченный буфер. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.04") {
        val channel=Channel<Int>(2) // TODO: эксперимент с ёмкостью
        launch { repeat(5) { event("Посылаю $it"); channel.send(it); event("Послано $it") }; channel.close() }
        for (item in channel) { delay(80); event("Получено $item") }
    }
}
