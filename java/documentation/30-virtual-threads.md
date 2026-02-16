# Virtual Threads in Java

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/virtual-threads-cheatsheet.md)

---

## What Are Virtual Threads?

Virtual threads (introduced in Java 21) are lightweight threads that allow you to write simple, blocking code that scales to handle massive concurrent workloads.

**In Plain Words:**
Imagine a restaurant with 10 waiters (platform threads). Each waiter can only serve one table at a time. If a customer is slow to order, the waiter just waits there doing nothing.

With virtual threads, it's like having thousands of "virtual waiters" that share those 10 physical waiters. When a virtual waiter is waiting for a customer to decide, the physical waiter goes to help someone else. When the customer is ready, any available physical waiter picks up where they left off.

**Visual Comparison:**

```
Platform Threads (Traditional):
┌─────────────────────────────────────────┐
│  OS Thread 1  │  OS Thread 2  │  ...    │
│  (1MB stack)  │  (1MB stack)  │         │
│               │               │         │
│   Task A      │   Task B      │         │
│   (waiting)   │   (waiting)   │         │
└─────────────────────────────────────────┘
Limited to thousands (memory constrained)

Virtual Threads (Java 21+):
┌─────────────────────────────────────────┐
│  Carrier Thread 1  │  Carrier Thread 2  │
│  (OS Thread)       │  (OS Thread)       │
│                    │                    │
│  ┌──┐ ┌──┐ ┌──┐   │  ┌──┐ ┌──┐ ┌──┐   │
│  │V1│ │V2│ │V3│   │  │V4│ │V5│ │V6│   │
│  └──┘ └──┘ └──┘   │  └──┘ └──┘ └──┘   │
│  Virtual threads   │  Virtual threads   │
└─────────────────────────────────────────┘
Can have millions (few KB each)
```

---

## Why Virtual Threads?

### The Problem with Platform Threads

| Issue | Description |
|-------|-------------|
| Heavy | Each platform thread uses ~1MB of memory for its stack |
| Limited | OS limits how many threads you can create (thousands) |
| Expensive | Creating and destroying threads has overhead |
| Wasteful | Threads often wait idle (I/O, network, database) |

### How Virtual Threads Help

| Benefit | Description |
|---------|-------------|
| Lightweight | Only a few KB per virtual thread |
| Massive scale | Can have millions of concurrent virtual threads |
| Simple code | Write blocking code that scales like async |
| No callback hell | No need for reactive/async complexity |

### When to Use Virtual Threads

| Workload Type | Use Virtual Threads? | Why |
|--------------|---------------------|-----|
| I/O-bound (network, file, DB) | Yes | Threads mostly wait, perfect fit |
| Many concurrent tasks | Yes | Can handle millions |
| HTTP server handling requests | Yes | Each request can have its own thread |
| CPU-bound computation | No | Virtual threads don't help here |
| Very short-lived tasks | Maybe | Overhead might not be worth it |

---

## Creating Virtual Threads

### Method 1: Thread.startVirtualThread()

```java
// Simplest way - start immediately
Thread vThread = Thread.startVirtualThread(() -> {
    System.out.println("Hello from virtual thread!");
    System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
});

// Wait for completion
vThread.join();
```

### Method 2: Thread.ofVirtual().start()

```java
// More control over thread creation
Thread vThread = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> {
        System.out.println("Running: " + Thread.currentThread().getName());
    });

vThread.join();
```

### Method 3: Thread.ofVirtual().unstarted()

```java
// Create without starting
Thread vThread = Thread.ofVirtual()
    .name("worker-1")
    .unstarted(() -> {
        System.out.println("Working...");
    });

// Start later
vThread.start();
vThread.join();
```

### Method 4: Thread.Builder with Factory

```java
// Create a factory for naming threads
Thread.Builder builder = Thread.ofVirtual().name("worker-", 0);

Thread t1 = builder.start(() -> doWork());  // worker-0
Thread t2 = builder.start(() -> doWork());  // worker-1
Thread t3 = builder.start(() -> doWork());  // worker-2
```

