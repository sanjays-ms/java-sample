# Functional Interfaces

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/functional-interfaces-cheatsheet.md)

---

## What Is a Functional Interface?

A functional interface is an interface that has exactly one abstract method. It can be used as the target type for a lambda expression or method reference.

```java
// A functional interface - exactly ONE abstract method
@FunctionalInterface
public interface Greeting {
    void sayHello(String name);
}

// Can be implemented with a lambda
Greeting greet = name -> System.out.println("Hello, " + name);
greet.sayHello("Alice");  // Output: Hello, Alice
```

**In plain words:** A functional interface is a contract that says "you must provide exactly one behavior." Because there's only one thing to implement, Java can figure out that your lambda is that one thing.

---

## Why Functional Interfaces Exist

### Problem 1: Lambda Target Types

Lambdas need a type. How does Java know what type a lambda should be?

```java
// What type is this lambda?
(x, y) -> x + y

// Java uses the context to determine the type
BinaryOperator<Integer> adder = (x, y) -> x + y;
BiFunction<Integer, Integer, Integer> adder2 = (x, y) -> x + y;
Comparator<Integer> comparator = (x, y) -> x + y;  // Also valid!
```

The functional interface provides the type information.

### Problem 2: Type Safety

Functional interfaces ensure type safety:

```java
// The interface defines the contract
@FunctionalInterface
interface StringProcessor {
    String process(String input);
}

// Lambda must match the contract
StringProcessor upper = s -> s.toUpperCase();  // OK
StringProcessor length = s -> s.length();       // Error! Returns int, not String
```

### Problem 3: Standard Contracts

Before Java 8, everyone created their own interfaces:

```java
// Different projects had different interfaces for the same thing
interface StringTransformer { String transform(String s); }
interface TextConverter { String convert(String text); }
interface StringMapper { String map(String input); }
```

Java 8 introduced standard functional interfaces in `java.util.function`.

---

## The @FunctionalInterface Annotation

This annotation is optional but recommended. It tells the compiler to verify the interface is valid:

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
}
```

### What the Annotation Does

```java
@FunctionalInterface
public interface Valid {
    void doSomething();  // One abstract method - OK
}

@FunctionalInterface
public interface Invalid {
    void method1();
    void method2();  // Compile error! Two abstract methods
}
```

### What Counts as Abstract?

```java
@FunctionalInterface
public interface StillFunctional {
    // The ONE abstract method
    void doWork();
    
    // Default methods don't count
    default void helper() {
        System.out.println("Helper");
    }
    
    // Static methods don't count
    static void utility() {
        System.out.println("Utility");
    }
    
    // Object methods don't count (toString, equals, hashCode)
    String toString();
    boolean equals(Object o);
    int hashCode();
}
```

---

## Core Functional Interfaces

Java provides 43 functional interfaces in `java.util.function`. Here are the most important ones:

### Predicate<T> - Test a Condition

**Purpose:** Take a value, return true or false.

```java
import java.util.function.Predicate;

// Test if a number is positive
Predicate<Integer> isPositive = n -> n > 0;

System.out.println(isPositive.test(5));   // true
System.out.println(isPositive.test(-3));  // false
System.out.println(isPositive.test(0));   // false
```

**Common uses:**

```java
// Filtering collections
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

Predicate<String> startsWithC = name -> name.startsWith("C");
List<String> cNames = names.stream()
    .filter(startsWithC)
    .toList();  // [Charlie]

// Validation
Predicate<String> isValidEmail = email -> 
    email != null && email.contains("@") && email.contains(".");

if (isValidEmail.test(userEmail)) {
    // Process email
}

// Removing from collections
List<String> mutableList = new ArrayList<>(names);
mutableList.removeIf(name -> name.length() < 4);  // Removes "Bob"
```

**Predicate methods:**

```java
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEven = n -> n % 2 == 0;

// Combine with AND
Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);
isPositiveAndEven.test(4);   // true
isPositiveAndEven.test(3);   // false
isPositiveAndEven.test(-2);  // false

// Combine with OR
Predicate<Integer> isPositiveOrEven = isPositive.or(isEven);
isPositiveOrEven.test(3);    // true (positive)
isPositiveOrEven.test(-2);   // true (even)
isPositiveOrEven.test(-3);   // false

