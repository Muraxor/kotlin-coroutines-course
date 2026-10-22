package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.05. Равные значения. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.05") {
        val state=MutableStateFlow(0)
        // TODO: подпишись, отправь 0 и 1, заверши collect через take(2)
        event("Текущее: ${state.value}")
    }
}
