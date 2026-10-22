# 02 Results

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/composing-suspending-functions.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Sequential by default

Two ordinary calls to suspending functions run sequentially. Two 200 ms waits take about 400 ms together. This is appropriate when the second call depends on the first. Identify dependencies before trying to run tasks concurrently.

[Official section](https://kotlinlang.org/docs/composing-suspending-functions.html#sequential-by-default).

## Concurrent using async

async creates a coroutine with a Deferred<T> result. await returns its value or throws its failure, suspending if needed. launch has no business result. Start both independent Deferred values before the first await; immediately awaiting each one recreates sequential execution.

[Official section](https://kotlinlang.org/docs/composing-suspending-functions.html#concurrent-using-async).

## Structured concurrency with async

Put a group of async calls inside coroutineScope and return the combined result. If a normal child fails, the scope cancels siblings and propagates the failure. The caller can treat the function return as completion of the whole operation. Avoid inventing a detached scope inside it.

[Official section](https://kotlinlang.org/docs/composing-suspending-functions.html#structured-concurrency-with-async).

## Concurrent using async

awaitAll returns results in Deferred order even if completion order differs. Logs reveal the observed schedule, which should not become part of the contract unless required. Compare the same operations in sequential and concurrent versions; exact timings vary.

[Official section](https://kotlinlang.org/docs/composing-suspending-functions.html#concurrent-using-async).

## Worked example

```kotlin
suspend fun load(): Int = coroutineScope {
    val a = async { delay(200); 20 }
    val b = async { delay(300); 22 }
    a.await() + b.await()
}
```

The result is 42. The waits overlap when both async calls precede await.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
