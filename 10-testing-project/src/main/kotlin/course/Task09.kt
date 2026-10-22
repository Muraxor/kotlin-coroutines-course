package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.09. Ограничить три запроса. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.09") {
        runTest {
            val ids = (1..10).toList(); val semaphore = Semaphore(3)
            event("Проверяем 10 запросов и лимит 3")
            var result = emptyList<Int>()
            // TODO: Начни обработку десяти id одновременно, но разрешай выполнять
            // не более заданного числа операций сразу. Каждая операция ждёт 100 мс
            // и возвращает id * 2; собери все ответы.
            event("Результаты: $result")
            checkResult("Десять значений", result == ids.map { it * 2 })
        }
    }
}
