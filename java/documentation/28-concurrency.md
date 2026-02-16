# Concurrency Utilities in Java

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/concurrency-cheatsheet.md)

---

## What Are Concurrency Utilities?

Concurrency utilities are higher-level tools that make it easier to write multi-threaded programs without dealing with low-level thread management.

**In Plain Words:**
Instead of creating and managing individual threads yourself (like hiring individual workers), you use a system that manages workers for you (like using a staffing agency). You just say "here's the work that needs to be done" and the system figures out who should do it and when.

**Why Use Them Instead of Raw Threads?**

| Raw Threads | Concurrency Utilities |
|-------------|----------------------|
| You create/destroy threads | Thread pool manages this |
| You handle coordination | Built-in coordination tools |
| Easy to make mistakes | Harder to make mistakes |
| Performance issues (too many threads) | Optimal thread reuse |
| Complex error handling | Cleaner error handling |

---

## The java.util.concurrent Package

Java's concurrency utilities live in `java.util.concurrent` (and sub-packages).

**Key Components:**

| Component | Purpose |
|-----------|---------|
| `ExecutorService` | Manages thread pools and task execution |
| `Future` | Represents a result that will be available later |
| `CompletableFuture` | Advanced Future with chaining and composition |
| `Callable` | Like Runnable, but can return a value |
| `Locks` | More flexible than synchronized |
| `Semaphore`, `CountDownLatch`, etc. | Coordination tools |
| `Concurrent Collections` | Thread-safe data structures |

---

## ExecutorService: The Thread Pool Manager

`ExecutorService` manages a pool of threads and handles task execution for you.

### Why Use a Thread Pool?

```
Without Thread Pool:
Task 1 → Create Thread → Execute → Destroy Thread
Task 2 → Create Thread → Execute → Destroy Thread
Task 3 → Create Thread → Execute → Destroy Thread
(Creating/destroying threads is expensive!)

With Thread Pool:
             ┌──────────────────┐
Task 1 ─────►│                  │
Task 2 ─────►│   Thread Pool    │
Task 3 ─────►│  (Reuses Threads)│
             └──────────────────┘
(Threads are reused - much more efficient!)
```

### Creating an ExecutorService

Java provides factory methods in the `Executors` class:

```java
import java.util.concurrent.*;

// Fixed thread pool - exactly N threads
ExecutorService fixedPool = Executors.newFixedThreadPool(4);

// Cached thread pool - creates threads as needed, reuses idle ones
ExecutorService cachedPool = Executors.newCachedThreadPool();

// Single thread executor - only one thread, tasks run sequentially
ExecutorService singlePool = Executors.newSingleThreadExecutor();

// Scheduled executor - for delayed/periodic tasks
ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);

// Work stealing pool (Java 8+) - uses all available processors
ExecutorService workStealingPool = Executors.newWorkStealingPool();

// Virtual thread executor (Java 21+) - creates a new virtual thread per task
ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
```

**Which Pool to Choose?**

| Pool Type | When to Use |
|-----------|------------|
| `newFixedThreadPool(n)` | Known number of concurrent tasks, CPU-bound work |
| `newCachedThreadPool()` | Many short-lived tasks, I/O-bound work |
| `newSingleThreadExecutor()` | Tasks must run sequentially |
| `newScheduledThreadPool(n)` | Delayed or periodic tasks |
| `newWorkStealingPool()` | Parallel processing with work distribution |
| `newVirtualThreadPerTaskExecutor()` | I/O-bound work, many concurrent tasks (Java 21+) |

### Thread Pool Sizing Guidelines

Choosing the right pool size is critical for performance.

**For CPU-Bound Tasks:**
```java
// Rule: number of threads = number of CPU cores
int cores = Runtime.getRuntime().availableProcessors();
ExecutorService cpuPool = Executors.newFixedThreadPool(cores);

// Or slightly more to account for occasional blocking
ExecutorService cpuPool2 = Executors.newFixedThreadPool(cores + 1);
```

**For I/O-Bound Tasks:**
```java
// Rule: more threads since they spend time waiting
// Formula: threads = cores * (1 + wait_time / compute_time)
// If tasks wait 90% of the time: threads = cores * 10

int cores = Runtime.getRuntime().availableProcessors();
int ioThreads = cores * 10;  // Or more for heavy I/O
ExecutorService ioPool = Executors.newFixedThreadPool(ioThreads);

// Or use cached pool for unpredictable I/O workloads
ExecutorService cachedPool = Executors.newCachedThreadPool();
```

**For Mixed Workloads:**
```java
// Separate pools for CPU and I/O work
ExecutorService cpuPool = Executors.newFixedThreadPool(cores);
ExecutorService ioPool = Executors.newCachedThreadPool();

// Submit CPU work to cpuPool, I/O work to ioPool
```

**Pool Size Summary:**

| Workload Type | Pool Size Formula | Example (8 cores) |
|--------------|-------------------|-------------------|
| CPU-bound | cores to cores+1 | 8-9 threads |
| I/O-bound (50% wait) | cores * 2 | 16 threads |
| I/O-bound (90% wait) | cores * 10 | 80 threads |
| Heavy I/O | Cached or virtual | Dynamic |
| Virtual threads | Unlimited (Java 21+) | Millions possible |

---

### Submitting Tasks to ExecutorService

#### Using execute() - Fire and Forget

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

// Submit Runnable - no return value
executor.execute(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
    // Do some work
});

executor.execute(() -> System.out.println("Another task"));
executor.execute(() -> System.out.println("Yet another task"));

// Don't forget to shut down!
executor.shutdown();
```

#### Using submit() - Get a Future Back

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

// Submit Runnable - returns Future<?>
Future<?> future1 = executor.submit(() -> {
    System.out.println("Task running...");
});

// Submit Callable - returns Future<T> with result
Future<Integer> future2 = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;  // This is a Callable (returns a value)
});

// Get the result (blocks until complete)
try {
    Integer result = future2.get();  // Blocks here
    System.out.println("Result: " + result);
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}

executor.shutdown();
```

