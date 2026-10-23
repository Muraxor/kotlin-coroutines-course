package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.05. Логическое имя и поток. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.05") {
        val seenNames = java.util.Collections.synchronizedList(mutableListOf<String>())
        suspend fun observe() {
            val name = currentCoroutineContext()[CoroutineName]?.name.orEmpty()
            seenNames += name
            event("Корутина=$name, поток=${Thread.currentThread().name}")
        }

        // TODO: Одновременно запусти на Default две дочерние корутины.
        // Первую назови loader, вторую parser; в каждой вызови observe().
        launch(Dispatchers.Default + CoroutineName("loader")) {
            observe()
        }

        launch(Dispatchers.Default + CoroutineName("parser")) {
            observe()
        }

        checkResult(
            "Обе стадии получили свои имена",
            seenNames.toSet() == setOf("loader", "parser")
        )
    }
}
