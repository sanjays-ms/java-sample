# Inheritance

[<- Previous: Encapsulation](11-encapsulation.md) | [Next: Polymorphism ->](13-polymorphism.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Inheritance Cheat Sheet](../cheatsheets/inheritance-cheatsheet.md)

---

## Overview

Inheritance is a fundamental OOP concept where a new class (subclass/child class) is created from an existing class (superclass/parent class). The subclass inherits fields and methods from the superclass, promoting code reuse and establishing a hierarchical relationship.

**Key Benefits:**
- **Code Reuse** - Inherit common functionality from parent classes
- **Hierarchical Organization** - Model real-world relationships
- **Extensibility** - Add new features without modifying existing code
- **Polymorphism** - Treat subclass objects as parent class types

```java
// Parent class (superclass)
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void eat() {
        System.out.println(name + " is eating");
    }
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Child class (subclass)
public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name);  // Call parent constructor
        this.breed = breed;
    }
    
    public void bark() {
        System.out.println(name + " is barking");
    }
}

// Usage
Dog dog = new Dog("Buddy", "Golden Retriever");
dog.eat();    // Inherited from Animal
dog.sleep();  // Inherited from Animal
dog.bark();   // Defined in Dog
```

---

## The extends Keyword

Use `extends` to create a subclass that inherits from a parent class.

### Basic Syntax

```java
public class ParentClass {
    // Fields and methods
}

public class ChildClass extends ParentClass {
    // Inherits all non-private members from ParentClass
    // Can add new fields and methods
    // Can override parent methods
}
```

### Single Inheritance

Java supports single inheritance only - a class can extend only one class.

```java
public class Vehicle { }
public class Car extends Vehicle { }      // OK
public class Truck extends Vehicle { }    // OK

// This is NOT allowed in Java:
// public class FlyingCar extends Car, Airplane { }  // Error!
```

### Inheritance Chain

Classes can form an inheritance chain (multilevel inheritance).

```java
public class Animal {
    public void breathe() {
        System.out.println("Breathing...");
    }
}

public class Mammal extends Animal {
    public void feedMilk() {
        System.out.println("Feeding milk...");
    }
}

public class Dog extends Mammal {
    public void bark() {
        System.out.println("Barking...");
    }
}

// Dog inherits from Mammal, which inherits from Animal
Dog dog = new Dog();
dog.breathe();   // From Animal
dog.feedMilk();  // From Mammal
dog.bark();      // From Dog
```

### What Gets Inherited

| Member Type | Inherited? | Notes |
|-------------|------------|-------|
| public fields | Yes | Accessible in subclass |
| protected fields | Yes | Accessible in subclass |
| package-private fields | Yes* | Only if same package |
| private fields | No | Not directly accessible |
| public methods | Yes | Can be overridden |
| protected methods | Yes | Can be overridden |
| package-private methods | Yes* | Only if same package |
| private methods | No | Not accessible |
| Constructors | No | Must be called via super() |
| static members | Yes | Inherited but not overridden |

```java
public class Parent {
    public int publicField = 1;
    protected int protectedField = 2;
    int packageField = 3;
    private int privateField = 4;
    
    public void publicMethod() { }
    protected void protectedMethod() { }
    void packageMethod() { }
    private void privateMethod() { }
}

public class Child extends Parent {
    public void test() {
        System.out.println(publicField);     // OK
        System.out.println(protectedField);  // OK
        System.out.println(packageField);    // OK (same package)
        // System.out.println(privateField); // Error! Not accessible
        
        publicMethod();     // OK
        protectedMethod();  // OK
        packageMethod();    // OK (same package)
        // privateMethod(); // Error! Not accessible
    }
}
```

---

## The super Keyword

The `super` keyword refers to the parent class. Use it to:
1. Call parent constructor
2. Call parent methods
3. Access parent fields (when shadowed)

### Calling Parent Constructor

```java
public class Person {
    protected String name;
    protected int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

public class Employee extends Person {
    private String employeeId;
    private double salary;
    
    public Employee(String name, int age, String employeeId, double salary) {
        super(name, age);  // MUST be first statement
        this.employeeId = employeeId;
        this.salary = salary;
    }
}

Employee emp = new Employee("Alice", 30, "E001", 50000);
```

