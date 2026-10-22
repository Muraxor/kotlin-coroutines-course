package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.01. Текущее состояние. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.01") {
        val state = MutableStateFlow(0)
        // TODO: установи 1
         event("Состояние: ${state.value}")
    }
}
