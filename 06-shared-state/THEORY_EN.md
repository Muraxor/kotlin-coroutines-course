# 06 Shared State

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## The problem

counter++ reads, computes, and writes. Two threads may read the same old value and overwrite each other, losing an increment. A single successful run does not prove the absence of a race; repeat experiments and assert invariants.

[Official section](https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#the-problem).

## Volatiles are of no help

volatile helps visibility of individual reads and writes but does not make a compound increment atomic. AtomicInteger.incrementAndGet makes this single counter operation thread-safe on the JVM.

[Official section](https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#volatiles-are-of-no-help).

## Mutual exclusion

Mutex.withLock protects a critical section. In a stock reservation, availability check and decrement belong under the same lock. Every access path affecting the invariant must follow the same rule. Avoid holding a lock around slow external work unnecessarily.

[Official section](https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#mutual-exclusion).

## Thread confinement coarse-grained

A lock is not always needed. Children can compute local values and return them to the parent for reduction, or one owner can manage mutable state. State the invariant first: total balance stays constant or stock never becomes negative.

[Official section](https://kotlinlang.org/docs/shared-mutable-state-and-concurrency.html#thread-confinement-coarse-grained).

## Worked example

```kotlin
val mutex = Mutex()
var stock = 10
suspend fun reserve(count: Int): Boolean = mutex.withLock {
    if (stock < count) false
    else { stock -= count; true }
}
```

If two clients each request 7, only one succeeds.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
