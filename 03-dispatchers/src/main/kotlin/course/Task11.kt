package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 03.11. Наследование контекста. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("03.11") {
        val seen = java.util.Collections.synchronizedList(mutableListOf<String>())
        val parentLinks = AtomicInteger()
        val inheritedDispatchers = AtomicInteger()
        val completed = AtomicInteger()
        event("Исследуем имя, диспетчер и связь с родительской задачей")
        // TODO: Создай родителя на вычислительном диспетчере с именем parent.
        // Запусти двух детей одновременно: один наследует имя,
        // другой получает имя child. Оба наследуют диспетчер.
        // Для каждого проверь, что у него свой Job,
        // а родитель действительно содержит его среди детей.
        // Дождись завершения обоих перед итоговым сообщением.
        event("Имена детей: $seen")
        event("Связи: ${parentLinks.get()}, диспетчеры: " +
            "${inheritedDispatchers.get()}, завершено: ${completed.get()}")
        checkResult("Имена parent и child",
            seen.toSet() == setOf("parent", "child"))
        checkResult("Связь и контекст обоих детей",
            parentLinks.get() == 2 && inheritedDispatchers.get() == 2)
        checkResult("Родитель дождался детей", completed.get() == 2)
    }
}