### super() Rules

```java
public class Parent {
    public Parent() {
        System.out.println("Parent no-arg constructor");
    }
    
    public Parent(String message) {
        System.out.println("Parent: " + message);
    }
}

public class Child extends Parent {
    // Rule 1: super() must be first statement
    public Child() {
        super();  // Calls Parent()
        System.out.println("Child constructor");
    }
    
    // Rule 2: If no super(), compiler adds super() automatically
    public Child(int x) {
        // super() is implicitly added here if not specified
        System.out.println("Child with int");
    }
    
    // Rule 3: Can call specific parent constructor
    public Child(String message) {
        super(message);  // Calls Parent(String)
        System.out.println("Child with message");
    }
}

new Child();
// Output:
// Parent no-arg constructor
// Child constructor

new Child("Hello");
// Output:
// Parent: Hello
// Child with message
```

### When Parent Has No No-Arg Constructor

```java
public class Parent {
    private String value;
    
    // Only parameterized constructor
    public Parent(String value) {
        this.value = value;
    }
}

public class Child extends Parent {
    // Error! No implicit super() available
    // public Child() { }
    
    // Must explicitly call parent constructor
    public Child() {
        super("default");  // Required!
    }
    
    public Child(String value) {
        super(value);  // Required!
    }
}
```

### Calling Parent Methods with super

```java
public class Animal {
    public void makeSound() {
        System.out.println("Some generic sound");
    }
    
    public void describe() {
        System.out.println("I am an animal");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }
    
    @Override
    public void describe() {
        super.describe();  // Call parent method first
        System.out.println("Specifically, I am a cat");
    }
    
    public void doubleMeow() {
        super.makeSound();  // Generic sound
        makeSound();        // Cat's meow
    }
}

Cat cat = new Cat();
cat.describe();
// Output:
// I am an animal
// Specifically, I am a cat
```

### Accessing Parent Fields with super

```java
public class Parent {
    protected String name = "Parent";
}

public class Child extends Parent {
    private String name = "Child";  // Shadows parent's name
    
    public void printNames() {
        System.out.println(name);        // Child
        System.out.println(this.name);   // Child
        System.out.println(super.name);  // Parent
    }
}
```

---

## Method Overriding

Method overriding allows a subclass to provide a specific implementation for a method already defined in its parent class.

### Basic Overriding

```java
public class Shape {
    public double getArea() {
        return 0;
    }
    
    public void draw() {
        System.out.println("Drawing a shape");
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override  // Annotation indicates override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a circle with radius " + radius);
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle " + width + "x" + height);
    }
}
```

### @Override Annotation

Always use `@Override` annotation when overriding methods. It:
- Documents intent to override
- Triggers compiler error if method doesn't actually override
- Protects against typos and signature changes

```java
public class Parent {
    public void process(String data) { }
}

public class Child extends Parent {
    @Override
    public void process(String data) { }  // Correct override
    
    // @Override
    // public void process(int data) { }  // Error! Different signature
    
    // @Override
    // public void proccess(String data) { }  // Error! Typo in name
}
```

### Overriding Rules

```java
public class Parent {
    public String getValue() {
        return "parent";
    }
    
    protected void process() { }
    
    void packageMethod() { }
}

public class Child extends Parent {
    // Rule 1: Same method signature (name + parameters)
    @Override
    public String getValue() {
        return "child";
    }
    
    // Rule 2: Return type must be same or covariant (subtype)
    // See covariant returns section below
    
    // Rule 3: Access modifier can be same or less restrictive
    @Override
    public void process() { }  // protected -> public is OK
    
    // @Override
    // private void process() { }  // Error! More restrictive
    
    // Rule 4: Cannot throw broader checked exceptions
    // See exception rules section below
}
```

### Covariant Return Types

Override can return a subtype of the original return type.

```java
public class Animal {
    public Animal reproduce() {
        return new Animal();
    }
}

public class Dog extends Animal {
    @Override
    public Dog reproduce() {  // Returns Dog instead of Animal
        return new Dog();
    }
}

public class Cat extends Animal {
    @Override
    public Cat reproduce() {  // Returns Cat instead of Animal
        return new Cat();
    }
}

Dog dog = new Dog();
Dog puppy = dog.reproduce();  // No casting needed!
```

