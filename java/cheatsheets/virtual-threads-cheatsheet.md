# Virtual Threads Cheatsheet

[Back to Full Documentation](../documentation/30-virtual-threads.md)

---

## Creating Virtual Threads

| Method | Use Case | Example |
|--------|----------|---------|
| `startVirtualThread()` | Quick start | `Thread.startVirtualThread(() -> task())` |
| `ofVirtual().start()` | Named thread | `Thread.ofVirtual().name("worker").start(() -> task())` |
| `ofVirtual().unstarted()` | Deferred start | `Thread.ofVirtual().unstarted(() -> task())` |
| `ofVirtual().factory()` | Thread factory | `ThreadFactory f = Thread.ofVirtual().factory()` |

```java
// Most common way
Thread.startVirtualThread(() -> doWork());

// Named virtual thread
Thread.ofVirtual().name("my-vthread").start(() -> doWork());
```

---

## Virtual Thread Executor (Most Common)

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    Future<String> future = executor.submit(() -> {
        // Each task gets its own virtual thread
        return fetchData();
    });
    String result = future.get();
}
```

---

## Checking Thread Type

```java
Thread.currentThread().isVirtual()  // true = virtual thread
```

---

## Virtual vs Platform Threads

| Aspect | Platform Thread | Virtual Thread |
|--------|----------------|----------------|
| Memory | ~1MB per thread | Few KB |
| Max count | Thousands | Millions |
| Create | `Thread.ofPlatform()` | `Thread.ofVirtual()` |
| Best for | CPU-bound | I/O-bound |
| Blocking | Wastes carrier | Yields carrier |

---

## When to Use Virtual Threads

| Scenario | Use Virtual Threads? |
|----------|---------------------|
| HTTP requests | Yes |
| Database queries | Yes |
| File I/O | Yes |
| Network calls | Yes |
| Pure computation | No |
| Short-lived CPU tasks | No |

---

## Avoiding Pinning

Pinning = virtual thread stuck on carrier thread

```java
// BAD: synchronized causes pinning
synchronized (lock) {
    Thread.sleep(1000);  // Carrier blocked!
}

// GOOD: ReentrantLock allows unmounting
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    Thread.sleep(1000);  // Carrier freed
} finally {
    lock.unlock();
}
```

### Detect Pinning
```bash
java -Djdk.tracePinnedThreads=short MyApp
```

---

## Structured Concurrency (Preview)

### ShutdownOnFailure - All Must Succeed
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var future1 = scope.fork(() -> fetchUser());
    var future2 = scope.fork(() -> fetchOrders());
    
    scope.join();
    scope.throwIfFailed();
    
    return combine(future1.get(), future2.get());
}
```

### ShutdownOnSuccess - First Wins
```java
try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
    for (String url : mirrors) {
        scope.fork(() -> fetch(url));
    }
    scope.join();
    return scope.result();  // First successful result
}
```

---

## Common Patterns

### Parallel API Calls
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    Future<User> userFuture = executor.submit(() -> fetchUser(id));
    Future<Orders> ordersFuture = executor.submit(() -> fetchOrders(id));
    
    User user = userFuture.get();
    Orders orders = ordersFuture.get();
}
```

### Processing Many Tasks
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<Result>> futures = items.stream()
        .map(item -> executor.submit(() -> process(item)))
        .toList();
    
    List<Result> results = futures.stream()
        .map(f -> f.get())
        .toList();
}
```

---

## Best Practices

| Do | Don't |
|----|-------|
| Use for I/O-bound tasks | Pool virtual threads |
| Create new virtual threads freely | Use synchronized with blocking |
| Use ReentrantLock | Limit virtual thread count |
| Write simple blocking code | Use for pure CPU work |
| Close executor with try-with-resources | Store large ThreadLocal values |

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Using synchronized | Use ReentrantLock |
| Pooling virtual threads | Create per task |
| CPU-bound in virtual threads | Use platform thread pool |
| Large ThreadLocal | Use ScopedValue (preview) |
| Not using try-with-resources | Always close executor |

---

## Quick Reference

```java
// Create and start
Thread.startVirtualThread(() -> work());

// Executor (preferred)
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(() -> work());
}

// Check type
Thread.currentThread().isVirtual();

// Named threads
Thread.ofVirtual().name("worker-", 0).factory();

// Detect pinning
java -Djdk.tracePinnedThreads=short Main
```
