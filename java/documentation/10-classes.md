# Classes and Objects

[<- Previous: Date and Time](09-date-time.md) | [Next: Encapsulation ->](11-encapsulation.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Classes Cheat Sheet](../cheatsheets/classes-cheatsheet.md)

---

## Overview

A class is a blueprint or template for creating objects. It defines the properties (fields) and behaviors (methods) that objects of that type will have. An object is an instance of a class - a concrete entity created from the blueprint.

**Key Concepts:**
- **Class** - Blueprint defining structure and behavior
- **Object** - Instance of a class
- **Fields** - Variables that hold object state (also called attributes or properties)
- **Methods** - Functions that define object behavior
- **Constructor** - Special method to initialize objects

```java
// Class definition (blueprint)
public class Car {
    // Fields (state)
    String brand;
    String model;
    int year;
    
    // Method (behavior)
    void start() {
        System.out.println("Car is starting...");
    }
}

// Creating objects (instances)
Car myCar = new Car();
Car yourCar = new Car();
```

---

## Class Declaration

### Basic Syntax

```java
[access_modifier] [modifiers] class ClassName [extends SuperClass] [implements Interfaces] {
    // Fields
    // Constructors
    // Methods
    // Nested classes
}
```

### Simple Class Example

```java
public class Person {
    // Fields (instance variables)
    String name;
    int age;
    String email;
    
    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Methods
    public void introduce() {
        System.out.println("Hi, I'm " + name + " and I'm " + age + " years old.");
    }
    
    public boolean isAdult() {
        return age >= 18;
    }
}
```

### Class Naming Conventions

```java
// Class names use PascalCase (UpperCamelCase)
public class Person { }
public class BankAccount { }
public class HttpRequestHandler { }
public class XMLParser { }

// One public class per file, filename must match class name
// Person.java contains public class Person
```

---

## Creating Objects

### The new Keyword

Objects are created using the `new` keyword, which:
1. Allocates memory for the object
2. Initializes fields to default values
3. Calls the constructor
4. Returns a reference to the object

```java
// Declaration and instantiation
Person person = new Person("Alice", 25);

// Can also be split
Person person;              // Declaration (reference is null)
person = new Person("Alice", 25);  // Instantiation

// Multiple objects from same class
Person person1 = new Person("Alice", 25);
Person person2 = new Person("Bob", 30);
Person person3 = new Person("Charlie", 35);

// Each object has its own copy of instance fields
person1.age = 26;  // Only affects person1
```

### Object References

Variables hold references (memory addresses) to objects, not the objects themselves.

```java
Person p1 = new Person("Alice", 25);
Person p2 = p1;  // p2 points to the SAME object as p1

p2.age = 30;
System.out.println(p1.age);  // 30 (same object!)

// Comparing references
Person p3 = new Person("Alice", 25);
System.out.println(p1 == p2);   // true (same reference)
System.out.println(p1 == p3);   // false (different objects)
System.out.println(p1.equals(p3));  // depends on equals() implementation
```

### Null References

```java
Person person = null;  // Reference points to nothing

// NullPointerException if you try to use null reference
// person.introduce();  // Throws NullPointerException!

// Always check for null before using
if (person != null) {
    person.introduce();
}

// Or use Objects.requireNonNull()
import java.util.Objects;
Person validPerson = Objects.requireNonNull(person, "Person cannot be null");
```

---

## Fields (Instance Variables)

### Declaring Fields

```java
public class Employee {
    // Fields with different data types
    String name;                    // Reference type, default null
    int age;                        // Primitive, default 0
    double salary;                  // Primitive, default 0.0
    boolean isActive;               // Primitive, default false
    char grade;                     // Primitive, default '\u0000'
    
    // Field with initial value
    String department = "General";
    int vacationDays = 20;
    
    // Field with access modifier
    private String ssn;             // Only accessible within class
    
    // Final field (must be initialized)
    final String employeeId;
    
    public Employee(String id) {
        this.employeeId = id;       // Final field initialized in constructor
    }
}
```

### Default Values

| Type | Default Value |
|------|---------------|
| byte, short, int, long | 0 |
| float, double | 0.0 |
| char | '\u0000' (null character) |
| boolean | false |
| Object references | null |

```java
public class DefaultValues {
    int number;         // 0
    double decimal;     // 0.0
    boolean flag;       // false
    String text;        // null
    int[] array;        // null
    
    public void printDefaults() {
        System.out.println("number: " + number);     // 0
        System.out.println("decimal: " + decimal);   // 0.0
        System.out.println("flag: " + flag);         // false
        System.out.println("text: " + text);         // null
        System.out.println("array: " + array);       // null
    }
}
```

