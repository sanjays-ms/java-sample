# Multithreading Cheatsheet

[Back to Full Documentation](../documentation/27-multithreading.md)

---

## Creating Threads

```java
// Lambda (simplest)
Thread t = new Thread(() -> doWork());
t.start();

// Runnable
Thread t = new Thread(new MyRunnable());
t.start();

// Extending Thread
class MyThread extends Thread {
    public void run() { doWork(); }
}
new MyThread().start();

// With name
Thread t = new Thread(() -> doWork(), "MyThread");
```

---

## Thread Methods

| Method | Purpose |
|--------|---------|
| `start()` | Begin execution |
| `run()` | Code to execute (don't call directly) |
| `sleep(ms)` | Pause thread |
| `join()` | Wait for thread to finish |
| `join(ms)` | Wait with timeout |
| `yield()` | Hint to scheduler to let others run |
| `isAlive()` | Check if running |
| `interrupt()` | Request interruption |
| `isInterrupted()` | Check interrupt status |
| `Thread.interrupted()` | Check and clear interrupt flag |
| `getName()` / `setName()` | Thread name |
| `getPriority()` / `setPriority()` | Thread priority (1-10) |
| `setDaemon(true)` | Make daemon thread |
| `currentThread()` | Get current thread reference |

---

## Thread States

| State | Description |
|-------|-------------|
| `NEW` | Created, not started |
| `RUNNABLE` | Ready or running |
| `BLOCKED` | Waiting for lock |
| `WAITING` | Waiting indefinitely |
| `TIMED_WAITING` | Waiting with timeout |
| `TERMINATED` | Finished |

```java
Thread.State state = thread.getState();
```

---

## Synchronization

### synchronized Method

```java
public synchronized void method() {
    // Only one thread at a time
}
```

### synchronized Block

```java
synchronized (lockObject) {
    // Critical section
}
```

### Static Synchronization

```java
public static synchronized void method() {
    // Lock on class object
}

synchronized (MyClass.class) {
    // Same as above
}
```

---

## volatile

```java
private volatile boolean flag = true;
```

- Ensures visibility across threads
- Does NOT provide atomicity
- Use for simple flags

---

## Atomic Classes

```java
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();    // ++count
count.getAndIncrement();    // count++
count.addAndGet(5);         // count += 5
count.compareAndSet(expected, newValue);

// High-performance counter
LongAdder adder = new LongAdder();
adder.increment();
adder.add(10);
long sum = adder.sum();

// Other atomic classes
AtomicLong, AtomicBoolean, AtomicReference<T>
```

---

## ThreadLocal

```java
// Each thread gets its own value
ThreadLocal<String> userContext = new ThreadLocal<>();
ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

userContext.set("User123");
String user = userContext.get();
userContext.remove();  // Always clean up in thread pools!

// Child threads inherit value
InheritableThreadLocal<String> inherited = new InheritableThreadLocal<>();
```

---

## Thread Communication

```java
synchronized (lock) {
    while (!condition) {
        lock.wait();       // Release lock and wait
    }
    // Do work
}

synchronized (lock) {
    condition = true;
    lock.notify();         // Wake one waiting thread
    lock.notifyAll();      // Wake all waiting threads
}
```

---

## Common Patterns

### Basic Thread

```java
Thread t = new Thread(() -> {
    try {
        // Do work
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});
t.start();
t.join();  // Wait for completion
```

### Thread-Safe Counter

```java
class Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    public void increment() { count.incrementAndGet(); }
    public int get() { return count.get(); }
}
```

### Stoppable Thread

```java
class StoppableTask implements Runnable {
    private volatile boolean running = true;
    
    public void stop() { running = false; }
    
    public void run() {
        while (running) {
            // Do work
        }
    }
}
```

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| `run()` instead of `start()` | Use `start()` to create new thread |
| Shared data without sync | Use synchronized/atomic |
| Catching InterruptedException wrong | Restore interrupt: `Thread.currentThread().interrupt()` |
| Busy waiting | Use `wait()`/`notify()` |
| Deadlock | Consistent lock ordering |

---

## Daemon Threads

```java
Thread t = new Thread(() -> doWork());
t.setDaemon(true);  // Must set before start()
t.start();

// Daemon threads don't prevent JVM exit
```

---

## Thread-Safe Collections

```java
// Synchronized wrappers
List<T> list = Collections.synchronizedList(new ArrayList<>());
Map<K,V> map = Collections.synchronizedMap(new HashMap<>());

// Concurrent collections (better)
ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<>();
BlockingQueue<T> queue = new LinkedBlockingQueue<>();
```

---

## Quick Reference

```java
// Sleep
Thread.sleep(1000);
TimeUnit.SECONDS.sleep(2);

// Current thread
Thread current = Thread.currentThread();

// Wait for thread
thread.join();
thread.join(5000);  // With timeout

// Synchronized block
synchronized (lock) { ... }

// Atomic increment
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();

// Interrupt handling
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();  // Restore flag
}
```
