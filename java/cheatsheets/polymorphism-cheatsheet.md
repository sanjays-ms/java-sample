# Polymorphism Cheat Sheet

[Full Documentation](../documentation/13-polymorphism.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)

---

## Two Types of Polymorphism

| Type | Mechanism | When Resolved |
|------|-----------|---------------|
| Compile-time | Method overloading | Compile time |
| Runtime | Method overriding | Runtime |

---

## Compile-Time: Method Overloading

Same method name, different parameters.

```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
}

calc.add(5, 3);      // Calls add(int, int)
calc.add(5.5, 3.5);  // Calls add(double, double)
calc.add(1, 2, 3);   // Calls add(int, int, int)
```

### Valid Overloading

```java
void process(int x) { }
void process(String x) { }      // Different type
void process(int x, int y) { }  // Different count
void process(int x, String y) { }
void process(String x, int y) { }  // Different order
```

### Invalid Overloading

```java
int getValue() { }
String getValue() { }  // Error! Only return type differs
```

---

## Runtime: Method Overriding

Subclass provides specific implementation.

```java
class Animal {
    void speak() { System.out.println("..."); }
}

class Dog extends Animal {
    @Override
    void speak() { System.out.println("Woof!"); }
}

class Cat extends Animal {
    @Override
    void speak() { System.out.println("Meow!"); }
}

Animal a1 = new Dog();
Animal a2 = new Cat();
a1.speak();  // "Woof!"
a2.speak();  // "Meow!"
```

---

## Upcasting and Downcasting

```java
// Upcasting - implicit, always safe
Dog dog = new Dog();
Animal animal = dog;  // Dog -> Animal

// Downcasting - explicit, can fail
Animal animal2 = new Dog();
Dog dog2 = (Dog) animal2;  // OK

// Safe downcasting
if (animal instanceof Dog d) {
    d.bark();  // Pattern matching (Java 16+)
}
```

---

## Polymorphism with Interfaces

```java
interface Drawable {
    void draw();
}

class Circle implements Drawable {
    public void draw() { System.out.println("Circle"); }
}

class Square implements Drawable {
    public void draw() { System.out.println("Square"); }
}

// Polymorphic collection
List<Drawable> shapes = List.of(new Circle(), new Square());
for (Drawable d : shapes) {
    d.draw();  // Each draws itself
}
```

---

## Polymorphism with Abstract Classes

```java
abstract class Employee {
    abstract double calculatePay();
}

class FullTime extends Employee {
    double calculatePay() { return salary; }
}

class Contractor extends Employee {
    double calculatePay() { return hours * rate; }
}

// Polymorphic usage
List<Employee> employees = List.of(new FullTime(), new Contractor());
double total = employees.stream()
    .mapToDouble(Employee::calculatePay)
    .sum();
```

---

## Covariant Return Types

Override can return a subtype.

```java
class AnimalShelter {
    Animal adopt() { return new Animal(); }
}

class DogShelter extends AnimalShelter {
    @Override
    Dog adopt() { return new Dog(); }  // Dog is subtype of Animal
}

DogShelter shelter = new DogShelter();
Dog dog = shelter.adopt();  // No cast needed
```

---

## Pattern Matching (Java 16+)

```java
// instanceof with pattern
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Switch pattern (Java 21+)
String result = switch (obj) {
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    case null -> "null";
    default -> "Unknown";
};
```

---

## Common Patterns

### Strategy Pattern

```java
interface Strategy {
    void execute();
}

class Context {
    private Strategy strategy;
    
    void setStrategy(Strategy s) { this.strategy = s; }
    void run() { strategy.execute(); }
}
```

### Factory Pattern

```java
interface Product { }
class ConcreteA implements Product { }
class ConcreteB implements Product { }

Product create(String type) {
    return switch (type) {
        case "A" -> new ConcreteA();
        case "B" -> new ConcreteB();
        default -> throw new IllegalArgumentException();
    };
}
```

---

## Quick Reference

### Overloading vs Overriding

| Feature | Overloading | Overriding |
|---------|-------------|------------|
| Location | Same class | Parent/child |
| Signature | Different | Same |
| Return type | Can differ | Same or covariant |
| Binding | Compile-time | Runtime |
| Annotation | None | @Override |

### Key Rules

- Upcasting is always safe
- Downcasting needs instanceof check
- Use @Override annotation always
- Program to interfaces, not implementations

---

[Full Documentation](../documentation/13-polymorphism.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)
