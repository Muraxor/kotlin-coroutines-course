# Решения: Structured concurrency и ошибки

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 05.01 · Ошибка области

**Что сделать:** Оберни падающий coroutineScope во внешний try/catch и выведи ошибку.

```kotlin
try { coroutineScope { launch { delay(30); error("сбой") } } }
catch (e: IllegalStateException) { event("Поймано: ${e.message}") }
```

**Почему:** Ошибка ребёнка завершает обычный scope ошибкой.

**Ожидаемый результат:** Поймано: сбой.

[Вернуться к задаче](TASKS.md)

## 05.02 · Сосед отменён

**Что сделать:** Второму ребёнку добавь finally; проверь, что он отменяется при падении первого.

```kotlin
try { coroutineScope {
    launch { delay(30); error("сбой") }
    launch { try { delay(500) } finally { event("Сосед завершён") } }
} } catch (e: IllegalStateException) { event("Родитель получил ошибку") }
```

**Почему:** Обычное дерево отменяет соседей при неисправимой ошибке ребёнка.

**Ожидаемый результат:** finally соседа до сообщения родителя.

[Вернуться к задаче](TASKS.md)

## 05.03 · Независимые дети

**Что сделать:** Замени обычную область на supervisorScope и обработай ошибку внутри первого ребёнка.

```kotlin
supervisorScope {
    launch { try { delay(30); error("сбой") } catch (e: IllegalStateException) { event("Ошибка первого") } }
    launch { delay(80); event("Второй завершён") }
}
```

**Почему:** supervisorScope изолирует сбой детей, но обработку ошибки должен выполнить код.

**Ожидаемый результат:** Оба сообщения, второй не отменён.

[Вернуться к задаче](TASKS.md)

## 05.04 · Наблюдать ошибку Deferred

**Что сделать:** В supervisorScope дождись падающего async и явно обработай исключение.

```kotlin
supervisorScope {
    val value = async<Int> { error("нет данных") }
    try { value.await() } catch (e: IllegalStateException) { event("await получил: ${e.message}") }
}
```

**Почему:** async хранит ошибку в Deferred до наблюдения через await.

**Ожидаемый результат:** В консоли причина нет данных.

[Вернуться к задаче](TASKS.md)

## 05.05 · Результат каждого источника

**Что сделать:** Для трёх источников верни Result<Int>; один источник падает, остальные успешны.

```kotlin
val values = supervisorScope { (1..3).map { n -> async { runCatching { delay(30); if (n==2) error("сбой"); n*10 } } }.awaitAll() }
values.forEachIndexed { i, r -> event("${i+1}: ${r.fold({ "успех $it" }, { "ошибка" })}") }
```

**Почему:** Ошибка становится данными результата только по явно выбранному контракту; внешнюю отмену ловить таким способом нельзя.

**Ожидаемый результат:** Успех 10, ошибка, успех 30.

[Вернуться к задаче](TASKS.md)

## 05.06 · Сравнить области

**Что сделать:** Запусти одинаковые две задачи: одна падает, другая задерживается. Сравни обычный scope и supervisorScope.

```kotlin
try { coroutineScope { launch { delay(20); error("x") }; launch { try { delay(70); event("Обычный сосед дошёл") } finally { event("Обычный сосед завершён") } } } } catch (_: IllegalStateException) { event("Обычный scope упал") }
supervisorScope { launch { try { delay(20); error("x") } catch (_: IllegalStateException) { event("Ошибка изолирована") } }; launch { delay(70); event("Supervised сосед дошёл") } }
```

**Почему:** Supervision меняет распространение отказа, а не работу планировщика.

**Ожидаемый результат:** В первом опыте сосед отменён; во втором дошёл до конца.

[Вернуться к задаче](TASKS.md)

## 05.07 · Три карточки

**Что сделать:** Загрузи три карточки, средняя падает. Сохрани результаты двух успешных.

```kotlin
val cards = supervisorScope { (1..3).map { n -> async { try { delay(20); if (n==2) error("x"); "card$n" } catch (e: IllegalStateException) { "error$n" } } }.awaitAll() }
event("$cards")
```

