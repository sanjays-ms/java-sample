# Multithreading in Java

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/multithreading-cheatsheet.md)

---

## What Is Multithreading?

Multithreading is a way to run multiple tasks at the same time within a single program.

**In Plain Words:**
Imagine you are cooking dinner. Instead of doing one thing at a time (boil water, then chop vegetables, then set the table), you do them simultaneously. The water boils while you chop vegetables. This is like multithreading - doing multiple things at once.

**Visual Representation:**

```
Single-threaded (one thing at a time):
[Boil Water]---->[Chop Vegetables]---->[Set Table]---->[Done]

Multi-threaded (multiple things at once):
[Boil Water]-------->
[Chop Vegetables]--->  [Done]
[Set Table]--------->
```

---

## Key Terms Explained

Before we dive in, let's understand the important terms:

| Term | What It Means | Real-World Analogy |
|------|--------------|-------------------|
| **Thread** | A single path of execution in your program | One worker doing one task |
| **Process** | The entire program running | The whole restaurant |
| **Multithreading** | Multiple threads in one process | Multiple workers in one restaurant |
| **Concurrency** | Multiple tasks making progress (may take turns) | One chef switching between dishes |
| **Parallelism** | Multiple tasks running at the exact same time | Multiple chefs cooking simultaneously |
| **Main Thread** | The primary thread that starts your program | The head chef |

**Important Distinction:**
- **Concurrency** = Dealing with many things at once (structure)
- **Parallelism** = Doing many things at once (execution)

A single-core CPU can have concurrency (switching between tasks quickly) but not true parallelism. A multi-core CPU can have both.

---

## Why Use Multithreading?

| Benefit | Description | Example |
|---------|-------------|---------|
| **Responsiveness** | UI stays interactive while work happens in background | App remains usable while downloading |
| **Performance** | Use all CPU cores for faster processing | Process large dataset 4x faster on 4 cores |
| **Resource Efficiency** | Threads share memory (less overhead than processes) | Multiple tasks use same data |
| **Better User Experience** | No freezing or "not responding" | Smooth animations while loading |
| **Background Operations** | Handle tasks without blocking main flow | Auto-save, notifications, logging |

**When NOT to Use Multithreading:**
- Simple sequential tasks
- When the overhead of thread management outweighs benefits
- When tasks are I/O bound and waiting anyway (consider async I/O instead)
- When shared state makes synchronization too complex

---

## Creating Threads

Java provides several ways to create threads. Let's explore each one.

### Method 1: Extending the Thread Class

You create a new class that extends `Thread` and override the `run()` method.

```java
// Step 1: Create a class that extends Thread
class MyThread extends Thread {
    
    private String taskName;
    
    public MyThread(String taskName) {
        this.taskName = taskName;
    }
    
    // Step 2: Override the run() method
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(taskName + " - Count: " + i);
            try {
                Thread.sleep(500);  // Pause for 500 milliseconds
            } catch (InterruptedException e) {
                System.out.println(taskName + " was interrupted");
            }
        }
        System.out.println(taskName + " finished!");
    }
}

// Step 3: Create and start the thread
public class Main {
    public static void main(String[] args) {
        MyThread thread1 = new MyThread("Thread-A");
        MyThread thread2 = new MyThread("Thread-B");
        
        // start() creates a new thread and calls run()
        thread1.start();
        thread2.start();
        
        System.out.println("Main thread continues...");
    }
}
```

**Output (order may vary):**
```
Main thread continues...
Thread-A - Count: 1
Thread-B - Count: 1
Thread-A - Count: 2
Thread-B - Count: 2
Thread-A - Count: 3
Thread-B - Count: 3
Thread-A finished!
Thread-B finished!
```

**Line-by-Line Explanation:**
- `extends Thread`: Our class inherits from the Thread class
- `run()`: Contains the code that will execute in the new thread
- `Thread.sleep(500)`: Pauses the thread for 500 milliseconds
- `start()`: Creates a new thread of execution and calls `run()` in that new thread
- Notice "Main thread continues..." prints first - the main thread doesn't wait

---

### Method 2: Implementing the Runnable Interface (Preferred)

You create a class that implements `Runnable` and pass it to a `Thread` constructor.

```java
// Step 1: Create a class that implements Runnable
class MyRunnable implements Runnable {
    
    private String taskName;
    
    public MyRunnable(String taskName) {
        this.taskName = taskName;
    }
    
    // Step 2: Implement the run() method
    @Override
    public void run() {
        System.out.println(taskName + " is running on: " + 
                          Thread.currentThread().getName());
        
        // Simulate some work
        for (int i = 0; i < 3; i++) {
            System.out.println(taskName + " working... " + i);
        }
    }
}

// Step 3: Pass the Runnable to a Thread
public class Main {
    public static void main(String[] args) {
        // Create the Runnable task
        MyRunnable task1 = new MyRunnable("Task-1");
        MyRunnable task2 = new MyRunnable("Task-2");
        
        // Wrap in Thread objects
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        
        // Optionally set thread names
        thread1.setName("Worker-Thread-1");
        thread2.setName("Worker-Thread-2");
        
        // Start the threads
        thread1.start();
        thread2.start();
    }
}
```

