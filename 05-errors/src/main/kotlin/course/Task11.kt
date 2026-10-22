package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.11. Отмена выше обработки ошибок. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.11") {
        val closed = AtomicInteger()
        suspend fun child(id: Int) { try { delay(500) } finally { closed.incrementAndGet(); event("Ребёнок $id закрыт") } }
        event("Внешняя отмена должна достичь обоих детей")
        // TODO: Запусти родителя с двумя дочерними операциями так, чтобы сбой
        // одного ребёнка не остановил другого. Затем отмени родителя и дождись
        // завершения детей.
        event("Завершено детей: ${closed.get()}")
        checkResult("Отмена распространилась", closed.get() == 2)
    }
}
