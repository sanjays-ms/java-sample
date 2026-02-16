# Generics Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/21-generics.md)

---

## Quick Reference

| Syntax | Meaning | Example |
|--------|---------|---------|
| `<T>` | Type parameter | `class Box<T>` |
| `<T, U>` | Multiple parameters | `class Pair<K, V>` |
| `<T extends X>` | Upper bound | `<T extends Number>` |
| `<T extends X & Y>` | Multiple bounds | `<T extends Number & Comparable<T>>` |
| `<?>` | Unknown type | `List<?>` |
| `<? extends T>` | T or subtype | `List<? extends Number>` |
| `<? super T>` | T or supertype | `List<? super Integer>` |
| `<>` | Diamond operator | `new ArrayList<>()` |

---

## Type Parameter Naming

| Letter | Meaning | Usage |
|--------|---------|-------|
| `T` | Type | General purpose |
| `E` | Element | Collections |
| `K` | Key | Maps |
| `V` | Value | Maps |
| `N` | Number | Numeric types |
| `S`, `U` | Additional types | Multiple parameters |

---

## Generic Class

```java
// Define
public class Box<T> {
    private T content;
    
    public Box(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}

// Use
Box<String> stringBox = new Box<>("Hello");
Box<Integer> intBox = new Box<>(42);
String s = stringBox.getContent();  // No cast needed
```

---

## Multiple Type Parameters

```java
// Define
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}

// Use
Pair<String, Integer> pair = new Pair<>("age", 30);
String key = pair.getKey();      // String
Integer value = pair.getValue(); // Integer
```

---

## Generic Method

```java
// Define - <T> before return type
public static <T> List<T> arrayToList(T[] array) {
    List<T> list = new ArrayList<>();
    for (T element : array) {
        list.add(element);
    }
    return list;
}

// Use - type inferred from argument
String[] names = {"Alice", "Bob"};
List<String> nameList = arrayToList(names);

Integer[] nums = {1, 2, 3};
List<Integer> numList = arrayToList(nums);
```

---

## Generic Interface

```java
// Define
public interface Container<T> {
    void add(T item);
    T get(int index);
}

// Implement (keep generic)
public class ListContainer<T> implements Container<T> {
    private List<T> items = new ArrayList<>();
    
    @Override
    public void add(T item) { items.add(item); }
    
    @Override
    public T get(int index) { return items.get(index); }
}

// Implement (fix type)
public class StringContainer implements Container<String> {
    // T is now String everywhere
}
```

---

## Bounded Type Parameters

```java
// Upper bound - T must extend Number
public class NumberBox<T extends Number> {
    private T value;
    
    public double doubleValue() {
        return value.doubleValue();  // Number method available
    }
}

NumberBox<Integer> intBox = new NumberBox<>();  // OK
NumberBox<Double> dblBox = new NumberBox<>();   // OK
// NumberBox<String> strBox;  // Error! String not a Number

// Multiple bounds - class first, then interfaces
public class MyClass<T extends Number & Comparable<T>> {
    // Can use Number and Comparable methods
}
```

---

## Bounded Generic Methods

```java
// Find max - T must be Comparable
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Sum numbers - T must be Number
public static <T extends Number> double sum(List<T> numbers) {
    double total = 0;
    for (T n : numbers) {
        total += n.doubleValue();
    }
    return total;
}
```

---

## Wildcards

### Unbounded: `<?>`

```java
// Accept any type
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

printList(List.of("A", "B"));  // OK
printList(List.of(1, 2, 3));   // OK
```

### Upper Bounded: `<? extends T>`

```java
// Accept T or subtypes - for READING
public static double sum(List<? extends Number> numbers) {
    double total = 0;
    for (Number n : numbers) {
        total += n.doubleValue();
    }
    return total;
}

sum(List.of(1, 2, 3));       // List<Integer> - OK
sum(List.of(1.5, 2.5));      // List<Double> - OK
```

### Lower Bounded: `<? super T>`

```java
// Accept T or supertypes - for WRITING
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

List<Integer> ints = new ArrayList<>();
List<Number> nums = new ArrayList<>();
List<Object> objs = new ArrayList<>();

addNumbers(ints);  // OK
addNumbers(nums);  // OK
addNumbers(objs);  // OK
```

---

## PECS Rule

**P**roducer **E**xtends, **C**onsumer **S**uper

| Operation | Wildcard | Can Do |
|-----------|----------|--------|
| Read from | `? extends T` | Read as T |
| Write to | `? super T` | Write T and subtypes |
| Both | No wildcard | Read and write T |

```java
// Copy: src produces, dest consumes
public static <T> void copy(
    List<? extends T> src,   // Producer - extends
    List<? super T> dest     // Consumer - super
) {
    for (T item : src) {
        dest.add(item);
    }
}
```

---

## Diamond Operator

```java
// Before Java 7
Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

// Java 7+ - compiler infers type
Map<String, List<Integer>> map = new HashMap<>();
List<String> list = new ArrayList<>();
Set<Integer> set = new HashSet<>();
```

---

## Type Erasure

At runtime, generic types are erased:

| Compile Time | Runtime |
|--------------|---------|
| `Box<String>` | `Box` |
| `List<Integer>` | `List` |
| `T` | `Object` |
| `T extends Number` | `Number` |

