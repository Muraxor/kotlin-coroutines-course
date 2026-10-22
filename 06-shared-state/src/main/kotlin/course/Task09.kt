package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.09. Склад. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.09") {
        var stock = 10; val mutex = Mutex()
        event("Два клиента хотят зарезервировать по 7 единиц из 10")
        // TODO: Реализуй reserve(n): проверка остатка и его уменьшение должны
        // выполняться как одно неделимое действие.
        val outcomes = emptyList<Boolean>()
        event("Резервы: $outcomes; остаток: $stock")
        checkResult("Один резерв успешен", outcomes.count { it } == 1 && stock == 3)
    }
}
