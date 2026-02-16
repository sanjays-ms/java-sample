# Generics

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/generics-cheatsheet.md)

---

## What Are Generics?

Generics allow you to write code that works with different types while still being type-safe.

Without generics, you would have to use `Object` for everything and cast manually:

```java
// Before generics (Java 1.4 and earlier)
List list = new ArrayList();     // Can hold anything
list.add("Hello");               // Add a String
list.add(42);                    // Add an Integer - no error!

String s = (String) list.get(0); // Must cast - might fail at runtime
String s2 = (String) list.get(1); // ClassCastException! 42 is not a String
```

With generics:

```java
// With generics (Java 5+)
List<String> list = new ArrayList<>();  // Can only hold Strings
list.add("Hello");                       // OK
list.add(42);                            // Compile error! Type safety

String s = list.get(0);                  // No cast needed
```

**In plain words:** Generics let you tell the compiler what type of data a class or method will work with. The compiler then checks that you only use that type, catching errors before your program runs.

---

## Why Generics Exist

Generics solve three main problems:

### Problem 1: Type Safety

Without generics, the compiler cannot check what you put into a collection:

```java
// Without generics - dangerous
List names = new ArrayList();
names.add("Alice");
names.add(123);        // Oops! Added a number to "names"
names.add(new Date()); // Oops! Added a date too

// Program crashes when you try to use the data
for (Object obj : names) {
    String name = (String) obj;  // ClassCastException on 123!
}
```

With generics, the compiler prevents this:

```java
// With generics - safe
List<String> names = new ArrayList<>();
names.add("Alice");
names.add(123);        // Compile error: incompatible types
names.add(new Date()); // Compile error: incompatible types
```

### Problem 2: Eliminating Casts

Without generics, you must cast every time you retrieve an element:

```java
// Without generics - tedious and error-prone
List list = new ArrayList();
list.add("Hello");
String s = (String) list.get(0);  // Cast required
```

With generics, no casting needed:

```java
// With generics - clean
List<String> list = new ArrayList<>();
list.add("Hello");
String s = list.get(0);  // No cast needed - compiler knows it's a String
```

### Problem 3: Reusable Code

Generics let you write one class or method that works with many types:

```java
// One Box class works with any type
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<User> userBox = new Box<>();
```

Without generics, you would need separate classes for each type or use Object and lose type safety.

---

## Type Parameters

Type parameters are placeholders for actual types. They are specified in angle brackets `<>`.

### Common Type Parameter Names

By convention, type parameters use single uppercase letters:

| Parameter | Meaning | Common Usage |
|-----------|---------|--------------|
| `T` | Type | General-purpose type |
| `E` | Element | Elements in a collection |
| `K` | Key | Keys in a map |
| `V` | Value | Values in a map |
| `N` | Number | Numeric types |
| `S`, `U` | Second, Third types | When you need multiple type parameters |

These are just conventions. You could use any name, but following conventions makes code easier to read.

---

## Generic Classes

A generic class is a class that has one or more type parameters.

### Basic Generic Class

```java
// A simple container that can hold any type
// T is a type parameter - a placeholder for a real type
public class Box<T> {
    
    // The field uses the type parameter T
    // When someone creates Box<String>, this becomes String content
    // When someone creates Box<Integer>, this becomes Integer content
    private T content;
    
    // Constructor accepts type T
    public Box(T content) {
        this.content = content;
    }
    
    // Getter returns type T
    public T getContent() {
        return content;
    }
    
    // Setter accepts type T
    public void setContent(T content) {
        this.content = content;
    }
    
    // Method can use T in logic
    public boolean isEmpty() {
        return content == null;
    }
}
```

Using the generic class:

```java
// Create a Box that holds Strings
// T becomes String throughout the class
Box<String> stringBox = new Box<>("Hello");
String s = stringBox.getContent();  // Returns String, no cast needed

// Create a Box that holds Integers
// T becomes Integer throughout the class
Box<Integer> intBox = new Box<>(42);
Integer i = intBox.getContent();  // Returns Integer, no cast needed

// Create a Box that holds a custom type
Box<User> userBox = new Box<>(new User("Alice"));
User u = userBox.getContent();  // Returns User, no cast needed
```

### The Diamond Operator

Since Java 7, you can omit the type on the right side using the diamond operator `<>`:

```java
// Before Java 7 - must repeat the type
Box<String> box = new Box<String>("Hello");

// Java 7+ - compiler infers the type from the left side
Box<String> box = new Box<>("Hello");  // Diamond operator

// Also works with complex types
Map<String, List<Integer>> map = new HashMap<>();  // Much cleaner!
```

### Multiple Type Parameters

A class can have multiple type parameters:

```java
// Pair class with two type parameters K and V
// K is the type of the first value (key)
// V is the type of the second value (value)
public class Pair<K, V> {
    
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public void setKey(K key) {
        this.key = key;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
```

Using multiple type parameters:

```java
// String key, Integer value
Pair<String, Integer> age = new Pair<>("Alice", 30);
String name = age.getKey();    // Returns String
Integer years = age.getValue(); // Returns Integer

// Integer key, String value
Pair<Integer, String> employee = new Pair<>(101, "Bob");

// Both same type is also valid
Pair<String, String> translation = new Pair<>("hello", "hola");

// Nested generics
Pair<String, List<Integer>> scores = new Pair<>("Alice", List.of(95, 87, 92));
```

### Practical Example: A Simple Cache

```java
// A cache that stores key-value pairs
// K is the key type, V is the value type
public class SimpleCache<K, V> {
    
    // Internal map uses the same type parameters
    private Map<K, V> cache = new HashMap<>();
    
    // Store a value with a key
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    // Retrieve a value by key
    // Returns null if key not found
    public V get(K key) {
        return cache.get(key);
    }
    
    // Check if key exists
    public boolean contains(K key) {
        return cache.containsKey(key);
    }
    
    // Remove a key-value pair
    public V remove(K key) {
        return cache.remove(key);
    }
    
    // Get number of items
    public int size() {
        return cache.size();
    }
    
    // Clear the cache
    public void clear() {
        cache.clear();
    }
}
```

Using the cache:

```java
// Cache with String keys and User values
SimpleCache<String, User> userCache = new SimpleCache<>();
userCache.put("alice", new User("Alice", 30));
userCache.put("bob", new User("Bob", 25));

User alice = userCache.get("alice");  // Returns User

// Cache with Integer keys and String values
SimpleCache<Integer, String> productCache = new SimpleCache<>();
productCache.put(1001, "Laptop");
productCache.put(1002, "Mouse");

String product = productCache.get(1001);  // Returns "Laptop"
```

---

## Generic Methods

