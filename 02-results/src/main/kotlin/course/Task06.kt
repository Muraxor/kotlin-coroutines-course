package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 02.06. Зависимый граф. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("02.06") {
        val userId = run { delay(50); event("Получен userId=7"); 7 }

        val summary = coroutineScope {
            // TODO 1: создай async для fetchProfile(userId).
            val deferredProfile = async { fetchProfile(userId) }
            // TODO 2: создай async для fetchOrders(userId) ДО первого await().
            val deferredOrders = async { fetchOrders(userId) }
            // TODO 3: дождись обоих результатов и собери строку "User7: 2".
            "${deferredProfile.await()}: ${deferredOrders.await()}"
        }

        event("Итог: $summary")
        checkResult("Профиль и заказы получены", summary == "User7: 2")
    }
}

private suspend fun fetchProfile(userId: Int): String {
    event("Профиль для $userId: старт")
    delay(100)
    event("Профиль для $userId: готов")
    return "User$userId"
}

private suspend fun fetchOrders(userId: Int): Int {
    event("Заказы для $userId: старт")
    delay(150)
    event("Заказы для $userId: готовы")
    return 2
}
