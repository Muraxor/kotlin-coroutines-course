# Тестирование и итоговый проект

**Источник:** [официальная документация Kotlin / JetBrains](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/). Ниже — выборочный учебный перевод и объяснение ключевых положений главы, а не полный текст документации. Примеры адаптированы для задач этого проекта. Чтобы сверить детали поведения, перейди по ссылкам у каждого раздела.

## runTest

runTest создаёт тестовый scope и разрешает вызывать suspend-код прямо внутри него. Задержки на тестовом планировщике проходят виртуально, поэтому тест с delay(1000) может закончиться почти сразу. Для консольных упражнений этого модуля библиотека test добавлена и в runtime; в обычном приложении она нужна только в test-зависимостях.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#runtest-api).

## Управление временем

runCurrent выполняет задачи, поставленные на текущую отметку. advanceTimeBy двигает виртуальное время; работу ровно на новой границе запускает следующий runCurrent. advanceUntilIdle выполняет оставшиеся конечные задачи. Бесконечный источник не следует ждать до idle — его нужно отменить или разместить в backgroundScope теста.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#controlling-the-virtual-time).

## Внедрение dispatcher

Жёстко заданные Dispatchers.Default и IO не подчиняются автоматически виртуальному времени теста. Передавай CoroutineDispatcher в код как зависимость там, где тесту нужно управлять расписанием. Подставь StandardTestDispatcher(testScheduler) и проверяй время без Thread.sleep.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#virtual-time-support-with-other-dispatchers).

## Пределы детерминированного теста

runTest обычно выполняет дочерние задачи на одном тестовом потоке. Он хорошо проверяет отмену, порядок зависимых событий, результаты и таймауты, но не доказывает отсутствие гонок на нескольких потоках. Для общего изменяемого состояния отдельно запускай конкурентные сценарии на Default и проверяй инварианты.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#launch-and-async).

## Разобранный пример

```kotlin
@Test
fun timeout() = runTest {
    val result = withTimeoutOrNull(100) { delay(500); 42 }
    assertNull(result)
    assertEquals(100, currentTime)
}
```

Тест проходит, не ожидая 500 реальных миллисекунд.

## Как проверить себя в задачах

Открой [задачи](TASKS.md) и запускай каждую отдельно. В журнале смотри на порядок событий, фактический результат и имя потока. Сравнивай только гарантированный порядок: завершение ребёнка до выхода из его scope, результат после await, cleanup до cancelAndJoin. Если поведение удивило, обсудим конкретный вывод в чате.
