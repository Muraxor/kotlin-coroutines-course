package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.06. Резервное значение. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.06") {
        val source=flow<Int> { emit(1); error("датчик") }
        // TODO: добавь catch, который отправляет -1, затем collect
        event("Источник готов: $source")
    }
}