### Accessing Fields

```java
public class Rectangle {
    double width;
    double height;
}

Rectangle rect = new Rectangle();

// Setting field values
rect.width = 10.0;
rect.height = 5.0;

// Getting field values
double w = rect.width;
double h = rect.height;
double area = rect.width * rect.height;

System.out.println("Width: " + rect.width);    // 10.0
System.out.println("Height: " + rect.height);  // 5.0
System.out.println("Area: " + area);           // 50.0
```

---

## Constructors

A constructor is a special method that initializes a new object. It has the same name as the class and no return type.

### Default Constructor

If you do not define any constructor, Java provides a default no-argument constructor.

```java
public class Book {
    String title;
    String author;
    // No constructor defined - Java provides default constructor
}

// Using default constructor
Book book = new Book();  // Fields are initialized to default values
book.title = "Java Basics";
book.author = "John Doe";
```

### Defining Constructors

```java
public class Book {
    String title;
    String author;
    int pages;
    
    // No-argument constructor
    public Book() {
        title = "Untitled";
        author = "Unknown";
        pages = 0;
    }
    
    // Parameterized constructor
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.pages = 0;
    }
    
    // Constructor with all parameters
    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }
}

// Using different constructors
Book book1 = new Book();                           // Untitled, Unknown, 0
Book book2 = new Book("1984", "George Orwell");    // 1984, George Orwell, 0
Book book3 = new Book("Dune", "Frank Herbert", 412); // Dune, Frank Herbert, 412
```

### The this Keyword in Constructors

`this` refers to the current object instance. It is used to:
1. Distinguish between fields and parameters with the same name
2. Call another constructor (constructor chaining)
3. Pass the current object as an argument

```java
public class Student {
    private String name;
    private int age;
    private String school;
    
    // Using this to distinguish field from parameter
    public Student(String name, int age) {
        this.name = name;    // this.name is field, name is parameter
        this.age = age;
    }
    
    // Constructor chaining with this()
    public Student(String name) {
        this(name, 18);  // Calls Student(String, int)
    }
    
    public Student() {
        this("Unknown");  // Calls Student(String)
    }
    
    // Passing this as argument
    public void enroll(School school) {
        school.addStudent(this);  // Pass current object
    }
}
```

### Constructor Chaining

```java
public class Account {
    private String accountNumber;
    private String holderName;
    private double balance;
    private String accountType;
    
    // Full constructor
    public Account(String accountNumber, String holderName, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.accountType = accountType;
    }
    
    // Chained constructor - default balance
    public Account(String accountNumber, String holderName, String accountType) {
        this(accountNumber, holderName, 0.0, accountType);  // Must be first statement
    }
    
    // Chained constructor - default type and balance
    public Account(String accountNumber, String holderName) {
        this(accountNumber, holderName, 0.0, "Savings");
    }
}

// Usage
Account a1 = new Account("001", "Alice", 1000.0, "Checking");
Account a2 = new Account("002", "Bob", "Savings");      // balance = 0.0
Account a3 = new Account("003", "Charlie");              // balance = 0.0, type = Savings
```

### Private Constructors

Used to prevent instantiation or for singleton pattern.

```java
// Utility class - prevent instantiation
public class MathUtils {
    private MathUtils() {
        // Private constructor prevents instantiation
    }
    
    public static int add(int a, int b) {
        return a + b;
    }
}
// MathUtils utils = new MathUtils();  // Error!
int sum = MathUtils.add(5, 3);         // OK

// Singleton pattern
public class Database {
    private static Database instance;
    
    private Database() {
        // Private constructor
    }
    
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}
```

### Copy Constructor

Creates a new object as a copy of an existing object.

```java
public class Point {
    private int x;
    private int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Copy constructor
    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
}

Point original = new Point(10, 20);
Point copy = new Point(original);  // Creates a copy

copy.x = 30;  // Does not affect original
System.out.println(original.getX());  // 10
System.out.println(copy.getX());      // 30
```

---

## The this Keyword

`this` is a reference to the current object.

### Disambiguating Fields and Parameters

```java
public class Rectangle {
    private double width;
    private double height;
    
    public void setDimensions(double width, double height) {
        this.width = width;    // this.width is field
        this.height = height;  // height alone would be parameter
    }
    
    // Without this, field is shadowed
    public void setBadDimensions(double width, double height) {
        width = width;   // Assigns parameter to itself! Bug!
        height = height; // Field remains unchanged
    }
}
```