### Exception Rules in Overriding

```java
import java.io.*;

public class Parent {
    public void read() throws IOException { }
    public void write() throws Exception { }
    public void process() { }
}

public class Child extends Parent {
    // Can throw same exception
    @Override
    public void read() throws IOException { }
    
    // Can throw narrower exception
    @Override
    public void write() throws IOException { }  // IOException is subtype of Exception
    
    // Can throw no exception
    // @Override
    // public void read() { }  // OK - no exception
    
    // Cannot throw broader exception
    // @Override
    // public void process() throws Exception { }  // Error! Parent doesn't throw
    
    // Can always throw unchecked exceptions
    @Override
    public void process() {
        throw new RuntimeException("OK");  // Unchecked - always allowed
    }
}
```

### Static Methods Cannot Be Overridden

Static methods are hidden, not overridden.

```java
public class Parent {
    public static void staticMethod() {
        System.out.println("Parent static");
    }
    
    public void instanceMethod() {
        System.out.println("Parent instance");
    }
}

public class Child extends Parent {
    // This HIDES the parent static method, does not override
    public static void staticMethod() {
        System.out.println("Child static");
    }
    
    @Override
    public void instanceMethod() {
        System.out.println("Child instance");
    }
}

Parent p = new Child();
p.staticMethod();    // "Parent static" - static binding
p.instanceMethod();  // "Child instance" - dynamic binding

Child c = new Child();
c.staticMethod();    // "Child static"
c.instanceMethod();  // "Child instance"
```

---

## The final Keyword

Use `final` to prevent inheritance or overriding.

### Final Classes

A final class cannot be extended.

```java
public final class String {  // Cannot be subclassed
    // ...
}

// public class MyString extends String { }  // Error!

// Common final classes in Java:
// String, Integer, Double, Boolean (all wrapper classes)
// System, Math
```

### Final Methods

A final method cannot be overridden.

```java
public class Parent {
    public final void criticalMethod() {
        // Cannot be overridden by subclasses
        System.out.println("This implementation is fixed");
    }
    
    public void regularMethod() {
        System.out.println("Can be overridden");
    }
}

public class Child extends Parent {
    // @Override
    // public void criticalMethod() { }  // Error! Cannot override final
    
    @Override
    public void regularMethod() {
        System.out.println("Overridden in child");
    }
}
```

### When to Use final

```java
// Use final for:

// 1. Security - prevent malicious overriding
public final class SecurityManager {
    public final boolean checkPermission(String permission) {
        // Cannot be bypassed by subclassing
    }
}

// 2. Immutability - prevent modification through subclassing
public final class ImmutablePoint {
    private final int x, y;
    // ...
}

// 3. Performance optimization (JVM can inline final methods)
public final void frequentlyCalledMethod() { }

// 4. Design clarity - signal that extension is not intended
public final class UtilityClass {
    private UtilityClass() { }  // No instances
    public static void utility() { }
}
```

---

## Constructor Chaining

Constructors can call other constructors in the same class or parent class.

### Within Same Class (this)

```java
public class Person {
    private String name;
    private int age;
    private String email;
    
    // Primary constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Chain to primary constructor
    public Person(String name, int age) {
        this(name, age, "unknown@example.com");  // Calls 3-arg constructor
    }
    
    // Chain to 2-arg constructor
    public Person(String name) {
        this(name, 0);  // Calls 2-arg constructor
    }
    
    // Chain to 1-arg constructor
    public Person() {
        this("Unknown");  // Calls 1-arg constructor
    }
}

// All of these are valid:
new Person();                              // name=Unknown, age=0, email=unknown@example.com
new Person("Alice");                       // name=Alice, age=0, email=unknown@example.com
new Person("Bob", 25);                     // name=Bob, age=25, email=unknown@example.com
new Person("Charlie", 30, "c@example.com"); // name=Charlie, age=30, email=c@example.com
```

### Across Inheritance Hierarchy (super)

