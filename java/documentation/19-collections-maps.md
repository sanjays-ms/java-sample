# Collections: Maps

[Back to Guide](../guide.md) | [Previous: Collections - Sets](18-collections-sets.md) | [Next: Collections - Queues](20-collections-queues.md)

---

## What Is a Map?

A Map is a collection that stores key-value pairs. Each key maps to exactly one value. You can think of it like a dictionary where you look up a word (key) to find its definition (value), or like a phone book where you look up a name (key) to find a phone number (value).

Maps are not part of the Collection interface, but they are part of the Collections Framework.

### Key Characteristics of Maps

| Characteristic | Description |
|----------------|-------------|
| Key-value pairs | Each entry has a key and associated value |
| Unique keys | No duplicate keys allowed (each key maps to one value) |
| Values can duplicate | Multiple keys can map to the same value |
| Fast lookup | Get value by key is typically O(1) or O(log n) |

### Map vs List vs Set

| Feature | Map | List | Set |
|---------|-----|------|-----|
| Stores | Key-value pairs | Single elements | Single elements |
| Access by | Key | Index | N/A |
| Duplicates | No duplicate keys | Allowed | Not allowed |
| Order | Depends on implementation | Insertion order | Depends on implementation |

---

## The Map Interface

The Map interface defines operations common to all map implementations.

```java
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class MapInterface {
    public static void main(String[] args) {
        // Different Map implementations
        Map<String, Integer> hashMap = new HashMap<>();       // Unordered
        Map<String, Integer> linkedMap = new LinkedHashMap<>(); // Insertion order
        Map<String, Integer> treeMap = new TreeMap<>();        // Sorted by key
        
        // All support the same basic operations
        hashMap.put("Alice", 25);
        linkedMap.put("Alice", 25);
        treeMap.put("Alice", 25);
    }
}
```

---

## Map Implementations Comparison

| Implementation | Key Order | Null Keys | Null Values | Performance |
|----------------|-----------|-----------|-------------|-------------|
| HashMap | None | One null | Multiple | O(1) get/put |
| LinkedHashMap | Insertion or access order | One null | Multiple | O(1) get/put |
| TreeMap | Sorted (natural or comparator) | No null | Multiple | O(log n) get/put |
| Hashtable | None | No null | No null | O(1), synchronized |
| ConcurrentHashMap | None | No null | No null | O(1), thread-safe |
| EnumMap | Natural enum order | No null | Multiple | O(1), enum keys only |

---

## HashMap

HashMap is the most commonly used Map implementation. It uses a hash table for storage, providing constant-time performance for basic operations.

### Creating a HashMap

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapCreation {
    public static void main(String[] args) {
        // Empty HashMap
        Map<String, Integer> ages = new HashMap<>();
        
        // HashMap with initial capacity
        Map<String, String> config = new HashMap<>(100);
        
        // HashMap with capacity and load factor
        Map<String, Double> prices = new HashMap<>(100, 0.75f);
        
        // HashMap from another map
        Map<String, Integer> copy = new HashMap<>(ages);
        
        // Using Map.of (immutable, Java 9+)
        Map<String, Integer> immutable = Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35
        );
        
        // Mutable copy from Map.of
        Map<String, Integer> mutable = new HashMap<>(Map.of(
            "Alice", 25,
            "Bob", 30
        ));
        
        // Using Map.ofEntries for more than 10 entries
        Map<String, Integer> large = Map.ofEntries(
            Map.entry("A", 1),
            Map.entry("B", 2),
            Map.entry("C", 3)
        );
    }
}
```

### Adding and Updating Entries

```java
import java.util.HashMap;
import java.util.Map;

public class AddingEntries {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>();
        
        // Put single entry (returns previous value or null)
        Integer prev1 = ages.put("Alice", 25);   // null (new key)
        Integer prev2 = ages.put("Bob", 30);     // null (new key)
        Integer prev3 = ages.put("Alice", 26);   // 25 (replaced)
        
        System.out.println("Previous Alice age: " + prev3);  // 25
        System.out.println(ages);  // {Alice=26, Bob=30}
        
        // Put only if key is absent
        ages.putIfAbsent("Charlie", 35);  // Added (key absent)
        ages.putIfAbsent("Alice", 100);   // Ignored (key exists)
        System.out.println(ages);  // {Alice=26, Bob=30, Charlie=35}
        
        // Put all from another map
        Map<String, Integer> more = Map.of("David", 40, "Eve", 28);
        ages.putAll(more);
        System.out.println(ages);
        
        // Put null key and value (HashMap allows)
        ages.put(null, 0);
        ages.put("Unknown", null);
        System.out.println("Null key value: " + ages.get(null));  // 0
    }
}
```

### Getting Values

```java
import java.util.HashMap;
import java.util.Map;

