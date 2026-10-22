package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.07. CPU-работа и ожидание. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.07") {
        launch { repeat(3) { delay(30); event("Тик $it") } }
        // TODO: добавь на Default три вычислительных шага с журналом
    }
}
