# Lambda Expressions

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/lambda-expressions-cheatsheet.md)

---

## What Is a Lambda Expression?

A lambda expression is a short way to write a method without giving it a name. It is an anonymous function that you can pass around like a value.

```java
// Traditional way - anonymous class
Runnable runnable = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda way - much shorter
Runnable runnable = () -> System.out.println("Hello");
```

Both do exactly the same thing, but the lambda is more concise.

**In plain words:** A lambda is a quick way to define "what to do" without all the ceremony of creating a class or method. Think of it as a recipe you can hand to someone - it describes the steps without being a full cookbook.

---

## Why Lambda Expressions Exist

### Problem 1: Verbose Anonymous Classes

Before lambdas, passing behavior required verbose anonymous classes:

```java
// Sorting with anonymous class - verbose!
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
});

// With lambda - clean!
Collections.sort(names, (a, b) -> a.length() - b.length());
```

### Problem 2: Boilerplate Code

Anonymous classes require a lot of boilerplate:

```java
// Anonymous class boilerplate
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});

// Lambda - just the essential code
button.addActionListener(e -> System.out.println("Button clicked!"));
```

### Problem 3: Passing Behavior

Lambdas make it easy to pass behavior as a parameter:

```java
// Filter a list - pass the filtering logic
List<String> longNames = filter(names, name -> name.length() > 5);

// Transform a list - pass the transformation logic
List<Integer> lengths = transform(names, name -> name.length());

// Process each element - pass the processing logic
names.forEach(name -> System.out.println(name));
```

### Benefits of Lambdas

| Benefit | Description |
|---------|-------------|
| Concise | Less code to write and read |
| Readable | Focus on what, not how |
| Functional style | Enable functional programming |
| Stream API | Foundation for stream operations |
| Parallel processing | Easier to parallelize code |

---

## Lambda Syntax

### Basic Syntax

```
(parameters) -> expression
```

or

```
(parameters) -> { statements; }
```

The arrow `->` separates parameters from the body.

### Syntax Variations

```java
// No parameters
() -> System.out.println("Hello")

// One parameter (parentheses optional)
x -> x * 2
(x) -> x * 2

// Multiple parameters
(x, y) -> x + y

// With type declarations
(int x, int y) -> x + y

// Multiple statements (need braces and return)
(x, y) -> {
    int sum = x + y;
    return sum;
}

// Single expression (no return keyword needed)
(x, y) -> x + y
```

### Parameter Rules

```java
// Types can be inferred (most common)
(a, b) -> a + b

// Or explicitly declared
(String a, String b) -> a + b

// Cannot mix - all or none
// (String a, b) -> a + b  // Error!

// Single parameter without parentheses
name -> name.toUpperCase()

// Single parameter with parentheses (also valid)
(name) -> name.toUpperCase()
```

### Body Rules

```java
// Single expression - no braces, no return
x -> x * 2

// Multiple statements - braces required, return required (if returning)
x -> {
    System.out.println("Processing: " + x);
    return x * 2;
}

// Void return - no return statement
x -> System.out.println(x)

// Void return with braces
x -> {
    System.out.println("Value: " + x);
    System.out.println("Done");
}
```

---

## Functional Interfaces

A lambda can only be used where a **functional interface** is expected. A functional interface is an interface with exactly one abstract method.

### What Is a Functional Interface?

```java
// Functional interface - ONE abstract method
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
}

// Not functional - TWO abstract methods
public interface NotFunctional {
    void method1();
    void method2();  // Second abstract method!
}
```

### Using Lambdas with Functional Interfaces

```java
// The functional interface
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
}

// Lambda implements the interface
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;
Calculator subtract = (a, b) -> a - b;

// Use the lambdas
int sum = add.calculate(5, 3);       // 8
int product = multiply.calculate(5, 3); // 15
int diff = subtract.calculate(5, 3);    // 2
```

### Default and Static Methods

