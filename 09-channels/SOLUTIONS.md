# Решения: Channel, очереди и ограничение нагрузки

Открывай соответствующий пункт после своей попытки. Код ниже вставляется **внутрь `runExercise { ... }`** файла `src/main/kotlin/course/TaskNN.kt`, заменяя заготовку. Общие импорты уже есть. После замены выполни команду задачи и сравни консоль с описанием результата. Это один из возможных вариантов решения.

## 09.01 · Передать одно значение

**Что сделать:** Отправь 42 из producer и получи его в consumer.

```kotlin
val channel=Channel<Int>();launch{channel.send(42)};event("Получено: ${channel.receive()}")
```

**Почему:** Rendezvous Channel связывает отправителя и получателя.

**Ожидаемый результат:** Получено: 42.

[Вернуться к задаче](TASKS.md)

## 09.02 · Закрыть и перебрать

**Что сделать:** Отправь 0,1,2, затем закрой Channel и прочитай все элементы через for.

```kotlin
val channel=Channel<Int>();launch{repeat(3){channel.send(it)};channel.close()}
for(n in channel) event("Получено $n")
```

**Почему:** close позволяет дочитать буфер и сообщает об окончании потока.

**Ожидаемый результат:** 0,1,2 и завершение цикла.

[Вернуться к задаче](TASKS.md)

## 09.03 · Исправить зависание

**Что сделать:** Готовый for ожидает новые элементы вечно. Добавь закрытие после отправки.

```kotlin
val channel=Channel<Int>();launch{repeat(3){channel.send(it)};channel.close()};for(n in channel)event("$n")
```

**Почему:** Итератору нужен сигнал завершения.

**Ожидаемый результат:** 0,1,2 и окончание программы.

[Вернуться к задаче](TASKS.md)

## 09.04 · Ограниченный буфер

**Что сделать:** Создай Channel capacity=2. Быстрый отправитель посылает пять чисел, медленный получатель обрабатывает каждое 80 мс. Выведи события до и после send.

```kotlin
val channel=Channel<Int>(2);launch{repeat(5){event("Посылаю $it");channel.send(it);event("Послано $it")};channel.close()}
for(n in channel){delay(80);event("Принято $n")}
```

**Почему:** Заполненный ограниченный буфер создаёт обратное давление.

**Ожидаемый результат:** Не все send завершаются сразу; все пять значений получены.

[Вернуться к задаче](TASKS.md)

## 09.05 · Два работника

**Что сделать:** Отправь шесть заданий в один Channel; два работника читают его и выводят id каждого.

```kotlin
val jobs=Channel<Int>();coroutineScope {repeat(2){worker->launch{for(id in jobs){delay(20);event("Работник $worker: $id")}}};launch{repeat(6){jobs.send(it)};jobs.close()}}
```

**Почему:** Несколько получателей распределяют задания, а не получают каждое все вместе.

**Ожидаемый результат:** Каждый id от 0 до 5 встречается один раз.

[Вернуться к задаче](TASKS.md)

## 09.06 · Закрытие при ошибке

**Что сделать:** Producer падает после двух элементов. В finally закрой канал с причиной, а consumer обработай ошибку и напечатай её.

```kotlin
val channel=Channel<Int>();launch{try{channel.send(1);channel.send(2);error("producer failed")}catch(e:IllegalStateException){channel.close(e)}}
try{for(n in channel)event("$n")}catch(e:IllegalStateException){event("Причина: ${e.message}")}
```

**Почему:** Закрытие с причиной сообщает получателю об отказе производителя.

**Ожидаемый результат:** 1,2, затем причина producer failed.

[Вернуться к задаче](TASKS.md)

## 09.07 · Не больше трёх операций

**Что сделать:** Запусти 10 операций по 60 мс через Semaphore(3). Посчитай максимальное число одновременно начатых.

```kotlin
val sem=Semaphore(3);val active=AtomicInteger();val peak=AtomicInteger()
coroutineScope{repeat(10){id->launch{sem.withPermit{val n=active.incrementAndGet();peak.accumulateAndGet(n,::maxOf);try{event("Старт $id, активных=$n");delay(60)}finally{active.decrementAndGet()}}}}}
event("Пик: ${peak.get()}");checkResult("Не больше 3",peak.get()<=3)
```

**Почему:** Semaphore удерживает permit на протяжении suspend-операции.

**Ожидаемый результат:** Пик не больше 3; все десять завершены.

