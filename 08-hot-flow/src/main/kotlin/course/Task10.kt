package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 08.10. Состояние и уведомление. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("08.10") {
        val state = MutableStateFlow("Idle"); val notices = MutableSharedFlow<String>(replay = 1)
        event("Потоки состояния и уведомлений созданы")
        // TODO: До появления наблюдателя переведи state в Ready и отправь
        // уведомление «Готово». Затем проверь, что увидит новый наблюдатель.
        var lastNotice = ""
        event("Состояние: ${state.value}; позднее уведомление: $lastNotice")
        checkResult("Поздний подписчик", state.value == "Ready" && lastNotice == "Готово")
    }
}