---

### Runnable vs Callable

| Runnable | Callable |
|----------|----------|
| `void run()` | `V call() throws Exception` |
| Cannot return a value | Returns a value |
| Cannot throw checked exceptions | Can throw checked exceptions |
| Use with `execute()` or `submit()` | Use with `submit()` |

```java
// Runnable - no return value
Runnable runnable = () -> {
    System.out.println("I don't return anything");
};

// Callable - returns a value
Callable<String> callable = () -> {
    return "I return a String!";
};

// Callable with exception
Callable<Integer> riskyCallable = () -> {
    if (Math.random() > 0.5) {
        throw new Exception("Something went wrong!");
    }
    return 100;
};
```

---

### Shutting Down ExecutorService

**This is critical! Always shut down your executor.**

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// Submit tasks...
executor.submit(() -> doWork());

// Graceful shutdown - no new tasks, complete existing ones
executor.shutdown();

// Wait for completion with timeout
try {
    boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
    if (!finished) {
        System.out.println("Some tasks didn't finish in time");
        // Force shutdown
        executor.shutdownNow();
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
}
```

**Shutdown Methods:**

| Method | Behavior |
|--------|----------|
| `shutdown()` | Stop accepting new tasks, complete queued tasks |
| `shutdownNow()` | Stop accepting, cancel queued, interrupt running |
| `awaitTermination(time, unit)` | Block until all complete or timeout |
| `isShutdown()` | Check if shutdown was called |
| `isTerminated()` | Check if all tasks completed |

**Best Practice Pattern:**

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Submit all your tasks
    executor.submit(() -> task1());
    executor.submit(() -> task2());
} finally {
    executor.shutdown();
    try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
    }
}
```

**Java 19+ try-with-resources (even better):**

```java
// AutoCloseable executor - automatically shuts down
try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
    executor.submit(() -> task1());
    executor.submit(() -> task2());
}  // Automatically calls shutdown() and awaitTermination()
```

---

### Submitting Multiple Tasks

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// Create list of tasks
List<Callable<Integer>> tasks = Arrays.asList(
    () -> { Thread.sleep(1000); return 1; },
    () -> { Thread.sleep(2000); return 2; },
    () -> { Thread.sleep(500);  return 3; }
);

// invokeAll - waits for ALL to complete
List<Future<Integer>> results = executor.invokeAll(tasks);
for (Future<Integer> future : results) {
    System.out.println("Result: " + future.get());
}
// Output: 1, 2, 3 (after all complete)

// invokeAny - returns result of FIRST to complete
Integer firstResult = executor.invokeAny(tasks);
System.out.println("First result: " + firstResult);  // Likely 3 (fastest)

executor.shutdown();
```

---

## Future: Representing Pending Results

A `Future` represents a result that will be available sometime in the future.

### Basic Future Usage

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Future<String> future = executor.submit(() -> {
    Thread.sleep(2000);  // Simulate long operation
    return "Hello from the future!";
});

// Do other work while task runs...
System.out.println("Task is running in background...");

// Now get the result (blocks if not ready)
try {
    String result = future.get();  // Waits up to 2 seconds here
    System.out.println(result);
} catch (InterruptedException e) {
    System.out.println("Interrupted while waiting");
} catch (ExecutionException e) {
    System.out.println("Task threw exception: " + e.getCause());
}

executor.shutdown();
```

### Future Methods

| Method | Description |
|--------|-------------|
| `get()` | Block and wait for result (forever) |
| `get(timeout, unit)` | Block with timeout, throws TimeoutException |
| `isDone()` | Check if complete (without blocking) |
| `isCancelled()` | Check if was cancelled |
| `cancel(mayInterrupt)` | Attempt to cancel the task |

### Getting Result with Timeout

```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(5000);
    return "Result";
});

try {
    // Wait maximum 2 seconds
    String result = future.get(2, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    System.out.println("Task took too long!");
    future.cancel(true);  // Cancel the slow task
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```

### Cancelling a Future

```java
Future<String> future = executor.submit(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // Long running work
        doWork();
    }
    return "Done";
});

// Later, decide to cancel
boolean cancelled = future.cancel(true);  // true = interrupt if running

if (cancelled) {
    System.out.println("Task was cancelled");
}
```

### Checking Status Without Blocking

```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(3000);
    return "Result";
});

// Poll for completion
while (!future.isDone()) {
    System.out.println("Still waiting...");
    Thread.sleep(500);
}

// Now safe to get without blocking
if (!future.isCancelled()) {
    String result = future.get();  // Returns immediately
    System.out.println("Got: " + result);
}
```

---

## CompletableFuture: Modern Async Programming

`CompletableFuture` (Java 8+) is a powerful improvement over `Future`:
- Chain multiple operations
- Combine multiple futures
- Handle errors elegantly
- Non-blocking callbacks

### Creating CompletableFutures

```java
import java.util.concurrent.CompletableFuture;

// Run async task with no return value
CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});

// Run async task that returns a value
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
    return "Hello from CompletableFuture!";
});

// Create an already-completed future
CompletableFuture<String> completed = CompletableFuture.completedFuture("Done!");

// Create and complete manually
CompletableFuture<Integer> manual = new CompletableFuture<>();
// Later...
manual.complete(42);  // Complete with value
// or
manual.completeExceptionally(new RuntimeException("Failed"));
```

### Using Custom Executor

```java
ExecutorService myExecutor = Executors.newFixedThreadPool(4);

// By default, uses ForkJoinPool.commonPool()
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Result");

// Use custom executor
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
    () -> "Result with custom executor",
    myExecutor
);

myExecutor.shutdown();
```

---

### Chaining Operations: Then Methods

The power of `CompletableFuture` is chaining transformations.

#### thenApply - Transform the Result

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")           // Add " World"
    .thenApply(String::toUpperCase);        // Make uppercase

String result = future.get();  // "HELLO WORLD"
```

**Visual Flow:**
```
supplyAsync("Hello") → thenApply(+" World") → thenApply(toUpperCase)
       ↓                      ↓                        ↓
    "Hello"           →   "Hello World"    →    "HELLO WORLD"
```

#### thenAccept - Consume the Result (No Return)

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenAccept(s -> System.out.println("Received: " + s));
// Prints: Received: Hello
```

#### thenRun - Run After Completion (Ignores Result)

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenRun(() -> System.out.println("Previous task completed!"));
// Prints: Previous task completed!
```

---

### Async Variants: Non-Blocking Callbacks

Each "then" method has an "Async" variant that runs on a different thread:

```java
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")       // Same thread as previous
    .thenApplyAsync(s -> s + "!")       // Different thread
    .thenApplyAsync(s -> s.toUpperCase(), myExecutor);  // Custom executor
```

| Method | Async Variant | Difference |
|--------|--------------|------------|
| `thenApply` | `thenApplyAsync` | Runs on different thread |
| `thenAccept` | `thenAcceptAsync` | Runs on different thread |
| `thenRun` | `thenRunAsync` | Runs on different thread |

**When to Use Async Variants:**
- The callback does significant work
- You want to parallelize more
- You want to use a specific executor

---

### Chaining Dependent Futures: thenCompose

When one async operation depends on another's result:

```java
// This returns CompletableFuture<CompletableFuture<String>> - WRONG!
// CompletableFuture.supplyAsync(() -> 42)
//     .thenApply(id -> getUserById(id));  // getUserById returns a Future

// Use thenCompose to flatten
CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> 42)
    .thenCompose(id -> getUserById(id));  // Properly chains the futures

