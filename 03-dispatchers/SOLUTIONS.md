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

## 03.05 · Имя корутины

**Что сделать:** Назови дочернюю корутину loader и выведи CoroutineName из её контекста. Затем запусти ребёнка с именем parser.

```kotlin
launch(CoroutineName("loader")) {
    event("Имя: ${currentCoroutineContext()[CoroutineName]?.name}")
    launch(CoroutineName("parser")) { event("Имя: ${currentCoroutineContext()[CoroutineName]?.name}") }
}.join()
```

**Почему:** CoroutineName — отдельный элемент контекста; имя корутины не равно имени потока.

**Ожидаемый результат:** Видны loader и parser.

[Вернуться к задаче](TASKS.md)

## 03.06 · Внедрить dispatcher

**Что сделать:** У функции вычисления сделай параметр CoroutineDispatcher. Выполни её на Default и на текущем диспетчере, выведи одинаковый результат.

```kotlin
suspend fun calculate(dispatcher: CoroutineDispatcher): Int = withContext(dispatcher) { event("Вычисление"); 6 * 7 }
val current = currentCoroutineContext()[ContinuationInterceptor] as CoroutineDispatcher
event("Default: ${calculate(Dispatchers.Default)}")
event("Текущий: ${calculate(current)}")
```

**Почему:** Внедрение диспетчера отделяет логику от среды выполнения и упрощает тестирование.

**Ожидаемый результат:** В обоих случаях 42, контекст выполнения может отличаться.

[Вернуться к задаче](TASKS.md)

## 03.07 · CPU-работа и ожидание

**Что сделать:** Добавь короткий ticker в вызывающем контексте, а серию вычислений запусти на Default. В журнале должны быть видны тики во время вычислений.

```kotlin
coroutineScope {
    launch { repeat(3) { delay(30); event("Тик $it") } }
    withContext(Dispatchers.Default) {
        var total = 0L
        repeat(3) { step -> repeat(2_000_000) { total += it }; event("Вычислен шаг $step"); delay(40) }
        event("Сумма: $total")
    }
}
```

**Почему:** Отдельный вычислительный dispatcher позволяет не занимать вызывающий поток. Задержка добавлена только для наглядности опыта.

**Ожидаемый результат:** Тики и вычислительные шаги могут чередоваться; всё завершается.

[Вернуться к задаче](TASKS.md)

## 03.08 · Ограничить параллельное исполнение

**Что сделать:** Создай представление Default с параллелизмом 2. Запусти шесть блоков, каждый занимает поток на 30 мс. Измерь максимум одновременно исполняемых блоков.

```kotlin
val dispatcher = Dispatchers.Default.limitedParallelism(2)
val active = AtomicInteger(); val peak = AtomicInteger()
coroutineScope { repeat(6) { id -> launch(dispatcher) {
    val n = active.incrementAndGet(); peak.accumulateAndGet(n, ::maxOf)
    try { event("Блок $id активных=$n"); Thread.sleep(30) } finally { active.decrementAndGet() }
} } }
event("Пик: ${peak.get()}")
checkResult("Не больше 2", peak.get() <= 2)
```

**Почему:** limitedParallelism ограничивает исполнение на потоках. Блокировка здесь лишь учебный способ удержать исполняемый блок; для suspend-операций в полёте нужен другой контроль.

**Ожидаемый результат:** Пик не превышает 2; конкретно 2 не гарантируется.

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

## 03.10 · Сервис с двумя диспетчерами

**Что сделать:** Добавь сервис и обработку списка вместо TODO, оставив временную переменную `answers`.

```kotlin
class Service(private val io: CoroutineDispatcher, private val cpu: CoroutineDispatcher) {
    suspend fun load(number: Int): Int {
        val input = withContext(io) { legacyRead(number) }
        return withContext(cpu) { double(input) }
    }
}
val service = Service(io, cpu)
answers = coroutineScope {
    inputs.map { number -> async { service.load(number) } }.awaitAll()
}
```

**Почему:** Сервис не выбирает исполнителей сам, а использует внедрённые зависимости. `withContext` гарантирует контекст для каждой стадии и возвращает её значение; одновременные вызовы сервиса сохраняют порядок ответов при сборе.

**Ожидаемый результат:** `[42, 26, 14]`, обе стадии каждого числа и две строки `OK`.

[Вернуться к задаче](TASKS.md)

## 03.11 · Наследование контекста

**Что сделать:** Добавь следующий код вместо TODO в `Task11.kt`.

```kotlin
withContext(Dispatchers.Default + CoroutineName("parent")) {
    val parentJob = coroutineContext[Job]!!
    val parentDispatcher = coroutineContext[ContinuationInterceptor]
    suspend fun observe(waitMs: Long) {
        val context = currentCoroutineContext()
        val job = context[Job]!!
        val name = context[CoroutineName]?.name.orEmpty()
        seen.add(name)
        if (job !== parentJob && parentJob.children.any { it === job }) {
            parentLinks.incrementAndGet()
        }
        if (context[ContinuationInterceptor] === parentDispatcher) {
            inheritedDispatchers.incrementAndGet()
        }
        event("Ребёнок $name: Job=$job")
        delay(waitMs)
        completed.incrementAndGet()
    }
    launch { observe(60) }
    launch(CoroutineName("child")) { observe(30) }
}
```

**Почему:** Дочерняя корутина получает новый `Job`, связанный с родительским, и наследует диспетчер. Новое `CoroutineName` заменяет только элемент имени. `withContext` возвращается после завершения обоих детей, поэтому итоговые счётчики уже окончательные.

**Ожидаемый результат:** Два имени, две связи с родителем, два наследования диспетчера и три строки `OK`.

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