```java
public class Vehicle {
    protected String make;
    protected String model;
    protected int year;
    
    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }
    
    public Vehicle(String make, String model) {
        this(make, model, 2024);
    }
}

public class Car extends Vehicle {
    private int numDoors;
    private String fuelType;
    
    public Car(String make, String model, int year, int numDoors, String fuelType) {
        super(make, model, year);  // Call parent constructor
        this.numDoors = numDoors;
        this.fuelType = fuelType;
    }
    
    public Car(String make, String model, int numDoors) {
        super(make, model);  // Call parent 2-arg constructor
        this.numDoors = numDoors;
        this.fuelType = "Gasoline";
    }
    
    public Car(String make, String model) {
        this(make, model, 4);  // Chain within Car, which calls super
    }
}
```

### Constructor Execution Order

```java
public class A {
    public A() {
        System.out.println("A constructor");
    }
}

public class B extends A {
    public B() {
        // super() implicitly called first
        System.out.println("B constructor");
    }
}

public class C extends B {
    public C() {
        // super() implicitly called first
        System.out.println("C constructor");
    }
}

new C();
// Output:
// A constructor
// B constructor
// C constructor
```

---

## The Object Class

Every class in Java implicitly extends `java.lang.Object`. Object is the root of the class hierarchy.

### Object Methods

```java
public class Object {
    // Returns string representation
    public String toString() { ... }
    
    // Compares for equality
    public boolean equals(Object obj) { ... }
    
    // Returns hash code
    public int hashCode() { ... }
    
    // Returns runtime class
    public final Class<?> getClass() { ... }
    
    // Creates and returns a copy
    protected Object clone() throws CloneNotSupportedException { ... }
    
    // Called by garbage collector
    protected void finalize() throws Throwable { ... }  // Deprecated
    
    // Thread notification methods
    public final void wait() throws InterruptedException { ... }
    public final void notify() { ... }
    public final void notifyAll() { ... }
}
```

### Overriding Object Methods

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "Person[name=" + name + ", age=" + age + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Person p1 = new Person("Alice", 25);
Person p2 = new Person("Alice", 25);

System.out.println(p1.toString());     // Person[name=Alice, age=25]
System.out.println(p1.equals(p2));     // true
System.out.println(p1.hashCode() == p2.hashCode());  // true
```

### getClass() Method

```java
public class Animal { }
public class Dog extends Animal { }

Animal animal = new Dog();

System.out.println(animal.getClass());              // class Dog
System.out.println(animal.getClass().getName());    // Dog
System.out.println(animal.getClass().getSimpleName()); // Dog
System.out.println(animal.getClass().getSuperclass()); // class Animal
```

---

## instanceof Operator

Check if an object is an instance of a specific class or interface.

### Basic Usage

```java
public class Animal { }
public class Dog extends Animal { }
public class Cat extends Animal { }

Animal animal = new Dog();

System.out.println(animal instanceof Dog);    // true
System.out.println(animal instanceof Animal); // true
System.out.println(animal instanceof Cat);    // false
System.out.println(animal instanceof Object); // true

// null is not an instance of anything
Animal nullAnimal = null;
System.out.println(nullAnimal instanceof Animal);  // false
```

### Pattern Matching with instanceof (Java 16+)

```java
// Traditional approach
public void process(Object obj) {
    if (obj instanceof String) {
        String str = (String) obj;  // Explicit cast needed
        System.out.println(str.toUpperCase());
    }
}

// Pattern matching (Java 16+)
public void processModern(Object obj) {
    if (obj instanceof String str) {  // Cast and assign in one step
        System.out.println(str.toUpperCase());
    }
}

// With negation
public void processWithCheck(Object obj) {
    if (!(obj instanceof String str)) {
        return;  // Early return if not String
    }
    // str is available here
    System.out.println(str.length());
}

// In complex conditions
public void processComplex(Object obj) {
    if (obj instanceof String str && str.length() > 5) {
        System.out.println("Long string: " + str);
    }
}
```

### instanceof with Interfaces

```java
public interface Flyable {
    void fly();
}

public class Bird implements Flyable {
    public void fly() { System.out.println("Bird flying"); }
}

