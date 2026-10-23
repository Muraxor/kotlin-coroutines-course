package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/** 03.11. Наследование контекста. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.11") {
        val seen = java.util.Collections.synchronizedList(mutableListOf<String>())
        val inheritedDispatchers = AtomicInteger()
        val completed = AtomicInteger()
        event("Исследуем наследование элементов контекста")
        // TODO: Создай родительский контекст для вычислительной работы
        // с логическим именем parent. Запомни его dispatcher.
        val parentDispatcher = Dispatchers.Default
        val parentContext = CoroutineName("parent") + parentDispatcher

        // Запусти двух детей одновременно:
        // первый без нового контекста, второй с CoroutineName("child").

        withContext(parentContext) {
            launch {
                if (coroutineContext[ContinuationInterceptor] === parentDispatcher) inheritedDispatchers.incrementAndGet()

                seen.add(coroutineContext[CoroutineName]?.name.orEmpty())

                delay(50)
                completed.incrementAndGet()
            }
            launch(CoroutineName("child")) {
                if (coroutineContext[ContinuationInterceptor] === parentDispatcher) inheritedDispatchers.incrementAndGet()

                seen.add(coroutineContext[CoroutineName]?.name.orEmpty())

                delay(50)
                completed.incrementAndGet()
            }
        }


        // В каждом сохрани фактическое имя, сравни dispatcher с родительским,
        // сделай короткий delay и увеличь completed.
        event("Имена детей: $seen")
        event(
            "Унаследовано диспетчеров: ${inheritedDispatchers.get()}, " +
                    "завершено: ${completed.get()}"
        )
        checkResult(
            "Имена parent и child",
            seen.toSet() == setOf("parent", "child")
        )
        checkResult(
            "Оба ребёнка унаследовали dispatcher",
            inheritedDispatchers.get() == 2
        )
        checkResult("Контекст дождался обоих детей", completed.get() == 2)
    }
}