Functional interfaces can have default and static methods:

```java
@FunctionalInterface
public interface Calculator {
    // The one abstract method
    int calculate(int a, int b);
    
    // Default methods are OK
    default int addThenDouble(int a, int b) {
        return calculate(a, b) * 2;
    }
    
    // Static methods are OK
    static Calculator getAdder() {
        return (a, b) -> a + b;
    }
}
```

### @FunctionalInterface Annotation

The `@FunctionalInterface` annotation is optional but recommended:

```java
@FunctionalInterface  // Compiler verifies this is valid
public interface Processor {
    void process(String input);
    
    // If you add another abstract method, compiler error!
    // void anotherMethod();  // Error!
}
```

---

## Common Functional Interfaces

Java provides many built-in functional interfaces in `java.util.function`:

### Predicate<T> - Test a Condition

Takes a value, returns boolean:

```java
import java.util.function.Predicate;

// Test if a string is long
Predicate<String> isLong = s -> s.length() > 5;

boolean result = isLong.test("Hello");      // false
boolean result2 = isLong.test("Hello World"); // true

// Use with streams
List<String> longNames = names.stream()
    .filter(isLong)
    .collect(Collectors.toList());

// Common predicates
Predicate<Integer> isPositive = n -> n > 0;
Predicate<String> isEmpty = s -> s.isEmpty();
Predicate<Object> isNull = obj -> obj == null;
```

### Function<T, R> - Transform a Value

Takes a value of type T, returns a value of type R:

```java
import java.util.function.Function;

// Convert string to its length
Function<String, Integer> length = s -> s.length();

int len = length.apply("Hello");  // 5

// Convert string to uppercase
Function<String, String> upper = s -> s.toUpperCase();

String result = upper.apply("hello");  // "HELLO"

// Use with streams
List<Integer> lengths = names.stream()
    .map(length)
    .collect(Collectors.toList());
```

### Consumer<T> - Consume a Value

Takes a value, returns nothing (void):

```java
import java.util.function.Consumer;

// Print a value
Consumer<String> printer = s -> System.out.println(s);

printer.accept("Hello");  // Prints: Hello

// Use with forEach
names.forEach(printer);

// Or inline
names.forEach(name -> System.out.println(name));
```

### Supplier<T> - Supply a Value

Takes nothing, returns a value:

```java
import java.util.function.Supplier;

// Supply a random number
Supplier<Double> random = () -> Math.random();

double r1 = random.get();  // Random number
double r2 = random.get();  // Another random number

// Supply a new object
Supplier<List<String>> listFactory = () -> new ArrayList<>();

List<String> list1 = listFactory.get();  // New list
List<String> list2 = listFactory.get();  // Another new list

// Lazy initialization
Supplier<ExpensiveObject> lazy = () -> new ExpensiveObject();
// Object not created until get() is called
```

### BiFunction<T, U, R> - Two Inputs

Takes two values, returns one value:

```java
import java.util.function.BiFunction;

// Add two numbers
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

int sum = add.apply(5, 3);  // 8

// Combine strings
BiFunction<String, String, String> concat = (a, b) -> a + " " + b;

String full = concat.apply("Hello", "World");  // "Hello World"
```

### UnaryOperator<T> - Same Type In and Out

Special case of Function where input and output are the same type:

```java
import java.util.function.UnaryOperator;

UnaryOperator<String> upper = s -> s.toUpperCase();
UnaryOperator<Integer> doubleIt = n -> n * 2;

String result = upper.apply("hello");  // "HELLO"
int doubled = doubleIt.apply(5);       // 10
```

### BinaryOperator<T> - Two Same Types

Special case of BiFunction where all types are the same:

```java
import java.util.function.BinaryOperator;

BinaryOperator<Integer> add = (a, b) -> a + b;
BinaryOperator<String> concat = (a, b) -> a + b;

int sum = add.apply(5, 3);             // 8
String combined = concat.apply("a", "b"); // "ab"
```

