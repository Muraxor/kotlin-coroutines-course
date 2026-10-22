package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.09. Очередь из ста заданий. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.09") {
        val ids = (1..100).toList(); val processed = mutableListOf<Int>()
        event("Заданий: ${ids.size}, работников: 4")
        // TODO: Передай все id четырём работникам через общую очередь. Каждый
        // обработанный id добавь в processed.
        event("Обработано: ${processed.size}, уникальных: ${processed.toSet().size}")
        checkResult("Каждый id один раз", processed.size == 100 && processed.toSet().size == 100)
    }
}
