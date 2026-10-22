package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.06. cancel против cancelAndJoin. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.06") {
        val job=launch { try { delay(500) } finally { event("finally") } }
        delay(30);job.cancel();event("После cancel")
        // TODO: дождись job и отметь событие после join
    }
}
