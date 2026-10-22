package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.04. Завершить сбор StateFlow. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.04") {
        val state = MutableStateFlow(0)
        val job = launch(start=CoroutineStart.UNDISPATCHED) { state.take(1).collect { event("Получено $it") } }
        state.value=1; job.join() // TODO: добейся получения и 0, и 1
    }
}