**Output:**
```
Task-1 is running on: Worker-Thread-1
Task-2 is running on: Worker-Thread-2
Task-1 working... 0
Task-2 working... 0
Task-1 working... 1
Task-2 working... 1
Task-1 working... 2
Task-2 working... 2
```

---

### Method 3: Lambda Expression (Java 8+ - Simplest)

Since `Runnable` is a functional interface (has only one abstract method), you can use a lambda.

```java
public class Main {
    public static void main(String[] args) {
        
        // Simple lambda
        Thread thread1 = new Thread(() -> {
            System.out.println("Hello from thread 1!");
        });
        
        // Lambda with multiple statements
        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            for (int i = 0; i < 3; i++) {
                System.out.println(threadName + " counting: " + i);
            }
        });
        thread2.setName("Counter-Thread");
        
        // One-liner for simple tasks
        Thread thread3 = new Thread(() -> System.out.println("Quick task!"));
        
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
```

---

### Method 4: Anonymous Inner Class

Before lambdas, this was the common approach for quick thread creation.

```java
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Anonymous runnable running");
    }
});
thread.start();
```

---

## Thread vs Runnable: Which to Choose?

| Aspect | Extending Thread | Implementing Runnable |
|--------|-----------------|----------------------|
| **Inheritance** | Cannot extend another class | Can extend another class |
| **Reusability** | Less reusable | More reusable (same task, different threads) |
| **Separation** | Mixes task logic with thread management | Clean separation of concerns |
| **Flexibility** | Limited | Can use with ExecutorService |
| **Best Practice** | Avoid | Preferred |

**Why Runnable is Better:**
1. Java allows only single inheritance - if you extend `Thread`, you cannot extend anything else
2. Runnable represents a task, Thread represents execution - separation of concerns
3. Same Runnable can be executed by different threads or thread pools
4. Works with the modern `ExecutorService` framework

```java
// Example: Same Runnable, multiple threads
Runnable sharedTask = () -> System.out.println("Executing on: " + 
                                                Thread.currentThread().getName());

new Thread(sharedTask, "Thread-1").start();
new Thread(sharedTask, "Thread-2").start();
new Thread(sharedTask, "Thread-3").start();
```

---

## start() vs run() - Critical Difference!

This is one of the most common mistakes beginners make.

```java
Thread thread = new Thread(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});

// WRONG: Calling run() directly
thread.run();  // Runs in main thread, NOT a new thread!

// CORRECT: Calling start()
thread.start();  // Creates new thread, then runs run() in that thread
```

**Demonstration:**

```java
public class StartVsRun {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Thread name: " + Thread.currentThread().getName());
        });
        
        System.out.println("Using run():");
        thread.run();  // Prints "Thread name: main"
        
        System.out.println("\nUsing start():");
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread name: " + Thread.currentThread().getName());
        });
        thread2.start();  // Prints "Thread name: Thread-0" (or similar)
    }
}
```

**Output:**
```
Using run():
Thread name: main

Using start():
Thread name: Thread-0
```

| Method | What Happens |
|--------|-------------|
| `run()` | Executes the code in the CURRENT thread (no new thread created) |
| `start()` | Creates a NEW thread, then calls `run()` in that new thread |

---

## Thread Lifecycle and States

A thread goes through several states during its life:

```
         start()
   NEW ─────────► RUNNABLE ◄─────────────────────────────┐
    │                │                                    │
    │                │ CPU schedules                      │
    │                ▼                                    │
    │            RUNNING                                  │
    │                │                                    │
    │    ┌───────────┼───────────────────┐               │
    │    │           │                   │               │
    │    ▼           ▼                   ▼               │
    │ BLOCKED    WAITING         TIMED_WAITING           │
    │    │           │                   │               │
    │    └───────────┴───────────────────┘               │
    │                │                                    │
    │                │ condition met / notify / timeout   │
    │                └────────────────────────────────────┘
    │
    │                run() completes
    └──────────────────────────► TERMINATED
```

### Thread States Explained

| State | Description | How It Happens |
|-------|-------------|----------------|
| **NEW** | Thread object created but not started | `new Thread()` |
| **RUNNABLE** | Ready to run or currently running | After `start()` is called |
| **BLOCKED** | Waiting to acquire a lock | Trying to enter `synchronized` block held by another thread |
| **WAITING** | Waiting indefinitely for another thread | `wait()`, `join()`, `LockSupport.park()` |
| **TIMED_WAITING** | Waiting for a specified time | `sleep(ms)`, `wait(ms)`, `join(ms)` |
| **TERMINATED** | Thread has completed execution | `run()` method returns or exception thrown |

### Checking Thread State

