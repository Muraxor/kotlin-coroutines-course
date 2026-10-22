package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.08. Ограничить параллельное исполнение. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.08") {
        val dispatcher = Dispatchers.Default // TODO: ограничь до 2
        // TODO: запусти шесть блоков и посчитай active/peak атомарно
    }
}
