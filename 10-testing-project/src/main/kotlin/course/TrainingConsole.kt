package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

private val startedAt = System.nanoTime()
fun event(message: Any?) = println("[%5d ms] [%-20s] %s".format((System.nanoTime()-startedAt)/1_000_000,Thread.currentThread().name,message))
fun checkResult(label: String, good: Boolean) = event("${if (good) "OK" else "ПОКА НЕ ГОТОВО"}: $label")
suspend fun runExercise(id: String, block: suspend CoroutineScope.() -> Unit) {
    event("=== Задача $id ===")
    try { coroutineScope { block() } }
    catch (e: Throwable) { event("Ошибка: ${e::class.simpleName}: ${e.message}") }
    event("=== Завершено $id ===")
}
