package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.10. Граф A → B,C → D. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.10") {
        event("Граф зависимостей: A → (B, C) → D")
        val a = fetchA()

        val result = coroutineScope {
            // TODO: после получения A начни вычислять B и C одновременно.
            val b = async { fetchB(a) }
            val c = async { fetchC(a) }
            // Значение D должно быть суммой обоих результатов и появиться только
            // после завершения B и C.
            val d = b.await() + c .await()
            d
        }

        event("D = $result")
        checkResult("D = 31", result == 31)
    }
}

private suspend fun fetchA(): Int {
    delay(30)
    event("A готово: 10")
    return 10
}

private suspend fun fetchB(a: Int): Int {
    event("B началось после A=$a")
    delay(80)
    val result = a * 2
    event("B готово: $result")
    return result
}

private suspend fun fetchC(a: Int): Int {
    event("C началось после A=$a")
    delay(60)
    val result = a + 1
    event("C готово: $result")
    return result
}

