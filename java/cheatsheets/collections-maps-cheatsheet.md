# Collections: Maps Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/19-collections-maps.md)

---

## Quick Reference

| Map Type | Key Order | Null Keys | Null Values | Performance |
|----------|-----------|-----------|-------------|-------------|
| HashMap | None | 1 null | Multiple | O(1) |
| LinkedHashMap | Insertion/access | 1 null | Multiple | O(1) |
| TreeMap | Sorted | No null | Multiple | O(log n) |
| EnumMap | Enum order | No null | Multiple | O(1) |
| ConcurrentHashMap | None | No null | No null | O(1), thread-safe |

---

## Creating Maps

```java
// Empty mutable map
Map<String, Integer> map = new HashMap<>();

// With initial capacity
Map<String, Integer> map = new HashMap<>(100);

// From values (immutable, up to 10 pairs)
Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);

// From entries (immutable, any number)
Map<String, Integer> map = Map.ofEntries(
    Map.entry("A", 1),
    Map.entry("B", 2)
);

// Mutable from immutable
Map<String, Integer> map = new HashMap<>(Map.of("A", 1));

// Ordered by insertion
Map<String, Integer> map = new LinkedHashMap<>();

// Sorted by key
Map<String, Integer> map = new TreeMap<>();

// Sorted with comparator
Map<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());

// Enum keys
Map<Day, String> map = new EnumMap<>(Day.class);
```

---

## Adding/Updating Entries

```java
Integer prev = map.put("A", 1);         // Returns previous value or null
map.putIfAbsent("A", 1);                // Only if key absent
map.putAll(otherMap);                   // Add all from other map

map.replace("A", 2);                    // Replace if key exists
map.replace("A", 1, 2);                 // Replace if key=value matches
map.replaceAll((k, v) -> v * 2);        // Transform all values

// Compute
map.compute("A", (k, v) -> v == null ? 0 : v + 1);
map.computeIfAbsent("A", k -> expensive());
map.computeIfPresent("A", (k, v) -> v + 1);

// Merge (great for counting)
map.merge("A", 1, Integer::sum);        // Add 1, or set to 1 if absent
map.merge("A", 1, (old, new_) -> old + new_);
```

---

## Getting Values

```java
Integer val = map.get("A");              // Returns null if not found
Integer val = map.getOrDefault("A", 0);  // Returns 0 if not found

boolean hasKey = map.containsKey("A");
boolean hasVal = map.containsValue(1);
int size = map.size();
boolean empty = map.isEmpty();
```

---

## Removing Entries

```java
Integer removed = map.remove("A");       // Returns removed value
boolean ok = map.remove("A", 1);         // Only if value matches
map.clear();                             // Remove all

// Remove with condition
map.entrySet().removeIf(e -> e.getValue() < 0);
map.keySet().removeIf(k -> k.startsWith("temp"));
```

---

## Iterating

```java
// Over entries (most common)
for (Map.Entry<String, Integer> e : map.entrySet()) {
    String key = e.getKey();
    Integer val = e.getValue();
}

// forEach lambda (cleanest)
map.forEach((key, value) -> System.out.println(key + "=" + value));

// Over keys
for (String key : map.keySet()) { }

// Over values
for (Integer val : map.values()) { }

// Iterator for safe removal
Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    if (condition) it.remove();
}
```

---

## Views

```java
Set<String> keys = map.keySet();              // All keys
Collection<Integer> values = map.values();    // All values
Set<Map.Entry<K, V>> entries = map.entrySet(); // All entries

// Views are backed by map!
map.put("new", 1);
// keys, values, entries now include "new"
```

---

## TreeMap Navigation

```java
TreeMap<Integer, String> tree = new TreeMap<>();

tree.firstKey();                 // Smallest key
tree.lastKey();                  // Largest key
tree.firstEntry();               // Entry with smallest key
tree.lastEntry();                // Entry with largest key

tree.lowerKey(30);               // Largest key < 30
tree.floorKey(30);               // Largest key <= 30
tree.higherKey(30);              // Smallest key > 30
tree.ceilingKey(30);             // Smallest key >= 30

tree.pollFirstEntry();           // Remove and return first
tree.pollLastEntry();            // Remove and return last

tree.headMap(30);                // Keys < 30 (view)
tree.tailMap(30);                // Keys >= 30 (view)
tree.subMap(20, 40);             // 20 <= keys < 40 (view)
tree.descendingMap();            // Reversed view
```