### Returning this for Method Chaining

```java
public class StringBuilder2 {
    private String value = "";
    
    public StringBuilder2 append(String s) {
        value += s;
        return this;  // Return current object for chaining
    }
    
    public StringBuilder2 appendLine(String s) {
        value += s + "\n";
        return this;
    }
    
    public String build() {
        return value;
    }
}

// Method chaining (fluent interface)
String result = new StringBuilder2()
    .append("Hello, ")
    .append("World!")
    .appendLine("")
    .append("How are you?")
    .build();
```

### Passing this to Methods

```java
public class Employee {
    private String name;
    private Department department;
    
    public Employee(String name) {
        this.name = name;
    }
    
    public void joinDepartment(Department dept) {
        this.department = dept;
        dept.addEmployee(this);  // Pass this object to department
    }
}

public class Department {
    private List<Employee> employees = new ArrayList<>();
    
    public void addEmployee(Employee emp) {
        employees.add(emp);
    }
}
```

---

## Static Members

Static members belong to the class itself, not to instances.

### Static Fields

```java
public class Counter {
    // Static field - shared by all instances
    private static int count = 0;
    
    // Instance field - unique to each instance
    private int id;
    
    public Counter() {
        count++;          // Increment shared counter
        this.id = count;  // Assign unique ID
    }
    
    public static int getCount() {
        return count;
    }
    
    public int getId() {
        return id;
    }
}

Counter c1 = new Counter();  // count = 1, c1.id = 1
Counter c2 = new Counter();  // count = 2, c2.id = 2
Counter c3 = new Counter();  // count = 3, c3.id = 3

System.out.println(Counter.getCount());  // 3
System.out.println(c1.getId());          // 1
System.out.println(c2.getId());          // 2
```

### Static Methods

```java
public class MathHelper {
    // Static method - no instance needed
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static int max(int a, int b) {
        return a > b ? a : b;
    }
    
    // Cannot access instance fields from static method
    private int instanceField;
    
    public static void staticMethod() {
        // System.out.println(instanceField);  // Error!
        // System.out.println(this);           // Error!
    }
}

// Call without creating instance
int sum = MathHelper.add(5, 3);
int maximum = MathHelper.max(10, 7);
```

### Static vs Instance Members

```java
public class BankAccount {
    // Static field - same for all accounts
    private static double interestRate = 0.05;
    private static int totalAccounts = 0;
    
    // Instance fields - unique to each account
    private String accountNumber;
    private double balance;
    
    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        totalAccounts++;
    }
    
    // Static method - operates on class-level data
    public static void setInterestRate(double rate) {
        interestRate = rate;  // Affects all accounts
    }
    
    public static double getInterestRate() {
        return interestRate;
    }
    
    public static int getTotalAccounts() {
        return totalAccounts;
    }
    
    // Instance method - operates on instance data
    public void applyInterest() {
        balance += balance * interestRate;
    }
    
    public double getBalance() {
        return balance;
    }
}

// Static members accessed via class name
BankAccount.setInterestRate(0.06);
System.out.println(BankAccount.getInterestRate());  // 0.06

// Instance members accessed via object
BankAccount acc1 = new BankAccount("001", 1000);
BankAccount acc2 = new BankAccount("002", 2000);
acc1.applyInterest();  // Applies to acc1 only
```

### Static Blocks

Static initialization blocks run once when the class is loaded.

```java
public class Configuration {
    private static Map<String, String> settings;
    private static final String CONFIG_FILE = "config.properties";
    
    // Static block - runs when class is loaded
    static {
        System.out.println("Loading configuration...");
        settings = new HashMap<>();
        loadSettings();
    }
    
    private static void loadSettings() {
        // Simulated loading
        settings.put("host", "localhost");
        settings.put("port", "8080");
    }
    
    public static String getSetting(String key) {
        return settings.get(key);
    }
}

// Static block runs automatically when class is first used
String host = Configuration.getSetting("host");  // Prints "Loading configuration..."
```

### Static Imports

```java
// Without static import
double result = Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2));

// With static import
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

double result = sqrt(pow(3, 2) + pow(4, 2));

// Import all static members
import static java.lang.Math.*;

double area = PI * pow(radius, 2);
```

---

## Instance Initializer Blocks

Instance initializer blocks run every time an object is created, before the constructor.

