package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.06. Перевод между счетами. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.06") {
        var a=100;var b=100;val mutex=Mutex()
        // TODO: напиши suspend fun transfer, сохраняющий общую сумму 200
        event("До переводов: $a + $b = ${a+b}")
    }
}
