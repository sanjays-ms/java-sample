# Optional in Java

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/optional-cheatsheet.md)

---

## What Is Optional?

`Optional` is a container that may or may not contain a value. It's Java's way of explicitly handling the possibility of "no value" without using `null`.

**In Plain Words:**
Imagine ordering a package online. When you check your mailbox:
- You might find a package (value present)
- You might find nothing (value absent)

`Optional` is like a mailbox that clearly tells you whether something is inside or not, rather than just finding an empty spot that could mean "nothing" or "not checked yet."

**The Problem Optional Solves:**

```java
// Without Optional - dangerous!
String name = user.getName();      // Could return null
int length = name.length();        // NullPointerException if null!

// With Optional - safe!
Optional<String> maybeName = user.getOptionalName();
int length = maybeName.map(String::length).orElse(0);
```

---

## Why Does Optional Exist?

### The Billion Dollar Mistake

Tony Hoare, who invented null references, called it his "billion dollar mistake" because null pointer exceptions cause countless bugs and crashes.

**Problems with null:**

| Problem | Description |
|---------|-------------|
| Ambiguous meaning | Does null mean "not found", "error", "not initialized", or "intentionally empty"? |
| Silent failures | Null can propagate through your program until it crashes somewhere unexpected |
| Defensive coding | You need `if (x != null)` checks everywhere |
| Poor API design | Method signatures don't tell you if null is possible |

**How Optional Helps:**

| Benefit | Description |
|---------|-------------|
| Explicit intent | The return type tells you a value might be absent |
| Forces handling | You must explicitly handle the empty case |
| Fluent API | Chain operations safely with map, filter, etc. |
| Self-documenting | Code is clearer about what's happening |

---

## Creating Optional Objects

### Optional.of() - When Value Is Definitely Present

```java
// Use when you're SURE the value is not null
String name = "John";
Optional<String> optName = Optional.of(name);

// DANGER: Throws NullPointerException if value is null!
String nullName = null;
Optional<String> opt = Optional.of(nullName);  // NullPointerException!
```

### Optional.ofNullable() - When Value Might Be Null

```java
// Use when value might be null
String maybeName = getUserInput();  // Could return null
Optional<String> optName = Optional.ofNullable(maybeName);

// If null, creates empty Optional
Optional<String> opt = Optional.ofNullable(null);  // Empty Optional, no exception
```

### Optional.empty() - Explicitly Empty

```java
// Create an empty Optional
Optional<String> empty = Optional.empty();

// Useful for return statements
public Optional<User> findUser(int id) {
    if (id < 0) {
        return Optional.empty();  // Invalid ID, no user
    }
    // ... find user
}
```

### When to Use Which?

| Method | Use When | If Null |
|--------|----------|---------|
| `of(value)` | Value is guaranteed non-null | Throws NullPointerException |
| `ofNullable(value)` | Value might be null | Returns empty Optional |
| `empty()` | You want an empty Optional | N/A |

---

## Checking for Value Presence

### isPresent() and isEmpty()

```java
Optional<String> optName = Optional.of("John");

// Check if value exists
if (optName.isPresent()) {
    System.out.println("Name is present: " + optName.get());
}

// Check if empty (Java 11+)
if (optName.isEmpty()) {
    System.out.println("No name provided");
}
```

### ifPresent() - Execute If Value Exists

```java
Optional<String> optName = Optional.of("John");

// Only runs if value is present
optName.ifPresent(name -> System.out.println("Hello, " + name));

// Method reference version
optName.ifPresent(System.out::println);
```

### ifPresentOrElse() - Handle Both Cases (Java 9+)

```java
Optional<String> optName = Optional.ofNullable(getName());

optName.ifPresentOrElse(
    name -> System.out.println("Hello, " + name),   // If present
    () -> System.out.println("Hello, stranger!")     // If empty
);
```

---

## Getting Values from Optional

### get() - Direct Access (Use with Caution!)

```java
Optional<String> optName = Optional.of("John");

// DANGER: Throws NoSuchElementException if empty!
String name = optName.get();

// Only use after checking isPresent()
if (optName.isPresent()) {
    String safeName = optName.get();
}
```

**Warning:** Avoid using `get()` without checking first. It defeats the purpose of Optional!

### orElse() - Provide Default Value

```java
Optional<String> optName = Optional.empty();

// Returns default if empty
String name = optName.orElse("Unknown");
System.out.println(name);  // "Unknown"

// With value present
Optional<String> optName2 = Optional.of("John");
String name2 = optName2.orElse("Unknown");
System.out.println(name2);  // "John"
```

### orElseGet() - Provide Default via Supplier

