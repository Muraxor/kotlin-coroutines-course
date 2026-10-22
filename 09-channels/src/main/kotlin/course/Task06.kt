package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.06. Закрытие при ошибке. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.06") {
        val channel=Channel<Int>()
        // TODO: producer посылает 1,2, затем падает и закрывает канал с причиной
        // TODO: consumer читает значения и причину
        event("Канал создан: $channel")
    }
}
