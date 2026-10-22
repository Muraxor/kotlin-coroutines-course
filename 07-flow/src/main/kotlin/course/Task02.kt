package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.02. Преобразовать значения. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.02") {
        flowOf(1,2,3) /* TODO: добавь map */ .collect { event("Значение $it") }
    }
}