// Example
public CompletableFuture<String> getUserById(int id) {
    return CompletableFuture.supplyAsync(() -> "User-" + id);
}
```

**thenApply vs thenCompose:**
- `thenApply`: transforms value → returns `CompletableFuture<T>`
- `thenCompose`: chains futures → flattens `CompletableFuture<CompletableFuture<T>>` to `CompletableFuture<T>`

---

### Combining Multiple Futures

#### thenCombine - Combine Two Futures

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

// Combine when both complete
CompletableFuture<String> combined = future1.thenCombine(future2, 
    (result1, result2) -> result1 + " " + result2);

String result = combined.get();  // "Hello World"
```

#### allOf - Wait for All Futures

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

// Wait for all to complete
CompletableFuture<Void> allDone = CompletableFuture.allOf(f1, f2, f3);

allDone.thenRun(() -> {
    // All futures are now complete
    try {
        System.out.println(f1.get() + f2.get() + f3.get());  // "ABC"
    } catch (Exception e) {
        e.printStackTrace();
    }
});

// Better pattern - collect all results
CompletableFuture<List<String>> allResults = CompletableFuture.allOf(f1, f2, f3)
    .thenApply(v -> Stream.of(f1, f2, f3)
        .map(CompletableFuture::join)  // join() is like get() but unchecked
        .toList());

List<String> results = allResults.get();  // ["A", "B", "C"]
```

#### anyOf - First to Complete Wins

```java
CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
    sleep(100);
    return "Fast";
});

CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
    sleep(5000);
    return "Slow";
});

// Get first result
CompletableFuture<Object> first = CompletableFuture.anyOf(fast, slow);
Object result = first.get();  // "Fast" (returns first to complete)
```

---

### Error Handling

#### exceptionally - Handle Exceptions

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Random failure!");
    }
    return "Success";
}).exceptionally(ex -> {
    System.out.println("Error: " + ex.getMessage());
    return "Default Value";  // Recovery value
});

String result = future.get();  // "Success" or "Default Value"
```

#### handle - Handle Both Success and Failure

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Failed!");
    }
    return "Success";
}).handle((result, exception) -> {
    if (exception != null) {
        return "Recovered from: " + exception.getMessage();
    }
    return "Got result: " + result;
});
```

#### whenComplete - Side Effect on Completion

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Result")
    .whenComplete((result, exception) -> {
        // Called after completion, but doesn't change the result
        if (exception != null) {
            System.out.println("Failed: " + exception);
        } else {
            System.out.println("Completed with: " + result);
        }
    });
// Original result/exception is preserved
```

**Error Handling Comparison:**

| Method | Purpose | Changes Result? |
|--------|---------|----------------|
| `exceptionally` | Handle only exceptions | Yes (returns recovery value) |
| `handle` | Handle both success and exception | Yes |
| `whenComplete` | Side effects (logging, cleanup) | No |

---

### Complete Example: Async API Calls

```java
import java.util.concurrent.*;

public class AsyncApiExample {
    
    public static void main(String[] args) throws Exception {
        
        // Simulate async API calls
        CompletableFuture<String> userFuture = fetchUser(1);
        CompletableFuture<String> ordersFuture = fetchOrders(1);
        CompletableFuture<String> recommendationsFuture = fetchRecommendations(1);
        
        // Process all in parallel, then combine
        CompletableFuture<String> dashboard = userFuture
            .thenCombine(ordersFuture, (user, orders) -> 
                "User: " + user + "\nOrders: " + orders)
            .thenCombine(recommendationsFuture, (combined, recs) ->
                combined + "\nRecommendations: " + recs);
        
        // Add timeout
        String result = dashboard.orTimeout(5, TimeUnit.SECONDS).get();
        System.out.println(result);
    }
    
    static CompletableFuture<String> fetchUser(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return "John Doe (ID: " + id + ")";
        });
    }
    
    static CompletableFuture<String> fetchOrders(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(1500);
            return "3 pending orders";
        });
    }
    
    static CompletableFuture<String> fetchRecommendations(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(800);
            return "5 product recommendations";
        });
    }
    
    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}
```