// Negate
Predicate<Integer> isNotPositive = isPositive.negate();
isNotPositive.test(-5);  // true
isNotPositive.test(5);   // false

// Static factory methods
Predicate<String> notNull = Predicate.not(Objects::isNull);
Predicate<String> alwaysTrue = s -> true;  // Or use Predicate.isEqual(null).negate()
```

---

### Function<T, R> - Transform a Value

**Purpose:** Take a value of type T, return a value of type R.

```java
import java.util.function.Function;

// Convert String to Integer
Function<String, Integer> length = s -> s.length();

System.out.println(length.apply("Hello"));  // 5
System.out.println(length.apply("Hi"));     // 2
```

**Common uses:**

```java
// Transforming collections
List<String> names = List.of("Alice", "Bob", "Charlie");

Function<String, String> toUpper = String::toUpperCase;
List<String> upperNames = names.stream()
    .map(toUpper)
    .toList();  // [ALICE, BOB, CHARLIE]

// Extracting data
Function<Person, String> getName = Person::getName;
List<String> allNames = people.stream()
    .map(getName)
    .toList();

// Converting types
Function<String, Integer> parseInt = Integer::parseInt;
Function<Integer, String> intToString = String::valueOf;
```

**Function methods:**

```java
Function<String, String> trim = String::trim;
Function<String, String> upper = String::toUpperCase;
Function<String, Integer> length = String::length;

// andThen - apply this function, then the next
Function<String, String> trimThenUpper = trim.andThen(upper);
trimThenUpper.apply("  hello  ");  // "HELLO"

// compose - apply the other function first, then this
Function<String, String> upperAfterTrim = upper.compose(trim);
upperAfterTrim.apply("  hello  ");  // "HELLO"

// Chain multiple functions
Function<String, Integer> pipeline = trim.andThen(upper).andThen(length);
pipeline.apply("  hello  ");  // 5

// Identity function - returns input unchanged
Function<String, String> identity = Function.identity();
identity.apply("Hello");  // "Hello"
```

---

### Consumer<T> - Consume a Value

**Purpose:** Take a value, do something with it, return nothing.

```java
import java.util.function.Consumer;

// Print a value
Consumer<String> printer = s -> System.out.println(s);

printer.accept("Hello");  // Prints: Hello
```

**Common uses:**

```java
// Processing each element
List<String> names = List.of("Alice", "Bob", "Charlie");

Consumer<String> greet = name -> System.out.println("Hello, " + name);
names.forEach(greet);
// Hello, Alice
// Hello, Bob
// Hello, Charlie

// Logging
Consumer<Exception> logError = e -> logger.error("Error: " + e.getMessage());

// Modifying objects
Consumer<Person> makeAdult = person -> person.setAge(18);
people.forEach(makeAdult);

// Saving to database
Consumer<User> saveUser = user -> userRepository.save(user);
```

**Consumer methods:**

```java
Consumer<String> print = System.out::println;
Consumer<String> log = s -> logger.info(s);

// andThen - do this, then do that
Consumer<String> printAndLog = print.andThen(log);
printAndLog.accept("Message");  // Prints and logs
```

---

### Supplier<T> - Supply a Value

**Purpose:** Take nothing, return a value.

```java
import java.util.function.Supplier;

// Supply a random number
Supplier<Double> randomSupplier = () -> Math.random();

System.out.println(randomSupplier.get());  // 0.7234...
System.out.println(randomSupplier.get());  // 0.1892...
```

**Common uses:**

```java
// Factory pattern
Supplier<List<String>> listFactory = ArrayList::new;
List<String> list1 = listFactory.get();  // New ArrayList
List<String> list2 = listFactory.get();  // Another new ArrayList

// Lazy initialization
Supplier<ExpensiveObject> lazy = () -> new ExpensiveObject();
// Object is NOT created yet
ExpensiveObject obj = lazy.get();  // NOW it's created

// Default values
Supplier<String> defaultName = () -> "Unknown";
String name = Optional.ofNullable(userName).orElseGet(defaultName);

// Configuration
Supplier<Connection> connectionSupplier = () -> 
    DriverManager.getConnection(url, user, password);

// Generating test data
Supplier<UUID> idGenerator = UUID::randomUUID;
```

---

### BiPredicate<T, U> - Test Two Values

**Purpose:** Take two values, return true or false.

```java
import java.util.function.BiPredicate;

