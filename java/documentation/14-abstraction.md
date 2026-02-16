# Abstraction

[<- Previous: Polymorphism](13-polymorphism.md) | [Next: Inner Classes ->](15-inner-classes.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Abstraction Cheat Sheet](../cheatsheets/abstraction-cheatsheet.md)

---

## Overview

Abstraction is the process of hiding implementation details and showing only the essential features of an object. It focuses on "what" an object does rather than "how" it does it.

**Java provides abstraction through:**
1. **Abstract Classes** - Partial abstraction with some implementation
2. **Interfaces** - Complete abstraction (pure contract)

```java
// Abstraction example - users don't need to know HOW it works
public interface EmailService {
    void send(String to, String subject, String body);
}

// Implementation details hidden
public class SmtpEmailService implements EmailService {
    private final SmtpClient client;
    private final String host;
    private final int port;
    
    @Override
    public void send(String to, String subject, String body) {
        // Complex SMTP protocol handling hidden from users
        client.connect(host, port);
        client.authenticate();
        client.sendMail(to, subject, body);
        client.disconnect();
    }
}

// User only interacts with the abstraction
EmailService email = new SmtpEmailService();
email.send("user@example.com", "Hello", "Welcome!");
```

**Key Benefits:**
- **Simplicity** - Hide complex implementation details
- **Security** - Protect internal workings
- **Flexibility** - Change implementation without affecting users
- **Maintainability** - Reduce code coupling
- **Reusability** - Define contracts that multiple classes can implement

---

## Abstract Classes

An abstract class is a class that cannot be instantiated and may contain abstract methods (methods without implementation).

### Basic Abstract Class

```java
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    // Abstract method - no implementation, must be overridden
    public abstract void makeSound();
    
    // Abstract method
    public abstract void move();
    
    // Concrete method - has implementation, can be inherited or overridden
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
    
    // Concrete method
    public String getName() {
        return name;
    }
}

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " says: Woof!");
    }
    
    @Override
    public void move() {
        System.out.println(name + " runs on four legs");
    }
}

public class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " says: Chirp!");
    }
    
    @Override
    public void move() {
        System.out.println(name + " flies through the air");
    }
}

// Cannot instantiate abstract class
// Animal animal = new Animal("Generic");  // Error!

// Can create instances of concrete subclasses
Animal dog = new Dog("Buddy");
Animal bird = new Bird("Tweety");

dog.makeSound();  // "Buddy says: Woof!"
dog.sleep();      // "Buddy is sleeping" - inherited
bird.move();      // "Tweety flies through the air"
```

### Abstract Class Rules

```java
// Rule 1: Cannot be instantiated
public abstract class Shape { }
// Shape s = new Shape();  // Error!

// Rule 2: Can have constructors (called by subclasses)
public abstract class Vehicle {
    protected String brand;
    
    public Vehicle(String brand) {
        this.brand = brand;
    }
}

// Rule 3: Can have abstract and concrete methods
public abstract class Animal {
    public abstract void eat();           // Abstract
    public void breathe() { }             // Concrete
}

// Rule 4: Abstract methods have no body
public abstract class Example {
    public abstract void doSomething();   // No braces!
    // public abstract void wrong() { }   // Error!
}

// Rule 5: Subclass must implement all abstract methods OR be abstract itself
public abstract class PartialImplementation extends Animal {
    @Override
    public void eat() { }  // Implemented one
    // Still abstract because makeSound() not implemented
}

// Rule 6: Can have fields (including static and final)
public abstract class Config {
    private int value;
    public static final String VERSION = "1.0";
}

// Rule 7: Can have static methods
public abstract class Utility {
    public static void helper() { }
}
```

### Abstract Methods

