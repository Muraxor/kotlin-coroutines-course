package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.08. Функция не оставляет фоновой работы. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.08") {
        suspend fun runBatch() = coroutineScope {
            // TODO: создай структурированную область и трёх детей
            repeat(3) {
                launch {
                    event("start $it")
                    delay(200)
                    event("end $it")
                }
            }
        }
        runBatch()
        event("runBatch вернулась")
    }
}