```java
public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        System.out.println("After creation: " + thread.getState());  // NEW
        
        thread.start();
        System.out.println("After start(): " + thread.getState());   // RUNNABLE
        
        Thread.sleep(100);
        System.out.println("While sleeping: " + thread.getState());  // TIMED_WAITING
        
        thread.join();
        System.out.println("After completion: " + thread.getState()); // TERMINATED
    }
}
```

**Output:**
```
After creation: NEW
After start(): RUNNABLE
While sleeping: TIMED_WAITING
After completion: TERMINATED
```

---

## Essential Thread Methods

### sleep() - Pause Execution

Makes the current thread pause for a specified time.

```java
public class SleepExample {
    public static void main(String[] args) {
        System.out.println("Starting...");
        
        try {
            System.out.println("Sleeping for 2 seconds...");
            Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
            System.out.println("Woke up!");
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted!");
        }
        
        // Using TimeUnit (more readable)
        try {
            TimeUnit.SECONDS.sleep(1);      // 1 second
            TimeUnit.MILLISECONDS.sleep(500); // 500 ms
            TimeUnit.MINUTES.sleep(1);      // 1 minute (use carefully!)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

**Important Points:**
- `sleep()` is a static method - always affects the CURRENT thread
- Does NOT release locks held by the thread
- Can be interrupted by another thread calling `interrupt()`
- Specify time in milliseconds or use `TimeUnit`

---

### join() - Wait for Thread to Complete

The calling thread waits until the specified thread finishes.

```java
public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        Thread downloadThread = new Thread(() -> {
            System.out.println("Downloading file...");
            try {
                Thread.sleep(3000);  // Simulate download
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Download complete!");
        });
        
        Thread processThread = new Thread(() -> {
            System.out.println("Processing file...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Processing complete!");
        });
        
        // Start download
        downloadThread.start();
        
        // Wait for download to complete before processing
        downloadThread.join();  // Main thread waits here
        
        // Now start processing
        processThread.start();
        processThread.join();
        
        System.out.println("All done!");
    }
}
```

**Output:**
```
Downloading file...
Download complete!
Processing file...
Processing complete!
All done!
```

**join() with timeout:**

```java
Thread longTask = new Thread(() -> {
    try {
        Thread.sleep(10000);  // 10 seconds
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});

longTask.start();

// Wait maximum 3 seconds
longTask.join(3000);

if (longTask.isAlive()) {
    System.out.println("Thread still running after 3 seconds!");
} else {
    System.out.println("Thread finished within 3 seconds");
}
```

---

### interrupt() - Request Thread to Stop

Politely asks a thread to stop what it's doing.

```java
public class InterruptExample {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try {
                System.out.println("Worker: Starting long task...");
                
                for (int i = 0; i < 10; i++) {
                    // Check if interrupted
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Worker: I was interrupted! Cleaning up...");
                        return;  // Exit gracefully
                    }
                    
                    System.out.println("Worker: Working... " + i);
                    Thread.sleep(1000);
                }
                
                System.out.println("Worker: Finished!");
            } catch (InterruptedException e) {
                // sleep() was interrupted
                System.out.println("Worker: Sleep was interrupted! Stopping...");
            }
        });
        
        worker.start();
        
        // Let it work for 3 seconds
        Thread.sleep(3000);
        
        // Request the thread to stop
        System.out.println("Main: Sending interrupt signal...");
        worker.interrupt();
        
        worker.join();
        System.out.println("Main: Worker has stopped");
    }
}
```

**Output:**
```
Worker: Starting long task...
Worker: Working... 0
Worker: Working... 1
Worker: Working... 2
Main: Sending interrupt signal...
Worker: Sleep was interrupted! Stopping...
Main: Worker has stopped
```

**Two Ways Interrupt Works:**
1. If thread is sleeping/waiting: throws `InterruptedException`
2. If thread is running: sets interrupt flag (check with `isInterrupted()`)

---

### Other Useful Methods

```java
Thread thread = new Thread(() -> doWork());

// Naming
thread.setName("MyWorker");
String name = thread.getName();

// Priority (1-10, default is 5)
thread.setPriority(Thread.MAX_PRIORITY);  // 10
thread.setPriority(Thread.MIN_PRIORITY);  // 1
thread.setPriority(Thread.NORM_PRIORITY); // 5

// Daemon thread (background thread that doesn't prevent JVM exit)
thread.setDaemon(true);

// Get current thread
Thread current = Thread.currentThread();
System.out.println("Current thread: " + current.getName());

// Check if alive
boolean running = thread.isAlive();

