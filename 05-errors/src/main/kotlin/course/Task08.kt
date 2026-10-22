package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 05.08. Обязательный и необязательные. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("05.08") {
        val required="Анна";val optional=listOf("рекомендации","баннер")
        // TODO: смоделируй ошибку рекомендаций, сохрани профиль и баннер
        event("Профиль: $required; дополнения: $optional")
    }
}