**Output:**
```
User: John Doe (ID: 1)
Orders: 3 pending orders
Recommendations: 5 product recommendations
```

---

### Timeout and Completion Control (Java 9+)

```java
// Complete with timeout
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    sleep(10000);  // Takes 10 seconds
    return "Result";
}).orTimeout(2, TimeUnit.SECONDS);  // Fail after 2 seconds

// Or complete with default on timeout
CompletableFuture<String> withDefault = CompletableFuture.supplyAsync(() -> {
    sleep(10000);
    return "Result";
}).completeOnTimeout("Default", 2, TimeUnit.SECONDS);  // Use default after 2s

// Force completion
CompletableFuture<String> manual = new CompletableFuture<>();
manual.complete("Manually completed");
// or
manual.completeExceptionally(new RuntimeException("Forced failure"));
```

---

## Virtual Threads (Java 21+)

Virtual threads are lightweight threads that make it easy to write highly concurrent applications.

### What Are Virtual Threads?

**Platform Threads (Traditional):**
- One Java thread = one OS thread
- Heavy (1MB+ stack size each)
- Limited by OS (thousands max)

**Virtual Threads:**
- Many virtual threads share few OS threads
- Lightweight (few KB each)
- Can have millions
- Ideal for I/O-bound tasks (waiting for network, database, etc.)

### Creating Virtual Threads

```java
// Direct creation
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in virtual thread: " + 
                      Thread.currentThread().isVirtual());
});

// With name
Thread named = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> doWork());

// Using factory
ThreadFactory factory = Thread.ofVirtual().factory();
Thread t = factory.newThread(() -> doWork());
t.start();
```

### Virtual Thread Executor

```java
// One virtual thread per task - can handle millions of concurrent tasks!
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    
    // Submit many concurrent tasks
    List<Future<String>> futures = new ArrayList<>();
    
    for (int i = 0; i < 10000; i++) {  // 10,000 concurrent tasks!
        final int taskId = i;
        futures.add(executor.submit(() -> {
            Thread.sleep(1000);  // Simulate I/O wait
            return "Result-" + taskId;
        }));
    }
    
    // All run concurrently (would exhaust memory with platform threads!)
    for (Future<String> future : futures) {
        System.out.println(future.get());
    }
}
```

### When to Use Virtual Threads

| Use Virtual Threads | Use Platform Threads |
|--------------------|---------------------|
| I/O-bound tasks (network, file, DB) | CPU-bound computation |
| Many concurrent blocking operations | Need pinning to CPU core |
| Simple request-handling servers | Very short tasks (overhead may not be worth it) |
| Tasks that mostly wait | Need ThreadLocal with large data |

### Virtual Thread Best Practices

```java
// DO: Use for I/O-heavy workloads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (String url : urls) {
        executor.submit(() -> fetchUrl(url));  // Each virtual thread waits for I/O
    }
}

// DO: Let virtual threads block naturally
String result = httpClient.send(request, BodyHandlers.ofString()).body();
// Virtual thread efficiently yields while waiting

// AVOID: Long CPU-bound computation in virtual thread
// (virtual thread stays pinned to platform thread, reducing benefits)
```

---

## Structured Concurrency (Java 21+ Preview)

Structured concurrency treats concurrent tasks as a unit that starts, runs, and completes together.

```java
import java.util.concurrent.StructuredTaskScope;

String fetchUserAndOrders() throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        
        // Fork subtasks
        var userFuture = scope.fork(() -> fetchUser());
        var ordersFuture = scope.fork(() -> fetchOrders());
        
        // Wait for all to complete (or first failure)
        scope.join();
        scope.throwIfFailed();  // Propagate exception if any task failed
        
        // Both completed successfully
        return "User: " + userFuture.get() + ", Orders: " + ordersFuture.get();
    }
    // If an exception occurs or scope exits, all subtasks are cancelled
}
```

**Benefits:**
- Tasks are bounded to their scope
- Automatic cancellation on failure
- Clear parent-child relationship
- Better stack traces

---

## Locks: Advanced Synchronization

The `java.util.concurrent.locks` package provides more flexible locking than `synchronized`.

### ReentrantLock

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();
    
    public void increment() {
        lock.lock();  // Acquire lock
        try {
            count++;
        } finally {
            lock.unlock();  // ALWAYS unlock in finally!
        }
    }
    
    // Try to acquire lock with timeout
    public boolean tryIncrement() {
        try {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    count++;
                    return true;
                } finally {
                    lock.unlock();
                }
            }
            return false;  // Couldn't acquire lock
        } catch (InterruptedException e) {
            return false;
        }
    }
}
```

### ReadWriteLock

Allows multiple readers OR one writer (not both).

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
    private final Map<String, String> data = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    public String get(String key) {
        rwLock.readLock().lock();  // Multiple readers allowed
        try {
            return data.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public void put(String key, String value) {
        rwLock.writeLock().lock();  // Exclusive access
        try {
            data.put(key, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
```

### Synchronized vs Lock

| synchronized | ReentrantLock |
|--------------|---------------|
| Implicit lock/unlock | Explicit lock/unlock |
| No timeout | tryLock with timeout |
| No fairness control | Can be fair |
| Cannot interrupt waiting | Can interrupt |
| Simpler syntax | More flexible |

---

## Coordination Utilities

### CountDownLatch

Wait for a fixed number of events.

```java
import java.util.concurrent.CountDownLatch;

public class CountDownExample {
    public static void main(String[] args) throws InterruptedException {
        int numTasks = 3;
        CountDownLatch latch = new CountDownLatch(numTasks);
        
        for (int i = 0; i < numTasks; i++) {
            final int taskId = i;
            new Thread(() -> {
                System.out.println("Task " + taskId + " running...");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                System.out.println("Task " + taskId + " done!");
                latch.countDown();  // Decrease count by 1
            }).start();
        }
        
        System.out.println("Waiting for all tasks...");
        latch.await();  // Block until count reaches 0
        System.out.println("All tasks completed!");
    }
}
```

