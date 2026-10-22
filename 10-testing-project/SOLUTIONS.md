# Решения: Тестирование и итоговый проект

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 10.01 · Первый runTest

**Что сделать:** В отдельном упражнении запусти suspend-функцию в runTest и проверь ответ 42. Выведи результат проверки в консоль.

```kotlin
var answer=0;runTest { delay(100);answer=42 };event("Ответ: $answer");checkResult("42",answer==42)
```

**Почему:** runTest использует виртуальное время для delay на тестовом планировщике.

**Ожидаемый результат:** Ответ 42 и OK без реальной паузы 100 мс.

[Вернуться к задаче](TASKS.md)

## 10.02 · Запустить текущую очередь

**Что сделать:** Внутри runTest запусти ребёнка и проверь started после runCurrent.

```kotlin
runTest {var started=false;val job=launch{started=true;delay(100)};testScheduler.runCurrent();event("Началась: $started");checkResult("Старт",started);job.cancelAndJoin()}
```

**Почему:** StandardTestDispatcher ставит ребёнка в очередь до явного продвижения.

**Ожидаемый результат:** Началась: true и OK.

[Вернуться к задаче](TASKS.md)

## 10.03 · Граница виртуального времени

**Что сделать:** Задача завершится через 1000 виртуальных мс. На отметке 999 проверь false, на отметке 1000 — true.

```kotlin
runTest {var done=false;launch{delay(1000);done=true};testScheduler.runCurrent();testScheduler.advanceTimeBy(999);event("999: $done");testScheduler.advanceTimeBy(1);testScheduler.runCurrent();event("1000: $done")}
```

**Почему:** Задача на новой границе выполняется после runCurrent.

**Ожидаемый результат:** 999: false; 1000: true.

[Вернуться к задаче](TASKS.md)

## 10.04 · Внедряемый dispatcher

**Что сделать:** Функция использует Default и настоящий delay. Передай dispatcher аргументом, вызови с тестовым dispatcher и покажи, что прошло 1000 виртуальных мс.

```kotlin
suspend fun answer(dispatcher:CoroutineDispatcher)=withContext(dispatcher){delay(1000);42}
runTest {val result=answer(StandardTestDispatcher(testScheduler));event("Ответ=$result, виртуальное время=$currentTime")}
```

**Почему:** Жёстко заданный Default не использует тестовый планировщик.

**Ожидаемый результат:** Ответ=42, виртуальное время=1000.

[Вернуться к задаче](TASKS.md)

## 10.05 · Таймаут без ожидания

**Что сделать:** Через runTest проверь withTimeoutOrNull(100) для delay(500). Выведи результат и виртуальное время.

```kotlin
runTest {val result=withTimeoutOrNull(100){delay(500);42};event("Результат=$result, время=$currentTime");checkResult("null",result==null)}
```

**Почему:** runTest позволяет испытывать таймаут без ожидания часов.

**Ожидаемый результат:** null, время 100.

[Вернуться к задаче](TASKS.md)

## 10.06 · Тест очистки

**Что сделать:** В runTest запусти задачу с finally, отмени и дождись; проверь флаг очистки.

```kotlin
runTest {var clean=false;val job=launch{try{delay(500)}finally{clean=true;event("Очистка")}};testScheduler.runCurrent();job.cancelAndJoin();checkResult("Очистка была",clean)}
```

**Почему:** cancelAndJoin даёт детерминированную точку для проверки ресурсов.

**Ожидаемый результат:** Очистка и OK.

[Вернуться к задаче](TASKS.md)

## 10.07 · Проверка Flow

**Что сделать:** Собери конечный flow в список и проверь [1,2,3]. Горячий поток ограничь take, чтобы не зависнуть.

```kotlin
runTest {val cold=flowOf(1,2,3).toList();event("Холодный: $cold");val hot=MutableStateFlow(7);val one=hot.take(1).toList();event("Горячий: $one");checkResult("Оба потока",cold==listOf(1,2,3)&&one==listOf(7))}
```

**Почему:** У StateFlow нет собственного нормального завершения.

**Ожидаемый результат:** [1,2,3], [7] и OK.

[Вернуться к задаче](TASKS.md)

## 10.08 · Независимая ошибка

**Что сделать:** В runTest проверь: одна supervised задача падает, другая возвращает 42. Выведи оба результата.

