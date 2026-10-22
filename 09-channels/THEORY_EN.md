# 09 Channels

**Source:** [official Kotlin / JetBrains documentation](https://kotlinlang.org/docs/channels.html). This is a focused study guide based on the official chapter, with adapted examples. It is not a full copy of the documentation. Follow the links in each section for the precise API contract.

## Channel basics

A Channel connects senders and receivers. send may suspend until capacity or a receiver is available, and receive waits for an item. With several workers, one receives each item. This is a work queue, unlike SharedFlow broadcasting to subscribers.

[Official section](https://kotlinlang.org/docs/channels.html#channel-basics).

## Closing and iteration over channels

After close, new sends fail, while buffered items remain available. A for loop ends when the channel is closed and drained. If a producer forgets to close it, a consumer may wait forever. A producer can close with a cause so consumers see its failure.

[Official section](https://kotlinlang.org/docs/channels.html#closing-and-iteration-over-channels).

## Buffered channels

An unbuffered channel synchronizes send with receive. A bounded buffer admits a few pending items but suspends send when full. This backpressure bounds queue growth. Logging before and after send reveals where the producer waits.

[Official section](https://kotlinlang.org/docs/channels.html#buffered-channels).

## Fan-out

Multiple workers can read one Channel and share jobs. Worker count bounds simultaneous processing; Semaphore.withPermit can bound arbitrary suspending operations. Channel alone does not guarantee exactly-once processing through failures; that requires retry and commit policy.

[Official section](https://kotlinlang.org/docs/channels.html#fan-out).

## Worked example

```kotlin
val queue = Channel<Int>(2)
launch { repeat(3) { queue.send(it) }; queue.close() }
for (item in queue) println(item)
```

Output is 0,1,2; then the loop completes.

## What to observe in the exercises

Open the [exercises](TASKS.md) and run each one separately. Observe event order, result, and thread name. Only rely on guaranteed order: children finish before their scope returns, await yields a value, and cleanup finishes before cancelAndJoin returns. Bring surprising output to the chat.