```java
public class Widget {
    private int id;
    private String name;
    private Date createdAt;
    
    // Instance initializer block
    {
        System.out.println("Instance initializer running...");
        createdAt = new Date();  // Common initialization for all constructors
    }
    
    public Widget() {
        this.id = 0;
        this.name = "Default";
    }
    
    public Widget(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

// Execution order:
// 1. Static blocks (once, when class is loaded)
// 2. Instance initializer blocks
// 3. Constructor
```

### Initialization Order

```java
public class InitOrder {
    // 1. Static field initialization
    private static int staticField = initStatic();
    
    // 2. Static block
    static {
        System.out.println("Static block");
    }
    
    // 3. Instance field initialization
    private int instanceField = initInstance();
    
    // 4. Instance initializer block
    {
        System.out.println("Instance initializer block");
    }
    
    // 5. Constructor
    public InitOrder() {
        System.out.println("Constructor");
    }
    
    private static int initStatic() {
        System.out.println("Static field init");
        return 1;
    }
    
    private int initInstance() {
        System.out.println("Instance field init");
        return 2;
    }
}

// Output when creating new InitOrder():
// Static field init    (first time only)
// Static block         (first time only)
// Instance field init
// Instance initializer block
// Constructor
```

---

## Final Fields

Final fields can only be assigned once.

### Final Instance Fields

```java
public class ImmutablePoint {
    private final int x;
    private final int y;
    
    public ImmutablePoint(int x, int y) {
        this.x = x;  // Must be assigned in constructor
        this.y = y;
    }
    
    // No setters - values cannot change
    
    public int getX() { return x; }
    public int getY() { return y; }
}

ImmutablePoint point = new ImmutablePoint(10, 20);
// point.x = 30;  // Error! Cannot assign to final field
```

### Blank Final Fields

Final fields without initializers must be assigned in constructor.

```java
public class Employee {
    private final String employeeId;  // Blank final
    private final Date hireDate;      // Blank final
    
    public Employee(String id) {
        this.employeeId = id;
        this.hireDate = new Date();
        // Both must be assigned before constructor completes
    }
}
```

### Final Static Fields (Constants)

```java
public class Constants {
    // Constants are static final
    public static final double PI = 3.14159265359;
    public static final int MAX_SIZE = 100;
    public static final String APP_NAME = "MyApp";
    
    // Naming convention: UPPER_SNAKE_CASE
    public static final int HTTP_OK = 200;
    public static final int HTTP_NOT_FOUND = 404;
}

// Usage
double circumference = 2 * Constants.PI * radius;
if (statusCode == Constants.HTTP_OK) {
    // ...
}
```

---

## Object Methods

Every class inherits these methods from Object class.

### toString()

Returns a string representation of the object.

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Override toString for meaningful output
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

Person p = new Person("Alice", 25);
System.out.println(p);           // Person{name='Alice', age=25}
System.out.println(p.toString()); // Same output

// Without override, you get: Person@7a81197d (class@hashcode)
```

### equals()

Compares objects for equality.

```java
public class Point {
    private int x;
    private int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
        // Same reference
        if (this == obj) return true;
        
        // Null or different class
        if (obj == null || getClass() != obj.getClass()) return false;
        
        // Cast and compare fields
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }
}

Point p1 = new Point(10, 20);
Point p2 = new Point(10, 20);
Point p3 = new Point(30, 40);

System.out.println(p1 == p2);      // false (different objects)
System.out.println(p1.equals(p2)); // true (same values)
System.out.println(p1.equals(p3)); // false (different values)
```

### hashCode()

Returns a hash code for the object. Must be consistent with equals().

```java
public class Point {
    private int x;
    private int y;
    
    // ... constructor, equals() ...
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

// Contract: if a.equals(b), then a.hashCode() == b.hashCode()
// Important for HashMap, HashSet, etc.
```

### Complete equals/hashCode Example

```java
import java.util.Objects;

public class Book {
    private String isbn;
    private String title;
    private String author;
    
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(isbn, book.isbn) &&
               Objects.equals(title, book.title) &&
               Objects.equals(author, book.author);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author);
    }
    
    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', title='" + title + "', author='" + author + "'}";
    }
}
```

### getClass()

Returns the runtime class of the object.

```java
Person p = new Person("Alice", 25);
Class<?> clazz = p.getClass();

System.out.println(clazz.getName());        // com.example.Person
System.out.println(clazz.getSimpleName());  // Person
System.out.println(clazz.getPackageName()); // com.example
```

### clone()

Creates a copy of the object (requires implementing Cloneable).

```java
public class Point implements Cloneable {
    private int x;
    private int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);  // Should never happen
        }
    }
}

