package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.02. Два независимых результата. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.02") {
        val a = async { delay(100); 20 }
        val b = async { delay(100); 22 }
        val sum = a.await() + b.await()// TODO: добавь результат b
        event("Сумма: $sum")
        checkResult("Сумма 42", sum == 42)
    }
}
