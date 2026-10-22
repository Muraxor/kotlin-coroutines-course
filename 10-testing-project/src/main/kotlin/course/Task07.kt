package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.07. Проверка Flow. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.07") {
        runTest {val cold=flowOf(1,2,3);val hot=MutableStateFlow(7)
            // TODO: собери cold в список, hot ограничь take(1)
            event("Созданы два потока: $cold / $hot")
        }
    }
}
