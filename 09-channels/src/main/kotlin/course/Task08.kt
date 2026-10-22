package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.08. Отменить producer и workers. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.08") {
        // TODO: producer и два workers
        val parent=launch { val queue=Channel<Int>(); queue.close() }
        delay(80);parent.cancelAndJoin();event("Родитель остановлен")
    }
}