---

## Stream Collectors

```java
// List to Map
Map<String, Integer> map = list.stream()
    .collect(Collectors.toMap(
        item -> item.getName(),    // Key
        item -> item.getValue()    // Value
    ));

// Handle duplicate keys
Map<String, Integer> map = list.stream()
    .collect(Collectors.toMap(
        Item::getName,
        Item::getValue,
        (existing, replacement) -> existing  // Merge function
    ));

// Collect to specific Map type
Map<String, Integer> sorted = list.stream()
    .collect(Collectors.toMap(
        Item::getName,
        Item::getValue,
        (a, b) -> a,
        TreeMap::new
    ));

// Grouping
Map<String, List<Item>> grouped = list.stream()
    .collect(Collectors.groupingBy(Item::getCategory));

// Grouping with downstream
Map<String, Long> counts = list.stream()
    .collect(Collectors.groupingBy(
        Item::getCategory,
        Collectors.counting()
    ));

// Partitioning (boolean groups)
Map<Boolean, List<Integer>> parts = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
```

---

## Common Patterns

```java
// Count occurrences
Map<String, Integer> counts = new HashMap<>();
for (String word : words) {
    counts.merge(word, 1, Integer::sum);
}

// Group into lists
Map<String, List<Item>> groups = new HashMap<>();
for (Item item : items) {
    groups.computeIfAbsent(item.getCategory(), k -> new ArrayList<>())
          .add(item);
}

// Get or create default
List<String> list = map.computeIfAbsent("key", k -> new ArrayList<>());

// Find max by value
Map.Entry<String, Integer> max = map.entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .orElse(null);

// Sort by value
Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new
    ));

// Invert map (swap keys and values)
Map<Integer, String> inverted = map.entrySet().stream()
    .collect(Collectors.toMap(
        Map.Entry::getValue,
        Map.Entry::getKey
    ));
```

---

## LRU Cache

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;
    
    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true);  // true = access order
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
```

---

## Thread Safety

```java
// Synchronized wrapper
Map<K, V> syncMap = Collections.synchronizedMap(new HashMap<>());

// ConcurrentHashMap (recommended)
Map<K, V> concurrent = new ConcurrentHashMap<>();

// Atomic operations with ConcurrentHashMap
concurrent.computeIfAbsent(key, k -> expensiveComputation());
concurrent.merge(key, 1, Integer::sum);
```

---

## Immutability

| Method | Mutable | Null Keys | Null Values |
|--------|---------|-----------|-------------|
| `new HashMap<>()` | Yes | 1 | Multiple |
| `Map.of()` | No | No | No |
| `Map.copyOf()` | No | No | No |
| `Collections.unmodifiableMap()` | No | Depends | Depends |

---

## Common Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| Modify during for-each | ConcurrentModificationException | Use Iterator or removeIf |
| Null key in TreeMap | NullPointerException | Use HashMap |
| Mutate key after insert | Key becomes "lost" | Use immutable keys |
| Forget merge function in stream | Exception on duplicates | Add merge function |
| Modify Map.of() result | UnsupportedOperationException | Wrap in HashMap |

---

## equals() and hashCode()

```java
// For HashMap/LinkedHashMap keys, implement both:
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyKey that = (MyKey) o;
    return Objects.equals(id, that.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

---

## Best Practices

```java
// 1. Program to interface
Map<String, Integer> map = new HashMap<>();

// 2. Use getOrDefault/computeIfAbsent
int count = map.getOrDefault("key", 0);
List<String> list = map.computeIfAbsent("key", k -> new ArrayList<>());

// 3. Use merge for counting
map.merge("key", 1, Integer::sum);

// 4. Specify initial capacity for large maps
Map<String, Integer> large = new HashMap<>(10000);

// 5. Return unmodifiable from methods
return Collections.unmodifiableMap(internal);
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/19-collections-maps.md)
