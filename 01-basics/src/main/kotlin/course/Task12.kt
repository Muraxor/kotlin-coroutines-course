package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.12. Завтрак с нуля. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.12") {
        event("Доработай этот файл по условию задачи 01.12")
        // TODO: Смоделируй три независимых дела: чай 300 мс, тост 200 мс, яйца 400
        // мс.
        // Напиши функцию приготовления завтрака, после возврата которой всё готово.

        suspend fun makeBreakfest() {
            coroutineScope {
                event("Старт завтрака")
                launch {
                    event("Делаем Чай")
                    delay(300)
                    event("Чай готов")
                }
                launch {
                    event("Делаем тост")
                    delay(200)
                    event("Тост готов")
                }

                launch {
                    event("Делаем яйца")
                    delay(400)
                    event("Яйца готовы")
                }
            }
        }

        makeBreakfest()

        event("Завтра готов")
    }
}