// Check if string contains substring
BiPredicate<String, String> contains = (str, sub) -> str.contains(sub);

System.out.println(contains.test("Hello World", "World"));  // true
System.out.println(contains.test("Hello World", "Java"));   // false
```

**Common uses:**

```java
// Comparing two objects
BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
BiPredicate<String, Integer> hasLength = (s, len) -> s.length() == len;

// Validation with context
BiPredicate<String, Integer> isLongEnough = (password, minLength) -> 
    password.length() >= minLength;

if (isLongEnough.test(userPassword, 8)) {
    // Password is valid
}
```

---

### BiFunction<T, U, R> - Transform Two Values

**Purpose:** Take two values, return one value.

```java
import java.util.function.BiFunction;

// Combine two strings
BiFunction<String, String, String> concat = (a, b) -> a + " " + b;

System.out.println(concat.apply("Hello", "World"));  // "Hello World"
```

**Common uses:**

```java
// Map operations
Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 100);

BiFunction<String, Integer, Integer> addBonus = (key, value) -> value + 10;
scores.replaceAll(addBonus);  // All scores increased by 10

// Combining data
BiFunction<Person, Address, Contact> createContact = (person, address) ->
    new Contact(person.getName(), person.getEmail(), address);

// Calculations
BiFunction<Double, Double, Double> power = Math::pow;
System.out.println(power.apply(2.0, 10.0));  // 1024.0
```

---

### BiConsumer<T, U> - Consume Two Values

**Purpose:** Take two values, do something with them, return nothing.

```java
import java.util.function.BiConsumer;

// Print key-value pair
BiConsumer<String, Integer> printEntry = (key, value) -> 
    System.out.println(key + " = " + value);

printEntry.accept("age", 25);  // age = 25
```

**Common uses:**

```java
// Iterating maps
Map<String, Integer> scores = Map.of("Alice", 100, "Bob", 85);

BiConsumer<String, Integer> printScore = (name, score) ->
    System.out.println(name + " scored " + score);
    
scores.forEach(printScore);
// Alice scored 100
// Bob scored 85

// Populating maps
BiConsumer<Map<String, String>, String> addDefault = (map, key) ->
    map.putIfAbsent(key, "default");
```

---

### UnaryOperator<T> - Same Type In and Out

**Purpose:** Take a value, return a value of the same type. Special case of `Function<T, T>`.

```java
import java.util.function.UnaryOperator;

// Double a number
UnaryOperator<Integer> doubleIt = n -> n * 2;

System.out.println(doubleIt.apply(5));  // 10
```

**Common uses:**

```java
// In-place transformations
List<String> names = new ArrayList<>(List.of("alice", "bob"));
UnaryOperator<String> capitalize = s -> 
    s.substring(0, 1).toUpperCase() + s.substring(1);

names.replaceAll(capitalize);  // [Alice, Bob]

// Increment operations
UnaryOperator<Integer> increment = n -> n + 1;

// String transformations
UnaryOperator<String> addPrefix = s -> "Mr. " + s;
```

---

### BinaryOperator<T> - Combine Two of Same Type

**Purpose:** Take two values of the same type, return one value of that type. Special case of `BiFunction<T, T, T>`.

```java
import java.util.function.BinaryOperator;

// Add two numbers
BinaryOperator<Integer> add = (a, b) -> a + b;

System.out.println(add.apply(5, 3));  // 8
```

**Common uses:**

```java
// Reducing streams
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

BinaryOperator<Integer> sum = (a, b) -> a + b;
int total = numbers.stream().reduce(0, sum);  // 15

BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
int maximum = numbers.stream().reduce(Integer.MIN_VALUE, max);  // 5

// Static factory methods
BinaryOperator<Integer> minOp = BinaryOperator.minBy(Comparator.naturalOrder());
BinaryOperator<Integer> maxOp = BinaryOperator.maxBy(Comparator.naturalOrder());

System.out.println(minOp.apply(5, 3));  // 3
System.out.println(maxOp.apply(5, 3));  // 5
```

---

## Primitive Specializations

To avoid boxing/unboxing overhead, Java provides primitive versions:

### Why Primitives?

```java
// With generics - boxing happens
Function<Integer, Integer> doubleIt = n -> n * 2;
int result = doubleIt.apply(5);  // 5 is boxed to Integer, result is unboxed

