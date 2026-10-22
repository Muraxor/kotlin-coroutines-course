package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.04. Выбрать builder для результата. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.04") {
        val result = async { delay(100); 42 }
        event("${result.await()}")
        // TODO: получи именно вычисленное число
    }
}