### Method 5: ThreadFactory

```java
// Create a thread factory
ThreadFactory factory = Thread.ofVirtual()
    .name("vthread-", 0)
    .factory();

// Use the factory
Thread t1 = factory.newThread(() -> task1());
Thread t2 = factory.newThread(() -> task2());

t1.start();
t2.start();
```

---

## Virtual Thread Executor

The most common way to use virtual threads is with an ExecutorService.

### newVirtualThreadPerTaskExecutor()

```java
// Creates a new virtual thread for each task
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    
    // Submit many tasks - each gets its own virtual thread
    for (int i = 0; i < 10_000; i++) {
        final int taskId = i;
        executor.submit(() -> {
            System.out.println("Task " + taskId + " on " + 
                              Thread.currentThread());
            Thread.sleep(1000);  // Simulated I/O wait
            return "Result " + taskId;
        });
    }
    
}  // Automatically shuts down and waits for all tasks
```

### Collecting Results from Many Tasks

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    
    List<String> urls = List.of(
        "http://api1.example.com",
        "http://api2.example.com",
        "http://api3.example.com"
    );
    
    // Submit all tasks and collect futures
    List<Future<String>> futures = urls.stream()
        .map(url -> executor.submit(() -> fetchUrl(url)))
        .toList();
    
    // Collect results
    List<String> results = new ArrayList<>();
    for (Future<String> future : futures) {
        results.add(future.get());  // Blocks until each completes
    }
    
    System.out.println("All results: " + results);
}
```

### Handling Exceptions

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    
    Future<String> future = executor.submit(() -> {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Random failure!");
        }
        return "Success";
    });
    
    try {
        String result = future.get();
        System.out.println(result);
    } catch (ExecutionException e) {
        System.err.println("Task failed: " + e.getCause().getMessage());
    }
}
```

---

## Virtual Threads vs Platform Threads

### Creating Platform Threads (for comparison)

```java
// Platform thread
Thread platformThread = Thread.ofPlatform()
    .name("platform-worker")
    .start(() -> doWork());

// Check thread type
System.out.println(platformThread.isVirtual());  // false

// Virtual thread
Thread virtualThread = Thread.ofVirtual()
    .name("virtual-worker")
    .start(() -> doWork());

System.out.println(virtualThread.isVirtual());  // true
```

### Key Differences

| Aspect | Platform Threads | Virtual Threads |
|--------|-----------------|-----------------|
| Memory | ~1MB per thread | Few KB per thread |
| Maximum count | Thousands | Millions |
| OS mapping | 1:1 with OS thread | Many:1 with carrier thread |
| Scheduling | OS scheduler | JVM scheduler |
| Stack | Fixed size | Grows/shrinks dynamically |
| Best for | CPU-bound work | I/O-bound work |
| Blocking | Wastes resources | Yields carrier thread |

### Demonstration: Scaling Comparison

```java
public class ScalingComparison {
    
    public static void main(String[] args) throws Exception {
        int taskCount = 100_000;
        
        // This would likely crash or run very slowly with platform threads
        // due to memory exhaustion
        
        System.out.println("Starting " + taskCount + " virtual threads...");
        long start = System.currentTimeMillis();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            
            for (int i = 0; i < taskCount; i++) {
                futures.add(executor.submit(() -> {
                    Thread.sleep(1000);  // Simulate network I/O
                    return null;
                }));
            }
            
            // Wait for all
            for (Future<?> future : futures) {
                future.get();
            }
        }
        
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Completed in " + elapsed + "ms");
        // Completes in ~1-2 seconds, not 100,000 seconds!
    }
}
```

---

## How Virtual Threads Work

### Carrier Threads and Mounting

Virtual threads run on "carrier threads" (platform threads). The JVM manages mounting and unmounting virtual threads from carriers.