public class Airplane implements Flyable {
    public void fly() { System.out.println("Airplane flying"); }
}

public void checkFlyable(Object obj) {
    if (obj instanceof Flyable flyable) {
        flyable.fly();  // Works for Bird, Airplane, or any Flyable
    }
}
```

---

## Inheritance vs Composition

### When to Use Inheritance ("is-a" relationship)

```java
// Good use of inheritance - Dog IS-A Animal
public class Animal {
    protected String name;
    public void eat() { }
    public void sleep() { }
}

public class Dog extends Animal {
    public void bark() { }
}

// Good use - Manager IS-A Employee
public class Employee {
    protected String name;
    protected double salary;
}

public class Manager extends Employee {
    private List<Employee> team;
}
```

### When to Use Composition ("has-a" relationship)

```java
// Bad inheritance - Car is NOT an Engine
// public class Car extends Engine { }  // Wrong!

// Good composition - Car HAS-A Engine
public class Engine {
    public void start() { }
    public void stop() { }
}

public class Car {
    private Engine engine;  // Composition
    private Wheel[] wheels;
    
    public Car() {
        this.engine = new Engine();
        this.wheels = new Wheel[4];
    }
    
    public void start() {
        engine.start();  // Delegate to engine
    }
}

// Another example - Person HAS-A Address
public class Address {
    private String street, city, zipCode;
}

public class Person {
    private String name;
    private Address address;  // Composition, not inheritance
}
```

### Favor Composition Over Inheritance

```java
// Problem with inheritance - tight coupling
public class ArrayList<E> {
    public boolean add(E e) { ... }
    public boolean addAll(Collection<? extends E> c) {
        // Calls add() for each element
    }
}

public class CountingList<E> extends ArrayList<E> {
    private int addCount = 0;
    
    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);  // Problem: super.addAll calls add()!
    }
    // addCount will be wrong because addAll calls add internally
}

// Solution with composition - delegation
public class CountingList<E> {
    private final List<E> list = new ArrayList<>();
    private int addCount = 0;
    
    public boolean add(E e) {
        addCount++;
        return list.add(e);
    }
    
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return list.addAll(c);  // No double counting
    }
    
    public int getAddCount() {
        return addCount;
    }
    
    // Delegate other methods as needed
    public int size() { return list.size(); }
    public E get(int index) { return list.get(index); }
}
```

---

## Sealed Classes (Java 17+)

Sealed classes restrict which classes can extend them.

### Basic Sealed Classes

```java
// Only Circle, Rectangle, and Triangle can extend Shape
public sealed class Shape permits Circle, Rectangle, Triangle {
    public abstract double area();
}

// Must be final, sealed, or non-sealed
public final class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public final class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

public final class Triangle extends Shape {
    private double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// This would NOT compile:
// public class Pentagon extends Shape { }  // Error! Not in permits clause
```

### Sealed Class Modifiers for Subclasses

```java
public sealed class Vehicle permits Car, Truck, Motorcycle {
}

// final - no further extension
public final class Motorcycle extends Vehicle { }

// sealed - controlled further extension
public sealed class Car extends Vehicle permits Sedan, SUV {
}

public final class Sedan extends Car { }
public final class SUV extends Car { }

// non-sealed - open for any extension
public non-sealed class Truck extends Vehicle { }

// Anyone can extend Truck
public class PickupTruck extends Truck { }
public class SemiTruck extends Truck { }
```

### Sealed Classes with Pattern Matching

```java
public sealed interface Result<T> permits Success, Failure {
}

public record Success<T>(T value) implements Result<T> { }
public record Failure<T>(String error) implements Result<T> { }

public void handleResult(Result<String> result) {
    switch (result) {
        case Success<String>(String value) -> System.out.println("Got: " + value);
        case Failure<String>(String error) -> System.out.println("Error: " + error);
        // No default needed - compiler knows all cases are covered
    }
}
```

### Benefits of Sealed Classes

```java
// 1. Exhaustive pattern matching
public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> 0.5 * t.base() * t.height();
        // No default needed - all subtypes are known
    };
}

// 2. Domain modeling - represent closed hierarchies
public sealed interface PaymentMethod 
    permits CreditCard, DebitCard, BankTransfer, DigitalWallet {
}

