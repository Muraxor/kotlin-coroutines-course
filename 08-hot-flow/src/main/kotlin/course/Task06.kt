package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.06. Поздний подписчик. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.06") {
        val noReplay=MutableSharedFlow<Int>(replay=0)
        val oneReplay=MutableSharedFlow<Int>(replay=1)
        // TODO: отправь 42 до подписок и сравни поздних подписчиков
        event("Созданы два SharedFlow")
    }
}
