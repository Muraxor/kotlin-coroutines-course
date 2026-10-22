package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 09.12. Очередь с давлением и ошибкой. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("09.12") {
        val ids = (1..20).toList()
        val done = AtomicInteger()
        val active = AtomicInteger()
        val peak = AtomicInteger()
        val sent = AtomicInteger()
        val sentBeforeWorkers = AtomicInteger(-1)
        val failures = java.util.concurrent.ConcurrentLinkedQueue<Int>()
        suspend fun process(id: Int) {
            val n = active.incrementAndGet()
            peak.accumulateAndGet(n, ::maxOf)
            try {
                delay(20)
                if (id == 7) error("пакет 7 повреждён")
                done.incrementAndGet()
                event("Пакет $id готов")
            } finally {
                active.decrementAndGet()
            }
        }
        event("Пакетов: 20, буфер: 3, работника: 2")

        // TODO: Сначала запусти отправителя, пока работники ещё не начали
        // принимать пакеты. Запиши в sentBeforeWorkers число отправленных.
        // Затем начни двух работников. Сбой пакета 7 не должен остановить их.
        // Дождись завершения отправителя и обоих работников.

        event("До работников: ${sentBeforeWorkers.get()} отправлено")
        event("Готово: ${done.get()}, ошибки: $failures, пик: ${peak.get()}")
        checkResult("Буфер действительно ограничивает отправителя",
            sentBeforeWorkers.get() == 3 && sent.get() == ids.size)
        checkResult("Ошибка одного пакета локальна",
            done.get() == 19 && failures.toList() == listOf(7))
        checkResult("Работников не больше двух",
            peak.get() in 1..2 && active.get() == 0)
    }
}