Point original = new Point(10, 20);
Point cloned = original.clone();
```

---

## Object Lifecycle

### Creation

```java
// 1. Memory allocated on heap
// 2. Fields initialized to defaults
// 3. Initializer blocks run
// 4. Constructor executes
Person person = new Person("Alice", 25);
```

### Usage

```java
// Object is accessible and usable
person.introduce();
person.setAge(26);
String name = person.getName();
```

### Garbage Collection

```java
// Object becomes eligible for garbage collection when no references exist
person = null;  // Reference removed

// Or when reference goes out of scope
public void method() {
    Person temp = new Person("Temp", 20);
    // temp used here
}  // temp goes out of scope, object eligible for GC

// You can request (not force) garbage collection
System.gc();  // Hint to JVM, may be ignored
```

### finalize() (Deprecated)

```java
// Deprecated since Java 9 - do not use
// Use try-with-resources or explicit cleanup instead
@Override
protected void finalize() throws Throwable {
    // Called before garbage collection
    // Not guaranteed to run!
}

// Better: implement AutoCloseable
public class Resource implements AutoCloseable {
    @Override
    public void close() {
        // Cleanup logic
    }
}

try (Resource r = new Resource()) {
    // Use resource
}  // close() called automatically
```

---

## Nested Classes Overview

Classes can be defined inside other classes.

### Types of Nested Classes

```java
public class Outer {
    // Static nested class
    public static class StaticNested {
        // Can access Outer's static members
    }
    
    // Inner class (non-static nested)
    public class Inner {
        // Can access all Outer members
    }
    
    public void method() {
        // Local class
        class Local {
            // Defined inside method
        }
        
        // Anonymous class
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // Inline implementation
            }
        };
    }
}
```

### Quick Comparison

| Type | Can access outer instance? | When to use |
|------|---------------------------|-------------|
| Static nested | No | Helper class, doesn't need outer instance |
| Inner class | Yes | Logically tied to outer instance |
| Local class | Yes | Needed only in one method |
| Anonymous class | Yes | One-time use, especially for callbacks |

*Detailed coverage in the Inner Classes topic.*

---

## Record Classes (Java 16+)

Records are a concise way to create immutable data classes. They automatically generate constructors, accessors, `equals()`, `hashCode()`, and `toString()`.

### Basic Record Syntax

```java
// Record declaration - one line!
public record Person(String name, int age) { }

// This is equivalent to writing:
public final class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String name() { return name; }  // No "get" prefix
    public int age() { return age; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    
    @Override
    public String toString() {
        return "Person[name=" + name + ", age=" + age + "]";
    }
}
```

### Using Records

```java
// Creating record instances
Person person = new Person("Alice", 25);

// Accessing components (no "get" prefix)
String name = person.name();   // "Alice"
int age = person.age();        // 25

// toString is automatically generated
System.out.println(person);    // Person[name=Alice, age=25]

// equals compares all components
Person p1 = new Person("Alice", 25);
Person p2 = new Person("Alice", 25);
Person p3 = new Person("Bob", 30);

System.out.println(p1.equals(p2));  // true (same values)
System.out.println(p1.equals(p3));  // false

// hashCode is consistent with equals
System.out.println(p1.hashCode() == p2.hashCode());  // true

// Works with collections
Set<Person> people = new HashSet<>();
people.add(p1);
people.add(p2);  // Not added, duplicate
System.out.println(people.size());  // 1
```

### Record Components

```java
// Multiple components of various types
public record Book(
    String isbn,
    String title,
    String author,
    int pages,
    double price,
    boolean available
) { }

// Components can be any type including other records
public record Address(String street, String city, String zipCode) { }
public record Customer(String name, Address address) { }

// Usage with nested records
Address addr = new Address("123 Main St", "Springfield", "12345");
Customer customer = new Customer("John Doe", addr);

System.out.println(customer.address().city());  // "Springfield"
```

### Compact Constructors

The compact constructor is used for validation and normalization. It does not need to assign fields - that happens automatically.

```java
public record Email(String address) {
    // Compact constructor - no parameters in parentheses
    public Email {
        // Validation
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!address.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Normalization - reassign the parameter (not the field)
        address = address.toLowerCase().trim();
    }
}

Email email = new Email("  JOHN@EXAMPLE.COM  ");
System.out.println(email.address());  // "john@example.com"
```

### Canonical Constructors

You can also write the full canonical constructor if needed:

```java
public record Range(int start, int end) {
    // Canonical constructor with explicit parameters
    public Range(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("start must be <= end");
        }
        // Must explicitly assign fields
        this.start = start;
        this.end = end;
    }
    
    public int length() {
        return end - start;
    }
}
```

### Additional Constructors

Records can have additional constructors that must delegate to the canonical constructor:

```java
public record Rectangle(double width, double height) {
    // Compact constructor for validation
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
    
