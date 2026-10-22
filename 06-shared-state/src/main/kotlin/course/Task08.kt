package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.08. Почему volatile не помогает. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.08") {
        val box=object { @Volatile var value=0 }
        // TODO: запусти 100 x 1000 increment на Default и сравни с 100000
        event("Начальное volatile: ${box.value}")
    }
}
