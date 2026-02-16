# Polymorphism

[<- Previous: Inheritance](12-inheritance.md) | [Next: Abstraction ->](14-abstraction.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Polymorphism Cheat Sheet](../cheatsheets/polymorphism-cheatsheet.md)

---

## Overview

Polymorphism means "many forms." In Java, it allows objects to be treated as instances of their parent class while behaving according to their actual class. This is one of the four fundamental OOP principles.

**Two Types of Polymorphism:**
1. **Compile-time Polymorphism** (Static) - Method overloading
2. **Runtime Polymorphism** (Dynamic) - Method overriding

```java
// Runtime polymorphism example
public class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }
}

// Polymorphism in action
Animal animal1 = new Dog();  // Dog stored as Animal
Animal animal2 = new Cat();  // Cat stored as Animal

animal1.makeSound();  // "Woof!" - calls Dog's method
animal2.makeSound();  // "Meow!" - calls Cat's method
```

**Key Benefits:**
- **Flexibility** - Write code that works with parent types but uses child implementations
- **Extensibility** - Add new subclasses without changing existing code
- **Maintainability** - Reduce code duplication through abstraction
- **Loose Coupling** - Depend on abstractions, not concrete implementations

---

## Compile-Time Polymorphism (Method Overloading)

Method overloading allows multiple methods with the same name but different parameters in the same class. The compiler determines which method to call based on the arguments.

### Basic Overloading

```java
public class Calculator {
    // Same method name, different parameter types
    public int add(int a, int b) {
        return a + b;
    }
    
    public double add(double a, double b) {
        return a + b;
    }
    
    public long add(long a, long b) {
        return a + b;
    }
    
    // Different number of parameters
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    public int add(int a, int b, int c, int d) {
        return a + b + c + d;
    }
}

Calculator calc = new Calculator();
calc.add(5, 3);           // Calls add(int, int) -> 8
calc.add(5.5, 3.5);       // Calls add(double, double) -> 9.0
calc.add(1, 2, 3);        // Calls add(int, int, int) -> 6
calc.add(1, 2, 3, 4);     // Calls add(int, int, int, int) -> 10
```

### Overloading Rules

```java
public class OverloadingRules {
    // Rule 1: Different parameter types
    public void process(int x) { }
    public void process(String x) { }
    public void process(double x) { }
    
    // Rule 2: Different number of parameters
    public void process(int x, int y) { }
    public void process(int x, int y, int z) { }
    
    // Rule 3: Different parameter order (for different types)
    public void process(int x, String y) { }
    public void process(String x, int y) { }
    
    // NOT valid overloading - same parameters, different return type
    // public int getValue() { return 0; }
    // public String getValue() { return ""; }  // Error!
    
    // NOT valid overloading - same parameters, different parameter names
    // public void test(int a) { }
    // public void test(int b) { }  // Error!
}
```

### Type Promotion in Overloading

```java
public class TypePromotion {
    public void print(int x) {
        System.out.println("int: " + x);
    }
    
    public void print(long x) {
        System.out.println("long: " + x);
    }
    
    public void print(double x) {
        System.out.println("double: " + x);
    }
    
    public void print(Object x) {
        System.out.println("Object: " + x);
    }
}

TypePromotion tp = new TypePromotion();
tp.print(5);       // int: 5
tp.print(5L);      // long: 5
tp.print(5.0);     // double: 5.0
tp.print("hello"); // Object: hello

// Type promotion hierarchy:
// byte -> short -> int -> long -> float -> double
// char -> int -> long -> float -> double

byte b = 5;
tp.print(b);  // Promotes to int: 5

short s = 5;
tp.print(s);  // Promotes to int: 5

float f = 5.0f;
tp.print(f);  // Promotes to double: 5.0
```

### Overloading with Varargs