    // Additional constructor - must call canonical constructor
    public Rectangle(double side) {
        this(side, side);  // Creates a square
    }
    
    // Static factory method alternative
    public static Rectangle square(double side) {
        return new Rectangle(side, side);
    }
    
    public double area() {
        return width * height;
    }
}

Rectangle rect = new Rectangle(4.0, 5.0);
Rectangle square = new Rectangle(5.0);        // Using additional constructor
Rectangle square2 = Rectangle.square(5.0);    // Using factory method
```

### Adding Methods to Records

Records can have instance methods, static methods, and static fields:

```java
public record Point(double x, double y) {
    // Static fields are allowed
    public static final Point ORIGIN = new Point(0, 0);
    
    // Instance methods
    public double distanceFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }
    
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public Point translate(double dx, double dy) {
        return new Point(x + dx, y + dy);  // Returns new instance (immutable)
    }
    
    // Static methods
    public static Point fromPolar(double r, double theta) {
        return new Point(r * Math.cos(theta), r * Math.sin(theta));
    }
}

Point p1 = new Point(3, 4);
Point p2 = p1.translate(1, 1);  // New point (4, 5)
double dist = p1.distanceFromOrigin();  // 5.0
```

### Records with Interfaces

Records can implement interfaces:

```java
public interface Drawable {
    void draw();
}

public interface Measurable {
    double measure();
}

public record Circle(double radius) implements Drawable, Measurable {
    public Circle {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing circle with radius " + radius);
    }
    
    @Override
    public double measure() {
        return Math.PI * radius * radius;  // Area
    }
    
    public double circumference() {
        return 2 * Math.PI * radius;
    }
}
```

### Records with Generics

Records can be generic:

```java
// Generic pair record
public record Pair<T, U>(T first, U second) { }

Pair<String, Integer> pair = new Pair<>("age", 25);
String key = pair.first();   // "age"
Integer value = pair.second(); // 25

// Generic result record
public record Result<T>(T value, String error) {
    public boolean isSuccess() {
        return error == null;
    }
    
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }
    
    public static <T> Result<T> failure(String error) {
        return new Result<>(null, error);
    }
}

Result<Integer> success = Result.success(42);
Result<Integer> failure = Result.failure("Not found");

if (success.isSuccess()) {
    System.out.println("Value: " + success.value());
}
```

### Local Records

Records can be declared locally within methods (Java 16+):

```java
public List<String> processData(List<Integer> numbers) {
    // Local record - only visible in this method
    record Stats(int min, int max, double avg) { }
    
    int min = numbers.stream().min(Integer::compare).orElse(0);
    int max = numbers.stream().max(Integer::compare).orElse(0);
    double avg = numbers.stream().mapToInt(i -> i).average().orElse(0);
    
    Stats stats = new Stats(min, max, avg);
    
    return List.of(
        "Min: " + stats.min(),
        "Max: " + stats.max(),
        "Avg: " + stats.avg()
    );
}
```

### Record Patterns (Java 21+)

Records work with pattern matching for powerful deconstruction:

```java
public record Point(int x, int y) { }

// Pattern matching with instanceof
Object obj = new Point(3, 4);
if (obj instanceof Point(int x, int y)) {
    System.out.println("x = " + x + ", y = " + y);
}

// Pattern matching in switch
public String describe(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
        case Point(int x, int y) when x == y -> "On diagonal at " + x;
        case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
        case null -> "Null";
        default -> "Unknown";
    };
}

// Nested record patterns
public record Line(Point start, Point end) { }

Line line = new Line(new Point(0, 0), new Point(10, 10));

if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    System.out.println("Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
}
```

### Records in Collections

```java
// Records work great with streams and collections
List<Person> people = List.of(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Charlie", 35)
);

// Sorting
List<Person> sortedByAge = people.stream()
    .sorted(Comparator.comparing(Person::age))
    .toList();

// Filtering
List<Person> adults = people.stream()
    .filter(p -> p.age() >= 18)
    .toList();

// Grouping
Map<Integer, List<Person>> byAge = people.stream()
    .collect(Collectors.groupingBy(Person::age));

