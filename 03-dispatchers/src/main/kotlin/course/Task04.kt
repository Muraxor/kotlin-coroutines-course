package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.04. Вернуть вычисленный результат. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.04") {
        suspend fun calculate(): Int {
            CoroutineScope(Dispatchers.Default).launch { (1..1000).sum() }
            return -1 // TODO: исправь всю функцию
        }
        event("Ответ: ${calculate()}")
    }
}
