# Collections: Queues

[Back to Guide](../guide.md) | [Previous: Collections - Maps](19-collections-maps.md) | [Next: Generics](21-generics.md)

---

## What Is a Queue?

A Queue is a collection designed for holding elements prior to processing. Queues typically order elements in FIFO (First-In-First-Out) manner, meaning the first element added is the first one removed, like a line of people waiting at a checkout counter.

### Key Characteristics of Queues

| Characteristic | Description |
|----------------|-------------|
| FIFO ordering | First element in is first element out (for most queues) |
| Head and tail | Elements added at tail, removed from head |
| No random access | Cannot access elements by index |
| Capacity | May be bounded (fixed size) or unbounded |

### Queue vs List vs Set

| Feature | Queue | List | Set |
|---------|-------|------|-----|
| Order | FIFO (usually) | By index | Depends on implementation |
| Access | Head only | Any index | No direct access |
| Duplicates | Allowed | Allowed | Not allowed |
| Main use | Processing in order | Random access | Unique elements |

---

## The Queue Interface

The Queue interface defines two sets of methods: one that throws exceptions and one that returns special values.

| Operation | Throws Exception | Returns Special Value |
|-----------|------------------|----------------------|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Examine | `element()` | `peek()` |

```java
import java.util.Queue;
import java.util.LinkedList;

public class QueueBasics {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        
        // Add elements
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println(queue);  // [First, Second, Third]
        
        // Peek at head (doesn't remove)
        String head = queue.peek();
        System.out.println("Head: " + head);  // First
        System.out.println(queue);  // [First, Second, Third]
        
        // Remove head
        String removed = queue.poll();
        System.out.println("Removed: " + removed);  // First
        System.out.println(queue);  // [Second, Third]
    }
}
```

---

## Queue Implementations

| Implementation | Order | Bounded | Thread-Safe | Use Case |
|----------------|-------|---------|-------------|----------|
| LinkedList | FIFO | No | No | General purpose queue |
| ArrayDeque | FIFO | No | No | Fast queue/stack |
| PriorityQueue | Priority | No | No | Process by priority |
| ArrayBlockingQueue | FIFO | Yes | Yes | Bounded blocking queue |
| LinkedBlockingQueue | FIFO | Optional | Yes | Producer-consumer |
| ConcurrentLinkedQueue | FIFO | No | Yes | Non-blocking concurrent |

---

## LinkedList as a Queue

LinkedList implements the Queue interface and is a common choice for a basic queue.

```java
import java.util.Queue;
import java.util.LinkedList;

public class LinkedListQueue {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        
        // Add to queue (at tail)
        queue.offer("Task 1");
        queue.offer("Task 2");
        queue.offer("Task 3");
        
        // Process queue (from head)
        while (!queue.isEmpty()) {
            String task = queue.poll();
            System.out.println("Processing: " + task);
        }
        // Output:
        // Processing: Task 1
        // Processing: Task 2
        // Processing: Task 3
    }
}
```

### Exception-Throwing vs Special-Value Methods

```java
import java.util.Queue;
import java.util.LinkedList;

public class QueueMethods {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        
        // Empty queue behavior
        
        // poll() returns null on empty queue
        String polled = queue.poll();
        System.out.println("poll() on empty: " + polled);  // null
        
        // peek() returns null on empty queue
        String peeked = queue.peek();
        System.out.println("peek() on empty: " + peeked);  // null
        
        // remove() throws exception on empty queue
        try {
            queue.remove();  // NoSuchElementException
        } catch (java.util.NoSuchElementException e) {
            System.out.println("remove() on empty: exception");
        }
        
        // element() throws exception on empty queue
        try {
            queue.element();  // NoSuchElementException
        } catch (java.util.NoSuchElementException e) {
            System.out.println("element() on empty: exception");
        }
        
        // Recommendation: Use offer/poll/peek for most cases
    }
}
```

---

## ArrayDeque

ArrayDeque is a resizable array implementation that is faster than LinkedList for most queue operations.

