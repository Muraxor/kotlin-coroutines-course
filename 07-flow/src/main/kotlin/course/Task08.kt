package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.08. Пропуск и отмена старой обработки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.08") {
        val source=flow { repeat(5) { emit(it);delay(20) } }
        // TODO: добавь два отдельных сбора: conflate и collectLatest
        event("Быстрый источник: $source")
    }
}
