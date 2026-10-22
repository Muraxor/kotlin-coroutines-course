package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.05. Собрать экран. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.05") {
        suspend fun loadDashboard(): String {
            // TODO: две независимые операции и объединение результатов
            val deferredName = async { delay(100); "Анна" }
            val deferredAmount = async { delay(150); 3 }
            return "${deferredName.await()}: ${deferredAmount.await()}"
        }
        event(loadDashboard())
    }
}
