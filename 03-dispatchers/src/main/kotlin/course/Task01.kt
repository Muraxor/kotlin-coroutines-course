package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.01. Увидеть поток. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.01") {
        event("До delay ${currentThread()}")
        delay(100)
        event("После delay ${currentThread()}")
    }
}
