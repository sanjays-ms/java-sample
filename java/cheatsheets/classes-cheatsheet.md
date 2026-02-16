# Classes and Objects Cheat Sheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/10-classes.md)

---

## Class Declaration

```java
public class ClassName {
    // Fields (state)
    private String field;
    
    // Constructor
    public ClassName(String field) {
        this.field = field;
    }
    
    // Methods (behavior)
    public void method() { }
}
```

---

## Creating Objects

```java
// Declaration and instantiation
Person p = new Person("Alice", 25);

// Reference assignment (same object)
Person p2 = p;  // p2 points to same object as p

// Null reference
Person p3 = null;

// Check for null
if (p != null) { p.method(); }
Objects.requireNonNull(p, "Cannot be null");
```

---

## Fields

```java
public class Example {
    // Instance fields (per object)
    String name;
    int count = 0;
    
    // Static field (shared by all)
    static int totalCount = 0;
    
    // Final field (must init in constructor)
    final String id;
    
    // Constant
    static final double PI = 3.14159;
}
```

### Default Values

| Type | Default |
|------|---------|
| int, long, short, byte | 0 |
| float, double | 0.0 |
| char | '\u0000' |
| boolean | false |
| Object | null |

---

## Constructors

```java
public class Person {
    private String name;
    private int age;
    
    // No-arg constructor
    public Person() {
        this("Unknown", 0);  // Chain to other constructor
    }
    
    // Parameterized constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Copy constructor
    public Person(Person other) {
        this(other.name, other.age);
    }
}
```

---

## The this Keyword

```java
// Refer to current object's field
this.name = name;

// Call another constructor (must be first line)
this(param1, param2);

// Return current object (for chaining)
return this;

// Pass current object
someMethod(this);
```

---

## Static Members

```java
public class Counter {
    // Static field
    private static int count = 0;
    
    // Static method
    public static int getCount() {
        return count;
    }
    
    // Static block (runs once on class load)
    static {
        System.out.println("Class loaded");
    }
}

// Access via class name
Counter.getCount();
```

### Static vs Instance

| Static | Instance |
|--------|----------|
| Belongs to class | Belongs to object |
| Shared by all instances | Unique per object |
| `ClassName.method()` | `object.method()` |
| Cannot use `this` | Can use `this` |
| Cannot access instance fields | Can access static fields |

---

## Initialization Order

1. Static fields and blocks (once, when class loads)
2. Instance fields
3. Instance initializer blocks
4. Constructor

---

## Object Methods (Override These)

```java
// toString - string representation
@Override
public String toString() {
    return "Person{name='" + name + "', age=" + age + "}";
}

// equals - logical equality
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Person other = (Person) obj;
    return age == other.age && Objects.equals(name, other.name);
}

// hashCode - must be consistent with equals
@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

---

## Records (Java 16+)

```java
// Concise immutable data class
public record Person(String name, int age) { }

// Auto-generated: constructor, accessors, equals, hashCode, toString

// Usage
Person p = new Person("Alice", 25);
String name = p.name();  // No "get" prefix
int age = p.age();
System.out.println(p);   // Person[name=Alice, age=25]

// Compact constructor (validation)
public record Email(String address) {
    public Email {  // No parentheses
        if (!address.contains("@")) {
            throw new IllegalArgumentException();
        }
        address = address.toLowerCase();  // Normalize
    }
}

// Additional constructor
public record Rectangle(double w, double h) {
    public Rectangle(double side) {
        this(side, side);  // Square
    }
}

// Adding methods
public record Point(int x, int y) {
    public static final Point ORIGIN = new Point(0, 0);
    
    public double distanceTo(Point other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}

// Generic records
public record Pair<K, V>(K key, V value) { }

// Implements interfaces
public record Circle(double r) implements Measurable {
    public double measure() { return Math.PI * r * r; }
}
```

### Record Patterns (Java 21+)

```java
// Deconstruct in instanceof
if (obj instanceof Point(int x, int y)) {
    System.out.println(x + ", " + y);
}

// Deconstruct in switch
switch (shape) {
    case Point(int x, int y) -> draw(x, y);
    case Circle(double r) -> drawCircle(r);
}
```

### Records: When to Use

| Use Records | Use Classes |
|-------------|-------------|
| Immutable data | Mutable state |
| DTOs, value objects | Entities with identity |
| Simple containers | Complex behavior |
| No inheritance | Need extends |

---

## Common Patterns

### Builder Pattern

```java
User user = User.builder("username", "email")
    .firstName("John")
    .lastName("Doe")
    .build();
```

### Singleton Pattern

```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() { }
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### Factory Method

```java
public static Shape create(String type) {
    return switch (type) {
        case "circle" -> new Circle();
        case "square" -> new Square();
        default -> throw new IllegalArgumentException();
    };
}
```

---

## Quick Reference

| Task | Code |
|------|------|
| Create object | `new ClassName()` |
| Access field | `object.field` |
| Call method | `object.method()` |
| Check null | `object != null` |
| Get class | `object.getClass()` |
| Compare | `obj1.equals(obj2)` |
| Static access | `ClassName.staticMethod()` |

---

## Best Practices

1. Make fields private
2. Provide getters/setters as needed
3. Validate in constructors
4. Override toString, equals, hashCode
5. Use final for immutable fields
6. Chain constructors to avoid duplication
7. Consider records for data classes
8. Keep classes focused (single responsibility)

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/10-classes.md)
