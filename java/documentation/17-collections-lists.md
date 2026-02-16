# Collections: Lists

[Back to Guide](../guide.md) | [Previous: Exception Handling](16-exception-handling.md) | [Next: Collections - Sets](18-collections-sets.md)

---

## What Are Collections?

A collection is a container that holds multiple elements together as a single unit. Think of it like a basket that can hold many apples, or a folder that contains many documents.

In Java, the Collections Framework is a set of interfaces and classes that provide different ways to store, retrieve, and manipulate groups of objects. Different collection types are optimized for different purposes.

### The Collection Hierarchy

```
                    Iterable
                       |
                   Collection
                   /    |    \
                List   Set   Queue
               /   \
        ArrayList  LinkedList
```

This document focuses on Lists, the most commonly used collection type.

---

## What Is a List?

A List is an ordered collection that allows duplicate elements. Each element has a position (index), and you can access elements by their position.

### Key Characteristics of Lists

| Characteristic | Description |
|----------------|-------------|
| Ordered | Elements maintain their insertion order |
| Indexed | Elements can be accessed by position (0, 1, 2, ...) |
| Allows duplicates | Same element can appear multiple times |
| Dynamic size | Grows and shrinks automatically |

### List vs Array

| Feature | Array | List |
|---------|-------|------|
| Size | Fixed at creation | Dynamic (grows/shrinks) |
| Type | Can hold primitives or objects | Objects only (uses wrappers for primitives) |
| Methods | Limited (just length) | Rich API (add, remove, search, sort) |
| Syntax | `array[i]` | `list.get(i)` |

---

## The List Interface

The List interface defines what operations all lists must support. The two main implementations are ArrayList and LinkedList.

```java
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListBasics {
    public static void main(String[] args) {
        // Create lists using interface type (recommended)
        List<String> arrayList = new ArrayList<>();
        List<String> linkedList = new LinkedList<>();
        
        // Both support the same operations
        arrayList.add("Apple");
        linkedList.add("Apple");
    }
}
```

---

## ArrayList

ArrayList is the most commonly used List implementation. It uses an internal array that grows automatically when needed.

### Creating an ArrayList

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ArrayListCreation {
    public static void main(String[] args) {
        // Empty ArrayList
        List<String> names = new ArrayList<>();
        
        // ArrayList with initial capacity (optimization)
        List<Integer> numbers = new ArrayList<>(100);
        
        // ArrayList from another collection
        List<String> copy = new ArrayList<>(names);
        
        // ArrayList from Arrays.asList
        List<String> fruits = new ArrayList<>(Arrays.asList("Apple", "Banana", "Cherry"));
        
        // Using List.of (immutable) then copying to ArrayList (mutable)
        List<String> colors = new ArrayList<>(List.of("Red", "Green", "Blue"));
        
        // Using var (Java 10+)
        var cities = new ArrayList<String>();
    }
}
```

### Adding Elements

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class AddingElements {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>();
        
        // Add single element at the end
        fruits.add("Apple");      // [Apple]
        fruits.add("Banana");     // [Apple, Banana]
        fruits.add("Cherry");     // [Apple, Banana, Cherry]
        
        // Add at specific index (shifts existing elements right)
        fruits.add(1, "Apricot"); // [Apple, Apricot, Banana, Cherry]
        
        // Add multiple elements from another collection
        List<String> moreFruits = Arrays.asList("Date", "Elderberry");
        fruits.addAll(moreFruits); // [Apple, Apricot, Banana, Cherry, Date, Elderberry]
        
        // Add multiple elements at specific index
        fruits.addAll(2, Arrays.asList("Blueberry", "Blackberry"));
        // [Apple, Apricot, Blueberry, Blackberry, Banana, Cherry, Date, Elderberry]
        
        System.out.println(fruits);
    }
}
```

### Accessing Elements

```java
import java.util.ArrayList;
import java.util.List;

public class AccessingElements {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        
        // Get element by index
        String first = fruits.get(0);    // Apple
        String second = fruits.get(1);   // Banana
        String last = fruits.get(fruits.size() - 1);  // Date
        
        // Get first and last (Java 21+)
        String firstElement = fruits.getFirst();  // Apple
        String lastElement = fruits.getLast();    // Date
        
        // Check if element exists
        boolean hasApple = fruits.contains("Apple");     // true
        boolean hasGrape = fruits.contains("Grape");     // false
        
        // Find index of element
        int bananaIndex = fruits.indexOf("Banana");      // 1
        int grapeIndex = fruits.indexOf("Grape");        // -1 (not found)
        
        // Find last occurrence
        fruits.add("Apple");  // [Apple, Banana, Cherry, Date, Apple]
        int lastApple = fruits.lastIndexOf("Apple");     // 4
        int firstApple = fruits.indexOf("Apple");        // 0
        
        // Get size
        int size = fruits.size();  // 5
        
        // Check if empty
        boolean isEmpty = fruits.isEmpty();  // false
    }
}
```