```java
public abstract class Document {
    protected String title;
    protected String content;
    
    public Document(String title) {
        this.title = title;
    }
    
    // Abstract methods define the contract
    public abstract void save();
    public abstract void load(String path);
    public abstract String export();
    
    // Can have different access modifiers (not private)
    protected abstract void validate();
    abstract void internalProcess();  // Package-private
    
    // Cannot be private - must be visible to subclasses
    // private abstract void hidden();  // Error!
    
    // Cannot be final - must be overridable
    // public final abstract void fixed();  // Error!
    
    // Cannot be static - instance method contract
    // public static abstract void utility();  // Error!
}
```

### Partial Implementation

```java
public abstract class HttpHandler {
    // Template method pattern
    public final void handle(Request request, Response response) {
        logRequest(request);
        if (authenticate(request)) {
            process(request, response);
        } else {
            response.setStatus(401);
        }
        logResponse(response);
    }
    
    // Concrete methods
    private void logRequest(Request request) {
        System.out.println("Received: " + request);
    }
    
    private void logResponse(Response response) {
        System.out.println("Sent: " + response);
    }
    
    // Abstract methods - subclasses decide implementation
    protected abstract boolean authenticate(Request request);
    protected abstract void process(Request request, Response response);
}

public class ApiHandler extends HttpHandler {
    @Override
    protected boolean authenticate(Request request) {
        String token = request.getHeader("Authorization");
        return validateToken(token);
    }
    
    @Override
    protected void process(Request request, Response response) {
        // Handle API request
        response.setBody(processApiRequest(request));
    }
    
    private boolean validateToken(String token) { return token != null; }
    private String processApiRequest(Request request) { return "{}"; }
}

public class WebHandler extends HttpHandler {
    @Override
    protected boolean authenticate(Request request) {
        String session = request.getCookie("session");
        return validateSession(session);
    }
    
    @Override
    protected void process(Request request, Response response) {
        // Render HTML page
        response.setBody(renderPage(request));
    }
    
    private boolean validateSession(String session) { return session != null; }
    private String renderPage(Request request) { return "<html>...</html>"; }
}
```

---

## Interfaces

An interface is a completely abstract type that defines a contract of methods that implementing classes must provide.

### Basic Interface

```java
public interface Drawable {
    void draw();
}

public interface Resizable {
    void resize(double factor);
    double getWidth();
    double getHeight();
}

public class Circle implements Drawable, Resizable {
    private double radius;
    private double x, y;
    
    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing circle at (" + x + "," + y + ") with radius " + radius);
    }
    
    @Override
    public void resize(double factor) {
        radius *= factor;
    }
    
    @Override
    public double getWidth() {
        return radius * 2;
    }
    
    @Override
    public double getHeight() {
        return radius * 2;
    }
}
```

### Interface Features (Java 8+)

```java
public interface ModernInterface {
    // Constant (implicitly public static final)
    String VERSION = "1.0";
    int MAX_SIZE = 100;
    
    // Abstract method (implicitly public abstract)
    void process();
    
    // Default method (Java 8+) - has implementation
    default void log(String message) {
        System.out.println("[LOG] " + message);
    }
    
    // Static method (Java 8+)
    static ModernInterface create() {
        return new DefaultImplementation();
    }
    
    // Private method (Java 9+) - helper for default methods
    private void helper() {
        System.out.println("Helper logic");
    }
    
    // Private static method (Java 9+)
    private static void staticHelper() {
        System.out.println("Static helper");
    }
}

class DefaultImplementation implements ModernInterface {
    @Override
    public void process() {
        log("Processing...");  // Uses default method
    }
}
```

### Default Methods

Default methods allow adding new methods to interfaces without breaking existing implementations.

```java
public interface Collection<E> {
    // Abstract methods
    boolean add(E element);
    boolean remove(E element);
    int size();
    
    // Default method - provides default implementation
    default boolean isEmpty() {
        return size() == 0;
    }
    
    // Default method with logic
    default void addAll(Collection<E> other) {
        for (E element : other) {
            add(element);
        }
    }
    
    // Default method calling abstract method
    default boolean contains(E element) {
        for (E e : this) {
            if (e.equals(element)) return true;
        }
        return false;
    }
}

public class MyList<E> implements Collection<E> {
    private List<E> items = new ArrayList<>();
    
    @Override
    public boolean add(E element) {
        return items.add(element);
    }
    
    @Override
    public boolean remove(E element) {
        return items.remove(element);
    }
    
    @Override
    public int size() {
        return items.size();
    }
    
    // isEmpty() and addAll() inherited from interface defaults
    
    // Can override default methods if needed
    @Override
    public boolean contains(E element) {
        return items.contains(element);  // More efficient
    }
}
```