```java
public class VarargsOverloading {
    public void print(int... numbers) {
        System.out.println("varargs");
    }
    
    public void print(int a, int b) {
        System.out.println("two ints");
    }
    
    public void print(int a, int b, int c) {
        System.out.println("three ints");
    }
}

VarargsOverloading vo = new VarargsOverloading();
vo.print(1, 2);        // "two ints" - specific match preferred
vo.print(1, 2, 3);     // "three ints" - specific match preferred
vo.print(1);           // "varargs" - no specific match
vo.print(1, 2, 3, 4);  // "varargs" - no specific match
vo.print();            // "varargs" - no specific match
```

### Constructor Overloading

```java
public class Person {
    private String name;
    private int age;
    private String email;
    
    // No-arg constructor
    public Person() {
        this("Unknown", 0, "unknown@example.com");
    }
    
    // One parameter
    public Person(String name) {
        this(name, 0, "unknown@example.com");
    }
    
    // Two parameters
    public Person(String name, int age) {
        this(name, age, "unknown@example.com");
    }
    
    // Full constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}

Person p1 = new Person();
Person p2 = new Person("Alice");
Person p3 = new Person("Bob", 25);
Person p4 = new Person("Charlie", 30, "charlie@example.com");
```

---

## Runtime Polymorphism (Method Overriding)

Runtime polymorphism occurs when a subclass provides a specific implementation for a method defined in its parent class. The actual method called is determined at runtime based on the object's actual type.

### Basic Runtime Polymorphism

```java
public class Shape {
    public void draw() {
        System.out.println("Drawing a shape");
    }
    
    public double getArea() {
        return 0;
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a circle");
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle");
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}

// Reference type: Shape, Actual type: Circle/Rectangle
Shape s1 = new Circle(5);
Shape s2 = new Rectangle(4, 6);

s1.draw();  // "Drawing a circle" - Circle's method called
s2.draw();  // "Drawing a rectangle" - Rectangle's method called

System.out.println(s1.getArea());  // 78.54 (Circle's calculation)
System.out.println(s2.getArea());  // 24.0 (Rectangle's calculation)
```

### Dynamic Method Dispatch

The JVM determines which method to call at runtime based on the actual object type.

```java
public class Vehicle {
    public void start() {
        System.out.println("Vehicle starting");
    }
}

public class Car extends Vehicle {
    @Override
    public void start() {
        System.out.println("Car starting with key");
    }
    
    public void honk() {
        System.out.println("Car honking");
    }
}

public class ElectricCar extends Car {
    @Override
    public void start() {
        System.out.println("Electric car starting silently");
    }
}

// Dynamic dispatch in action
Vehicle v1 = new Vehicle();
Vehicle v2 = new Car();
Vehicle v3 = new ElectricCar();

v1.start();  // "Vehicle starting"
v2.start();  // "Car starting with key"
v3.start();  // "Electric car starting silently"

// Compile-time type determines available methods
// v2.honk();  // Error! Vehicle type doesn't have honk()

// Cast to access subclass methods
if (v2 instanceof Car car) {
    car.honk();  // OK after cast
}
```

### Polymorphism with Collections

```java
import java.util.ArrayList;
import java.util.List;

public class PaymentProcessor {
    public void processPayments(List<Payment> payments) {
        for (Payment payment : payments) {
            payment.process();  // Each payment processed according to its type
        }
    }
}

public abstract class Payment {
    protected double amount;
    
    public Payment(double amount) {
        this.amount = amount;
    }
    
    public abstract void process();
}

public class CreditCardPayment extends Payment {
    private String cardNumber;
    
    public CreditCardPayment(double amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }
    
    @Override
    public void process() {
        System.out.println("Processing credit card payment of $" + amount);
    }
}

public class PayPalPayment extends Payment {
    private String email;
    
    public PayPalPayment(double amount, String email) {
        super(amount);
        this.email = email;
    }
    
    @Override
    public void process() {
        System.out.println("Processing PayPal payment of $" + amount);
    }
}

public class BankTransfer extends Payment {
    private String accountNumber;
    
    public BankTransfer(double amount, String accountNumber) {
        super(amount);
        this.accountNumber = accountNumber;
    }
    
    @Override
    public void process() {
        System.out.println("Processing bank transfer of $" + amount);
    }
}

// Usage
List<Payment> payments = new ArrayList<>();
payments.add(new CreditCardPayment(100.0, "1234-5678-9012"));
payments.add(new PayPalPayment(50.0, "user@email.com"));
payments.add(new BankTransfer(200.0, "ACC123456"));

PaymentProcessor processor = new PaymentProcessor();
processor.processPayments(payments);
// Output:
// Processing credit card payment of $100.0
// Processing PayPal payment of $50.0
// Processing bank transfer of $200.0
```