// With primitive specialization - no boxing
IntUnaryOperator doubleItPrimitive = n -> n * 2;
int result2 = doubleItPrimitive.applyAsInt(5);  // All primitives, no boxing
```

### Primitive Predicates

```java
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;

IntPredicate isEven = n -> n % 2 == 0;
LongPredicate isPositive = n -> n > 0;
DoublePredicate isFinite = Double::isFinite;

isEven.test(4);        // true (no boxing)
isPositive.test(100L); // true
isFinite.test(3.14);   // true
```

### Primitive Functions

```java
// T -> primitive
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.ToDoubleFunction;

ToIntFunction<String> stringLength = String::length;
int len = stringLength.applyAsInt("Hello");  // 5

ToDoubleFunction<Integer> toPercent = n -> n / 100.0;
double percent = toPercent.applyAsDouble(75);  // 0.75
```

```java
// primitive -> R
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.DoubleFunction;

IntFunction<String> intToString = n -> "Number: " + n;
String result = intToString.apply(42);  // "Number: 42"

IntFunction<int[]> arrayCreator = int[]::new;
int[] array = arrayCreator.apply(10);  // int[10]
```

```java
// primitive -> primitive
import java.util.function.IntToLongFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;

IntToDoubleFunction half = n -> n / 2.0;
double result = half.applyAsDouble(5);  // 2.5
```

### Primitive Consumers

```java
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.DoubleConsumer;

IntConsumer printInt = n -> System.out.println("Int: " + n);
printInt.accept(42);  // "Int: 42"

// Chaining
IntConsumer first = n -> System.out.print(n);
IntConsumer second = n -> System.out.println(" squared is " + n * n);
IntConsumer both = first.andThen(second);
both.accept(5);  // "5 squared is 25"
```

### Primitive Suppliers

```java
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.DoubleSupplier;

IntSupplier randomInt = () -> (int)(Math.random() * 100);
int value = randomInt.getAsInt();  // Random 0-99

LongSupplier currentTime = System::currentTimeMillis;
long time = currentTime.getAsLong();

DoubleSupplier random = Math::random;
double r = random.getAsDouble();
```

### Primitive Operators

```java
import java.util.function.IntUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.DoubleBinaryOperator;

IntUnaryOperator negate = n -> -n;
int result = negate.applyAsInt(5);  // -5

IntBinaryOperator multiply = (a, b) -> a * b;
int product = multiply.applyAsInt(6, 7);  // 42

// Chaining unary operators
IntUnaryOperator addOne = n -> n + 1;
IntUnaryOperator timesTwo = n -> n * 2;
IntUnaryOperator combined = addOne.andThen(timesTwo);
int result2 = combined.applyAsInt(5);  // 12 ((5+1)*2)
```

### Object-and-Primitive Functions

```java
// Object and primitive to object
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjDoubleConsumer;

ObjIntConsumer<String> printWithIndex = (s, i) -> 
    System.out.println(i + ": " + s);
printWithIndex.accept("Hello", 0);  // "0: Hello"

// Bi-functions with primitives
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToDoubleBiFunction;

ToIntBiFunction<String, String> compareLengths = (a, b) -> 
    a.length() - b.length();
