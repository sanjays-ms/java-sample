# Concurrency Cheatsheet

[Back to Full Documentation](../documentation/28-concurrency.md)

---

## ExecutorService

### Creating

```java
// Fixed pool (most common)
ExecutorService executor = Executors.newFixedThreadPool(4);

// Single thread
ExecutorService executor = Executors.newSingleThreadExecutor();

// Cached (grows as needed)
ExecutorService executor = Executors.newCachedThreadPool();

// Scheduled
ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

// Virtual threads (Java 21+)
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
```

### Submitting Tasks

```java
// Runnable (no result)
executor.execute(() -> doWork());
executor.submit(() -> doWork());

// Callable (with result)
Future<T> future = executor.submit(() -> computeResult());
```

### Shutdown

```java
executor.shutdown();                              // Graceful
executor.shutdownNow();                           // Force
executor.awaitTermination(60, TimeUnit.SECONDS);  // Wait
```

---

## Future

| Method | Description |
|--------|-------------|
| `get()` | Block and get result |
| `get(timeout, unit)` | Block with timeout |
| `isDone()` | Check if complete |
| `isCancelled()` | Check if cancelled |
| `cancel(interrupt)` | Cancel task |

```java
Future<String> future = executor.submit(() -> "Result");
String result = future.get();  // Blocks
String result = future.get(5, TimeUnit.SECONDS);  // With timeout
```

---

## CompletableFuture

### Creating

```java
// Async with result
CompletableFuture<T> cf = CompletableFuture.supplyAsync(() -> value);

// Async without result
CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> doWork());

// Already completed
CompletableFuture<T> cf = CompletableFuture.completedFuture(value);
```

### Transforming

| Method | Input | Output |
|--------|-------|--------|
| `thenApply(fn)` | T | R |
| `thenAccept(consumer)` | T | Void |
| `thenRun(runnable)` | - | Void |
| `thenCompose(fn)` | T | CompletableFuture<R> |

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenApply(String::toUpperCase)
    .thenAccept(System.out::println);
```

### Combining

```java
// Wait for all
CompletableFuture.allOf(cf1, cf2, cf3).join();

// Wait for any
CompletableFuture.anyOf(cf1, cf2, cf3).join();

// Combine two
cf1.thenCombine(cf2, (a, b) -> a + b);

// Chain futures
cf1.thenCompose(result -> anotherAsyncCall(result));
```

### Error Handling

```java
.exceptionally(ex -> defaultValue)

.handle((result, ex) -> {
    if (ex != null) return defaultValue;
    return result;
})

.whenComplete((result, ex) -> {
    // Always runs, for logging/cleanup
})
```

### Timeout (Java 9+)

```java
.orTimeout(5, TimeUnit.SECONDS)           // Throws on timeout
.completeOnTimeout(default, 5, TimeUnit.SECONDS)  // Default on timeout
```

---

## Async Variants

| Sync | Async |
|------|-------|
| `thenApply` | `thenApplyAsync` |
| `thenAccept` | `thenAcceptAsync` |
| `thenRun` | `thenRunAsync` |
| `thenCompose` | `thenComposeAsync` |
| `thenCombine` | `thenCombineAsync` |

```java
// Same thread
.thenApply(fn)

// Different thread (ForkJoinPool)
.thenApplyAsync(fn)

// Custom executor
.thenApplyAsync(fn, executor)
```

---

## Virtual Threads (Java 21+)

```java
// Start virtual thread
Thread.startVirtualThread(() -> doWork());

// Thread.ofVirtual()
Thread t = Thread.ofVirtual().start(() -> doWork());

// Executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> task1());
    executor.submit(() -> task2());
}
```

---

## Common Patterns

### Fire and Forget

```java
CompletableFuture.runAsync(() -> sendEmail());
```

### Fetch and Transform

```java
CompletableFuture<User> user = CompletableFuture
    .supplyAsync(() -> fetchUser(id))
    .thenApply(this::enrichUser);
```

### Parallel Fetch

```java
var userFuture = CompletableFuture.supplyAsync(() -> fetchUser(id));
var ordersFuture = CompletableFuture.supplyAsync(() -> fetchOrders(id));

