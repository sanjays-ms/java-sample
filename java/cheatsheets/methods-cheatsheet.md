# Methods Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Methods](../documentation/07-methods.md)

Quick reference for Java methods.

---

## Method Declaration

```java
accessModifier static returnType methodName(parameters) {
    // body
    return value;
}
```

```java
public static int add(int a, int b) {
    return a + b;
}
```

---

## Method Types

```java
// No return, no parameters
public void sayHello() {
    System.out.println("Hello");
}

// With parameters, no return
public void greet(String name) {
    System.out.println("Hello, " + name);
}

// With return value
public int add(int a, int b) {
    return a + b;
}

// Boolean method
public boolean isEven(int n) {
    return n % 2 == 0;
}
```

---

## Calling Methods

```java
// Instance method (requires object)
Calculator calc = new Calculator();
int result = calc.add(5, 3);

// Static method (on class)
int result = Math.max(10, 20);
double sq = MathUtils.square(5);
```

---

## Parameters

### Pass by Value

```java
// Primitives - value copied (original unchanged)
void modify(int x) { x = 100; }

// Objects - reference copied (can modify contents)
void modify(int[] arr) { arr[0] = 100; }
```

### Final Parameters

```java
public void process(final int value) {
    // value = 10;  // Error: cannot reassign
}
```

---

## Return Types

```java
void           // No return value
int            // Primitive
String         // Object
int[]          // Array
List<String>   // Generic type
Optional<T>    // Optional wrapper
```

### Multiple Return Values

```java
// Using array
public int[] getMinMax(int[] arr) {
    return new int[] {min, max};
}

// Using Record (Java 16+)
public record Result(int min, int max) {}
public Result getMinMax(int[] arr) {
    return new Result(min, max);
}
```

---

## Method Overloading

```java
public int add(int a, int b) { return a + b; }
public int add(int a, int b, int c) { return a + b + c; }
public double add(double a, double b) { return a + b; }
```

Rules:
- Same name, different parameter list
- Return type alone is NOT enough
- Access modifier can differ

---

## Varargs

```java
public int sum(int... numbers) {
    int total = 0;
    for (int n : numbers) total += n;
    return total;
}

sum();           // 0
sum(1, 2);       // 3
sum(1, 2, 3, 4); // 10
```

Rules:
- Only ONE varargs per method
- Must be LAST parameter
- Treated as array inside method

---

## Recursion

```java
// Always have base case!
public int factorial(int n) {
    if (n <= 1) return 1;           // Base case
    return n * factorial(n - 1);     // Recursive case
}

public int fibonacci(int n) {
    if (n <= 1) return n;            // Base case
    return fibonacci(n-1) + fibonacci(n-2);  // Recursive
}
```

---

## Static vs Instance

```java
public class Example {
    private int instanceVar;          // Per-object
    private static int staticVar;     // Shared
    
    // Instance method - access both
    public void instanceMethod() {
        instanceVar++;
        staticVar++;
    }
    
    // Static method - only static members
    public static void staticMethod() {
        staticVar++;
        // instanceVar++;  // Error!
    }
}
```

---

## Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|:-----:|:-------:|:--------:|:-----:|
| `public` | Y | Y | Y | Y |
| `protected` | Y | Y | Y | N |
| (default) | Y | Y | N | N |
| `private` | Y | N | N | N |

---

## Final and Abstract

```java
// Final - cannot override
public final void cannotOverride() { }

// Abstract - must override (in abstract class)
public abstract void mustOverride();
```

---

## Interface Methods

```java
public interface MyInterface {
    void abstractMethod();              // Abstract (default)
    
    default void defaultMethod() { }    // Default impl (Java 8+)
    
    static void staticMethod() { }      // Static (Java 8+)
    
    private void helper() { }           // Private (Java 9+)
}
```

---

## Method References (Java 8+)

| Type | Syntax | Example |
|------|--------|---------|
| Static | `Class::staticMethod` | `Integer::parseInt` |
| Instance (specific) | `obj::method` | `str::length` |
| Instance (any) | `Class::method` | `String::toUpperCase` |
| Constructor | `Class::new` | `ArrayList::new` |

```java
// Lambda
list.forEach(s -> System.out.println(s));

// Method reference
list.forEach(System.out::println);

// More examples
list.stream().map(String::toUpperCase)
list.stream().sorted(Integer::compare)
list.stream().map(Person::getName)
```

---

## Method Chaining

```java
public class Builder {
    public Builder setName(String n) {
        this.name = n;
        return this;  // Return this
    }
    public Builder setAge(int a) {
        this.age = a;
        return this;
    }
}

// Usage
Person p = new Builder()
    .setName("Alice")
    .setAge(30)
    .build();
```

---

## Naming Conventions

| Type | Pattern | Examples |
|------|---------|----------|
| Actions | verb | `calculate()`, `send()`, `process()` |
| Booleans | is/has/can | `isEmpty()`, `hasNext()`, `canRead()` |
| Getters | get | `getName()`, `getAge()` |
| Setters | set | `setName()`, `setAge()` |
| Converters | to | `toString()`, `toArray()` |
| Factory | of/from | `List.of()`, `LocalDate.from()` |

---

## Best Practices

```java
// Return early
public String process(String s) {
    if (s == null) return null;
    if (s.isEmpty()) return "";
    return s.toUpperCase();
}

// Limit parameters (use object)
// Bad:  void create(String a, String b, String c, ...)
// Good: void create(UserInfo info)

// Keep methods focused (single responsibility)
// Bad:  validateAndSaveAndNotify()
// Good: validate() + save() + notify()
```

---

## Quick Reference

| Task | Example |
|------|---------|
| Declare | `public int add(int a, int b)` |
| Call instance | `obj.method()` |
| Call static | `Class.method()` |
| Return value | `return result;` |
| Return void | `return;` (optional) |
| Overload | Same name, different params |
| Varargs | `void m(int... nums)` |
| Final | `final void m()` |
| Abstract | `abstract void m();` |
| Static import | `import static pkg.Class.method;` |

---

## Common Patterns

```java
// Factory method
public static Connection create(String url) {
    return new Connection(url);
}

// Builder pattern
return new Builder().setX(x).setY(y).build();

// Callback
void process(Callback cb) {
    cb.onComplete(result);
}

// Template method
public final void execute() {
    step1();
    step2();  // Override this
    step3();
}
```