```java
import java.util.Queue;
import java.util.ArrayDeque;

public class ArrayDequeQueue {
    public static void main(String[] args) {
        Queue<String> queue = new ArrayDeque<>();
        
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        
        System.out.println("Queue: " + queue);  // [A, B, C]
        System.out.println("Head: " + queue.peek());  // A
        
        // Process all
        while (!queue.isEmpty()) {
            System.out.println("Removed: " + queue.poll());
        }
    }
}
```

### ArrayDeque vs LinkedList for Queues

| Aspect | ArrayDeque | LinkedList |
|--------|------------|------------|
| Speed | Faster | Slower |
| Memory | Less (no node objects) | More (nodes with pointers) |
| Null elements | Not allowed | Allowed |
| Cache friendly | Yes | No |

**Recommendation:** Use ArrayDeque for queues unless you need null elements.

---

## The Deque Interface

A Deque (Double-Ended Queue) allows adding and removing elements from both ends. It can be used as both a queue (FIFO) and a stack (LIFO).

### Deque Methods Overview

| Operation | First Element (Head) | Last Element (Tail) |
|-----------|---------------------|---------------------|
| Insert | `addFirst(e)` / `offerFirst(e)` | `addLast(e)` / `offerLast(e)` |
| Remove | `removeFirst()` / `pollFirst()` | `removeLast()` / `pollLast()` |
| Examine | `getFirst()` / `peekFirst()` | `getLast()` / `peekLast()` |

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class DequeExample {
    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();
        
        // Add at both ends
        deque.addFirst("B");   // [B]
        deque.addFirst("A");   // [A, B]
        deque.addLast("C");    // [A, B, C]
        deque.addLast("D");    // [A, B, C, D]
        
        System.out.println("Deque: " + deque);  // [A, B, C, D]
        
        // Peek at both ends
        System.out.println("First: " + deque.peekFirst());  // A
        System.out.println("Last: " + deque.peekLast());    // D
        
        // Remove from both ends
        System.out.println("Remove first: " + deque.pollFirst());  // A
        System.out.println("Remove last: " + deque.pollLast());    // D
        System.out.println("Deque: " + deque);  // [B, C]
    }
}
```

### Using Deque as a Queue (FIFO)

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class DequeAsQueue {
    public static void main(String[] args) {
        Deque<String> queue = new ArrayDeque<>();
        
        // Queue: add at tail, remove from head
        queue.addLast("First");   // or offer()
        queue.addLast("Second");
        queue.addLast("Third");
        
        while (!queue.isEmpty()) {
            System.out.println(queue.pollFirst());  // or poll()
        }
        // Output: First, Second, Third (FIFO)
    }
}
```

### Using Deque as a Stack (LIFO)

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class DequeAsStack {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>();
        
        // Stack: add and remove from same end (head)
        stack.push("First");   // addFirst
        stack.push("Second");
        stack.push("Third");
        
        System.out.println("Stack: " + stack);  // [Third, Second, First]
        
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());  // removeFirst
        }
        // Output: Third, Second, First (LIFO)
    }
}
```

### Deque Methods for Stack Operations

| Stack Method | Deque Equivalent |
|--------------|------------------|
| `push(e)` | `addFirst(e)` |
| `pop()` | `removeFirst()` |
| `peek()` | `peekFirst()` |

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class StackOperations {
    public static void main(String[] args) {
        Deque<Integer> stack = new ArrayDeque<>();
        
        // Push elements
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack: " + stack);  // [3, 2, 1]
        
        // Peek at top
        System.out.println("Top: " + stack.peek());  // 3
        
        // Pop elements
        while (!stack.isEmpty()) {
            System.out.println("Pop: " + stack.pop());
        }
        // Pop: 3, Pop: 2, Pop: 1
    }
}
```

---

## PriorityQueue

PriorityQueue orders elements by their natural ordering (Comparable) or by a Comparator. The head is always the smallest (or highest priority) element.

### Basic PriorityQueue (Natural Ordering)

