package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.04. Защитить проверку и запись. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.04") {
        var balance = 100; val mutex = Mutex()
        suspend fun withdraw(n: Int): Boolean {
            // TODO: защити проверку и изменение вместе
            if (balance < n) return false
            return mutex.withLock { balance -= n; true }
        }
        val outcomes = coroutineScope { listOf(async(Dispatchers.Default){withdraw(80)},async(Dispatchers.Default){withdraw(80)}).awaitAll() }
        event("Снятия: $outcomes; баланс: $balance")
    }
}
