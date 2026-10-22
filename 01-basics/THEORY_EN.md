# 01 Basics

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/coroutines-basics.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Comparing coroutines and JVM threads

The official guide defines a coroutine as a suspendable computation. On the JVM, threads still execute the instructions. While one coroutine waits, another can use the thread. This is concurrency; parallel execution depends on the dispatcher and available threads. Coroutine count is not thread count.

[Official section](https://kotlinlang.org/docs/coroutines-basics.html#comparing-coroutines-and-jvm-threads).

## Suspending functions

suspend lets a function pause and later resume at the same point. The keyword alone neither starts a thread nor creates a child task. Call it from another suspending function or a coroutine. A blocking Thread.sleep remains blocking even inside a suspend function.

[Official section](https://kotlinlang.org/docs/coroutines-basics.html#suspending-functions).

## Coroutine scope and structured concurrency

launch starts a child coroutine and returns Job. The parent continues after launch, but coroutineScope waits for all its children. A message after coroutineScope can therefore report completion of the group. runBlocking bridges ordinary main and suspending code by blocking the calling thread.

[Official section](https://kotlinlang.org/docs/coroutines-basics.html#coroutine-scope-and-structured-concurrency).

## Create your first coroutines

delay suspends a coroutine without holding the thread during the wait. Independent delays of 200 and 300 ms can overlap, making total time closer to 300 ms. Unrelated log order can vary. Use Job.join to wait for a specific task instead of guessing a delay.

[Official section](https://kotlinlang.org/docs/coroutines-basics.html#create-your-first-coroutines).

## Worked example

```kotlin
suspend fun work(name: String) {
    println("$name: start")
    delay(200)
    println("$name: done")
}

fun main() = runBlocking {
    coroutineScope {
        launch { work("A") }
        launch { work("B") }
    }
    println("Both completed")
}
```

Both starts precede group completion; the final message follows both children.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
