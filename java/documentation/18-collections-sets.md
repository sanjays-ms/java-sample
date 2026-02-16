# Collections: Sets

[Back to Guide](../guide.md) | [Previous: Collections - Lists](17-collections-lists.md) | [Next: Collections - Maps](19-collections-maps.md)

---

## What Is a Set?

A Set is a collection that cannot contain duplicate elements. If you try to add an element that already exists, the set ignores it. Sets model the mathematical set abstraction.

Think of a Set like a bag of unique items. You can put items in, take items out, and check if an item is in the bag, but you cannot have two identical items.

### Key Characteristics of Sets

| Characteristic | Description |
|----------------|-------------|
| No duplicates | Each element can appear only once |
| Unordered | Most sets do not maintain insertion order |
| Null handling | Depends on implementation |
| Fast lookup | Checking if element exists is typically O(1) or O(log n) |

### Set vs List

| Feature | Set | List |
|---------|-----|------|
| Duplicates | Not allowed | Allowed |
| Order | Usually unordered | Ordered by index |
| Access by index | Not supported | Supported |
| Main use case | Unique elements, membership testing | Ordered sequences |

---

## The Set Interface

The Set interface defines operations common to all set implementations.

```java
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class SetInterface {
    public static void main(String[] args) {
        // Different Set implementations
        Set<String> hashSet = new HashSet<>();        // Unordered, fastest
        Set<String> linkedSet = new LinkedHashSet<>(); // Insertion order
        Set<String> treeSet = new TreeSet<>();         // Sorted order
        
        // All support the same basic operations
        hashSet.add("Apple");
        linkedSet.add("Apple");
        treeSet.add("Apple");
    }
}
```

---

## Set Implementations Comparison

| Implementation | Order | Null | Performance | Use Case |
|----------------|-------|------|-------------|----------|
| HashSet | None | One null allowed | O(1) add/remove/contains | General purpose, fastest |
| LinkedHashSet | Insertion order | One null allowed | O(1) add/remove/contains | When order matters |
| TreeSet | Sorted (natural or comparator) | No nulls | O(log n) add/remove/contains | When sorted order needed |
| EnumSet | Natural enum order | No nulls | O(1) | Sets of enum values only |

---

## HashSet

HashSet is the most commonly used Set implementation. It uses a hash table for storage, providing constant-time performance for basic operations.

### Creating a HashSet

```java
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class HashSetCreation {
    public static void main(String[] args) {
        // Empty HashSet
        Set<String> fruits = new HashSet<>();
        
        // HashSet with initial capacity
        Set<Integer> numbers = new HashSet<>(100);
        
        // HashSet with initial capacity and load factor
        Set<String> optimized = new HashSet<>(100, 0.75f);
        
        // HashSet from another collection
        Set<String> copy = new HashSet<>(fruits);
        
        // HashSet from List (removes duplicates)
        Set<String> fromList = new HashSet<>(Arrays.asList("A", "B", "A", "C"));
        System.out.println(fromList);  // [A, B, C] - duplicates removed
        
        // Using Set.of (immutable)
        Set<String> immutable = Set.of("Apple", "Banana", "Cherry");
        
        // Mutable copy from Set.of
        Set<String> mutable = new HashSet<>(Set.of("Apple", "Banana", "Cherry"));
    }
}
```

### Adding Elements

```java
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class AddingElements {
    public static void main(String[] args) {
        Set<String> fruits = new HashSet<>();
        
        // Add single element (returns true if added, false if already exists)
        boolean added1 = fruits.add("Apple");   // true
        boolean added2 = fruits.add("Banana");  // true
        boolean added3 = fruits.add("Apple");   // false (duplicate)
        
        System.out.println("Apple added first time: " + added1);
        System.out.println("Apple added second time: " + added3);
        System.out.println(fruits);  // [Apple, Banana]
        
        // Add multiple elements from collection
        fruits.addAll(Arrays.asList("Cherry", "Date", "Apple"));
        System.out.println(fruits);  // [Apple, Banana, Cherry, Date]
        
        // Add null (allowed in HashSet)
        fruits.add(null);
        System.out.println("Contains null: " + fruits.contains(null));  // true
    }
}
```

### Checking Elements

```java
import java.util.HashSet;
import java.util.Set;

public class CheckingElements {
    public static void main(String[] args) {
        Set<String> fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry"));
        
        // Check if element exists
        boolean hasApple = fruits.contains("Apple");   // true
        boolean hasGrape = fruits.contains("Grape");   // false
        
        // Check size
        int size = fruits.size();  // 3
        
        // Check if empty
        boolean empty = fruits.isEmpty();  // false
        
        // Check if contains all elements from collection
        boolean hasAll = fruits.containsAll(Set.of("Apple", "Banana"));  // true
        boolean hasMissing = fruits.containsAll(Set.of("Apple", "Grape"));  // false
        
        System.out.println("Has Apple: " + hasApple);
        System.out.println("Size: " + size);
    }
}
```

