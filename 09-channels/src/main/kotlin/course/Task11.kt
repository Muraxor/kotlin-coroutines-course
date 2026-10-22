package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.11. Ошибка отдельного задания. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.11") {
        val ids = (1..5).toList(); val results = mutableListOf<Pair<Int, String>>()
        fun process(id: Int): String { if (id == 3) error("Задание 3 упало"); return "Success" }
        event("Задания: $ids; ошибка только у id=3")
        // TODO: Распредели id между работниками через очередь. Ошибка одного id не
        // должна остановить обработку остальных.
        event("Итоги: ${results.sortedBy { it.first }}")
        checkResult("Пять исходов", results.size == 5 && results.singleOrNull { it.first == 3 }?.second == "Failure")
    }
}