```
Virtual Thread 1 starts on Carrier A
    │
    ▼
Virtual Thread 1 blocks (I/O wait)
    │
    ▼
Virtual Thread 1 unmounts from Carrier A
    │
    ├── Carrier A picks up Virtual Thread 2
    │
    ▼
I/O completes
    │
    ▼
Virtual Thread 1 mounts on Carrier B (or A)
    │
    ▼
Virtual Thread 1 continues execution
```

### What Happens When Virtual Threads Block

```java
Thread.startVirtualThread(() -> {
    // Virtual thread is mounted on a carrier
    System.out.println("Starting...");
    
    // Blocking call - virtual thread unmounts!
    // Carrier thread is free to run other virtual threads
    String data = httpClient.send(request).body();
    
    // Virtual thread mounts again (maybe different carrier)
    System.out.println("Got: " + data);
});
```

**Operations that unmount (yield) the virtual thread:**
- `Thread.sleep()`
- `BlockingQueue.take()`
- `Lock.lock()` (on virtual-thread-friendly locks)
- I/O operations (socket, file)
- `Future.get()`
- `CountDownLatch.await()`

---

## Thread Pinning

"Pinning" occurs when a virtual thread cannot unmount from its carrier, reducing concurrency.

### Causes of Pinning

1. **Synchronized blocks/methods:**
```java
Thread.startVirtualThread(() -> {
    synchronized (lock) {  // Virtual thread is PINNED here!
        // If this blocks, carrier thread is blocked too
        Thread.sleep(1000);  // Bad - carrier thread wasted
    }
});
```

2. **Native methods or foreign functions**

### Avoiding Pinning

```java
import java.util.concurrent.locks.ReentrantLock;

// AVOID: synchronized (causes pinning)
synchronized (lock) {
    doBlockingOperation();
}

// PREFER: ReentrantLock (no pinning)
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    doBlockingOperation();  // Virtual thread can unmount
} finally {
    lock.unlock();
}
```

### Detecting Pinning

```bash
# Run with JVM flag to log pinning
java -Djdk.tracePinnedThreads=full MyApp
# or
java -Djdk.tracePinnedThreads=short MyApp
```

---

## Structured Concurrency (Preview)

Structured concurrency (Java 21+ preview) provides better control over groups of virtual threads.

### ShutdownOnFailure Scope

```java
import java.util.concurrent.StructuredTaskScope;

public String fetchUserData(int userId) throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        
        // Fork subtasks (each in its own virtual thread)
        var userFuture = scope.fork(() -> fetchUser(userId));
        var ordersFuture = scope.fork(() -> fetchOrders(userId));
        var prefsFuture = scope.fork(() -> fetchPreferences(userId));
        
        // Wait for all tasks to complete (or first failure)
        scope.join();
        
        // Throw if any task failed
        scope.throwIfFailed();
        
        // All succeeded - get results
        User user = userFuture.get();
        List<Order> orders = ordersFuture.get();
        Preferences prefs = prefsFuture.get();
        
        return formatUserData(user, orders, prefs);
    }
    // If scope exits early, all incomplete subtasks are cancelled
}
```

### ShutdownOnSuccess Scope

```java
public String fetchFromFastestMirror(List<String> mirrorUrls) throws Exception {
    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
        
        // Race all mirrors - first success wins
        for (String url : mirrorUrls) {
            scope.fork(() -> fetchFromUrl(url));
        }
        
        // Wait for first success (or all to fail)
        scope.join();
        
        // Get the winning result
        return scope.result();
    }
    // Other still-running tasks are automatically cancelled
}
```

### Benefits of Structured Concurrency

| Benefit | Description |
|---------|-------------|
| Automatic cleanup | Subtasks are cancelled when scope exits |
| Clear ownership | Parent scope owns all child tasks |
| Better stack traces | Relationship between tasks is preserved |
| Easier error handling | Exceptions propagate naturally |
| Resource safety | No leaked threads |

---

## Practical Examples

### Example 1: HTTP Server Handling Requests

