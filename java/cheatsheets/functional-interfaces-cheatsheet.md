# Functional Interfaces Cheatsheet

[Back to Full Documentation](../documentation/25-functional-interfaces.md)

---

## Core Functional Interfaces

| Interface | Method | Input | Output | Example |
|-----------|--------|-------|--------|---------|
| `Predicate<T>` | `test(T)` | T | boolean | `s -> s.isEmpty()` |
| `Function<T,R>` | `apply(T)` | T | R | `s -> s.length()` |
| `Consumer<T>` | `accept(T)` | T | void | `s -> System.out.println(s)` |
| `Supplier<T>` | `get()` | none | T | `() -> new ArrayList<>()` |
| `UnaryOperator<T>` | `apply(T)` | T | T | `s -> s.toUpperCase()` |
| `BinaryOperator<T>` | `apply(T,T)` | T, T | T | `(a,b) -> a + b` |

---

## Two-Argument Interfaces

| Interface | Method | Input | Output | Example |
|-----------|--------|-------|--------|---------|
| `BiPredicate<T,U>` | `test(T,U)` | T, U | boolean | `(s,n) -> s.length() > n` |
| `BiFunction<T,U,R>` | `apply(T,U)` | T, U | R | `(a,b) -> a + b` |
| `BiConsumer<T,U>` | `accept(T,U)` | T, U | void | `(k,v) -> map.put(k,v)` |

---

## Primitive Specializations

### Predicates

| Interface | Input | Method |
|-----------|-------|--------|
| `IntPredicate` | int | `test(int)` |
| `LongPredicate` | long | `test(long)` |
| `DoublePredicate` | double | `test(double)` |

### Functions (T -> primitive)

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `ToIntFunction<T>` | T | int | `applyAsInt(T)` |
| `ToLongFunction<T>` | T | long | `applyAsLong(T)` |
| `ToDoubleFunction<T>` | T | double | `applyAsDouble(T)` |

### Functions (primitive -> R)

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `IntFunction<R>` | int | R | `apply(int)` |
| `LongFunction<R>` | long | R | `apply(long)` |
| `DoubleFunction<R>` | double | R | `apply(double)` |

### Operators

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `IntUnaryOperator` | int | int | `applyAsInt(int)` |
| `LongUnaryOperator` | long | long | `applyAsLong(long)` |
| `DoubleUnaryOperator` | double | double | `applyAsDouble(double)` |
| `IntBinaryOperator` | int, int | int | `applyAsInt(int,int)` |
| `LongBinaryOperator` | long, long | long | `applyAsLong(long,long)` |
| `DoubleBinaryOperator` | double, double | double | `applyAsDouble(double,double)` |

### Consumers

| Interface | Input | Method |
|-----------|-------|--------|
| `IntConsumer` | int | `accept(int)` |
| `LongConsumer` | long | `accept(long)` |
| `DoubleConsumer` | double | `accept(double)` |

### Suppliers

| Interface | Output | Method |
|-----------|--------|--------|
| `IntSupplier` | int | `getAsInt()` |
| `LongSupplier` | long | `getAsLong()` |
| `DoubleSupplier` | double | `getAsDouble()` |
| `BooleanSupplier` | boolean | `getAsBoolean()` |

---

## Composition Methods

### Predicate

```java
predicate.and(other)      // Both must be true
predicate.or(other)       // Either must be true
predicate.negate()        // Opposite result
Predicate.not(predicate)  // Static negation
Predicate.isEqual(value)  // Equals check
```

### Function

```java
function.andThen(after)   // Apply this, then after
function.compose(before)  // Apply before, then this
Function.identity()       // Returns input unchanged
```

### Consumer

```java
consumer.andThen(after)   // Accept this, then after
```

### UnaryOperator

```java
UnaryOperator.identity()  // Returns input unchanged
```

### BinaryOperator

```java
BinaryOperator.minBy(comparator)  // Returns minimum
BinaryOperator.maxBy(comparator)  // Returns maximum
```

---

## Quick Reference: What to Use When

| I want to... | Use |
|--------------|-----|
| Check a condition | `Predicate<T>` |
| Transform a value | `Function<T,R>` |
| Do something with a value | `Consumer<T>` |
| Create/supply a value | `Supplier<T>` |
| Transform to same type | `UnaryOperator<T>` |
| Combine two values | `BinaryOperator<T>` |
| Check with two values | `BiPredicate<T,U>` |
| Transform two values | `BiFunction<T,U,R>` |
| Process two values | `BiConsumer<T,U>` |

---

## Common Patterns

### Validation Chain

```java
Predicate<String> notNull = s -> s != null;
Predicate<String> notEmpty = s -> !s.isEmpty();
Predicate<String> minLength = s -> s.length() >= 8;

Predicate<String> valid = notNull.and(notEmpty).and(minLength);
```

### Transformation Pipeline

```java
Function<String, String> trim = String::trim;
Function<String, String> lower = String::toLowerCase;

Function<String, String> normalize = trim.andThen(lower);
```

### Action Chain

```java
Consumer<Order> validate = this::validateOrder;
Consumer<Order> save = this::saveOrder;
Consumer<Order> notify = this::notifyCustomer;

Consumer<Order> process = validate.andThen(save).andThen(notify);
```

---

## JDK Functional Interfaces

| Interface | Package | Signature |
|-----------|---------|-----------|
| `Runnable` | java.lang | `() -> void` |
| `Callable<V>` | java.util.concurrent | `() -> V throws Exception` |
| `Comparator<T>` | java.util | `(T,T) -> int` |
| `ActionListener` | java.awt.event | `(ActionEvent) -> void` |
| `FileFilter` | java.io | `(File) -> boolean` |

---

## Creating Custom Interfaces

```java
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
```

---

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `Consumer<String> c = s -> s.length()` | Return value ignored | Use `Function` if you need result |
| `Function<String, String> f = s -> { s.toUpperCase(); }` | Missing return | Add `return` or use expression form |
| Multiple abstract methods | Not a functional interface | Keep only one abstract method |
| Not handling nulls | NullPointerException | Add null checks in predicates |

---

## Best Practices

| Do | Avoid |
|----|-------|
| Use `@FunctionalInterface` annotation | Leaving interface unprotected |
| Use standard interfaces | Creating duplicates |
| Use primitive specializations | Boxing with generics |
| Keep functions pure | Side effects in functions |
| Use method references | Verbose lambdas |
