package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.06. Внедрить dispatcher. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.06") {
        val contextMatches = java.util.Collections.synchronizedList(mutableListOf<Boolean>())
        suspend fun calculate(dispatcher: CoroutineDispatcher): Int = withContext(dispatcher) {
            event("Вычисление на ${Thread.currentThread().name}")
            contextMatches +=
                currentCoroutineContext()[ContinuationInterceptor] === dispatcher
            6 * 7
        }

        val results = mutableListOf<Int>()
        // TODO: Убери жёстко заданный Default из calculate:
        // передавай CoroutineDispatcher параметром и используй его в withContext.
        // Внутри withContext добавь в contextMatches результат проверки:
        // currentCoroutineContext()[ContinuationInterceptor] === dispatcher.
        // Вызови calculate с Default и IO и сохрани оба ответа в results.

        calculate(Dispatchers.Default).apply {
            results.add(this)
        }


        calculate(Dispatchers.IO).apply {
            results.add(this)
        }


        event("Ответы: $results")
        checkResult("Оба вызова вернули 42", results == listOf(42, 42))
        checkResult(
            "Функция использовала переданные диспетчеры",
            contextMatches.size == 2 && contextMatches.all { it })
    }
}
