package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.03. Mutex для счётчика. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.03") {
        var counter = 0; val mutex = Mutex()
        // TODO: защити критическую секцию
        withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { counter++ } } } } }
        event("Счётчик: $counter"); checkResult("100000",counter==100000)
    }
}