// Thread ID (unique identifier)
long id = thread.getId();
```

---

## Thread Priority

Thread priority is a hint to the scheduler about relative importance.

```java
public class PriorityExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + 
                                  " (Priority: " + Thread.currentThread().getPriority() + 
                                  ") - " + i);
            }
        };
        
        Thread lowPriority = new Thread(task, "Low-Priority");
        Thread highPriority = new Thread(task, "High-Priority");
        
        lowPriority.setPriority(Thread.MIN_PRIORITY);   // 1
        highPriority.setPriority(Thread.MAX_PRIORITY);  // 10
        
        lowPriority.start();
        highPriority.start();
    }
}
```

**Important:** Priority is only a HINT to the OS scheduler. It does not guarantee execution order. Different operating systems handle priorities differently.

---

## Thread.yield() - Hint to Scheduler

`yield()` suggests that the current thread is willing to give up its current use of CPU.

```java
public class YieldExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);
                Thread.yield();  // Hint: "I'm willing to pause, let others run"
            }
        };
        
        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }
}
```

**Key Points:**
- `yield()` is a static method (affects current thread)
- It's only a hint - scheduler may ignore it
- Does NOT release any locks
- Rarely used in practice (prefer higher-level coordination)

---

## Daemon Threads

Daemon threads are background threads that don't prevent the JVM from exiting.

**What This Means:**
- When all non-daemon threads finish, JVM exits
- Daemon threads are abruptly stopped when JVM exits
- Use for background tasks like garbage collection, auto-save

```java
public class DaemonExample {
    public static void main(String[] args) throws InterruptedException {
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon: Background work...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        // Must set before starting!
        daemonThread.setDaemon(true);
        daemonThread.start();
        
        System.out.println("Main: Doing some work...");
        Thread.sleep(3000);
        
        System.out.println("Main: Exiting...");
        // JVM exits, daemon thread is abruptly stopped
    }
}
```

**Output:**
```
Main: Doing some work...
Daemon: Background work...
Daemon: Background work...
Daemon: Background work...
Main: Exiting...
```

---

## Race Conditions and Why Synchronization Matters

When multiple threads access shared data, unexpected problems can occur.

### The Problem: Race Condition

```java
public class RaceConditionDemo {
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Runnable incrementTask = () -> {
            for (int i = 0; i < 10000; i++) {
                counter++;  // This is NOT atomic!
            }
        };
        
        Thread t1 = new Thread(incrementTask);
        Thread t2 = new Thread(incrementTask);
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Expected: 20000");
        System.out.println("Actual: " + counter);  // Often less than 20000!
    }
}
```

**Why This Happens:**

The `counter++` operation looks simple but is actually three steps:
1. **Read** the current value of counter
2. **Increment** the value
3. **Write** the new value back

```
Thread 1                    Thread 2
--------                    --------
Read counter (= 5)          
                            Read counter (= 5)
Increment (= 6)             
                            Increment (= 6)
Write counter (= 6)         
                            Write counter (= 6)

Result: counter = 6 (should be 7!)
```

This is called a **race condition** - the final result depends on the timing/order of thread execution.

---

## Synchronization Solutions

### Solution 1: synchronized Keyword (Method Level)

```java
public class SynchronizedCounter {
    private int count = 0;
    
    // Only one thread can execute this method at a time
    public synchronized void increment() {
        count++;
    }
    
    public synchronized void decrement() {
        count--;
    }
    
    public synchronized int getCount() {
        return count;
    }
}

// Usage
public class Main {
    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Count: " + counter.getCount());  // Always 20000!
    }
}
```

**How It Works:**
- Each object has an implicit lock (called a monitor)
- When a thread enters a `synchronized` method, it acquires the lock
- Other threads must wait until the lock is released
- Lock is released when the method returns (or throws exception)

---

### Solution 2: synchronized Block

More fine-grained control - only synchronize the critical section.

```java
public class SynchronizedBlockExample {
    private int count = 0;
    private final Object lock = new Object();  // Dedicated lock object
    
    public void increment() {
        // Non-synchronized code can run here...
        
        synchronized (lock) {  // Only this block is synchronized
            count++;
        }
        
        // ...and here
    }
    
    // You can also synchronize on 'this'
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }
}
```

**When to Use Block vs Method:**
- **Method**: When the entire method needs to be synchronized
- **Block**: When only part of the method needs synchronization (better performance)

---

### Solution 3: Static Synchronization

For synchronizing static methods or class-level data.

```java
public class StaticSynchronization {
    private static int globalCount = 0;
    
    // Lock on the Class object
    public static synchronized void incrementGlobal() {
        globalCount++;
    }
    
    // Equivalent using synchronized block
    public static void decrementGlobal() {
        synchronized (StaticSynchronization.class) {
            globalCount--;
        }
    }
}
```

**Lock Objects:**
- Instance method: locks on `this` (the object instance)
- Static method: locks on the `Class` object (`MyClass.class`)

---

### Solution 4: Atomic Classes (Best for Simple Operations)

Java provides atomic classes for common operations without explicit synchronization.

```java
import java.util.concurrent.atomic.*;

public class AtomicExample {
    // Atomic counter - thread-safe without synchronized
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicLong longCount = new AtomicLong(0);
    private AtomicBoolean flag = new AtomicBoolean(false);
    private AtomicReference<String> name = new AtomicReference<>("initial");
    
    public void increment() {
        count.incrementAndGet();  // Atomically increments and returns new value
    }
    
