package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.07. Не больше трёх операций. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.07") {
        val semaphore=Semaphore(3);val active=AtomicInteger()
        // TODO: запусти 10 операций по 60 мс и измерь пик active
        event("Разрешений: ${semaphore.availablePermits}")
    }
}