public class GettingValues {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>(Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35
        ));
        
        // Get value by key
        Integer aliceAge = ages.get("Alice");   // 25
        Integer unknownAge = ages.get("David"); // null (key not found)
        
        // Get with default value (if key not found)
        Integer davidAge = ages.getOrDefault("David", 0);  // 0
        Integer bobAge = ages.getOrDefault("Bob", 0);      // 30
        
        System.out.println("Alice: " + aliceAge);
        System.out.println("David (default): " + davidAge);
        
        // Check if key exists
        boolean hasAlice = ages.containsKey("Alice");   // true
        boolean hasDavid = ages.containsKey("David");   // false
        
        // Check if value exists
        boolean has25 = ages.containsValue(25);   // true
        boolean has100 = ages.containsValue(100); // false
        
        // Get size
        int size = ages.size();  // 3
        
        // Check if empty
        boolean empty = ages.isEmpty();  // false
    }
}
```

### Removing Entries

```java
import java.util.HashMap;
import java.util.Map;

public class RemovingEntries {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>(Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35,
            "David", 40
        ));
        
        // Remove by key (returns removed value or null)
        Integer removed = ages.remove("Alice");
        System.out.println("Removed: " + removed);  // 25
        System.out.println(ages);  // {Bob=30, Charlie=35, David=40}
        
        // Remove only if key maps to specific value
        boolean removed1 = ages.remove("Bob", 30);    // true (value matches)
        boolean removed2 = ages.remove("Charlie", 99); // false (value doesn't match)
        System.out.println(ages);  // {Charlie=35, David=40}
        
        // Reset map
        ages = new HashMap<>(Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35,
            "David", 40
        ));
        
        // Remove entries matching condition
        ages.entrySet().removeIf(entry -> entry.getValue() > 30);
        System.out.println("After removeIf: " + ages);  // {Alice=25, Bob=30}
        
        // Clear all entries
        ages.clear();
        System.out.println("After clear: " + ages);  // {}
    }
}
```

### Updating Values

```java
import java.util.HashMap;
import java.util.Map;

public class UpdatingValues {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>(Map.of(
            "Alice", 100,
            "Bob", 85,
            "Charlie", 90
        ));
        
        // Replace value (only if key exists)
        Integer oldAlice = scores.replace("Alice", 105);
        Integer oldDavid = scores.replace("David", 50);  // null (key doesn't exist)
        System.out.println("Old Alice score: " + oldAlice);  // 100
        System.out.println(scores);  // {Alice=105, Bob=85, Charlie=90}
        
        // Replace only if current value matches
        boolean replaced = scores.replace("Bob", 85, 90);  // true
        System.out.println("Bob replaced: " + replaced);
        System.out.println(scores);  // {Alice=105, Bob=90, Charlie=90}
        
        // Replace all values using function
        scores.replaceAll((name, score) -> score + 10);
        System.out.println("After +10: " + scores);  // {Alice=115, Bob=100, Charlie=100}
        
        // Compute new value based on key
        scores.compute("Alice", (key, value) -> value == null ? 0 : value + 5);
        System.out.println("After compute: " + scores.get("Alice"));  // 120
        
        // Compute only if key is absent
        scores.computeIfAbsent("David", key -> key.length() * 10);
        System.out.println("David: " + scores.get("David"));  // 50 (5 letters * 10)
        
        // Compute only if key is present
        scores.computeIfPresent("Charlie", (key, value) -> value * 2);
        System.out.println("Charlie: " + scores.get("Charlie"));  // 200
        
        // Merge (combine old and new value)
        scores.merge("Alice", 10, Integer::sum);  // Add 10 to Alice's score
        System.out.println("Alice after merge: " + scores.get("Alice"));  // 130
        
        // Merge with absent key (just sets the value)
        scores.merge("Eve", 75, Integer::sum);
        System.out.println("Eve: " + scores.get("Eve"));  // 75
    }
}
```

---

## Iterating Over Maps

Maps can be iterated in several ways: by keys, values, or entries.

### Iterating Over Keys

```java
import java.util.Map;
import java.util.HashMap;

public class IterateKeys {
    public static void main(String[] args) {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        
        // Enhanced for loop
        for (String name : ages.keySet()) {
            System.out.println(name + " -> " + ages.get(name));
        }
        
        // forEach with lambda
        ages.keySet().forEach(name -> System.out.println(name));
    }
}
```

### Iterating Over Values

```java
import java.util.Map;

public class IterateValues {
    public static void main(String[] args) {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        
        // Enhanced for loop
        for (Integer age : ages.values()) {
            System.out.println(age);
        }
        
        // forEach with lambda
        ages.values().forEach(System.out::println);
    }
}
```

### Iterating Over Entries (Most Common)

```java
import java.util.Map;
import java.util.HashMap;