```java
import com.sun.net.httpserver.*;
import java.util.concurrent.*;

public class VirtualThreadServer {
    
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0);
        
        // Each request handled by a virtual thread
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        
        server.createContext("/api/data", exchange -> {
            // This can block without wasting resources
            String data = fetchFromDatabase();
            
            byte[] response = data.getBytes();
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        
        server.start();
        System.out.println("Server started on port 8080");
    }
}
```

### Example 2: Parallel API Aggregation

```java
public class ApiAggregator {
    private final HttpClient client = HttpClient.newHttpClient();
    
    public DashboardData fetchDashboard(String userId) throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Fetch all data in parallel
            Future<UserProfile> profileFuture = executor.submit(() -> 
                fetchProfile(userId));
            Future<List<Notification>> notificationsFuture = executor.submit(() -> 
                fetchNotifications(userId));
            Future<List<Order>> ordersFuture = executor.submit(() -> 
                fetchRecentOrders(userId));
            Future<AccountBalance> balanceFuture = executor.submit(() -> 
                fetchBalance(userId));
            
            // Combine results
            return new DashboardData(
                profileFuture.get(),
                notificationsFuture.get(),
                ordersFuture.get(),
                balanceFuture.get()
            );
        }
    }
    
    private UserProfile fetchProfile(String userId) {
        // HTTP call - virtual thread yields while waiting
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://api/users/" + userId))
            .build();
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        return parseProfile(response.body());
    }
    // ... other fetch methods
}
```

### Example 3: Database Connection Pool Simulation

```java
public class DatabaseSimulation {
    
    public static void main(String[] args) throws Exception {
        // Simulate 10,000 concurrent database queries
        int queryCount = 10_000;
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            List<Future<String>> futures = new ArrayList<>();
            
            for (int i = 0; i < queryCount; i++) {
                final int queryId = i;
                futures.add(executor.submit(() -> {
                    // Simulate database query (I/O bound)
                    Thread.sleep(100);  // Simulate query latency
                    return "Result for query " + queryId;
                }));
            }
            
            // Process results as they complete
            int completed = 0;
            for (Future<String> future : futures) {
                String result = future.get();
                completed++;
                if (completed % 1000 == 0) {
                    System.out.println("Completed: " + completed);
                }
            }
        }
    }
}
```

### Example 4: Web Crawler

```java
public class WebCrawler {
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    
    public void crawl(String startUrl, int maxDepth) {
        crawlPage(startUrl, 0, maxDepth);
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void crawlPage(String url, int depth, int maxDepth) {
        if (depth > maxDepth || !visited.add(url)) {
            return;
        }
        
        executor.submit(() -> {
            try {
                System.out.println("Crawling: " + url + " (depth " + depth + ")");
                
                // Fetch page (blocks, but yields virtual thread)
                String content = fetchPage(url);
                
                // Extract links
                List<String> links = extractLinks(content);
                
                // Crawl found links in parallel
                for (String link : links) {
                    crawlPage(link, depth + 1, maxDepth);
                }
            } catch (Exception e) {
                System.err.println("Failed to crawl: " + url);
            }
        });
    }
}
```

### Example 5: Rate-Limited API Client

```java
public class RateLimitedClient {
    private final Semaphore rateLimiter;
    private final ExecutorService executor;
    
    public RateLimitedClient(int maxConcurrent) {
        this.rateLimiter = new Semaphore(maxConcurrent);
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    public <T> CompletableFuture<T> request(Supplier<T> apiCall) {
        CompletableFuture<T> future = new CompletableFuture<>();
        
        executor.submit(() -> {
            try {
                rateLimiter.acquire();  // Virtual thread yields here if needed
                try {
                    T result = apiCall.get();
                    future.complete(result);
                } finally {
                    rateLimiter.release();
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}

// Usage
RateLimitedClient client = new RateLimitedClient(10);  // Max 10 concurrent

List<CompletableFuture<String>> futures = urls.stream()
    .map(url -> client.request(() -> fetchUrl(url)))
    .toList();

// Wait for all
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
```