    public void decrement() {
        count.decrementAndGet();
    }
    
    public int get() {
        return count.get();
    }
    
    public void addFive() {
        count.addAndGet(5);  // Atomically adds 5
    }
    
    public void updateIfExpected() {
        // Compare-and-set: if current value is 10, set to 20
        boolean success = count.compareAndSet(10, 20);
    }
}
```

**Atomic Classes Available:**

| Class | Purpose |
|-------|---------|
| `AtomicInteger` | Thread-safe int |
| `AtomicLong` | Thread-safe long |
| `AtomicBoolean` | Thread-safe boolean |
| `AtomicReference<V>` | Thread-safe object reference |
| `AtomicIntegerArray` | Thread-safe int array |
| `AtomicLongArray` | Thread-safe long array |
| `LongAdder` | High-performance counter (Java 8+) |

### LongAdder: High-Performance Counter

`LongAdder` is optimized for high-contention scenarios where many threads frequently update a counter.

```java
import java.util.concurrent.atomic.LongAdder;

public class LongAdderExample {
    private LongAdder counter = new LongAdder();
    
    public void increment() {
        counter.increment();  // Very fast under contention
    }
    
    public void add(long value) {
        counter.add(value);
    }
    
    public long getSum() {
        return counter.sum();  // Returns current sum
    }
    
    public void reset() {
        counter.reset();  // Reset to zero
    }
    
    public long sumThenReset() {
        return counter.sumThenReset();  // Get sum and reset atomically
    }
}
```

**LongAdder vs AtomicLong:**

| Scenario | Use |
|----------|-----|
| Few threads, read-heavy | `AtomicLong` |
| Many threads, write-heavy | `LongAdder` |
| Need exact point-in-time value | `AtomicLong` |
| Need high-throughput counter | `LongAdder` |

**AtomicInteger Methods:**

| Method | Description |
|--------|-------------|
| `get()` | Get current value |
| `set(value)` | Set value |
| `getAndSet(value)` | Get old value and set new |
| `incrementAndGet()` | ++counter (returns new) |
| `getAndIncrement()` | counter++ (returns old) |
| `decrementAndGet()` | --counter (returns new) |
| `getAndDecrement()` | counter-- (returns old) |
| `addAndGet(delta)` | Add and return new |
| `getAndAdd(delta)` | Add and return old |
| `compareAndSet(expect, update)` | Set if current equals expected |
| `updateAndGet(function)` | Apply function atomically |

---

## volatile Keyword

The `volatile` keyword ensures visibility of changes across threads.

### The Visibility Problem

```java
public class VisibilityProblem {
    private boolean running = true;  // Not volatile
    
    public void stop() {
        running = false;
    }
    
    public void run() {
        while (running) {  // May never see the change!
            // Do work
        }
    }
}
```

**Why This Fails:**
Each thread may have its own cached copy of `running`. The change made by one thread may not be visible to another.

### The Solution: volatile

```java
public class VolatileFix {
    private volatile boolean running = true;  // Volatile!
    
    public void stop() {
        running = false;  // Write goes to main memory
    }
    
    public void run() {
        while (running) {  // Always reads from main memory
            // Do work
        }
    }
}
```

**What volatile Does:**
- Writes go directly to main memory (not cached)
- Reads always come from main memory
- Prevents instruction reordering around the volatile variable

**When to Use volatile:**
| Use Case | Use volatile? |
|----------|--------------|
| Simple flag (start/stop) | Yes |
| Status variable read by many, written by one | Yes |
| Counter that gets incremented | No (use Atomic) |
| Multiple related variables | No (use synchronized) |

**volatile vs synchronized:**

| volatile | synchronized |
|----------|--------------|
| Only visibility | Visibility + atomicity |
| No blocking | Blocking (acquires lock) |
| Single variable | Any code block |
| Faster | Slower |
| No race condition protection | Full race condition protection |

---

## Thread Communication: wait(), notify(), notifyAll()

Threads can communicate and coordinate using these methods.

### Producer-Consumer Example

```java
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int MAX_SIZE = 5;
    
    // Producer adds items
    public synchronized void produce(int item) throws InterruptedException {
        // Wait while queue is full
        while (queue.size() == MAX_SIZE) {
            System.out.println("Queue full, producer waiting...");
            wait();  // Release lock and wait
        }
        
        queue.add(item);
        System.out.println("Produced: " + item + " | Queue size: " + queue.size());
        
        // Notify consumers that item is available
        notifyAll();
    }
    
    // Consumer removes items
    public synchronized int consume() throws InterruptedException {
        // Wait while queue is empty
        while (queue.isEmpty()) {
            System.out.println("Queue empty, consumer waiting...");
            wait();  // Release lock and wait
        }
        
        int item = queue.poll();
        System.out.println("Consumed: " + item + " | Queue size: " + queue.size());
        
        // Notify producers that space is available
        notifyAll();
        
        return item;
    }
    
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    pc.produce(i);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    pc.consume();
                    Thread.sleep(500);
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