public class IterateEntries {
    public static void main(String[] args) {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        
        // Enhanced for loop (most efficient for both key and value)
        for (Map.Entry<String, Integer> entry : ages.entrySet()) {
            String name = entry.getKey();
            Integer age = entry.getValue();
            System.out.println(name + " is " + age + " years old");
        }
        
        // forEach with lambda (cleanest)
        ages.forEach((name, age) -> {
            System.out.println(name + " is " + age + " years old");
        });
    }
}
```

### Iterating with Iterator (Safe Removal)

```java
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class IteratorRemoval {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>(Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35,
            "David", 40
        ));
        
        // Remove during iteration using Iterator
        Iterator<Map.Entry<String, Integer>> iterator = ages.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if (entry.getValue() > 30) {
                iterator.remove();  // Safe removal
            }
        }
        System.out.println(ages);  // {Alice=25, Bob=30}
        
        // Or use removeIf (simpler)
        ages = new HashMap<>(Map.of("Alice", 25, "Bob", 30, "Charlie", 35));
        ages.entrySet().removeIf(entry -> entry.getValue() > 30);
        System.out.println(ages);  // {Alice=25, Bob=30}
    }
}
```

---

## LinkedHashMap

LinkedHashMap maintains insertion order (or access order) while providing O(1) performance. It uses a hash table with a doubly-linked list running through entries.

### Insertion Order

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapInsertion {
    public static void main(String[] args) {
        Map<String, Integer> ages = new LinkedHashMap<>();
        
        ages.put("Charlie", 35);
        ages.put("Alice", 25);
        ages.put("Bob", 30);
        
        // Iteration order matches insertion order
        ages.forEach((name, age) -> System.out.println(name + ": " + age));
        // Output:
        // Charlie: 35
        // Alice: 25
        // Bob: 30
        
        // Compare with HashMap (order unpredictable)
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Charlie", 35);
        hashMap.put("Alice", 25);
        hashMap.put("Bob", 30);
        System.out.println("HashMap: " + hashMap);  // Order varies
    }
}
```

### Access Order (LRU Cache)

LinkedHashMap can maintain access order instead of insertion order, making it useful for implementing LRU (Least Recently Used) caches.

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapAccessOrder {
    public static void main(String[] args) {
        // true = access order, false = insertion order (default)
        Map<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
        
        accessOrder.put("A", 1);
        accessOrder.put("B", 2);
        accessOrder.put("C", 3);
        System.out.println("Initial: " + accessOrder);  // {A=1, B=2, C=3}
        
        // Access "A" - moves to end
        accessOrder.get("A");
        System.out.println("After get(A): " + accessOrder);  // {B=2, C=3, A=1}
        
        // Access "B" - moves to end
        accessOrder.get("B");
        System.out.println("After get(B): " + accessOrder);  // {C=3, A=1, B=2}
    }
}
```

### Simple LRU Cache

```java
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;
    
    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true);  // true = access order
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
    
    public static void main(String[] args) {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        System.out.println(cache);  // {A=1, B=2, C=3}
        
        cache.put("D", 4);  // Exceeds max size, removes least recently used (A)
        System.out.println(cache);  // {B=2, C=3, D=4}
        
        cache.get("B");     // Access B, moves to end
        cache.put("E", 5);  // Removes least recently used (C)
        System.out.println(cache);  // {D=4, B=2, E=5}
    }
}
```

---

## TreeMap

TreeMap stores entries sorted by key using a Red-Black tree. Keys must be Comparable or a Comparator must be provided.

### Creating a TreeMap

```java
import java.util.TreeMap;
import java.util.Map;
import java.util.Comparator;

public class TreeMapCreation {
    public static void main(String[] args) {
        // Natural ordering (keys must implement Comparable)
        Map<String, Integer> natural = new TreeMap<>();
        natural.put("Charlie", 35);
        natural.put("Alice", 25);
        natural.put("Bob", 30);
        System.out.println(natural);  // {Alice=25, Bob=30, Charlie=35} - sorted!
        
        // Reverse order
        Map<String, Integer> reverse = new TreeMap<>(Comparator.reverseOrder());
        reverse.put("Charlie", 35);
        reverse.put("Alice", 25);
        reverse.put("Bob", 30);
        System.out.println(reverse);  // {Charlie=35, Bob=30, Alice=25}
        
        // Case-insensitive order
        Map<String, Integer> caseInsensitive = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitive.put("Alice", 25);
        caseInsensitive.put("alice", 26);  // Replaces (same key case-insensitively)
        System.out.println(caseInsensitive);  // {Alice=26}
        
        // Custom comparator (by key length)
        Map<String, Integer> byLength = new TreeMap<>(Comparator.comparingInt(String::length));
        byLength.put("Bob", 30);
        byLength.put("Alice", 25);
        byLength.put("Jo", 40);
        System.out.println(byLength);  // {Jo=40, Bob=30, Alice=25}
    }
}
```

### TreeMap Navigation Methods

```java
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Map;

public class TreeMapNavigation {
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(10, "Ten");
        map.put(20, "Twenty");
        map.put(30, "Thirty");
        map.put(40, "Forty");
        map.put(50, "Fifty");
        
        // First and last
        System.out.println("First key: " + map.firstKey());        // 10
        System.out.println("Last key: " + map.lastKey());          // 50
        System.out.println("First entry: " + map.firstEntry());    // 10=Ten
        System.out.println("Last entry: " + map.lastEntry());      // 50=Fifty
        
        // Lower (strictly less than)
        System.out.println("Lower than 30: " + map.lowerKey(30));  // 20
        System.out.println("Lower than 25: " + map.lowerKey(25));  // 20
        
        // Floor (less than or equal)
        System.out.println("Floor of 30: " + map.floorKey(30));    // 30
        System.out.println("Floor of 25: " + map.floorKey(25));    // 20
        
