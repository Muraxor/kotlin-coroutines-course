package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.05. Несколько прогонов. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.05") {
        suspend fun unsafe():Int { var n=0;withContext(Dispatchers.Default){coroutineScope{repeat(100){launch{repeat(1000){n++}}}}};return n }
        // TODO: повтори опыт три раза и выведи каждое значение
        event("Первый прогон: ${unsafe()}")
    }
}
