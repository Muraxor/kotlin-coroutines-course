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
        val dispatcher = Dispatchers.Default.limitedParallelism(2) // TODO: ограничь до 2
        val active = AtomicInteger()
        val peak = AtomicInteger()
        val completed = AtomicInteger()
        fun blockingSection(id: Int) {
            val now = active.incrementAndGet()
            peak.accumulateAndGet(now, ::maxOf)
            try {
                event("Блок $id: активно=$now")
                Thread.sleep(40)
            } finally {
                active.decrementAndGet()
                completed.incrementAndGet()
            }
        }

        // TODO: В coroutineScope запусти шесть корутин на dispatcher;
        // каждая вызывает blockingSection со своим номером.
        coroutineScope  {
            withContext(dispatcher) {
                launch { blockingSection(peak.get()) }
                launch { blockingSection(peak.get()) }
                launch { blockingSection(peak.get()) }
                launch { blockingSection(peak.get()) }
                launch { blockingSection(peak.get()) }
                launch { blockingSection(peak.get()) }
            }
        }

        event("Завершено: ${completed.get()}, пик: ${peak.get()}")
        checkResult("Все блоки завершились", completed.get() == 6)
        checkResult("Одновременно выполнялось не больше двух",
            peak.get() in 1..2)
    }
}