### Modifying Elements

```java
import java.util.ArrayList;
import java.util.List;

public class ModifyingElements {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry"));
        
        // Replace element at index
        String old = fruits.set(1, "Blueberry");
        // old = "Banana", list = [Apple, Blueberry, Cherry]
        
        // Replace all elements matching condition
        fruits.replaceAll(s -> s.toUpperCase());
        // [APPLE, BLUEBERRY, CHERRY]
        
        // Replace using a pattern
        fruits.replaceAll(s -> s.startsWith("A") ? s.toLowerCase() : s);
        // [apple, BLUEBERRY, CHERRY]
        
        System.out.println(fruits);
    }
}
```

### Removing Elements

```java
import java.util.ArrayList;
import java.util.List;

public class RemovingElements {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date", "Apple", "Elderberry")
        );
        
        // Remove by index (returns removed element)
        String removed = fruits.remove(0);
        // removed = "Apple", list = [Banana, Cherry, Date, Apple, Elderberry]
        
        // Remove by object (removes first occurrence, returns boolean)
        boolean wasRemoved = fruits.remove("Apple");
        // wasRemoved = true, list = [Banana, Cherry, Date, Elderberry]
        
        // Remove first and last (Java 21+)
        fruits.removeFirst();  // [Cherry, Date, Elderberry]
        fruits.removeLast();   // [Cherry, Date]
        
        // Reset the list
        fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date", "Elderberry")
        );
        
        // Remove all matching a condition
        fruits.removeIf(s -> s.length() > 5);
        // [Apple, Date]
        
        // Remove all elements from another collection
        fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date")
        );
        fruits.removeAll(List.of("Apple", "Date"));
        // [Banana, Cherry]
        
        // Keep only elements from another collection
        fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date")
        );
        fruits.retainAll(List.of("Apple", "Cherry", "Grape"));
        // [Apple, Cherry]
        
        // Clear all elements
        fruits.clear();
        // []
    }
}
```

### Removing Integers from a List

When removing from a `List<Integer>`, be careful about remove(int index) vs remove(Object o):

```java
import java.util.ArrayList;
import java.util.List;

public class IntegerRemoval {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(10, 20, 30, 40, 50));
        
        // Remove by index (removes element at position 2)
        numbers.remove(2);  // Removes 30, list = [10, 20, 40, 50]
        
        // Remove by value (must cast or use Integer.valueOf)
        numbers.remove(Integer.valueOf(40));  // Removes 40, list = [10, 20, 50]
        
        // Alternative: use removeIf for clarity
        numbers.removeIf(n -> n == 20);  // [10, 50]
        
        System.out.println(numbers);
    }
}
```

---

## Iterating Over Lists

There are several ways to loop through a list.

### Enhanced For Loop (Recommended for Reading)

```java
import java.util.List;

public class EnhancedForLoop {
    public static void main(String[] args) {
        List<String> fruits = List.of("Apple", "Banana", "Cherry");
        
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        // Output:
        // Apple
        // Banana
        // Cherry
    }
}
```

### Traditional For Loop (When You Need Index)

```java
import java.util.List;

public class TraditionalForLoop {
    public static void main(String[] args) {
        List<String> fruits = List.of("Apple", "Banana", "Cherry");
        
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println(i + ": " + fruits.get(i));
        }
        // Output:
        // 0: Apple
        // 1: Banana
        // 2: Cherry
    }
}
```

### forEach with Lambda

```java
import java.util.List;

public class ForEachLambda {
    public static void main(String[] args) {
        List<String> fruits = List.of("Apple", "Banana", "Cherry");
        
        // Simple forEach
        fruits.forEach(fruit -> System.out.println(fruit));
        
        // Method reference (equivalent)
        fruits.forEach(System.out::println);
        
        // With index using IntStream
        java.util.stream.IntStream.range(0, fruits.size())
            .forEach(i -> System.out.println(i + ": " + fruits.get(i)));
    }
}
```

