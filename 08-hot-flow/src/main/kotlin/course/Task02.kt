package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.02. Атомарное обновление. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.02") {
        val state = MutableStateFlow(0)
        // TODO: обнови атомарно
         event("Состояние: ${state.value}")
    }
}