A generic method is a method that has its own type parameter(s), independent of any class type parameters.

### Basic Generic Method

```java
public class Utility {
    
    // Generic method with type parameter T
    // <T> before the return type declares T as a type parameter for this method
    // The method takes an array of T and returns a List of T
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T element : array) {
            list.add(element);
        }
        return list;
    }
}
```

Using the generic method:

```java
// With String array
String[] names = {"Alice", "Bob", "Charlie"};
List<String> nameList = Utility.arrayToList(names);
// Compiler infers T = String from the argument

// With Integer array
Integer[] numbers = {1, 2, 3, 4, 5};
List<Integer> numberList = Utility.arrayToList(numbers);
// Compiler infers T = Integer from the argument

// You can also specify the type explicitly (rarely needed)
List<String> list = Utility.<String>arrayToList(names);
```

### Generic Method in Non-Generic Class

Generic methods can exist in regular (non-generic) classes:

```java
public class ArrayUtils {
    
    // Find the first element that matches
    // T is the type of elements in the array
    public static <T> T findFirst(T[] array, Predicate<T> condition) {
        for (T element : array) {
            if (condition.test(element)) {
                return element;
            }
        }
        return null;
    }
    
    // Swap two elements in an array
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    // Print all elements
    public static <T> void printAll(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }
}
```

Using these methods:

```java
// Find first name starting with 'A'
String[] names = {"Bob", "Alice", "Anna"};
String found = ArrayUtils.findFirst(names, name -> name.startsWith("A"));
// found = "Alice"

// Swap elements
Integer[] numbers = {1, 2, 3, 4, 5};
ArrayUtils.swap(numbers, 0, 4);
// numbers = {5, 2, 3, 4, 1}

// Print all
ArrayUtils.printAll(names);
```

### Generic Method with Multiple Type Parameters

```java
public class PairUtils {
    
    // Create a pair from two values
    // K is the key type, V is the value type
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
    
    // Swap key and value in a pair
    // Takes Pair<K, V> and returns Pair<V, K>
    public static <K, V> Pair<V, K> swap(Pair<K, V> pair) {
        return new Pair<>(pair.getValue(), pair.getKey());
    }
    
    // Compare two pairs
    public static <K, V> boolean equals(Pair<K, V> p1, Pair<K, V> p2) {
        return Objects.equals(p1.getKey(), p2.getKey()) 
            && Objects.equals(p1.getValue(), p2.getValue());
    }
}
```

Using multiple type parameters:

```java
// Create a pair
Pair<String, Integer> p1 = PairUtils.of("Alice", 30);

// Swap creates Pair<Integer, String>
Pair<Integer, String> p2 = PairUtils.swap(p1);
// p2 = (30, "Alice")

// Compare
Pair<String, Integer> p3 = PairUtils.of("Alice", 30);
boolean same = PairUtils.equals(p1, p3);  // true
```

### Instance Generic Methods

Non-static methods can also be generic:

```java
public class Converter {
    
    // Instance method with its own type parameter
    public <T> List<T> singletonList(T item) {
        List<T> list = new ArrayList<>();
        list.add(item);
        return list;
    }
}
```

```java
Converter converter = new Converter();
List<String> strings = converter.singletonList("Hello");
List<Integer> numbers = converter.singletonList(42);
```

---

## Generic Interfaces

Interfaces can also have type parameters.

### Defining a Generic Interface

```java
// Generic interface with type parameter T
public interface Container<T> {
    
    void add(T item);
    
    T get(int index);
    
    boolean contains(T item);
    
    int size();
}
```

### Implementing a Generic Interface

There are three ways to implement a generic interface:

#### Option 1: Keep It Generic

The implementing class remains generic:

```java
// ListContainer is also generic with parameter T
public class ListContainer<T> implements Container<T> {
    
    private List<T> items = new ArrayList<>();
    
    @Override
    public void add(T item) {
        items.add(item);
    }
    
    @Override
    public T get(int index) {
        return items.get(index);
    }
    
    @Override
    public boolean contains(T item) {
        return items.contains(item);
    }
    
    @Override
    public int size() {
        return items.size();
    }
}
```

```java
// Can be used with any type
Container<String> strings = new ListContainer<>();
Container<Integer> numbers = new ListContainer<>();
```

#### Option 2: Specify the Type

The implementing class fixes the type:

```java
// StringContainer specifically handles Strings
public class StringContainer implements Container<String> {
    
    private List<String> items = new ArrayList<>();
    
    @Override
    public void add(String item) {
        items.add(item);
    }
    
    @Override
    public String get(int index) {
        return items.get(index);
    }
    
    @Override
    public boolean contains(String item) {
        return items.contains(item);
    }
    
    @Override
    public int size() {
        return items.size();
    }
}
```

```java
// Only works with Strings
Container<String> container = new StringContainer();
container.add("Hello");
```

#### Option 3: Add More Type Parameters

The implementing class can add its own type parameters:

```java
// SpecialContainer has T from interface plus its own parameter U
public class SpecialContainer<T, U> implements Container<T> {
    
    private List<T> items = new ArrayList<>();
    private U metadata;  // Extra type parameter
    
    public SpecialContainer(U metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public void add(T item) {
        items.add(item);
    }
    
    @Override
    public T get(int index) {
        return items.get(index);
    }
    
    @Override
    public boolean contains(T item) {
        return items.contains(item);
    }
    
    @Override
    public int size() {
        return items.size();
    }
    
    public U getMetadata() {
        return metadata;
    }
}
```

### The Comparable Interface

`Comparable<T>` is a commonly used generic interface:

```java
public interface Comparable<T> {
    int compareTo(T o);
}
```

Implementing Comparable:

```java
public class Student implements Comparable<Student> {
    
    private String name;
    private int grade;
    
    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
    
    // Compare students by grade
    @Override
    public int compareTo(Student other) {
        // Negative if this < other
        // Zero if this == other
        // Positive if this > other
        return Integer.compare(this.grade, other.grade);
    }
    
    public String getName() {
        return name;
    }
    
    public int getGrade() {
        return grade;
    }
}
```

```java
List<Student> students = new ArrayList<>();
students.add(new Student("Alice", 85));
students.add(new Student("Bob", 92));
students.add(new Student("Charlie", 78));

Collections.sort(students);  // Uses compareTo
// Now sorted by grade: Charlie(78), Alice(85), Bob(92)
```

---

## Bounded Type Parameters

Sometimes you want to restrict what types can be used as type arguments. Bounded type parameters let you do this.

### Upper Bound with extends

Use `extends` to specify that T must be a subtype of a specific class or interface:

```java
// T must be a subtype of Number (Integer, Double, Long, etc.)
public class NumberBox<T extends Number> {
    
    private T value;
    
    public NumberBox(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    // Because T extends Number, we can use Number methods
    public double doubleValue() {
        return value.doubleValue();  // Number method
    }
    
    public int intValue() {
        return value.intValue();  // Number method
    }
}
```

```java
// Valid - Integer extends Number
NumberBox<Integer> intBox = new NumberBox<>(42);

// Valid - Double extends Number
NumberBox<Double> doubleBox = new NumberBox<>(3.14);

// Invalid - String does not extend Number
NumberBox<String> stringBox = new NumberBox<>("Hello");  // Compile error!
```

### Why Use Bounds?

Bounds let you use methods of the bound type:

```java
// Without bound - can only use Object methods
public class Box<T> {
    private T value;
    
    public void printLength() {
        // value.length();  // Error! Object has no length() method
    }
}

// With bound - can use CharSequence methods
public class TextBox<T extends CharSequence> {
    private T value;
    
    public TextBox(T value) {
        this.value = value;
    }
    
    public int getLength() {
        return value.length();  // CharSequence method - OK!
    }
    
    public char getFirst() {
        return value.charAt(0);  // CharSequence method - OK!
    }
}
```

```java
TextBox<String> stringBox = new TextBox<>("Hello");
int len = stringBox.getLength();  // 5

TextBox<StringBuilder> sbBox = new TextBox<>(new StringBuilder("World"));
int len2 = sbBox.getLength();  // 5
```

### Multiple Bounds

A type parameter can have multiple bounds using `&`:

```java
// T must extend Number AND implement Comparable
// Note: class must come first, then interfaces
public class ComparableNumber<T extends Number & Comparable<T>> {
    
    private T value;
    
    public ComparableNumber(T value) {
        this.value = value;
    }
    
    // Can use Number methods
    public double asDouble() {
        return value.doubleValue();
    }
    
    // Can use Comparable methods
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}
```

```java
// Integer extends Number and implements Comparable<Integer>
ComparableNumber<Integer> num = new ComparableNumber<>(42);
boolean greater = num.isGreaterThan(30);  // true
```

### Bounded Generic Methods

Methods can also have bounded type parameters:

```java
public class MathUtils {
    
    // Find the maximum of three values
    // T must be Comparable so we can use compareTo
    public static <T extends Comparable<T>> T max(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) {
            max = b;
        }
        if (c.compareTo(max) > 0) {
            max = c;
        }
        return max;
    }
    
    // Sum all numbers in a list
    // T must extend Number so we can use doubleValue
    public static <T extends Number> double sum(List<T> numbers) {
        double total = 0.0;
        for (T number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }
}
```

```java
// Works with any Comparable type
String maxString = MathUtils.max("apple", "banana", "cherry");  // "cherry"
Integer maxInt = MathUtils.max(10, 25, 15);  // 25
Double maxDouble = MathUtils.max(1.5, 2.5, 1.0);  // 2.5

// Works with any Number type
List<Integer> ints = List.of(1, 2, 3, 4, 5);
double sum1 = MathUtils.sum(ints);  // 15.0

List<Double> doubles = List.of(1.5, 2.5, 3.0);
double sum2 = MathUtils.sum(doubles);  // 7.0
```

---

## Wildcards

Wildcards represent an unknown type. They are written as `?` and are useful when you want flexibility in method parameters.

### Unbounded Wildcard: `<?>`

Use `?` when you do not care about the type:

```java
// Print any list
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

```java
List<String> strings = List.of("A", "B", "C");
List<Integer> numbers = List.of(1, 2, 3);
List<User> users = List.of(new User("Alice"), new User("Bob"));

printList(strings);  // Works
printList(numbers);  // Works
printList(users);    // Works
```

**Important:** With unbounded wildcard, you can only read as Object and cannot add elements (except null):

```java
public static void cannotModify(List<?> list) {
    Object item = list.get(0);  // OK - read as Object
    
    // list.add("Hello");  // Error! Cannot add
    // list.add(123);      // Error! Cannot add
    list.add(null);         // OK - null is valid for any type
}
```

### Upper Bounded Wildcard: `<? extends Type>`

Use `? extends Type` when you want to read from a collection of a type or its subtypes:

```java
// Can read as Number from any list of Number or subtype
public static double sumAll(List<? extends Number> numbers) {
    double sum = 0.0;
    for (Number n : numbers) {
        sum += n.doubleValue();
    }
    return sum;
}
```

```java
List<Integer> integers = List.of(1, 2, 3);
List<Double> doubles = List.of(1.5, 2.5, 3.0);
List<Number> numbers = List.of(1, 2.5, 3L);

double sum1 = sumAll(integers);  // Works - Integer extends Number
double sum2 = sumAll(doubles);   // Works - Double extends Number
double sum3 = sumAll(numbers);   // Works - Number is Number
```

**The PECS Rule - Producer Extends:**
Use `extends` when the collection PRODUCES (you read from it):

```java
public static void readFromList(List<? extends Number> producer) {
    // Can read as Number (or Object)
    Number n = producer.get(0);  // OK
    
    // Cannot write (except null)
    // producer.add(1);      // Error!
    // producer.add(1.0);    // Error!
}
```

### Lower Bounded Wildcard: `<? super Type>`

Use `? super Type` when you want to write to a collection that holds a type or its supertypes:

```java
// Can add Integers to any list that accepts Integers
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}
```

```java
List<Integer> integers = new ArrayList<>();
List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();

addNumbers(integers);  // Works - Integer is Integer
addNumbers(numbers);   // Works - Number is supertype of Integer
addNumbers(objects);   // Works - Object is supertype of Integer
```

**The PECS Rule - Consumer Super:**
Use `super` when the collection CONSUMES (you write to it):

```java
public static void writeToList(List<? super Integer> consumer) {
    // Can write Integer (and subtypes)
    consumer.add(10);      // OK
    consumer.add(20);      // OK
    
    // Can only read as Object
    Object o = consumer.get(0);  // OK - but only as Object
    // Integer i = consumer.get(0);  // Error!
}
```

### PECS: Producer Extends, Consumer Super

This is a key rule for using wildcards correctly:

| Scenario | Wildcard | You Can |
|----------|----------|---------|
| Read from collection | `? extends T` | Read as T |
| Write to collection | `? super T` | Write T (and subtypes) |
| Read and write | No wildcard (`T`) | Read and write T |

**Example: Copy elements from one list to another:**

```java
// src is a producer (we read from it) - use extends
// dest is a consumer (we write to it) - use super
public static <T> void copy(List<? extends T> src, List<? super T> dest) {
    for (T item : src) {
        dest.add(item);
    }
}
```

```java
List<Integer> source = List.of(1, 2, 3);
List<Number> destination = new ArrayList<>();

