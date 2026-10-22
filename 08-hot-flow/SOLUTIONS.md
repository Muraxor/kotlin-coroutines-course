# Решения: StateFlow и SharedFlow

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 08.01 · Текущее состояние

**Что сделать:** Создай MutableStateFlow(0), установи 1 и прочитай value.

```kotlin
val state=MutableStateFlow(0);state.value=1;event("Состояние: ${state.value}")
```

**Почему:** StateFlow хранит одно актуальное значение.

**Ожидаемый результат:** Состояние: 1.

[Вернуться к задаче](TASKS.md)

## 08.02 · Атомарное обновление

**Что сделать:** Увеличь MutableStateFlow на 1 через update.

```kotlin
val state=MutableStateFlow(0);state.update {it+1};event("Состояние: ${state.value}")
```

**Почему:** update безопасен для конкурентного обновления.

**Ожидаемый результат:** Состояние: 1.

[Вернуться к задаче](TASKS.md)

## 08.03 · Скрыть запись

**Что сделать:** В классе счётчика оставь mutable-состояние приватным, наружу выставь StateFlow. Выведи состояние до и после increment.

```kotlin
class Counter {private val mutable=MutableStateFlow(0);val state: StateFlow<Int> = mutable.asStateFlow();fun increment(){mutable.update{it+1}}}
val c=Counter();event("До: ${c.state.value}");c.increment();event("После: ${c.state.value}")
```

**Почему:** Потребитель наблюдает состояние, но не может присвоить новое значение.

**Ожидаемый результат:** До: 0; После: 1.

[Вернуться к задаче](TASKS.md)

## 08.04 · Завершить сбор StateFlow

**Что сделать:** Подпишись, получи 0 и 1, затем заверши сбор через take(2). Обеспечь регистрацию подписчика до изменения значения.

```kotlin
val state=MutableStateFlow(0)
val job=launch(start=CoroutineStart.UNDISPATCHED) {state.take(2).collect{event("Получено $it")}}
state.value=1;job.join()
```

**Почему:** StateFlow сам не завершится; старт подписчика и ограничение количества делают опыт детерминированным.

**Ожидаемый результат:** Получено 0 и 1; программа завершается.

[Вернуться к задаче](TASKS.md)

## 08.05 · Равные значения

**Что сделать:** Отправь в StateFlow значение 0 повторно, затем 1. Собери два разных значения и выведи количество.

```kotlin
val state=MutableStateFlow(0);val received=mutableListOf<Int>()
val job=launch(start=CoroutineStart.UNDISPATCHED){state.take(2).collect{received+=it;event("Получено $it")}}
state.value=0;state.value=1;job.join();event("Количество: ${received.size}")
```

**Почему:** StateFlow подавляет значение, равное текущему по equals.

**Ожидаемый результат:** 0,1; количество 2.

[Вернуться к задаче](TASKS.md)

## 08.06 · Поздний подписчик

**Что сделать:** Сравни SharedFlow replay=0 и replay=1: отправь событие до подписки и проверь, что видит новый подписчик.

```kotlin
val zero=MutableSharedFlow<Int>(replay=0);val one=MutableSharedFlow<Int>(replay=1)
zero.emit(42);one.emit(42)
event("Без replay: ${zero.replayCache}");event("С replay: ${one.first()}")
```

**Почему:** replay задаёт сохранённую историю для новых подписчиков.

**Ожидаемый результат:** Без replay: []; С replay: 42.

[Вернуться к задаче](TASKS.md)

## 08.07 · Два подписчика

**Что сделать:** Создай двух подписчиков SharedFlow. Дождись их запуска перед emit(7); каждый должен увидеть 7.

```kotlin
val bus=MutableSharedFlow<Int>();val a=launch(start=CoroutineStart.UNDISPATCHED){event("A: ${bus.first()}")};val b=launch(start=CoroutineStart.UNDISPATCHED){event("B: ${bus.first()}")}
bus.emit(7);joinAll(a,b)
```

**Почему:** SharedFlow рассылает значение активным подписчикам.

**Ожидаемый результат:** A:7 и B:7.

[Вернуться к задаче](TASKS.md)

## 08.08 · Разделить холодный источник

**Что сделать:** Преобразуй холодный flow в StateFlow с начальным -1, покажи текущее значение после завершения источника. Используй scope задачи.

```kotlin
val source=flow {delay(30);emit(42)}
coroutineScope {val state=source.stateIn(this,SharingStarted.Eagerly,-1);event("Сначала ${state.value}");event("Потом ${state.first{it==42}}")}
```

**Почему:** stateIn запускает разделяемый источник в указанном scope.

**Ожидаемый результат:** Сначала -1; Потом 42.

[Вернуться к задаче](TASKS.md)

## 08.09 · Состояния загрузки

**Что сделать:** С нуля выведи переходы Idle→Loading→Success через StateFlow. Собери ровно три состояния.

```kotlin
val state=MutableStateFlow("Idle");val job=launch(start=CoroutineStart.UNDISPATCHED){state.take(3).collect{event(it)}}
state.value="Loading";yield();state.value="Success";job.join()
```

**Почему:** StateFlow представляет текущую модель; между обновлениями нужен шанс подписчику обработать промежуточное состояние.

**Ожидаемый результат:** Idle, Loading, Success.

[Вернуться к задаче](TASKS.md)

## 08.10 · Состояние и уведомление

**Что сделать:** С нуля создай StateFlow состояния и SharedFlow уведомлений с replay=1. Поздний подписчик должен увидеть последнее уведомление.

```kotlin
val state=MutableStateFlow("Idle");val notices=MutableSharedFlow<String>(replay=1)
state.value="Ready";notices.emit("Готово")
event("Состояние: ${state.value}");event("Уведомление: ${notices.first()}")
```

**Почему:** Для текущего состояния и событий используются разные контракты хранения.

**Ожидаемый результат:** Ready и Готово.

[Вернуться к задаче](TASKS.md)

## 08.11 · Конкурентные обновления

**Что сделать:** Сделай 1000 конкурентных update { it+1 } и прочитай итог. Не требуй увидеть все промежуточные состояния подписчиком.

```kotlin
val state=MutableStateFlow(0);coroutineScope {repeat(1000){launch(Dispatchers.Default){state.update{it+1}}}}
event("Итог: ${state.value}");checkResult("1000",state.value==1000)
```

**Почему:** update атомарен; StateFlow конфлирует быстрые промежуточные значения.

**Ожидаемый результат:** Итог: 1000.

[Вернуться к задаче](TASKS.md)

## 08.12 · Поиск со сменой запроса и ошибкой

**Что сделать:** Добавь следующий код вместо TODO в `Task12.kt`. Остальные данные и проверки уже есть в шаблоне.

```kotlin
requests.collectLatest { query ->
    state.value = "Loading $query"
    try {
        state.value = search(query)
        completed += query
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        failed += query
        state.value = "Error $query"
    }
}
lateSeen += state.first()
```

**Почему:** `collectLatest` отменяет обработку A при появлении B. Отмена должна пройти дальше как сигнал управления, иначе A ошибочно попадёт в `failed`. Ошибка B обрабатывается внутри своей операции, поэтому C всё равно будет принят. Новый подписчик StateFlow сразу получает текущее значение `Result C`.

**Ожидаемый результат:** Завершён только C, B записан как ошибка, поздний подписчик видит `Result C`; три строки `OK`.

[Вернуться к задаче](TASKS.md)

