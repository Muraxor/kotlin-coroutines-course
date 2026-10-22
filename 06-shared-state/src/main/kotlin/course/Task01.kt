package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.01. Найти гонку. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.01") {
        var counter = 0
        withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { counter++ } } } } }
        event("Счётчик: $counter; ожидается 100000")
    }
}
