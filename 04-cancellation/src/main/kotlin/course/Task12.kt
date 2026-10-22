package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 04.12. Отмена сессии и гарантированная очистка. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("04.12") {
        val resourceClosed = AtomicInteger()
        val childrenClosed = AtomicInteger()
        val callerThread = Thread.currentThread()
        val cleanupOrder = java.util.Collections.synchronizedList(
            mutableListOf<String>())
        val resourceOffCaller = AtomicInteger()
        fun closeResource() {
            Thread.sleep(20)
            if (Thread.currentThread() !== callerThread) {
                resourceOffCaller.incrementAndGet()
            }
            resourceClosed.incrementAndGet()
            cleanupOrder.add("resource")
            event("Ресурс закрыт")
        }
        event("Два ребёнка работают; сессию отменят через 40 мс")

        // TODO: Запусти двух детей внутри одной сессии.
        // Оба должны пережить отмену достаточно долго,
        // чтобы завершить приостанавливающую очистку.
        // После их очистки закрой блокирующий ресурс вне вызывающего потока.
        // Отмени сессию через 40 мс и дождись всей очистки.

        event("Порядок очистки: $cleanupOrder")
        checkResult("Каждый объект закрыт один раз",
            childrenClosed.get() == 2 && resourceClosed.get() == 1)
        checkResult("Ресурс после детей",
            cleanupOrder.size == 3 &&
                cleanupOrder.take(2).toSet() == setOf("child0", "child1") &&
                cleanupOrder.last() == "resource")
        checkResult("Блокирующая очистка вне вызывающего потока",
            resourceOffCaller.get() == 1)
    }
}