        // Higher (strictly greater than)
        System.out.println("Higher than 30: " + map.higherKey(30)); // 40
        
        // Ceiling (greater than or equal)
        System.out.println("Ceiling of 30: " + map.ceilingKey(30)); // 30
        System.out.println("Ceiling of 25: " + map.ceilingKey(25)); // 30
        
        // Poll (remove and return)
        System.out.println("Poll first: " + map.pollFirstEntry()); // 10=Ten
        System.out.println("Poll last: " + map.pollLastEntry());   // 50=Fifty
        System.out.println("Remaining: " + map);  // {20=Twenty, 30=Thirty, 40=Forty}
    }
}
```

### TreeMap Range Views

```java
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.SortedMap;

public class TreeMapRanges {
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();
        for (int i = 10; i <= 100; i += 10) {
            map.put(i, "Value" + i);
        }
        
        // Submap (from inclusive, to exclusive by default)
        SortedMap<Integer, String> subMap = map.subMap(30, 70);
        System.out.println("subMap(30, 70): " + subMap);
        // {30=Value30, 40=Value40, 50=Value50, 60=Value60}
        
        // Submap with inclusive/exclusive control
        NavigableMap<Integer, String> navSubMap = map.subMap(30, true, 70, true);
        System.out.println("subMap(30, true, 70, true): " + navSubMap);
        // {30=Value30, 40=Value40, 50=Value50, 60=Value60, 70=Value70}
        
        // Head map (keys less than)
        SortedMap<Integer, String> headMap = map.headMap(50);
        System.out.println("headMap(50): " + headMap);
        // {10=Value10, 20=Value20, 30=Value30, 40=Value40}
        
        // Tail map (keys greater than or equal)
        SortedMap<Integer, String> tailMap = map.tailMap(50);
        System.out.println("tailMap(50): " + tailMap);
        // {50=Value50, 60=Value60, ..., 100=Value100}
        
        // Descending map (reversed view)
        NavigableMap<Integer, String> descending = map.descendingMap();
        System.out.println("Descending: " + descending);
        // {100=Value100, 90=Value90, ..., 10=Value10}
        
        // Views are backed by original map!
        map.put(55, "Value55");
        System.out.println("After adding 55, subMap: " + subMap);
        // {30=Value30, 40=Value40, 50=Value50, 55=Value55, 60=Value60}
    }
}
```

---

## EnumMap

EnumMap is a specialized Map implementation for enum keys. It is extremely efficient and should always be used when keys are enum values.

```java
import java.util.EnumMap;
import java.util.Map;

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

public class EnumMapExample {
    public static void main(String[] args) {
        // Create EnumMap
        Map<Day, String> schedule = new EnumMap<>(Day.class);
        
        schedule.put(Day.MONDAY, "Work");
        schedule.put(Day.TUESDAY, "Work");
        schedule.put(Day.WEDNESDAY, "Work");
        schedule.put(Day.THURSDAY, "Work");
        schedule.put(Day.FRIDAY, "Work");
        schedule.put(Day.SATURDAY, "Rest");
        schedule.put(Day.SUNDAY, "Rest");
        
        // Iteration is in enum declaration order
        schedule.forEach((day, activity) -> 
            System.out.println(day + ": " + activity));
        
        // Get value
        String mondayPlan = schedule.get(Day.MONDAY);
        System.out.println("Monday: " + mondayPlan);
        
        // EnumMap is more efficient than HashMap for enum keys
        // Uses internal array indexed by enum ordinal
    }
}
```

### EnumMap with Default Values

```java
import java.util.EnumMap;
import java.util.Map;

enum Status {
    PENDING, PROCESSING, COMPLETED, FAILED
}

public class EnumMapDefaults {
    public static void main(String[] args) {
        // Initialize all enum values with default
        Map<Status, Integer> statusCounts = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            statusCounts.put(status, 0);
        }
        
        // Increment counts
        statusCounts.merge(Status.PENDING, 1, Integer::sum);
        statusCounts.merge(Status.PENDING, 1, Integer::sum);
        statusCounts.merge(Status.COMPLETED, 1, Integer::sum);
        
        System.out.println(statusCounts);
        // {PENDING=2, PROCESSING=0, COMPLETED=1, FAILED=0}
    }
}
```

---

## Immutable Maps

### Map.of() (Java 9+)

```java
import java.util.Map;

public class ImmutableMaps {
    public static void main(String[] args) {
        // Up to 10 key-value pairs
        Map<String, Integer> small = Map.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35
        );
        
        // Reading works
        System.out.println(small.get("Alice"));  // 25
        
        // Modification throws exception
        try {
            small.put("David", 40);  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutable map");
        }
        
        // No null keys or values
        try {
            Map<String, Integer> withNull = Map.of("A", null);  // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("Map.of does not allow null");
        }
        
        // No duplicate keys
        try {
            Map<String, Integer> dup = Map.of("A", 1, "A", 2);  // IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Map.of does not allow duplicate keys");
        }
    }
}
```

### Map.ofEntries() (Java 9+)

For more than 10 entries or for programmatic creation:

```java
import java.util.Map;
import static java.util.Map.entry;

