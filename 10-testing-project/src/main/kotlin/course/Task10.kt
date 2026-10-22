package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import kotlinx.coroutines.test.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 10.10. Прогресс и отмена. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("10.10") {
        runTest {
            val progress = MutableStateFlow(0)
            event("Нужно остановить прогресс примерно на 40%")
            // TODO: Обновляй progress на 20 каждые 100 мс виртуального времени.
            // Продвинь время, затем отмени работу и проверь итог.
            val stopped = progress.value
            event("После отмены: $stopped")
            checkResult("Не дошли до 100%", stopped in 20..80)
        }
    }
}
