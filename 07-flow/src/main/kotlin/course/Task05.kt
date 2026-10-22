package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.05. Холодный источник. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.05") {
        var runs=0;val source=flow { runs++;emit(42) }
        // TODO: собери source дважды и покажи runs после каждого collect
        event("Запусков: $runs")
    }
}
