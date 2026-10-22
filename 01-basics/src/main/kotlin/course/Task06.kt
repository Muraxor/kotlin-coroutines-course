package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.06. Блокировка против приостановки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.06") {
        event("Блокирующий опыт")
        coroutineScope { repeat(2) { id -> launch { event("sleep $id начало"); Thread.sleep(100); event("sleep $id готов") } } }
        // TODO: добавь такой же опыт с delay и сообщения начала
        coroutineScope { repeat(2) {
            launch {
                event("delay $it начало")
                delay(100)
                event("delay $it готово")
            }
        } }
    }
}