copy(source, destination);  // Works!
// Reads Integers from source, writes them to destination (which accepts Number)
```

### Wildcard Capture

Sometimes you need to work with the unknown type. Java can "capture" the wildcard:

```java
public static void swap(List<?> list, int i, int j) {
    // Cannot directly work with ?
    // list.set(i, list.get(j));  // Error!
    
    // Use a helper method to capture the type
    swapHelper(list, i, j);
}

// Helper method captures ? as T
private static <T> void swapHelper(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}
```

### When to Use Wildcards vs Type Parameters

| Situation | Use |
|-----------|-----|
| Multiple parameters must be same type | Type parameter `<T>` |
| Return type depends on parameter type | Type parameter `<T>` |
| Only reading, type does not matter | Wildcard `<?>` |
| Only reading a subtype | Wildcard `<? extends T>` |
| Only writing a supertype | Wildcard `<? super T>` |

```java
// Must use type parameter - return type depends on input
public static <T> T getFirst(List<T> list) {
    return list.get(0);
}

// Can use wildcard - just printing, type does not matter
public static void printAll(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// Must use type parameter - two lists must have same element type
public static <T> void transfer(List<T> from, List<T> to) {
    to.addAll(from);
}
```

---

## Type Erasure

Java uses type erasure to implement generics. This means that at runtime, generic type information is removed.

### What Is Type Erasure?

```java
// What you write
public class Box<T> {
    private T content;
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}

// What the compiler creates (after erasure)
public class Box {
    private Object content;  // T becomes Object
    
    public Object getContent() {
        return content;
    }
    
    public void setContent(Object content) {
        this.content = content;
    }
}
```

### Bounded Types After Erasure

When a type parameter has a bound, it erases to the bound:

```java
// What you write
public class NumberBox<T extends Number> {
    private T value;
    
    public T getValue() {
        return value;
    }
}

// After erasure
public class NumberBox {
    private Number value;  // T becomes Number (the bound)
    
    public Number getValue() {
        return value;
    }
}
```

### Consequences of Type Erasure

#### 1. Cannot Create Generic Arrays

```java
// Error: Cannot create generic array
T[] array = new T[10];  // Compile error!

// Workaround: Use Object array and cast
@SuppressWarnings("unchecked")
T[] array = (T[]) new Object[10];

// Better: Use ArrayList instead
List<T> list = new ArrayList<>();
```

#### 2. Cannot Use instanceof with Generic Types

```java
public static <T> void checkType(Object obj) {
    // Error: Cannot use instanceof with generic type
    if (obj instanceof T) {  // Compile error!
    }
    
    // Error: Cannot use instanceof with parameterized type
    if (obj instanceof List<String>) {  // Compile error!
    }
    
    // OK: Can use instanceof with raw type
    if (obj instanceof List) {  // OK - but loses type info
    }
    
    // OK: Can use instanceof with unbounded wildcard
    if (obj instanceof List<?>) {  // OK
    }
}
```

#### 3. Cannot Create Instances of Type Parameters

```java
public class Factory<T> {
    // Error: Cannot create instance of type parameter
    public T create() {
        return new T();  // Compile error!
    }
    
    // Workaround: Use Class<T> and reflection
    private Class<T> type;
    
    public Factory(Class<T> type) {
        this.type = type;
    }
    
    public T create() throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }
}
```

```java
// Usage
Factory<User> factory = new Factory<>(User.class);
User user = factory.create();
```

#### 4. Static Fields Cannot Use Type Parameters

```java
public class Container<T> {
    // Error: Cannot use T in static context
    private static T instance;  // Compile error!
    
    // OK: Instance field
    private T value;  // OK
}
```

---

## Generic Inheritance

### Extending Generic Classes

```java
// Generic parent class
public class Box<T> {
    private T content;
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}

// Option 1: Keep generic
public class ColoredBox<T> extends Box<T> {
    private String color;
    
    public ColoredBox(T content, String color) {
        setContent(content);
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
}

// Option 2: Fix the type
public class StringBox extends Box<String> {
    public StringBox(String content) {
        setContent(content);
    }
    
    // Can add String-specific methods
    public int getLength() {
        return getContent().length();
    }
}

// Option 3: Add more type parameters
public class MappedBox<K, V> extends Box<V> {
    private K key;
    
    public MappedBox(K key, V value) {
        this.key = key;
        setContent(value);
    }
    
    public K getKey() {
        return key;
    }
}
```

### Generic Type Relationships

Important: `List<Integer>` is NOT a subtype of `List<Number>`:

```java
List<Integer> integers = new ArrayList<>();
// List<Number> numbers = integers;  // Compile error!

// Why? If this was allowed:
// numbers.add(3.14);  // Adding Double to Integer list!
// Integer i = integers.get(0);  // ClassCastException!
```

Wildcards solve this:

```java
List<Integer> integers = new ArrayList<>();
List<? extends Number> numbers = integers;  // OK with wildcard
// But now you cannot add to numbers
```

### Covariance, Invariance, and Contravariance

| Type | Behavior | Example |
|------|----------|---------|
| Invariant | No subtyping | `List<Integer>` is not `List<Number>` |
| Covariant | Subtypes allowed | `List<? extends Number>` accepts `List<Integer>` |
| Contravariant | Supertypes allowed | `List<? super Integer>` accepts `List<Number>` |

```java
// Invariant - exact type match required
List<Number> numbers;
// numbers = new ArrayList<Integer>();  // Error!

// Covariant - subtypes allowed (for reading)
List<? extends Number> covariant;
covariant = new ArrayList<Integer>();  // OK
covariant = new ArrayList<Double>();   // OK

// Contravariant - supertypes allowed (for writing)
List<? super Integer> contravariant;
contravariant = new ArrayList<Integer>();  // OK
contravariant = new ArrayList<Number>();   // OK
contravariant = new ArrayList<Object>();   // OK
```

---

## Recursive Type Bounds

Sometimes a type parameter needs to refer to itself. This is called a recursive type bound.

### Self-Referencing Types

```java
// T must be Comparable to itself
public class MaxFinder<T extends Comparable<T>> {
    
    public T findMax(List<T> items) {
        if (items.isEmpty()) {
            return null;
        }
        
        T max = items.get(0);
        for (T item : items) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}
```

### Builder Pattern with Recursive Generics

```java
// Base builder that returns the correct subtype
public abstract class Builder<T extends Builder<T>> {
    
    protected String name;
    protected int value;
    
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
    
    public T withName(String name) {
        this.name = name;
        return self();
    }
    