**Output:**
```
Waiting for all tasks...
Task 0 running...
Task 1 running...
Task 2 running...
Task 0 done!
Task 1 done!
Task 2 done!
All tasks completed!
```

---

### CyclicBarrier

Multiple threads wait for each other at a barrier point.

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        int numWorkers = 3;
        
        // Barrier action runs when all threads reach barrier
        CyclicBarrier barrier = new CyclicBarrier(numWorkers, 
            () -> System.out.println("=== All workers synchronized ==="));
        
        for (int i = 0; i < numWorkers; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    System.out.println("Worker " + workerId + " phase 1");
                    barrier.await();  // Wait for others
                    
                    System.out.println("Worker " + workerId + " phase 2");
                    barrier.await();  // Reusable barrier!
                    
                    System.out.println("Worker " + workerId + " done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

**Output:**
```
Worker 0 phase 1
Worker 1 phase 1
Worker 2 phase 1
=== All workers synchronized ===
Worker 0 phase 2
Worker 1 phase 2
Worker 2 phase 2
=== All workers synchronized ===
Worker 0 done
Worker 1 done
Worker 2 done
```

**CountDownLatch vs CyclicBarrier:**

| CountDownLatch | CyclicBarrier |
|---------------|---------------|
| One-time use | Reusable |
| Threads count down | Threads wait at barrier |
| Main thread waits for workers | Workers wait for each other |

---

### Semaphore

Control access to a resource with limited capacity.

```java
import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    public static void main(String[] args) {
        // Only 3 threads can access resource at a time
        Semaphore semaphore = new Semaphore(3);
        
        for (int i = 0; i < 10; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();  // Get permit (blocks if none available)
                    System.out.println("Thread " + id + " accessing resource");
                    Thread.sleep(2000);  // Use resource
                    System.out.println("Thread " + id + " releasing");
                    semaphore.release();  // Return permit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

**Use Cases:**
- Connection pooling
- Rate limiting
- Resource access control

---

### Phaser

A more flexible synchronization barrier (like a reusable, dynamic CyclicBarrier).

```java
import java.util.concurrent.Phaser;

public class PhaserExample {
    public static void main(String[] args) {
        // 1 = main thread as initial party
        Phaser phaser = new Phaser(1);
        
        for (int i = 0; i < 3; i++) {
            final int workerId = i;
            phaser.register();  // Register new party
            
            new Thread(() -> {
                System.out.println("Worker " + workerId + " phase 1");
                phaser.arriveAndAwaitAdvance();  // Wait at barrier
                
                System.out.println("Worker " + workerId + " phase 2");
                phaser.arriveAndAwaitAdvance();
                
                System.out.println("Worker " + workerId + " done");
                phaser.arriveAndDeregister();  // Leave the phaser
            }).start();
        }
        
        // Main thread coordinates phases
        phaser.arriveAndAwaitAdvance();  // Wait for phase 1
        System.out.println("=== Phase 1 complete ===");
        
        phaser.arriveAndAwaitAdvance();  // Wait for phase 2
        System.out.println("=== Phase 2 complete ===");
        
        phaser.arriveAndDeregister();  // Main thread leaves
    }
}
```

**Phaser vs CyclicBarrier:**
- Phaser allows dynamic registration/deregistration
- Phaser can terminate when no parties registered
- More flexible for complex multi-phase algorithms

---

### Exchanger

Allows two threads to exchange data at a synchronization point.

```java
import java.util.concurrent.Exchanger;

public class ExchangerExample {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        
        // Producer thread
        new Thread(() -> {
            try {
                String data = "Data from Producer";
                System.out.println("Producer sending: " + data);
                
                // Exchange and receive
                String received = exchanger.exchange(data);
                System.out.println("Producer received: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        // Consumer thread
        new Thread(() -> {
            try {
                String data = "Acknowledgment from Consumer";
                System.out.println("Consumer sending: " + data);
                
                // Exchange and receive
                String received = exchanger.exchange(data);
                System.out.println("Consumer received: " + received);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

**Output:**
```
Producer sending: Data from Producer
Consumer sending: Acknowledgment from Consumer
Producer received: Acknowledgment from Consumer
Consumer received: Data from Producer
```

---

## BlockingQueue: Thread-Safe Producer-Consumer

BlockingQueue implementations block on put (when full) and take (when empty).

### BlockingQueue Implementations

| Implementation | Description |
|---------------|-------------|
| `ArrayBlockingQueue` | Fixed-size, bounded queue |
| `LinkedBlockingQueue` | Optionally bounded, better throughput |
| `PriorityBlockingQueue` | Unbounded, priority ordering |
| `DelayQueue` | Elements available after delay expires |
| `SynchronousQueue` | Zero capacity, direct handoff |
| `LinkedTransferQueue` | Combines features of several queues |

### ArrayBlockingQueue Example

```java
import java.util.concurrent.*;

public class BlockingQueueExample {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        
        // Producer
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item-" + i;
                    queue.put(item);  // Blocks if queue is full
                    System.out.println("Produced: " + item);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        // Consumer
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = queue.take();  // Blocks if queue is empty
                    System.out.println("Consumed: " + item);
                    Thread.sleep(300);  // Consumer is slower
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        producer.start();
        consumer.start();
    }
}
```

### BlockingQueue Methods

| Method | Behavior When Cannot Complete |
|--------|------------------------------|
| `put(e)` | Blocks until space available |
| `take()` | Blocks until element available |
| `offer(e)` | Returns false immediately |
| `offer(e, timeout, unit)` | Waits up to timeout |
| `poll()` | Returns null immediately |
| `poll(timeout, unit)` | Waits up to timeout |

### DelayQueue Example

Elements become available only after their delay expires:

```java
import java.util.concurrent.*;

class DelayedTask implements Delayed {
    private final String name;
    private final long triggerTime;
    
    public DelayedTask(String name, long delayMs) {
        this.name = name;
        this.triggerTime = System.currentTimeMillis() + delayMs;
    }
    
    @Override
    public long getDelay(TimeUnit unit) {
        long remaining = triggerTime - System.currentTimeMillis();
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public int compareTo(Delayed other) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), 
                           other.getDelay(TimeUnit.MILLISECONDS));
    }
    
    @Override
    public String toString() { return name; }
}

public class DelayQueueExample {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        
        queue.put(new DelayedTask("Task-3sec", 3000));
        queue.put(new DelayedTask("Task-1sec", 1000));
        queue.put(new DelayedTask("Task-2sec", 2000));
        
        System.out.println("Waiting for tasks...");
        
        // Tasks come out in delay order
        System.out.println("Got: " + queue.take());  // After 1 sec
        System.out.println("Got: " + queue.take());  // After 2 sec
        System.out.println("Got: " + queue.take());  // After 3 sec
    }
}
```

---

## ForkJoinPool: Divide and Conquer

ForkJoinPool is designed for tasks that can be broken into smaller subtasks.

### How It Works

```
                    Big Task
                       │
           ┌───────────┼───────────┐
           ▼           ▼           ▼
       Subtask 1   Subtask 2   Subtask 3
           │           │           │
     ┌─────┴─────┐     │     ┌─────┴─────┐
     ▼           ▼     ▼     ▼           ▼
  Sub 1a     Sub 1b  (done)  Sub 3a   Sub 3b
     │           │           │           │
     └─────┬─────┘           └─────┬─────┘
           ▼                       ▼
       Result 1                Result 3
           │           │           │
           └───────────┴───────────┘
                       ▼
                 Final Result
```

### RecursiveTask Example (Returns Value)

```java
import java.util.concurrent.*;

public class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private final long[] array;
    private final int start, end;
    
    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute() {
        int length = end - start;
        
        // Base case: small enough to compute directly
        if (length <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        
        // Split into two subtasks
        int mid = start + length / 2;
        SumTask leftTask = new SumTask(array, start, mid);
        SumTask rightTask = new SumTask(array, mid, end);
        
        // Fork left task (runs in parallel)
        leftTask.fork();
        
        // Compute right task in current thread
        long rightResult = rightTask.compute();
        
        // Join left task and combine results
        long leftResult = leftTask.join();
        
        return leftResult + rightResult;
    }
    
    public static void main(String[] args) {
        long[] array = new long[10_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        SumTask task = new SumTask(array, 0, array.length);
        
        long result = pool.invoke(task);
        System.out.println("Sum: " + result);
    }
}
```

### RecursiveAction Example (No Return Value)

```java
import java.util.concurrent.*;

public class IncrementTask extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private final int[] array;
    private final int start, end;
    
    public IncrementTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected void compute() {
        if (end - start <= THRESHOLD) {
            // Base case: increment directly
            for (int i = start; i < end; i++) {
                array[i]++;
            }
        } else {
            // Split
            int mid = start + (end - start) / 2;
            invokeAll(
                new IncrementTask(array, start, mid),
                new IncrementTask(array, mid, end)
            );
        }
    }
}
```

### ForkJoinPool Methods

| Method | Description |
|--------|-------------|
| `invoke(task)` | Execute and wait for result |
| `execute(task)` | Execute asynchronously |
| `submit(task)` | Execute and return Future |
| `commonPool()` | Get shared common pool |
| `getParallelism()` | Get parallelism level |

---

## StampedLock: Optimistic Reading

StampedLock (Java 8+) is faster than ReadWriteLock for read-heavy scenarios.

```java
import java.util.concurrent.locks.StampedLock;

public class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();
    
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    // Optimistic read - very fast, no locking
    public double distanceFromOrigin() {
        // Try optimistic read (no lock acquired)
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;
        
        // Check if a write occurred during our read
        if (!lock.validate(stamp)) {
            // Fall back to regular read lock
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    // Regular read lock (when optimistic won't work)
    public double getX() {
        long stamp = lock.readLock();
        try {
            return x;
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
```

**StampedLock vs ReadWriteLock:**
- StampedLock has optimistic reads (no blocking)
- StampedLock is not reentrant
- StampedLock doesn't support Conditions
- StampedLock is faster for read-mostly scenarios

---

## ScheduledExecutorService: Delayed and Periodic Tasks

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Run once after delay
scheduler.schedule(() -> {
    System.out.println("Runs after 3 seconds");
}, 3, TimeUnit.SECONDS);

// Run repeatedly at fixed rate (start-to-start timing)
scheduler.scheduleAtFixedRate(() -> {
    System.out.println("Runs every 2 seconds (fixed rate)");
}, 1, 2, TimeUnit.SECONDS);  // Initial delay 1s, then every 2s

// Run repeatedly with fixed delay (end-to-start timing)
scheduler.scheduleWithFixedDelay(() -> {
    System.out.println("Runs 2 seconds after previous completes");
    sleep(1000);  // Takes 1 second
}, 1, 2, TimeUnit.SECONDS);  // 1s initial, 2s between completions

// Don't forget to shutdown eventually!
// scheduler.shutdown();
```

**Fixed Rate vs Fixed Delay:**
- **Fixed Rate**: Next execution starts X time after previous START
- **Fixed Delay**: Next execution starts X time after previous END

---

## Practical Complete Example

```java
import java.util.concurrent.*;
import java.util.*;

public class OrderProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    
    public CompletableFuture<OrderResult> processOrder(Order order) {
        // Validate order
        return validateOrder(order)
            // Then check inventory (depends on validation)
            .thenCompose(validated -> checkInventory(validated))
            // Then charge payment (depends on inventory)
            .thenCompose(inventoryOk -> chargePayment(order))
            // Then ship order (depends on payment)
            .thenCompose(paymentOk -> shipOrder(order))
            // Handle any errors
            .exceptionally(ex -> {
                System.err.println("Order failed: " + ex.getMessage());
                return new OrderResult("FAILED", ex.getMessage());
            })
            // Log completion
            .whenComplete((result, ex) -> {
                System.out.println("Order processing complete: " + result);
            });
    }
    
    private CompletableFuture<Boolean> validateOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Validating order...");
            // Validation logic
            return true;
        }, executor);
    }
    
    private CompletableFuture<Boolean> checkInventory(boolean validated) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Checking inventory...");
            return true;
        }, executor);
    }
    
    private CompletableFuture<Boolean> chargePayment(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Charging payment...");
            return true;
        }, executor);
    }
    
    private CompletableFuture<OrderResult> shipOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Shipping order...");
            return new OrderResult("SUCCESS", "Order shipped!");
        }, executor);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}