public class MapOfEntries {
    public static void main(String[] args) {
        Map<String, Integer> map = Map.ofEntries(
            entry("A", 1),
            entry("B", 2),
            entry("C", 3),
            entry("D", 4),
            entry("E", 5),
            entry("F", 6),
            entry("G", 7),
            entry("H", 8),
            entry("I", 9),
            entry("J", 10),
            entry("K", 11),  // More than 10 entries
            entry("L", 12)
        );
        
        System.out.println("Size: " + map.size());  // 12
    }
}
```

### Map.copyOf() (Java 10+)

```java
import java.util.Map;
import java.util.HashMap;

public class MapCopyOf {
    public static void main(String[] args) {
        Map<String, Integer> mutable = new HashMap<>();
        mutable.put("Alice", 25);
        mutable.put("Bob", 30);
        
        // Create immutable copy
        Map<String, Integer> immutable = Map.copyOf(mutable);
        
        // Modifying original doesn't affect copy
        mutable.put("Charlie", 35);
        System.out.println(mutable);    // {Alice=25, Bob=30, Charlie=35}
        System.out.println(immutable);  // {Alice=25, Bob=30}
    }
}
```

---

## Collecting to Maps with Streams

### Basic Collectors.toMap()

```java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamToMap {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        
        // Name -> length
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(
                name -> name,           // Key mapper
                name -> name.length()   // Value mapper
            ));
        System.out.println(nameLengths);  // {Alice=5, Bob=3, Charlie=7}
        
        // Using method references
        Map<String, Integer> nameLengths2 = names.stream()
            .collect(Collectors.toMap(
                String::toString,       // Key: the name itself
                String::length          // Value: its length
            ));
    }
}
```

### Handling Duplicate Keys

```java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Person {
    String name;
    int age;
    String city;
    
    Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
}

public class DuplicateKeys {
    public static void main(String[] args) {
        List<Person> people = List.of(
            new Person("Alice", 25, "NYC"),
            new Person("Bob", 30, "LA"),
            new Person("Charlie", 35, "NYC"),  // Same city as Alice
            new Person("David", 40, "LA")      // Same city as Bob
        );
        
        // Without merge function: throws exception on duplicate keys
        // Map<String, String> cityToName = people.stream()
        //     .collect(Collectors.toMap(p -> p.city, p -> p.name));  // Exception!
        
        // With merge function: handle duplicates
        Map<String, String> cityToNames = people.stream()
            .collect(Collectors.toMap(
                p -> p.city,
                p -> p.name,
                (existing, replacement) -> existing + ", " + replacement
            ));
        System.out.println(cityToNames);  // {NYC=Alice, Charlie, LA=Bob, David}
        
        // Keep first occurrence
        Map<String, String> firstByCity = people.stream()
            .collect(Collectors.toMap(
                p -> p.city,
                p -> p.name,
                (first, second) -> first
            ));
        System.out.println(firstByCity);  // {NYC=Alice, LA=Bob}
        
        // Keep last occurrence
        Map<String, String> lastByCity = people.stream()
            .collect(Collectors.toMap(
                p -> p.city,
                p -> p.name,
                (first, second) -> second
            ));
        System.out.println(lastByCity);  // {NYC=Charlie, LA=David}
    }
}
```

### Collecting to Specific Map Type

```java
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class SpecificMapType {
    public static void main(String[] args) {
        List<String> names = List.of("Charlie", "Alice", "Bob");
        
        // Collect to TreeMap (sorted by key)
        Map<String, Integer> sorted = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length,
                (a, b) -> a,        // Merge function (not used here)
                TreeMap::new        // Map supplier
            ));
        System.out.println("Sorted: " + sorted);  // {Alice=5, Bob=3, Charlie=7}
        
        // Collect to LinkedHashMap (preserve stream order)
        Map<String, Integer> ordered = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length,
                (a, b) -> a,
                LinkedHashMap::new
            ));
        System.out.println("Ordered: " + ordered);  // {Charlie=7, Alice=5, Bob=3}
    }
}
```

### Grouping By

```java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingBy {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Anna", "Charlie", "Ben", "Catherine");
        
        // Group by first letter
        Map<Character, List<String>> byFirstLetter = names.stream()
            .collect(Collectors.groupingBy(name -> name.charAt(0)));
        System.out.println(byFirstLetter);
        // {A=[Alice, Anna], B=[Bob, Ben], C=[Charlie, Catherine]}
        
        // Group by length
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println(byLength);
        // {3=[Bob, Ben], 4=[Anna], 5=[Alice], 7=[Charlie], 9=[Catherine]}
        
        // Group and count
        Map<Character, Long> countByFirstLetter = names.stream()
            .collect(Collectors.groupingBy(
                name -> name.charAt(0),
                Collectors.counting()
            ));
        System.out.println(countByFirstLetter);  // {A=2, B=2, C=2}
        
        // Group and join
        Map<Character, String> joinedByLetter = names.stream()
            .collect(Collectors.groupingBy(
                name -> name.charAt(0),
                Collectors.joining(", ")
            ));
        System.out.println(joinedByLetter);
        // {A=Alice, Anna, B=Bob, Ben, C=Charlie, Catherine}
    }
}
```

### Partitioning By

```java
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitioningBy {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Partition into two groups: true and false
        Map<Boolean, List<Integer>> evenOdd = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        
        System.out.println("Even: " + evenOdd.get(true));   // [2, 4, 6, 8, 10]
        System.out.println("Odd: " + evenOdd.get(false));   // [1, 3, 5, 7, 9]
        
        // Partition and count
        Map<Boolean, Long> countEvenOdd = numbers.stream()
            .collect(Collectors.partitioningBy(
                n -> n % 2 == 0,
                Collectors.counting()
            ));
        System.out.println("Even count: " + countEvenOdd.get(true));   // 5
        System.out.println("Odd count: " + countEvenOdd.get(false));   // 5
    }
}
```

---

## Common Operations

### Getting Keys, Values, and Entries

```java
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;