### Static Methods in Interfaces

```java
public interface StringUtils {
    // Static factory method
    static StringUtils create() {
        return new DefaultStringUtils();
    }
    
    // Utility methods
    static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
    
    static String capitalize(String s) {
        if (isNullOrEmpty(s)) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
    // Abstract method for implementations
    String process(String input);
}

// Usage - call static methods on interface
boolean empty = StringUtils.isNullOrEmpty("");  // true
String cap = StringUtils.capitalize("hello");    // "Hello"
```

### Private Methods (Java 9+)

```java
public interface Logger {
    void log(String message);
    
    default void info(String message) {
        log(formatMessage("INFO", message));
    }
    
    default void warn(String message) {
        log(formatMessage("WARN", message));
    }
    
    default void error(String message) {
        log(formatMessage("ERROR", message));
    }
    
    // Private helper method - reduces code duplication
    private String formatMessage(String level, String message) {
        return "[" + level + "] " + timestamp() + " - " + message;
    }
    
    // Private static helper
    private static String timestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
```

### Multiple Interface Inheritance

```java
public interface Flyable {
    void fly();
    
    default void takeOff() {
        System.out.println("Taking off...");
    }
}

public interface Swimmable {
    void swim();
    
    default void dive() {
        System.out.println("Diving...");
    }
}

public interface Walkable {
    void walk();
}

// Class can implement multiple interfaces
public class Duck implements Flyable, Swimmable, Walkable {
    @Override
    public void fly() {
        System.out.println("Duck flying");
    }
    
    @Override
    public void swim() {
        System.out.println("Duck swimming");
    }
    
    @Override
    public void walk() {
        System.out.println("Duck walking");
    }
    
    // Inherits takeOff() from Flyable
    // Inherits dive() from Swimmable
}

Duck duck = new Duck();
duck.fly();     // "Duck flying"
duck.swim();    // "Duck swimming"
duck.walk();    // "Duck walking"
duck.takeOff(); // "Taking off..."
duck.dive();    // "Diving..."
```

### Resolving Default Method Conflicts

```java
public interface InterfaceA {
    default void greet() {
        System.out.println("Hello from A");
    }
}

public interface InterfaceB {
    default void greet() {
        System.out.println("Hello from B");
    }
}

// Must resolve conflict explicitly
public class MyClass implements InterfaceA, InterfaceB {
    @Override
    public void greet() {
        // Option 1: Provide own implementation
        System.out.println("Hello from MyClass");
        
        // Option 2: Call specific interface's default
        // InterfaceA.super.greet();
        
        // Option 3: Call both
        // InterfaceA.super.greet();
        // InterfaceB.super.greet();
    }
}
```

---

## Functional Interfaces

A functional interface has exactly one abstract method and can be used with lambda expressions.

### Built-in Functional Interfaces

```java
import java.util.function.*;

// Predicate<T> - takes T, returns boolean
Predicate<String> isEmpty = s -> s.isEmpty();
Predicate<Integer> isPositive = n -> n > 0;

boolean result = isEmpty.test("");  // true

// Function<T, R> - takes T, returns R
Function<String, Integer> length = s -> s.length();
Function<Integer, String> toString = Object::toString;

int len = length.apply("Hello");  // 5

// Consumer<T> - takes T, returns void
Consumer<String> print = System.out::println;
Consumer<List<?>> clear = List::clear;

print.accept("Hello");  // Prints "Hello"

// Supplier<T> - takes nothing, returns T
Supplier<Double> random = Math::random;
Supplier<LocalDate> today = LocalDate::now;

double r = random.get();  // Random number

// BiFunction<T, U, R> - takes T and U, returns R
BiFunction<String, String, String> concat = (a, b) -> a + b;

String combined = concat.apply("Hello", "World");  // "HelloWorld"

// UnaryOperator<T> - takes T, returns T
UnaryOperator<String> upper = String::toUpperCase;

String result2 = upper.apply("hello");  // "HELLO"

// BinaryOperator<T> - takes T and T, returns T
BinaryOperator<Integer> add = (a, b) -> a + b;

int sum = add.apply(5, 3);  // 8
```

