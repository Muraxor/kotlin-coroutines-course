# 10 Testing Project

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## runTest API

runTest creates a test scope and allows direct calls to suspending code. Delays on its test scheduler advance virtually, so a delay(1000) test can finish almost instantly. This module includes the test library at runtime for console exercises; normal applications keep it in test dependencies.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#runtest-api).

## Controlling the virtual time

runCurrent executes work scheduled at the current instant. advanceTimeBy moves virtual time; use runCurrent for work exactly at the new boundary. advanceUntilIdle drains finite scheduled work. Cancel an infinite source or place it in the test backgroundScope.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#controlling-the-virtual-time).

## Virtual time support with other dispatchers

Hardcoded Dispatchers.Default and IO do not automatically use the test scheduler. Inject a CoroutineDispatcher where tests need schedule control. Supply StandardTestDispatcher(testScheduler) and assert virtual time without Thread.sleep.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#virtual-time-support-with-other-dispatchers).

## launch and async

runTest normally executes child work on one test thread. It is excellent for cancellation, dependent event order, results, and timeouts, but it cannot prove absence of multithreaded races. Exercise shared mutable state separately on Default and assert invariants.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/#launch-and-async).

## Worked example

```kotlin
@Test
fun timeout() = runTest {
    val result = withTimeoutOrNull(100) { delay(500); 42 }
    assertNull(result)
    assertEquals(100, currentTime)
}
```

The test passes without waiting 500 real milliseconds.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
