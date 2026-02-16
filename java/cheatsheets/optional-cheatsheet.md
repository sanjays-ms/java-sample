# Optional Cheatsheet

[Back to Full Documentation](../documentation/29-optional.md)

---

## Creating Optional

| Goal | Method | Example |
|------|--------|---------|
| Wrap a non-null value | `Optional.of(value)` | `Optional.of("hello")` |
| Wrap a possibly-null value | `Optional.ofNullable(value)` | `Optional.ofNullable(getName())` |
| Create empty Optional | `Optional.empty()` | `Optional.empty()` |

```java
Optional<String> present = Optional.of("hello");
Optional<String> maybe = Optional.ofNullable(null);  // empty
Optional<String> empty = Optional.empty();
```

---

## Checking for Value

| Goal | Method | Returns |
|------|--------|---------|
| Check if value exists | `isPresent()` | `boolean` |
| Check if empty | `isEmpty()` | `boolean` |
| Run action if present | `ifPresent(Consumer)` | `void` |
| Run action or else | `ifPresentOrElse(Consumer, Runnable)` | `void` |

```java
if (opt.isPresent()) { /* has value */ }
if (opt.isEmpty()) { /* no value */ }

opt.ifPresent(v -> System.out.println(v));
opt.ifPresentOrElse(
    v -> use(v),
    () -> handleMissing()
);
```

---

## Getting Values

| Goal | Method | When to Use |
|------|--------|-------------|
| Get or throw | `get()` | Only after `isPresent()` check |
| Get with fallback | `orElse(default)` | Constant default value |
| Get with lazy fallback | `orElseGet(Supplier)` | Computed default value |
| Get or throw custom | `orElseThrow(Supplier)` | Custom exception needed |

```java
String value = opt.get();                      // Risky - throws if empty
String value = opt.orElse("default");          // Returns "default" if empty
String value = opt.orElseGet(() -> compute()); // Lazy computation
String value = opt.orElseThrow(() -> new NotFoundException());
```

---

## Transforming Values

| Goal | Method | Description |
|------|--------|-------------|
| Transform value | `map(Function)` | Apply function to value |
| Flatten nested Optional | `flatMap(Function)` | When function returns Optional |
| Filter by condition | `filter(Predicate)` | Keep if matches, empty if not |

```java
// map: transform the value
opt.map(String::toUpperCase)       // Optional<String>
opt.map(String::length)            // Optional<Integer>

// flatMap: avoid Optional<Optional<T>>
opt.flatMap(this::findById)        // When findById returns Optional

// filter: keep if matches
opt.filter(s -> s.length() > 5)    // Empty if too short
```

---

## Chaining Operations

```java
Optional<String> result = user
    .map(User::getAddress)
    .map(Address::getCity)
    .map(City::getName)
    .filter(name -> !name.isEmpty())
    .map(String::toUpperCase);
```

---

## Converting to Stream

```java
Optional<String> opt = Optional.of("hello");
Stream<String> stream = opt.stream();  // Stream with 0 or 1 element

// Useful for filtering empty optionals
list.stream()
    .map(this::findById)      // Stream<Optional<T>>
    .flatMap(Optional::stream) // Stream<T> - empties removed
    .collect(Collectors.toList());
```

---

## Primitive Optionals

| Type | Class | No Auto-boxing |
|------|-------|----------------|
| int | `OptionalInt` | `getAsInt()`, `orElse(0)` |
| long | `OptionalLong` | `getAsLong()`, `orElse(0L)` |
| double | `OptionalDouble` | `getAsDouble()`, `orElse(0.0)` |

```java
OptionalInt optInt = OptionalInt.of(42);
int value = optInt.orElse(0);

OptionalDouble optDouble = OptionalDouble.empty();
double value = optDouble.orElse(0.0);
```

---

## Quick Patterns

### Safe Property Access
```java
String city = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

### First Non-Null
```java
String value = opt1
    .or(() -> opt2)
    .or(() -> opt3)
    .orElse("default");
```

### Conditional Action
```java
findUser(id).ifPresentOrElse(
    user -> render(user),
    () -> showNotFound()
);
```

---

## Common Mistakes

| Mistake | Problem | Correct |
|---------|---------|---------|
| `opt.get()` without check | `NoSuchElementException` | Use `orElse()` |
| `Optional.of(null)` | `NullPointerException` | Use `ofNullable()` |
| Optional in fields | Memory overhead | Use null |
| Optional in parameters | Poor API design | Use overloading |
| `isPresent()` + `get()` | Verbose, error-prone | Use `orElse()` family |

---

## When NOT to Use Optional

- Class fields
- Method parameters
- Collections (use empty collection)
- Performance-critical code
- Primitive values (use OptionalInt, etc.)

---

## Comparison Quick Reference

```java
// AVOID: verbose null check pattern
if (value != null) {
    return value;
} else {
    return "default";
}

// PREFER: Optional pattern
return Optional.ofNullable(value).orElse("default");
```