int diff = compareLengths.applyAsInt("Hello", "Hi");  // 3
```

---

## Complete Interface Reference

### Basic Interfaces

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `Predicate<T>` | T | boolean | `test(T)` |
| `Function<T,R>` | T | R | `apply(T)` |
| `Consumer<T>` | T | void | `accept(T)` |
| `Supplier<T>` | none | T | `get()` |

### Two-Argument Interfaces

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `BiPredicate<T,U>` | T, U | boolean | `test(T,U)` |
| `BiFunction<T,U,R>` | T, U | R | `apply(T,U)` |
| `BiConsumer<T,U>` | T, U | void | `accept(T,U)` |

### Operator Interfaces

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `UnaryOperator<T>` | T | T | `apply(T)` |
| `BinaryOperator<T>` | T, T | T | `apply(T,T)` |

### Primitive Predicates

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `IntPredicate` | int | boolean | `test(int)` |
| `LongPredicate` | long | boolean | `test(long)` |
| `DoublePredicate` | double | boolean | `test(double)` |

### Primitive Functions

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `IntFunction<R>` | int | R | `apply(int)` |
| `LongFunction<R>` | long | R | `apply(long)` |
| `DoubleFunction<R>` | double | R | `apply(double)` |
| `ToIntFunction<T>` | T | int | `applyAsInt(T)` |
| `ToLongFunction<T>` | T | long | `applyAsLong(T)` |
| `ToDoubleFunction<T>` | T | double | `applyAsDouble(T)` |

### Primitive Operators

| Interface | Input | Output | Method |
|-----------|-------|--------|--------|
| `IntUnaryOperator` | int | int | `applyAsInt(int)` |
| `LongUnaryOperator` | long | long | `applyAsLong(long)` |
| `DoubleUnaryOperator` | double | double | `applyAsDouble(double)` |
| `IntBinaryOperator` | int, int | int | `applyAsInt(int,int)` |
| `LongBinaryOperator` | long, long | long | `applyAsLong(long,long)` |
| `DoubleBinaryOperator` | double, double | double | `applyAsDouble(double,double)` |

---

## Creating Custom Functional Interfaces

Sometimes the standard interfaces don't fit your needs:

### Simple Custom Interface

```java
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

// Usage
TriFunction<String, String, String, String> fullName = 
    (first, middle, last) -> first + " " + middle + " " + last;

String name = fullName.apply("John", "William", "Smith");
// "John William Smith"
```

### Interface with Default Methods

```java
@FunctionalInterface
public interface Validator<T> {
    boolean validate(T value);
    
    // Combine validators with AND
    default Validator<T> and(Validator<T> other) {
        return value -> this.validate(value) && other.validate(value);
    }
    
    // Combine validators with OR
    default Validator<T> or(Validator<T> other) {
        return value -> this.validate(value) || other.validate(value);
    }
    
    // Negate this validator
    default Validator<T> negate() {
        return value -> !this.validate(value);
    }
    
    // Static factory for always-valid
    static <T> Validator<T> alwaysValid() {
        return value -> true;
    }
}

// Usage
Validator<String> notEmpty = s -> s != null && !s.isEmpty();
Validator<String> notTooLong = s -> s.length() <= 100;
Validator<String> combined = notEmpty.and(notTooLong);

combined.validate("Hello");  // true
combined.validate("");       // false
combined.validate(null);     // false
```

### Throwing Functional Interface

Standard interfaces can't throw checked exceptions. Create custom ones that can:

```java
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
    
    // Wrap to standard Function, converting to unchecked exception
    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R, ?> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

// Usage
ThrowingFunction<String, String, IOException> readFile = 
    path -> Files.readString(Path.of(path));

// Convert to regular Function
Function<String, String> safeRead = ThrowingFunction.unchecked(readFile);

// Use with streams
List<String> contents = paths.stream()
    .map(ThrowingFunction.unchecked(p -> Files.readString(Path.of(p))))
    .toList();
```

### Interface with Type Constraints

```java
@FunctionalInterface
public interface ComparableFunction<T extends Comparable<T>> {
    T apply(T a, T b);
    
    static <T extends Comparable<T>> ComparableFunction<T> max() {
        return (a, b) -> a.compareTo(b) >= 0 ? a : b;
    }
    
    static <T extends Comparable<T>> ComparableFunction<T> min() {
        return (a, b) -> a.compareTo(b) <= 0 ? a : b;
    }
}

// Usage
ComparableFunction<Integer> maxInt = ComparableFunction.max();
int result = maxInt.apply(5, 3);  // 5
```

---

## Functional Interfaces in the JDK

Many JDK interfaces are functional interfaces:

### Runnable

```java
// No parameters, no return value
Runnable task = () -> System.out.println("Running!");
new Thread(task).start();
```

### Callable<V>

```java
import java.util.concurrent.Callable;

// No parameters, returns V, can throw Exception
Callable<Integer> compute = () -> {
    Thread.sleep(1000);
    return 42;
};

Future<Integer> future = executor.submit(compute);
```

### Comparator<T>

```java
// Compare two values of type T
Comparator<String> byLength = (a, b) -> a.length() - b.length();
Comparator<String> byLengthMethod = Comparator.comparingInt(String::length);