```java
Optional<String> optName = Optional.empty();

// Supplier is only called if Optional is empty
String name = optName.orElseGet(() -> generateDefaultName());
String name2 = optName.orElseGet(() -> "Default");

// Method reference
String name3 = optName.orElseGet(this::getDefaultName);
```

### orElse() vs orElseGet() - Important Difference!

```java
// orElse: Default is ALWAYS evaluated
Optional<String> opt = Optional.of("John");
String name = opt.orElse(expensiveOperation());  // expensiveOperation() RUNS!

// orElseGet: Supplier is only called when needed
String name2 = opt.orElseGet(() -> expensiveOperation());  // NOT called!
```

**Rule of Thumb:**
- Use `orElse()` for simple values: `orElse("default")`
- Use `orElseGet()` for computed values: `orElseGet(() -> compute())`

### orElseThrow() - Throw Exception If Empty

```java
Optional<User> optUser = findUser(id);

// Throw default NoSuchElementException
User user = optUser.orElseThrow();

// Throw custom exception
User user2 = optUser.orElseThrow(() -> 
    new UserNotFoundException("User not found: " + id));

// Method reference for no-arg constructor
User user3 = optUser.orElseThrow(UserNotFoundException::new);
```

---

## Transforming Optional Values

### map() - Transform the Value

```java
Optional<String> optName = Optional.of("john");

// Transform if present
Optional<String> optUpper = optName.map(String::toUpperCase);
System.out.println(optUpper.get());  // "JOHN"

// Chain transformations
Optional<Integer> optLength = optName
    .map(String::toUpperCase)
    .map(String::length);
System.out.println(optLength.get());  // 4

// If empty, map returns empty
Optional<String> empty = Optional.empty();
Optional<String> result = empty.map(String::toUpperCase);
System.out.println(result.isEmpty());  // true
```

### flatMap() - For Nested Optionals

When your transformation function returns an Optional, use `flatMap()` to avoid `Optional<Optional<T>>`.

```java
class User {
    private String email;
    
    // Returns Optional because email might not be set
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}

Optional<User> optUser = findUser(id);

// WRONG: map() would give Optional<Optional<String>>
Optional<Optional<String>> nested = optUser.map(User::getEmail);

// CORRECT: flatMap() flattens it
Optional<String> email = optUser.flatMap(User::getEmail);

// Practical example: chain of possibly-empty values
Optional<String> domain = findUser(id)
    .flatMap(User::getEmail)
    .map(email -> email.split("@")[1]);
```

### map() vs flatMap()

| Scenario | Use |
|----------|-----|
| Transform returns a regular value | `map()` |
| Transform returns an Optional | `flatMap()` |

```java
// map: T -> R becomes Optional<R>
Optional<String> name = Optional.of("John");
Optional<Integer> length = name.map(String::length);  // String -> Integer

// flatMap: T -> Optional<R> becomes Optional<R> (not Optional<Optional<R>>)
Optional<User> user = Optional.of(new User());
Optional<String> email = user.flatMap(User::getEmail);  // User -> Optional<String>
```

---

## Filtering Optional Values

### filter() - Keep Value If It Matches

```java
Optional<String> optName = Optional.of("John");

// Keep only if predicate is true
Optional<String> longName = optName.filter(name -> name.length() > 3);
System.out.println(longName.isPresent());  // true

Optional<String> veryLongName = optName.filter(name -> name.length() > 10);
System.out.println(veryLongName.isPresent());  // false (became empty)

// Practical example
Optional<Integer> age = Optional.of(25);
Optional<Integer> adultAge = age.filter(a -> a >= 18);
```

### Combining filter, map, and orElse

```java
// Get uppercase name if it's long enough, otherwise "UNKNOWN"
String result = Optional.of("john")
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .orElse("UNKNOWN");
System.out.println(result);  // "JOHN"

// Same chain with short name
String result2 = Optional.of("Jo")
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .orElse("UNKNOWN");
System.out.println(result2);  // "UNKNOWN"
```

---

## Optional with Streams

### stream() - Convert to Stream (Java 9+)

```java
Optional<String> optName = Optional.of("John");

// Convert Optional to Stream (0 or 1 elements)
Stream<String> stream = optName.stream();
stream.forEach(System.out::println);  // "John"

// Useful for flattening a list of Optionals
List<Optional<String>> optionals = List.of(
    Optional.of("A"),
    Optional.empty(),
    Optional.of("B"),
    Optional.empty(),
    Optional.of("C")
);

// Get only the present values
List<String> values = optionals.stream()
    .flatMap(Optional::stream)  // Removes empties
    .toList();
System.out.println(values);  // [A, B, C]
```

