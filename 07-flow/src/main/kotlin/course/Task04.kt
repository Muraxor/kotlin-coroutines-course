package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.04. Ограничить бесконечный поток. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.04") {
        flow { var n=0; while(true) { emit(n++); delay(20) } }
            .take(1) // TODO: добейся трёх значений
            .collect { event("$it") }
    }
}