// As map keys (equals/hashCode work correctly)
Map<Person, String> personRoles = new HashMap<>();
personRoles.put(new Person("Alice", 25), "Admin");
String role = personRoles.get(new Person("Alice", 25));  // "Admin"
```

### Records for DTOs and Value Objects

```java
// Data Transfer Object
public record UserDTO(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) { }

// API Response
public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    int statusCode
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, "Success", 200);
    }
    
    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>(false, null, message, code);
    }
}

// Configuration
public record DatabaseConfig(
    String host,
    int port,
    String database,
    String username,
    String password
) {
    public String connectionUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}
```

### Record Limitations

Records have some restrictions:

```java
// 1. Records are implicitly final - cannot be extended
public record Point(int x, int y) { }
// public class Point3D extends Point { }  // Error!

// 2. Records cannot extend other classes (already extend Record)
// public record MyRecord() extends SomeClass { }  // Error!

// 3. All fields are final - truly immutable
Point p = new Point(1, 2);
// p.x = 5;  // Error! Cannot assign to final field

// 4. Cannot declare additional instance fields
public record Person(String name) {
    // private int age;  // Error! Cannot have instance fields
    
    // But static fields are allowed
    private static int counter = 0;
}

// 5. Cannot have instance initializers
public record Data(String value) {
    // { System.out.println("Init"); }  // Error!
}
```

### When to Use Records vs Classes

| Use Records When | Use Classes When |
|-----------------|------------------|
| Data is immutable | Need mutable state |
| Simple data container | Complex behavior |
| Value-based equality | Identity-based equality |
| No inheritance needed | Need to extend a class |
| Want automatic equals/hashCode | Custom equality logic |
| DTOs, value objects | Entity objects with lifecycle |

### Record Best Practices

```java
// 1. Use compact constructor for validation
public record Age(int value) {
    public Age {
        if (value < 0 || value > 150) {
            throw new IllegalArgumentException("Invalid age: " + value);
        }
    }
}

// 2. Add factory methods for common cases
public record Color(int red, int green, int blue) {
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(255, 255, 255);
    
    public static Color fromHex(String hex) {
        // Parse hex string
        int r = Integer.parseInt(hex.substring(1, 3), 16);
        int g = Integer.parseInt(hex.substring(3, 5), 16);
        int b = Integer.parseInt(hex.substring(5, 7), 16);
        return new Color(r, g, b);
    }
}

// 3. Return new records for transformations (immutability)
public record Money(BigDecimal amount, String currency) {
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}

// 4. Use records for method return types
public record SearchResult(List<Item> items, int totalCount, int page) { }

public SearchResult search(String query, int page) {
    // ... search logic
    return new SearchResult(items, total, page);
}
```

---

## Practical Examples

### Complete Class Example

```java
import java.util.Objects;

public class Product {
    // Constants
    private static final double DEFAULT_TAX_RATE = 0.10;
    
    // Static fields
    private static int productCount = 0;
    
    // Instance fields
    private final String sku;
    private String name;
    private double price;
    private int quantity;
    
    // Static initializer
    static {
        System.out.println("Product class loaded");
    }
    
    // Instance initializer
    {
        productCount++;
    }
    
    // Constructors
    public Product(String sku, String name, double price) {
        this(sku, name, price, 0);
    }
    
    public Product(String sku, String name, double price, int quantity) {
        this.sku = Objects.requireNonNull(sku, "SKU cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        setPrice(price);
        setQuantity(quantity);
    }
    
    // Copy constructor
    public Product(Product other) {
        this(other.sku, other.name, other.price, other.quantity);
    }
    
    // Static methods
    public static int getProductCount() {
        return productCount;
    }
    
    public static double calculateTax(double amount) {
        return amount * DEFAULT_TAX_RATE;
    }
    
    // Getters
    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    
    // Setters with validation
    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }
    
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
    
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }
    
    // Business methods
    public double getTotalValue() {
        return price * quantity;
    }
    
    public double getPriceWithTax() {
        return price + calculateTax(price);
    }
    
    public boolean isInStock() {
        return quantity > 0;
    }
    
    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative stock");
        }
        this.quantity += amount;
    }
    
    public void removeStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot remove negative stock");
        }
        if (amount > quantity) {
            throw new IllegalStateException("Insufficient stock");
        }
        this.quantity -= amount;
    }
    
    // Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(sku, product.sku);  // SKU is unique identifier
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
    
    @Override
    public String toString() {
        return "Product{" +
               "sku='" + sku + '\'' +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               '}';
    }
}
```

### Builder Pattern

```java
public class User {
    private final String username;    // Required
    private final String email;       // Required
    private final String firstName;   // Optional
    private final String lastName;    // Optional
    private final int age;            // Optional
    
