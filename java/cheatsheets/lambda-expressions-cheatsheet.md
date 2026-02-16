# Lambda Expressions Cheatsheet

[Back to Full Documentation](../documentation/24-lambda-expressions.md)

---

## Lambda Syntax

| Form | Syntax | Example |
|------|--------|---------|
| No parameters | `() -> expr` | `() -> "Hello"` |
| One parameter | `x -> expr` | `x -> x * 2` |
| One with parens | `(x) -> expr` | `(x) -> x * 2` |
| Multiple params | `(x, y) -> expr` | `(x, y) -> x + y` |
| With types | `(Type x) -> expr` | `(String s) -> s.length()` |
| Block body | `(x) -> { stmts; }` | `(x) -> { return x * 2; }` |

---

## Core Functional Interfaces

| Interface | Method | Takes | Returns | Example |
|-----------|--------|-------|---------|---------|
| `Predicate<T>` | `test(T)` | T | boolean | `s -> s.isEmpty()` |
| `Function<T,R>` | `apply(T)` | T | R | `s -> s.length()` |
| `Consumer<T>` | `accept(T)` | T | void | `s -> System.out.println(s)` |
| `Supplier<T>` | `get()` | nothing | T | `() -> new ArrayList<>()` |
| `UnaryOperator<T>` | `apply(T)` | T | T | `s -> s.toUpperCase()` |
| `BinaryOperator<T>` | `apply(T,T)` | T, T | T | `(a, b) -> a + b` |

---

## Two-Argument Interfaces

| Interface | Method | Takes | Returns | Example |
|-----------|--------|-------|---------|---------|
| `BiPredicate<T,U>` | `test(T,U)` | T, U | boolean | `(s, n) -> s.length() > n` |
| `BiFunction<T,U,R>` | `apply(T,U)` | T, U | R | `(a, b) -> a + b` |
| `BiConsumer<T,U>` | `accept(T,U)` | T, U | void | `(k, v) -> map.put(k, v)` |

---

## Primitive Specializations

| Base Interface | int | long | double |
|---------------|-----|------|--------|
| Predicate | `IntPredicate` | `LongPredicate` | `DoublePredicate` |
| Function | `IntFunction<R>` | `LongFunction<R>` | `DoubleFunction<R>` |
| Consumer | `IntConsumer` | `LongConsumer` | `DoubleConsumer` |
| Supplier | `IntSupplier` | `LongSupplier` | `DoubleSupplier` |
| UnaryOperator | `IntUnaryOperator` | `LongUnaryOperator` | `DoubleUnaryOperator` |
| BinaryOperator | `IntBinaryOperator` | `LongBinaryOperator` | `DoubleBinaryOperator` |

**Conversion functions:**

| Interface | Takes | Returns | Example |
|-----------|-------|---------|---------|
| `ToIntFunction<T>` | T | int | `s -> s.length()` |
| `ToLongFunction<T>` | T | long | `s -> s.hashCode()` |
| `ToDoubleFunction<T>` | T | double | `n -> n * 1.5` |
| `IntToLongFunction` | int | long | `n -> (long)n` |
| `IntToDoubleFunction` | int | double | `n -> n / 2.0` |

---

## Method References

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static method | `Math::abs` | `x -> Math.abs(x)` |
| Bound instance | `str::length` | `() -> str.length()` |
| Unbound instance | `String::length` | `s -> s.length()` |
| Constructor | `ArrayList::new` | `() -> new ArrayList<>()` |
| Array constructor | `String[]::new` | `n -> new String[n]` |

### Common Method References

```java
System.out::println      // Consumer<T>
String::toUpperCase      // Function<String, String>
String::length           // ToIntFunction<String>
Integer::parseInt        // Function<String, Integer>
String::isEmpty          // Predicate<String>
ArrayList::new           // Supplier<List<T>>
```

---

## Composing Functions

### Predicate

```java
Predicate<String> isLong = s -> s.length() > 5;
Predicate<String> startsWithA = s -> s.startsWith("A");

isLong.and(startsWithA)   // Both true
isLong.or(startsWithA)    // Either true
isLong.negate()           // Opposite
```

### Function