```java
import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueBasics {
    public static void main(String[] args) {
        Queue<Integer> pq = new PriorityQueue<>();
        
        // Add in random order
        pq.offer(30);
        pq.offer(10);
        pq.offer(50);
        pq.offer(20);
        pq.offer(40);
        
        System.out.println("PriorityQueue: " + pq);  // Internal order, not sorted!
        
        // Remove in priority order (smallest first)
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        // Output: 10, 20, 30, 40, 50 (sorted order)
    }
}
```

### PriorityQueue with Strings

```java
import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueStrings {
    public static void main(String[] args) {
        Queue<String> pq = new PriorityQueue<>();
        
        pq.offer("Charlie");
        pq.offer("Alice");
        pq.offer("Bob");
        pq.offer("David");
        
        // Remove in alphabetical order
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
        // Output: Alice, Bob, Charlie, David
    }
}
```

### PriorityQueue with Custom Comparator

```java
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

public class PriorityQueueComparator {
    public static void main(String[] args) {
        // Max heap (largest first)
        Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        
        maxHeap.offer(30);
        maxHeap.offer(10);
        maxHeap.offer(50);
        maxHeap.offer(20);
        
        System.out.println("Max heap poll order:");
        while (!maxHeap.isEmpty()) {
            System.out.println(maxHeap.poll());
        }
        // Output: 50, 30, 20, 10
        
        // Custom comparator (by string length)
        Queue<String> byLength = new PriorityQueue<>(Comparator.comparingInt(String::length));
        
        byLength.offer("Banana");
        byLength.offer("Fig");
        byLength.offer("Apple");
        byLength.offer("Kiwi");
        
        System.out.println("\nBy length poll order:");
        while (!byLength.isEmpty()) {
            System.out.println(byLength.poll());
        }
        // Output: Fig, Kiwi, Apple, Banana
    }
}
```

### PriorityQueue with Custom Objects

```java
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

class Task {
    private String name;
    private int priority;  // Lower number = higher priority
    
    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    
    public String getName() { return name; }
    public int getPriority() { return priority; }
    
    @Override
    public String toString() {
        return name + "(priority=" + priority + ")";
    }
}

public class PriorityQueueObjects {
    public static void main(String[] args) {
        // Priority queue ordered by task priority
        Queue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(Task::getPriority)
        );
        
        taskQueue.offer(new Task("Low priority task", 3));
        taskQueue.offer(new Task("High priority task", 1));
        taskQueue.offer(new Task("Medium priority task", 2));
        taskQueue.offer(new Task("Critical task", 0));
        
        System.out.println("Processing tasks by priority:");
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("Processing: " + task);
        }
        // Output:
        // Processing: Critical task(priority=0)
        // Processing: High priority task(priority=1)
        // Processing: Medium priority task(priority=2)
        // Processing: Low priority task(priority=3)
    }
}
```

### PriorityQueue with Multiple Criteria

```java
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

class Job {
    private String name;
    private int priority;
    private long timestamp;
    
    public Job(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.timestamp = System.nanoTime();
    }
    
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() { return name; }
}

public class MultiCriteriaPriorityQueue {
    public static void main(String[] args) {
        // First by priority (ascending), then by timestamp (oldest first)
        Queue<Job> jobQueue = new PriorityQueue<>(
            Comparator.comparingInt(Job::getPriority)
                      .thenComparingLong(Job::getTimestamp)
        );
        
        jobQueue.offer(new Job("Job A", 2));
        jobQueue.offer(new Job("Job B", 1));
        jobQueue.offer(new Job("Job C", 2));  // Same priority as A, but later
        jobQueue.offer(new Job("Job D", 1));  // Same priority as B, but later
        
        System.out.println("Processing order:");
        while (!jobQueue.isEmpty()) {
            System.out.println(jobQueue.poll());
        }
        // Output: Job B, Job D, Job A, Job C
        // (priority 1 before priority 2, within same priority by timestamp)
    }
}
```

### Important PriorityQueue Notes