    public T withValue(int value) {
        this.value = value;
        return self();
    }
    
    public abstract Object build();
}

// Concrete builder
public class UserBuilder extends Builder<UserBuilder> {
    
    private String email;
    
    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;  // Returns UserBuilder, not Builder
    }
    
    @Override
    public User build() {
        return new User(name, value, email);
    }
}
```

```java
// Fluent chain returns correct type at each step
User user = new UserBuilder()
    .withName("Alice")    // Returns UserBuilder
    .withValue(100)       // Returns UserBuilder
    .withEmail("a@b.com") // Returns UserBuilder
    .build();             // Returns User
```

---

## Common Generic Patterns

### Generic Factory Pattern

```java
// Factory interface
public interface Factory<T> {
    T create();
}

// String factory
public class StringFactory implements Factory<String> {
    @Override
    public String create() {
        return "";
    }
}

// Generic usage
public class Registry<T> {
    private Factory<T> factory;
    private List<T> instances = new ArrayList<>();
    
    public Registry(Factory<T> factory) {
        this.factory = factory;
    }
    
    public T createAndRegister() {
        T instance = factory.create();
        instances.add(instance);
        return instance;
    }
}
```

### Generic Singleton Holder

```java
public class SingletonHolder<T> {
    
    private T instance;
    private final Supplier<T> supplier;
    
    public SingletonHolder(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public synchronized T getInstance() {
        if (instance == null) {
            instance = supplier.get();
        }
        return instance;
    }
}
```

```java
SingletonHolder<DatabaseConnection> dbHolder = 
    new SingletonHolder<>(() -> new DatabaseConnection("localhost"));

DatabaseConnection db = dbHolder.getInstance();
```

### Generic Converter Pattern

```java
// Converter interface
public interface Converter<S, T> {
    T convert(S source);
}

// String to Integer converter
public class StringToIntConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.parseInt(source);
    }
}

// User to DTO converter
public class UserToDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }
}
```

### Generic Repository Pattern

```java
// Generic repository interface
public interface Repository<T, ID> {
    
    T findById(ID id);
    
    List<T> findAll();
    
    T save(T entity);
    
    void deleteById(ID id);
    
    boolean existsById(ID id);
}

// User repository implementation
public class UserRepository implements Repository<User, Long> {
    
    private Map<Long, User> storage = new HashMap<>();
    private long nextId = 1;
    
    @Override
    public User findById(Long id) {
        return storage.get(id);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        }
        storage.put(user.getId(), user);
        return user;
    }
    
    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
```

### Generic Event System

```java
// Generic event
public class Event<T> {
    private T data;
    private LocalDateTime timestamp;
    
