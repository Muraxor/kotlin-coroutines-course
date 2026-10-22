package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.07. Отменить дерево. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.07") {
        val parent=launch { repeat(2) { id -> launch { try { delay(500) } finally { event("Ребёнок $id закрыт") } } } }
        delay(30)
        // TODO: отмени именно родителя и дождись обоих детей
    }
}