public class KeysValuesEntries {
    public static void main(String[] args) {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        
        // Get all keys (Set - no duplicates)
        Set<String> keys = ages.keySet();
        System.out.println("Keys: " + keys);  // [Alice, Bob, Charlie]
        
        // Get all values (Collection - may have duplicates)
        Collection<Integer> values = ages.values();
        System.out.println("Values: " + values);  // [25, 30, 35]
        
        // Get all entries
        Set<Map.Entry<String, Integer>> entries = ages.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}
```

### Counting Occurrences

```java
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CountOccurrences {
    public static void main(String[] args) {
        List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
        
        // Count using compute
        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            counts.compute(word, (key, value) -> value == null ? 1 : value + 1);
        }
        System.out.println(counts);  // {apple=3, banana=2, cherry=1}
        
        // Count using merge (cleaner)
        counts = new HashMap<>();
        for (String word : words) {
            counts.merge(word, 1, Integer::sum);
        }
        System.out.println(counts);  // {apple=3, banana=2, cherry=1}
        
        // Count using getOrDefault
        counts = new HashMap<>();
        for (String word : words) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }
        System.out.println(counts);  // {apple=3, banana=2, cherry=1}
        
        // Count using Stream
        Map<String, Long> streamCounts = words.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                word -> word,
                java.util.stream.Collectors.counting()
            ));
        System.out.println(streamCounts);  // {apple=3, banana=2, cherry=1}
    }
}
```

### Inverting a Map

```java
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class InvertMap {
    public static void main(String[] args) {
        Map<String, Integer> nameToAge = Map.of("Alice", 25, "Bob", 30, "Charlie", 35);
        
        // Invert: age -> name (assuming unique values)
        Map<Integer, String> ageToName = nameToAge.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getValue,
                Map.Entry::getKey
            ));
        System.out.println(ageToName);  // {25=Alice, 30=Bob, 35=Charlie}
        
        // Invert with potential duplicate values
        Map<String, String> cityToCountry = Map.of(
            "NYC", "USA",
            "LA", "USA",
            "London", "UK"
        );
        
        // Country -> List of cities
        Map<String, java.util.List<String>> countryToCities = cityToCountry.entrySet().stream()
            .collect(Collectors.groupingBy(
                Map.Entry::getValue,
                Collectors.mapping(Map.Entry::getKey, Collectors.toList())
            ));
        System.out.println(countryToCities);  // {USA=[NYC, LA], UK=[London]}
    }
}
```

### Finding Max/Min by Value

```java
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;

public class MaxMinByValue {
    public static void main(String[] args) {
        Map<String, Integer> scores = Map.of(
            "Alice", 85,
            "Bob", 92,
            "Charlie", 78,
            "David", 92
        );
        
        // Find entry with max value
        Optional<Map.Entry<String, Integer>> maxEntry = scores.entrySet().stream()
            .max(Map.Entry.comparingByValue());
        maxEntry.ifPresent(e -> System.out.println("Max: " + e));  // Max: Bob=92
        
        // Find entry with min value
        Optional<Map.Entry<String, Integer>> minEntry = scores.entrySet().stream()
            .min(Map.Entry.comparingByValue());
        minEntry.ifPresent(e -> System.out.println("Min: " + e));  // Min: Charlie=78
        
        // Find all max values
        int maxScore = scores.values().stream()
            .max(Integer::compareTo)
            .orElse(0);
        
        scores.entrySet().stream()
            .filter(e -> e.getValue() == maxScore)
            .forEach(e -> System.out.println("Top scorer: " + e.getKey()));
        // Top scorer: Bob
        // Top scorer: David
    }
}
```

---

## Thread Safety

HashMap, LinkedHashMap, and TreeMap are not thread-safe.

### Synchronized Map

```java
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedMap {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> syncMap = Collections.synchronizedMap(map);
        
        // Thread-safe operations
        syncMap.put("A", 1);
        syncMap.put("B", 2);
        
        // Iteration requires manual synchronization
        synchronized (syncMap) {
            for (Map.Entry<String, Integer> entry : syncMap.entrySet()) {
                System.out.println(entry);
            }
        }
    }
}
```

### ConcurrentHashMap

ConcurrentHashMap is the recommended thread-safe Map for concurrent access:

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        
        // Thread-safe operations
        map.put("A", 1);
        map.put("B", 2);
        
        // Atomic compute operations
        map.compute("A", (key, value) -> value == null ? 1 : value + 1);
        map.computeIfAbsent("C", key -> 0);
        map.merge("A", 1, Integer::sum);
        
        // Safe iteration (no ConcurrentModificationException)
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry);
        }
        
        // Parallel operations
        map.forEach(1, (key, value) -> System.out.println(key + "=" + value));
    }
}
```

