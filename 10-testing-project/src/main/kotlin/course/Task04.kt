package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.04. Внедряемый dispatcher. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.04") {
        suspend fun answer(): Int = withContext(Dispatchers.Default) { delay(1000); 42 }
        // TODO: передай dispatcher параметром; подставь TestDispatcher из runTest
        runTest { event("Ответ: ${answer()}") }
    }
}
