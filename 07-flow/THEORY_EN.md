# 07 Flow

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/flow.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Flows are cold

An ordinary flow executes its builder only when collected. A second collect normally reruns the source. emit sends an item, collect receives it. This suits an operation that different callers can start independently.

[Official section](https://kotlinlang.org/docs/flow.html#flows-are-cold).

## Intermediate flow operators

map, filter, and take build a chain without starting it. Terminal operators such as collect and toList trigger execution. take limits the number of values and can finish collection from an otherwise endless source.

`distinctUntilChanged` removes consecutive repeated values. A value that appears again after a different value is kept. The final exercise uses it to ignore repeated sensor readings without discarding later changes. [Official operator reference](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/distinct-until-changed.html).

[Official section](https://kotlinlang.org/docs/flow.html#intermediate-flow-operators).

## Buffering

Flow stages are sequential by default. buffer lets producer and consumer overlap. conflate can drop intermediate values for a slow consumer, while collectLatest cancels processing of the previous value when a new one arrives. Choose based on whether every item or only the latest matters.

[Official section](https://kotlinlang.org/docs/flow.html#buffering).

## Flow exceptions

catch handles failures upstream of its position but not an exception in a downstream collect. flowOn changes the upstream context. Cancelling a collector reaches the source and runs its finally block. Operator placement changes behavior.

[Official section](https://kotlinlang.org/docs/flow.html#flow-exceptions).

## Worked example

```kotlin
flow { for (n in 1..5) emit(n) }
    .filter { it % 2 == 1 }
    .map { it * 10 }
    .collect { println(it) }
```

Output: 10, 30, 50.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
