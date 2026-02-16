# Methods

[<- Previous: Strings](06-strings.md) | [Next: Math ->](08-math.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Methods Cheat Sheet](../cheatsheets/methods-cheatsheet.md)

---

## Overview

A method is a block of code that performs a specific task. Methods allow you to organize code into reusable, logical units. They are also called functions in other programming languages.

**Why Use Methods?**
- **Reusability** - Write once, use many times
- **Organization** - Break complex problems into smaller, manageable pieces
- **Maintainability** - Easier to update and fix bugs
- **Readability** - Self-documenting code with descriptive method names
- **Testing** - Easier to test individual components

---

## Method Declaration

### Basic Syntax

```java
accessModifier returnType methodName(parameterList) {
    // method body
    return value;  // if returnType is not void
}
```

### Components of a Method

```java
public static int add(int a, int b) {
    return a + b;
}
```

| Component | Description | Example |
|-----------|-------------|---------|
| Access Modifier | Controls visibility | `public`, `private`, `protected` |
| Static Modifier | Belongs to class, not instance | `static` (optional) |
| Return Type | Type of value returned | `int`, `String`, `void` |
| Method Name | Identifier for the method | `add` |
| Parameters | Input values | `int a, int b` |
| Method Body | Code to execute | `return a + b;` |

### Simple Method Examples

```java
// Method with no parameters, no return value
public void sayHello() {
    System.out.println("Hello!");
}

// Method with parameters, no return value
public void greet(String name) {
    System.out.println("Hello, " + name + "!");
}

// Method with parameters and return value
public int multiply(int x, int y) {
    return x * y;
}

// Method returning a boolean
public boolean isEven(int number) {
    return number % 2 == 0;
}
```

---

## Calling Methods

### Calling Instance Methods

Instance methods require an object to be called.

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int subtract(int a, int b) {
        return a - b;
    }
}

// Calling methods
Calculator calc = new Calculator();
int sum = calc.add(5, 3);        // 8
int diff = calc.subtract(10, 4); // 6
```

### Calling Static Methods

Static methods are called on the class, not an instance.

```java
public class MathUtils {
    public static int square(int n) {
        return n * n;
    }
    
    public static double average(double a, double b) {
        return (a + b) / 2;
    }
}

// Calling static methods
int result = MathUtils.square(5);      // 25
double avg = MathUtils.average(10, 20); // 15.0

// Can also call from within the same class without class name
int sq = square(4);  // If called from within MathUtils
```

### Calling Methods Within Methods

```java
public class Example {
    public int addThree(int a, int b, int c) {
        return add(add(a, b), c);  // Calling add() within addThree()
    }
    
    public int add(int a, int b) {
        return a + b;
    }
}
```

---

## Method Parameters

### Parameter Types

Parameters are variables that receive values when the method is called.

```java
// Single parameter
public void printMessage(String message) {
    System.out.println(message);
}

// Multiple parameters
public double calculateArea(double length, double width) {
    return length * width;
}

// Array parameter
public int sum(int[] numbers) {
    int total = 0;
    for (int num : numbers) {
        total += num;
    }
    return total;
}

// Object parameter
public void printPerson(Person person) {
    System.out.println(person.getName());
}
```

### Pass by Value

Java always passes arguments **by value**. For primitives, the value is copied. For objects, the reference is copied (not the object itself).

#### Primitives - Value is Copied

```java
public static void modifyPrimitive(int x) {
    x = x * 2;  // Modifies local copy only
    System.out.println("Inside method: " + x);  // 20
}

public static void main(String[] args) {
    int num = 10;
    modifyPrimitive(num);
    System.out.println("After method: " + num);  // 10 (unchanged)
}
```

#### Objects - Reference is Copied

```java
public static void modifyArray(int[] arr) {
    arr[0] = 100;  // Modifies actual array (same reference)
}

public static void reassignArray(int[] arr) {
    arr = new int[] {1, 2, 3};  // Creates new local reference
    // Original array unchanged
}

