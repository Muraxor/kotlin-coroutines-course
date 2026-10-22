package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.09. Таймер с нуля. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.09") {
        event("Доработай этот файл по условию задачи 01.09")
        // TODO: Напиши таймер из пяти тиков. Первый тик — через 100 мс, следующие с
        // тем же интервалом.
        //  После пятого выведи «Таймер завершён».
        launch {
            repeat(5) {
                delay(100)
                event("${it + 1}")
            }
            event("Таймер завершён")
        }
    }
}
