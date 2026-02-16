# Abstraction Cheat Sheet

[Full Documentation](../documentation/14-abstraction.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)

---

## Abstract Classes

```java
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    // Abstract method - no body
    public abstract void makeSound();
    
    // Concrete method - has body
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}
```

### Abstract Class Rules

- Cannot be instantiated
- Can have constructors
- Can have abstract and concrete methods
- Can have any access modifiers
- Subclass must implement all abstract methods (or be abstract)

---

## Interfaces

```java
public interface Drawable {
    void draw();  // Abstract (implicit)
}

public interface Resizable {
    void resize(double factor);
    
    // Default method (Java 8+)
    default void reset() {
        resize(1.0);
    }
    
    // Static method
    static Resizable create() {
        return factor -> { };
    }
}

// Multiple interfaces
public class Circle implements Drawable, Resizable {
    @Override
    public void draw() { }
    
    @Override
    public void resize(double factor) { }
}
```

### Interface Members

| Member | Syntax |
|--------|--------|
| Constant | `int MAX = 100;` (implicit public static final) |
| Abstract | `void process();` (implicit public abstract) |
| Default | `default void log() { }` (Java 8+) |
| Static | `static void helper() { }` (Java 8+) |
| Private | `private void internal() { }` (Java 9+) |

---

## Functional Interfaces

Single abstract method, usable with lambdas.

```java
@FunctionalInterface
public interface Processor<T> {
    T process(T input);
}

// Lambda usage
Processor<String> upper = s -> s.toUpperCase();
String result = upper.process("hello");  // "HELLO"
```

### Common Functional Interfaces

| Interface | Method | Usage |
|-----------|--------|-------|
| `Predicate<T>` | `boolean test(T)` | Condition |
| `Function<T,R>` | `R apply(T)` | Transform |
| `Consumer<T>` | `void accept(T)` | Action |
| `Supplier<T>` | `T get()` | Factory |
| `UnaryOperator<T>` | `T apply(T)` | Same type transform |
| `BinaryOperator<T>` | `T apply(T,T)` | Combine two |

---

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Instantiate | No | No |
| Constructors | Yes | No |
| Fields | Any | public static final |
| Methods | Any | abstract, default, static |
| Inheritance | Single | Multiple |
| Lambdas | No | Yes (functional) |

### When to Use

```java
// Abstract class - shared code, state, related classes
public abstract class Vehicle {
    protected String vin;
    public abstract void start();
}

// Interface - contract, unrelated classes, multiple inheritance
public interface Drivable {
    void drive();
}
```

---

## Sealed Interfaces (Java 17+)

```java
public sealed interface Shape 
    permits Circle, Rectangle {
}

public final class Circle implements Shape { }
public final class Rectangle implements Shape { }
```

---

## Default Method Conflict

```java
interface A { default void greet() { } }
interface B { default void greet() { } }

class C implements A, B {
    @Override
    public void greet() {
        A.super.greet();  // Choose one
    }
}
```

---

## Common Patterns

### Template Method

```java
public abstract class Processor {
    public final void process() {
        step1();
        step2();
        step3();
    }
    
    protected abstract void step1();
    protected abstract void step2();
    protected void step3() { }  // Optional hook
}
```

### Strategy

```java
interface Strategy { void execute(); }

class Context {
    private Strategy strategy;
    void setStrategy(Strategy s) { strategy = s; }
    void run() { strategy.execute(); }
}
```

---

## Quick Reference

### Abstract Method Rules

- No body (no braces)
- Cannot be private, final, or static
- Must be implemented by concrete subclass

### Interface Method Rules

- Abstract methods are implicitly public
- Default methods need `default` keyword
- Static methods belong to interface, not inherited
- Private methods (Java 9+) for internal helpers

---

[Full Documentation](../documentation/14-abstraction.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)
