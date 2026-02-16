# Collections: Lists Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/17-collections-lists.md)

---

## Quick Reference

| List Type | Best For | Avoid When |
|-----------|----------|------------|
| ArrayList | Random access, general use | Frequent insert/remove at start |
| LinkedList | Add/remove at start/end | Random access by index |
| CopyOnWriteArrayList | Thread-safe, many reads | Frequent writes |
| List.of() | Immutable constants | Need to modify |

---

## Creating Lists

```java
// Empty mutable list
List<String> list = new ArrayList<>();

// With initial capacity
List<String> list = new ArrayList<>(100);

// From values (immutable)
List<String> list = List.of("A", "B", "C");

// From values (mutable)
List<String> list = new ArrayList<>(List.of("A", "B", "C"));

// From array
List<String> list = new ArrayList<>(Arrays.asList(array));

// Copy of another list
List<String> copy = new ArrayList<>(original);
```

---

## Adding Elements

```java
list.add("X");                  // Add at end
list.add(0, "X");               // Add at index (shifts others)
list.addAll(otherList);         // Add all from collection
list.addAll(0, otherList);      // Add all at index

// Java 21+
list.addFirst("X");             // Add at beginning
list.addLast("X");              // Add at end
```

---

## Accessing Elements

```java
String s = list.get(0);         // Get by index
String first = list.getFirst(); // Java 21+
String last = list.getLast();   // Java 21+

int size = list.size();         // Number of elements
boolean empty = list.isEmpty(); // True if empty

boolean has = list.contains("X");      // Check existence
int idx = list.indexOf("X");           // First index (-1 if not found)
int lastIdx = list.lastIndexOf("X");   // Last index
```

---

## Modifying Elements

```java
String old = list.set(0, "New");       // Replace at index, returns old

list.replaceAll(s -> s.toUpperCase()); // Transform all elements

Collections.fill(list, "X");           // Fill all with same value
Collections.replaceAll(list, "A", "B"); // Replace all A with B
```

---

## Removing Elements

```java
String removed = list.remove(0);       // By index, returns removed
boolean ok = list.remove("X");         // By value (first occurrence)

list.removeFirst();                    // Java 21+
list.removeLast();                     // Java 21+

list.removeIf(s -> s.isEmpty());       // All matching condition
list.removeAll(otherList);             // All in other collection
list.retainAll(otherList);             // Keep only in other collection
list.clear();                          // Remove all
```

### Remove Integer by Value

```java
List<Integer> nums = new ArrayList<>(List.of(1, 2, 3));
nums.remove(Integer.valueOf(2));       // Removes value 2, not index 2
```

---

## Iterating

```java
// Enhanced for (most common)
for (String s : list) { }

// Traditional for (when index needed)
for (int i = 0; i < list.size(); i++) {
    String s = list.get(i);
}

// forEach with lambda
list.forEach(s -> System.out.println(s));
list.forEach(System.out::println);

// Iterator (for safe removal)
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (condition) it.remove();
}

// ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) { lit.next(); }
while (lit.hasPrevious()) { lit.previous(); }
```

---

## Sorting

```java
// Natural order
Collections.sort(list);
list.sort(null);

// Reverse order
Collections.sort(list, Collections.reverseOrder());
list.sort(Comparator.reverseOrder());

// Custom comparator
list.sort(Comparator.comparingInt(String::length));
list.sort((a, b) -> a.compareTo(b));

// Chain comparators
list.sort(Comparator.comparingInt(Person::getAge)
                    .thenComparing(Person::getName));

// Reverse any comparator
list.sort(Comparator.comparing(Person::getName).reversed());
```

---

## Searching

```java
// Linear search
boolean found = list.contains("X");
int index = list.indexOf("X");

// Binary search (list must be sorted!)
Collections.sort(list);
int idx = Collections.binarySearch(list, "X");
// Returns negative if not found: -(insertion point) - 1
```