### Iterator (When You Need to Remove During Iteration)

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorExample {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        
        // WRONG: ConcurrentModificationException
        // for (String fruit : fruits) {
        //     if (fruit.startsWith("B")) {
        //         fruits.remove(fruit);  // Throws exception!
        //     }
        // }
        
        // CORRECT: Use Iterator
        Iterator<String> iterator = fruits.iterator();
        while (iterator.hasNext()) {
            String fruit = iterator.next();
            if (fruit.startsWith("B")) {
                iterator.remove();  // Safe removal
            }
        }
        System.out.println(fruits);  // [Apple, Cherry, Date]
        
        // ALSO CORRECT: Use removeIf (simpler)
        fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        fruits.removeIf(fruit -> fruit.startsWith("C"));
        System.out.println(fruits);  // [Apple, Banana, Date]
    }
}
```

### ListIterator (Bidirectional Iteration)

```java
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorExample {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(List.of("Apple", "Banana", "Cherry"));
        
        // Get ListIterator
        ListIterator<String> listIterator = fruits.listIterator();
        
        // Forward iteration
        System.out.println("Forward:");
        while (listIterator.hasNext()) {
            int index = listIterator.nextIndex();
            String fruit = listIterator.next();
            System.out.println(index + ": " + fruit);
        }
        
        // Backward iteration (iterator is now at the end)
        System.out.println("\nBackward:");
        while (listIterator.hasPrevious()) {
            int index = listIterator.previousIndex();
            String fruit = listIterator.previous();
            System.out.println(index + ": " + fruit);
        }
        
        // Start from specific index
        ListIterator<String> fromMiddle = fruits.listIterator(1);
        System.out.println("\nFrom index 1:");
        while (fromMiddle.hasNext()) {
            System.out.println(fromMiddle.next());
        }
        
        // Modify during iteration
        ListIterator<String> modifier = fruits.listIterator();
        while (modifier.hasNext()) {
            String fruit = modifier.next();
            if (fruit.equals("Banana")) {
                modifier.set("Blueberry");  // Replace current
            }
            if (fruit.equals("Cherry")) {
                modifier.add("Cranberry");  // Insert after current
            }
        }
        System.out.println("\nAfter modification: " + fruits);
        // [Apple, Blueberry, Cherry, Cranberry]
    }
}
```

---

## Sorting Lists

### Sorting with Comparable

When elements implement Comparable, you can sort directly:

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingComparable {
    public static void main(String[] args) {
        // Strings implement Comparable (alphabetical order)
        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));
        Collections.sort(names);
        System.out.println(names);  // [Alice, Bob, Charlie]
        
        // Reverse order
        Collections.sort(names, Collections.reverseOrder());
        System.out.println(names);  // [Charlie, Bob, Alice]
        
        // Numbers implement Comparable (numerical order)
        List<Integer> numbers = new ArrayList<>(List.of(30, 10, 20, 50, 40));
        Collections.sort(numbers);
        System.out.println(numbers);  // [10, 20, 30, 40, 50]
        
        // Using List.sort (Java 8+)
        numbers.sort(null);  // null uses natural ordering
        System.out.println(numbers);  // [10, 20, 30, 40, 50]
    }
}
```

### Sorting with Comparator

For custom sorting or when elements don't implement Comparable:

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortingComparator {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Charlie", "alice", "Bob", "david"));
        
        // Sort by length
        names.sort(Comparator.comparingInt(String::length));
        System.out.println(names);  // [Bob, alice, david, Charlie]
        
        // Sort case-insensitive
        names.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(names);  // [alice, Bob, Charlie, david]
        
        // Sort with lambda
        names.sort((a, b) -> a.toLowerCase().compareTo(b.toLowerCase()));
        System.out.println(names);  // [alice, Bob, Charlie, david]
        
        // Reverse any comparator
        names.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println(names);  // [Charlie, david, alice, Bob]
        
        // Chain comparators (sort by length, then alphabetically)
        names.sort(Comparator.comparingInt(String::length)
                             .thenComparing(String::toLowerCase));
        System.out.println(names);  // [Bob, alice, david, Charlie]
    }
}
```

### Sorting Objects

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}

public class SortingObjects {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 30));
        people.add(new Person("David", 25));
        
        // Sort by age
        people.sort(Comparator.comparingInt(Person::getAge));
        System.out.println(people);
        // [Bob(25), David(25), Alice(30), Charlie(30)]
        
        // Sort by name
        people.sort(Comparator.comparing(Person::getName));
        System.out.println(people);
        // [Alice(30), Bob(25), Charlie(30), David(25)]
        
        // Sort by age, then by name
        people.sort(Comparator.comparingInt(Person::getAge)
                              .thenComparing(Person::getName));
        System.out.println(people);
        // [Bob(25), David(25), Alice(30), Charlie(30)]
        
        // Sort by age descending, then name ascending
        people.sort(Comparator.comparingInt(Person::getAge).reversed()
                              .thenComparing(Person::getName));
        System.out.println(people);
        // [Alice(30), Charlie(30), Bob(25), David(25)]
    }
}
```

