package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.04. Наблюдать ошибку Deferred. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.04") {
        supervisorScope {
            val result = async<Int> { error("нет данных") }
            // TODO: await и обработка ошибки
            event("Deferred создан: $result")
        }
    }
}