public static void main(String[] args) {
    int[] numbers = {10, 20, 30};
    
    modifyArray(numbers);
    System.out.println(numbers[0]);  // 100 (modified)
    
    reassignArray(numbers);
    System.out.println(numbers[0]);  // 100 (not reassigned)
}
```

### Final Parameters

Use `final` to prevent parameter reassignment within the method.

```java
public void process(final int value) {
    // value = 10;  // Compilation error!
    System.out.println(value);
}

public void processArray(final int[] arr) {
    arr[0] = 100;    // OK - modifying contents
    // arr = new int[5];  // Error - cannot reassign
}
```

---

## Return Types

### void - No Return Value

```java
public void printGreeting() {
    System.out.println("Hello!");
    // No return statement needed (optional: return;)
}

public void printIfPositive(int n) {
    if (n < 0) {
        return;  // Early exit
    }
    System.out.println(n);
}
```

### Primitive Return Types

```java
public int getInt() {
    return 42;
}

public double getDouble() {
    return 3.14159;
}

public boolean isValid() {
    return true;
}

public char getFirstLetter() {
    return 'A';
}
```

### Object Return Types

```java
public String getMessage() {
    return "Hello, World!";
}

public int[] getNumbers() {
    return new int[] {1, 2, 3, 4, 5};
}

public List<String> getNames() {
    return Arrays.asList("Alice", "Bob", "Charlie");
}

public Person createPerson(String name, int age) {
    return new Person(name, age);
}
```

### Returning null

```java
public String findName(int id) {
    if (id == 1) {
        return "Alice";
    }
    return null;  // Not found
}

// Better: Use Optional (Java 8+)
public Optional<String> findNameSafe(int id) {
    if (id == 1) {
        return Optional.of("Alice");
    }
    return Optional.empty();
}
```

### Returning Multiple Values

Java methods can only return one value, but you can use workarounds:

#### Using Arrays

```java
public int[] getMinMax(int[] numbers) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    
    for (int n : numbers) {
        if (n < min) min = n;
        if (n > max) max = n;
    }
    
    return new int[] {min, max};
}

// Usage
int[] result = getMinMax(new int[] {3, 1, 4, 1, 5});
System.out.println("Min: " + result[0] + ", Max: " + result[1]);
```

#### Using a Custom Class

```java
public class MinMax {
    public final int min;
    public final int max;
    
    public MinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }
}

public MinMax getMinMax(int[] numbers) {
    // ... calculate min and max
    return new MinMax(min, max);
}
```

#### Using Records (Java 16+)

```java
public record MinMax(int min, int max) {}

public MinMax getMinMax(int[] numbers) {
    // ... calculate
    return new MinMax(min, max);
}

// Usage
MinMax result = getMinMax(numbers);
System.out.println(result.min());  // Using record accessor
```

#### Using Map

```java
public Map<String, Integer> getStats(int[] numbers) {
    Map<String, Integer> stats = new HashMap<>();
    stats.put("min", Arrays.stream(numbers).min().orElse(0));
    stats.put("max", Arrays.stream(numbers).max().orElse(0));
    stats.put("sum", Arrays.stream(numbers).sum());
    return stats;
}
```

---

## Method Overloading

Method overloading allows multiple methods with the same name but different parameters.

### Rules for Overloading

1. Methods must have the same name
2. Methods must have different parameter lists (type, number, or order)
3. Return type alone is not sufficient for overloading
4. Access modifiers can be different

### Examples of Overloading

```java
public class Calculator {
    // Different number of parameters
    public int add(int a, int b) {
        return a + b;
    }
    
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Different parameter types
    public double add(double a, double b) {
        return a + b;
    }
    
    // Different parameter order
    public String add(String s, int n) {
        return s + n;
    }
    
    public String add(int n, String s) {
        return n + s;
    }
}