List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
names.sort(byLength);  // [Bob, Alice, Charlie]
```

### ActionListener (Swing)

```java
import java.awt.event.ActionListener;

// One parameter (ActionEvent), no return value
button.addActionListener(e -> System.out.println("Clicked!"));
```

### FileFilter

```java
import java.io.FileFilter;

// One parameter (File), returns boolean
FileFilter javaFiles = file -> file.getName().endsWith(".java");
File[] files = directory.listFiles(javaFiles);
```

---

## Combining and Composing

### Building Complex Predicates

```java
Predicate<Person> isAdult = p -> p.getAge() >= 18;
Predicate<Person> hasEmail = p -> p.getEmail() != null;
Predicate<Person> isActive = p -> p.isActive();

// Combine all conditions
Predicate<Person> canReceiveNewsletter = isAdult
    .and(hasEmail)
    .and(isActive);

// Filter list
List<Person> recipients = people.stream()
    .filter(canReceiveNewsletter)
    .toList();
```

### Building Processing Pipelines

```java
Function<String, String> trim = String::trim;
Function<String, String> lower = String::toLowerCase;
Function<String, String> removeSpaces = s -> s.replace(" ", "_");

// Build pipeline
Function<String, String> normalize = trim
    .andThen(lower)
    .andThen(removeSpaces);

String result = normalize.apply("  Hello World  ");  // "hello_world"
```

### Building Consumer Chains

```java
Consumer<Order> validate = order -> {
    if (order.getItems().isEmpty()) {
        throw new IllegalStateException("Empty order");
    }
};

Consumer<Order> calculateTotal = order -> {
    double total = order.getItems().stream()
        .mapToDouble(Item::getPrice)
        .sum();
    order.setTotal(total);
};

Consumer<Order> applyDiscount = order -> {
    if (order.getTotal() > 100) {
        order.setTotal(order.getTotal() * 0.9);
    }
};

Consumer<Order> save = order -> orderRepository.save(order);

// Chain all operations
Consumer<Order> processOrder = validate
    .andThen(calculateTotal)
    .andThen(applyDiscount)
    .andThen(save);

processOrder.accept(newOrder);
```

---

## Practical Patterns

### Strategy Pattern

```java
public class PaymentProcessor {
    private Function<Order, PaymentResult> paymentStrategy;
    
    public PaymentProcessor(Function<Order, PaymentResult> strategy) {
        this.paymentStrategy = strategy;
    }
    
    public PaymentResult process(Order order) {
        return paymentStrategy.apply(order);
    }
}

// Different strategies
Function<Order, PaymentResult> creditCard = order -> 
    new PaymentResult(true, "Paid with credit card");

Function<Order, PaymentResult> paypal = order -> 
    new PaymentResult(true, "Paid with PayPal");

// Use different strategies
PaymentProcessor processor1 = new PaymentProcessor(creditCard);
PaymentProcessor processor2 = new PaymentProcessor(paypal);
```

### Factory Pattern

```java
public class ShapeFactory {
    private static Map<String, Supplier<Shape>> factories = new HashMap<>();
    
    static {
        factories.put("circle", Circle::new);
        factories.put("square", Square::new);
        factories.put("triangle", Triangle::new);
    }
    
    public static Shape create(String type) {
        Supplier<Shape> factory = factories.get(type.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("Unknown shape: " + type);
        }
        return factory.get();
    }
}

Shape circle = ShapeFactory.create("circle");
```

### Validator Pattern

```java
public class Validators {
    public static <T> Predicate<T> notNull() {
        return obj -> obj != null;
    }
    
    public static Predicate<String> notEmpty() {
        return s -> s != null && !s.isEmpty();
    }
    
    public static Predicate<String> minLength(int min) {
        return s -> s != null && s.length() >= min;
    }
    
    public static Predicate<String> maxLength(int max) {
        return s -> s != null && s.length() <= max;
    }
    
    public static Predicate<String> matches(String regex) {
        return s -> s != null && s.matches(regex);
    }
}

// Compose validators
Predicate<String> validPassword = Validators.notEmpty()
    .and(Validators.minLength(8))
    .and(Validators.maxLength(20))
    .and(Validators.matches(".*[A-Z].*"))  // Has uppercase
    .and(Validators.matches(".*[0-9].*")); // Has digit

