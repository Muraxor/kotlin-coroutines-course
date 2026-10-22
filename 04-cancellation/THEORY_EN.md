# 04 Cancellation

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/cancellation-and-timeouts.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Make coroutines react to cancellation

cancel requests cancellation. Suspending functions in kotlinx.coroutines normally check it. A CPU loop with no suspension must call currentCoroutineContext().ensureActive(), inspect isActive, or yield. Otherwise it can ignore cancellation indefinitely.

[Official section](https://kotlinlang.org/docs/cancellation-and-timeouts.html#make-coroutines-react-to-cancellation).

## Cancel coroutines

Cancelling a parent propagates to its children. cancel alone does not mean their finally blocks have finished. Use cancelAndJoin when cleanup must be complete before continuing.

[Official section](https://kotlinlang.org/docs/cancellation-and-timeouts.html#cancel-coroutines).

## Run non-cancelable blocks

finally runs after success, failure, and cancellation. CancellationException is a control signal; a broad catch(Exception) should rethrow it. A short withContext(NonCancellable) block can complete essential suspending cleanup, but should not enclose ordinary work.

[Official section](https://kotlinlang.org/docs/cancellation-and-timeouts.html#run-non-cancelable-blocks).

## Timeout

withTimeout cancels the block at its deadline and throws; withTimeoutOrNull returns null for its own timeout. The deadline covers the nested operation and its children. Virtual-time tests can exercise these cases without real waiting.

[Official section](https://kotlinlang.org/docs/cancellation-and-timeouts.html#timeout).

## Worked example

```kotlin
val job = launch {
    try { while (true) { delay(100); println("Working") } }
    finally { println("Cleanup") }
}
delay(250)
job.cancelAndJoin()
println("Stopped")
```

Cleanup appears before “Stopped”; no later ticks are printed.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
