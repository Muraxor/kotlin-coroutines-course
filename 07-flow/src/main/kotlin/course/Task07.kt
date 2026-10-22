package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 07.07. Буфер. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("07.07") {
        val source=flow { repeat(3) { delay(20);emit(it) } }
        // TODO: сравни два collect с задержкой 50 мс в потребителе — без buffer и с
        // buffer
        event("Начинаю сравнение $source")
    }
}
