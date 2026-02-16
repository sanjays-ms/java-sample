# Collections: Queues Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/20-collections-queues.md)

---

## Quick Reference

| Type | Order | Bounded | Thread-Safe | Use Case |
|------|-------|---------|-------------|----------|
| ArrayDeque | FIFO | No | No | Fast queue/stack |
| LinkedList | FIFO | No | No | Queue with nulls |
| PriorityQueue | Priority | No | No | Process by priority |
| ConcurrentLinkedQueue | FIFO | No | Yes | Non-blocking concurrent |
| ArrayBlockingQueue | FIFO | Yes | Yes | Bounded blocking |
| LinkedBlockingQueue | FIFO | Optional | Yes | Producer-consumer |
| PriorityBlockingQueue | Priority | No | Yes | Thread-safe priority |

---

## Queue Methods

| Operation | Throws Exception | Returns null/false |
|-----------|------------------|-------------------|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Examine | `element()` | `peek()` |

**Prefer**: `offer()`, `poll()`, `peek()` - they don't throw exceptions.

---

## Creating Queues

```java
// General purpose queue (preferred)
Queue<String> queue = new ArrayDeque<>();

// Queue with nulls allowed
Queue<String> queue = new LinkedList<>();

// Priority queue (min heap by default)
Queue<Integer> pq = new PriorityQueue<>();

// Priority queue (max heap)
Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

// Priority queue with custom comparator
Queue<Task> tasks = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority));

// Thread-safe non-blocking
Queue<String> concurrent = new ConcurrentLinkedQueue<>();

// Blocking queue (bounded)
BlockingQueue<String> blocking = new ArrayBlockingQueue<>(100);

// Blocking queue (unbounded)
BlockingQueue<String> blocking = new LinkedBlockingQueue<>();
```

---

## Basic Queue Operations

```java
Queue<String> queue = new ArrayDeque<>();

// Add (at tail)
queue.offer("A");               // Returns false if full
queue.add("B");                 // Throws if full

// Remove (from head)
String item = queue.poll();     // Returns null if empty
String item = queue.remove();   // Throws if empty

// Peek (head without removing)
String head = queue.peek();     // Returns null if empty
String head = queue.element();  // Throws if empty

// Size
int size = queue.size();
boolean empty = queue.isEmpty();

// Clear
queue.clear();
```

---

## Deque (Double-Ended Queue)

```java
Deque<String> deque = new ArrayDeque<>();

// Add at both ends
deque.offerFirst("A");          // Add at head
deque.offerLast("B");           // Add at tail

// Remove from both ends
String first = deque.pollFirst();
String last = deque.pollLast();

// Peek at both ends
String first = deque.peekFirst();
String last = deque.peekLast();
```

---

## Deque as Stack (LIFO)

```java
Deque<String> stack = new ArrayDeque<>();

// Push (add at top)
stack.push("A");
stack.push("B");
stack.push("C");

// Pop (remove from top)
String top = stack.pop();       // C
String top = stack.pop();       // B

// Peek (see top)
String top = stack.peek();      // A
```

| Stack Operation | Deque Method |
|-----------------|--------------|
| `push(e)` | `addFirst(e)` |
| `pop()` | `removeFirst()` |
| `peek()` | `peekFirst()` |

---

## PriorityQueue

```java
// Min heap (smallest first)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(30);
minHeap.offer(10);
minHeap.offer(20);
minHeap.poll();                 // 10 (smallest)
minHeap.poll();                 // 20
minHeap.poll();                 // 30

// Max heap (largest first)
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

// Custom priority
PriorityQueue<String> byLength = new PriorityQueue<>(
    Comparator.comparingInt(String::length)
);

// Multiple criteria
PriorityQueue<Task> tasks = new PriorityQueue<>(
    Comparator.comparingInt(Task::getPriority)
              .thenComparingLong(Task::getTimestamp)
);
```

**Important**: Iterator does NOT return elements in priority order. Only `poll()` does.

---

## Blocking Queue

| Operation | Throws | Special Value | Blocks | Times Out |
|-----------|--------|---------------|--------|-----------|
| Insert | `add(e)` | `offer(e)` | `put(e)` | `offer(e, time, unit)` |
| Remove | `remove()` | `poll()` | `take()` | `poll(time, unit)` |

```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

// Blocking operations
queue.put("A");                 // Blocks if full
String item = queue.take();     // Blocks if empty

// Timeout operations
boolean added = queue.offer("A", 1, TimeUnit.SECONDS);
String item = queue.poll(1, TimeUnit.SECONDS);
```

---

## Common Patterns

### Process All Items

```java
// With poll (removes items)
String item;
while ((item = queue.poll()) != null) {
    process(item);
}

// With for-each (keeps items)
for (String item : queue) {
    process(item);
}
```

### Producer-Consumer

```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>(100);

// Producer
new Thread(() -> {
    while (running) {
        queue.put(produceItem());  // Blocks if full
    }
}).start();

// Consumer
new Thread(() -> {
    while (running) {
        String item = queue.take();  // Blocks if empty
        process(item);
    }
}).start();
```

### Top K Elements

```java
// Keep k largest elements using min heap
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) {
        minHeap.poll();  // Remove smallest
    }
}
// minHeap now contains k largest elements
```

### BFS Traversal

```java
Queue<Node> queue = new ArrayDeque<>();
Set<Node> visited = new HashSet<>();

queue.offer(startNode);
visited.add(startNode);

while (!queue.isEmpty()) {
    Node node = queue.poll();
    process(node);
    
    for (Node neighbor : node.getNeighbors()) {
        if (!visited.contains(neighbor)) {
            visited.add(neighbor);
            queue.offer(neighbor);
        }
    }
}
```

---

## Common Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| Use Stack class | Legacy, slow | Use `Deque` as stack |
| Iterate PriorityQueue for order | Iterator is unordered | Use `poll()` for order |
| Null in ArrayDeque | NullPointerException | Use LinkedList |
| No comparator for custom objects | ClassCastException | Provide Comparator |
| `take()` on empty queue | Blocks forever | Use `poll(timeout)` |

---

## Performance

| Operation | ArrayDeque | LinkedList | PriorityQueue |
|-----------|------------|------------|---------------|
| offer/add | O(1)* | O(1) | O(log n) |
| poll/remove | O(1) | O(1) | O(log n) |
| peek | O(1) | O(1) | O(1) |

*Amortized

---

## Best Practices

```java
// 1. Use ArrayDeque over LinkedList
Queue<String> queue = new ArrayDeque<>();  // Not LinkedList

// 2. Use Deque for stack operations
Deque<String> stack = new ArrayDeque<>();  // Not Stack class

// 3. Use offer/poll/peek (no exceptions)
queue.offer("A");      // Not add()
queue.poll();          // Not remove()
queue.peek();          // Not element()

// 4. Bound blocking queues
BlockingQueue<String> queue = new LinkedBlockingQueue<>(1000);

// 5. Use timeout with blocking queues
String item = queue.poll(5, TimeUnit.SECONDS);
```

---

## Null Handling

| Type | Allows Null |
|------|-------------|
| ArrayDeque | No |
| LinkedList | Yes |
| PriorityQueue | No |
| ConcurrentLinkedQueue | No |
| ArrayBlockingQueue | No |
| LinkedBlockingQueue | No |

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/20-collections-queues.md)