---

## Polymorphism with Interfaces

Interfaces provide the purest form of polymorphism, allowing completely unrelated classes to be treated uniformly.

### Interface Polymorphism

```java
public interface Drawable {
    void draw();
}

public interface Resizable {
    void resize(double factor);
}

// Classes from different hierarchies can implement the same interface
public class Circle implements Drawable, Resizable {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing circle with radius " + radius);
    }
    
    @Override
    public void resize(double factor) {
        radius *= factor;
    }
}

public class TextBox implements Drawable, Resizable {
    private String text;
    private int fontSize;
    
    public TextBox(String text, int fontSize) {
        this.text = text;
        this.fontSize = fontSize;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing text: " + text);
    }
    
    @Override
    public void resize(double factor) {
        fontSize = (int)(fontSize * factor);
    }
}

public class Icon implements Drawable {
    private String iconPath;
    
    public Icon(String iconPath) {
        this.iconPath = iconPath;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing icon: " + iconPath);
    }
}

// Using interface polymorphism
List<Drawable> drawables = new ArrayList<>();
drawables.add(new Circle(5));
drawables.add(new TextBox("Hello", 12));
drawables.add(new Icon("icon.png"));

for (Drawable d : drawables) {
    d.draw();  // Each draws according to its type
}

// Work with specific interface
List<Resizable> resizables = new ArrayList<>();
resizables.add(new Circle(5));
resizables.add(new TextBox("Hello", 12));
// resizables.add(new Icon("icon.png"));  // Error! Icon doesn't implement Resizable

for (Resizable r : resizables) {
    r.resize(1.5);
}
```

### Functional Interfaces and Lambdas

```java
@FunctionalInterface
public interface Processor<T> {
    T process(T input);
}

// Multiple implementations via lambdas
Processor<String> upperCase = s -> s.toUpperCase();
Processor<String> reverse = s -> new StringBuilder(s).reverse().toString();
Processor<String> trim = String::trim;

// Polymorphic usage
List<Processor<String>> processors = List.of(trim, upperCase, reverse);

String result = "  Hello World  ";
for (Processor<String> p : processors) {
    result = p.process(result);
}
System.out.println(result);  // "DLROW OLLEH"

// Or with method
public String applyAll(String input, List<Processor<String>> processors) {
    String result = input;
    for (Processor<String> p : processors) {
        result = p.process(result);
    }
    return result;
}
```

### Strategy Pattern with Interfaces

```java
public interface SortingStrategy {
    void sort(int[] array);
}

public class BubbleSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting with bubble sort");
        // Bubble sort implementation
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}

public class QuickSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting with quick sort");
        quickSort(array, 0, array.length - 1);
    }
    
    private void quickSort(int[] array, int low, int high) {
        // Quick sort implementation
    }
}

public class MergeSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        System.out.println("Sorting with merge sort");
        // Merge sort implementation
    }
}

public class Sorter {
    private SortingStrategy strategy;
    
    public Sorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void performSort(int[] array) {
        strategy.sort(array);  // Delegates to current strategy
    }
}

// Usage
int[] numbers = {5, 2, 8, 1, 9};
Sorter sorter = new Sorter(new BubbleSort());
sorter.performSort(numbers);  // "Sorting with bubble sort"

sorter.setStrategy(new QuickSort());
sorter.performSort(numbers);  // "Sorting with quick sort"
```

---

## Upcasting and Downcasting

### Upcasting

