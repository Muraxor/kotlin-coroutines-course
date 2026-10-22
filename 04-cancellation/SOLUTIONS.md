# Решения: Отмена, таймауты и очистка

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 04.01 · Остановить тики

**Что сделать:** После 250 мс отмени задачу и дождись её окончания.

```kotlin
val job = launch { repeat(20) { delay(100); event("Тик $it") } }
delay(250)
job.cancelAndJoin()
event("Остановлено")
```

**Почему:** cancelAndJoin гарантирует окончание отменяемой задачи.

**Ожидаемый результат:** После «Остановлено» нет тиков.

[Вернуться к задаче](TASKS.md)

## 04.02 · Очистка при отмене

**Что сделать:** Добавь finally и покажи, что очистка происходит до возврата cancelAndJoin.

```kotlin
val job = launch { try { repeat(20) { delay(100) } } finally { event("Ресурс освобождён") } }
delay(50); job.cancelAndJoin(); event("Остановка завершена")
```

**Почему:** finally выполняется и при отмене.

**Ожидаемый результат:** Очистка раньше сообщения об остановке.

[Вернуться к задаче](TASKS.md)

## 04.03 · Мягкий таймаут

**Что сделать:** Запусти работу 500 мс с лимитом 100 мс; напечатай результат null.

```kotlin
val result = withTimeoutOrNull(100) { delay(500); "ready" }
event("Результат: $result")
checkResult("Таймаут вернул null", result == null)
```

**Почему:** withTimeoutOrNull превращает истечение собственного срока в null.

**Ожидаемый результат:** Результат: null и OK.

[Вернуться к задаче](TASKS.md)

## 04.04 · Не проглотить отмену

**Что сделать:** Исправь catch(Exception): он должен повторно выбрасывать CancellationException.

```kotlin
val job = launch {
    try { delay(500) }
    catch (e: Exception) { if (e is CancellationException) throw e; event("Ошибка: $e") }
    finally { event("finally") }
}
delay(50); job.cancelAndJoin(); event("Остановка")
```

**Почему:** CancellationException передаёт сигнал по дереву корутин.

**Ожидаемый результат:** finally и остановка; отмена не превращается в обычную ошибку.

[Вернуться к задаче](TASKS.md)

## 04.05 · Цикл без delay

**Что сделать:** Сделай длительный вычислительный цикл отзывчивым к отмене через ensureActive.

```kotlin
val job = launch(Dispatchers.Default) { var n = 0L; while (true) { if (n++ % 10_000 == 0L) currentCoroutineContext().ensureActive() } }
delay(30); job.cancelAndJoin(); event("Цикл остановлен")
```

**Почему:** Вычисление без точек приостановки должно явно проверять отмену.

**Ожидаемый результат:** Программа быстро заканчивается.

[Вернуться к задаче](TASKS.md)

## 04.06 · cancel против cancelAndJoin

**Что сделать:** Сравни событие сразу после cancel с событием после join.

```kotlin
val job = launch { try { delay(500) } finally { delay(50); event("finally завершился") } }
delay(30); job.cancel(); event("После cancel"); job.join(); event("После join")
```

**Почему:** cancel запрашивает отмену, join ждёт финального состояния.

**Ожидаемый результат:** «После join» следует за finally; после cancel очистка может ещё идти.

[Вернуться к задаче](TASKS.md)

## 04.07 · Отменить дерево

**Что сделать:** Запусти родителя с двумя детьми и отмени его. У каждого ребёнка должно появиться сообщение finally.

```kotlin
val parent = launch { repeat(2) { id -> launch { try { delay(500) } finally { event("Ребёнок $id завершён") } } } }
delay(50); parent.cancelAndJoin(); event("Всё дерево остановлено")
```

**Почему:** Отмена родителя распространяется по структурированному дереву.

**Ожидаемый результат:** Два сообщения детей до итогового.

[Вернуться к задаче](TASKS.md)

## 04.08 · Приостанавливающая очистка

**Что сделать:** Добавь короткую suspend-очистку на 50 мс внутри NonCancellable.

```kotlin
val job = launch { try { delay(500) } finally { withContext(NonCancellable) { delay(50); event("Очистка выполнена") } } }
delay(30); job.cancelAndJoin(); event("join вернулся")
```

**Почему:** Обычный отменённый контекст не позволяет приостанавливать очистку.

**Ожидаемый результат:** Очистка раньше возврата join.

[Вернуться к задаче](TASKS.md)

## 04.09 · Поиск с таймаутом

**Что сделать:** С нуля имитируй поиск; для лимита 100 мс верни null, для 300 мс — строку «найдено».

```kotlin
suspend fun search(limit: Long): String? = withTimeoutOrNull(limit) { delay(150); "найдено" }
event("100: ${search(100)}"); event("300: ${search(300)}")
```

**Почему:** Лимит окружает всю операцию, а не отдельный её шаг.

**Ожидаемый результат:** 100: null; 300: найдено.

[Вернуться к задаче](TASKS.md)

## 04.10 · Отменяемая обработка

**Что сделать:** С нуля обработай большой список на Default; проверь отмену внутри цикла. Прерви обработку через 30 мс.

```kotlin
val job = launch(Dispatchers.Default) { var total=0L; for (i in 0..Int.MAX_VALUE) { if (i % 10_000 == 0) currentCoroutineContext().ensureActive(); total += i }; event("$total") }
delay(30); job.cancelAndJoin(); event("Обработка остановлена")
```

**Почему:** Кооперативная отмена нужна и CPU-коду.

**Ожидаемый результат:** Работа останавливается; полная сумма обычно не выводится.

[Вернуться к задаче](TASKS.md)

## 04.11 · Три исхода

**Что сделать:** Покажи успех, собственный таймаут и внешнюю отмену отдельными вызовами.

```kotlin
suspend fun work() { delay(100); event("Работа готова") }
withTimeout(200) { work() }
event("Таймаут: ${withTimeoutOrNull(30) { work(); "ok" }}")
val job = launch { work() }; delay(20); job.cancelAndJoin(); event("Внешняя отмена")
```

**Почему:** Разные причины завершения требуют разной обработки.

**Ожидаемый результат:** Первый вызов успешен, второй null, третий отменён.

[Вернуться к задаче](TASKS.md)

## 04.12 · Отмена сессии и гарантированная очистка

**Что сделать:** Добавь этот фрагмент вместо TODO в `Task12.kt`.

```kotlin
val session = launch {
    try {
        coroutineScope {
            repeat(2) { id ->
                launch {
                    try {
                        delay(500)
                    } finally {
                        withContext(NonCancellable) {
                            delay(20)
                            childrenClosed.incrementAndGet()
                            cleanupOrder.add("child$id")
                            event("Ребёнок $id очищен")
                        }
                    }
                }
            }
        }
    } finally {
        withContext(NonCancellable + Dispatchers.IO) {
            closeResource()
        }
    }
}
delay(40)
session.cancelAndJoin()
```

**Почему:** Оба ребёнка принадлежат сессии, поэтому отмена доходит до них. `NonCancellable` даёт их приостанавливающей очистке закончиться. Родительская `coroutineScope` ждёт детей, и лишь затем внешний `finally` закрывает ресурс. `Dispatchers.IO` уводит блокирующее закрытие с вызывающего потока; `cancelAndJoin` ждёт весь порядок до итоговой проверки.

**Ожидаемый результат:** Сначала `child0` и `child1` в любом порядке, затем `resource` и три строки `OK`.

[Вернуться к задаче](TASKS.md)

