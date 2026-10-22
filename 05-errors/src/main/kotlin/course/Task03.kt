package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.03. Независимые дети. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.03") {
        // TODO: изолируй ошибку первого ребёнка
        coroutineScope {
            launch { try { error("сбой") } catch (_: IllegalStateException) { event("Ошибка первого") } }
            launch { delay(80); event("Второй завершён") }
        }
    }
}
