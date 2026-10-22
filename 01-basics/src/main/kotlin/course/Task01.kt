package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.01. Пауза без блокировки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.01") {
        event("Начало")
        // TODO: добавь паузу 200 мс
        delay(200)
        event("Конец")
    }
}
