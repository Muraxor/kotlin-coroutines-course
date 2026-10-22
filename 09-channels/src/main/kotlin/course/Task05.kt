package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.05. Два работника. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.05") {
        val jobs=Channel<Int>()
        // TODO: запусти двух workers и producer для id 0..5, затем закрой jobs
        event("Канал создан: $jobs")
    }
}