Upcasting is converting a subclass reference to a parent class reference. It's always safe and implicit.

```java
public class Animal {
    public void eat() {
        System.out.println("Animal eating");
    }
}

public class Dog extends Animal {
    @Override
    public void eat() {
        System.out.println("Dog eating");
    }
    
    public void bark() {
        System.out.println("Dog barking");
    }
}

// Upcasting - implicit, always safe
Dog dog = new Dog();
Animal animal = dog;  // Upcasting: Dog -> Animal

animal.eat();    // "Dog eating" - still calls Dog's method
// animal.bark();  // Error! Animal type doesn't have bark()

// Direct upcasting
Animal animal2 = new Dog();  // Also upcasting
```

### Downcasting

Downcasting is converting a parent class reference to a subclass reference. It requires explicit cast and can fail at runtime.

```java
Animal animal = new Dog();  // Upcasting

// Downcasting - explicit, can fail
Dog dog = (Dog) animal;  // OK - animal actually IS a Dog
dog.bark();  // Now we can call Dog methods

Animal animal2 = new Animal();
// Dog dog2 = (Dog) animal2;  // ClassCastException at runtime!

// Safe downcasting with instanceof
if (animal instanceof Dog) {
    Dog d = (Dog) animal;
    d.bark();
}

// Pattern matching (Java 16+) - recommended
if (animal instanceof Dog d) {
    d.bark();  // No explicit cast needed
}
```

### Casting with instanceof Pattern Matching

```java
public void process(Object obj) {
    // Traditional
    if (obj instanceof String) {
        String s = (String) obj;
        System.out.println("String length: " + s.length());
    } else if (obj instanceof Integer) {
        Integer i = (Integer) obj;
        System.out.println("Integer value: " + i);
    } else if (obj instanceof List) {
        List<?> list = (List<?>) obj;
        System.out.println("List size: " + list.size());
    }
    
    // Pattern matching (Java 16+)
    if (obj instanceof String s) {
        System.out.println("String length: " + s.length());
    } else if (obj instanceof Integer i) {
        System.out.println("Integer value: " + i);
    } else if (obj instanceof List<?> list) {
        System.out.println("List size: " + list.size());
    }
}

// Switch with pattern matching (Java 21+)
public String describe(Object obj) {
    return switch (obj) {
        case String s -> "String of length " + s.length();
        case Integer i -> "Integer: " + i;
        case List<?> list -> "List with " + list.size() + " elements";
        case null -> "null value";
        default -> "Unknown type";
    };
}
```

---

## Polymorphism with Abstract Classes

Abstract classes provide a template for polymorphism with partial implementations.

```java
public abstract class Employee {
    protected String name;
    protected double baseSalary;
    
    public Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract double calculatePay();
    
    // Concrete method - can be overridden
    public double getBonus() {
        return baseSalary * 0.1;
    }
    
    // Template method pattern
    public final void printPaySlip() {
        System.out.println("Employee: " + name);
        System.out.println("Base Salary: $" + baseSalary);
        System.out.println("Bonus: $" + getBonus());
        System.out.println("Total Pay: $" + calculatePay());
        System.out.println("---");
    }
}

public class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String name, double baseSalary) {
        super(name, baseSalary);
    }
    
    @Override
    public double calculatePay() {
        return baseSalary + getBonus();
    }
}

public class ContractEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;
    
    public ContractEmployee(String name, double hourlyRate, int hoursWorked) {
        super(name, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }
    
    @Override
    public double calculatePay() {
        return hoursWorked * hourlyRate;
    }
    
    @Override
    public double getBonus() {
        return 0;  // No bonus for contractors
    }
}

public class CommissionEmployee extends Employee {
    private double salesAmount;
    private double commissionRate;
    
    public CommissionEmployee(String name, double baseSalary, 
                               double salesAmount, double commissionRate) {
        super(name, baseSalary);
        this.salesAmount = salesAmount;
        this.commissionRate = commissionRate;
    }
    
    @Override
    public double calculatePay() {
        return baseSalary + (salesAmount * commissionRate) + getBonus();
    }
}

// Polymorphic usage
List<Employee> employees = new ArrayList<>();
employees.add(new FullTimeEmployee("Alice", 5000));
employees.add(new ContractEmployee("Bob", 50, 160));
employees.add(new CommissionEmployee("Charlie", 3000, 50000, 0.05));

for (Employee emp : employees) {
    emp.printPaySlip();  // Each calculates pay differently
}

double totalPayroll = employees.stream()
    .mapToDouble(Employee::calculatePay)
    .sum();
```