---

## Common Operations

```java
// Reverse
Collections.reverse(list);

// Shuffle
Collections.shuffle(list);

// Rotate
Collections.rotate(list, 2);           // Right by 2

// Min/Max
String min = Collections.min(list);
String max = Collections.max(list);

// Frequency
int count = Collections.frequency(list, "X");

// Sublist (view - changes affect original!)
List<String> sub = list.subList(1, 4); // fromIndex inclusive, toIndex exclusive

// Independent sublist copy
List<String> subCopy = new ArrayList<>(list.subList(1, 4));
```

---

## List to Array

```java
// Object array
Object[] arr = list.toArray();

// Typed array
String[] arr = list.toArray(new String[0]);

// Java 11+
String[] arr = list.toArray(String[]::new);
```

---

## Array to List

```java
// Fixed-size (can modify elements, not add/remove)
List<String> list = Arrays.asList(array);

// Mutable copy
List<String> list = new ArrayList<>(Arrays.asList(array));

// Primitive array to List
int[] arr = {1, 2, 3};
List<Integer> list = Arrays.stream(arr).boxed().toList();
```

---

## Stream Operations

```java
// Filter
list.stream().filter(s -> s.length() > 3).toList();

// Map
list.stream().map(String::toUpperCase).toList();

// Find
Optional<String> first = list.stream().filter(condition).findFirst();

// Count
long count = list.stream().filter(condition).count();

// Distinct
list.stream().distinct().toList();

// Sorted
list.stream().sorted().toList();

// Limit/Skip
list.stream().skip(5).limit(10).toList();

// Reduce
int sum = nums.stream().reduce(0, Integer::sum);

// Any/All/None match
boolean any = list.stream().anyMatch(condition);
boolean all = list.stream().allMatch(condition);
boolean none = list.stream().noneMatch(condition);
```

---

## Immutability

| Method | Mutable | Add/Remove | Modify | Null |
|--------|---------|------------|--------|------|
| `new ArrayList<>()` | Yes | Yes | Yes | Yes |
| `List.of()` | No | No | No | No |
| `Arrays.asList()` | Partial | No | Yes | Yes |
| `Collections.unmodifiableList()` | No | No | No | Yes |
| `List.copyOf()` | No | No | No | No |

---

## Thread Safety

```java
// Synchronized wrapper
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// Thread-safe (many reads, few writes)
List<String> cowList = new CopyOnWriteArrayList<>();
```

---

## Common Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| `list.remove(20)` on `List<Integer>` | Removes at index 20, not value | `list.remove(Integer.valueOf(20))` |
| Modify during for-each | ConcurrentModificationException | Use Iterator or removeIf |
| Assume subList is copy | Changes affect original | `new ArrayList<>(subList)` |
| Modify List.of() result | UnsupportedOperationException | Wrap in ArrayList |
| LinkedList with get(i) in loop | O(n) per access | Use for-each or ArrayList |

---

## Performance

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| get(index) | O(1) | O(n) |
| add(end) | O(1)* | O(1) |
| add(start) | O(n) | O(1) |
| add(middle) | O(n) | O(1)** |
| remove(index) | O(n) | O(n) |
| contains | O(n) | O(n) |
| iterator.remove | O(n) | O(1) |

*Amortized (occasional resize)  
**After finding position

---

## Best Practices

```java
// 1. Program to interface
List<String> list = new ArrayList<>();  // Not ArrayList<String> list

// 2. Use diamond operator
List<String> list = new ArrayList<>();  // Not new ArrayList<String>()

// 3. Specify initial capacity for large lists
List<String> list = new ArrayList<>(10000);

// 4. Return unmodifiable from methods
public List<String> getItems() {
    return Collections.unmodifiableList(items);
}

// 5. Use removeIf instead of manual iteration
list.removeIf(s -> s.isEmpty());
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/17-collections-lists.md)
