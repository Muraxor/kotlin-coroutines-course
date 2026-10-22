package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.01. Получить значение. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.01") {
        val deferred = async { delay(100); 42 }
        val answer = deferred.await() // TODO: получи значение deferred
        event("Ответ: $answer")
        checkResult("Ответ равен 42", answer == 42)
    }
}