---

## Polymorphism with Generics

Generics provide compile-time polymorphism for type safety.

```java
// Generic class with polymorphic behavior
public class Box<T> {
    private T content;
    
    public void set(T content) {
        this.content = content;
    }
    
    public T get() {
        return content;
    }
}

Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String s = stringBox.get();  // No cast needed

Box<Integer> intBox = new Box<>();
intBox.set(42);
Integer i = intBox.get();  // No cast needed

// Generic method
public static <T extends Comparable<T>> T findMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

String maxString = findMax("apple", "banana");  // "banana"
Integer maxInt = findMax(10, 20);  // 20

// Bounded type parameters
public class NumberBox<T extends Number> {
    private T value;
    
    public NumberBox(T value) {
        this.value = value;
    }
    
    public double doubleValue() {
        return value.doubleValue();  // Can call Number methods
    }
}

NumberBox<Integer> intBox2 = new NumberBox<>(42);
NumberBox<Double> doubleBox = new NumberBox<>(3.14);
// NumberBox<String> stringBox2 = new NumberBox<>("hello");  // Error!
```

### Wildcards in Generics

```java
// Upper bounded wildcard - read from
public double sumOfList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {
        sum += n.doubleValue();
    }
    return sum;
}

List<Integer> ints = List.of(1, 2, 3);
List<Double> doubles = List.of(1.1, 2.2, 3.3);

sumOfList(ints);     // Works
sumOfList(doubles);  // Works

// Lower bounded wildcard - write to
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();

addNumbers(numbers);  // Works
addNumbers(objects);  // Works

// Unbounded wildcard - read only
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

---

## Covariant Return Types

Overridden methods can return a more specific type than the parent method.

```java
public class AnimalShelter {
    public Animal adopt() {
        return new Animal();
    }
}

public class DogShelter extends AnimalShelter {
    @Override
    public Dog adopt() {  // Covariant return type
        return new Dog();
    }
}

public class CatShelter extends AnimalShelter {
    @Override
    public Cat adopt() {  // Covariant return type
        return new Cat();
    }
}

// Benefits of covariant returns
DogShelter dogShelter = new DogShelter();
Dog dog = dogShelter.adopt();  // No cast needed!