// Usage
Calculator calc = new Calculator();
System.out.println(calc.add(1, 2));           // 3 (int version)
System.out.println(calc.add(1, 2, 3));        // 6 (three params)
System.out.println(calc.add(1.5, 2.5));       // 4.0 (double version)
System.out.println(calc.add("Value: ", 42)); // "Value: 42"
```

### Invalid Overloading

```java
// ERROR: Cannot overload by return type alone
public int getValue() { return 1; }
public double getValue() { return 1.0; }  // Compilation error!

// ERROR: Cannot overload by parameter names alone
public void print(int a) { }
public void print(int b) { }  // Compilation error!
```

### Type Promotion in Overloading

When exact match is not found, Java promotes smaller types to larger types.

```java
public class TypePromotion {
    public void show(int x) {
        System.out.println("int: " + x);
    }
    
    public void show(long x) {
        System.out.println("long: " + x);
    }
    
    public void show(double x) {
        System.out.println("double: " + x);
    }
}

TypePromotion tp = new TypePromotion();
tp.show(10);      // int: 10 (exact match)
tp.show(10L);     // long: 10 (exact match)
tp.show(10.0);    // double: 10.0 (exact match)

byte b = 5;
tp.show(b);       // int: 5 (byte promoted to int)

short s = 100;
tp.show(s);       // int: 100 (short promoted to int)

float f = 1.5f;
tp.show(f);       // double: 1.5 (float promoted to double)
```

### Constructor Overloading

Constructors can also be overloaded:

```java
public class Person {
    private String name;
    private int age;
    
    public Person() {
        this("Unknown", 0);
    }
    
