package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.02. Закрыть и перебрать. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.02") {
        val channel = Channel<Int>()
        launch { repeat(3) { channel.send(it) }; /* TODO: закрой канал */ }
        // TODO: замени на for и закрой канал
        repeat(3) { event("Получено ${channel.receive()}") }
    }
}
