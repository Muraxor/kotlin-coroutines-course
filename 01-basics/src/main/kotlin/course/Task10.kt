package course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.system.measureTimeMillis

/** 01.10. Три загрузки с нуля. См. TASKS.md. */
fun main() = runBlocking {
    runExercise("01.10") {
        event("Доработай этот файл по условию задачи 01.10")
        // TODO: Смоделируй загрузку трёх файлов. Настоящая сеть не нужна: используй
        // ожидания.
        //  Выведи имя, старт, окончание и общий итог.
        coroutineScope {
            repeat(3) {
                launch { loadFile("File-$it") }
            }
        }

        event("Все файлы выгружены")
    }
}

suspend fun loadFile(name: String) {
    event("Start loading $name")
    delay(500)
    event("End loading $name")
}