### Removing Elements

```java
import java.util.HashSet;
import java.util.Set;

public class RemovingElements {
    public static void main(String[] args) {
        Set<String> fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry", "Date", "Elderberry"));
        
        // Remove single element (returns true if removed)
        boolean removed = fruits.remove("Apple");
        System.out.println("Removed Apple: " + removed);  // true
        System.out.println(fruits);
        
        // Remove element that doesn't exist
        boolean removedGrape = fruits.remove("Grape");
        System.out.println("Removed Grape: " + removedGrape);  // false
        
        // Remove all elements in a collection
        fruits.removeAll(Set.of("Banana", "Cherry", "Grape"));
        System.out.println("After removeAll: " + fruits);  // [Date, Elderberry]
        
        // Reset
        fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry", "Date", "Elderberry"));
        
        // Retain only elements in a collection
        fruits.retainAll(Set.of("Apple", "Cherry", "Grape"));
        System.out.println("After retainAll: " + fruits);  // [Apple, Cherry]
        
        // Remove with condition
        fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry", "Date", "Elderberry"));
        fruits.removeIf(f -> f.length() > 5);
        System.out.println("After removeIf: " + fruits);  // [Apple, Date]
        
        // Clear all elements
        fruits.clear();
        System.out.println("After clear: " + fruits);  // []
    }
}
```

---

## LinkedHashSet

LinkedHashSet maintains insertion order while still providing O(1) performance. It uses a hash table with a linked list running through all entries.

### Creating and Using LinkedHashSet

```java
import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetExample {
    public static void main(String[] args) {
        // LinkedHashSet maintains insertion order
        Set<String> fruits = new LinkedHashSet<>();
        
        fruits.add("Cherry");
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Date");
        
        // Elements are in insertion order
        System.out.println(fruits);  // [Cherry, Apple, Banana, Date]
        
        // Adding duplicate doesn't change order
        fruits.add("Apple");
        System.out.println(fruits);  // [Cherry, Apple, Banana, Date]
        
        // Compare with HashSet (unpredictable order)
        Set<String> hashSet = new java.util.HashSet<>();
        hashSet.add("Cherry");
        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Date");
        System.out.println("HashSet: " + hashSet);  // Order varies
    }
}
```

### When to Use LinkedHashSet

```java
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class LinkedHashSetUseCases {
    public static void main(String[] args) {
        // Use case 1: Remove duplicates while preserving order
        List<String> listWithDuplicates = List.of("B", "A", "C", "A", "B", "D");
        Set<String> uniqueOrdered = new LinkedHashSet<>(listWithDuplicates);
        System.out.println(uniqueOrdered);  // [B, A, C, D]
        
        // Use case 2: Track unique visitors in order
        Set<String> visitors = new LinkedHashSet<>();
        visitors.add("Alice");
        visitors.add("Bob");
        visitors.add("Alice");  // Already visited
        visitors.add("Charlie");
        System.out.println("Visitors in order: " + visitors);  // [Alice, Bob, Charlie]
        
        // Use case 3: LRU-like behavior (simple)
        Set<String> recentItems = new LinkedHashSet<>();
        String[] accesses = {"page1", "page2", "page1", "page3", "page2"};
        for (String page : accesses) {
            recentItems.remove(page);  // Remove if exists
            recentItems.add(page);     // Add to end
        }
        System.out.println("Recent order: " + recentItems);  // [page1, page3, page2]
    }
}
```

---

## TreeSet

TreeSet stores elements in sorted order using a Red-Black tree. Elements must be comparable or a comparator must be provided.

### Creating a TreeSet

```java
import java.util.TreeSet;
import java.util.Set;
import java.util.Comparator;

public class TreeSetCreation {
    public static void main(String[] args) {
        // Natural ordering (elements must implement Comparable)
        Set<String> names = new TreeSet<>();
        names.add("Charlie");
        names.add("Alice");
        names.add("Bob");
        System.out.println(names);  // [Alice, Bob, Charlie] - sorted!
        
        // With custom comparator
        Set<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.add("Apple");
        byLength.add("Fig");
        byLength.add("Banana");
        byLength.add("Kiwi");  // Same length as "Fig" - won't be added!
        System.out.println(byLength);  // [Fig, Apple, Banana]
        
        // Reverse order
        Set<Integer> descending = new TreeSet<>(Comparator.reverseOrder());
        descending.add(3);
        descending.add(1);
        descending.add(2);
        System.out.println(descending);  // [3, 2, 1]
        
        // Case-insensitive strings
        Set<String> caseInsensitive = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitive.add("Apple");
        caseInsensitive.add("apple");  // Duplicate (case-insensitive)
        caseInsensitive.add("BANANA");
        System.out.println(caseInsensitive);  // [Apple, BANANA]
    }
}
```

