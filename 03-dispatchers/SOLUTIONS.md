# Решения: Контекст, потоки и диспетчеры

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 03.01 · Увидеть поток

**Что сделать:** Добавь вывод имени потока до и после приостанавливающего ожидания. Сравни имена, не предполагая обязательного переключения.

```kotlin
event("До delay: ${Thread.currentThread().name}")
delay(100)
event("После delay: ${Thread.currentThread().name}")
```

**Почему:** Приостановка не означает переключение потока.

**Ожидаемый результат:** В консоли видны имена потоков; в runBlocking они обычно одинаковы.

[Вернуться к задаче](TASKS.md)

## 03.02 · Вычисление на Default

**Что сделать:** Перенеси суммирование 1..1000 в вычислительный диспетчер. Выведи поток внутри блока и результат снаружи.

```kotlin
val sum = withContext(Dispatchers.Default) { event("Вычисление"); (1..1000).sum() }
event("Сумма: $sum")
checkResult("500500", sum == 500500)
```

**Почему:** withContext возвращает значение блока и ждёт его завершения.

**Ожидаемый результат:** Вычисление видно на рабочем потоке, сумма 500500.

[Вернуться к задаче](TASKS.md)

## 03.03 · Блокирующая операция на IO

**Что сделать:** Готовое блокирующее ожидание имитирует старое API чтения. Перенеси его на IO и верни строку в вызывающий контекст.

```kotlin
val text = withContext(Dispatchers.IO) { event("Блокирующее чтение"); Thread.sleep(100); "data" }
event("Получено: $text")
```

**Почему:** IO подходит для блокирующего ввода-вывода; suspend сам не превращает sleep в неблокирующий вызов.

**Ожидаемый результат:** Чтение на рабочем потоке, результат data у вызывающего.

[Вернуться к задаче](TASKS.md)

## 03.04 · Вернуть вычисленный результат

**Что сделать:** Функция запускает отдельный scope и сразу отдаёт -1. Исправь её, чтобы вернуть реальную сумму и не оставлять фоновую работу.

```kotlin
suspend fun calculate(): Int = withContext(Dispatchers.Default) { (1..1000).sum() }
val result = calculate()
event("Ответ: $result")
checkResult("Результат вычисления", result == 500500)
```

**Почему:** withContext связывает результат с завершением вычисления; отдельный scope ломает эту связь.

**Ожидаемый результат:** 500500 вместо -1.

[Вернуться к задаче](TASKS.md)

## 03.05 · Логическое имя и поток

**Что сделать:** Одновременно запусти две дочерние корутины на `Default`, составив их контекст из dispatcher и логического имени.

```kotlin
coroutineScope {
    launch(Dispatchers.Default + CoroutineName("loader")) { observe() }
    launch(Dispatchers.Default + CoroutineName("parser")) { observe() }
}
```

**Почему:** `CoroutineName` описывает логическую операцию, а dispatcher выбирает исполнителя. Две корутины с разными именами могут оказаться на одном рабочем потоке; имя корутины не управляет планированием.

**Ожидаемый результат:** Видны имена `loader` и `parser`, проверка имеет статус `OK`; имена потоков не фиксированы.

[Вернуться к задаче](TASKS.md)

## 03.06 · Внедрить dispatcher

**Что сделать:** Передай dispatcher функции явно, проверь фактический элемент контекста и вызови функцию с двумя разными зависимостями.

```kotlin
suspend fun calculate(dispatcher: CoroutineDispatcher): Int = withContext(dispatcher) {
    contextMatches += currentCoroutineContext()[ContinuationInterceptor] === dispatcher
    event("Вычисление на ${Thread.currentThread().name}")
    6 * 7
}
results = listOf(
    calculate(Dispatchers.Default),
    calculate(Dispatchers.IO),
)
```

**Почему:** Функция больше не выбирает среду выполнения внутри себя. Вызывающий код может подставить подходящий dispatcher, а в тесте — управляемую тестовую реализацию. `Default` и `IO` могут использовать потоки с похожими именами, поэтому проверяется элемент контекста, а не текст имени потока.

**Ожидаемый результат:** Ответы `[42, 42]` и две строки `OK`.

[Вернуться к задаче](TASKS.md)

## 03.07 · CPU-работа и ожидание

**Что сделать:** Перенеси три готовых процессорных шага на `Default`. Между шагами нет `delay`: вызывающий поток остаётся свободным именно благодаря смене dispatcher.

```kotlin
result = withContext(Dispatchers.Default) {
    var value = 1L
    repeat(3) { step ->
        value = cpuStep(value.toInt() + step)
        completedSteps.incrementAndGet()
        event("Вычислен шаг ${step + 1}")
    }
    value
}
```

**Почему:** `cpuStep` не приостанавливается и занял бы поток `runBlocking`. `withContext(Dispatchers.Default)` переносит эту работу на вычислительный пул, поэтому ticker продолжает выполняться у вызывающего.

**Ожидаемый результат:** Во время вычисления появляются тики, выполнены три шага и обе проверки имеют статус `OK`.

[Вернуться к задаче](TASKS.md)

## 03.08 · Ограничить параллельное исполнение

**Что сделать:** Создай представление `Default` с параллелизмом 2 и запусти на нём шесть готовых блокирующих секций.