### or() - Provide Alternative Optional (Java 9+)

```java
Optional<String> primary = Optional.empty();
Optional<String> fallback = Optional.of("Fallback");

// Try primary, then fallback
Optional<String> result = primary.or(() -> fallback);
System.out.println(result.get());  // "Fallback"

// Chain multiple sources
Optional<String> config = getFromEnv()
    .or(this::getFromFile)
    .or(this::getFromDatabase)
    .or(() -> Optional.of("default"));
```

---

## Practical Examples

### Example 1: Safe Property Access

```java
class Address {
    private String city;
    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }
}

class User {
    private Address address;
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
}

// Safe chained access
public String getUserCity(User user) {
    return Optional.ofNullable(user)
        .flatMap(User::getAddress)
        .flatMap(Address::getCity)
        .orElse("Unknown City");
}
```

### Example 2: Configuration with Fallbacks

```java
public class Config {
    
    public Optional<String> getEnvVar(String name) {
        return Optional.ofNullable(System.getenv(name));
    }
    
    public Optional<String> getProperty(String name) {
        return Optional.ofNullable(System.getProperty(name));
    }
    
    public String getConfigValue(String name, String defaultValue) {
        return getEnvVar(name)
            .or(() -> getProperty(name))
            .orElse(defaultValue);
    }
}

// Usage
Config config = new Config();
String dbUrl = config.getConfigValue("DATABASE_URL", "localhost:5432");
```

### Example 3: Repository Pattern

```java
public class UserRepository {
    private Map<Integer, User> users = new HashMap<>();
    
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }
    
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst();
    }
}

// Service layer
public class UserService {
    private UserRepository repo;
    
    public User getUserOrThrow(int id) {
        return repo.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    public String getUserDisplayName(int id) {
        return repo.findById(id)
            .map(User::getName)
            .map(name -> "User: " + name)
            .orElse("Guest");
    }
}
```

### Example 4: Parsing with Optional

```java
public class Parser {
    
    public Optional<Integer> parseInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    public Optional<LocalDate> parseDate(String s) {
        try {
            return Optional.of(LocalDate.parse(s));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}

// Usage
Parser parser = new Parser();

int age = parser.parseInt(userInput)
    .filter(n -> n >= 0 && n <= 150)
    .orElse(0);

LocalDate date = parser.parseDate(dateInput)
    .orElse(LocalDate.now());
```

### Example 5: Optional in Method Chaining

```java
public class OrderProcessor {
    
    public Optional<Order> findOrder(String orderId) {
        // ...
    }
    
    public String getOrderSummary(String orderId) {
        return findOrder(orderId)
            .filter(Order::isActive)
            .map(order -> String.format(
                "Order %s: %d items, $%.2f",
                order.getId(),
                order.getItemCount(),
                order.getTotal()))
            .orElse("Order not found or inactive");
    }
    
    public void processOrder(String orderId) {
        findOrder(orderId)
            .filter(Order::isPending)
            .ifPresentOrElse(
                this::ship,
                () -> log.warn("Cannot process order: " + orderId)
            );
    }
}
```

---

## Optional for Primitive Types

Java provides specialized Optional classes for primitives to avoid boxing overhead:

```java
// OptionalInt for int
OptionalInt optInt = OptionalInt.of(42);
int value = optInt.orElse(0);
optInt.ifPresent(System.out::println);

// OptionalLong for long
OptionalLong optLong = OptionalLong.of(100L);
long longValue = optLong.orElse(0L);

// OptionalDouble for double
OptionalDouble optDouble = OptionalDouble.of(3.14);
double doubleValue = optDouble.orElse(0.0);

// Common with streams
OptionalInt max = IntStream.of(1, 2, 3, 4, 5).max();
OptionalDouble avg = IntStream.of(1, 2, 3, 4, 5).average();
```

**Methods Available:**

| Method | Description |
|--------|-------------|
| `of(value)` | Create with value |
| `empty()` | Create empty |
| `isPresent()` | Check if value exists |
| `isEmpty()` | Check if empty (Java 11+) |
| `getAsInt/Long/Double()` | Get value |
| `orElse(default)` | Get value or default |
| `orElseGet(supplier)` | Get value or compute default |
| `orElseThrow()` | Get value or throw |
| `ifPresent(consumer)` | Execute if present |

**Note:** Primitive Optionals don't have `map()`, `filter()`, or `flatMap()`. If you need these, use regular Optional<Integer>, etc.

---

## When to Use Optional

### DO Use Optional For:

| Use Case | Example |
|----------|---------|
| Method return types | `Optional<User> findById(int id)` |
| Indicating possible absence | `Optional<String> getMiddleName()` |
| Chaining transformations | `.map().filter().orElse()` |
| Stream terminal operations | `findFirst()`, `findAny()`, `max()`, `min()` |

### DON'T Use Optional For:

| Avoid | Why | Instead |
|-------|-----|---------|
| Method parameters | Makes API awkward | Use overloading or @Nullable |
| Class fields | Memory overhead, serialization issues | Use null with good docs |
| Collections | Empty collection is better | Return empty List, Set, etc. |
| Performance-critical paths | Creates object overhead | Use null with checks |

```java
// DON'T: Optional as parameter
public void process(Optional<String> name) { }  // Awkward!

// DO: Overload or nullable
public void process(String name) { }
public void process() { process(null); }

// DON'T: Optional as field
class User {
    private Optional<String> nickname;  // Don't do this!
}

// DO: Null with getter returning Optional
class User {
    private String nickname;  // Can be null
    
    public Optional<String> getNickname() {
        return Optional.ofNullable(nickname);
    }
}

// DON'T: Return Optional of collection
public Optional<List<User>> getUsers() { }  // No!

// DO: Return empty collection
public List<User> getUsers() {
    return users != null ? users : Collections.emptyList();
}
```

---

## Common Mistakes and How to Avoid Them

| Mistake | Problem | Correct Approach |
|---------|---------|-----------------|
| `optional.get()` without check | NoSuchElementException | Use `orElse()`, `orElseThrow()`, etc. |
| `if (opt.isPresent()) { opt.get() }` | Verbose, misses the point | Use `ifPresent()`, `map()`, etc. |
| `Optional.of(nullableValue)` | NullPointerException | Use `Optional.ofNullable()` |
| Optional as method parameter | Awkward API | Use overloading or null |
| Optional as class field | Overhead, serialization issues | Use null, return Optional from getter |
| `opt.orElse(expensiveCall())` | Always evaluates | Use `orElseGet(() -> ...)` |
| Returning `null` instead of `Optional.empty()` | Defeats the purpose | Always return Optional |

### Code Smell Examples

```java
// BAD: Using isPresent() + get()
if (optional.isPresent()) {
    String value = optional.get();
    doSomething(value);
}

// GOOD: Using ifPresent()
optional.ifPresent(this::doSomething);

// BAD: Nested if statements
Optional<User> optUser = findUser(id);
if (optUser.isPresent()) {
    User user = optUser.get();
    Optional<Address> optAddr = user.getAddress();
    if (optAddr.isPresent()) {
        return optAddr.get().getCity();
    }
}
return "Unknown";

// GOOD: Chained flatMap
return findUser(id)
    .flatMap(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

---

## Best Practices Summary

1. **Use `Optional` for return types that might have no value**
   - Makes the API honest about absence possibility

2. **Prefer functional methods over `isPresent()` + `get()`**
   - Use `map()`, `flatMap()`, `filter()`, `ifPresent()`, `orElse()`

3. **Use `orElseGet()` for expensive default computations**
   - Lazy evaluation only when needed

4. **Use `orElseThrow()` for required values**
   - Clear exception when value is mandatory

5. **Don't use Optional for parameters or fields**
   - Use null with good documentation
   - Return Optional from getters

6. **Return empty collections instead of Optional<Collection>**
   - `Collections.emptyList()` is cleaner than `Optional<List>`

7. **Use primitive variants for performance**
   - `OptionalInt`, `OptionalLong`, `OptionalDouble`

---

## Cheat Sheet

### Creating

```java
Optional.of(value)           // Non-null value (throws if null)
Optional.ofNullable(value)   // Possibly null value
Optional.empty()             // Empty optional
```

### Checking

```java
opt.isPresent()              // true if value present
opt.isEmpty()                // true if empty (Java 11+)
opt.ifPresent(consumer)      // Run if present
opt.ifPresentOrElse(consumer, runnable)  // Run either way (Java 9+)
```

### Getting

```java
opt.get()                    // Get (throws if empty - avoid!)
opt.orElse(default)          // Get or default
opt.orElseGet(supplier)      // Get or compute default
opt.orElseThrow()            // Get or throw NoSuchElementException
opt.orElseThrow(exSupplier)  // Get or throw custom exception
```

### Transforming

```java
opt.map(function)            // Transform value
opt.flatMap(function)        // Transform to Optional, flatten
opt.filter(predicate)        // Keep if matches
opt.or(supplier)             // Alternative Optional (Java 9+)
opt.stream()                 // Convert to Stream (Java 9+)
```

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Concurrency](./28-concurrency.md) | [Guide](../guide.md) | [Virtual Threads](./30-virtual-threads.md) |