### Summary Table

| Interface | Parameters | Returns | Method |
|-----------|------------|---------|--------|
| `Predicate<T>` | T | boolean | `test(T)` |
| `Function<T,R>` | T | R | `apply(T)` |
| `Consumer<T>` | T | void | `accept(T)` |
| `Supplier<T>` | none | T | `get()` |
| `BiPredicate<T,U>` | T, U | boolean | `test(T,U)` |
| `BiFunction<T,U,R>` | T, U | R | `apply(T,U)` |
| `BiConsumer<T,U>` | T, U | void | `accept(T,U)` |
| `UnaryOperator<T>` | T | T | `apply(T)` |
| `BinaryOperator<T>` | T, T | T | `apply(T,T)` |

---

## Primitive Functional Interfaces

To avoid boxing/unboxing overhead, Java provides primitive specializations:

### IntPredicate, LongPredicate, DoublePredicate

```java
import java.util.function.IntPredicate;

// No boxing - works directly with int
IntPredicate isEven = n -> n % 2 == 0;

boolean result = isEven.test(4);  // true - no Integer boxing

// Compare with Predicate<Integer>
Predicate<Integer> isEvenBoxed = n -> n % 2 == 0;
boolean result2 = isEvenBoxed.test(4);  // Boxing happens
```

### IntFunction, LongFunction, DoubleFunction

```java
import java.util.function.IntFunction;

// int -> R (no boxing of input)
IntFunction<String> intToString = n -> "Number: " + n;

String result = intToString.apply(42);  // "Number: 42"
```

### ToIntFunction, ToLongFunction, ToDoubleFunction

```java
import java.util.function.ToIntFunction;

// T -> int (no boxing of output)
ToIntFunction<String> stringLength = s -> s.length();

int len = stringLength.applyAsInt("Hello");  // 5
```

### IntConsumer, LongConsumer, DoubleConsumer

```java
import java.util.function.IntConsumer;

IntConsumer printInt = n -> System.out.println(n);

printInt.accept(42);  // Prints: 42
```

### IntSupplier, LongSupplier, DoubleSupplier

```java
import java.util.function.IntSupplier;

IntSupplier randomInt = () -> (int)(Math.random() * 100);

int value = randomInt.getAsInt();  // Random int 0-99
```

### IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator

```java
import java.util.function.IntUnaryOperator;

IntUnaryOperator doubleIt = n -> n * 2;

int result = doubleIt.applyAsInt(5);  // 10
```

### IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator

```java
import java.util.function.IntBinaryOperator;

IntBinaryOperator add = (a, b) -> a + b;

int sum = add.applyAsInt(5, 3);  // 8
```

---

## Method References

Method references are shortcuts for lambdas that just call an existing method:

### Types of Method References

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static method | `Class::staticMethod` | `x -> Class.staticMethod(x)` |
| Instance method of object | `object::method` | `x -> object.method(x)` |
| Instance method of parameter | `Class::instanceMethod` | `x -> x.instanceMethod()` |
| Constructor | `Class::new` | `x -> new Class(x)` |

### Static Method Reference

```java
// Lambda
Function<String, Integer> parse1 = s -> Integer.parseInt(s);

// Method reference
Function<String, Integer> parse2 = Integer::parseInt;

// Both work the same
int num1 = parse1.apply("42");  // 42
int num2 = parse2.apply("42");  // 42
```

More examples:

```java
// Math.abs
Function<Integer, Integer> abs = Math::abs;

// String.valueOf
Function<Integer, String> toString = String::valueOf;

// System.out.println
Consumer<String> print = System.out::println;
```

### Instance Method Reference (Bound)

Reference to a method on a specific object:

```java
String prefix = "Hello, ";

// Lambda
Function<String, String> greeter1 = name -> prefix.concat(name);

// Method reference (bound to prefix object)
Function<String, String> greeter2 = prefix::concat;

String result = greeter2.apply("World");  // "Hello, World"
```