### TreeSet Navigation Methods

TreeSet provides methods to navigate through the sorted elements:

```java
import java.util.TreeSet;
import java.util.NavigableSet;

public class TreeSetNavigation {
    public static void main(String[] args) {
        TreeSet<Integer> numbers = new TreeSet<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        numbers.add(50);
        
        // First and last
        System.out.println("First: " + numbers.first());  // 10
        System.out.println("Last: " + numbers.last());    // 50
        
        // Lower (strictly less than)
        System.out.println("Lower than 30: " + numbers.lower(30));  // 20
        System.out.println("Lower than 25: " + numbers.lower(25));  // 20
        
        // Floor (less than or equal)
        System.out.println("Floor of 30: " + numbers.floor(30));    // 30
        System.out.println("Floor of 25: " + numbers.floor(25));    // 20
        
        // Higher (strictly greater than)
        System.out.println("Higher than 30: " + numbers.higher(30)); // 40
        System.out.println("Higher than 25: " + numbers.higher(25)); // 30
        
        // Ceiling (greater than or equal)
        System.out.println("Ceiling of 30: " + numbers.ceiling(30)); // 30
        System.out.println("Ceiling of 25: " + numbers.ceiling(25)); // 30
        
        // Poll (remove and return)
        System.out.println("Poll first: " + numbers.pollFirst());  // 10 (removed)
        System.out.println("Poll last: " + numbers.pollLast());    // 50 (removed)
        System.out.println("Remaining: " + numbers);  // [20, 30, 40]
    }
}
```

### TreeSet Range Views

```java
import java.util.TreeSet;
import java.util.NavigableSet;
import java.util.SortedSet;

public class TreeSetRanges {
    public static void main(String[] args) {
        TreeSet<Integer> numbers = new TreeSet<>();
        for (int i = 10; i <= 100; i += 10) {
            numbers.add(i);  // 10, 20, 30, ..., 100
        }
        
        // Subset (from inclusive, to exclusive by default)
        SortedSet<Integer> subset = numbers.subSet(30, 70);
        System.out.println("subSet(30, 70): " + subset);  // [30, 40, 50, 60]
        
        // Subset with inclusive/exclusive control
        NavigableSet<Integer> navSubset = numbers.subSet(30, true, 70, true);
        System.out.println("subSet(30, true, 70, true): " + navSubset);  // [30, 40, 50, 60, 70]
        
        // Head set (less than)
        SortedSet<Integer> head = numbers.headSet(50);
        System.out.println("headSet(50): " + head);  // [10, 20, 30, 40]
        
        // Head set (less than or equal)
        NavigableSet<Integer> headInclusive = numbers.headSet(50, true);
        System.out.println("headSet(50, true): " + headInclusive);  // [10, 20, 30, 40, 50]
        
        // Tail set (greater than or equal)
        SortedSet<Integer> tail = numbers.tailSet(50);
        System.out.println("tailSet(50): " + tail);  // [50, 60, 70, 80, 90, 100]
        
        // Tail set (greater than)
        NavigableSet<Integer> tailExclusive = numbers.tailSet(50, false);
        System.out.println("tailSet(50, false): " + tailExclusive);  // [60, 70, 80, 90, 100]
        
        // Descending set (view in reverse order)
        NavigableSet<Integer> descending = numbers.descendingSet();
        System.out.println("Descending: " + descending);  // [100, 90, 80, ..., 10]
        
        // Views are backed by original set!
        numbers.add(55);
        System.out.println("Subset after adding 55: " + subset);  // [30, 40, 50, 55, 60]
    }
}
```

### TreeSet with Custom Objects

```java
import java.util.TreeSet;
import java.util.Set;
import java.util.Comparator;

class Person implements Comparable<Person> {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);  // Natural order by name
    }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

public class TreeSetCustomObjects {
    public static void main(String[] args) {
        // Using natural order (Comparable)
        Set<Person> byName = new TreeSet<>();
        byName.add(new Person("Charlie", 25));
        byName.add(new Person("Alice", 30));
        byName.add(new Person("Bob", 20));
        System.out.println("By name: " + byName);
        // [Alice(30), Bob(20), Charlie(25)]
        
        // Using custom comparator (by age)
        Set<Person> byAge = new TreeSet<>(Comparator.comparingInt(Person::getAge));
        byAge.add(new Person("Charlie", 25));
        byAge.add(new Person("Alice", 30));
        byAge.add(new Person("Bob", 20));
        System.out.println("By age: " + byAge);
        // [Bob(20), Charlie(25), Alice(30)]
        
        // Multiple criteria (age, then name)
        Set<Person> byAgeThenName = new TreeSet<>(
            Comparator.comparingInt(Person::getAge)
                      .thenComparing(Person::getName)
        );
        byAgeThenName.add(new Person("Charlie", 25));
        byAgeThenName.add(new Person("Alice", 25));
        byAgeThenName.add(new Person("Bob", 25));
        System.out.println("By age then name: " + byAgeThenName);
        // [Alice(25), Bob(25), Charlie(25)]
    }
}
```