[Вернуться к задаче](TASKS.md)

## 09.08 · Отменить producer и workers

**Что сделать:** Оберни producer и двух workers в родительскую задачу. Через 80 мс отмени её; каждый ребёнок должен вывести finally.

```kotlin
val parent=launch{val jobs=Channel<Int>();launch{try{var n=0;while(true){jobs.send(n++);delay(20)}}finally{jobs.cancel();event("Producer закрыт")}};repeat(2){id->launch{try{for(n in jobs)delay(40)}finally{event("Worker $id закрыт")}}}}
delay(80);parent.cancelAndJoin();event("Все остановлены")
```

**Почему:** Отмена родителя и закрытие канала завершают конвейер.

**Ожидаемый результат:** Три cleanup до итогового сообщения.

[Вернуться к задаче](TASKS.md)

## 09.09 · Очередь из ста заданий

**Что сделать:** С нуля обработай id 1..100 четырьмя workers. В успешном прогоне каждое id должно попасть в результат один раз.

```kotlin
val jobs=Channel<Int>(10);val results=Channel<Int>(100)
coroutineScope{repeat(4){launch{for(id in jobs){delay(1);results.send(id)}}};launch{for(id in 1..100)jobs.send(id);jobs.close()}}
results.close();val values=results.toList();event("Обработано ${values.size}, уникальных ${values.toSet().size}");checkResult("100 id",values.size==100 && values.toSet().size==100)
```

**Почему:** Одно получение на элемент обеспечивается каналом с несколькими workers; гарантия здесь относится к успешному прогону.

**Ожидаемый результат:** Обработано 100, уникальных 100.

[Вернуться к задаче](TASKS.md)

## 09.10 · Две стадии

**Что сделать:** С нуля создай конвейер: первая стадия удваивает числа 1..5, вторая прибавляет 1. Используй два канала с capacity=2.

```kotlin
val input=Channel<Int>(2);val middle=Channel<Int>(2)
coroutineScope{launch{for(n in 1..5)input.send(n);input.close()};launch{for(n in input)middle.send(n*2);middle.close()};launch{for(n in middle)event("Результат ${n+1}")}}
```

**Почему:** Ограниченные очереди связывают скорости стадий.

**Ожидаемый результат:** 3,5,7,9,11.

[Вернуться к задаче](TASKS.md)

## 09.11 · Ошибка отдельного задания

**Что сделать:** Из пяти заданий третье падает. Верни текстовый Success или Failure по каждому id; остальные должны завершиться.

```kotlin
val jobs=Channel<Int>();val results=Channel<Pair<Int,String>>(5)
coroutineScope{repeat(2){launch{for(id in jobs){val result=try{if(id==3)error("x");"Success"}catch(_:IllegalStateException){"Failure"};results.send(id to result)}}};launch{for(id in 1..5)jobs.send(id);jobs.close()}}
results.close();event("${results.toList().sortedBy{it.first}}")
```

**Почему:** Локальная ошибка задания не обязана валить очередь при выбранной политике.

**Ожидаемый результат:** Третий Failure; четыре Success.

[Вернуться к задаче](TASKS.md)

## 09.12 · Очередь с давлением и ошибкой

**Что сделать:** Добавь следующий фрагмент вместо TODO в `Task12.kt`.

```kotlin
val queue = Channel<Int>(capacity = 3)
coroutineScope {
    val producer = launch {
        try {
            for (id in ids) {
                queue.send(id)
                sent.incrementAndGet()
                event("Пакет $id отправлен")
            }
        } finally {
            queue.close()
        }
    }
    while (sent.get() < 3) yield()
    sentBeforeWorkers.set(sent.get())

    val workers = List(2) {
        launch {
            for (id in queue) {
                try {
                    process(id)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    failures.add(id)
                    event("Пакет $id: ${e.message}")
                }
            }
        }
    }
    producer.join()
    workers.joinAll()
}
```

**Почему:** Пока получателей нет, четвёртая отправка приостанавливается: это наблюдаемое обратное давление. Закрытие очереди сообщает работникам, что новых пакетов не будет; ожидание всех задач гарантирует законченный отчёт. Ошибка пакета 7 обработана внутри работника и не отменяет соседние задания. Счётчики безопасны при одновременной работе.

**Ожидаемый результат:** До запуска работников отправлено 3, успешно обработано 19, ошибка только у 7, пик не выше 2 и три строки `OK`.

[Вернуться к задаче](TASKS.md)