More examples:

```java
List<String> names = new ArrayList<>();

// Lambda
Consumer<String> adder1 = name -> names.add(name);

// Method reference
Consumer<String> adder2 = names::add;

adder2.accept("Alice");  // Adds to the list
```

### Instance Method Reference (Unbound)

Reference to a method using the first parameter as the instance:

```java
// Lambda - s is the instance
Function<String, String> upper1 = s -> s.toUpperCase();

// Method reference - first parameter becomes "this"
Function<String, String> upper2 = String::toUpperCase;

String result = upper2.apply("hello");  // "HELLO"
```

For methods with parameters:

```java
// Lambda
BiFunction<String, String, Boolean> contains1 = (s, sub) -> s.contains(sub);

// Method reference - first param is instance, second is argument
BiFunction<String, String, Boolean> contains2 = String::contains;

boolean result = contains2.apply("Hello", "ell");  // true
```

### Constructor Reference

```java
// Lambda
Supplier<ArrayList<String>> listFactory1 = () -> new ArrayList<>();

// Constructor reference
Supplier<ArrayList<String>> listFactory2 = ArrayList::new;

List<String> list = listFactory2.get();  // New ArrayList
```

With parameters:

```java
// Lambda
Function<String, StringBuilder> sbFactory1 = s -> new StringBuilder(s);

// Constructor reference
Function<String, StringBuilder> sbFactory2 = StringBuilder::new;

StringBuilder sb = sbFactory2.apply("Hello");
```

Array constructor:

```java
// Lambda
IntFunction<String[]> arrayFactory1 = size -> new String[size];

// Constructor reference
IntFunction<String[]> arrayFactory2 = String[]::new;

String[] array = arrayFactory2.apply(5);  // String array of size 5
```

### When to Use Method References

Use method references when the lambda just calls an existing method:

```java
// Good - lambda just calls method
names.forEach(System.out::println);       // Instead of: name -> System.out.println(name)
names.stream().map(String::toUpperCase);  // Instead of: s -> s.toUpperCase()
names.stream().filter(String::isEmpty);   // Instead of: s -> s.isEmpty()

// Keep lambda when doing more than calling a method
names.stream()
    .filter(name -> name.length() > 3)    // Keep lambda - has logic
    .map(String::toUpperCase)             // Use reference - just method call
    .forEach(System.out::println);        // Use reference - just method call
```

---

## Variable Capture

Lambdas can use variables from the enclosing scope:

### Effectively Final Variables

```java
String greeting = "Hello";  // Effectively final (never reassigned)

Consumer<String> greeter = name -> System.out.println(greeting + ", " + name);

greeter.accept("World");  // "Hello, World"
```

The variable must be **effectively final** - it cannot be reassigned:

```java
String greeting = "Hello";
greeting = "Hi";  // Now it's not effectively final!

// Error: Variable used in lambda must be final or effectively final
Consumer<String> greeter = name -> System.out.println(greeting + ", " + name);
```

### Why Effectively Final?

```java
int counter = 0;

// This would be dangerous if allowed:
// Runnable r = () -> counter++;  // Error!

// The lambda might run later, on a different thread
// What would counter be? It's confusing and error-prone
```

### Workarounds for Mutable State

```java
// Use an array (the array reference is final, but contents can change)
int[] counter = {0};
Runnable r = () -> counter[0]++;

// Use AtomicInteger
AtomicInteger atomicCounter = new AtomicInteger(0);
Runnable r2 = () -> atomicCounter.incrementAndGet();

// Use a mutable wrapper class
class Counter {
    int value = 0;
}
Counter c = new Counter();
Runnable r3 = () -> c.value++;
```

### Instance Variables