```kotlin
val dispatcher = Dispatchers.Default.limitedParallelism(2)
coroutineScope {
    repeat(6) { id ->
        launch(dispatcher) { blockingSection(id) }
    }
}
```

**Почему:** `limitedParallelism(2)` ограничивает число одновременно исполняемых блоков этого представления dispatcher. `Thread.sleep` здесь намеренно удерживает блок, чтобы ограничение можно было измерить; для ограничения числа приостановленных операций нужен другой механизм, например `Semaphore`.

**Ожидаемый результат:** Завершены все шесть блоков, пик равен 1 или 2 и обе проверки имеют статус `OK`.

[Вернуться к задаче](TASKS.md)

## 03.09 · Два независимых чтения

**Что сделать:** Добавь следующий код вместо TODO, оставив временную переменную `result`.

```kotlin
suspend fun process(input: Pair<String, String>): Pair<String, Int> {
    val data = withContext(Dispatchers.IO) { legacyRead(input) }
    val count = withContext(Dispatchers.Default) { countParts(data) }
    return input.first to count
}
result = coroutineScope {
    inputs.map { input -> async { process(input) } }.awaitAll()
}
```

**Почему:** Сначала создаются обе независимые дочерние операции. Каждая переносит блокирующее чтение на IO, обработку — на Default. `awaitAll()` собирает ответы в исходном порядке, а счётчик подтверждает, что чтения перекрылись.

**Ожидаемый результат:** `[(left, 3), (right, 2)]`, пик 2 и две строки `OK`.

[Вернуться к задаче](TASKS.md)

## 03.10 · Собственный пул потоков

**Что сделать:** Создай фиксированный `ExecutorService`, преобразуй его в dispatcher, запусти все операции и закрой принадлежащий программе ресурс.

```kotlin
val executor = java.util.concurrent.Executors.newFixedThreadPool(2) { task ->
    Thread(task, "course-worker-${createdThreads.incrementAndGet()}")
}
executor.asCoroutineDispatcher().use { dispatcher ->
    results = coroutineScope {
        inputs.map { number ->
            async(dispatcher) { blockingWork(number) }
        }.awaitAll()
    }
}
executorWasClosed = executor.isShutdown
```

**Почему:** В отличие от `limitedParallelism`, собственный executor создаёт принадлежащие приложению потоки. `asCoroutineDispatcher` позволяет планировать на них корутины, а `use` закрывает dispatcher и underlying executor. Все `async` создаются до `awaitAll`, поэтому две операции могут выполняться одновременно; порядок списка результатов соответствует порядку `Deferred`.

**Ожидаемый результат:** `[1, 4, 9, 16, 25, 36]`, два именованных потока, пик 2, закрытый пул и три строки `OK`.

[Вернуться к задаче](TASKS.md)

## 03.11 · Наследование контекста

**Что сделать:** Добавь следующий код вместо TODO в `Task11.kt`.

```kotlin
withContext(Dispatchers.Default + CoroutineName("parent")) {
    val parentDispatcher = coroutineContext[ContinuationInterceptor]
    suspend fun observe(waitMs: Long) {
        val context = currentCoroutineContext()
        val name = context[CoroutineName]?.name.orEmpty()
        seen.add(name)
        if (context[ContinuationInterceptor] === parentDispatcher) {
            inheritedDispatchers.incrementAndGet()
        }
        event("Ребёнок $name на ${Thread.currentThread().name}")
        delay(waitMs)
        completed.incrementAndGet()
    }
    launch { observe(60) }
    launch(CoroutineName("child")) { observe(30) }
}
```

**Почему:** Первый ребёнок наследует оба элемента контекста. У второго новый `CoroutineName` заменяет родительское имя, но не затрагивает dispatcher. `withContext` возвращается после завершения обоих детей, поэтому итоговые счётчики уже окончательные.

**Ожидаемый результат:** Имена `parent` и `child`, два унаследованных dispatcher и три строки `OK`.

[Вернуться к задаче](TASKS.md)

## 03.12 · Конвейер файлов с ограничением нагрузки

**Что сделать:** Вставь фрагмент вместо TODO в `Task12.kt`, оставив временную переменную `result`. Остальные данные, тикер и проверки уже готовы.

```kotlin
val readDispatcher = Dispatchers.IO.limitedParallelism(2)
val analysisDispatcher = Dispatchers.Default.limitedParallelism(1)
result = coroutineScope {
    names.map { name ->
        async {
            val content = withContext(readDispatcher) { legacyRead(name) }
            withContext(analysisDispatcher) { analyze(name, content) }
        }
    }.awaitAll()
}
```

**Почему:** `async` начинает работу для каждого файла до сбора результатов. Отдельные представления диспетчеров ограничивают чтения двумя и анализ одной одновременно исполняемой стадией. Каждый файл переходит к анализу сразу после своего чтения, поэтому стадии перекрываются. `awaitAll()` возвращает результаты в порядке списка задач, даже если завершения перемешались. Вызывающий поток в это время может выполнять тикер.

**Ожидаемый результат:** Шесть пар в порядке имён, пики чтения и анализа 2 и 1, перекрытие стадий, тики вызывающего потока и четыре строки `OK`.

[Вернуться к задаче](TASKS.md)