```java
import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueNotes {
    public static void main(String[] args) {
        Queue<Integer> pq = new PriorityQueue<>();
        pq.offer(30);
        pq.offer(10);
        pq.offer(20);
        
        // NOTE 1: Iterator does NOT return elements in priority order!
        System.out.println("Iterator order (NOT priority):");
        for (Integer n : pq) {
            System.out.print(n + " ");  // Arbitrary internal order
        }
        System.out.println();
        
        // NOTE 2: toString() does NOT show priority order!
        System.out.println("toString: " + pq);  // Arbitrary order
        
        // NOTE 3: Only poll()/remove() guarantee priority order
        System.out.println("poll() order (IS priority):");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");  // 10 20 30
        }
        System.out.println();
        
        // NOTE 4: No null elements allowed
        try {
            pq.offer(null);  // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("null not allowed");
        }
    }
}
```

---

## Blocking Queues

Blocking queues support operations that wait for the queue to become non-empty when retrieving, or wait for space when inserting.

### BlockingQueue Methods

| Operation | Throws Exception | Special Value | Blocks | Times Out |
|-----------|------------------|---------------|--------|-----------|
| Insert | `add(e)` | `offer(e)` | `put(e)` | `offer(e, time, unit)` |
| Remove | `remove()` | `poll()` | `take()` | `poll(time, unit)` |
| Examine | `element()` | `peek()` | N/A | N/A |

### ArrayBlockingQueue

Bounded blocking queue backed by an array.

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueExample {
    public static void main(String[] args) throws InterruptedException {
        // Bounded queue with capacity 3
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        
        // Add elements
        queue.put("A");  // Blocks if full
        queue.put("B");
        queue.put("C");
        
        System.out.println("Queue full: " + queue);  // [A, B, C]
        
        // offer() returns false if full (non-blocking)
        boolean added = queue.offer("D");
        System.out.println("Offer D: " + added);  // false
        
        // Take elements
        String item = queue.take();  // Blocks if empty
        System.out.println("Took: " + item);  // A
        
        // Now we can add
        queue.put("D");
        System.out.println("Queue: " + queue);  // [B, C, D]
    }
}
```

### LinkedBlockingQueue

Optionally bounded blocking queue backed by linked nodes.

```java
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LinkedBlockingQueueExample {
    public static void main(String[] args) throws InterruptedException {
        // Unbounded queue (actually bounded by Integer.MAX_VALUE)
        BlockingQueue<String> unbounded = new LinkedBlockingQueue<>();
        
        // Bounded queue
        BlockingQueue<String> bounded = new LinkedBlockingQueue<>(100);
        
        // Add elements
        unbounded.put("A");
        unbounded.put("B");
        unbounded.put("C");
        
        // Take with timeout
        String item = unbounded.poll(1, java.util.concurrent.TimeUnit.SECONDS);
        System.out.println("Polled: " + item);  // A
    }
}
```

### Producer-Consumer Pattern

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(5);
        
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item-" + i;
                    queue.put(item);  // Blocks if queue is full
                    System.out.println("Produced: " + item);
                    Thread.sleep(100);
                }
                queue.put("DONE");  // Signal end
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String item = queue.take();  // Blocks if queue is empty
                    if ("DONE".equals(item)) break;
                    System.out.println("Consumed: " + item);
                    Thread.sleep(150);  // Consumer is slower
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        System.out.println("Done!");
    }
}
```

### PriorityBlockingQueue

Unbounded blocking priority queue.

```java
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Comparator;

public class PriorityBlockingQueueExample {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> pq = new PriorityBlockingQueue<>();
        
        // Add in random order
        pq.put(30);
        pq.put(10);
        pq.put(50);
        pq.put(20);
        
        // Take in priority order
        System.out.println(pq.take());  // 10
        System.out.println(pq.take());  // 20
        System.out.println(pq.take());  // 30
        System.out.println(pq.take());  // 50
        
        // With comparator (max heap)
        BlockingQueue<Integer> maxPQ = new PriorityBlockingQueue<>(
            11, Comparator.reverseOrder()
        );
        
        maxPQ.put(30);
        maxPQ.put(10);
        maxPQ.put(50);
        
        System.out.println(maxPQ.take());  // 50
        System.out.println(maxPQ.take());  // 30
    }
}
```

---

## Concurrent Queues

### ConcurrentLinkedQueue

Non-blocking thread-safe queue using lock-free algorithms.