### Custom Functional Interfaces

```java
@FunctionalInterface
public interface Validator<T> {
    boolean validate(T value);
    
    // Can have default methods
    default Validator<T> and(Validator<T> other) {
        return value -> this.validate(value) && other.validate(value);
    }
    
    default Validator<T> or(Validator<T> other) {
        return value -> this.validate(value) || other.validate(value);
    }
    
    default Validator<T> negate() {
        return value -> !this.validate(value);
    }
    
    // Can have static methods
    static <T> Validator<T> notNull() {
        return value -> value != null;
    }
}

// Usage
Validator<String> notEmpty = s -> !s.isEmpty();
Validator<String> notNull = Validator.notNull();
Validator<String> validString = notNull.and(notEmpty);

boolean valid = validString.validate("Hello");  // true
boolean invalid = validString.validate("");     // false
```

### Functional Interface with Generics

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);
    
    default <V> Transformer<T, V> andThen(Transformer<R, V> after) {
        return input -> after.transform(this.transform(input));
    }
    
    static <T> Transformer<T, T> identity() {
        return t -> t;
    }
}

Transformer<String, Integer> toLength = String::length;
Transformer<Integer, String> toHex = Integer::toHexString;

Transformer<String, String> lengthToHex = toLength.andThen(toHex);
String result = lengthToHex.transform("Hello");  // "5"
```

---

## Abstract Classes vs Interfaces

### When to Use Abstract Classes

```java
// Use abstract class when:

// 1. You need to share code among closely related classes
public abstract class Animal {
    protected String name;
    protected int age;
    
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    public abstract void makeSound();
}

// 2. You need non-public members
public abstract class BaseRepository {
    protected Connection connection;  // Protected field
    
    protected void connect() { }  // Protected method
    
    public abstract void save(Object entity);
}

// 3. You need constructors
public abstract class Vehicle {
    private final String vin;
    
    protected Vehicle(String vin) {
        this.vin = vin;
    }
}

// 4. You need non-static, non-final fields
public abstract class Counter {
    private int count = 0;  // Mutable state
    
    public void increment() { count++; }
    public abstract void process();
}
```

### When to Use Interfaces

```java
// Use interface when:

// 1. Unrelated classes would implement the interface
public interface Comparable<T> {
    int compareTo(T other);
}
// String, Integer, LocalDate all implement Comparable

// 2. You want to specify behavior without implementation
public interface Runnable {
    void run();
}

// 3. You want multiple inheritance of type
public class Duck implements Flyable, Swimmable, Quackable { }

// 4. You want to define a contract/API
public interface PaymentGateway {
    PaymentResult charge(String cardNumber, double amount);
    PaymentResult refund(String transactionId);
}

// 5. You want to use lambda expressions
@FunctionalInterface
public interface EventHandler {
    void handle(Event event);
}

EventHandler handler = event -> System.out.println(event);
```

### Comparison Table

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Instantiation | No | No |
| Constructors | Yes | No |
| Fields | Any type | Only public static final |
| Methods | Any type | public abstract, default, static, private |
| Access modifiers | Any | public only (mostly) |
| Inheritance | Single (extends) | Multiple (implements) |
| Use with lambdas | No | Yes (if functional) |

### Combining Both

```java
// Interface defines the contract
public interface Shape {
    double area();
    double perimeter();
    void draw();
}