```java
Function<String, String> trim = String::trim;
Function<String, String> upper = String::toUpperCase;

trim.andThen(upper)       // trim first, then upper
upper.compose(trim)       // trim first, then upper (same result)
```

### Consumer

```java
Consumer<String> print = System.out::println;
Consumer<String> log = logger::info;

print.andThen(log)        // print then log
```

---

## Stream Operations

| Operation | Type | Lambda |
|-----------|------|--------|
| `filter()` | Predicate | `s -> s.length() > 3` |
| `map()` | Function | `s -> s.toUpperCase()` |
| `flatMap()` | Function returning Stream | `s -> s.chars().boxed()` |
| `forEach()` | Consumer | `s -> System.out.println(s)` |
| `reduce()` | BinaryOperator | `(a, b) -> a + b` |
| `sorted()` | Comparator | `(a, b) -> a.compareTo(b)` |
| `anyMatch()` | Predicate | `s -> s.isEmpty()` |
| `allMatch()` | Predicate | `s -> s.length() > 0` |
| `noneMatch()` | Predicate | `s -> s.isEmpty()` |

---

## Comparators with Lambdas

```java
// Basic
(a, b) -> a.compareTo(b)

// By field
Comparator.comparing(Person::getName)
Comparator.comparingInt(Person::getAge)

// Multiple fields
Comparator.comparing(Person::getLastName)
    .thenComparing(Person::getFirstName)

// Reversed
Comparator.comparing(Person::getAge).reversed()

// Null handling
Comparator.nullsFirst(Comparator.comparing(Person::getName))
Comparator.nullsLast(Comparator.naturalOrder())
```

---

## Variable Capture Rules

| Variable Type | Can Capture? | Can Modify? |
|--------------|--------------|-------------|
| Local variable | Yes | No (must be effectively final) |
| Parameter | Yes | No (must be effectively final) |
| Instance field | Yes | Yes |
| Static field | Yes | Yes |

```java
// Effectively final - OK
String prefix = "Hello";
Consumer<String> c = s -> System.out.println(prefix + s);

// Not effectively final - ERROR
String prefix = "Hello";
prefix = "Hi";  // Now not effectively final
Consumer<String> c = s -> System.out.println(prefix + s); // Error!

// Workaround: Use wrapper
int[] counter = {0};
Runnable r = () -> counter[0]++;  // OK - array reference is final
```

---

## Quick Patterns

### Event Handling

```java
button.addActionListener(e -> handleClick());
```

### Threading

```java
new Thread(() -> doWork()).start();
executor.submit(() -> compute());
```

### Optional

```java
optional.map(String::toUpperCase)
        .filter(s -> s.length() > 3)
        .ifPresent(System.out::println);
```

### Collection Operations

```java
list.removeIf(s -> s.isEmpty());
list.replaceAll(String::toUpperCase);
list.sort(Comparator.naturalOrder());
map.forEach((k, v) -> System.out.println(k + "=" + v));
map.computeIfAbsent(key, k -> new ArrayList<>());
```

---

## Handling Exceptions

```java
// Wrap checked exception
Function<String, String> readFile = path -> {
    try {
        return Files.readString(Path.of(path));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
};

// Custom throwing interface
@FunctionalInterface
interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;
}
```

---

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `x -> { x.toUpperCase(); }` | Missing return | `x -> { return x.toUpperCase(); }` or `x -> x.toUpperCase()` |
| `var -> var.length()` | `var` is reserved | Use different name: `s -> s.length()` |
| `(a, b) -> a++` | Modifying param | `(a, b) -> a + 1` |
| Capturing non-final local | Compile error | Make variable final or use wrapper |
| Too complex lambda | Hard to read/debug | Extract to named method |

---

## Best Practices

| Do | Avoid |
|----|-------|
| Keep lambdas short (1-2 lines) | Complex multi-line lambdas |
| Use method references when possible | `s -> s.toString()` when `Object::toString` works |
| Use standard functional interfaces | Creating custom when standard exists |
| Name complex lambdas | Anonymous complex logic |
| Extract to methods if > 3 lines | Long inline lambdas |
| Avoid side effects in lambdas | Modifying external state |