---

## Searching Lists

### Linear Search

```java
import java.util.List;

public class LinearSearch {
    public static void main(String[] args) {
        List<String> fruits = List.of("Apple", "Banana", "Cherry", "Date");
        
        // Check if contains
        boolean hasApple = fruits.contains("Apple");  // true
        
        // Find index
        int index = fruits.indexOf("Cherry");  // 2
        
        // Find with condition
        String found = null;
        for (String fruit : fruits) {
            if (fruit.startsWith("D")) {
                found = fruit;
                break;
            }
        }
        System.out.println(found);  // Date
        
        // Using Stream (Java 8+)
        String result = fruits.stream()
            .filter(f -> f.startsWith("D"))
            .findFirst()
            .orElse("Not found");
        System.out.println(result);  // Date
    }
}
```

### Binary Search (Sorted Lists Only)

Binary search is much faster than linear search for large sorted lists.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarySearch {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(10, 20, 30, 40, 50, 60, 70, 80, 90));
        // List MUST be sorted for binary search
        
        // Search for existing element
        int index = Collections.binarySearch(numbers, 50);
        System.out.println("50 found at index: " + index);  // 4
        
        // Search for non-existing element
        int notFound = Collections.binarySearch(numbers, 55);
        System.out.println("55 result: " + notFound);  // -6 (insertion point would be 5)
        // Formula: -(insertion point) - 1
        
        // Calculate insertion point from negative result
        if (notFound < 0) {
            int insertionPoint = -(notFound + 1);
            System.out.println("55 would be inserted at: " + insertionPoint);  // 5
        }
        
        // Binary search with custom comparator
        List<String> names = new ArrayList<>(List.of("alice", "Bob", "CHARLIE", "david"));
        names.sort(String.CASE_INSENSITIVE_ORDER);  // Must sort with same comparator
        
        int bobIndex = Collections.binarySearch(names, "BOB", String.CASE_INSENSITIVE_ORDER);
        System.out.println("BOB found at: " + bobIndex);  // 1
    }
}
```

---

## Sublists and Views

### Creating Sublists

```java
import java.util.ArrayList;
import java.util.List;

public class SublistExample {
    public static void main(String[] args) {
        List<String> fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date", "Elderberry")
        );
        
        // Get sublist (fromIndex inclusive, toIndex exclusive)
        List<String> middle = fruits.subList(1, 4);
        System.out.println(middle);  // [Banana, Cherry, Date]
        
        // Sublist is a VIEW - changes affect original list!
        middle.set(0, "Blueberry");
        System.out.println(fruits);  
        // [Apple, Blueberry, Cherry, Date, Elderberry]
        
        // Clear a range
        fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date", "Elderberry")
        );
        fruits.subList(1, 4).clear();
        System.out.println(fruits);  // [Apple, Elderberry]
        
        // Create independent copy of sublist
        fruits = new ArrayList<>(
            List.of("Apple", "Banana", "Cherry", "Date", "Elderberry")
        );
        List<String> copy = new ArrayList<>(fruits.subList(1, 4));
        copy.set(0, "Blueberry");
        System.out.println(fruits);  // [Apple, Banana, Cherry, Date, Elderberry] (unchanged)
        System.out.println(copy);    // [Blueberry, Cherry, Date]
    }
}
```

---

## LinkedList

LinkedList is a doubly-linked list implementation. Each element (node) contains references to the previous and next elements.

### When to Use LinkedList vs ArrayList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| Access by index | O(1) Fast | O(n) Slow |
| Add at end | O(1) Fast | O(1) Fast |
| Add at beginning | O(n) Slow | O(1) Fast |
| Add in middle | O(n) Slow | O(1) after finding position |
| Remove from end | O(1) Fast | O(1) Fast |
| Remove from beginning | O(n) Slow | O(1) Fast |
| Memory usage | Less (contiguous) | More (nodes with pointers) |
| Cache friendly | Yes | No |

**Rule of thumb:** Use ArrayList unless you need frequent insertions/removals at the beginning or middle of the list.

### LinkedList as a Deque

LinkedList also implements the Deque interface, making it useful as a double-ended queue:

```java
import java.util.LinkedList;
import java.util.Deque;

