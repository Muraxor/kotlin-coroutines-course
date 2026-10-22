package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.06. Тест очистки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.06") {
        runTest {var clean=false;val job=launch {try {delay(500)}finally{clean=true}}
            // TODO: запусти ребёнка, отмени с ожиданием и проверь clean
            event("Очистка: $clean");job.cancelAndJoin()
        }
    }
}