---

## EnumSet

EnumSet is a specialized Set implementation for enum values. It is extremely efficient and should always be used for sets of enums.

```java
import java.util.EnumSet;
import java.util.Set;

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

public class EnumSetExample {
    public static void main(String[] args) {
        // Create from specific values
        Set<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        System.out.println("Weekend: " + weekend);
        
        // All values
        Set<Day> allDays = EnumSet.allOf(Day.class);
        System.out.println("All days: " + allDays);
        
        // Empty set
        Set<Day> none = EnumSet.noneOf(Day.class);
        none.add(Day.MONDAY);
        System.out.println("None then add: " + none);
        
        // Range (inclusive)
        Set<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
        System.out.println("Weekdays: " + weekdays);
        
        // Complement (all except specified)
        Set<Day> notWeekend = EnumSet.complementOf(EnumSet.of(Day.SATURDAY, Day.SUNDAY));
        System.out.println("Not weekend: " + notWeekend);
        
        // Copy
        Set<Day> copy = EnumSet.copyOf(weekend);
        System.out.println("Copy: " + copy);
        
        // Check membership
        Day today = Day.MONDAY;
        if (weekdays.contains(today)) {
            System.out.println(today + " is a weekday");
        }
    }
}
```

### EnumSet Performance

EnumSet is implemented as a bit vector, making it extremely efficient:

```java
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

enum Permission {
    READ, WRITE, EXECUTE, DELETE, CREATE, MODIFY, ADMIN
}

public class EnumSetPerformance {
    public static void main(String[] args) {
        // EnumSet operations are O(1) and use minimal memory
        Set<Permission> userPermissions = EnumSet.of(
            Permission.READ, 
            Permission.WRITE
        );
        
        Set<Permission> adminPermissions = EnumSet.of(
            Permission.READ, 
            Permission.WRITE, 
            Permission.DELETE, 
            Permission.ADMIN
        );
        
        // Fast set operations
        Set<Permission> commonPermissions = EnumSet.copyOf(userPermissions);
        commonPermissions.retainAll(adminPermissions);  // Intersection
        System.out.println("Common: " + commonPermissions);
        
        // Check if user has all required permissions
        Set<Permission> required = EnumSet.of(Permission.READ, Permission.WRITE);
        boolean hasRequired = userPermissions.containsAll(required);
        System.out.println("Has required: " + hasRequired);
        
        // Much more efficient than HashSet<Permission>
        // EnumSet uses ~1 bit per enum value
        // HashSet uses ~32 bytes per entry
    }
}
```

---

## Iterating Over Sets

### Enhanced For Loop

```java
import java.util.Set;

public class SetIteration {
    public static void main(String[] args) {
        Set<String> fruits = Set.of("Apple", "Banana", "Cherry");
        
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
    }
}
```

### forEach with Lambda

```java
import java.util.Set;

public class SetForEach {
    public static void main(String[] args) {
        Set<String> fruits = Set.of("Apple", "Banana", "Cherry");
        
        fruits.forEach(fruit -> System.out.println(fruit));
        
        // Method reference
        fruits.forEach(System.out::println);
    }
}
```

### Iterator

```java
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetIterator {
    public static void main(String[] args) {
        Set<String> fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry", "Date"));
        
        // Using iterator for safe removal
        Iterator<String> iterator = fruits.iterator();
        while (iterator.hasNext()) {
            String fruit = iterator.next();
            if (fruit.startsWith("B")) {
                iterator.remove();  // Safe removal
            }
        }
        System.out.println(fruits);  // [Apple, Cherry, Date]
        
        // Or use removeIf (simpler)
        fruits = new HashSet<>(Set.of("Apple", "Banana", "Cherry", "Date"));
        fruits.removeIf(f -> f.startsWith("C"));
        System.out.println(fruits);  // [Apple, Banana, Date]
    }
}
```

---

## Set Operations (Mathematical)

Sets support mathematical set operations: union, intersection, and difference.

### Union (All Elements from Both Sets)

