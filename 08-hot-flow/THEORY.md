# StateFlow и SharedFlow

**Источник:** [официальная документация Kotlin / JetBrains](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/). Ниже — выборочный учебный перевод и объяснение ключевых положений главы, а не полный текст документации. Примеры адаптированы для задач этого проекта. Чтобы сверить детали поведения, перейди по ссылкам у каждого раздела.

## StateFlow как состояние

StateFlow — горячий поток с обязательным начальным и всегда доступным текущим значением value. Новый подписчик получает последнее состояние. Одинаковые по equals обновления подавляются, а медленный подписчик может пропустить промежуточные версии. Поэтому StateFlow подходит для модели экрана, но не для журнала каждого отдельного события.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

## Атомарное update

Если несколько корутин меняют счётчик, используй update { old -> old + 1 }, а не отдельное чтение value и присваивание. update выполняется атомарно. Лямбда может вычисляться повторно при конкуренции, поэтому не помещай в неё отправку сообщений или другие внешние побочные эффекты. Публично отдавай asStateFlow, оставляя запись внутри владельца.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-mutable-state-flow/).

## SharedFlow как рассылка

SharedFlow рассылает элементы активным подписчикам. replay задаёт, сколько прошлых элементов получит новый подписчик. Если replay=0 и получателей нет, событие не сохраняется. Это важно для интерфейсных уведомлений: отдельно реши, нужно ли позднему подписчику увидеть прошлое событие.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-shared-flow/).

## Преобразование холодного потока

stateIn и shareIn делают из холодного источника разделяемый горячий поток. Для этого им нужен scope-владелец и политика SharingStarted: запуск сразу, при первом подписчике или пока есть подписчики. Такой поток живёт по сроку scope, поэтому учебный collect следует ограничивать take или отменой.

[Официальный раздел](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/state-in.html).

## Разобранный пример

```kotlin
private val _state = MutableStateFlow(0)
val state: StateFlow<Int> = _state.asStateFlow()
fun increment() { _state.update { it + 1 } }
```

После increment текущее значение state увеличится на 1; подписчик может пропустить промежуточные обновления.

## Как проверить себя в задачах

Открой [задачи](TASKS.md) и запускай каждую отдельно. В журнале смотри на порядок событий, фактический результат и имя потока. Сравнивай только гарантированный порядок: завершение ребёнка до выхода из его scope, результат после await, cleanup до cancelAndJoin. Если поведение удивило, обсудим конкретный вывод в чате.