public class LinkedListDeque {
    public static void main(String[] args) {
        Deque<String> deque = new LinkedList<>();
        
        // Add at front
        deque.addFirst("B");
        deque.addFirst("A");
        System.out.println(deque);  // [A, B]
        
        // Add at back
        deque.addLast("C");
        deque.addLast("D");
        System.out.println(deque);  // [A, B, C, D]
        
        // Remove from front
        String first = deque.removeFirst();
        System.out.println(first + " removed, list: " + deque);  // A removed, list: [B, C, D]
        
        // Remove from back
        String last = deque.removeLast();
        System.out.println(last + " removed, list: " + deque);  // D removed, list: [B, C]
        
        // Peek without removing
        String peekFirst = deque.peekFirst();  // B
        String peekLast = deque.peekLast();    // C
        System.out.println("First: " + peekFirst + ", Last: " + peekLast);
        
        // Stack operations (LIFO)
        deque.push("X");  // Same as addFirst
        System.out.println(deque);  // [X, B, C]
        
        String popped = deque.pop();  // Same as removeFirst
        System.out.println(popped + " popped, list: " + deque);  // X popped, list: [B, C]
    }
}
```

---

## Immutable Lists

### List.of() (Java 9+)

Creates an immutable list that cannot be modified:

```java
import java.util.List;

public class ImmutableLists {
    public static void main(String[] args) {
        // Create immutable list
        List<String> fruits = List.of("Apple", "Banana", "Cherry");
        
        // Reading works
        System.out.println(fruits.get(0));  // Apple
        System.out.println(fruits.size());  // 3
        
        // Modification throws exception
        try {
            fruits.add("Date");  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutable list");
        }
        
        try {
            fruits.set(0, "Apricot");  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutable list");
        }
        
        // Null elements not allowed
        try {
            List<String> withNull = List.of("A", null, "B");  // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("List.of does not allow null");
        }
    }
}
```

### Arrays.asList()

Creates a fixed-size list backed by an array:

```java
import java.util.Arrays;
import java.util.List;

public class ArraysAsList {
    public static void main(String[] args) {
        // Fixed-size list
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
        
        // Modification of existing elements works
        fruits.set(0, "Apricot");
        System.out.println(fruits);  // [Apricot, Banana, Cherry]
        
        // Adding/removing throws exception
        try {
            fruits.add("Date");  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot add to fixed-size list");
        }
        
        // Backed by original array
        String[] array = {"Apple", "Banana", "Cherry"};
        List<String> list = Arrays.asList(array);
        array[0] = "Apricot";
        System.out.println(list.get(0));  // Apricot (changed!)
        
        // Allows null elements
        List<String> withNull = Arrays.asList("A", null, "B");  // OK
    }
}
```

### Comparison of List Creation Methods

| Method | Mutable | Add/Remove | Modify Elements | Allows Null |
|--------|---------|------------|-----------------|-------------|
| `new ArrayList<>()` | Yes | Yes | Yes | Yes |
| `List.of()` | No | No | No | No |
| `Arrays.asList()` | Partial | No | Yes | Yes |
| `Collections.unmodifiableList()` | No | No | No | Yes |
| `List.copyOf()` | No | No | No | No |

---

## Copying Lists

### Shallow Copy

```java
import java.util.ArrayList;
import java.util.List;

public class ShallowCopy {
    public static void main(String[] args) {
        List<StringBuilder> original = new ArrayList<>();
        original.add(new StringBuilder("Hello"));
        original.add(new StringBuilder("World"));
        
        // Shallow copy - new list, same elements
        List<StringBuilder> copy = new ArrayList<>(original);
        
        // Modifying the copy's structure doesn't affect original
        copy.add(new StringBuilder("!"));
        System.out.println(original.size());  // 2
        System.out.println(copy.size());      // 3
        
        // BUT modifying shared elements affects both!
        copy.get(0).append(" Java");
        System.out.println(original.get(0));  // Hello Java (changed!)
        System.out.println(copy.get(0));      // Hello Java
    }
}
```

### Deep Copy

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Item implements Cloneable {
    private String name;
    
    public Item(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Override
    public Item clone() {
        return new Item(this.name);  // Create new object
    }
    
    @Override
    public String toString() { return name; }
}

public class DeepCopy {
    public static void main(String[] args) {
        List<Item> original = new ArrayList<>();
        original.add(new Item("Apple"));
        original.add(new Item("Banana"));
        
        // Deep copy using stream and clone
        List<Item> deepCopy = original.stream()
            .map(Item::clone)
            .collect(Collectors.toList());
        
        // Deep copy using loop
        List<Item> deepCopy2 = new ArrayList<>();
        for (Item item : original) {
            deepCopy2.add(item.clone());
        }
        
        // Now modifying elements doesn't affect original
        deepCopy.get(0).setName("Apricot");
        System.out.println(original.get(0));  // Apple (unchanged)
        System.out.println(deepCopy.get(0));  // Apricot
    }
}
```