AnimalShelter shelter = new DogShelter();
Animal animal = shelter.adopt();  // Still works polymorphically
```

### Covariant Returns with Cloning

```java
public class Person implements Cloneable {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    @Override
    public Person clone() {  // Returns Person, not Object
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Employee extends Person {
    private String employeeId;
    
    public Employee(String name, String employeeId) {
        super(name);
        this.employeeId = employeeId;
    }
    
    @Override
    public Employee clone() {  // Returns Employee, not Person
        return (Employee) super.clone();
    }
}

Employee emp = new Employee("Alice", "E001");
Employee clone = emp.clone();  // No cast needed
```

---

## Practical Examples

### Plugin System

```java
public interface Plugin {
    String getName();
    void initialize();
    void execute();
    void shutdown();
}

public class LoggingPlugin implements Plugin {
    @Override
    public String getName() { return "Logging Plugin"; }
    
    @Override
    public void initialize() {
        System.out.println("Initializing logging...");
    }
    
    @Override
    public void execute() {
        System.out.println("Logging executed");
    }
    
    @Override
    public void shutdown() {
        System.out.println("Logging shutdown");
    }
}

public class SecurityPlugin implements Plugin {
    @Override
    public String getName() { return "Security Plugin"; }
    
    @Override
    public void initialize() {
        System.out.println("Initializing security...");
    }
    
    @Override
    public void execute() {
        System.out.println("Security check executed");
    }
    
    @Override
    public void shutdown() {
        System.out.println("Security shutdown");
    }
}

public class CachePlugin implements Plugin {
    @Override
    public String getName() { return "Cache Plugin"; }
    
    @Override
    public void initialize() {
        System.out.println("Initializing cache...");
    }
    
    @Override
    public void execute() {
        System.out.println("Cache operation executed");
    }
    
    @Override
    public void shutdown() {
        System.out.println("Cache cleared and shutdown");
    }
}

public class PluginManager {
    private List<Plugin> plugins = new ArrayList<>();
    
    public void registerPlugin(Plugin plugin) {
        plugins.add(plugin);
    }
    
    public void initializeAll() {
        for (Plugin plugin : plugins) {
            System.out.println("Initializing: " + plugin.getName());
            plugin.initialize();
        }
    }
    
    public void executeAll() {
        for (Plugin plugin : plugins) {
            plugin.execute();
        }
    }
    
    public void shutdownAll() {
        for (Plugin plugin : plugins) {
            plugin.shutdown();
        }
    }
}

// Usage
PluginManager manager = new PluginManager();
manager.registerPlugin(new LoggingPlugin());
manager.registerPlugin(new SecurityPlugin());
manager.registerPlugin(new CachePlugin());

manager.initializeAll();
manager.executeAll();
manager.shutdownAll();
```

### Notification System

```java
public interface NotificationChannel {
    boolean send(String recipient, String message);
    String getChannelName();
}

public class EmailNotification implements NotificationChannel {
    @Override
    public boolean send(String recipient, String message) {
        System.out.println("Sending email to " + recipient + ": " + message);
        return true;
    }
    
    @Override
    public String getChannelName() { return "Email"; }
}

public class SMSNotification implements NotificationChannel {
    @Override
    public boolean send(String recipient, String message) {
        if (message.length() > 160) {
            System.out.println("SMS too long, truncating...");
            message = message.substring(0, 157) + "...";
        }
        System.out.println("Sending SMS to " + recipient + ": " + message);
        return true;
    }
    
    @Override
    public String getChannelName() { return "SMS"; }
}

public class PushNotification implements NotificationChannel {
    @Override
    public boolean send(String recipient, String message) {
        System.out.println("Sending push to device " + recipient + ": " + message);
        return true;
    }
    
    @Override
    public String getChannelName() { return "Push"; }
}

public class SlackNotification implements NotificationChannel {
    private String webhookUrl;
    
    public SlackNotification(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
    
    @Override
    public boolean send(String recipient, String message) {
        System.out.println("Sending Slack message to #" + recipient + ": " + message);
        return true;
    }
    
    @Override
    public String getChannelName() { return "Slack"; }
}

public class NotificationService {
    private List<NotificationChannel> channels = new ArrayList<>();
    
    public void addChannel(NotificationChannel channel) {
        channels.add(channel);
    }
    
    public void broadcast(String message, Map<String, String> recipients) {
        for (NotificationChannel channel : channels) {
            String recipient = recipients.get(channel.getChannelName());
            if (recipient != null) {
                boolean sent = channel.send(recipient, message);
                System.out.println(channel.getChannelName() + " sent: " + sent);
            }
        }
    }
}

// Usage
NotificationService service = new NotificationService();
service.addChannel(new EmailNotification());
service.addChannel(new SMSNotification());
service.addChannel(new PushNotification());
service.addChannel(new SlackNotification("https://hooks.slack.com/..."));

Map<String, String> recipients = Map.of(
    "Email", "user@example.com",
    "SMS", "+1234567890",
    "Push", "device-token-123",
    "Slack", "general"
);

service.broadcast("System maintenance at midnight", recipients);
```

### Shape Calculator

```java
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
    double perimeter();
}

public record Circle(double radius) implements Shape {
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

public record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

public record Triangle(double a, double b, double c) implements Shape {
    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
    @Override
    public double perimeter() {
        return a + b + c;
    }
}

public class ShapeCalculator {
    public static double totalArea(List<Shape> shapes) {
        return shapes.stream()
            .mapToDouble(Shape::area)
            .sum();
    }
    
    public static double totalPerimeter(List<Shape> shapes) {
        return shapes.stream()
            .mapToDouble(Shape::perimeter)
            .sum();
    }
    
    public static String describe(Shape shape) {
        return switch (shape) {
            case Circle(double r) -> "Circle with radius " + r;
            case Rectangle(double w, double h) -> "Rectangle " + w + "x" + h;
            case Triangle(double a, double b, double c) -> 
                "Triangle with sides " + a + ", " + b + ", " + c;
        };
    }
}

// Usage
List<Shape> shapes = List.of(
    new Circle(5),
    new Rectangle(4, 6),
    new Triangle(3, 4, 5)
);

System.out.println("Total area: " + ShapeCalculator.totalArea(shapes));
System.out.println("Total perimeter: " + ShapeCalculator.totalPerimeter(shapes));

for (Shape shape : shapes) {
    System.out.println(ShapeCalculator.describe(shape));
}
```

---

## Best Practices

### Polymorphism Guidelines

```java
// 1. Program to interfaces, not implementations
// Good
List<String> names = new ArrayList<>();
Map<String, Integer> scores = new HashMap<>();

// Less flexible
ArrayList<String> names2 = new ArrayList<>();
HashMap<String, Integer> scores2 = new HashMap<>();

// 2. Use the most general type that works
public void process(Collection<String> items) { }  // More flexible
public void process(ArrayList<String> items) { }   // Less flexible

// 3. Prefer composition and interfaces over inheritance
public interface Flyable {
    void fly();
}

public class Bird implements Flyable {
    private FlyingBehavior flyingBehavior;  // Composition
    
    @Override
    public void fly() {
        flyingBehavior.fly();
    }
}

// 4. Use @Override annotation always
@Override
public void process() { }

// 5. Don't overuse inheritance - favor composition
// Bad: Car extends Engine
// Good: Car has Engine

// 6. Keep inheritance hierarchies shallow
// Parent -> Child -> Grandchild (OK)
// A -> B -> C -> D -> E -> F (Too deep)

// 7. Use sealed classes for controlled hierarchies (Java 17+)
public sealed interface Result permits Success, Failure { }

// 8. Use pattern matching for cleaner type checks (Java 16+)
if (obj instanceof String s) {
    // Use s directly
}
```

---

## Summary

### Types of Polymorphism

| Type | Mechanism | Binding |
|------|-----------|---------|
| Compile-time | Method overloading | Static (compile time) |
| Runtime | Method overriding | Dynamic (runtime) |

### Method Overloading Rules

| Valid | Not Valid |
|-------|-----------|
| Different parameter types | Different return type only |
| Different number of parameters | Different parameter names only |
| Different parameter order | Different access modifiers only |

### Method Overriding Requirements

| Requirement | Description |
|-------------|-------------|
| Same signature | Method name and parameters |
| Covariant return | Same or subtype return |
| Access | Same or less restrictive |
| Exceptions | Same, narrower, or none |

### Casting

| Operation | Direction | Safety |
|-----------|-----------|--------|
| Upcasting | Child -> Parent | Implicit, always safe |
| Downcasting | Parent -> Child | Explicit, can fail |

### Key Points

- Polymorphism enables writing flexible, extensible code
- Compile-time: method overloading (same name, different parameters)
- Runtime: method overriding (same signature in parent/child)
- Upcasting is safe and implicit
- Downcasting requires explicit cast and instanceof check
- Use interfaces for maximum flexibility
- Pattern matching simplifies type checking (Java 16+)
- Prefer composition over inheritance

---

[<- Previous: Inheritance](12-inheritance.md) | [Next: Abstraction ->](14-abstraction.md) | [Back to Guide](../guide.md)
