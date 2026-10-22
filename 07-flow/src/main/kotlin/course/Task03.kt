package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.03. Отобрать чётные. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.03") {
        // TODO: filter для чётных
        (1..6).asFlow() .collect { event("Чётное $it") }
    }
}