```java
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueExample {
    public static void main(String[] args) {
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        
        // Thread-safe operations (non-blocking)
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        
        // Safe to iterate (weakly consistent)
        for (String item : queue) {
            System.out.println(item);
        }
        
        // Safe to poll while iterating
        String item;
        while ((item = queue.poll()) != null) {
            System.out.println("Processed: " + item);
        }
    }
}
```

### ConcurrentLinkedDeque

Non-blocking thread-safe deque.

```java
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeExample {
    public static void main(String[] args) {
        Deque<String> deque = new ConcurrentLinkedDeque<>();
        
        // Thread-safe operations at both ends
        deque.offerFirst("A");
        deque.offerLast("B");
        deque.offerFirst("C");
        
        System.out.println("Deque: " + deque);  // [C, A, B]
        
        // Poll from both ends
        System.out.println("First: " + deque.pollFirst());  // C
        System.out.println("Last: " + deque.pollLast());    // B
    }
}
```

---

## Common Use Cases

### Task Queue

```java
import java.util.Queue;
import java.util.LinkedList;

public class TaskQueue {
    private Queue<Runnable> tasks = new LinkedList<>();
    
    public void addTask(Runnable task) {
        tasks.offer(task);
    }
    
    public void processTasks() {
        Runnable task;
        while ((task = tasks.poll()) != null) {
            task.run();
        }
    }
    
    public static void main(String[] args) {
        TaskQueue queue = new TaskQueue();
        
        queue.addTask(() -> System.out.println("Task 1"));
        queue.addTask(() -> System.out.println("Task 2"));
        queue.addTask(() -> System.out.println("Task 3"));
        
        queue.processTasks();
    }
}
```

### BFS (Breadth-First Search)

```java
import java.util.*;

public class BreadthFirstSearch {
    public static void main(String[] args) {
        // Graph represented as adjacency list
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D", "E"));
        graph.put("C", List.of("F"));
        graph.put("D", List.of());
        graph.put("E", List.of("F"));
        graph.put("F", List.of());
        
        // BFS traversal
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer("A");
        visited.add("A");
        
        System.out.println("BFS traversal:");
        while (!queue.isEmpty()) {
            String node = queue.poll();
            System.out.print(node + " ");
            
            for (String neighbor : graph.getOrDefault(node, List.of())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        // Output: A B C D E F
    }
}
```

### Recent Items (Using Deque)

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class RecentItems<T> {
    private final Deque<T> items = new ArrayDeque<>();
    private final int maxSize;
    
    public RecentItems(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public void add(T item) {
        // Remove if already exists
        items.remove(item);
        
        // Add to front
        items.addFirst(item);
        
        // Remove oldest if over capacity
        if (items.size() > maxSize) {
            items.removeLast();
        }
    }
    
    public Deque<T> getItems() {
        return new ArrayDeque<>(items);
    }
    
    public static void main(String[] args) {
        RecentItems<String> recent = new RecentItems<>(3);
        
        recent.add("page1");
        recent.add("page2");
        recent.add("page3");
        System.out.println(recent.getItems());  // [page3, page2, page1]
        
        recent.add("page4");  // Removes page1
        System.out.println(recent.getItems());  // [page4, page3, page2]
        
        recent.add("page2");  // Moves page2 to front
        System.out.println(recent.getItems());  // [page2, page4, page3]
    }
}
```

### Expression Evaluation (Using Stack/Deque)

```java
import java.util.Deque;
import java.util.ArrayDeque;

public class ExpressionEvaluator {
    public static int evaluatePostfix(String expression) {
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (String token : expression.split(" ")) {
            switch (token) {
                case "+" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                }
                case "-" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                }
                case "*" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a * b);
                }
                case "/" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a / b);
                }
                default -> stack.push(Integer.parseInt(token));
            }
        }
        
        return stack.pop();
    }
    
    public static void main(String[] args) {
        // Postfix: 3 4 + 5 * = (3+4)*5 = 35
        System.out.println(evaluatePostfix("3 4 + 5 *"));  // 35
        
        // Postfix: 10 2 3 * - = 10-(2*3) = 4
        System.out.println(evaluatePostfix("10 2 3 * -"));  // 4
    }
}
```

### Top K Elements (Using PriorityQueue)

```java
import java.util.PriorityQueue;
import java.util.List;
import java.util.Comparator;

