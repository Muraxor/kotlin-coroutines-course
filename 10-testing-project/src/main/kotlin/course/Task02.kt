package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.02. Запустить текущую очередь. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.02") {
        runTest { var started=false; val job=launch { started=true; delay(100) }
            // TODO: выполни очередь текущего момента
            event("Началась: $started"); checkResult("Старт",started); job.cancelAndJoin()
        }
    }
}