// 3. API design - control extension points
public sealed class DatabaseConnection 
    permits MySQLConnection, PostgresConnection, OracleConnection {
    // Third parties cannot add new connection types
}
```

---

## Abstract Classes vs Inheritance

Abstract classes provide partial implementations for subclasses.

```java
public abstract class AbstractDocument {
    protected String title;
    protected String content;
    
    public AbstractDocument(String title) {
        this.title = title;
    }
    
    // Concrete method - inherited as-is
    public String getTitle() {
        return title;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract void save();
    public abstract void load(String path);
    
    // Template method pattern
    public final void process() {
        load(getDefaultPath());
        validate();
        transform();
        save();
    }
    
    protected abstract String getDefaultPath();
    protected void validate() { }  // Optional override
    protected abstract void transform();
}

public class PDFDocument extends AbstractDocument {
    public PDFDocument(String title) {
        super(title);
    }
    
    @Override
    public void save() {
        System.out.println("Saving PDF: " + title);
    }
    
    @Override
    public void load(String path) {
        System.out.println("Loading PDF from: " + path);
    }
    
    @Override
    protected String getDefaultPath() {
        return "/documents/pdf/";
    }
    
    @Override
    protected void transform() {
        System.out.println("Transforming PDF");
    }
}
```

---

## Practical Examples

### Employee Hierarchy

```java
public class Employee {
    protected String name;
    protected String employeeId;
    protected double baseSalary;
    protected LocalDate hireDate;
    
    public Employee(String name, String employeeId, double baseSalary) {
        this.name = name;
        this.employeeId = employeeId;
        this.baseSalary = baseSalary;
        this.hireDate = LocalDate.now();
    }
    
    public double calculatePay() {
        return baseSalary;
    }
    
    public double getBonus() {
        return baseSalary * 0.05;  // 5% base bonus
    }
    
    public String getDetails() {
        return String.format("Employee[id=%s, name=%s, salary=%.2f]",
            employeeId, name, baseSalary);
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmployeeId() { return employeeId; }
    public double getBaseSalary() { return baseSalary; }
}

public class Manager extends Employee {
    private List<Employee> team;
    private double managementBonus;
    
    public Manager(String name, String employeeId, double baseSalary, double managementBonus) {
        super(name, employeeId, baseSalary);
        this.team = new ArrayList<>();
        this.managementBonus = managementBonus;
    }
    
    public void addTeamMember(Employee employee) {
        team.add(employee);
    }
    
    public List<Employee> getTeam() {
        return new ArrayList<>(team);
    }
    
    @Override
    public double calculatePay() {
        return super.calculatePay() + managementBonus;
    }
    
    @Override
    public double getBonus() {
        // Manager gets 10% + bonus per team member
        return baseSalary * 0.10 + (team.size() * 500);
    }
    
    @Override
    public String getDetails() {
        return String.format("Manager[id=%s, name=%s, salary=%.2f, teamSize=%d]",
            employeeId, name, baseSalary, team.size());
    }
}

public class Developer extends Employee {
    private String programmingLanguage;
    private int experienceYears;
    
    public Developer(String name, String employeeId, double baseSalary, 
                     String programmingLanguage, int experienceYears) {
        super(name, employeeId, baseSalary);
        this.programmingLanguage = programmingLanguage;
        this.experienceYears = experienceYears;
    }
    
    @Override
    public double getBonus() {
        // Developer bonus based on experience
        double experienceMultiplier = 1 + (experienceYears * 0.02);
        return baseSalary * 0.08 * experienceMultiplier;
    }
    
    public void code() {
        System.out.println(name + " is coding in " + programmingLanguage);
    }
    
