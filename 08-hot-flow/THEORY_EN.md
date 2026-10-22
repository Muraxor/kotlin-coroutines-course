# 08 Hot Flow

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## StateFlow API

StateFlow is a hot stream with a required initial value and an always available current value. A new subscriber receives the latest state. Equal updates are suppressed, and a slow subscriber may miss intermediate states. It suits a screen model rather than a record of every event.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

## MutableStateFlow API

For concurrent increments use update { old -> old + 1 } rather than separate value read and assignment. update is atomic. Its lambda may run more than once under contention, so keep external side effects out of it. Expose asStateFlow while retaining write access internally.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-mutable-state-flow/).

## SharedFlow API

SharedFlow broadcasts to active subscribers. replay determines how many past items a new subscriber receives. With replay=0 and no subscribers, an event is not retained. Decide explicitly whether late subscribers should receive past notifications.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-shared-flow/).

## stateIn and shareIn API

stateIn and shareIn convert a cold source into shared hot data. They require an owning scope and a SharingStarted policy: start eagerly, lazily, or while subscribed. Their lifetime follows that scope, so bound a learning collect with take or cancellation.

[Official section](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/state-in.html).

## Worked example

```kotlin
private val _state = MutableStateFlow(0)
val state: StateFlow<Int> = _state.asStateFlow()
fun increment() { _state.update { it + 1 } }
```

After increment, state increases by 1; a subscriber may miss intermediate updates.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
