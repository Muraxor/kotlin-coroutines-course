# 05 Errors

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/exception-handling.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Exception propagation

In an ordinary coroutineScope, an unhandled child failure cancels the parent and sibling children. Their finally blocks still run. An outer try/catch sees the scope failure after the structure completes. An async child in an ordinary scope can trigger cancellation even before await.

[Official section](https://kotlinlang.org/docs/exception-handling.html#exception-propagation).

## Exception propagation

launch has no result, while async stores a value or failure in Deferred. A root launch failure is treated as uncaught; async exposes its failure through await. This distinction does not remove the need for explicit failure policy within a child tree.

[Official section](https://kotlinlang.org/docs/exception-handling.html#exception-propagation).

## Supervision scope

supervisorScope prevents one child failure from cancelling siblings. It does not automatically turn failure into a success value: handle it inside a supervised launch or via await for async. External cancellation of the supervisor still cancels its children.

[Official section](https://kotlinlang.org/docs/exception-handling.html#supervision-scope).

## CoroutineExceptionHandler

CoroutineExceptionHandler sees uncaught failures in suitable root or supervised coroutines. The failed coroutine has already completed; the handler cannot resume it. For a local fallback, handle the error inside the relevant child or model an optional source as a result.

[Official section](https://kotlinlang.org/docs/exception-handling.html#coroutineexceptionhandler).

## Worked example

```kotlin
supervisorScope {
    launch { try { error("failure") } catch (e: Exception) { println(e.message) } }
    launch { delay(100); println("Second completed") }
}
```

Both messages appear; the first child failure is handled locally.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
