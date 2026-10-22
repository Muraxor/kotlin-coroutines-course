package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.03. Граница виртуального времени. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.03") {
        runTest { var done=false; launch { delay(1000); done=true }
            testScheduler.runCurrent()
            // TODO: продвинь время до 999 и 1000 мс, выведи флаг после каждого шага
            event("Сейчас: $done")
        }
    }
}