record Order(String id, String product, int quantity) {}
record OrderResult(String status, String message) {}
```

---

## More Practical Examples

### Example: Web Scraper with Rate Limiting

```java
import java.util.concurrent.*;

public class RateLimitedScraper {
    private final ExecutorService executor;
    private final Semaphore rateLimiter;
    
    public RateLimitedScraper(int maxConcurrent) {
        this.executor = Executors.newFixedThreadPool(maxConcurrent);
        this.rateLimiter = new Semaphore(maxConcurrent);
    }
    
    public CompletableFuture<String> scrape(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                rateLimiter.acquire();  // Wait for permit
                try {
                    System.out.println("Scraping: " + url);
                    Thread.sleep(1000);  // Simulate HTTP request
                    return "Content from " + url;
                } finally {
                    rateLimiter.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    public static void main(String[] args) throws Exception {
        RateLimitedScraper scraper = new RateLimitedScraper(3);  // Max 3 concurrent
        
        List<String> urls = List.of(
            "http://site1.com", "http://site2.com", "http://site3.com",
            "http://site4.com", "http://site5.com", "http://site6.com"
        );
        
        List<CompletableFuture<String>> futures = urls.stream()
            .map(scraper::scrape)
            .toList();
        
        // Wait for all and print results
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                futures.forEach(f -> {
                    try { System.out.println(f.get()); } 
                    catch (Exception e) { e.printStackTrace(); }
                });
                scraper.shutdown();
            });
        
        Thread.sleep(5000);  // Wait for completion
    }
}
```

### Example: Parallel Data Processing Pipeline

```java
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