**Почему:** Для независимых карточек ошибка одной может остаться локальной.

**Ожидаемый результат:** [card1, error2, card3].

[Вернуться к задаче](TASKS.md)

## 05.08 · Обязательный и необязательные

**Что сделать:** Профиль обязателен; рекомендации и баннер необязательны. При сбое рекомендаций профиль и баннер должны остаться.

```kotlin
val screen = supervisorScope {
    val profile = async { delay(20); "Анна" }
    val recs = async { try { delay(20); error("x") } catch (_: IllegalStateException) { "нет рекомендаций" } }
    val banner = async { delay(20); "баннер" }
    "${profile.await()}, ${recs.await()}, ${banner.await()}"
}
event(screen)
```

**Почему:** Необязательные ошибки обрабатываются внутри отдельных детей.

**Ожидаемый результат:** Анна, нет рекомендаций, баннер.

[Вернуться к задаче](TASKS.md)

## 05.09 · Отказ обязательного источника

**Что сделать:** С нуля собери экран так, чтобы ошибка обязательного профиля завершала всю функцию и отменяла остальные запросы.

```kotlin
try { coroutineScope {
    val profile = async<String> { delay(30); error("профиль недоступен") }
    val orders = async { try { delay(500); 3 } finally { event("Заказы остановлены") } }
    event("${profile.await()}: ${orders.await()}")
} } catch (e: IllegalStateException) { event("Экран не загружен: ${e.message}") }
```

**Почему:** Обязательная зависимость требует общего отказа.

**Ожидаемый результат:** Заказы остановлены, затем причина отказа экрана.

[Вернуться к задаче](TASKS.md)

## 05.10 · Частичные данные

**Что сделать:** С нуля реализуй загрузку трёх карточек с результатом по каждой и продолжением после одной ошибки.

```kotlin
val result = supervisorScope { listOf("A", "B", "C").map { name -> async { try { delay(20); if (name=="B") error("x"); "$name:OK" } catch (_: IllegalStateException) { "$name:ERROR" } } }.awaitAll() }
event("Карточки: $result")
```

**Почему:** Изоляция отказа позволяет показать частичные данные.

**Ожидаемый результат:** [A:OK, B:ERROR, C:OK].

[Вернуться к задаче](TASKS.md)

## 05.11 · Отмена выше обработки ошибок

**Что сделать:** Проверь внешнюю отмену supervised группы, в которой ошибка карточки обрабатывается. Отмена должна завершить всю группу.

```kotlin
val job = launch { supervisorScope { repeat(2) { id -> launch { try { delay(500) } catch (e: CancellationException) { event("Отмена $id"); throw e } } } } }
delay(30); job.cancelAndJoin(); event("Группа завершена")
```

**Почему:** Нельзя превращать CancellationException в обычную локальную ошибку.

**Ожидаемый результат:** Две отмены до финального сообщения.

[Вернуться к задаче](TASKS.md)

## 05.12 · Обязательный источник и отмена соседей

**Что сделать:** Добавь следующий код вместо TODO в `Task12.kt`, оставив временные переменные моделей.

```kotlin
suspend fun loadScreen(profileFails: Boolean): List<String>? = supervisorScope {
    val profileResult = async { profile(profileFails) }
    val newsResult = async {
        try {
            news()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            "news=empty"
        }
    }
    val offersResult = async { offers() }

    val user = try {
        profileResult.await()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        newsResult.cancel()
        offersResult.cancelAndJoin()
        return@supervisorScope null
    }
    listOf(user, newsResult.await(), offersResult.await())
}
goodModel = loadScreen(profileFails = false)
event("Сценарий 2: ошибка обязательного профиля")
failedModel = loadScreen(profileFails = true)
```

**Почему:** Внутри `supervisorScope` сбой одного источника не отменяет остальные автоматически. Ошибку необязательных новостей можно заменить значением, но отмену надо пропустить дальше. После сбоя обязательного профиля оставшиеся операции отменяются явно; ожидание предложений подтверждает, что очистка завершилась до результата.

**Ожидаемый результат:** Первый экран собран, второй равен `null`, предложения отменены, три строки `OK`.

[Вернуться к задаче](TASKS.md)

