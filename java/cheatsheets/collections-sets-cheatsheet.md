# Collections: Sets Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/18-collections-sets.md)

---

## Quick Reference

| Set Type | Order | Null | Performance | Use Case |
|----------|-------|------|-------------|----------|
| HashSet | None | 1 null | O(1) | General purpose, fastest |
| LinkedHashSet | Insertion | 1 null | O(1) | When order matters |
| TreeSet | Sorted | No null | O(log n) | Sorted data needed |
| EnumSet | Enum order | No null | O(1) | Enum values only |

---

## Creating Sets

```java
// Empty mutable set
Set<String> set = new HashSet<>();

// With initial capacity
Set<String> set = new HashSet<>(100);

// From values (immutable)
Set<String> set = Set.of("A", "B", "C");

// From values (mutable)
Set<String> set = new HashSet<>(Set.of("A", "B", "C"));

// From list (removes duplicates)
Set<String> set = new HashSet<>(list);

// Preserve order when removing duplicates
Set<String> set = new LinkedHashSet<>(list);

// Sorted set
Set<String> set = new TreeSet<>();

// Sorted with comparator
Set<String> set = new TreeSet<>(Comparator.reverseOrder());

// Enum set
Set<Day> days = EnumSet.of(Day.MONDAY, Day.FRIDAY);
Set<Day> allDays = EnumSet.allOf(Day.class);
Set<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
```

---

## Adding Elements

```java
boolean added = set.add("X");     // true if new, false if exists
set.addAll(otherCollection);      // Add all from collection
```

---

## Checking Elements

```java
boolean has = set.contains("X");           // Check existence
boolean hasAll = set.containsAll(other);   // Check all exist
int size = set.size();                     // Number of elements
boolean empty = set.isEmpty();             // True if empty
```

---

## Removing Elements

```java
boolean removed = set.remove("X");         // Remove element
set.removeAll(otherCollection);            // Remove all in other
set.retainAll(otherCollection);            // Keep only in other
set.removeIf(s -> s.isEmpty());            // Remove matching
set.clear();                               // Remove all
```

---

## Iterating

```java
// Enhanced for
for (String s : set) { }

// forEach
set.forEach(s -> System.out.println(s));
set.forEach(System.out::println);

// Iterator (for safe removal)
Iterator<String> it = set.iterator();
while (it.hasNext()) {
    if (condition) it.remove();
}
```

---

## Set Operations (Mathematical)

```java
Set<Integer> a = new HashSet<>(Set.of(1, 2, 3));
Set<Integer> b = new HashSet<>(Set.of(2, 3, 4));

// Union (all elements)
Set<Integer> union = new HashSet<>(a);
union.addAll(b);               // {1, 2, 3, 4}

// Intersection (common elements)
Set<Integer> inter = new HashSet<>(a);
inter.retainAll(b);            // {2, 3}

// Difference (in a but not b)
Set<Integer> diff = new HashSet<>(a);
diff.removeAll(b);             // {1}

// Subset check
boolean isSubset = b.containsAll(a);  // Is a subset of b?

// Disjoint check (no common elements)
boolean disjoint = Collections.disjoint(a, b);
```

---

## TreeSet Navigation

```java
TreeSet<Integer> nums = new TreeSet<>(Set.of(10, 20, 30, 40, 50));

nums.first();              // 10 (smallest)
nums.last();               // 50 (largest)

nums.lower(30);            // 20 (strictly less)
nums.floor(30);            // 30 (less or equal)
nums.higher(30);           // 40 (strictly greater)
nums.ceiling(30);          // 30 (greater or equal)

nums.pollFirst();          // 10 (remove and return smallest)
nums.pollLast();           // 50 (remove and return largest)

nums.headSet(30);          // [10, 20] (< 30)
nums.tailSet(30);          // [30, 40, 50] (>= 30)
nums.subSet(20, 40);       // [20, 30] (>= 20 and < 40)

nums.descendingSet();      // [50, 40, 30, 20, 10] (reversed view)
```

