package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.05. Имя корутины. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.05") {
        launch {
            event("Имя: ${currentCoroutineContext()[CoroutineName]?.name}")
            // TODO: задай имя loader, добавь ребёнка parser
        }.join()
    }
}