    public Event(T data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    public T getData() {
        return data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

// Generic listener
public interface EventListener<T> {
    void onEvent(Event<T> event);
}

// Event bus
public class EventBus<T> {
    
    private List<EventListener<T>> listeners = new ArrayList<>();
    
    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }
    
    public void unsubscribe(EventListener<T> listener) {
        listeners.remove(listener);
    }
    
    public void publish(T data) {
        Event<T> event = new Event<>(data);
        for (EventListener<T> listener : listeners) {
            listener.onEvent(event);
        }
    }
}
```

```java
// Usage
EventBus<String> messageBus = new EventBus<>();

messageBus.subscribe(event -> {
    System.out.println("Received: " + event.getData());
});

messageBus.publish("Hello!");  // Prints: Received: Hello!
```

---

## Generic Methods in Collections

The Java Collections Framework makes heavy use of generic methods:

### Collections Utility Methods

```java
// Sorting
List<String> names = new ArrayList<>(List.of("Bob", "Alice", "Charlie"));
Collections.sort(names);  // Uses Comparable

// Sorting with comparator
Collections.sort(names, Comparator.reverseOrder());

// Binary search
int index = Collections.binarySearch(names, "Bob");

// Min and max
String min = Collections.min(names);
String max = Collections.max(names);

// Reverse
Collections.reverse(names);

// Shuffle
Collections.shuffle(names);

// Fill
Collections.fill(names, "X");  // All elements become "X"

// Copy
List<String> dest = new ArrayList<>(names);
Collections.copy(dest, names);

// Frequency
int count = Collections.frequency(names, "Alice");

// Disjoint (no common elements)
boolean noCommon = Collections.disjoint(list1, list2);
```

### Creating Immutable Collections

```java
// Empty collections
List<String> emptyList = Collections.emptyList();
Set<Integer> emptySet = Collections.emptySet();
Map<String, Integer> emptyMap = Collections.emptyMap();

// Single element collections
List<String> single = Collections.singletonList("only");
Set<String> singleSet = Collections.singleton("only");
Map<String, Integer> singleMap = Collections.singletonMap("key", 1);

// Unmodifiable wrappers
List<String> modifiable = new ArrayList<>(List.of("A", "B"));
List<String> unmodifiable = Collections.unmodifiableList(modifiable);
// unmodifiable.add("C");  // UnsupportedOperationException

// Thread-safe wrappers
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
```

---

## Restrictions and Limitations

### Things You Cannot Do with Generics

| Cannot Do | Why | Alternative |
|-----------|-----|-------------|
| `new T()` | Type unknown at runtime | Pass `Class<T>` and use reflection |
| `new T[]` | Array type unknown | Use `Object[]` or `ArrayList<T>` |
| `instanceof T` | Type erased | Pass `Class<T>` for checks |
| Static field of type T | Type varies per instance | Use instance field |
| Primitive types (`List<int>`) | Generics require objects | Use wrapper (`List<Integer>`) |
| Throw/catch T | Exception type erased | Use concrete exception class |

### Primitive Types and Generics

Generics do not work with primitive types:

```java
// Error: Cannot use primitive type
List<int> numbers;  // Compile error!

// Must use wrapper class
List<Integer> numbers = new ArrayList<>();

// Autoboxing handles conversion
numbers.add(5);       // int 5 autoboxed to Integer
int value = numbers.get(0);  // Integer unboxed to int
```

### Generic Exceptions

You cannot throw or catch generic types:

```java
// Error: Cannot extend Throwable with generic parameter
public class MyException<T> extends Exception {  // Compile error!
}

// Error: Cannot catch generic type
try {
    // code
} catch (T e) {  // Compile error!
}

// OK: Generic method that throws checked exception
public <E extends Exception> void mayThrow(E exception) throws E {
    throw exception;
}
```

---

## Raw Types

A raw type is a generic type used without type arguments. This is for backward compatibility but should be avoided.

```java
// Raw type (avoid!)
List rawList = new ArrayList();
rawList.add("Hello");
rawList.add(123);        // No type checking!

String s = (String) rawList.get(0);  // Manual cast required
String s2 = (String) rawList.get(1); // ClassCastException at runtime!

// Parameterized type (correct)
List<String> typedList = new ArrayList<>();
typedList.add("Hello");
// typedList.add(123);  // Compile error - type safe!

String s = typedList.get(0);  // No cast needed
```

### Warning: Mixing Raw and Parameterized Types

```java
// This compiles but is dangerous
List<String> strings = new ArrayList<>();
List rawList = strings;  // Allowed but risky

rawList.add(123);  // No compile error!

// Later...
String s = strings.get(0);  // ClassCastException!
```

**Rule:** Always use parameterized types. Never use raw types in new code.

---

## Best Practices

### 1. Use Descriptive Type Parameter Names

```java
// Good - clear what each parameter represents
public class Cache<K, V> { }
public interface Converter<Source, Target> { }
public class Pair<First, Second> { }

// Avoid single letters when meaning is not obvious
// T, E, K, V, N are OK for standard uses
```

### 2. Prefer Generic Methods Over Generic Classes

When only one method needs generics, use a generic method:

```java
// Good - only the method is generic
public class Utils {
    public static <T> List<T> toList(T[] array) { }
}

// Unnecessary - don't make the class generic
public class Utils<T> {
    public List<T> toList(T[] array) { }
}
```

### 3. Use Bounded Wildcards for Flexibility

```java
// More flexible with wildcards
public void processNumbers(List<? extends Number> numbers) { }

// Less flexible - only accepts List<Number>
public void processNumbers(List<Number> numbers) { }
```

### 4. Apply PECS Consistently

```java
// Producer - use extends
public void readFrom(Collection<? extends T> source) {
    for (T item : source) {
        process(item);
    }
}

// Consumer - use super
public void writeTo(Collection<? super T> dest) {
    dest.add(createItem());
}
```

### 5. Avoid Raw Types

```java
// Bad
List list = new ArrayList();
Map map = new HashMap();

// Good
List<String> list = new ArrayList<>();
Map<String, Integer> map = new HashMap<>();
```

### 6. Use Diamond Operator

```java
// Unnecessary repetition
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// Clean with diamond operator
Map<String, List<Integer>> map = new HashMap<>();
```

### 7. Be Careful with Type Inference

```java
// Type inference may not always give what you expect
var list = new ArrayList<>();  // ArrayList<Object>!

// Be explicit when needed
List<String> list = new ArrayList<>();  // List<String>
```

---

## Common Mistakes

### Mistake 1: Ignoring Generic Warnings

```java
// Warning: Raw type
List list = new ArrayList();  // Unchecked warning

// Fix: Add type parameter
List<String> list = new ArrayList<>();
```

### Mistake 2: Creating Generic Arrays Incorrectly

```java
// Error: Cannot create generic array
T[] array = new T[10];

// Workaround
@SuppressWarnings("unchecked")
T[] array = (T[]) new Object[10];

// Better: Use List
List<T> list = new ArrayList<>();
```

### Mistake 3: Overusing Wildcards

```java
// Too complicated
public <T> void process(List<? extends List<? extends T>> items) { }

// Simpler
public <T> void process(List<List<T>> items) { }
```

### Mistake 4: Forgetting Type Erasure

```java
// Error: Both methods have same erasure
public void process(List<String> strings) { }
public void process(List<Integer> integers) { }  // Compile error!

// After erasure, both are:
// public void process(List strings) { }

// Fix: Use different method names
public void processStrings(List<String> strings) { }
public void processIntegers(List<Integer> integers) { }
```

### Mistake 5: Assuming Generic Type at Runtime

```java
public <T> void checkType(Object obj, Class<T> type) {
    // Cannot check generic type at runtime
    // if (obj instanceof List<T>)  // Error!
    
    // Can check class
    if (type.isInstance(obj)) {
        T typed = type.cast(obj);
    }
}
```

---

## Generic Constructors

Constructors can have their own type parameters, independent of the class:

```java
public class Container<T> {
    
    private T content;
    
    // Regular constructor - uses class type parameter T
    public Container(T content) {
        this.content = content;
    }
    
    // Generic constructor - has its own type parameter U
    // U is independent of T
    public <U> Container(U source, Function<U, T> converter) {
        // Convert from U to T
        this.content = converter.apply(source);
    }
    
    public T getContent() {
        return content;
    }
}
```

Using a generic constructor:

```java
// Regular constructor
Container<String> c1 = new Container<>("Hello");

// Generic constructor - converts Integer to String
Container<String> c2 = new Container<>(42, num -> "Number: " + num);
// The constructor has U = Integer, class has T = String
// Converts Integer 42 to String "Number: 42"

// Generic constructor - converts List to String
Container<String> c3 = new Container<>(
    List.of("A", "B", "C"), 
    list -> String.join(", ", list)
);
// Result: "A, B, C"
```

### Another Example: Flexible Initialization

```java
public class Pair<K, V> {
    
    private K key;
    private V value;
    
    // Standard constructor
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    // Generic constructor that extracts key from value
    // S is the source type, KeyExtractor converts S to K
    public <S> Pair(S source, Function<S, K> keyExtractor, Function<S, V> valueExtractor) {
        this.key = keyExtractor.apply(source);
        this.value = valueExtractor.apply(source);
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}
```

```java
// Using generic constructor with a User object
User user = new User("alice", "Alice Smith");

Pair<String, String> pair = new Pair<>(
    user,
    User::getId,      // Extract key from User
    User::getName     // Extract value from User
);
// pair = ("alice", "Alice Smith")
```

---

## Varargs and Generics

When combining generics with varargs (variable arguments), you need to be aware of heap pollution and how to handle it safely.

### The Problem: Heap Pollution

Heap pollution occurs when a variable of a parameterized type refers to an object that is not of that type:

```java
// This method has a problem
public static <T> void addToList(List<T> list, T... elements) {
    for (T element : elements) {
        list.add(element);
    }
}
```

The compiler generates a warning because:
1. Varargs creates an array: `T... elements` becomes `T[] elements`
2. Generic arrays are not type-safe due to type erasure
3. At runtime, `T[]` becomes `Object[]`

### Understanding the Warning

```java
// What the compiler sees
public static <T> void addToList(List<T> list, T... elements) {
    // At runtime, elements is Object[], not T[]
    // This could lead to heap pollution
}

// Example of heap pollution
public static void dangerous(List<String>... lists) {
    // lists is actually List[] at runtime (not List<String>[])
    Object[] array = lists;  // Legal - arrays are covariant
    array[0] = List.of(42);  // Puts List<Integer> where List<String> expected
    
    String s = lists[0].get(0);  // ClassCastException!
}
```

### The @SafeVarargs Annotation

Use `@SafeVarargs` to suppress the warning when you know your method is safe:

```java
// Safe method - only reads from the varargs array
// Does not store anything in it or expose it
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    List<T> list = new ArrayList<>();
    for (T element : elements) {
        list.add(element);  // Only reading from elements
    }
    return list;
}

// Safe method - only iterates
@SafeVarargs
public static <T> void printAll(T... elements) {
    for (T element : elements) {
        System.out.println(element);
    }
}

// Safe method - passes to another safe method
@SafeVarargs
public static <T> Set<T> asSet(T... elements) {
    return new HashSet<>(asList(elements));
}
```

### Rules for @SafeVarargs

You can only use `@SafeVarargs` on:
- `static` methods
- `final` instance methods
- `private` instance methods (Java 9+)
- Constructors

```java
public class SafeContainer<T> {
    
    private List<T> items = new ArrayList<>();
    
    // OK - constructor
    @SafeVarargs
    public SafeContainer(T... elements) {
        Collections.addAll(items, elements);
    }
    
    // OK - static method
    @SafeVarargs
    public static <T> SafeContainer<T> of(T... elements) {
        return new SafeContainer<>(elements);
    }
    
    // OK - final method
    @SafeVarargs
    public final void addAll(T... elements) {
        Collections.addAll(items, elements);
    }
    
    // OK - private method (Java 9+)
    @SafeVarargs
    private void internalAdd(T... elements) {
        Collections.addAll(items, elements);
    }
    
    // ERROR - cannot use on non-final instance method
    // @SafeVarargs
    // public void add(T... elements) { }  // Compile error!
}
```

### When NOT to Use @SafeVarargs

Do not use `@SafeVarargs` if your method:
- Stores elements in a field of type `T[]`
- Returns the varargs array
- Passes the array to code that might modify it unsafely

```java
// UNSAFE - do not use @SafeVarargs here
public static <T> T[] toArray(T... elements) {
    return elements;  // Returning the varargs array is unsafe
}

// UNSAFE - storing the array
public class BadContainer<T> {
    private T[] elements;
    
    // Do NOT add @SafeVarargs - this is unsafe!
    public BadContainer(T... elements) {
        this.elements = elements;  // Storing the array is dangerous
    }
}
```

### Safe Alternative: Use Collections

```java
// Instead of varargs, use List
public static <T> List<T> combine(List<T> first, List<T> second) {
    List<T> result = new ArrayList<>(first);
    result.addAll(second);
    return result;
}

// Or use List.of() at call site
List<String> combined = combine(
    List.of("A", "B"),
    List.of("C", "D")
);
```

---

## Reifiable and Non-Reifiable Types

Understanding which types retain their type information at runtime is important for working with generics.

### Reifiable Types

A reifiable type is one whose type information is fully available at runtime:

```java
// Reifiable types - full type info at runtime
int                    // Primitives
String                 // Non-generic classes
List                   // Raw types
List<?>                // Unbounded wildcard
Map<?, ?>              // Unbounded wildcards
int[]                  // Arrays of primitives
String[]               // Arrays of reifiable types
```

### Non-Reifiable Types

A non-reifiable type loses type information at runtime due to type erasure:

```java
// Non-reifiable types - type info lost at runtime
List<String>           // Parameterized type
List<Integer>          // Parameterized type
Map<String, Integer>   // Parameterized type
List<? extends Number> // Bounded wildcard
T                      // Type parameter
```

### Why This Matters

You cannot use non-reifiable types in certain contexts:

```java
// Cannot create arrays of non-reifiable types
// List<String>[] arrays = new List<String>[10];  // Error!

// Can create array of raw type or unbounded wildcard
List<?>[] arrays = new List<?>[10];  // OK
List[] rawArrays = new List[10];     // OK but warning

// Cannot use instanceof with parameterized type
Object obj = new ArrayList<String>();
// if (obj instanceof List<String>) { }  // Error!
if (obj instanceof List<?>) { }          // OK
if (obj instanceof List) { }             // OK but loses type info
```

### Practical Implications

```java
public class TypeChecker {
    
    // Cannot check generic type at runtime
    public static <T> boolean isListOf(Object obj, Class<T> elementType) {
        if (!(obj instanceof List<?>)) {
            return false;
        }
        
        // Must check elements individually
        List<?> list = (List<?>) obj;
        for (Object element : list) {
            if (element != null && !elementType.isInstance(element)) {
                return false;
            }
        }
        return true;
    }
}
```

```java
List<String> strings = List.of("A", "B", "C");
List<Integer> integers = List.of(1, 2, 3);

boolean isStrings = TypeChecker.isListOf(strings, String.class);   // true
boolean isIntegers = TypeChecker.isListOf(integers, Integer.class); // true
boolean wrong = TypeChecker.isListOf(strings, Integer.class);       // false
```

---

## Bridge Methods

Bridge methods are synthetic methods generated by the compiler to preserve polymorphism with generics. Understanding them helps when debugging or using reflection.

### Why Bridge Methods Exist

```java
// Generic interface
public interface Comparable<T> {
    int compareTo(T o);
}

// Implementation
public class Age implements Comparable<Age> {
    private int years;
    
    public Age(int years) {
        this.years = years;
    }
    
    @Override
    public int compareTo(Age other) {
        return Integer.compare(this.years, other.years);
    }
}
```

After type erasure, `Comparable<Age>` becomes just `Comparable`, and `compareTo(Age)` should be `compareTo(Object)`. The compiler generates a bridge method:

```java
// What the compiler generates (conceptually)
public class Age implements Comparable {
    
    // Your method
    public int compareTo(Age other) {
        return Integer.compare(this.years, other.years);
    }
    
    // Bridge method generated by compiler
    // Marked as synthetic and bridge
    public int compareTo(Object other) {
        return compareTo((Age) other);  // Delegates to typed method
    }
}
```

### Seeing Bridge Methods

```java
// Using reflection to see bridge methods
for (Method method : Age.class.getDeclaredMethods()) {
    if (method.isBridge()) {
        System.out.println("Bridge: " + method);
    }
}
// Output: Bridge: public int Age.compareTo(java.lang.Object)
```

### When Bridge Methods Matter

Bridge methods usually work transparently. You might encounter them when:
- Using reflection
- Debugging stack traces
- Working with bytecode

```java
// Stack trace might show bridge method
Age a1 = new Age(25);
Object obj = "not an Age";

// This calls the bridge method, which casts and throws
a1.compareTo(obj);  // ClassCastException in bridge method
```

---

## Generics with Records (Java 16+)

Records can be generic, providing a concise way to create immutable data carriers with type safety.

### Basic Generic Record

```java
// A generic record with two type parameters
public record Pair<K, V>(K key, V value) {
    // Compact canonical constructor for validation
    public Pair {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
    }
}
```

Using the generic record:

```java
// Type-safe pairs
Pair<String, Integer> age = new Pair<>("Alice", 30);
String name = age.key();      // "Alice"
Integer years = age.value();  // 30

Pair<Integer, String> employee = new Pair<>(101, "Bob");

// Pattern matching with records (Java 21+)
if (age instanceof Pair<String, Integer>(String n, Integer a)) {
    System.out.println(n + " is " + a + " years old");
}
```

### Generic Record with Bounds

```java
// Record with bounded type parameter
public record NumberRange<T extends Number & Comparable<T>>(T min, T max) {
    
    public NumberRange {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }
    }
    
    public boolean contains(T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
    
    public double span() {
        return max.doubleValue() - min.doubleValue();
    }
}
```

```java
NumberRange<Integer> intRange = new NumberRange<>(1, 10);
boolean contains5 = intRange.contains(5);  // true
double span = intRange.span();             // 9.0

NumberRange<Double> doubleRange = new NumberRange<>(0.0, 1.0);
boolean containsHalf = doubleRange.contains(0.5);  // true
```

### Generic Record as Return Type

```java
// Result type that can hold success value or error
public record Result<T>(T value, String error) {
    
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }
    
    public static <T> Result<T> failure(String error) {
        return new Result<>(null, error);
    }
    
    public boolean isSuccess() {
        return error == null;
    }
    
    public T orElse(T defaultValue) {
        return isSuccess() ? value : defaultValue;
    }
}
```

```java
public Result<User> findUser(String id) {
    User user = database.find(id);
    if (user != null) {
        return Result.success(user);
    }
    return Result.failure("User not found: " + id);
}

// Usage
Result<User> result = findUser("alice");
if (result.isSuccess()) {
    User user = result.value();
    System.out.println("Found: " + user.getName());
} else {
    System.out.println("Error: " + result.error());
}
```

### Record with Nested Generics

```java
// A tree node record
public record TreeNode<T>(T value, List<TreeNode<T>> children) {
    