---

## Conversions

```java
// Set to List
List<String> list = new ArrayList<>(set);

// Set to Array
String[] arr = set.toArray(new String[0]);
String[] arr = set.toArray(String[]::new);  // Java 11+

// List to Set (removes duplicates)
Set<String> set = new HashSet<>(list);
Set<String> ordered = new LinkedHashSet<>(list);  // Preserve order
```

---

## Stream Operations

```java
// Filter
Set<String> result = set.stream()
    .filter(s -> s.length() > 3)
    .collect(Collectors.toSet());

// Map
Set<Integer> lengths = set.stream()
    .map(String::length)
    .collect(Collectors.toSet());

// To specific Set type
Set<String> sorted = set.stream()
    .collect(Collectors.toCollection(TreeSet::new));

// Check conditions
boolean any = set.stream().anyMatch(s -> s.startsWith("A"));
boolean all = set.stream().allMatch(s -> s.length() > 0);
boolean none = set.stream().noneMatch(s -> s.isEmpty());
```

---

## Immutability

| Method | Mutable | Null | Duplicates in Creation |
|--------|---------|------|------------------------|
| `new HashSet<>()` | Yes | Yes | Silently ignored |
| `Set.of()` | No | No | Throws exception |
| `Set.copyOf()` | No | No | Silently ignored |
| `Collections.unmodifiableSet()` | No | Yes | N/A (wraps existing) |

---

## Thread Safety

```java
// Synchronized wrapper
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());

// Concurrent set (best for concurrent access)
Set<String> concurrentSet = ConcurrentHashMap.newKeySet();

// Copy-on-write (many reads, few writes)
Set<String> cowSet = new CopyOnWriteArraySet<>();
```

---

## equals() and hashCode()

```java
// For HashSet/LinkedHashSet, objects must implement:
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass that = (MyClass) o;
    return Objects.equals(id, that.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

---

## Common Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| Modify during for-each | ConcurrentModificationException | Use Iterator or removeIf |
| Null in TreeSet | NullPointerException | Use HashSet or check for null |
| Modify Set.of() | UnsupportedOperationException | Wrap in `new HashSet<>()` |
| Mutate element after add | Element becomes "lost" | Use immutable objects |
| TreeSet with bad Comparator | Elements not added | Comparator must distinguish all |

---

## Performance

| Operation | HashSet | LinkedHashSet | TreeSet |
|-----------|---------|---------------|---------|
| add | O(1) | O(1) | O(log n) |
| remove | O(1) | O(1) | O(log n) |
| contains | O(1) | O(1) | O(log n) |
| iteration | O(n) | O(n) | O(n) |
| first/last | N/A | N/A | O(1) |

---

## Best Practices

```java
// 1. Program to interface
Set<String> set = new HashSet<>();  // Not HashSet<String> set

// 2. Use EnumSet for enums
Set<Day> days = EnumSet.of(Day.MONDAY);  // Not new HashSet<>()

// 3. Specify capacity for large sets
Set<String> large = new HashSet<>(10000);

// 4. Return unmodifiable from methods
public Set<String> getItems() {
    return Collections.unmodifiableSet(items);
}

// 5. Use LinkedHashSet to preserve order when removing duplicates
List<String> unique = new ArrayList<>(new LinkedHashSet<>(list));
```

---

## Common Use Cases

```java
// Remove duplicates (preserve order)
List<String> unique = new ArrayList<>(new LinkedHashSet<>(list));

// Check membership
if (validCodes.contains(code)) { ... }

// Find common elements
Set<String> common = new HashSet<>(set1);
common.retainAll(set2);

// Track unique items
Set<String> seen = new HashSet<>();
for (String item : items) {
    if (!seen.add(item)) {
        // Duplicate found
    }
}
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/18-collections-sets.md)