public class TopKElements {
    public static List<Integer> findTopK(int[] nums, int k) {
        // Min heap of size k
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();  // Remove smallest
            }
        }
        
        // Convert to list (will be in ascending order)
        return minHeap.stream().sorted(Comparator.reverseOrder()).toList();
    }
    
    public static void main(String[] args) {
        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        
        List<Integer> top3 = findTopK(nums, 3);
        System.out.println("Top 3: " + top3);  // [9, 6, 5]
    }
}
```

### Merge K Sorted Lists (Using PriorityQueue)

```java
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class MergeKSortedLists {
    public static List<Integer> merge(List<List<Integer>> lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> lists.get(a[0]).get(a[1]))
        );
        
        // Add first element of each list: [listIndex, elementIndex]
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                pq.offer(new int[]{i, 0});
            }
        }
        
        List<Integer> result = new ArrayList<>();
        
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int listIdx = curr[0];
            int elemIdx = curr[1];
            
            result.add(lists.get(listIdx).get(elemIdx));
            
            // Add next element from same list
            if (elemIdx + 1 < lists.get(listIdx).size()) {
                pq.offer(new int[]{listIdx, elemIdx + 1});
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        List<List<Integer>> lists = List.of(
            List.of(1, 4, 7),
            List.of(2, 5, 8),
            List.of(3, 6, 9)
        );
        
        System.out.println(merge(lists));  // [1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
}
```

---

## Iterating Over Queues

### Enhanced For Loop (Does Not Remove)

```java
import java.util.Queue;
import java.util.LinkedList;

public class QueueIteration {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        
        // Does NOT remove elements
        for (String item : queue) {
            System.out.println(item);
        }
        System.out.println("After for-each: " + queue);  // [A, B, C]
    }
}
```

### While Loop with poll() (Removes)

```java
import java.util.Queue;
import java.util.LinkedList;

public class QueueDrain {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        
        // Removes elements
        String item;
        while ((item = queue.poll()) != null) {
            System.out.println(item);
        }
        System.out.println("After poll loop: " + queue);  // []
    }
}
```

---

## Common Mistakes and How to Avoid Them

### Mistake 1: Using Stack Class Instead of Deque

```java
// AVOID: Stack is a legacy class
java.util.Stack<String> stack = new java.util.Stack<>();

// CORRECT: Use Deque as a stack
Deque<String> stack = new ArrayDeque<>();
stack.push("A");
stack.pop();
```

### Mistake 2: Iterating PriorityQueue for Ordered Output

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.offer(30);
pq.offer(10);
pq.offer(20);

// WRONG: Does NOT give priority order!
for (Integer i : pq) {
    System.out.println(i);  // Order is undefined
}

// CORRECT: Use poll() for priority order
while (!pq.isEmpty()) {
    System.out.println(pq.poll());  // 10, 20, 30
}
```

### Mistake 3: Null in ArrayDeque or PriorityQueue

```java
// WRONG: NullPointerException
Deque<String> deque = new ArrayDeque<>();
// deque.offer(null);  // Exception!

PriorityQueue<Integer> pq = new PriorityQueue<>();
// pq.offer(null);  // Exception!

// CORRECT: Use LinkedList if null needed
Queue<String> queue = new LinkedList<>();
queue.offer(null);  // OK (but usually not recommended)
```

### Mistake 4: Forgetting Comparator for Custom Objects

```java
class Task {
    int priority;
}

// WRONG: ClassCastException (Task doesn't implement Comparable)
// PriorityQueue<Task> pq = new PriorityQueue<>();
// pq.offer(new Task());

// CORRECT: Provide Comparator or implement Comparable
PriorityQueue<Task> pq = new PriorityQueue<>(
    Comparator.comparingInt(t -> t.priority)
);
```

### Mistake 5: Blocking Forever on Empty Queue

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// WRONG: Blocks forever if no producer
// String item = queue.take();

