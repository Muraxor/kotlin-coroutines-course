package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.07. Два подписчика. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.07") {
        val bus=MutableSharedFlow<Int>()
        // TODO: зарегистрируй двух подписчиков до bus.emit(7)
        event("Подписчиков: ${bus.subscriptionCount.value}")
    }
}