CompletableFuture.allOf(userFuture, ordersFuture).join();

User user = userFuture.join();
List<Order> orders = ordersFuture.join();
```

### With Timeout and Fallback

```java
CompletableFuture.supplyAsync(() -> fetchData())
    .completeOnTimeout(defaultData, 5, TimeUnit.SECONDS)
    .exceptionally(ex -> fallbackData);
```

---

## Coordination Utilities

### CountDownLatch

```java
CountDownLatch latch = new CountDownLatch(3);

// Workers call
latch.countDown();

// Main waits
latch.await();                              // Wait forever
latch.await(5, TimeUnit.SECONDS);           // Wait with timeout
```

### CyclicBarrier

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All reached barrier");
});

// Each thread calls (reusable)
barrier.await();
```

### Semaphore

```java
Semaphore sem = new Semaphore(3);  // 3 permits
sem.acquire();                     // Get permit
sem.release();                     // Return permit
sem.tryAcquire(1, TimeUnit.SECONDS);  // Try with timeout
```

### Phaser

```java
Phaser phaser = new Phaser(1);     // Initial parties
phaser.register();                 // Add party
phaser.arriveAndAwaitAdvance();    // Wait at barrier
phaser.arriveAndDeregister();      // Leave phaser
```

### Exchanger

```java
Exchanger<String> exchanger = new Exchanger<>();
String received = exchanger.exchange(myData);  // Swap with other thread
```

---

## BlockingQueue

```java
BlockingQueue<T> queue = new ArrayBlockingQueue<>(10);
queue.put(item);           // Blocks if full
T item = queue.take();     // Blocks if empty
queue.offer(item, 1, TimeUnit.SECONDS);  // Timeout
queue.poll(1, TimeUnit.SECONDS);         // Timeout
```

| Implementation | Use Case |
|---------------|----------|
| `ArrayBlockingQueue` | Fixed size |
| `LinkedBlockingQueue` | Flexible size |
| `PriorityBlockingQueue` | Priority order |
| `DelayQueue` | Delayed elements |
| `SynchronousQueue` | Direct handoff |

---

## ForkJoinPool

```java
// RecursiveTask (returns value)
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        if (small) return directCompute();
        SumTask left = new SumTask(...);
        left.fork();  // Run async
        long right = new SumTask(...).compute();
        return left.join() + right;
    }
}

ForkJoinPool pool = ForkJoinPool.commonPool();
Long result = pool.invoke(new SumTask(...));
```

---

## Locks

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();  // Always in finally!
}

// Try with timeout
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try { ... } finally { lock.unlock(); }
}

// Read-write lock
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();   // Multiple readers OK
rwLock.writeLock().lock();  // Exclusive

// StampedLock (Java 8+, optimistic reads)
StampedLock sl = new StampedLock();
long stamp = sl.tryOptimisticRead();
// read data
if (!sl.validate(stamp)) {
    stamp = sl.readLock();
    try { /* read again */ } finally { sl.unlockRead(stamp); }
}
```

---

## Thread Pool Sizing

```java
int cores = Runtime.getRuntime().availableProcessors();

// CPU-bound: cores to cores+1
ExecutorService cpuPool = Executors.newFixedThreadPool(cores);

// I/O-bound: cores * (1 + wait/compute ratio)
ExecutorService ioPool = Executors.newFixedThreadPool(cores * 10);
```

---

## Quick Reference

```java
// Create executor
ExecutorService exec = Executors.newFixedThreadPool(4);

// Submit and get result
Future<T> f = exec.submit(() -> compute());
T result = f.get();
T result = f.get(5, TimeUnit.SECONDS);  // With timeout

// CompletableFuture chain
CompletableFuture.supplyAsync(() -> value)
    .thenApply(transform)
    .thenAccept(consume)
    .exceptionally(handleError);

// Virtual threads (Java 21+)
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(() -> task());
}

// Always shutdown
exec.shutdown();
exec.awaitTermination(60, TimeUnit.SECONDS);
```
