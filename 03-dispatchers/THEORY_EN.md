# 03 Dispatchers

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Job in the context

A coroutine context contains a Job, a dispatcher, and optional elements such as CoroutineName. A child inherits elements while receiving its own linked Job. A new element of the same key replaces the inherited one: child can replace the parent name without severing parenthood.

[Official section](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#job-in-the-context).

## Dispatchers and threads

A dispatcher selects threads for executing coroutine code. Dispatchers.Default is designed for computation, while Dispatchers.IO suits blocking JVM I/O. The actual thread name may change across suspension or stay the same. Test behavior rather than a required name change.

[Official section](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#dispatchers-and-threads).

## Jumping between threads

withContext executes a block in another context and returns its value after completion. It provides a clear boundary: blocking read on IO, computation on Default, result back to the caller. launch returns only Job, while a detached scope can outlive the function.

[Official section](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#jumping-between-threads).

## Naming coroutines for debugging

CoroutineName helps identify logical work in concurrent logs. Print it alongside thread names to separate tasks from their executors. limitedParallelism bounds concurrently executing blocks on a dispatcher view; it is not a general data lock or a limit on whole suspended operations.

[Official section](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#naming-coroutines-for-debugging).

## Worked example

```kotlin
suspend fun readAndCount(): Int {
    val text = withContext(Dispatchers.IO) { blockingRead() }
    return withContext(Dispatchers.Default) { text.length }
}
```

Blocking read uses IO, computation uses Default, and the caller receives Int. blockingRead is a placeholder.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