### Atomic Operations with ConcurrentHashMap

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicOperations {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
        
        // Thread-safe counter initialization and increment
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counters.computeIfAbsent("counter", k -> new AtomicInteger(0))
                        .incrementAndGet();
            }
        };
        
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Final count: " + counters.get("counter").get());  // 2000
    }
}
```

---

## equals() and hashCode() for Map Keys

Custom objects used as keys in HashMap and LinkedHashMap must properly implement equals() and hashCode().

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Employee {
    private int id;
    private String name;
    
    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;  // Employees equal if same ID
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);  // Must be consistent with equals
    }
    
    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "'}";
    }
}

public class MapWithCustomKeys {
    public static void main(String[] args) {
        Map<Employee, String> departments = new HashMap<>();
        
        departments.put(new Employee(1, "Alice"), "Engineering");
        departments.put(new Employee(2, "Bob"), "Marketing");
        departments.put(new Employee(1, "Alice Updated"), "Sales");  // Same ID
        
        System.out.println("Size: " + departments.size());  // 2 (not 3)
        
        // Lookup by ID
        Employee searchKey = new Employee(1, "Any Name");
        String dept = departments.get(searchKey);
        System.out.println("Employee 1 department: " + dept);  // Sales
    }
}
```

---

## Common Use Cases

### Configuration/Settings

```java
import java.util.Map;
import java.util.HashMap;

public class Configuration {
    private Map<String, String> settings = new HashMap<>();
    
    public void set(String key, String value) {
        settings.put(key, value);
    }
    
    public String get(String key) {
        return settings.getOrDefault(key, "");
    }
    
    public String get(String key, String defaultValue) {
        return settings.getOrDefault(key, defaultValue);
    }
    
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.set("database.host", "localhost");
        config.set("database.port", "5432");
        
        System.out.println("Host: " + config.get("database.host"));
        System.out.println("Timeout: " + config.get("database.timeout", "30"));
    }
}
```

### Caching

```java
import java.util.Map;
import java.util.HashMap;

public class SimpleCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final java.util.function.Function<K, V> loader;
    
    public SimpleCache(java.util.function.Function<K, V> loader) {
        this.loader = loader;
    }
    
    public V get(K key) {
        return cache.computeIfAbsent(key, loader);
    }
    
    public void invalidate(K key) {
        cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public static void main(String[] args) {
        // Cache expensive computations
        SimpleCache<Integer, Integer> fibCache = new SimpleCache<>(n -> {
            System.out.println("Computing fib(" + n + ")");
            // Simplified - in reality would be recursive with cache
            return n <= 1 ? n : n * 2;  // Placeholder
        });
        
        System.out.println(fibCache.get(5));  // Computing fib(5) -> 10
        System.out.println(fibCache.get(5));  // Returns cached value (no computing)
    }
}
```

### Frequency Map / Histogram

```java
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class FrequencyMap {
    public static void main(String[] args) {
        String text = "hello world hello java world world";
        String[] words = text.split(" ");
        
        // Build frequency map
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.merge(word, 1, Integer::sum);
        }
        System.out.println("Frequency: " + frequency);
        // {java=1, world=3, hello=2}
        
        // Sort by frequency (descending)
        Map<String, Integer> sorted = frequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));
        System.out.println("Sorted by frequency: " + sorted);
        // {world=3, hello=2, java=1}
    }
}
```

### Multi-value Map

```java
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MultiValueMap {
    public static void main(String[] args) {
        // Map with multiple values per key
        Map<String, List<String>> groupedData = new HashMap<>();
        
        // Add values
        groupedData.computeIfAbsent("fruits", k -> new ArrayList<>()).add("apple");
        groupedData.computeIfAbsent("fruits", k -> new ArrayList<>()).add("banana");
        groupedData.computeIfAbsent("vegetables", k -> new ArrayList<>()).add("carrot");
        groupedData.computeIfAbsent("fruits", k -> new ArrayList<>()).add("cherry");
        
        System.out.println(groupedData);
        // {fruits=[apple, banana, cherry], vegetables=[carrot]}
        
        // Get values (with empty list as default)
        List<String> fruits = groupedData.getOrDefault("fruits", List.of());
        List<String> meats = groupedData.getOrDefault("meats", List.of());
        
        System.out.println("Fruits: " + fruits);  // [apple, banana, cherry]
        System.out.println("Meats: " + meats);    // []
    }
}
```

---

## Common Mistakes and How to Avoid Them

### Mistake 1: Modifying Map During Iteration

```java
// WRONG: ConcurrentModificationException
for (String key : map.keySet()) {
    if (condition) {
        map.remove(key);  // Exception!
    }
}

// CORRECT: Use Iterator
Iterator<String> it = map.keySet().iterator();
while (it.hasNext()) {
    if (condition) {
        it.remove();
    }
}

// CORRECT: Use removeIf
map.entrySet().removeIf(entry -> condition);
```

