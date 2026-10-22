package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.08. Независимая ошибка. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.08") {
        runTest {supervisorScope {
            val failed=async {runCatching {error("x")}};val good=async {42}
            // TODO: await оба, выведи ошибку и успех
            event("Задачи созданы: $failed / $good")
        }}
    }
}