---

## Common List Operations

### Converting Between List and Array

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListArrayConversion {
    public static void main(String[] args) {
        // Array to List
        String[] array = {"Apple", "Banana", "Cherry"};
        List<String> list = new ArrayList<>(Arrays.asList(array));
        
        // List to Array
        String[] backToArray = list.toArray(new String[0]);
        
        // List to Array (Java 11+)
        String[] backToArray2 = list.toArray(String[]::new);
        
        // For primitives, use streams
        int[] intArray = {1, 2, 3, 4, 5};
        List<Integer> intList = Arrays.stream(intArray)
            .boxed()
            .toList();  // Java 16+
        
        int[] backToIntArray = intList.stream()
            .mapToInt(Integer::intValue)
            .toArray();
    }
}
```

### Shuffling and Reversing

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleReverse {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        
        // Shuffle randomly
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);  // Random order
        
        // Reverse
        numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.reverse(numbers);
        System.out.println("Reversed: " + numbers);  // [5, 4, 3, 2, 1]
        
        // Rotate (move elements)
        numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(numbers, 2);  // Rotate right by 2
        System.out.println("Rotated right by 2: " + numbers);  // [4, 5, 1, 2, 3]
        
        Collections.rotate(numbers, -1);  // Rotate left by 1
        System.out.println("Rotated left by 1: " + numbers);  // [5, 1, 2, 3, 4]
    }
}
```

### Finding Min and Max

```java
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MinMax {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(30, 10, 50, 20, 40);
        
        // Find min and max
        int min = Collections.min(numbers);
        int max = Collections.max(numbers);
        System.out.println("Min: " + min + ", Max: " + max);  // Min: 10, Max: 50
        
        // With custom comparator
        List<String> words = List.of("apple", "blueberry", "fig", "date");
        
        String shortest = Collections.min(words, Comparator.comparingInt(String::length));
        String longest = Collections.max(words, Comparator.comparingInt(String::length));
        System.out.println("Shortest: " + shortest);  // fig
        System.out.println("Longest: " + longest);    // blueberry
    }
}
```

### Filling and Replacing

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillReplace {
    public static void main(String[] args) {
        // Fill entire list with same value
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        Collections.fill(list, "X");
        System.out.println(list);  // [X, X, X, X]
        
        // Create list filled with n copies
        List<String> copies = new ArrayList<>(Collections.nCopies(5, "Hello"));
        System.out.println(copies);  // [Hello, Hello, Hello, Hello, Hello]
        
        // Replace all occurrences
        list = new ArrayList<>(List.of("A", "B", "A", "C", "A"));
        Collections.replaceAll(list, "A", "Z");
        System.out.println(list);  // [Z, B, Z, C, Z]
    }
}
```

### Checking Equality

```java
import java.util.ArrayList;
import java.util.List;

public class ListEquality {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>(List.of("A", "B", "C"));
        List<String> list2 = new ArrayList<>(List.of("A", "B", "C"));
        List<String> list3 = new ArrayList<>(List.of("A", "C", "B"));
        
        // Equals checks same elements in same order
        System.out.println(list1.equals(list2));  // true
        System.out.println(list1.equals(list3));  // false (different order)
        
        // Different types of Lists can be equal
        List<String> linkedList = new java.util.LinkedList<>(List.of("A", "B", "C"));
        System.out.println(list1.equals(linkedList));  // true
    }
}
```

---

## Lists with Streams

Java Streams provide powerful operations on lists.

### Basic Stream Operations

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamBasics {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");
        
        // Filter
        List<String> longNames = names.stream()
            .filter(name -> name.length() > 4)
            .collect(Collectors.toList());
        System.out.println(longNames);  // [Alice, Charlie, David]
        
        // Map (transform)
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println(upperNames);  // [ALICE, BOB, CHARLIE, DAVID, EVE]
        
        // Map to different type
        List<Integer> lengths = names.stream()
            .map(String::length)
            .collect(Collectors.toList());
        System.out.println(lengths);  // [5, 3, 7, 5, 3]
        
        // Chain operations
        List<String> result = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
        System.out.println(result);  // [ALICE, CHARLIE, DAVID]
        
        // toList() method (Java 16+)
        List<String> simplified = names.stream()
            .filter(name -> name.startsWith("A"))
            .toList();
        System.out.println(simplified);  // [Alice]
    }
}
```

