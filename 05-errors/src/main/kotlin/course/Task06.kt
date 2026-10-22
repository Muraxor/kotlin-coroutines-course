package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.06. Сравнить области. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.06") {
        suspend fun ordinary()=coroutineScope { launch { delay(30);error("x") }; launch { try { delay(100) } finally { event("Обычный сосед закрыт") } } }
        // TODO: добавь supervised-версию и сравни два исхода
        try { ordinary() } catch (_: IllegalStateException) { event("Обычный scope упал") }
    }
}
