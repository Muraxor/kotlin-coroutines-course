# Решения: Общие данные: гонки, атомики и Mutex

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 06.01 · Найти гонку

**Что сделать:** Запусти 100 детей по 1000 increment на Default. Выведи фактическое и ожидаемое значение.

```kotlin
var counter=0
withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { counter++ } } } } }
event("Счётчик: $counter; ожидается 100000")
```

**Почему:** counter++ составной и не атомарен.

**Ожидаемый результат:** Фактический результат может быть меньше 100000; иногда гонка не проявится.

[Вернуться к задаче](TASKS.md)

## 06.02 · Атомарный счётчик

**Что сделать:** Исправь тот же эксперимент с AtomicInteger.

```kotlin
val counter=AtomicInteger()
withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { counter.incrementAndGet() } } } } }
event("Счётчик: ${counter.get()}"); checkResult("100000",counter.get()==100000)
```

**Почему:** AtomicInteger даёт атомарную операцию увеличения.

**Ожидаемый результат:** 100000 и OK.

[Вернуться к задаче](TASKS.md)

## 06.03 · Mutex для счётчика

**Что сделать:** Реши задачу счётчика через Mutex.withLock.

```kotlin
var counter=0; val mutex=Mutex()
withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { mutex.withLock { counter++ } } } } } }
event("Счётчик: $counter"); checkResult("100000",counter==100000)
```

**Почему:** Все записи защищены одной критической секцией.

**Ожидаемый результат:** 100000 и OK.

[Вернуться к задаче](TASKS.md)

## 06.04 · Защитить проверку и запись

**Что сделать:** Баланс 100. Запусти два конкурентных снятия по 80. Защити проверку и изменение одним Mutex; баланс не должен стать отрицательным.

```kotlin
var balance=100; val mutex=Mutex()
suspend fun withdraw(n:Int)=mutex.withLock { if (balance>=n) { balance-=n; true } else false }
val outcomes=coroutineScope { listOf(async(Dispatchers.Default){withdraw(80)},async(Dispatchers.Default){withdraw(80)}).awaitAll() }
event("Снятия: $outcomes; баланс: $balance"); checkResult("Ровно одно снятие", outcomes.count { it }==1 && balance==20)
```

**Почему:** Защита только записи не защищает инвариант между проверкой и изменением.

**Ожидаемый результат:** Одно успешное снятие; баланс 20.

[Вернуться к задаче](TASKS.md)

## 06.05 · Несколько прогонов

**Что сделать:** Повтори небезопасный счётчик три раза; выведи каждый результат, без требования воспроизвести гонку в каждом запуске.

```kotlin
repeat(3) { run -> var counter=0; withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { counter++ } } } } }; event("Прогон $run: $counter") }
```

**Почему:** Отсутствие ошибки в одном прогоне не доказывает потокобезопасность.

**Ожидаемый результат:** Три значения; могут отличаться или совпасть.

[Вернуться к задаче](TASKS.md)

## 06.06 · Перевод между счетами

**Что сделать:** Создай два баланса по 100. Выполни много переводов по 1 в обе стороны. Общая сумма всегда должна быть 200.

```kotlin
var a=100; var b=100; val mutex=Mutex()
suspend fun move(toB:Boolean)=mutex.withLock { if (toB && a>0) {a--;b++} else if (!toB && b>0) {b--;a++} }
coroutineScope { repeat(100) { launch(Dispatchers.Default) { move(it%2==0) } } }
event("A=$a, B=$b");checkResult("Сумма 200",a+b==200)
```

**Почему:** Одна критическая секция сохраняет инвариант двух переменных.

**Ожидаемый результат:** Сумма 200.

[Вернуться к задаче](TASKS.md)

## 06.07 · Без общего счётчика

**Что сделать:** Каждый из 100 детей возвращает локальную сумму 1000 единиц. Родитель собирает общий итог.

```kotlin
val parts=coroutineScope { (1..100).map { async(Dispatchers.Default) { var local=0; repeat(1000) { local++ }; local } }.awaitAll() }
event("Итог: ${parts.sum()}");checkResult("100000",parts.sum()==100000)
```

**Почему:** Локальное состояние не разделяется между детьми.

**Ожидаемый результат:** 100000 без блокировок.