**Key Points:**
- `wait()` must be called inside synchronized block/method
- `wait()` releases the lock and waits for `notify()`
- `notify()` wakes up ONE waiting thread
- `notifyAll()` wakes up ALL waiting threads (usually safer)
- Always use `while` loop (not `if`) to check condition after waking

**Why while instead of if?**
```java
// WRONG: if
if (queue.isEmpty()) {
    wait();
}
// What if another thread consumed the item right after we woke up?
// We proceed with empty queue!

// CORRECT: while
while (queue.isEmpty()) {
    wait();
}
// Re-checks condition after waking, handles spurious wakeups
```

---

## Deadlock

Deadlock occurs when threads wait for each other forever.

### Deadlock Example

```java
public class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            System.out.println("Thread 1: Holding lock1...");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            
            System.out.println("Thread 1: Waiting for lock2...");
            synchronized (lock2) {
                System.out.println("Thread 1: Holding lock1 & lock2");
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {  // Opposite order!
            System.out.println("Thread 2: Holding lock2...");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            
            System.out.println("Thread 2: Waiting for lock1...");
            synchronized (lock1) {
                System.out.println("Thread 2: Holding lock2 & lock1");
            }
        }
    }
    
    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();
        
        Thread t1 = new Thread(example::method1);
        Thread t2 = new Thread(example::method2);
        
        t1.start();
        t2.start();
        // DEADLOCK! Both threads wait forever
    }
}
```

**What Happens:**
```
Thread 1 acquires lock1
Thread 2 acquires lock2
Thread 1 waits for lock2 (held by Thread 2)
Thread 2 waits for lock1 (held by Thread 1)
= Both wait forever = DEADLOCK
```

### Preventing Deadlock

**1. Consistent Lock Ordering:**
```java
// ALWAYS acquire locks in the same order
public void method1() {
    synchronized (lock1) {
        synchronized (lock2) {
            // ...
        }
    }
}

public void method2() {
    synchronized (lock1) {  // Same order as method1!
        synchronized (lock2) {
            // ...
        }
    }
}
```

**2. Use tryLock() with Timeout (from java.util.concurrent):**
```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class TryLockExample {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    
    public void safeMethod() {
        try {
            if (lock1.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            // Do work
                        } finally {
                            lock2.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

**3. Avoid Nested Locks When Possible**

**4. Use Higher-Level Concurrency Utilities**
(See next chapter on Concurrency)

---

## Thread-Safe Collections

Java provides thread-safe collection alternatives.

### Synchronized Wrappers

```java
import java.util.*;

// Wrap regular collections
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// Usage - still need to synchronize iteration!
synchronized (syncList) {
    for (String item : syncList) {
        System.out.println(item);
    }
}
```

### Concurrent Collections (Better)

```java
import java.util.concurrent.*;

// Better alternatives - no external sync needed
ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

// ConcurrentHashMap operations
concurrentMap.put("key", 1);
concurrentMap.putIfAbsent("key", 2);  // Only if key doesn't exist
concurrentMap.computeIfAbsent("key", k -> computeValue(k));
concurrentMap.merge("key", 1, Integer::sum);  // Atomic merge
```

| Collection | Use Case |
|------------|----------|
| `ConcurrentHashMap` | Thread-safe map with high concurrency |
| `CopyOnWriteArrayList` | Read-heavy, write-rare lists |
| `ConcurrentLinkedQueue` | Non-blocking queue |
| `LinkedBlockingQueue` | Blocking producer-consumer queue |
| `ConcurrentSkipListMap` | Thread-safe sorted map |

---

## ThreadLocal: Thread-Specific Data

`ThreadLocal` provides each thread with its own isolated copy of a variable.

### Why Use ThreadLocal?

When multiple threads need their own independent copy of data (not shared):
- User session information per request
- Database connection per thread
- Formatting objects (SimpleDateFormat is not thread-safe)

### Basic Usage

```java
public class ThreadLocalExample {
    // Each thread gets its own counter
    private static ThreadLocal<Integer> threadLocalCounter = ThreadLocal.withInitial(() -> 0);
    
    public static void main(String[] args) {
        Runnable task = () -> {
            // Each thread has its own value
            for (int i = 0; i < 3; i++) {
                int value = threadLocalCounter.get();
                threadLocalCounter.set(value + 1);
                System.out.println(Thread.currentThread().getName() + 
                                  " counter: " + threadLocalCounter.get());
            }
        };
        
        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        
        t1.start();
        t2.start();
    }
}
```

**Output:**
```
Thread-A counter: 1
Thread-A counter: 2
Thread-A counter: 3
Thread-B counter: 1
Thread-B counter: 2
Thread-B counter: 3
```

Notice each thread counts independently from 1 to 3.

### Common Use Cases

```java
// Date formatter per thread (SimpleDateFormat is not thread-safe!)
private static ThreadLocal<SimpleDateFormat> dateFormatter = 
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