```java
import java.util.HashSet;
import java.util.Set;

public class SetUnion {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Set.of(4, 5, 6, 7, 8));
        
        // Union: combine all elements
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);  // [1, 2, 3, 4, 5, 6, 7, 8]
        
        // Original sets unchanged
        System.out.println("Set1: " + set1);  // [1, 2, 3, 4, 5]
        System.out.println("Set2: " + set2);  // [4, 5, 6, 7, 8]
    }
}
```

### Intersection (Common Elements)

```java
import java.util.HashSet;
import java.util.Set;

public class SetIntersection {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Set.of(4, 5, 6, 7, 8));
        
        // Intersection: keep only common elements
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);  // [4, 5]
    }
}
```

### Difference (Elements in First but Not Second)

```java
import java.util.HashSet;
import java.util.Set;

public class SetDifference {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Set.of(4, 5, 6, 7, 8));
        
        // Difference: elements in set1 but not in set2
        Set<Integer> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);  // [1, 2, 3]
        
        // Reverse difference
        Set<Integer> reverseDiff = new HashSet<>(set2);
        reverseDiff.removeAll(set1);
        System.out.println("Difference (set2 - set1): " + reverseDiff);  // [6, 7, 8]
    }
}
```

### Symmetric Difference (Elements in Either but Not Both)

```java
import java.util.HashSet;
import java.util.Set;

public class SetSymmetricDifference {
    public static void main(String[] args) {
        Set<Integer> set1 = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> set2 = new HashSet<>(Set.of(4, 5, 6, 7, 8));
        
        // Symmetric difference: elements in either set but not both
        Set<Integer> symmetricDiff = new HashSet<>(set1);
        symmetricDiff.addAll(set2);  // Union
        
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);  // Intersection
        
        symmetricDiff.removeAll(intersection);  // Remove common elements
        System.out.println("Symmetric difference: " + symmetricDiff);  // [1, 2, 3, 6, 7, 8]
    }
}
```

### Subset and Superset

```java
import java.util.Set;

public class SubsetSuperset {
    public static void main(String[] args) {
        Set<Integer> set1 = Set.of(1, 2, 3);
        Set<Integer> set2 = Set.of(1, 2, 3, 4, 5);
        Set<Integer> set3 = Set.of(1, 2, 3);
        
        // Check if subset
        boolean isSubset = set2.containsAll(set1);  // set1 is subset of set2
        System.out.println("set1 subset of set2: " + isSubset);  // true
        
        // Check if superset
        boolean isSuperset = set2.containsAll(set1);  // set2 is superset of set1
        System.out.println("set2 superset of set1: " + isSuperset);  // true
        
        // Check equality
        boolean equal = set1.equals(set3);
        System.out.println("set1 equals set3: " + equal);  // true
        
        // Disjoint (no common elements)
        Set<Integer> set4 = Set.of(6, 7, 8);
        boolean disjoint = java.util.Collections.disjoint(set1, set4);
        System.out.println("set1 and set4 disjoint: " + disjoint);  // true
    }
}
```

---

## Immutable Sets

### Set.of() (Java 9+)

```java
import java.util.Set;

public class ImmutableSets {
    public static void main(String[] args) {
        // Create immutable set
        Set<String> fruits = Set.of("Apple", "Banana", "Cherry");
        
        // Reading works
        System.out.println(fruits.contains("Apple"));  // true
        System.out.println(fruits.size());  // 3
        
        // Modification throws exception
        try {
            fruits.add("Date");  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutable set");
        }
        
        // No null allowed
        try {
            Set<String> withNull = Set.of("A", null, "B");  // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("Set.of does not allow null");
        }
        
        // No duplicates allowed
        try {
            Set<String> withDup = Set.of("A", "B", "A");  // IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Set.of does not allow duplicates");
        }
    }
}
```

### Set.copyOf() (Java 10+)

```java
import java.util.Set;
import java.util.HashSet;

public class SetCopyOf {
    public static void main(String[] args) {
        Set<String> mutable = new HashSet<>();
        mutable.add("Apple");
        mutable.add("Banana");
        
        // Create immutable copy
        Set<String> immutable = Set.copyOf(mutable);
        
        // Modifying original doesn't affect copy
        mutable.add("Cherry");
        System.out.println(mutable);    // [Apple, Banana, Cherry]
        System.out.println(immutable);  // [Apple, Banana]
    }
}
```

---

## Converting Between Set and Other Collections

### Set to List

```java
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class SetToList {
    public static void main(String[] args) {
        Set<String> set = Set.of("Apple", "Banana", "Cherry");
        
        // Using ArrayList constructor
        List<String> list1 = new ArrayList<>(set);
        
        // Using Stream (Java 16+)
        List<String> list2 = set.stream().toList();
        
        // Using Stream (older Java)
        List<String> list3 = set.stream().collect(Collectors.toList());
        
        System.out.println(list1);
    }
}
```

