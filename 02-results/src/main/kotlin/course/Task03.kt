package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.03. Исправить случайную последовательность. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.03") {
        val a = async { event("A начало"); delay(150); 20 }
        val b = async { event("B начало"); delay(150); 22 }
        event("Сумма: ${a.await() + b.await()}")
    }
}