    public Person(String name) {
        this(name, 0);
    }
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

// Usage
Person p1 = new Person();              // Unknown, 0
Person p2 = new Person("Alice");       // Alice, 0
Person p3 = new Person("Bob", 25);     // Bob, 25
```

---

## Variable Arguments (Varargs)

Varargs allow you to pass zero or more arguments of the same type.

### Syntax

```java
public void methodName(Type... variableName) {
    // variableName is treated as an array
}
```

### Examples

```java
// Accept any number of integers
public int sum(int... numbers) {
    int total = 0;
    for (int n : numbers) {
        total += n;
    }
    return total;
}

// Usage
sum();              // 0
sum(5);             // 5
sum(1, 2, 3);       // 6
sum(1, 2, 3, 4, 5); // 15

// Can also pass an array
int[] arr = {1, 2, 3};
sum(arr);           // 6
```

### Varargs with Other Parameters

```java
// Varargs must be the last parameter
public void printAll(String prefix, int... numbers) {
    System.out.print(prefix + ": ");
    for (int n : numbers) {
        System.out.print(n + " ");
    }
    System.out.println();
}

// Usage
printAll("Numbers", 1, 2, 3);  // Numbers: 1 2 3
printAll("Empty");              // Empty:
```

### Varargs Rules

```java
// Only one varargs parameter allowed
// public void invalid(int... a, String... b) { }  // Error!

// Varargs must be last
// public void invalid(int... a, String b) { }  // Error!

// This is valid
public void valid(String a, int b, double... values) { }
```

### Varargs vs Array Parameter

```java
// Array parameter - must pass an array
public void withArray(int[] numbers) { }
withArray(new int[] {1, 2, 3});  // Must create array

// Varargs - more flexible
public void withVarargs(int... numbers) { }
withVarargs(1, 2, 3);            // Direct values
withVarargs(new int[] {1, 2, 3}); // Or array
withVarargs();                    // Or nothing
```

### Common Use Cases

```java
// printf-style formatting
public static void log(String format, Object... args) {
    System.out.printf(format + "%n", args);
}

log("Name: %s, Age: %d", "Alice", 30);

// Factory methods
public static <T> List<T> listOf(T... elements) {
    return Arrays.asList(elements);
}

List<String> names = listOf("Alice", "Bob", "Charlie");
```

---

## Recursion

Recursion is when a method calls itself to solve a problem.

### Basic Structure

```java
public returnType recursiveMethod(parameters) {
    // Base case - stops recursion
    if (baseCondition) {
        return baseValue;
    }
    
    // Recursive case - calls itself
    return recursiveMethod(modifiedParameters);
}
```

### Factorial Example

```java
public static long factorial(int n) {
    // Base case
    if (n <= 1) {
        return 1;
    }
    
    // Recursive case
    return n * factorial(n - 1);
}

// How it works:
// factorial(5) = 5 * factorial(4)
// factorial(4) = 4 * factorial(3)
// factorial(3) = 3 * factorial(2)
// factorial(2) = 2 * factorial(1)
// factorial(1) = 1 (base case)
// Result: 5 * 4 * 3 * 2 * 1 = 120
```

### Fibonacci Example

```java
public static int fibonacci(int n) {
    // Base cases
    if (n <= 0) return 0;
    if (n == 1) return 1;
    
    // Recursive case
    return fibonacci(n - 1) + fibonacci(n - 2);
}

// Note: This is inefficient. Use memoization or iteration for large n.
```

### Fibonacci with Memoization

```java
public static long fibonacciMemo(int n, Map<Integer, Long> memo) {
    if (n <= 0) return 0;
    if (n == 1) return 1;
    
    if (memo.containsKey(n)) {
        return memo.get(n);
    }
    
    long result = fibonacciMemo(n - 1, memo) + fibonacciMemo(n - 2, memo);
    memo.put(n, result);
    return result;
}

// Usage
Map<Integer, Long> memo = new HashMap<>();
long fib50 = fibonacciMemo(50, memo);  // Much faster!
```

### Sum of Array (Recursive)

```java
public static int sumArray(int[] arr, int index) {
    // Base case
    if (index >= arr.length) {
        return 0;
    }
    
    // Recursive case
    return arr[index] + sumArray(arr, index + 1);
}

// Usage
int[] numbers = {1, 2, 3, 4, 5};
int sum = sumArray(numbers, 0);  // 15
```

### Binary Search (Recursive)

```java
public static int binarySearch(int[] arr, int target, int low, int high) {
    // Base case - not found
    if (low > high) {
        return -1;
    }
    
    int mid = low + (high - low) / 2;
    
    // Base case - found
    if (arr[mid] == target) {
        return mid;
    }
    
    // Recursive cases
    if (arr[mid] > target) {
        return binarySearch(arr, target, low, mid - 1);
    } else {
        return binarySearch(arr, target, mid + 1, high);
    }
}

// Usage
int[] sorted = {1, 3, 5, 7, 9, 11, 13};
int index = binarySearch(sorted, 7, 0, sorted.length - 1);  // 3
```

### Recursion vs Iteration

| Aspect | Recursion | Iteration |
|--------|-----------|-----------|
| Readability | Often cleaner for tree/graph problems | Better for simple loops |
| Performance | Can be slower (function call overhead) | Usually faster |
| Memory | Uses stack space (risk of overflow) | Constant memory |
| Use case | Tree traversal, divide-and-conquer | Simple counting, array traversal |

### Avoiding Stack Overflow

```java
// Bad: Can cause StackOverflowError for large n
public static long badFactorial(int n) {
    return n * badFactorial(n - 1);  // No base case!
}

// Good: Always have a base case
public static long goodFactorial(int n) {
    if (n <= 1) return 1;  // Base case
    return n * goodFactorial(n - 1);
}

// Better: Use iteration for simple cases
public static long iterativeFactorial(int n) {
    long result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}
```

---

## Static Methods

Static methods belong to the class, not to instances of the class.

### Declaring Static Methods

```java
public class MathUtils {
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static double square(double n) {
        return n * n;
    }
    
