package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.05. Таймаут без ожидания. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.05") {
        runTest { val value=withTimeoutOrNull(100) { delay(500);42 }
            // TODO: выведи value и currentTime, проверь null
            event("Значение: $value")
        }
    }
}