### List to Set (Remove Duplicates)

```java
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListToSet {
    public static void main(String[] args) {
        List<String> list = List.of("A", "B", "A", "C", "B", "D");
        
        // HashSet (unordered)
        Set<String> hashSet = new HashSet<>(list);
        System.out.println(hashSet);  // Order varies
        
        // LinkedHashSet (preserves first occurrence order)
        Set<String> linkedSet = new LinkedHashSet<>(list);
        System.out.println(linkedSet);  // [A, B, C, D]
        
        // Using Stream
        Set<String> streamSet = list.stream().collect(Collectors.toSet());
        
        // Using Stream to LinkedHashSet
        Set<String> orderedSet = list.stream()
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
```

### Set to Array

```java
import java.util.Set;

public class SetToArray {
    public static void main(String[] args) {
        Set<String> set = Set.of("Apple", "Banana", "Cherry");
        
        // To Object array
        Object[] objArray = set.toArray();
        
        // To typed array
        String[] strArray = set.toArray(new String[0]);
        
        // Java 11+
        String[] strArray2 = set.toArray(String[]::new);
        
        for (String s : strArray) {
            System.out.println(s);
        }
    }
}
```

---

## Sets with Streams

```java
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class SetStreams {
    public static void main(String[] args) {
        Set<String> names = Set.of("Alice", "Bob", "Charlie", "David", "Eve");
        
        // Filter
        Set<String> longNames = names.stream()
            .filter(name -> name.length() > 4)
            .collect(Collectors.toSet());
        System.out.println(longNames);  // [Alice, Charlie, David]
        
        // Map to different type
        Set<Integer> lengths = names.stream()
            .map(String::length)
            .collect(Collectors.toSet());
        System.out.println(lengths);  // [3, 5, 7]
        
        // Filter and transform
        Set<String> result = names.stream()
            .filter(name -> name.startsWith("A") || name.startsWith("B"))
            .map(String::toUpperCase)
            .collect(Collectors.toSet());
        System.out.println(result);  // [ALICE, BOB]
        
        // Collect to specific Set type
        Set<String> treeSet = names.stream()
            .filter(n -> n.length() > 3)
            .collect(Collectors.toCollection(java.util.TreeSet::new));
        System.out.println(treeSet);  // [Alice, Charlie, David] - sorted!
        
        // Check conditions
        boolean anyStartsWithA = names.stream().anyMatch(n -> n.startsWith("A"));
        boolean allLong = names.stream().allMatch(n -> n.length() > 2);
        System.out.println("Any starts with A: " + anyStartsWithA);  // true
        System.out.println("All length > 2: " + allLong);  // true
    }
}
```

---

## equals() and hashCode() for Set Elements

For custom objects to work correctly in HashSet and LinkedHashSet, they must properly implement equals() and hashCode().

### Why equals() and hashCode() Matter

```java
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class Product {
    private String id;
    private String name;
    
    public Product(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    
    // Without equals/hashCode, each object is unique
    // @Override equals and hashCode below to fix this
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);  // Products equal if same ID
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);  // Must be consistent with equals
    }
    
    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "'}";
    }
}

public class EqualsHashCode {
    public static void main(String[] args) {
        Set<Product> products = new HashSet<>();
        
        products.add(new Product("P001", "Laptop"));
        products.add(new Product("P002", "Phone"));
        products.add(new Product("P001", "Laptop Updated"));  // Same ID
        
        System.out.println("Size: " + products.size());  // 2 (not 3)
        
        // Check contains
        Product searchProduct = new Product("P001", "Whatever");
        System.out.println("Contains P001: " + products.contains(searchProduct));  // true
    }
}
```

### The hashCode Contract

```java
// Rules for hashCode:
// 1. Same object must always return same hashCode
// 2. If equals() returns true, hashCode must be same
// 3. If hashCode differs, equals() must return false
// 4. Different objects CAN have same hashCode (collision)

import java.util.Objects;

class Employee {
    private int id;
    private String name;
    private String department;
    
    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }
    
    // Good: uses same fields as equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && 
               Objects.equals(name, employee.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);  // Same fields as equals
    }
}
```

---

## Thread Safety

HashSet, LinkedHashSet, and TreeSet are not thread-safe. Use synchronized wrappers or concurrent alternatives.

### Synchronized Set

```java
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SynchronizedSet {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        Set<String> syncSet = Collections.synchronizedSet(set);
        
        // Thread-safe operations
        syncSet.add("A");
        syncSet.add("B");
        
        // Iteration requires manual synchronization
        synchronized (syncSet) {
            for (String s : syncSet) {
                System.out.println(s);
            }
        }
    }
}
```