public class DataPipeline {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    
    public CompletableFuture<List<ProcessedData>> process(List<RawData> rawData) {
        // Stage 1: Validate all data in parallel
        List<CompletableFuture<ValidatedData>> validationFutures = rawData.stream()
            .map(data -> CompletableFuture.supplyAsync(() -> validate(data), executor))
            .toList();
        
        // Stage 2: Transform validated data
        List<CompletableFuture<TransformedData>> transformFutures = validationFutures.stream()
            .map(f -> f.thenApplyAsync(this::transform, executor))
            .toList();
        
        // Stage 3: Enrich transformed data
        List<CompletableFuture<ProcessedData>> enrichFutures = transformFutures.stream()
            .map(f -> f.thenApplyAsync(this::enrich, executor))
            .toList();
        
        // Combine all results
        return CompletableFuture.allOf(enrichFutures.toArray(new CompletableFuture[0]))
            .thenApply(v -> enrichFutures.stream()
                .map(CompletableFuture::join)
                .toList());
    }
    
    private ValidatedData validate(RawData data) {
        System.out.println("Validating: " + data);
        return new ValidatedData(data.value());
    }
    
    private TransformedData transform(ValidatedData data) {
        System.out.println("Transforming: " + data);
        return new TransformedData(data.value().toUpperCase());
    }
    
    private ProcessedData enrich(TransformedData data) {
        System.out.println("Enriching: " + data);
        return new ProcessedData(data.value(), System.currentTimeMillis());
    }
    
    record RawData(String value) {}
    record ValidatedData(String value) {}
    record TransformedData(String value) {}
    record ProcessedData(String value, long timestamp) {}
}
```

### Example: Retry with Exponential Backoff

```java
import java.util.concurrent.*;

public class RetryHelper {
    
    public static <T> CompletableFuture<T> retryWithBackoff(
            Supplier<CompletableFuture<T>> operation,
            int maxRetries,
            long initialDelayMs) {
        
        return operation.get().exceptionallyCompose(ex -> {
            if (maxRetries <= 0) {
                return CompletableFuture.failedFuture(ex);
            }
            
            System.out.println("Retry in " + initialDelayMs + "ms due to: " + ex.getMessage());
            
            return CompletableFuture.supplyAsync(() -> null)
                .thenComposeAsync(v -> {
                    try {
                        Thread.sleep(initialDelayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return retryWithBackoff(operation, maxRetries - 1, initialDelayMs * 2);
                });
        });
    }
    
    public static void main(String[] args) throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);
        
        CompletableFuture<String> result = retryWithBackoff(
            () -> CompletableFuture.supplyAsync(() -> {
                int attempt = attempts.incrementAndGet();
                System.out.println("Attempt " + attempt);
                if (attempt < 3) {
                    throw new RuntimeException("Simulated failure");
                }
                return "Success on attempt " + attempt;
            }),
            5,      // Max retries
            100     // Initial delay ms
        );
        
        System.out.println("Result: " + result.get());
    }
}
```

### Example: Timeout with Fallback

```java
public class TimeoutWithFallback {
    
    public static <T> CompletableFuture<T> withTimeoutAndFallback(
            CompletableFuture<T> future,
            T fallback,
            long timeout,
            TimeUnit unit) {
        
        return future
            .completeOnTimeout(fallback, timeout, unit)
            .exceptionally(ex -> {
                System.out.println("Error, using fallback: " + ex.getMessage());
                return fallback;
            });
    }
    