if (validPassword.test(password)) {
    // Accept password
}
```

### Lazy Evaluation

```java
public class Lazy<T> {
    private Supplier<T> supplier;
    private T value;
    private boolean initialized = false;
    
    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        if (!initialized) {
            value = supplier.get();
            initialized = true;
            supplier = null;  // Allow GC
        }
        return value;
    }
}

// Usage
Lazy<ExpensiveObject> lazy = new Lazy<>(() -> {
    System.out.println("Creating expensive object...");
    return new ExpensiveObject();
});

// Object not created yet
System.out.println("Before get");
ExpensiveObject obj = lazy.get();  // NOW it's created
ExpensiveObject obj2 = lazy.get(); // Same instance returned
```

---

## Best Practices

### 1. Use Standard Interfaces When Possible

```java
// Good - use standard interface
Function<String, Integer> length = String::length;

// Avoid - creating equivalent custom interface
interface StringToInt {
    int convert(String s);
}
```

### 2. Use @FunctionalInterface Annotation

```java
// Good - annotation protects the contract
@FunctionalInterface
public interface Handler {
    void handle(Event event);
}

// If someone adds another abstract method, they get a compile error
```

### 3. Keep Functions Pure

```java
// Good - pure function, no side effects
Function<String, String> upper = s -> s.toUpperCase();

// Avoid - side effects in functions
List<String> results = new ArrayList<>();
Function<String, String> badUpper = s -> {
    results.add(s);  // Side effect!
    return s.toUpperCase();
};
```

### 4. Prefer Method References

```java
// Good - clear and concise
Function<String, Integer> length = String::length;
Consumer<String> print = System.out::println;

// Less good - unnecessary lambda
Function<String, Integer> length = s -> s.length();
Consumer<String> print = s -> System.out.println(s);
```

### 5. Use Primitive Specializations

```java
// Good - no boxing
IntPredicate isEven = n -> n % 2 == 0;

// Less efficient - boxing overhead
Predicate<Integer> isEvenBoxed = n -> n % 2 == 0;
```

---

## Common Mistakes

### Mistake 1: Wrong Interface Choice

```java
// Wrong - Consumer can't return a value
Consumer<String> getLength = s -> s.length();  // length() result is ignored!

// Right - use Function
Function<String, Integer> getLength = s -> s.length();
```

### Mistake 2: Ignoring Null Safety

```java
// Dangerous - can throw NullPointerException
Predicate<String> isLong = s -> s.length() > 5;

// Safer
Predicate<String> isLong = s -> s != null && s.length() > 5;
```

### Mistake 3: Complex Lambdas

```java
// Hard to read and debug
Function<Person, String> format = p -> {
    StringBuilder sb = new StringBuilder();
    sb.append(p.getFirstName());
    sb.append(" ");
    sb.append(p.getLastName());
    if (p.getTitle() != null) {
        sb.insert(0, p.getTitle() + " ");
    }
    return sb.toString();
};

// Better - extract to method
Function<Person, String> format = this::formatPersonName;

private String formatPersonName(Person p) {
    StringBuilder sb = new StringBuilder();
    // ... same logic, but testable and debuggable
}
```

### Mistake 4: Not Handling Exceptions

```java
// Won't compile - Function.apply doesn't throw checked exceptions
Function<String, String> readFile = path -> Files.readString(Path.of(path));

// Fix - wrap or use custom interface
Function<String, String> readFile = path -> {
    try {
        return Files.readString(Path.of(path));
    } catch (IOException e) {
        throw new UncheckedIOException(e);
    }
};
```

---

## Cheat Sheet

| When you need... | Use |
|-----------------|-----|
| Test condition | `Predicate<T>` |
| Transform value | `Function<T,R>` |
| Use value (no return) | `Consumer<T>` |
| Create value | `Supplier<T>` |
| Same type in/out | `UnaryOperator<T>` |
| Combine two same types | `BinaryOperator<T>` |
| Two inputs, boolean out | `BiPredicate<T,U>` |
| Two inputs, transform | `BiFunction<T,U,R>` |
| Two inputs, no return | `BiConsumer<T,U>` |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Lambda Expressions](./24-lambda-expressions.md) | [Guide](../guide.md) | [Stream API](./26-stream-api.md) |