// Abstract class provides partial implementation
public abstract class AbstractShape implements Shape {
    protected String color;
    protected boolean filled;
    
    public AbstractShape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }
    
    public String getColor() { return color; }
    public boolean isFilled() { return filled; }
    
    @Override
    public void draw() {
        System.out.println("Drawing " + (filled ? "filled " : "") + 
                           color + " " + getClass().getSimpleName());
    }
    
    // area() and perimeter() still abstract
}

// Concrete class completes the implementation
public class Circle extends AbstractShape {
    private double radius;
    
    public Circle(String color, boolean filled, double radius) {
        super(color, filled);
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
```

---

## Sealed Interfaces (Java 17+)

Sealed interfaces restrict which classes or interfaces can implement them.

```java
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {
    double area();
}

public final class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public final class Rectangle implements Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

public final class Triangle implements Shape {
    private final double base, height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// Exhaustive pattern matching
public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> c.area();
        case Rectangle r -> r.area();
        case Triangle t -> t.area();
        // No default needed - all cases covered
    };
}
```

### Sealed Interface Hierarchy

```java
public sealed interface Result<T> permits Success, Failure {
    boolean isSuccess();
}

public record Success<T>(T value) implements Result<T> {
    @Override
    public boolean isSuccess() { return true; }
}

public sealed interface Failure<T> extends Result<T> 
    permits ValidationError, NetworkError, UnknownError {
    String message();
    
    @Override
    default boolean isSuccess() { return false; }
}

public record ValidationError<T>(String field, String message) implements Failure<T> { }
public record NetworkError<T>(int statusCode, String message) implements Failure<T> { }
public record UnknownError<T>(String message, Throwable cause) implements Failure<T> { }

// Usage
public <T> void handleResult(Result<T> result) {
    switch (result) {
        case Success<T>(T value) -> System.out.println("Success: " + value);
        case ValidationError<T>(String field, String msg) -> 
            System.out.println("Validation error on " + field + ": " + msg);
        case NetworkError<T>(int code, String msg) -> 
            System.out.println("Network error " + code + ": " + msg);
        case UnknownError<T>(String msg, Throwable cause) -> 
            System.out.println("Unknown error: " + msg);
    }
}
```

---

## Design Patterns Using Abstraction

### Template Method Pattern

```java
public abstract class DataProcessor {
    // Template method - defines the algorithm skeleton
    public final void process() {
        readData();
        processData();
        writeData();
        cleanup();
    }
    
    // Abstract steps - subclasses provide implementation
    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
    
    // Hook method - optional override
    protected void cleanup() {
        // Default: do nothing
    }
}

public class CsvProcessor extends DataProcessor {
    private List<String[]> data;
    
    @Override
    protected void readData() {
        System.out.println("Reading CSV file...");
        // CSV reading logic
    }
    
    @Override
    protected void processData() {
        System.out.println("Processing CSV data...");
        // CSV processing logic
    }
    
    @Override
    protected void writeData() {
        System.out.println("Writing processed CSV...");
        // CSV writing logic
    }
}

public class JsonProcessor extends DataProcessor {
    private JsonNode data;
    
    @Override
    protected void readData() {
        System.out.println("Reading JSON file...");
    }
    
    @Override
    protected void processData() {
        System.out.println("Processing JSON data...");
    }
    
    @Override
    protected void writeData() {
        System.out.println("Writing processed JSON...");
    }
    
    @Override
    protected void cleanup() {
        System.out.println("Cleaning up JSON resources...");
    }
}
```

### Strategy Pattern

```java
public interface CompressionStrategy {
    byte[] compress(byte[] data);
    byte[] decompress(byte[] data);
}

public class ZipCompression implements CompressionStrategy {
    @Override
    public byte[] compress(byte[] data) {
        System.out.println("Compressing with ZIP...");
        // ZIP compression logic
        return data;
    }
    
    @Override
    public byte[] decompress(byte[] data) {
        System.out.println("Decompressing ZIP...");
        return data;
    }
}

public class GzipCompression implements CompressionStrategy {
    @Override
    public byte[] compress(byte[] data) {
        System.out.println("Compressing with GZIP...");
        return data;
    }
    
