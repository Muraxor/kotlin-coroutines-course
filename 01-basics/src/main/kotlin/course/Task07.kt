package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.07. Журнал трёх задач. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.07") {
        val waits = listOf(100L, 200L, 300L)
        // TODO: запусти задачу для каждого ожидания и выведи итог после всех
        coroutineScope {
            waits.forEach {
                launch {
                    event("start $it")
                    delay(it)
                    event("end $it")
                }
            }

        }

        event("Done")
    }
}
