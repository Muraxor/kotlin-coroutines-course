package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.12. Обязательный источник и отмена соседей. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.12") {
        val active = AtomicInteger()
        val peak = AtomicInteger()
        val cancelledOffers = AtomicInteger()
        suspend fun <T> observe(label: String, action: suspend () -> T): T {
            val count = active.incrementAndGet()
            peak.accumulateAndGet(count, ::maxOf)
            event("$label: старт, одновременно=$count")
            try {
                return action()
            } finally {
                active.decrementAndGet()
                event("$label: завершён")
            }
        }
        suspend fun profile(fails: Boolean): String = observe("Профиль") {
            delay(40)
            if (fails) error("профиль недоступен")
            "user=7"
        }
        suspend fun news(): String = observe("Новости") {
            delay(20)
            error("нет новостей")
        }
        suspend fun offers(): String = observe("Предложения") {
            try {
                delay(100)
                "offers=2"
            } catch (e: CancellationException) {
                cancelledOffers.incrementAndGet()
                throw e
            }
        }

        event("Сценарий 1: ошибка необязательных новостей")
        var goodModel: List<String>? = null
        var failedModel: List<String>? = emptyList()
        // TODO: Собери экран из трёх одновременно начатых источников.
        // Ошибку новостей замени запасным значением.
        // Если обязательный профиль не получен, верни null
        // и прекрати остальные загрузки. Проверь оба сценария.
        event("Сценарий 1: $goodModel; сценарий 2: $failedModel")
        event("Пик: ${peak.get()}, отменено предложений: " +
            "${cancelledOffers.get()}")
        checkResult("Частичный экран собран",
            goodModel == listOf("user=7", "news=empty", "offers=2"))
        checkResult("Без профиля экрана нет",
            failedModel == null && cancelledOffers.get() == 1)
        checkResult("Источники перекрывались и завершились",
            peak.get() >= 2 && active.get() == 0)
    }
}