### ConcurrentHashMap.newKeySet() (Java 8+)

```java
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentSet {
    public static void main(String[] args) {
        // Thread-safe set backed by ConcurrentHashMap
        Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
        
        concurrentSet.add("A");
        concurrentSet.add("B");
        
        // Safe to iterate without synchronization
        for (String s : concurrentSet) {
            System.out.println(s);
        }
    }
}
```

### CopyOnWriteArraySet

For scenarios with many reads and few writes:

```java
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteSetExample {
    public static void main(String[] args) {
        Set<String> cowSet = new CopyOnWriteArraySet<>();
        
        cowSet.add("A");
        cowSet.add("B");
        cowSet.add("C");
        
        // Safe to iterate and modify (iterator sees snapshot)
        for (String s : cowSet) {
            System.out.println(s);
            cowSet.add("D");  // Won't appear in this iteration
        }
        
        System.out.println("After loop: " + cowSet);
    }
}
```

---

## Common Use Cases

### Removing Duplicates from a Collection

```java
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<String> withDuplicates = List.of("A", "B", "A", "C", "B", "D", "A");
        
        // Preserve order
        List<String> unique = new ArrayList<>(new LinkedHashSet<>(withDuplicates));
        System.out.println(unique);  // [A, B, C, D]
    }
}
```

### Finding Common Elements

```java
import java.util.Set;
import java.util.HashSet;

public class FindCommon {
    public static void main(String[] args) {
        Set<String> group1Skills = Set.of("Java", "Python", "SQL");
        Set<String> group2Skills = Set.of("Python", "JavaScript", "SQL");
        
        Set<String> commonSkills = new HashSet<>(group1Skills);
        commonSkills.retainAll(group2Skills);
        
        System.out.println("Common skills: " + commonSkills);  // [Python, SQL]
    }
}
```

### Tracking Unique Visitors

```java
import java.util.Set;
import java.util.HashSet;

public class UniqueVisitors {
    private Set<String> visitors = new HashSet<>();
    
    public void recordVisit(String visitorId) {
        visitors.add(visitorId);
    }
    
    public int getUniqueVisitorCount() {
        return visitors.size();
    }
    
    public boolean hasVisited(String visitorId) {
        return visitors.contains(visitorId);
    }
    
    public static void main(String[] args) {
        UniqueVisitors tracker = new UniqueVisitors();
        
        tracker.recordVisit("user1");
        tracker.recordVisit("user2");
        tracker.recordVisit("user1");  // Duplicate
        tracker.recordVisit("user3");
        tracker.recordVisit("user2");  // Duplicate
        
        System.out.println("Unique visitors: " + tracker.getUniqueVisitorCount());  // 3
    }
}
```

### Checking Permissions

```java
import java.util.Set;
import java.util.EnumSet;

enum Permission {
    READ, WRITE, DELETE, ADMIN
}

public class PermissionCheck {
    public static void main(String[] args) {
        Set<Permission> userPermissions = EnumSet.of(Permission.READ, Permission.WRITE);
        Set<Permission> requiredPermissions = EnumSet.of(Permission.READ, Permission.DELETE);
        
        // Check if user has all required permissions
        boolean hasAllRequired = userPermissions.containsAll(requiredPermissions);
        System.out.println("Has all required: " + hasAllRequired);  // false
        
        // Find missing permissions
        Set<Permission> missing = EnumSet.copyOf(requiredPermissions);
        missing.removeAll(userPermissions);
        System.out.println("Missing: " + missing);  // [DELETE]
        
        // Check if has any required
        Set<Permission> intersection = EnumSet.copyOf(requiredPermissions);
        intersection.retainAll(userPermissions);
        boolean hasAny = !intersection.isEmpty();
        System.out.println("Has any required: " + hasAny);  // true
    }
}
```

---

## Common Mistakes and How to Avoid Them

### Mistake 1: Modifying Set During Iteration

```java
// WRONG
for (String item : set) {
    if (condition) {
        set.remove(item);  // ConcurrentModificationException
    }
}

// CORRECT: Use Iterator
Iterator<String> it = set.iterator();
while (it.hasNext()) {
    if (condition) {
        it.remove();
    }
}

// CORRECT: Use removeIf
set.removeIf(item -> condition);
```

### Mistake 2: Null in TreeSet

```java
TreeSet<String> treeSet = new TreeSet<>();

// WRONG: TreeSet doesn't allow null
// treeSet.add(null);  // NullPointerException

// Use HashSet or LinkedHashSet if null needed
Set<String> hashSet = new HashSet<>();
hashSet.add(null);  // OK
```