    @Override
    public String getDetails() {
        return String.format("Developer[id=%s, name=%s, lang=%s, exp=%d years]",
            employeeId, name, programmingLanguage, experienceYears);
    }
}

// Usage
Manager manager = new Manager("Alice", "M001", 80000, 10000);
Developer dev1 = new Developer("Bob", "D001", 60000, "Java", 5);
Developer dev2 = new Developer("Charlie", "D002", 55000, "Python", 3);

manager.addTeamMember(dev1);
manager.addTeamMember(dev2);

System.out.println(manager.getDetails());  // Manager details with team size
System.out.println(manager.calculatePay()); // 90000 (base + management bonus)
System.out.println(manager.getBonus());     // 9000 (10% + 2*500)

System.out.println(dev1.getDetails());     // Developer details
System.out.println(dev1.getBonus());       // 5280 (8% * 1.10 experience multiplier)
```

### Shape Hierarchy with Areas

```java
public abstract class Shape {
    protected String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    public abstract double getArea();
    public abstract double getPerimeter();
    
    public String getColor() { return color; }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[color=" + color + 
               ", area=" + String.format("%.2f", getArea()) + "]";
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
    
    public double getRadius() { return radius; }
}

public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

public class Square extends Rectangle {
    public Square(String color, double side) {
        super(color, side, side);
    }
    
    public double getSide() {
        return getWidth();
    }
}

// Usage
List<Shape> shapes = List.of(
    new Circle("Red", 5),
    new Rectangle("Blue", 4, 6),
    new Square("Green", 4)
);

double totalArea = shapes.stream()
    .mapToDouble(Shape::getArea)
    .sum();
System.out.println("Total area: " + totalArea);

for (Shape shape : shapes) {
    System.out.println(shape);
}
```

---

## Best Practices

### Inheritance Guidelines

```java
// 1. Use inheritance for "is-a" relationships only
public class Dog extends Animal { }  // Dog IS-A Animal - Good
// public class Car extends Engine { } // Car is NOT an Engine - Bad

// 2. Keep inheritance hierarchies shallow (2-3 levels max)
Animal -> Mammal -> Dog  // OK
A -> B -> C -> D -> E -> F  // Too deep - hard to understand

// 3. Program to interfaces when possible
List<String> list = new ArrayList<>();  // Good
// ArrayList<String> list = new ArrayList<>();  // Less flexible

// 4. Favor composition over inheritance for code reuse
public class Car {
    private Engine engine;  // Composition
    private Transmission transmission;
}

// 5. Use @Override annotation always
@Override
public String toString() { ... }

// 6. Call super methods when extending behavior
@Override
public void init() {
    super.init();  // Don't forget parent initialization
    // Additional initialization
}

// 7. Document inheritance contracts
/**
 * Subclasses must call super.process() first.
 * @throws IllegalStateException if not initialized
 */
protected void process() { ... }

// 8. Consider making classes final by default
public final class ImmutableValue { }

// 9. Use sealed classes for controlled hierarchies (Java 17+)
public sealed class Shape permits Circle, Rectangle, Triangle { }
```

---

## Summary

### extends Keyword

| Usage | Description |
|-------|-------------|
| `class Child extends Parent` | Single inheritance |
| Inherits | public, protected, package-private members |
| Does NOT inherit | private members, constructors |

### super Keyword

| Usage | Description |
|-------|-------------|
| `super()` | Call parent constructor (must be first) |
| `super(args)` | Call specific parent constructor |
| `super.method()` | Call parent method |
| `super.field` | Access parent field (when shadowed) |

### Method Overriding Rules

| Rule | Description |
|------|-------------|
| Same signature | Name and parameters must match |
| Covariant return | Can return subtype |
| Access modifier | Same or less restrictive |
| Exceptions | Same, narrower, or none |
| Use @Override | Always annotate |

### final Keyword

| Usage | Effect |
|-------|--------|
| `final class` | Cannot be extended |
| `final method` | Cannot be overridden |

### Sealed Classes (Java 17+)

| Modifier | Description |
|----------|-------------|
| `sealed` | Restricted extension via permits |
| `final` | No further extension |
| `non-sealed` | Open for extension |

**Key Points:**
- Java supports single inheritance only
- Use `super()` to call parent constructors
- Always use `@Override` annotation
- Prefer composition over inheritance
- Use sealed classes for controlled hierarchies
- Keep inheritance hierarchies shallow

---

[<- Previous: Encapsulation](11-encapsulation.md) | [Next: Polymorphism ->](13-polymorphism.md) | [Back to Guide](../guide.md)
