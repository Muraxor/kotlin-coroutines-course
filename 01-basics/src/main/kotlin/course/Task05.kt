package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.05. Повторное использование suspend-функции. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.05") {
        suspend fun work(name: String) {
            event("$name начало")
            // TODO: пауза и сообщение об окончании
            delay(200)
            event("$name конец")
        }
        coroutineScope { launch { work("A") }; launch { work("B") } }
    }
}
