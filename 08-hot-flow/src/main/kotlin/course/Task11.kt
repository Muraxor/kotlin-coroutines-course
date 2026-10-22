package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.11. Конкурентные обновления. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.11") {
        val state = MutableStateFlow(0)
        event("1000 конкурентных увеличений состояния")
        // TODO: Начни 1000 одновременных увеличений state на единицу. Ни одно
        // обновление не должно потеряться.
        event("Итоговое значение: ${state.value}")
        checkResult("1000", state.value == 1000)
    }
}