// User context per request
private static ThreadLocal<User> currentUser = new ThreadLocal<>();

public void handleRequest(User user) {
    currentUser.set(user);
    try {
        // Any method in this thread can access the user
        processRequest();
    } finally {
        currentUser.remove();  // Always clean up!
    }
}

public void processRequest() {
    User user = currentUser.get();  // Get current user without passing as parameter
    // ...
}
```

### InheritableThreadLocal

Child threads can inherit values from parent thread:

```java
private static InheritableThreadLocal<String> inheritableValue = 
    new InheritableThreadLocal<>();

public static void main(String[] args) {
    inheritableValue.set("From Parent");
    
    new Thread(() -> {
        System.out.println("Child sees: " + inheritableValue.get());
        // Prints: "Child sees: From Parent"
    }).start();
}
```

### Important: Always Clean Up!

```java
// Memory leak if not cleaned up!
try {
    threadLocal.set(value);
    // Do work
} finally {
    threadLocal.remove();  // CRITICAL in thread pools!
}
```

**Why Clean Up Matters:**
In thread pools, threads are reused. If you don't remove ThreadLocal values, the next task using that thread sees stale data.

---

## Uncaught Exception Handler

When a thread throws an uncaught exception, you can handle it gracefully.

### Setting Exception Handler

```java
public class ExceptionHandlerExample {
    public static void main(String[] args) {
        // Handler for a specific thread
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Something went wrong!");
        });
        
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Thread " + t.getName() + " threw: " + e.getMessage());
            // Log error, send alert, cleanup, etc.
        });
        
        thread.start();
    }
}
```

### Default Handler for All Threads

```java
public class DefaultExceptionHandler {
    public static void main(String[] args) {
        // Set default handler for ALL threads
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Unhandled exception in " + t.getName());
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            // Could log to file, send to monitoring system, etc.
        });
        
        // Now any unhandled exception will be caught
        new Thread(() -> {
            throw new RuntimeException("Thread 1 error");
        }).start();
        
        new Thread(() -> {
            throw new RuntimeException("Thread 2 error");
        }).start();
    }
}
```

**Handler Priority:**
1. Thread-specific handler (set with `setUncaughtExceptionHandler`)
2. ThreadGroup's `uncaughtException` method
3. Default handler (set with `setDefaultUncaughtExceptionHandler`)
4. If none set, JVM prints stack trace to stderr

---

## Thread Groups

ThreadGroup is a way to organize threads into groups.

```java
public class ThreadGroupExample {
    public static void main(String[] args) {
        // Create a group
        ThreadGroup group = new ThreadGroup("WorkerGroup");
        
        // Create threads in the group
        Thread t1 = new Thread(group, () -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }, "Worker-1");
        
        Thread t2 = new Thread(group, () -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }, "Worker-2");
        
        t1.start();
        t2.start();
        
        // Group operations
        System.out.println("Active threads: " + group.activeCount());
        System.out.println("Group name: " + group.getName());
        
        // List all threads
        Thread[] threads = new Thread[group.activeCount()];
        group.enumerate(threads);
        for (Thread t : threads) {
            System.out.println("Thread: " + t.getName());
        }
        
        // Interrupt all threads in group
        group.interrupt();
    }
}
```

**Note:** ThreadGroup is considered somewhat outdated. For most use cases, prefer `ExecutorService` for managing groups of threads.

---

## Practical Examples

### Example 1: Parallel File Processing

```java
import java.util.*;

public class ParallelFileProcessor {
    
    public static void main(String[] args) throws InterruptedException {
        List<String> files = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
        List<Thread> threads = new ArrayList<>();
        
        for (String fileName : files) {
            Thread thread = new Thread(() -> processFile(fileName));
            thread.setName("Processor-" + fileName);
            threads.add(thread);
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("All files processed!");
    }
    
    private static void processFile(String fileName) {
        System.out.println(Thread.currentThread().getName() + 
                          " processing " + fileName);
        try {
            Thread.sleep(1000);  // Simulate processing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + 
                          " finished " + fileName);
    }
}
```

### Example 2: Thread-Safe Bank Account

```java
import java.util.concurrent.atomic.AtomicInteger;

public class BankAccount {
    private final String accountId;
    private final AtomicInteger balance;
    
    public BankAccount(String accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = new AtomicInteger(initialBalance);
    }
    
    public void deposit(int amount) {
        int newBalance = balance.addAndGet(amount);
        System.out.println("Deposited " + amount + ", new balance: " + newBalance);
    }
    
    public boolean withdraw(int amount) {
        while (true) {
            int current = balance.get();
            if (current < amount) {
                System.out.println("Insufficient funds");
                return false;
            }
            // Atomically set new balance if current hasn't changed
            if (balance.compareAndSet(current, current - amount)) {
                System.out.println("Withdrew " + amount + ", new balance: " + 
                                  (current - amount));
                return true;
            }
            // If compareAndSet failed, another thread modified, try again
        }
    }
    