---

## ThreadLocal with Virtual Threads

ThreadLocal works with virtual threads, but be careful about memory usage.

### Potential Issue

```java
// Each virtual thread gets its own ThreadLocal value
// With millions of virtual threads, this can use lots of memory!
private static ThreadLocal<byte[]> buffer = ThreadLocal.withInitial(() -> 
    new byte[1024 * 1024]);  // 1MB per virtual thread = danger!
```

### Better Approach: Scoped Values (Preview)

```java
// Java 21+ Preview: ScopedValue
// More efficient for virtual threads
import jdk.incubator.concurrent.ScopedValue;

final static ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

void handleRequest(User user, Runnable task) {
    ScopedValue.where(CURRENT_USER, user).run(task);
}

void processData() {
    User user = CURRENT_USER.get();  // Available in this scope
}
```

---

## Common Mistakes and How to Avoid Them

| Mistake | Problem | Solution |
|---------|---------|----------|
| Using synchronized with blocking | Pins virtual thread | Use ReentrantLock |
| Large ThreadLocal values | Memory explosion | Use ScopedValue or smaller values |
| CPU-bound work in virtual threads | No benefit, possible overhead | Use platform thread pool |
| Pooling virtual threads | Unnecessary, defeats purpose | Create new virtual threads freely |
| Not closing executor | Resource leak | Use try-with-resources |
| Ignoring pinning warnings | Poor performance | Check logs, replace synchronized |

### Anti-Patterns

```java
// WRONG: Pooling virtual threads (unnecessary)
ExecutorService pool = Executors.newFixedThreadPool(100);
// Then using virtual threads in pool tasks - no benefit!

// CORRECT: Virtual thread per task
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();


// WRONG: CPU-bound work
executor.submit(() -> {
    // Pure computation - no I/O, no waiting
    return fibonacci(45);  // Virtual thread adds overhead, no benefit
});

// CORRECT: Use platform threads for CPU-bound work
ExecutorService cpuPool = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors());
cpuPool.submit(() -> fibonacci(45));
```

---

## Best Practices Summary

1. **Use virtual threads for I/O-bound workloads**
   - Network calls, database queries, file operations
   - Any code that waits more than computes

2. **Use newVirtualThreadPerTaskExecutor()**
   - One virtual thread per task
   - Don't pool or reuse virtual threads

3. **Replace synchronized with ReentrantLock**
   - Avoids pinning
   - Better virtual thread compatibility

4. **Don't limit virtual thread count**
   - Let the JVM handle it
   - They're designed to scale to millions

5. **Write simple blocking code**
   - No need for callbacks or reactive patterns
   - Blocking is fine with virtual threads

6. **Monitor for pinning**
   - Use `-Djdk.tracePinnedThreads=short`
   - Fix synchronized blocks

7. **Use structured concurrency for related tasks**
   - Clear ownership and cleanup
   - Better error handling

---

## Cheat Sheet

### Creating Virtual Threads

```java
// Immediate start
Thread.startVirtualThread(() -> task());

// Builder pattern
Thread.ofVirtual().name("worker").start(() -> task());

// Factory
ThreadFactory factory = Thread.ofVirtual().factory();

// Executor (most common)
ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();
```

### Checking Thread Type

```java
Thread.currentThread().isVirtual()  // true for virtual threads
```

### Structured Concurrency (Preview)

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var f1 = scope.fork(() -> task1());
    var f2 = scope.fork(() -> task2());
    scope.join();
    scope.throwIfFailed();
    return combine(f1.get(), f2.get());
}
```

### Avoiding Pinning

```java
// Replace synchronized with ReentrantLock
ReentrantLock lock = new ReentrantLock();
lock.lock();
try { /* blocking ops OK */ } finally { lock.unlock(); }
```

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Optional](./29-optional.md) | [Guide](../guide.md) | Coming Soon |
