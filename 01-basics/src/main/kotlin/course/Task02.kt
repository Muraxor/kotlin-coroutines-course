package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.02. Родитель продолжает работу. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.02") {
        // TODO: оберни следующие три строки в launch
        launch {
            event("Ребёнок начал")
            delay(200)
            event("Ребёнок закончил")
        }

        event("Родитель продолжает")
    }
}