### Consequences

```java
// Cannot create generic array
T[] array = new T[10];  // Error!

// Cannot use instanceof with generic
if (obj instanceof List<String>) { }  // Error!

// Cannot create instance of type parameter
T item = new T();  // Error!

// Cannot use T in static context
static T field;  // Error!
```

---

## Common Patterns

### Generic Factory

```java
public interface Factory<T> {
    T create();
}

Factory<User> userFactory = () -> new User();
User user = userFactory.create();
```

### Generic Repository

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
}
```

### Generic Converter

```java
public interface Converter<S, T> {
    T convert(S source);
}

Converter<String, Integer> toInt = Integer::parseInt;
Integer num = toInt.convert("42");
```

---

## Restrictions

| Cannot Do | Reason | Alternative |
|-----------|--------|-------------|
| `new T()` | Type unknown | Pass `Class<T>`, use reflection |
| `new T[]` | Array type unknown | Use `ArrayList<T>` |
| `instanceof T` | Type erased | Pass `Class<T>` |
| `List<int>` | No primitives | Use `List<Integer>` |
| Static field `T` | Type varies | Use instance field |
| `catch(T e)` | Exception erased | Use concrete exception |

---

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Raw types | No type safety | Always use `<Type>` |
| Ignoring warnings | Runtime errors | Fix all generic warnings |
| Same erasure methods | Compile error | Use different names |
| Generic arrays | Not allowed | Use `ArrayList<T>` |
| Assuming runtime type | Erased | Pass `Class<T>` |

### Same Erasure Example

```java
// Error: Same erasure
void process(List<String> s) { }
void process(List<Integer> i) { }  // Error!

// Fix: Different names
void processStrings(List<String> s) { }
void processIntegers(List<Integer> i) { }
```

---

## Best Practices

```java
// 1. Always use parameterized types
List<String> list = new ArrayList<>();  // Not List list

// 2. Use diamond operator
Map<String, Integer> map = new HashMap<>();

// 3. Apply PECS
void read(List<? extends T> producer) { }
void write(List<? super T> consumer) { }

// 4. Prefer generic methods over generic classes
public static <T> T first(List<T> list) { }

// 5. Use meaningful names for complex generics
interface Converter<Source, Target> { }

// 6. Suppress warnings only when safe
@SuppressWarnings("unchecked")
T[] array = (T[]) new Object[size];
```

---

## Generic Constructors

```java
public class Container<T> {
    private T content;
    
    // Regular constructor
    public Container(T content) {
        this.content = content;
    }
    
    // Generic constructor with its own type parameter U
    public <U> Container(U source, Function<U, T> converter) {
        this.content = converter.apply(source);
    }
}

// Usage
Container<String> c1 = new Container<>("Hello");
Container<String> c2 = new Container<>(42, num -> "Number: " + num);
```

---

## Varargs and @SafeVarargs

```java
// Safe - only reads from varargs
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    List<T> list = new ArrayList<>();
    for (T e : elements) list.add(e);
    return list;
}

// @SafeVarargs allowed on:
// - static methods
// - final methods
// - private methods (Java 9+)
// - constructors
```

| Safe | Unsafe |
|------|--------|
| Reading from array | Returning the array |
| Iterating elements | Storing array in field |
| Passing to safe method | Exposing array reference |

---

## Reifiable vs Non-Reifiable

| Reifiable (runtime info kept) | Non-Reifiable (erased) |
|-------------------------------|------------------------|
| `int`, `String` | `List<String>` |
| `List` (raw) | `Map<K, V>` |
| `List<?>` | `T` |
| `String[]` | `List<? extends Number>` |

```java
// Non-reifiable limitations
// new List<String>[10];          // Error!
// obj instanceof List<String>    // Error!

// OK alternatives
new List<?>[10];                   // OK
obj instanceof List<?>             // OK
```

---

## Generic Records (Java 16+)

```java
// Generic record
public record Pair<K, V>(K key, V value) { }

// With bounds
public record Range<T extends Comparable<T>>(T min, T max) {
    public Range {
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException();
    }
}

// Usage
Pair<String, Integer> p = new Pair<>("age", 30);
Range<Integer> r = new Range<>(1, 10);
```

---

## Type Inference

```java
// Diamond operator (Java 7+)
List<String> list = new ArrayList<>();

// Method type inference
List<String> names = Collections.emptyList();

// Target type inference (Java 8+)
Comparator<String> comp = (a, b) -> a.length() - b.length();

// var with generics
var list = new ArrayList<String>();  // ArrayList<String>
var list = new ArrayList<>();        // ArrayList<Object> - careful!
```

---

## Quick Examples

```java
// Generic class
Box<String> box = new Box<>("Hello");

// Generic method
List<Integer> list = arrayToList(new Integer[]{1, 2, 3});

// Bounded type
<T extends Comparable<T>> T max(T a, T b)

// Wildcard - read any
void print(List<?> list)

// Wildcard - read Numbers
double sum(List<? extends Number> nums)

// Wildcard - write Integers
void add(List<? super Integer> list)

// PECS
<T> void copy(List<? extends T> src, List<? super T> dest)
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/21-generics.md)
