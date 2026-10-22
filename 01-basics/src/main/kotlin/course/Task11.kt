package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.11. Один поток — несколько корутин. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.11") {
        event("Доработай этот файл по условию задачи 01.11")
        // TODO: Проведи опыт с двумя корутинами на наследуемом потоке.
        // Каждая должна трижды печатать своё имя с паузой. По журналу объясни,
        // выполнялись ли они параллельно.

        repeat(2) {
            launch(CoroutineName("Coroutine $it")) {
                repeat(3) {
                    delay(100)
                    event("${coroutineContext[CoroutineName]}")
                }
            }
        }
    }
}
