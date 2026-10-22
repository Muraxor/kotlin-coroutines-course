package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.03. Скрыть запись. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.03") {
        class Counter { val state = MutableStateFlow(0); fun increment() { state.value++ } }
        val c = Counter(); c.increment(); event("Значение: ${c.state.value}")
        // TODO: сделай mutable-состояние приватным и наружу верни asStateFlow
    }
}