    public static boolean isPositive(int n) {
        return n > 0;
    }
}

// Calling static methods
int sum = MathUtils.add(5, 3);
double sq = MathUtils.square(4.0);
boolean positive = MathUtils.isPositive(10);
```

### Static vs Instance Methods

```java
public class Counter {
    private static int totalCount = 0;  // Shared across instances
    private int instanceCount = 0;       // Unique to each instance
    
    // Instance method - can access both static and instance members
    public void increment() {
        instanceCount++;
        totalCount++;
    }
    
    // Static method - can only access static members
    public static int getTotalCount() {
        return totalCount;
        // return instanceCount;  // Error! Cannot access instance field
    }
    
    public int getInstanceCount() {
        return instanceCount;
    }
}

Counter c1 = new Counter();
Counter c2 = new Counter();
c1.increment();
c1.increment();
c2.increment();

System.out.println(c1.getInstanceCount());  // 2
System.out.println(c2.getInstanceCount());  // 1
System.out.println(Counter.getTotalCount()); // 3
```

### When to Use Static Methods

Use static methods when:
- The method does not use any instance variables
- The method is a utility function
- Factory methods
- The method should be callable without creating an instance

```java
public class StringUtils {
    // Utility method - no instance state needed
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    public static String capitalize(String s) {
        if (isEmpty(s)) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

// Factory method
public class Connection {
    private Connection() { }  // Private constructor
    
    public static Connection create(String url) {
        Connection conn = new Connection();
        // ... configure connection
        return conn;
    }
}
```

### Static Import

You can import static methods to use them without the class name.

```java
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import static java.lang.Math.*;  // Import all static members

public class Example {
    public void calculate() {
        double result = sqrt(16);        // Instead of Math.sqrt(16)
        double power = pow(2, 8);        // Instead of Math.pow(2, 8)
        double pi = PI;                  // Instead of Math.PI
    }
}
```

---

## Access Modifiers

Access modifiers control the visibility of methods.

### The Four Access Levels

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (default) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

### Examples

```java
public class AccessExample {
    // Accessible from anywhere
    public void publicMethod() { }
    
    // Accessible within package and subclasses
    protected void protectedMethod() { }
    
    // Accessible within package only (default/package-private)
    void packageMethod() { }
    
    // Accessible within this class only
    private void privateMethod() { }
    
    public void doSomething() {
        // All methods accessible here
        publicMethod();
        protectedMethod();
        packageMethod();
        privateMethod();
    }
}
```

### Best Practices

```java
public class BankAccount {
    private double balance;  // Private field
    
    // Public API
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        if (validateAmount(amount)) {
            balance += amount;
            logTransaction("deposit", amount);
        }
    }
    
    // Private helper methods
    private boolean validateAmount(double amount) {
        return amount > 0;
    }
    
    private void logTransaction(String type, double amount) {
        System.out.println(type + ": " + amount);
    }
}
```

---

## Final Methods

A `final` method cannot be overridden by subclasses.

```java
public class Parent {
    // Cannot be overridden
    public final void importantMethod() {
        System.out.println("This implementation is final");
    }
    
    // Can be overridden
    public void normalMethod() {
        System.out.println("This can be overridden");
    }
}

public class Child extends Parent {
    // Error! Cannot override final method
    // public void importantMethod() { }
    
    @Override
    public void normalMethod() {
        System.out.println("Overridden in Child");
    }
}
```

### When to Use Final Methods

- Security-critical methods that should not be changed
- Template methods where certain steps must not be modified
- Performance optimization (JVM can inline final methods)

---

## Abstract Methods

Abstract methods have no implementation and must be overridden by subclasses.

```java
public abstract class Shape {
    // Abstract method - no body
    public abstract double calculateArea();
    
    // Abstract method with parameters
    public abstract void draw(Graphics g);
    