    private User(Builder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
    }
    
    // Getters only (immutable)
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    
    // Static builder method
    public static Builder builder(String username, String email) {
        return new Builder(username, email);
    }
    
    // Builder class
    public static class Builder {
        // Required
        private final String username;
        private final String email;
        
        // Optional with defaults
        private String firstName = "";
        private String lastName = "";
        private int age = 0;
        
        public Builder(String username, String email) {
            this.username = username;
            this.email = email;
        }
        
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}

// Usage
User user = User.builder("alice", "alice@example.com")
    .firstName("Alice")
    .lastName("Smith")
    .age(25)
    .build();
```

### Factory Pattern

```java
public abstract class Shape {
    public abstract double area();
    public abstract double perimeter();
    
    // Factory method
    public static Shape create(String type, double... params) {
        return switch (type.toLowerCase()) {
            case "circle" -> new Circle(params[0]);
            case "rectangle" -> new Rectangle(params[0], params[1]);
            case "square" -> new Square(params[0]);
            default -> throw new IllegalArgumentException("Unknown shape: " + type);
        };
    }
}

class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// Usage
Shape circle = Shape.create("circle", 5.0);
Shape rect = Shape.create("rectangle", 4.0, 6.0);
```

---

## Best Practices

### Class Design

```java
// 1. Keep classes focused (Single Responsibility)
// Bad: One class doing too much
public class UserManager {
    public void createUser() { }
    public void sendEmail() { }
    public void generateReport() { }
    public void connectDatabase() { }
}

// Good: Separate concerns
public class UserService { public void createUser() { } }
public class EmailService { public void sendEmail() { } }
public class ReportService { public void generateReport() { } }

// 2. Make classes immutable when possible
public final class ImmutablePerson {
    private final String name;
    private final int age;
    
    public ImmutablePerson(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    public ImmutablePerson withAge(int newAge) {
        return new ImmutablePerson(name, newAge);
    }
}

// 3. Validate constructor arguments
public class Account {
    private final String id;
    
    public Account(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be blank");
        }
        this.id = id;
    }
}

// 4. Use private fields with public getters/setters
// Covered in Encapsulation topic
```

### Constructor Best Practices

```java
// 1. Chain constructors to avoid duplication
public class Product {
    public Product() {
        this("Unknown", 0.0);
    }
    
    public Product(String name) {
        this(name, 0.0);
    }
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

// 2. Consider static factory methods over constructors
public class Color {
    private final int red, green, blue;
    
    private Color(int r, int g, int b) {
        this.red = r; this.green = g; this.blue = b;
    }
    
    public static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }
    
    public static Color fromHex(String hex) {
        // Parse hex and create Color
        return new Color(/* parsed values */);
    }
}

// 3. Document constructor behavior
/**
 * Creates a new User with the specified credentials.
 *
 * @param username the unique username, must not be null or blank
 * @param email the email address, must be valid format
 * @throws IllegalArgumentException if username is blank or email is invalid
 */
public User(String username, String email) {
    // ...
}
```

---

## Summary

### Class Components

| Component | Description |
|-----------|-------------|
| Fields | Store object state (instance or static) |
| Constructors | Initialize new objects |
| Methods | Define object behavior |
| Static members | Belong to class, not instances |
| this | Reference to current object |
| Nested classes | Classes defined within classes |

### Key Concepts

| Concept | Description |
|---------|-------------|
| Instantiation | Creating objects with `new` |
| Reference | Variable holding memory address of object |
| null | Reference pointing to nothing |
| Constructor chaining | Calling one constructor from another |
| Initialization order | Static -> instance fields -> instance blocks -> constructor |

### Important Methods from Object

| Method | Purpose |
|--------|---------|
| `toString()` | String representation |
| `equals()` | Logical equality comparison |
| `hashCode()` | Hash code for collections |
| `getClass()` | Runtime class information |
| `clone()` | Create object copy |

**Key Points:**
- Classes are blueprints, objects are instances
- Use `new` keyword to create objects
- Constructors initialize object state
- `this` refers to current object
- Static members belong to the class
- Override `toString()`, `equals()`, and `hashCode()` for custom classes
- Consider using records for simple data classes (Java 16+)
- Follow single responsibility principle
- Validate input in constructors and setters

---

[<- Previous: Date and Time](09-date-time.md) | [Next: Encapsulation ->](11-encapsulation.md) | [Back to Guide](../guide.md)
