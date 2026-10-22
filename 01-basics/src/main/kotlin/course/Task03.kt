package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.03. Два одновременных ожидания. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.03") {
        launch { event("A начало"); delay(200); event("A конец") }
        launch { event("B начало"); delay(300); event("B конец") }
        // TODO: создай две дочерние корутины
    }
}