    public static void main(String[] args) throws Exception {
        // Simulates slow service
        CompletableFuture<String> slowService = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
            return "Slow response";
        });
        
        String result = withTimeoutAndFallback(
            slowService,
            "Cached response (fallback)",
            2,
            TimeUnit.SECONDS
        ).get();
        
        System.out.println("Got: " + result);  // "Cached response (fallback)"
    }
}
```

---

## Common Mistakes and How to Avoid Them

| Mistake | Problem | Solution |
|---------|---------|----------|
| Not shutting down ExecutorService | Memory leak, app won't exit | Always call `shutdown()` in finally/try-with-resources |
| Blocking in CompletableFuture callbacks | Defeats async purpose | Use async variants or separate executor |
| Ignoring exceptions in Future.get() | Silent failures | Always handle ExecutionException |
| Using too many threads | Performance degrades | Use appropriate pool size (cores for CPU, more for I/O) |
| Not handling timeouts | Hanging forever | Use `get(timeout)` or `orTimeout()` |
| Modifying shared state in callbacks | Race conditions | Use atomic types or synchronization |
| Using CachedThreadPool for long tasks | Unbounded thread creation | Use FixedThreadPool or WorkStealingPool |
| Not propagating cancellation | Wasted resources | Check `Thread.interrupted()` in long loops |
| Forgetting to complete manually created CompletableFuture | Waiting forever | Always complete or completeExceptionally |

---

## Testing Concurrent Code

Testing multithreaded code is challenging. Here are some strategies.

### Using CountDownLatch for Synchronization

```java
import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTest {
    
    @Test
    void testConcurrentAccess() throws InterruptedException {
        Counter counter = new Counter();
        int numThreads = 10;
        int incrementsPerThread = 1000;
        
        CountDownLatch startLatch = new CountDownLatch(1);  // Start signal
        CountDownLatch doneLatch = new CountDownLatch(numThreads);  // Done signal
        
        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();  // Wait for start signal
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counter.increment();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }
        
        startLatch.countDown();  // Start all threads at once
        doneLatch.await();  // Wait for all to complete
        
        assertEquals(numThreads * incrementsPerThread, counter.get());
    }
}
```

### Testing CompletableFuture

```java
@Test
void testAsyncOperation() throws Exception {
    CompletableFuture<String> future = myService.asyncOperation();
    
    // Use get with timeout in tests
    String result = future.get(5, TimeUnit.SECONDS);
    assertEquals("expected", result);
}

@Test
void testAsyncOperationFailure() {
    CompletableFuture<String> future = myService.failingOperation();
    
    assertThrows(ExecutionException.class, () -> {
        future.get(5, TimeUnit.SECONDS);
    });
}
```

### Using awaitility Library (Recommended)

```java
// Add dependency: org.awaitility:awaitility

import static org.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;

@Test
void testAsyncWithAwaitility() {
    myService.startAsyncOperation();
    
    // Wait until condition is met
    await()
        .atMost(5, TimeUnit.SECONDS)
        .pollInterval(100, TimeUnit.MILLISECONDS)
        .until(() -> myService.isComplete());
    
    // Or check value
    await()
        .atMost(5, TimeUnit.SECONDS)
        .until(myService::getResult, equalTo("expected"));
}
```

### Detecting Race Conditions

```java
@Test
void testForRaceConditions() throws InterruptedException {
    // Run test multiple times to catch intermittent issues
    for (int run = 0; run < 100; run++) {
        Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    counter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        // If there's a race condition, this might fail on some runs
        assertEquals(10000, counter.get(), 
            "Race condition detected on run " + run);
    }
}
```

---

## Best Practices Summary

1. **Use ExecutorService, not raw Threads**
   - Better resource management
   - Reusable thread pools

2. **Always shut down executors**
   - Use try-with-resources (Java 19+) or finally blocks

3. **Choose the right pool**
   - Fixed pool for CPU-bound
   - Cached pool for I/O-bound
   - Virtual threads for massive I/O concurrency (Java 21+)

4. **Use CompletableFuture for async workflows**
   - Chain operations
   - Combine results
   - Handle errors elegantly

5. **Handle timeouts**
   - Never wait indefinitely
   - Use `orTimeout()` or `get(timeout, unit)`

6. **Consider virtual threads (Java 21+)**
   - Perfect for I/O-bound workloads
   - Can handle millions of concurrent tasks

---

## Cheat Sheet

### ExecutorService Quick Reference

```java
// Create
ExecutorService exec = Executors.newFixedThreadPool(4);
ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();  // Java 21+

// Submit
exec.execute(() -> doWork());  // No return
Future<T> f = exec.submit(() -> compute());  // With return

// Shutdown
exec.shutdown();
exec.awaitTermination(60, TimeUnit.SECONDS);
```

### CompletableFuture Quick Reference

```java
// Create
CompletableFuture.runAsync(() -> doWork());  // No return
CompletableFuture.supplyAsync(() -> getValue());  // With return

// Chain
.thenApply(v -> transform(v))      // Transform value
.thenCompose(v -> asyncCall(v))    // Chain futures
.thenAccept(v -> consume(v))       // Consume (no return)
.thenRun(() -> action())           // Run after (ignores result)

// Combine
.thenCombine(other, (a, b) -> combine(a, b))
CompletableFuture.allOf(f1, f2, f3)
CompletableFuture.anyOf(f1, f2, f3)

// Errors
.exceptionally(ex -> defaultValue)
.handle((result, ex) -> handleBoth())

// Timeout (Java 9+)
.orTimeout(5, TimeUnit.SECONDS)
.completeOnTimeout(defaultValue, 5, TimeUnit.SECONDS)
```

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Multithreading](./27-multithreading.md) | [Guide](../guide.md) | Coming Soon |
