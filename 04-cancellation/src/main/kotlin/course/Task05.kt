package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.05. Цикл без delay. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.05") {
        val job=launch(Dispatchers.Default) { var n=0L; while(true) { if(n++ % 10_000L==0L) currentCoroutineContext().ensureActive() } }
        delay(30);job.cancelAndJoin();event("Цикл остановлен")
        // TODO: сделай цикл без встроенной проверки и убедись, что понимаешь,
        // почему он не остановится
    }
}