Instance variables can be modified (they're accessed through `this`, which is effectively final):

```java
public class Counter {
    private int count = 0;  // Instance variable
    
    public void incrementAsync() {
        // OK - accessing instance variable through 'this'
        Runnable r = () -> count++;
        new Thread(r).start();
    }
}
```

---

## Composing Lambdas

Functional interfaces provide methods to combine lambdas:

### Predicate Composition

```java
Predicate<String> isLong = s -> s.length() > 5;
Predicate<String> startsWithA = s -> s.startsWith("A");

// AND
Predicate<String> longAndStartsWithA = isLong.and(startsWithA);

// OR
Predicate<String> longOrStartsWithA = isLong.or(startsWithA);

// NOT (negate)
Predicate<String> isShort = isLong.negate();

// Usage
boolean result = longAndStartsWithA.test("Alexandra");  // true
```

### Function Composition

```java
Function<String, String> trim = String::trim;
Function<String, String> upper = String::toUpperCase;
Function<String, Integer> length = String::length;

// andThen - apply this, then that
Function<String, String> trimThenUpper = trim.andThen(upper);
String result = trimThenUpper.apply("  hello  ");  // "HELLO"

// compose - apply that first, then this
Function<String, String> upperThenTrim = upper.compose(trim);
String result2 = upperThenTrim.apply("  hello  ");  // "HELLO"

// Chain multiple
Function<String, Integer> process = trim.andThen(upper).andThen(length);
int len = process.apply("  hello  ");  // 5
```

### Consumer Composition

```java
Consumer<String> print = System.out::println;
Consumer<String> log = s -> logger.info(s);

// andThen - do this, then that
Consumer<String> printAndLog = print.andThen(log);

printAndLog.accept("Hello");  // Prints and logs
```

---

## Lambdas and Exceptions

### Checked Exceptions Problem

Functional interfaces don't declare checked exceptions:

```java
// This won't compile!
Function<String, String> readFile = path -> {
    return Files.readString(Path.of(path));  // Throws IOException!
};
```

### Solution 1: Wrap in Runtime Exception

```java
Function<String, String> readFile = path -> {
    try {
        return Files.readString(Path.of(path));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
};
```

### Solution 2: Create Throwing Functional Interface

```java
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}

// Use it
ThrowingFunction<String, String, IOException> readFile = 
    path -> Files.readString(Path.of(path));

// Wrap for regular Function use
public static <T, R> Function<T, R> wrap(ThrowingFunction<T, R, ?> f) {
    return t -> {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}

// Usage
Function<String, String> safeRead = wrap(path -> Files.readString(Path.of(path)));
```

### Solution 3: Use Sneaky Throws

```java
@SuppressWarnings("unchecked")
public static <E extends Exception> void sneakyThrow(Exception e) throws E {
    throw (E) e;
}

Function<String, String> readFile = path -> {
    try {
        return Files.readString(Path.of(path));
    } catch (IOException e) {
        sneakyThrow(e);
        return null;  // Never reached
    }
};
```

---

## Lambdas with Streams

Lambdas are heavily used with the Stream API:

### Filtering

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Filter with lambda
List<String> longNames = names.stream()
    .filter(name -> name.length() > 4)
    .collect(Collectors.toList());
// [Alice, Charlie, David]
```

### Mapping

```java
// Transform each element
List<Integer> lengths = names.stream()
    .map(name -> name.length())
    .collect(Collectors.toList());
// [5, 3, 7, 5]

// Or with method reference
List<Integer> lengths2 = names.stream()
    .map(String::length)
    .collect(Collectors.toList());
```

### Reducing

```java
// Combine all elements
int totalLength = names.stream()
    .mapToInt(String::length)
    .sum();
// 20

// Custom reduction
String combined = names.stream()
    .reduce("", (a, b) -> a + b);
// "AliceBobCharlieDavid"
```

### Sorting

```java
// Sort with lambda
List<String> sorted = names.stream()
    .sorted((a, b) -> a.length() - b.length())
    .collect(Collectors.toList());

// Or with Comparator
List<String> sorted2 = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
```

### ForEach

```java
// Process each element
names.forEach(name -> System.out.println("Hello, " + name));

// Or with method reference
names.forEach(System.out::println);
```

---

## Practical Examples

### Event Handling

```java
// Swing button
button.addActionListener(e -> System.out.println("Clicked!"));

// Multiple statements
button.addActionListener(e -> {
    System.out.println("Button: " + e.getSource());
    handleClick();
});
```

### Threading

```java
// Create a thread
Thread thread = new Thread(() -> {
    System.out.println("Running in thread");
});
thread.start();

// Runnable
Runnable task = () -> System.out.println("Task running");
executor.submit(task);

// Callable
Callable<String> callable = () -> {
    Thread.sleep(1000);
    return "Done";
};
Future<String> future = executor.submit(callable);
```

### Comparators

```java
List<Person> people = getPeople();

// Sort by name
people.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

// Sort by age
people.sort((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));

// Using Comparator methods
people.sort(Comparator.comparing(Person::getName));
people.sort(Comparator.comparingInt(Person::getAge));

// Multiple criteria
people.sort(Comparator
    .comparing(Person::getLastName)
    .thenComparing(Person::getFirstName)
    .thenComparingInt(Person::getAge));

// Reversed
people.sort(Comparator.comparing(Person::getAge).reversed());
```

### Optional Handling

```java
Optional<String> name = findName();

// Map with lambda
Optional<String> upper = name.map(s -> s.toUpperCase());

// Filter with lambda
Optional<String> longName = name.filter(s -> s.length() > 5);

// OrElseGet with supplier
String result = name.orElseGet(() -> generateDefault());

// IfPresent with consumer
name.ifPresent(n -> System.out.println("Found: " + n));
```

### Custom Functional Methods

```java
public class ListUtils {
    
    // Filter with custom predicate
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    // Transform with custom function
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    // Process with custom consumer
    public static <T> void forEach(List<T> list, Consumer<T> action) {
        for (T item : list) {
            action.accept(item);
        }
    }
}

// Usage
List<String> names = List.of("Alice", "Bob", "Charlie");

List<String> longNames = ListUtils.filter(names, n -> n.length() > 3);
List<Integer> lengths = ListUtils.map(names, String::length);
ListUtils.forEach(names, System.out::println);
```

---

## Lambda Performance

### Compared to Anonymous Classes

Lambdas are generally more efficient:

```java
// Anonymous class - creates new class file, new instance each time
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda - uses invokedynamic, may reuse instances
Runnable r2 = () -> System.out.println("Hello");
```

### Caching Lambdas

For stateless lambdas, the JVM may reuse the same instance:

```java
// These might be the same object
Runnable r1 = () -> System.out.println("Hello");
Runnable r2 = () -> System.out.println("Hello");
// r1 == r2 might be true
```

### Capturing Variables Affects Caching

```java
// Non-capturing lambda - can be cached
Predicate<String> isLong = s -> s.length() > 5;

// Capturing lambda - new instance each time
int threshold = 5;
Predicate<String> isLong2 = s -> s.length() > threshold;
```

---

## Best Practices

### 1. Keep Lambdas Short

```java
// Good - short and clear
names.stream()
    .filter(name -> name.length() > 5)
    .forEach(System.out::println);

// Bad - too long, extract to method
names.stream()
    .filter(name -> {
        if (name == null) return false;
        if (name.isEmpty()) return false;
        if (name.length() <= 5) return false;
        return name.matches("[A-Z].*");
    })
    .forEach(System.out::println);

// Better - extract to method
names.stream()
    .filter(this::isValidLongName)
    .forEach(System.out::println);

private boolean isValidLongName(String name) {
    if (name == null || name.isEmpty()) return false;
    if (name.length() <= 5) return false;
    return name.matches("[A-Z].*");
}
```

### 2. Use Method References When Possible

```java
// Good - method reference
names.forEach(System.out::println);
names.stream().map(String::toUpperCase);

// Less good - unnecessary lambda
names.forEach(name -> System.out.println(name));
names.stream().map(name -> name.toUpperCase());
```

### 3. Prefer Standard Functional Interfaces

```java
// Good - use standard interface
Function<String, Integer> length = String::length;

// Avoid - creating custom interface when standard exists
interface StringToInt {
    int convert(String s);
}
```

### 4. Name Complex Lambdas

```java
// Hard to read
process(data, (x, y) -> x.getValue() > y.getValue() ? x : y);

// Better - named and documented
BinaryOperator<Item> selectHigherValue = (x, y) -> 
    x.getValue() > y.getValue() ? x : y;
process(data, selectHigherValue);
```

### 5. Avoid Side Effects

```java
// Bad - side effect in lambda
List<String> results = new ArrayList<>();
names.stream()
    .filter(name -> name.length() > 3)
    .forEach(name -> results.add(name));  // Side effect!

// Good - use collect
List<String> results = names.stream()
    .filter(name -> name.length() > 3)
    .collect(Collectors.toList());
```

---

## Common Mistakes

### Mistake 1: Modifying Captured Variables

```java
int count = 0;
// Error: Variable used in lambda must be final or effectively final
names.forEach(name -> count++);

// Fix: Use AtomicInteger
AtomicInteger count = new AtomicInteger(0);
names.forEach(name -> count.incrementAndGet());
```

### Mistake 2: Ignoring Return Type

```java
// Bug: Missing return in block lambda
Function<String, String> upper = s -> {
    s.toUpperCase();  // Oops! Forgot return
};

// Fix
Function<String, String> upper = s -> {
    return s.toUpperCase();
};

// Or use expression form
Function<String, String> upper = s -> s.toUpperCase();
```

### Mistake 3: Wrong Functional Interface

```java
// Bug: Predicate returns boolean, not void
Predicate<String> printer = s -> System.out.println(s);  // Error!

// Fix: Use Consumer for void operations
Consumer<String> printer = s -> System.out.println(s);
```

### Mistake 4: Overusing Lambdas

```java
// Too complex - hard to read and debug
result = items.stream()
    .filter(i -> i.getStatus() == Status.ACTIVE && 
                 i.getDate().isAfter(cutoff) &&
                 i.getCategory().equals(category) &&
                 i.getValue() > threshold)
    .map(i -> new Result(i.getId(), 
                         i.getName().toUpperCase(),
                         calculateScore(i)))
    .collect(Collectors.toList());

// Better - extract predicates and use methods
Predicate<Item> isRelevant = this::isRelevantItem;
Function<Item, Result> toResult = this::convertToResult;

result = items.stream()
    .filter(isRelevant)
    .map(toResult)
    .collect(Collectors.toList());
```

---

## Cheat Sheet

| Lambda | Description |
|--------|-------------|
| `() -> expr` | No parameters |
| `x -> expr` | One parameter |
| `(x, y) -> expr` | Multiple parameters |
| `(x, y) -> { stmts; }` | Block body |

| Method Reference | Lambda Equivalent |
|------------------|-------------------|
| `Class::staticMethod` | `x -> Class.staticMethod(x)` |
| `obj::method` | `x -> obj.method(x)` |
| `Class::method` | `x -> x.method()` |
| `Class::new` | `x -> new Class(x)` |

| Interface | Method | Signature |
|-----------|--------|-----------|
| `Predicate<T>` | `test` | `T -> boolean` |
| `Function<T,R>` | `apply` | `T -> R` |
| `Consumer<T>` | `accept` | `T -> void` |
| `Supplier<T>` | `get` | `() -> T` |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Annotations](./23-annotations.md) | [Guide](../guide.md) | [Functional Interfaces](./25-functional-interfaces.md) |