    // Regular method with implementation
    public void printInfo() {
        System.out.println("Area: " + calculateArea());
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public void draw(Graphics g) {
        // Draw circle implementation
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public void draw(Graphics g) {
        // Draw rectangle implementation
    }
}
```

---

## Interface Methods

### Abstract Methods in Interfaces

By default, interface methods are public and abstract.

```java
public interface Drawable {
    void draw();  // Implicitly public abstract
    
    void resize(int width, int height);
}

public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
    
    @Override
    public void resize(int width, int height) {
        System.out.println("Resizing circle");
    }
}
```

### Default Methods (Java 8+)

Default methods provide a default implementation.

```java
public interface Vehicle {
    void start();
    void stop();
    
    // Default method
    default void honk() {
        System.out.println("Beep beep!");
    }
    
    default String getInfo() {
        return "This is a vehicle";
    }
}

public class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car starting");
    }
    
    @Override
    public void stop() {
        System.out.println("Car stopping");
    }
    
    // Can use default honk() or override it
    @Override
    public void honk() {
        System.out.println("Car horn: HONK!");
    }
}

Car car = new Car();
car.honk();     // "Car horn: HONK!" (overridden)
car.getInfo();  // "This is a vehicle" (default)
```

### Static Methods in Interfaces (Java 8+)

```java
public interface StringUtils {
    static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}

// Call static interface methods
boolean empty = StringUtils.isEmpty("");
String reversed = StringUtils.reverse("hello");
```

### Private Methods in Interfaces (Java 9+)

Private methods help avoid code duplication in default methods.

```java
public interface Logger {
    default void logInfo(String message) {
        log("INFO", message);
    }
    
    default void logError(String message) {
        log("ERROR", message);
    }
    