    public int getBalance() {
        return balance.get();
    }
    
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount("ACC-001", 1000);
        
        Thread depositor = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(100);
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
        });
        
        Thread withdrawer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.withdraw(150);
                try { Thread.sleep(70); } catch (InterruptedException e) {}
            }
        });
        
        depositor.start();
        withdrawer.start();
        
        depositor.join();
        withdrawer.join();
        
        System.out.println("Final balance: " + account.getBalance());
    }
}
```

### Example 3: Download Manager with Progress

```java
public class DownloadManager {
    private volatile int progress = 0;
    private volatile boolean cancelled = false;
    
    public void download(String url) {
        Thread downloadThread = new Thread(() -> {
            System.out.println("Starting download: " + url);
            
            for (int i = 0; i <= 100; i += 10) {
                if (cancelled) {
                    System.out.println("Download cancelled!");
                    return;
                }
                
                progress = i;
                System.out.println("Progress: " + progress + "%");
                
                try {
                    Thread.sleep(500);  // Simulate downloading
                } catch (InterruptedException e) {
                    System.out.println("Download interrupted!");
                    return;
                }
            }
            
            System.out.println("Download complete!");
        });
        
        downloadThread.start();
    }
    
    public int getProgress() {
        return progress;
    }
    
    public void cancel() {
        cancelled = true;
    }
    
    public static void main(String[] args) throws InterruptedException {
        DownloadManager manager = new DownloadManager();
        manager.download("http://example.com/file.zip");
        
        // Cancel after 2 seconds
        Thread.sleep(2000);
        manager.cancel();
    }
}
```

---

## Common Mistakes and How to Avoid Them

| Mistake | Problem | Solution |
|---------|---------|----------|
| Calling `run()` instead of `start()` | No new thread created | Always use `start()` |
| Not handling `InterruptedException` | Swallowing interrupts | Restore interrupt flag or handle properly |
| Shared mutable state without sync | Race conditions | Use synchronized, Atomic, or volatile |
| Using `notify()` instead of `notifyAll()` | Some threads may never wake | Prefer `notifyAll()` unless you're certain |
| Deadlock | Threads wait forever | Consistent lock ordering, timeouts |
| Busy waiting in loop | Wastes CPU | Use `wait()`/`notify()` or sleep |
| Not joining threads | Main program exits early | Use `join()` to wait |
| Thread-safe class with non-thread-safe operations | Data corruption | Review entire class for thread safety |

### Handling InterruptedException Correctly

```java
// WRONG: Swallowing the exception
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Doing nothing loses the interrupt!
}

// CORRECT: Restore interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();  // Restore interrupt flag
    return;  // Exit method
}

// CORRECT: Propagate the exception
public void myMethod() throws InterruptedException {
    Thread.sleep(1000);  // Let caller handle it
}
```

---

## Best Practices Summary

1. **Prefer Runnable over Thread**
   - Better separation of concerns
   - Works with ExecutorService

2. **Use Higher-Level Concurrency Utilities**
   - ExecutorService instead of raw threads
   - ConcurrentHashMap instead of synchronized HashMap
   - See next chapter

3. **Minimize Synchronized Blocks**
   - Only synchronize what's necessary
   - Smaller critical sections = better performance

4. **Use Immutable Objects**
   - Immutable objects are inherently thread-safe
   - No synchronization needed

5. **Prefer Atomic Variables for Simple Counters**
   - AtomicInteger, AtomicLong, etc.
   - Better performance than synchronized

6. **Document Thread Safety**
   - Clearly indicate if a class is thread-safe
   - Document synchronization requirements

7. **Avoid Nested Locks**
   - Reduces deadlock risk
   - Simplifies reasoning about code

8. **Use Thread-Safe Collections**
   - ConcurrentHashMap, CopyOnWriteArrayList
   - Better performance than synchronized wrappers

---

## Cheat Sheet

### Creating Threads

```java
// Lambda (simplest)
Thread t = new Thread(() -> doWork());
t.start();

// Runnable
Thread t = new Thread(new MyRunnable());
t.start();
```

### Basic Operations

```java
Thread.sleep(1000);              // Pause current thread
thread.join();                   // Wait for thread to finish
thread.join(5000);               // Wait with timeout
Thread.currentThread();          // Get current thread
thread.isAlive();                // Check if running
thread.interrupt();              // Request stop
thread.isInterrupted();          // Check interrupt flag
Thread.interrupted();            // Check and clear flag
```

### Synchronization

```java
synchronized void method() { }        // Method level
synchronized (lockObject) { }         // Block level
volatile boolean flag;                // Visibility only
AtomicInteger counter;                // Atomic operations
```

### Thread Communication

```java
synchronized (lock) {
    while (!condition) { wait(); }    // Wait for condition
}
synchronized (lock) {
    condition = true;
    notifyAll();                      // Wake waiting threads
}
```

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Stream API](./26-stream-api.md) | [Guide](../guide.md) | [Concurrency](./28-concurrency.md) |