### Reduction Operations

```java
import java.util.List;
import java.util.Optional;

public class StreamReduction {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        
        // Sum
        int sum = numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
        System.out.println("Sum: " + sum);  // 15
        
        // Average
        double average = numbers.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        System.out.println("Average: " + average);  // 3.0
        
        // Count
        long count = numbers.stream()
            .filter(n -> n % 2 == 0)
            .count();
        System.out.println("Even count: " + count);  // 2
        
        // Reduce (custom accumulation)
        int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println("Product: " + product);  // 120
        
        // Find first matching
        Optional<Integer> firstEven = numbers.stream()
            .filter(n -> n % 2 == 0)
            .findFirst();
        System.out.println("First even: " + firstEven.orElse(-1));  // 2
        
        // Any match
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        System.out.println("Has even: " + hasEven);  // true
        
        // All match
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        System.out.println("All positive: " + allPositive);  // true
        
        // None match
        boolean noNegative = numbers.stream().noneMatch(n -> n < 0);
        System.out.println("No negative: " + noNegative);  // true
    }
}
```

### Distinct and Limit

```java
import java.util.List;
import java.util.stream.Collectors;

public class DistinctLimit {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        
        // Remove duplicates
        List<Integer> unique = numbers.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println(unique);  // [1, 2, 3, 4]
        
        // Limit to first n elements
        List<Integer> firstThree = numbers.stream()
            .limit(3)
            .collect(Collectors.toList());
        System.out.println(firstThree);  // [1, 2, 2]
        
        // Skip first n elements
        List<Integer> afterThree = numbers.stream()
            .skip(3)
            .collect(Collectors.toList());
        System.out.println(afterThree);  // [3, 3, 3, 4, 4, 4, 4]
        
        // Combine: skip and limit for pagination
        int page = 1;
        int pageSize = 3;
        List<Integer> pageItems = numbers.stream()
            .skip((long) page * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());
        System.out.println("Page 1: " + pageItems);  // [3, 3, 3]
    }
}
```

---

## Thread Safety

ArrayList and LinkedList are not thread-safe. Use synchronized wrappers or concurrent collections for multi-threaded code.

### Synchronized List

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynchronizedList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        List<String> syncList = Collections.synchronizedList(list);
        
        // Individual operations are thread-safe
        syncList.add("A");
        syncList.add("B");
        
        // BUT iteration requires manual synchronization
        synchronized (syncList) {
            for (String s : syncList) {
                System.out.println(s);
            }
        }
    }
}
```

### CopyOnWriteArrayList

For scenarios with many reads and few writes:

```java
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteExample {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();
        
        list.add("A");
        list.add("B");
        list.add("C");
        
        // Safe to iterate without synchronization
        for (String s : list) {
            System.out.println(s);
            // Safe to modify during iteration
            // (iterator sees snapshot, not modifications)
            list.add("D");  // Won't appear in this iteration
        }
        
        System.out.println("After loop: " + list);
    }
}
```

---

## Common Mistakes and How to Avoid Them

### Mistake 1: Modifying List During Enhanced For Loop

```java
// WRONG
for (String item : list) {
    if (condition) {
        list.remove(item);  // ConcurrentModificationException
    }
}

// CORRECT: Use Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (condition) {
        it.remove();
    }
}

// CORRECT: Use removeIf
list.removeIf(item -> condition);
```

### Mistake 2: Using Wrong Remove Method for Integers

```java
List<Integer> numbers = new ArrayList<>(List.of(10, 20, 30));

// WRONG: Removes at index 20 (out of bounds)
// numbers.remove(20);

// CORRECT: Remove value 20
numbers.remove(Integer.valueOf(20));
```

### Mistake 3: Assuming subList is Independent

```java
List<String> original = new ArrayList<>(List.of("A", "B", "C"));
List<String> sub = original.subList(0, 2);

// WRONG: Modifying original invalidates sublist
original.add("D");
// sub.get(0);  // ConcurrentModificationException

