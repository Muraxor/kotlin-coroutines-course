package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.09. Состояния загрузки. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.09") {
        val state = MutableStateFlow("Idle")
        val observed = mutableListOf<String>()
        event("Начальное состояние: ${state.value}")
        // TODO: Начни наблюдать за состоянием и запиши три увиденных значения.
        // Затем переведи состояние в Loading и Success; дождись наблюдателя.
        event("Переходы: $observed")
        checkResult("Три состояния", observed == listOf("Idle", "Loading", "Success"))
    }
}
