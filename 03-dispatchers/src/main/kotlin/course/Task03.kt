package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.03. Блокирующая операция на IO. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.03") {
        val text = withContext(Dispatchers.IO) { event("Блокирующее чтение"); Thread.sleep(100); "data" }
        // TODO: замени run подходящим переключением контекста
        event("Получено: $text")
    }
}