```kotlin
runTest {val pair=supervisorScope {val failed=async {runCatching {error("x")}};val good=async{42};failed.await().isFailure to good.await()};event("Ошибка=${pair.first}, успех=${pair.second}");checkResult("Изоляция",pair.first&&pair.second==42)}
```

**Почему:** Supervision позволяет проверить локальный отказ без отмены соседней задачи.

**Ожидаемый результат:** Ошибка=true, успех=42 и OK.

[Вернуться к задаче](TASKS.md)

## 10.09 · Ограничить три запроса

**Что сделать:** С нуля реализуй десять запросов с Semaphore(3), результатами по id и проверкой пика.

```kotlin
runTest {val sem=Semaphore(3);var active=0;var peak=0;val values=coroutineScope{(1..10).map{id->async{sem.withPermit{active++;peak=maxOf(peak,active);try{delay(100);id*2}finally{active--}}}}.awaitAll()};event("Пик=$peak, результаты=$values");checkResult("Ограничение",peak<=3&&values==(1..10).map{it*2})}
```

**Почему:** Semaphore контролирует целую suspend-операцию.

**Ожидаемый результат:** Пик ≤3, результаты [2,4,...,20].

[Вернуться к задаче](TASKS.md)

## 10.10 · Прогресс и отмена

**Что сделать:** Создай StateFlow прогресса 0..100 с шагом 20, отмени задачу на половине и проверь, что значение больше не меняется.

```kotlin
runTest {val progress=MutableStateFlow(0);val job=launch {for(n in 20..100 step 20){delay(100);progress.value=n}};testScheduler.runCurrent();testScheduler.advanceTimeBy(250);testScheduler.runCurrent();job.cancelAndJoin();val stop=progress.value;testScheduler.advanceUntilIdle();event("Остановлено на $stop");checkResult("Не меняется",progress.value==stop)}
```

**Почему:** Проверка использует управляемое время, а не Thread.sleep.

**Ожидаемый результат:** Прогресс 40; после отмены он не меняется.

[Вернуться к задаче](TASKS.md)

## 10.11 · Набор проверок

**Что сделать:** Напиши консольные проверки успеха, ошибки, таймаута, ограничения нагрузки и отмены. Используй заглушки вместо сети.

```kotlin
runTest {checkResult("Успех",async{42}.await()==42);checkResult("Ошибка",runCatching{error("x")}.isFailure);checkResult("Таймаут",withTimeoutOrNull(10){delay(100);1}==null);val sem=Semaphore(1);var active=0;var peak=0;coroutineScope{repeat(3){launch{sem.withPermit{active++;peak=maxOf(peak,active);delay(10);active--}}}};checkResult("Нагрузка",peak==1);val job=launch{delay(100)};job.cancelAndJoin();checkResult("Отмена",job.isCancelled)}
```

**Почему:** Каждый важный исход должен иметь собственный наблюдаемый критерий.

**Ожидаемый результат:** Пять проверок OK.

[Вернуться к задаче](TASKS.md)

## 10.12 · Итоговый сервис и виртуальный тест

**Что сделать:** Вставь код вместо TODO в `Task12.kt`, оставив временную переменную `results`. Остальной тестовый шаблон уже готов.

```kotlin
val queue = Channel<Int>(capacity = 10)
val output = Channel<Pair<Int, Int>>(capacity = 100)
coroutineScope {
    val workers = List(3) {
        launch {
            for (id in queue) {
                try {
                    output.send(id to process(id))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    failures.add(id)
                } finally {
                    progress.update { it + 1 }
                }
            }
        }
    }
    launch {
        try {
            for (id in ids) queue.send(id)
        } finally {
            queue.close()
        }
    }
    workers.joinAll()
}
output.close()
results = output.toList()
```

**Почему:** Очередь ограничивает число ожидающих заданий, а три работника — число обрабатываемых. Ошибка одного задания остаётся локальной; `finally` учитывает любое завершённое задание в прогрессе. После завершения работников результаты можно собрать полностью. `runTest` выполняет задержки по виртуальному времени, поэтому проверяется вся последовательность без реального ожидания.

**Ожидаемый результат:** 99 результатов, ошибка id 50, прогресс 100, пик не выше трёх, около 340 мс виртуального времени и три строки `OK`.

[Вернуться к задаче](TASKS.md)

