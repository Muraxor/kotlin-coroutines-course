package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 06.10. Кеш. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("06.10") {
        val cache = mutableMapOf<Int, String>(); val mutex = Mutex()
        event("Два запроса к одному ключу 7")
        // TODO: Реализуй get(id): доступ к общему cache должен быть безопасным.
        // Начни два запроса одного id одновременно.
        val values = emptyList<String>()
        event("Значения: $values; размер кеша: ${cache.size}")
        checkResult("Один ключ", values == listOf("item7", "item7") && cache.size == 1)
    }
}