[Вернуться к задаче](TASKS.md)

## 06.08 · Почему volatile не помогает

**Что сделать:** Запусти счётчик с volatile-полем и большим числом инкрементов. Сравни с ожидаемым; объясни в решении составную операцию.

```kotlin
val box=object { @Volatile var value=0 }
withContext(Dispatchers.Default) { coroutineScope { repeat(100) { launch { repeat(1000) { box.value++ } } } } }
event("volatile: ${box.value}; ожидается 100000")
```

**Почему:** Видимость отдельного чтения/записи не даёт атомарности read-modify-write.

**Ожидаемый результат:** Значение может быть меньше ожидаемого.

[Вернуться к задаче](TASKS.md)

## 06.09 · Склад

**Что сделать:** С нуля реализуй склад с остатком 10. Два клиента одновременно резервируют по 7; успешным должен стать ровно один.

```kotlin
class Stock { private val mutex=Mutex(); private var count=10; suspend fun reserve(n:Int)=mutex.withLock { if(count<n) false else { count-=n; true } }; suspend fun left()=mutex.withLock { count } }
val stock=Stock(); val orders=coroutineScope { listOf(async(Dispatchers.Default){stock.reserve(7)},async(Dispatchers.Default){stock.reserve(7)}).awaitAll() }
event("Заказы: $orders, остаток: ${stock.left()}");checkResult("Инвариант склада",orders.count{it}==1 && stock.left()==3)
```

**Почему:** Проверка и изменение остатка должны быть атомарны как единая операция.

**Ожидаемый результат:** Одно бронирование, остаток 3.

[Вернуться к задаче](TASKS.md)

## 06.10 · Кеш

**Что сделать:** С нуля создай кеш Map<Int,String> с Mutex. Два запроса одного id должны вернуть одинаковое значение и не повредить map.

```kotlin
val mutex=Mutex();val cache=mutableMapOf<Int,String>()
suspend fun get(id:Int):String=mutex.withLock { cache.getOrPut(id) { "item$id" } }
val values=coroutineScope { (1..2).map { async(Dispatchers.Default){ get(7) } }.awaitAll() }
event("$values; размер=${cache.size}");checkResult("Один ключ",cache.size==1)
```

**Почему:** Mutex защищает согласованность mutable map; здесь получение целиком внутри lock.

**Ожидаемый результат:** Оба item7, размер 1.

[Вернуться к задаче](TASKS.md)

## 06.11 · Стресс-проверка склада

**Что сделать:** Выполни 100 конкурентных резервирований по 1 из 50 единиц. Проверь, что успешных ровно 50, остаток 0.

```kotlin
var left=50;val mutex=Mutex()
suspend fun reserve()=mutex.withLock { if(left>0){left--;true}else false }
val answers=coroutineScope { (1..100).map { async(Dispatchers.Default){reserve()} }.awaitAll() }
event("Успешных: ${answers.count{it}}, остаток: $left");checkResult("Инвариант",answers.count{it}==50 && left==0)
```

**Почему:** Стресс-тест проверяет инвариант независимо от порядка выполнения.

**Ожидаемый результат:** 50 успешных, остаток 0.

[Вернуться к задаче](TASKS.md)

## 06.12 · Бронирование мест

**Что сделать:** Вставь фрагмент вместо TODO в `Task12.kt`, оставив временную переменную `sales`. Остальные данные и проверки уже есть в шаблоне.

```kotlin
suspend fun buy(request: Pair<String, Int>): Pair<String, Int> = mutex.withLock {
    val (name, count) = request
    if (available >= count) {
        available -= count
        name to count
    } else {
        name to 0
    }
}
sales = coroutineScope {
    requests.map { request ->
        async(Dispatchers.Default) { buy(request) }
    }.awaitAll()
}
```

**Почему:** Проверка остатка и уменьшение происходят под одним замком, поэтому между ними другая заявка не может изменить доступное число мест. Результат каждой заявки хранит её имя и фактически купленное число. `awaitAll()` сохраняет порядок списка заявок, хотя победители зависят от расписания.

**Ожидаемый результат:** Пять решений; продано четыре места, остаток ноль, две строки `OK`.

[Вернуться к задаче](TASKS.md)

