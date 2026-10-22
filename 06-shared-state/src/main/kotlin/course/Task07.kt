package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.07. Без общего счётчика. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.07") {
        val parts=(1..100).toList()
        // TODO: запусти отдельный async на каждый элемент и верни локальную сумму
        // 1000
        event("Частей: ${parts.size}")
    }
}
