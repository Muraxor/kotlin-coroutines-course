package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.06. Внедрить dispatcher. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.06") {
        suspend fun calculate(): Int = withContext(Dispatchers.Default) { 6 * 7 }
        // TODO: добавь параметр dispatcher и два вызова
        event("Ответ: ${calculate()}")
    }
}
