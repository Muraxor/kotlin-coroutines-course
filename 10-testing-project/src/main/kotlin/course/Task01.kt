package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.01. Первый runTest. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.01") {
        var answer = 0
        // TODO: вызови suspend-код внутри runTest, получи 42
         event("Ответ: $answer"); checkResult("42",answer==42)
    }
}