### Mistake 2: Null Key in TreeMap

```java
TreeMap<String, Integer> treeMap = new TreeMap<>();

// WRONG: NullPointerException
// treeMap.put(null, 1);

// Use HashMap if null keys needed
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put(null, 1);  // OK
```

### Mistake 3: Mutable Keys

```java
// WRONG: Modifying key after insertion
class Person {
    String name;  // Mutable!
    // equals/hashCode use name
}

Map<Person, String> map = new HashMap<>();
Person p = new Person("Alice");
map.put(p, "Engineer");
p.name = "Bob";  // hashCode changes!
map.get(p);      // Returns null!

// CORRECT: Use immutable keys or don't modify after insertion
```

### Mistake 4: Forgetting Merge Function for Duplicates

```java
// WRONG: Throws exception on duplicate keys
// list.stream().collect(Collectors.toMap(key, value));

// CORRECT: Provide merge function
list.stream().collect(Collectors.toMap(
    keyMapper,
    valueMapper,
    (existing, replacement) -> existing  // or replacement, or combine
));
```

### Mistake 5: Assuming Map.of() is Mutable

```java
// WRONG
Map<String, Integer> map = Map.of("A", 1);
// map.put("B", 2);  // UnsupportedOperationException

// CORRECT: Wrap in mutable map
Map<String, Integer> mutable = new HashMap<>(Map.of("A", 1));
mutable.put("B", 2);  // OK
```

---

## Best Practices

### 1. Program to Interface

```java
// GOOD
Map<String, Integer> map = new HashMap<>();

// AVOID
HashMap<String, Integer> map = new HashMap<>();
```

### 2. Choose the Right Implementation

```java
// General purpose, fastest
Map<String, Integer> hashMap = new HashMap<>();

// When iteration order matters
Map<String, Integer> linkedMap = new LinkedHashMap<>();

// When sorted order needed
Map<String, Integer> treeMap = new TreeMap<>();

// For enum keys (always use for enums)
Map<Day, String> enumMap = new EnumMap<>(Day.class);

// For concurrent access
Map<String, Integer> concurrent = new ConcurrentHashMap<>();
```

### 3. Specify Initial Capacity for Large Maps

```java
// GOOD: Avoids rehashing
Map<String, Integer> large = new HashMap<>(10000);

// May cause multiple rehashes
Map<String, Integer> large = new HashMap<>();
```

### 4. Use getOrDefault() and computeIfAbsent()

```java
// GOOD: Cleaner null handling
int count = map.getOrDefault("key", 0);
List<String> list = map.computeIfAbsent("key", k -> new ArrayList<>());

// VERBOSE: Manual null checks
Integer count = map.get("key");
if (count == null) count = 0;
```

### 5. Return Unmodifiable Maps from Methods

```java
public class SafeReturn {
    private Map<String, Integer> internal = new HashMap<>();
    
    // GOOD: Return unmodifiable view
    public Map<String, Integer> getData() {
        return Collections.unmodifiableMap(internal);
    }
    
    // GOOD: Return copy
    public Map<String, Integer> getDataCopy() {
        return new HashMap<>(internal);
    }
}
```

---

## Cheat Sheet

| Operation | Code |
|-----------|------|
| Create empty map | `new HashMap<>()` |
| Create from values | `Map.of("A", 1, "B", 2)` (immutable) |
| Create mutable | `new HashMap<>(Map.of("A", 1))` |
| Put entry | `map.put(key, value)` |
| Put if absent | `map.putIfAbsent(key, value)` |
| Get value | `map.get(key)` |
| Get with default | `map.getOrDefault(key, default)` |
| Remove by key | `map.remove(key)` |
| Remove if value matches | `map.remove(key, value)` |
| Contains key | `map.containsKey(key)` |
| Contains value | `map.containsValue(value)` |
| Size | `map.size()` |
| Is empty | `map.isEmpty()` |
| Clear | `map.clear()` |
| All keys | `map.keySet()` |
| All values | `map.values()` |
| All entries | `map.entrySet()` |
| Replace value | `map.replace(key, newValue)` |
| Merge values | `map.merge(key, value, remappingFunction)` |
| Compute value | `map.compute(key, (k, v) -> newValue)` |
| Iterate | `map.forEach((k, v) -> ...)` |

---

## Summary

Maps store key-value pairs where each key maps to exactly one value. Key points:

1. **HashMap** is the default choice for most use cases (fastest)
2. **LinkedHashMap** maintains insertion order (or access order for LRU)
3. **TreeMap** maintains sorted order by key
4. **EnumMap** is extremely efficient for enum keys
5. **ConcurrentHashMap** is the recommended thread-safe implementation
6. Keys must properly implement equals() and hashCode()
7. Use getOrDefault(), computeIfAbsent(), and merge() for cleaner code
8. Views (keySet, values, entrySet) are backed by the map

---

[Back to Guide](../guide.md) | [Previous: Collections - Sets](18-collections-sets.md) | [Next: Collections - Queues](20-collections-queues.md)
