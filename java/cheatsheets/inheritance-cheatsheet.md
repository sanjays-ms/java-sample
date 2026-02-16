# Inheritance Cheat Sheet

[Full Documentation](../documentation/12-inheritance.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)

---

## Basic Syntax

```java
// Parent class
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void eat() {
        System.out.println(name + " eats");
    }
}

// Child class
public class Dog extends Animal {
    public Dog(String name) {
        super(name);  // Call parent constructor
    }
    
    public void bark() {
        System.out.println(name + " barks");
    }
}
```

---

## What Gets Inherited

| Member | Inherited? |
|--------|------------|
| public fields/methods | Yes |
| protected fields/methods | Yes |
| package-private | Yes (same package) |
| private | No |
| Constructors | No (call via super) |

---

## super Keyword

```java
public class Child extends Parent {
    public Child(String value) {
        super(value);  // Call parent constructor (must be first!)
    }
    
    @Override
    public void process() {
        super.process();  // Call parent method
        // Additional logic
    }
    
    public void showName() {
        System.out.println(super.name);  // Access parent field
    }
}
```

---

## Method Overriding

```java
public class Parent {
    public String getValue() {
        return "parent";
    }
}

public class Child extends Parent {
    @Override  // Always use this annotation
    public String getValue() {
        return "child";
    }
}
```

### Overriding Rules

| Rule | Description |
|------|-------------|
| Same signature | Name + parameters must match |
| Return type | Same or subtype (covariant) |
| Access | Same or less restrictive |
| Exceptions | Same, narrower, or none |

```java
// Covariant return
class Parent {
    Animal getAnimal() { return new Animal(); }
}
class Child extends Parent {
    @Override
    Dog getAnimal() { return new Dog(); }  // Dog is subtype of Animal
}
```

---

## final Keyword

```java
// Cannot be extended
public final class String { }

// Cannot be overridden
public final void criticalMethod() { }
```

---

## Constructor Chaining

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public Person(String name) {
        this(name, 0);  // Chain to other constructor
    }
}

public class Employee extends Person {
    private String id;
    
    public Employee(String name, int age, String id) {
        super(name, age);  // Call parent constructor
        this.id = id;
    }
}
```

---

## instanceof Operator

```java
Animal animal = new Dog();

animal instanceof Dog     // true
animal instanceof Animal  // true
animal instanceof Cat     // false

// Pattern matching (Java 16+)
if (animal instanceof Dog dog) {
    dog.bark();  // No cast needed
}
```

---

## Sealed Classes (Java 17+)

```java
// Only listed classes can extend
public sealed class Shape 
    permits Circle, Rectangle, Triangle { }

public final class Circle extends Shape { }
public final class Rectangle extends Shape { }
public non-sealed class Triangle extends Shape { }
// Anyone can extend Triangle
```

---

## Inheritance vs Composition

```java
// IS-A: Use inheritance
class Dog extends Animal { }  // Dog IS-A Animal

// HAS-A: Use composition
class Car {
    private Engine engine;  // Car HAS-A Engine
}
```

---

## Quick Reference

### Constructor Rules

```java
class Child extends Parent {
    Child() {
        super();      // Must be first statement
    }                 // Added implicitly if not specified
}
```

### Static Methods

```java
// Static methods are HIDDEN, not overridden
Parent p = new Child();
p.staticMethod();  // Calls Parent.staticMethod()
```

### Object Class Methods

Every class extends Object. Override these:

```java
@Override
public String toString() { }

@Override
public boolean equals(Object obj) { }

@Override
public int hashCode() { }
```

---

## Common Patterns

### Template Method

```java
abstract class Report {
    public final void generate() {
        fetchData();
        processData();
        format();
    }
    
    protected abstract void fetchData();
    protected abstract void processData();
    protected void format() { }  // Optional override
}
```

### Factory Method

```java
abstract class Creator {
    public abstract Product createProduct();
    
    public void doSomething() {
        Product p = createProduct();
        p.use();
    }
}
```

---

[Full Documentation](../documentation/12-inheritance.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)
