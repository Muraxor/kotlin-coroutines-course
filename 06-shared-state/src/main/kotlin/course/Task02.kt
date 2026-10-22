package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.02. Атомарный счётчик. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.02") {
        val counter = AtomicInteger()
        // TODO: атомарно увеличь
        withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { } } } } }
        event("Счётчик: ${counter.get()}"); checkResult("100000",counter.get()==100000)
    }
}