    // Private helper method
    private void log(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
    
    // Private static method
    private static String formatMessage(String msg) {
        return msg.trim().toUpperCase();
    }
}
```

---

## Method References (Java 8+)

Method references provide a shorthand for lambda expressions that call a single method.

### Types of Method References

| Type | Syntax | Lambda Equivalent |
|------|--------|-------------------|
| Static method | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` |
| Instance method (specific object) | `object::instanceMethod` | `(args) -> object.instanceMethod(args)` |
| Instance method (any object) | `ClassName::instanceMethod` | `(obj, args) -> obj.instanceMethod(args)` |
| Constructor | `ClassName::new` | `(args) -> new ClassName(args)` |

### Static Method Reference

```java
// Lambda
List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
numbers.sort((a, b) -> Integer.compare(a, b));

// Method reference
numbers.sort(Integer::compare);

// Another example
List<String> strings = Arrays.asList("1", "2", "3");
List<Integer> integers = strings.stream()
    .map(Integer::parseInt)  // Instead of s -> Integer.parseInt(s)
    .collect(Collectors.toList());
```

### Instance Method Reference (Specific Object)

```java
String prefix = "Hello, ";

// Lambda
Function<String, String> greeter1 = name -> prefix.concat(name);

// Method reference
Function<String, String> greeter2 = prefix::concat;

System.out.println(greeter2.apply("World"));  // "Hello, World"
```

### Instance Method Reference (Any Object of Type)

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Lambda
names.forEach(name -> System.out.println(name));

// Method reference
names.forEach(System.out::println);

// String methods
List<String> upperNames = names.stream()
    .map(String::toUpperCase)  // Calls toUpperCase on each String
    .collect(Collectors.toList());
```

### Constructor Reference

```java
// Lambda
Supplier<List<String>> listSupplier1 = () -> new ArrayList<>();

// Constructor reference
Supplier<List<String>> listSupplier2 = ArrayList::new;

// With parameters
Function<String, StringBuilder> sbCreator = StringBuilder::new;
StringBuilder sb = sbCreator.apply("Hello");

// Array constructor
Function<Integer, int[]> arrayCreator = int[]::new;
int[] arr = arrayCreator.apply(5);  // new int[5]
```

### Practical Examples

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    public static int compareByAge(Person a, Person b) {
        return Integer.compare(a.getAge(), b.getAge());
    }
}

List<Person> people = Arrays.asList(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
);

// Sort by age using static method reference
people.sort(Person::compareByAge);

// Sort by name using instance method reference
people.sort(Comparator.comparing(Person::getName));

// Get all names
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());
```

---

## Method Chaining

Method chaining allows calling multiple methods in a single statement.

### Returning this

```java
public class Builder {
    private String name;
    private int age;
    private String email;
    
    public Builder setName(String name) {
        this.name = name;
        return this;  // Return this for chaining
    }
    
    public Builder setAge(int age) {
        this.age = age;
        return this;
    }
    
    public Builder setEmail(String email) {
        this.email = email;
        return this;
    }
    
    public Person build() {
        return new Person(name, age, email);
    }
}

// Usage with chaining
Person person = new Builder()
    .setName("Alice")
    .setAge(30)
    .setEmail("alice@example.com")
    .build();
```

### Fluent Interface Pattern

```java
public class Query {
    private String table;
    private String[] columns;
    private String condition;
    private int limit;
    
    public Query select(String... columns) {
        this.columns = columns;
        return this;
    }
    
    public Query from(String table) {
        this.table = table;
        return this;
    }
    
    public Query where(String condition) {
        this.condition = condition;
        return this;
    }
    
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public String build() {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(String.join(", ", columns));
        sql.append(" FROM ").append(table);
        if (condition != null) {
            sql.append(" WHERE ").append(condition);
        }
        if (limit > 0) {
            sql.append(" LIMIT ").append(limit);
        }
        return sql.toString();
    }
}

// Usage
String sql = new Query()
    .select("id", "name", "email")
    .from("users")
    .where("active = true")
    .limit(10)
    .build();
// SELECT id, name, email FROM users WHERE active = true LIMIT 10
```

### Chaining with Streams

```java
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .distinct()
    .limit(5)
    .collect(Collectors.toList());
```

---

## Best Practices

### Method Naming Conventions

```java
// Use verbs for action methods
public void calculateTotal() { }
public void sendEmail() { }
public void processOrder() { }

// Use is/has/can for boolean methods
public boolean isEmpty() { }
public boolean hasPermission() { }
public boolean canExecute() { }

// Use get/set for accessors
public String getName() { }
public void setName(String name) { }

// Use to for conversions
public String toString() { }
public int[] toArray() { }

// Use of/from for factory methods (static)
public static LocalDate of(int year, int month, int day) { }
public static Period from(TemporalAmount amount) { }
```

### Keep Methods Short and Focused

```java
// Bad: One method doing too much
public void processOrder(Order order) {
    validateOrder(order);
    calculateTax(order);
    applyDiscount(order);
    updateInventory(order);
    sendConfirmation(order);
    logOrder(order);
}

// Good: Break into smaller methods
public void processOrder(Order order) {
    validateOrder(order);
    calculateTotal(order);
    updateInventory(order);
    notifyCustomer(order);
}

private void calculateTotal(Order order) {
    calculateTax(order);
    applyDiscount(order);
}

private void notifyCustomer(Order order) {
    sendConfirmation(order);
    logOrder(order);
}
```

### Limit Number of Parameters

```java
// Bad: Too many parameters
public void createUser(String firstName, String lastName, String email, 
                       String phone, String address, String city, 
                       String state, String zip, String country) { }

// Good: Use a parameter object
public void createUser(UserInfo userInfo) { }

// Or use a builder
public void createUser(User user) { }
User user = User.builder()
    .firstName("John")
    .lastName("Doe")
    .email("john@example.com")
    .build();
```

### Return Early

```java
// Bad: Deeply nested
public String process(String input) {
    if (input != null) {
        if (!input.isEmpty()) {
            if (input.length() > 5) {
                return input.toUpperCase();
            }
        }
    }
    return null;
}

// Good: Return early
public String process(String input) {
    if (input == null) return null;
    if (input.isEmpty()) return null;
    if (input.length() <= 5) return null;
    
    return input.toUpperCase();
}
```

### Document Public Methods

```java
/**
 * Calculates the total price including tax.
 *
 * @param items    the list of items to calculate
 * @param taxRate  the tax rate as a decimal (e.g., 0.08 for 8%)
 * @return the total price including tax
 * @throws IllegalArgumentException if items is null or taxRate is negative
 */
public double calculateTotal(List<Item> items, double taxRate) {
    if (items == null) {
        throw new IllegalArgumentException("Items cannot be null");
    }
    if (taxRate < 0) {
        throw new IllegalArgumentException("Tax rate cannot be negative");
    }
    
    double subtotal = items.stream()
        .mapToDouble(Item::getPrice)
        .sum();
    
    return subtotal * (1 + taxRate);
}
```

---

## Common Patterns

### Factory Method

```java
public class Connection {
    private String url;
    private int timeout;
    
    private Connection() { }  // Private constructor
    
    public static Connection create(String url) {
        Connection conn = new Connection();
        conn.url = url;
        conn.timeout = 30000;
        return conn;
    }
    
    public static Connection createWithTimeout(String url, int timeout) {
        Connection conn = new Connection();
        conn.url = url;
        conn.timeout = timeout;
        return conn;
    }
}

// Usage
Connection conn = Connection.create("jdbc:mysql://localhost/db");
```

### Template Method

```java
public abstract class DataProcessor {
    // Template method
    public final void process() {
        readData();
        processData();
        writeData();
    }
    
    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
}

public class CSVProcessor extends DataProcessor {
    @Override
    protected void readData() {
        System.out.println("Reading CSV file");
    }
    
    @Override
    protected void processData() {
        System.out.println("Processing CSV data");
    }
    
    @Override
    protected void writeData() {
        System.out.println("Writing output file");
    }
}
```

### Callback Pattern

```java
public interface Callback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}

public class AsyncTask {
    public void execute(Callback<String> callback) {
        try {
            String result = doWork();
            callback.onSuccess(result);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
    
    private String doWork() {
        return "Task completed";
    }
}

// Usage
AsyncTask task = new AsyncTask();
task.execute(new Callback<String>() {
    @Override
    public void onSuccess(String result) {
        System.out.println("Success: " + result);
    }
    
    @Override
    public void onError(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
});

// With lambda (Java 8+)
task.execute(new Callback<String>() {
    public void onSuccess(String result) { System.out.println(result); }
    public void onError(Exception e) { e.printStackTrace(); }
});
```

---

## Summary

### Method Declaration

| Component | Purpose |
|-----------|---------|
| Access modifier | `public`, `private`, `protected`, default |
| Static | Belongs to class, not instance |
| Return type | Type returned, or `void` |
| Method name | Identifier (camelCase) |
| Parameters | Input values with types |
| Body | Code block with implementation |

### Key Concepts

| Concept | Description |
|---------|-------------|
| Overloading | Same name, different parameters |
| Varargs | Variable number of arguments (`Type...`) |
| Recursion | Method calls itself |
| Pass by value | Java always copies values |
| Method reference | Shorthand for lambdas (`Class::method`) |
| Method chaining | Return `this` for fluent API |

### Method Types

| Type | Description |
|------|-------------|
| Instance method | Requires object, can access instance fields |
| Static method | Called on class, no access to instance fields |
| Final method | Cannot be overridden |
| Abstract method | No body, must be overridden |
| Default method | Interface method with implementation |

**Key Points:**
- Methods organize code into reusable units
- Use meaningful names that describe what the method does
- Keep methods focused on a single task
- Prefer returning values over modifying parameters
- Use overloading for similar operations on different types
- Use varargs for flexible argument counts
- Be careful with recursion (always have base case)
- Use static methods for utility functions
- Document public API methods with Javadoc

---

[<- Previous: Strings](06-strings.md) | [Next: Math ->](08-math.md) | [Back to Guide](../guide.md)
