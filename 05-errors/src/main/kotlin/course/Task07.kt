package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.07. Три карточки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.07") {
        val ids=listOf(1,2,3)
        // TODO: второй id должен дать ошибку; первый и третий — карточки
        event("Карточки: $ids")
    }
}