    public TreeNode(T value) {
        this(value, List.of());
    }
    
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    public int size() {
        return 1 + children.stream()
            .mapToInt(TreeNode::size)
            .sum();
    }
}
```

```java
// Build a tree
TreeNode<String> leaf1 = new TreeNode<>("Leaf1");
TreeNode<String> leaf2 = new TreeNode<>("Leaf2");
TreeNode<String> parent = new TreeNode<>("Parent", List.of(leaf1, leaf2));
TreeNode<String> root = new TreeNode<>("Root", List.of(parent));

int totalNodes = root.size();  // 4
```

---

## Type Inference

Java's type inference has improved over versions. Understanding it helps write cleaner code.

### Constructor Type Inference (Diamond Operator)

```java
// Java 7+ - diamond operator
List<String> list = new ArrayList<>();
Map<String, List<Integer>> map = new HashMap<>();

// Nested generics
Pair<String, Pair<Integer, Double>> nested = new Pair<>("key", new Pair<>(1, 2.0));
```

### Method Type Inference

```java
// Compiler infers type from argument
List<String> names = Arrays.asList("Alice", "Bob");  // Infers String

// Compiler infers type from assignment
List<Integer> numbers = Collections.emptyList();  // Infers Integer

// Chained calls
List<String> result = Stream.of("a", "b", "c")
    .map(String::toUpperCase)  // Infers String
    .collect(Collectors.toList());  // Infers List<String>
```

### Target Type Inference (Java 8+)

The compiler uses the target type (what you're assigning to) to infer types:

```java
// Target type inference in lambdas
Comparator<String> comp = (a, b) -> a.length() - b.length();
// Compiler knows a and b are String from Comparator<String>

// Target type in method references
Function<String, Integer> len = String::length;
// Compiler knows input is String, output is Integer

// In method calls
processItems(Collections.emptyList());  // Infers from parameter type

public void processItems(List<String> items) { }
```

### Generic Method Type Inference

```java
public static <T> T firstOrDefault(List<T> list, T defaultValue) {
    return list.isEmpty() ? defaultValue : list.get(0);
}

// Inferred from arguments
String first = firstOrDefault(List.of("A", "B"), "default");

// Inferred from context
Integer number = firstOrDefault(List.of(), 0);

// Explicit when needed (rare)
Number n = GenericUtils.<Number>firstOrDefault(List.of(1, 2), 0);
```

### var and Generics

```java
// var with diamond - infers Object!
var list1 = new ArrayList<>();     // ArrayList<Object> - usually not what you want

// var with explicit type on right
var list2 = new ArrayList<String>();  // ArrayList<String>

// var with factory methods
var list3 = List.of("A", "B", "C");   // List<String>

// var preserves inferred type
var numbers = Arrays.asList(1, 2, 3);  // List<Integer>

// Be careful with var and wildcards
List<?> wildcard = getWildcardList();
var v = wildcard;  // Type is List<?> - might not be what you expect
```

### Intersection Type Inference

```java
// Compiler can infer intersection types
var random = new Random();

// result is Serializable & Comparable<...>
var result = random.nextBoolean() ? "hello" : 42;
// Common interface/class is inferred

// With explicit type
Serializable s = random.nextBoolean() ? "hello" : 42;  // OK
Comparable<?> c = random.nextBoolean() ? "hello" : 42; // OK
```

---

## Cheat Sheet

| Concept | Syntax | Example |
|---------|--------|---------|
| Generic class | `class Name<T>` | `class Box<T> { T item; }` |
| Generic method | `<T> ReturnType name()` | `<T> List<T> toList(T[] arr)` |
| Multiple parameters | `<T, U>` | `class Pair<K, V>` |
| Upper bound | `<T extends Type>` | `<T extends Number>` |
| Multiple bounds | `<T extends A & B>` | `<T extends Number & Comparable<T>>` |
| Unbounded wildcard | `<?>` | `List<?> list` |
| Upper bounded wildcard | `<? extends Type>` | `List<? extends Number>` |
| Lower bounded wildcard | `<? super Type>` | `List<? super Integer>` |
| Diamond operator | `<>` | `new ArrayList<>()` |

### PECS Summary

| Use | When | Can Do |
|-----|------|--------|
| `? extends T` | Producer (read) | Read as T |
| `? super T` | Consumer (write) | Write T |
| No wildcard | Both | Read and write |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Collections - Queues](./20-collections-queues.md) | [Guide](../guide.md) | [Enums](./22-enums.md) |