    @Override
    public byte[] decompress(byte[] data) {
        System.out.println("Decompressing GZIP...");
        return data;
    }
}

public class FileCompressor {
    private CompressionStrategy strategy;
    
    public FileCompressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(CompressionStrategy strategy) {
        this.strategy = strategy;
    }
    
    public byte[] compress(byte[] data) {
        return strategy.compress(data);
    }
    
    public byte[] decompress(byte[] data) {
        return strategy.decompress(data);
    }
}

// Usage
FileCompressor compressor = new FileCompressor(new ZipCompression());
byte[] compressed = compressor.compress(data);

compressor.setStrategy(new GzipCompression());
compressed = compressor.compress(data);
```

### Factory Pattern

```java
public interface Vehicle {
    void drive();
}

public class Car implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving a car");
    }
}

public class Motorcycle implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Riding a motorcycle");
    }
}

public class Truck implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving a truck");
    }
}

public interface VehicleFactory {
    Vehicle createVehicle();
}

public class CarFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle() {
        return new Car();
    }
}

public class MotorcycleFactory implements VehicleFactory {
    @Override
    public Vehicle createVehicle() {
        return new Motorcycle();
    }
}

// Or use a simple factory method
public class VehicleFactorySimple {
    public static Vehicle create(String type) {
        return switch (type.toLowerCase()) {
            case "car" -> new Car();
            case "motorcycle" -> new Motorcycle();
            case "truck" -> new Truck();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
```

---

## Practical Examples

### Repository Pattern

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(ID id);
    boolean existsById(ID id);
    long count();
}

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    protected abstract String getTableName();
    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract void setInsertParameters(PreparedStatement ps, T entity) throws SQLException;
    
    @Override
    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        // Execute query and map results
        return results;
    }
    
    @Override
    public boolean existsById(ID id) {
        return findById(id) != null;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM " + getTableName();
        // Execute and return count
        return 0;
    }
}

public class UserRepository extends AbstractRepository<User, Long> {
    @Override
    protected String getTableName() {
        return "users";
    }
    
    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email")
        );
    }
    
    @Override
    protected void setInsertParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
    }
    
    @Override
    public User findById(Long id) {
        // Implementation
        return null;
    }
    
    @Override
    public User save(User entity) {
        // Implementation
        return entity;
    }
    
    @Override
    public void delete(Long id) {
        // Implementation
    }
}
```

### Payment Processing

```java
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
    PaymentResult refund(String transactionId, double amount);
    PaymentStatus getStatus(String transactionId);
}

public abstract class AbstractPaymentProcessor implements PaymentProcessor {
    protected final String merchantId;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected AbstractPaymentProcessor(String merchantId) {
        this.merchantId = merchantId;
    }
    
    @Override
    public final PaymentResult process(PaymentRequest request) {
        logger.info("Processing payment for merchant: {}", merchantId);
        
        if (!validateRequest(request)) {
            return PaymentResult.failure("Invalid request");
        }
        
        try {
            return doProcess(request);
        } catch (Exception e) {
            logger.error("Payment processing failed", e);
            return PaymentResult.failure(e.getMessage());
        }
    }
    
    protected boolean validateRequest(PaymentRequest request) {
        return request != null && 
               request.getAmount() > 0 &&
               request.getCurrency() != null;
    }
    
    protected abstract PaymentResult doProcess(PaymentRequest request);
}

public class StripePaymentProcessor extends AbstractPaymentProcessor {
    private final StripeClient stripeClient;
    
    public StripePaymentProcessor(String merchantId, String apiKey) {
        super(merchantId);
        this.stripeClient = new StripeClient(apiKey);
    }
    
    @Override
    protected PaymentResult doProcess(PaymentRequest request) {
        // Stripe-specific implementation
        StripeCharge charge = stripeClient.createCharge(
            request.getAmount(),
            request.getCurrency(),
            request.getCardToken()
        );
        return PaymentResult.success(charge.getId());
    }
    
    @Override
    public PaymentResult refund(String transactionId, double amount) {
        // Stripe refund implementation
        return PaymentResult.success(transactionId);
    }
    
    @Override
    public PaymentStatus getStatus(String transactionId) {
        return stripeClient.getChargeStatus(transactionId);
    }
}

public class PayPalPaymentProcessor extends AbstractPaymentProcessor {
    private final PayPalClient paypalClient;
    
    public PayPalPaymentProcessor(String merchantId, String clientId, String secret) {
        super(merchantId);
        this.paypalClient = new PayPalClient(clientId, secret);
    }
    
    @Override
    protected PaymentResult doProcess(PaymentRequest request) {
        // PayPal-specific implementation
        return PaymentResult.success("PP-" + System.currentTimeMillis());
    }
    
    @Override
    public PaymentResult refund(String transactionId, double amount) {
        // PayPal refund implementation
        return PaymentResult.success(transactionId);
    }
    
    @Override
    public PaymentStatus getStatus(String transactionId) {
        return paypalClient.getStatus(transactionId);
    }
}
```

---

## Best Practices

### Abstraction Guidelines

```java
// 1. Define clear contracts
public interface UserService {
    User findById(Long id);
    User create(CreateUserRequest request);
    void delete(Long id);
}

// 2. Hide implementation details
public class DefaultUserService implements UserService {
    private final UserRepository repository;  // Hidden
    private final PasswordEncoder encoder;    // Hidden
    private final EmailService emailService;  // Hidden
    
    // Only interface methods exposed
}

// 3. Use the most abstract type possible
public void process(Collection<String> items) { }  // Good
public void process(ArrayList<String> items) { }   // Less flexible

// 4. Favor interfaces over abstract classes
public interface Sortable { }       // Prefer
public abstract class Sortable { }  // Use when state needed

// 5. Keep interfaces focused (Interface Segregation)
// Bad: One big interface
public interface Worker {
    void work();
    void eat();
    void sleep();
    void program();
    void manage();
}

// Good: Focused interfaces
public interface Workable { void work(); }
public interface Eatable { void eat(); }
public interface Programmer { void program(); }
public interface Manager { void manage(); }

// 6. Use default methods for backward compatibility
public interface Collection<E> {
    // New method with default implementation
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}

// 7. Document abstract methods
public abstract class Report {
    /**
     * Generates the report content.
     * @return the generated report as a string
     * @throws ReportException if generation fails
     */
    protected abstract String generate();
}
```

---

## Summary

### Abstract Classes

| Feature | Description |
|---------|-------------|
| Purpose | Partial abstraction with shared code |
| Methods | Abstract and concrete |
| Fields | Any type |
| Constructors | Yes |
| Inheritance | Single (extends) |

### Interfaces

| Feature | Description |
|---------|-------------|
| Purpose | Complete abstraction / contract |
| Methods | Abstract, default, static, private |
| Fields | public static final only |
| Constructors | No |
| Inheritance | Multiple (implements) |

### When to Use

| Use Case | Abstract Class | Interface |
|----------|----------------|-----------|
| Share code among related classes | Yes | Via default methods |
| Need constructors | Yes | No |
| Need non-public members | Yes | No |
| Multiple inheritance | No | Yes |
| Define API contract | Possible | Preferred |
| Use with lambdas | No | Yes (functional) |

### Key Points

- Abstraction hides complexity and shows only essentials
- Abstract classes: partial implementation, single inheritance
- Interfaces: pure contracts, multiple inheritance
- Use interfaces for API contracts and flexibility
- Use abstract classes for shared code among related classes
- Combine both for maximum flexibility
- Sealed types (Java 17+) allow controlled hierarchies

---

[<- Previous: Polymorphism](13-polymorphism.md) | [Next: Inner Classes ->](15-inner-classes.md) | [Back to Guide](../guide.md)