// CORRECT: Create independent copy
List<String> independentSub = new ArrayList<>(original.subList(0, 2));
```

### Mistake 4: Expecting List.of() to Be Mutable

```java
// WRONG
List<String> list = List.of("A", "B");
// list.add("C");  // UnsupportedOperationException

// CORRECT: Wrap in ArrayList for mutability
List<String> mutableList = new ArrayList<>(List.of("A", "B"));
mutableList.add("C");  // Works
```

### Mistake 5: Using LinkedList When ArrayList is Better

```java
// Usually WRONG (slow random access)
List<String> list = new LinkedList<>();
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));  // O(n) for each get!
}

// CORRECT: Use ArrayList for random access
List<String> list = new ArrayList<>();

// OR use enhanced for loop with LinkedList
for (String s : list) {
    System.out.println(s);  // O(1) per element
}
```

---

## Best Practices

### 1. Program to Interface

```java
// GOOD: Use List interface
List<String> names = new ArrayList<>();

// AVOID: Declaring with implementation type (unless needed)
ArrayList<String> names = new ArrayList<>();
```

### 2. Specify Initial Capacity for Large Lists

```java
// GOOD: Avoids resizing overhead
List<String> largeList = new ArrayList<>(10000);

// May cause multiple array resizes
List<String> largeList = new ArrayList<>();
```

### 3. Use Appropriate List Type

```java
// Random access, general purpose
List<String> general = new ArrayList<>();

// Frequent add/remove at beginning
Deque<String> queue = new LinkedList<>();

// Thread-safe with many reads
List<String> concurrent = new CopyOnWriteArrayList<>();

// Immutable list
List<String> constants = List.of("A", "B", "C");
```

### 4. Return Unmodifiable Lists from Methods

```java
public class SafeReturn {
    private List<String> internalList = new ArrayList<>();
    
    // GOOD: Return unmodifiable view
    public List<String> getItems() {
        return Collections.unmodifiableList(internalList);
    }
    
    // GOOD: Return copy
    public List<String> getItemsCopy() {
        return new ArrayList<>(internalList);
    }
    
    // BAD: Exposes internal list
    public List<String> getItemsUnsafe() {
        return internalList;  // Caller can modify!
    }
}
```

### 5. Use Diamond Operator

```java
// GOOD: Type inferred
List<String> list = new ArrayList<>();
Map<String, List<Integer>> map = new HashMap<>();

// AVOID: Redundant type specification
List<String> list = new ArrayList<String>();
```

---

## Cheat Sheet

| Operation | Code | Notes |
|-----------|------|-------|
| Create empty list | `new ArrayList<>()` | Mutable, empty |
| Create from values | `List.of("A", "B")` | Immutable |
| Create mutable from values | `new ArrayList<>(List.of("A", "B"))` | Mutable copy |
| Add element | `list.add("X")` | At end |
| Add at index | `list.add(0, "X")` | Shifts elements |
| Get element | `list.get(0)` | By index |
| Set element | `list.set(0, "X")` | Replace at index |
| Remove by index | `list.remove(0)` | Returns removed |
| Remove by value | `list.remove("X")` | First occurrence |
| Remove matching | `list.removeIf(x -> condition)` | All matching |
| Size | `list.size()` | Element count |
| Contains | `list.contains("X")` | Boolean |
| Find index | `list.indexOf("X")` | -1 if not found |
| Sort | `list.sort(null)` or `Collections.sort(list)` | Natural order |
| Sort custom | `list.sort(Comparator.comparing(...))` | Custom order |
| Reverse | `Collections.reverse(list)` | In place |
| Shuffle | `Collections.shuffle(list)` | Random order |
| Clear | `list.clear()` | Remove all |
| Sublist | `list.subList(1, 4)` | View (be careful!) |
| To array | `list.toArray(new String[0])` | Copy to array |
| Stream | `list.stream()` | For functional ops |

---

## Summary

Lists are the most commonly used collection type in Java. Key points to remember:

1. **ArrayList** is the default choice for most use cases
2. **LinkedList** is better for frequent insertions/removals at the beginning
3. Use the **List interface** when declaring variables
4. **List.of()** creates immutable lists
5. Be careful with **subList()** as it creates a view, not a copy
6. Use **Iterator** or **removeIf()** when removing during iteration
7. For thread safety, use **synchronized wrappers** or **CopyOnWriteArrayList**
8. Use **Streams** for functional-style operations

---

[Back to Guide](../guide.md) | [Previous: Exception Handling](16-exception-handling.md) | [Next: Collections - Sets](18-collections-sets.md)