// CORRECT: Use poll with timeout
String item = queue.poll(5, TimeUnit.SECONDS);
if (item == null) {
    System.out.println("Timeout - no item received");
}
```

---

## Best Practices

### 1. Choose the Right Queue Type

```java
// General FIFO queue
Queue<String> queue = new ArrayDeque<>();

// Stack (LIFO)
Deque<String> stack = new ArrayDeque<>();

// Priority-based processing
Queue<Task> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority));

// Thread-safe queue
Queue<String> concurrent = new ConcurrentLinkedQueue<>();

// Blocking queue for producer-consumer
BlockingQueue<String> blocking = new LinkedBlockingQueue<>();
```

### 2. Prefer ArrayDeque Over LinkedList

```java
// BETTER: Faster and less memory
Queue<String> queue = new ArrayDeque<>();
Deque<String> deque = new ArrayDeque<>();

// SLOWER: More memory overhead
Queue<String> queue = new LinkedList<>();
```

### 3. Use offer/poll/peek Over add/remove/element

```java
Queue<String> queue = new ArrayDeque<>();

// PREFERRED: Returns false/null instead of exception
queue.offer("A");        // true
String item = queue.poll();  // null if empty
String head = queue.peek();  // null if empty

// AVOID: Throws exception if operation fails
queue.add("A");          // throws if capacity exceeded
queue.remove();          // throws if empty
queue.element();         // throws if empty
```

### 4. Use Deque for Stack Operations

```java
// CORRECT: Use Deque as stack
Deque<String> stack = new ArrayDeque<>();
stack.push("A");
stack.pop();
stack.peek();

// AVOID: Legacy Stack class
// Stack<String> stack = new Stack<>();
```

### 5. Consider Capacity for Blocking Queues

```java
// Unbounded - can grow to exhaust memory
BlockingQueue<String> unbounded = new LinkedBlockingQueue<>();

// Bounded - provides backpressure
BlockingQueue<String> bounded = new LinkedBlockingQueue<>(1000);
BlockingQueue<String> array = new ArrayBlockingQueue<>(1000);
```

---

## Cheat Sheet

| Operation | Queue | Deque (as Queue) | Deque (as Stack) |
|-----------|-------|------------------|------------------|
| Add | `offer(e)` | `offerLast(e)` | `push(e)` |
| Remove | `poll()` | `pollFirst()` | `pop()` |
| Examine | `peek()` | `peekFirst()` | `peek()` |

### Queue Types Quick Reference

| Type | Use Case |
|------|----------|
| `ArrayDeque` | Fast general-purpose queue/stack |
| `LinkedList` | Queue that needs null elements |
| `PriorityQueue` | Process by priority |
| `ConcurrentLinkedQueue` | Thread-safe non-blocking |
| `ArrayBlockingQueue` | Bounded blocking queue |
| `LinkedBlockingQueue` | Optionally bounded blocking |
| `PriorityBlockingQueue` | Thread-safe priority queue |

### Common Operations

```java
// Create queue
Queue<String> queue = new ArrayDeque<>();

// Add
queue.offer("A");           // Returns false if full

// Remove
String item = queue.poll(); // Returns null if empty

// Peek
String head = queue.peek(); // Returns null if empty

// Size
int size = queue.size();
boolean empty = queue.isEmpty();

// Clear
queue.clear();

// Drain (process all)
while ((item = queue.poll()) != null) { }
```

---

## Summary

Queues and Deques are essential collections for ordered processing. Key points:

1. **Queue** is FIFO (First-In-First-Out)
2. **Deque** supports operations at both ends (can be queue or stack)
3. **ArrayDeque** is faster than LinkedList for most cases
4. **PriorityQueue** orders by priority, not insertion order
5. Use **offer/poll/peek** instead of add/remove/element
6. **BlockingQueue** supports waiting for elements
7. Use **Deque** instead of legacy **Stack** class
8. PriorityQueue iteration does NOT guarantee order - only poll() does

---

[Back to Guide](../guide.md) | [Previous: Collections - Maps](19-collections-maps.md) | [Next: Generics](21-generics.md)