### Mistake 3: Mutable Objects as Set Elements

```java
// WRONG: Modifying element after adding
class Person {
    private String name;
    public Person(String name) { this.name = name; }
    public void setName(String name) { this.name = name; }
    // hashCode uses name
}

Set<Person> people = new HashSet<>();
Person p = new Person("Alice");
people.add(p);
p.setName("Bob");  // hashCode changes!
people.contains(p);  // May return false!

// CORRECT: Use immutable objects or don't modify after adding
```

### Mistake 4: Comparator Inconsistent with Equals (TreeSet)

```java
// WRONG: Comparator considers some different objects "equal"
Set<String> set = new TreeSet<>(Comparator.comparingInt(String::length));
set.add("Apple");
set.add("Grape");  // Same length as Apple - won't be added!
System.out.println(set.size());  // 1 (only Apple)

// CORRECT: Make comparator fully distinguish elements
Set<String> set2 = new TreeSet<>(
    Comparator.comparingInt(String::length)
              .thenComparing(Comparator.naturalOrder())
);
set2.add("Apple");
set2.add("Grape");
System.out.println(set2.size());  // 2
```

### Mistake 5: Assuming Set.of() is Mutable

```java
// WRONG
Set<String> set = Set.of("A", "B");
// set.add("C");  // UnsupportedOperationException

// CORRECT: Wrap in mutable set
Set<String> mutableSet = new HashSet<>(Set.of("A", "B"));
mutableSet.add("C");  // OK
```

---

## Best Practices

### 1. Program to Interface

```java
// GOOD
Set<String> names = new HashSet<>();

// AVOID
HashSet<String> names = new HashSet<>();
```

### 2. Choose the Right Implementation

```java
// General purpose, fastest
Set<String> hashSet = new HashSet<>();

// When insertion order matters
Set<String> linkedSet = new LinkedHashSet<>();

// When sorted order needed
Set<String> treeSet = new TreeSet<>();

// For enum values (always use this for enums!)
Set<Day> days = EnumSet.of(Day.MONDAY, Day.TUESDAY);
```

### 3. Specify Initial Capacity for Large Sets

```java
// GOOD: Avoids rehashing
Set<String> largeSet = new HashSet<>(10000);

// May cause multiple rehashes
Set<String> largeSet = new HashSet<>();
```

### 4. Return Unmodifiable Sets from Methods

```java
public class SafeReturn {
    private Set<String> internalSet = new HashSet<>();
    
    // GOOD: Return unmodifiable view
    public Set<String> getItems() {
        return Collections.unmodifiableSet(internalSet);
    }
    
    // GOOD: Return copy
    public Set<String> getItemsCopy() {
        return new HashSet<>(internalSet);
    }
}
```

### 5. Use EnumSet for Enum Values

```java
// GOOD: EnumSet is optimized for enums
Set<Permission> permissions = EnumSet.of(Permission.READ, Permission.WRITE);

// BAD: HashSet works but is slower and uses more memory
Set<Permission> permissions = new HashSet<>();
```

---

## Cheat Sheet

| Operation | Code |
|-----------|------|
| Create empty set | `new HashSet<>()` |
| Create from values | `Set.of("A", "B", "C")` (immutable) |
| Create mutable | `new HashSet<>(Set.of("A", "B"))` |
| Add element | `set.add("X")` |
| Remove element | `set.remove("X")` |
| Check contains | `set.contains("X")` |
| Size | `set.size()` |
| Is empty | `set.isEmpty()` |
| Clear | `set.clear()` |
| Union | `set1.addAll(set2)` (modifies set1) |
| Intersection | `set1.retainAll(set2)` (modifies set1) |
| Difference | `set1.removeAll(set2)` (modifies set1) |
| Subset check | `set2.containsAll(set1)` |
| Remove if | `set.removeIf(x -> condition)` |
| To list | `new ArrayList<>(set)` |
| To array | `set.toArray(new String[0])` |

---

## Summary

Sets are collections that store unique elements. Key points to remember:

1. **HashSet** is the default choice for most use cases (fastest)
2. **LinkedHashSet** maintains insertion order
3. **TreeSet** maintains sorted order (elements must be Comparable or use Comparator)
4. **EnumSet** is extremely efficient for enum values
5. Sets do not support access by index (no get(i))
6. Mathematical set operations: union (addAll), intersection (retainAll), difference (removeAll)
7. Custom objects need proper equals() and hashCode()
8. For thread safety, use synchronized wrappers or concurrent alternatives

---

[Back to Guide](../guide.md) | [Previous: Collections - Lists](17-collections-lists.md) | [Next: Collections - Maps](19-collections-maps.md)
