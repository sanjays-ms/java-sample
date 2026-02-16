# Encapsulation

[<- Previous: Classes and Objects](10-classes.md) | [Next: Inheritance ->](12-inheritance.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Encapsulation Cheat Sheet](../cheatsheets/encapsulation-cheatsheet.md)

---

## Overview

Encapsulation is one of the four fundamental principles of Object-Oriented Programming (OOP). It is the practice of bundling data (fields) and methods that operate on that data within a single unit (class), and restricting direct access to some of the object's components.

**Key Benefits:**
- **Data Protection** - Prevent unauthorized or invalid modifications
- **Implementation Hiding** - Hide internal details, expose only what is necessary
- **Flexibility** - Change internal implementation without affecting external code
- **Maintainability** - Easier to modify and debug
- **Control** - Add validation, logging, or computed values

```java
// Poor encapsulation - public fields
public class BankAccountBad {
    public double balance;  // Anyone can modify directly!
}

BankAccountBad account = new BankAccountBad();
account.balance = -1000;  // Invalid state allowed!

// Good encapsulation - private fields with controlled access
public class BankAccountGood {
    private double balance;
    
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
```

---

## Access Modifiers

Java provides four access modifiers to control visibility of classes, fields, methods, and constructors.

### Access Modifier Table

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (default/package-private) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

### public

Accessible from anywhere.

```java
public class PublicClass {
    public int publicField = 10;
    
    public void publicMethod() {
        System.out.println("Accessible from anywhere");
    }
}

// From any class, any package
PublicClass obj = new PublicClass();
System.out.println(obj.publicField);  // OK
obj.publicMethod();                    // OK
```

### private

Accessible only within the same class.

```java
public class PrivateExample {
    private int secretData = 42;
    
    private void secretMethod() {
        System.out.println("Only this class can call me");
    }
    
    public void publicMethod() {
        // Same class can access private members
        System.out.println(secretData);
        secretMethod();
    }
}

PrivateExample obj = new PrivateExample();
// obj.secretData;      // Error! Cannot access private field
// obj.secretMethod();  // Error! Cannot access private method
obj.publicMethod();     // OK - this can access private members internally
```

### protected

Accessible within the same package and by subclasses (even in different packages).

```java
// In package com.example.base
package com.example.base;

public class Animal {
    protected String name;
    
    protected void makeSound() {
        System.out.println("Some sound");
    }
}

// In package com.example.animals (different package)
package com.example.animals;

import com.example.base.Animal;

public class Dog extends Animal {
    public void bark() {
        // Can access protected members from parent
        name = "Buddy";        // OK
        makeSound();           // OK
        System.out.println(name + " barks!");
    }
}

// In package com.example.other
package com.example.other;

import com.example.base.Animal;

public class OtherClass {
    public void test() {
        Animal animal = new Animal();
        // animal.name = "Test";   // Error! Not a subclass
        // animal.makeSound();     // Error! Not a subclass
    }
}
```

### Default (Package-Private)

No modifier means accessible only within the same package.

```java
// In package com.example
package com.example;

class PackageClass {           // Package-private class
    int packageField = 10;     // Package-private field
    
    void packageMethod() {     // Package-private method
        System.out.println("Package-private");
    }
}

public class SamePackage {
    public void test() {
        PackageClass obj = new PackageClass();
        obj.packageField = 20;   // OK - same package
        obj.packageMethod();     // OK - same package
    }
}

// In different package
package com.other;

import com.example.PackageClass;  // Error! PackageClass is not public
```

### Choosing the Right Access Modifier

```java
public class BestPractices {
    // Private: internal implementation details
    private int internalCounter;
    private void helperMethod() { }
    
    // Package-private: shared within package, not part of public API
    int sharedWithPackage;
    void packageHelper() { }
    
    // Protected: intended for subclass extension
    protected void hookMethod() { }
    
    // Public: part of the public API
    public void doSomething() { }
}
```

**General Guidelines:**
1. Start with `private` - the most restrictive
2. Use package-private for package-internal sharing
3. Use `protected` sparingly, mainly for extensibility
4. Use `public` only for intentional public API

---

## Getters and Setters

Getters and setters (accessors and mutators) provide controlled access to private fields.

### Basic Getters and Setters

```java
public class Person {
    private String name;
    private int age;
    
    // Getter for name
    public String getName() {
        return name;
    }
    
    // Setter for name
    public void setName(String name) {
        this.name = name;
    }
    
    // Getter for age
    public int getAge() {
        return age;
    }
    
    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }
}

Person person = new Person();
person.setName("Alice");
person.setAge(25);
System.out.println(person.getName());  // Alice
System.out.println(person.getAge());   // 25
```

### Naming Conventions

```java
public class NamingConventions {
    private String name;        // get/set
    private int count;          // get/set
    private boolean active;     // is/set (boolean)
    private boolean hasLicense; // has (already has prefix)
    
    // Standard getter/setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    // Boolean getter uses "is" prefix
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    // If field already starts with "has/is/can", use as-is
    public boolean hasLicense() { return hasLicense; }
    public void setHasLicense(boolean hasLicense) { this.hasLicense = hasLicense; }
}
```

### Setters with Validation

```java
public class Employee {
    private String name;
    private int age;
    private double salary;
    private String email;
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }
    
    public void setAge(int age) {
        if (age < 18 || age > 120) {
            throw new IllegalArgumentException("Age must be between 18 and 120");
        }
        this.age = age;
    }
    
    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }
    
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase();
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
    public String getEmail() { return email; }
}
```

### Read-Only Properties

Provide only getters for properties that should not be changed after initialization.

```java
public class ImmutablePoint {
    private final int x;
    private final int y;
    
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Only getters, no setters
    public int getX() { return x; }
    public int getY() { return y; }
    
    // Return new object for "modifications"
    public ImmutablePoint translate(int dx, int dy) {
        return new ImmutablePoint(x + dx, y + dy);
    }
}

public class Order {
    private final String orderId;        // Set once, never changed
    private final LocalDateTime created; // Set once
    private String status;               // Can change
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.created = LocalDateTime.now();
        this.status = "PENDING";
    }
    
    // Read-only
    public String getOrderId() { return orderId; }
    public LocalDateTime getCreated() { return created; }
    
    // Read-write
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
```

### Write-Only Properties

Rarely used, but useful for passwords or sensitive data.

```java
public class User {
    private String username;
    private String passwordHash;
    
    public void setPassword(String plainPassword) {
        // Hash the password - never store plain text
        this.passwordHash = hashPassword(plainPassword);
    }
    
    // No getter for password!
    
    public boolean checkPassword(String plainPassword) {
        return hashPassword(plainPassword).equals(this.passwordHash);
    }
    
    private String hashPassword(String password) {
        // Simplified - use proper hashing in production
        return Integer.toHexString(password.hashCode());
    }
}
```

### Computed Properties

Getters can compute values instead of returning stored fields.

```java
public class Rectangle {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    public void setWidth(double width) {
        if (width <= 0) throw new IllegalArgumentException();
        this.width = width;
    }
    
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException();
        this.height = height;
    }
    
    // Computed properties - no field, calculated on demand
    public double getArea() {
        return width * height;
    }
    
    public double getPerimeter() {
        return 2 * (width + height);
    }
    
    public double getDiagonal() {
        return Math.sqrt(width * width + height * height);
    }
    
    public boolean isSquare() {
        return width == height;
    }
}

Rectangle rect = new Rectangle(4, 5);
System.out.println(rect.getArea());       // 20.0 (computed)
System.out.println(rect.getPerimeter());  // 18.0 (computed)
```

### Lazy Initialization

Defer expensive computations until needed.

```java
public class ExpensiveResource {
    private Data data;          // Stored field
    private Report cachedReport; // Lazy field
    
    public ExpensiveResource(Data data) {
        this.data = data;
    }
    
    public Report getReport() {
        // Only compute once, when first requested
        if (cachedReport == null) {
            cachedReport = computeExpensiveReport();
        }
        return cachedReport;
    }
    
    private Report computeExpensiveReport() {
        // Time-consuming operation
        return new Report(data);
    }
    
    public void setData(Data data) {
        this.data = data;
        this.cachedReport = null;  // Invalidate cache
    }
}
```

---

## Defensive Copying

When returning or accepting mutable objects, create copies to prevent external modification.

### Returning Mutable Objects

```java
// Bad - returns reference to internal list
public class BadTeam {
    private List<String> members = new ArrayList<>();
    
    public List<String> getMembers() {
        return members;  // Exposes internal state!
    }
}

BadTeam team = new BadTeam();
team.getMembers().add("Hacker");  // Modifies internal state!

// Good - returns copy
public class GoodTeam {
    private List<String> members = new ArrayList<>();
    
    public List<String> getMembers() {
        return new ArrayList<>(members);  // Return copy
    }
    
    // Or return unmodifiable view
    public List<String> getMembersView() {
        return Collections.unmodifiableList(members);
    }
    
    // Or return immutable copy (Java 10+)
    public List<String> getMembersCopy() {
        return List.copyOf(members);
    }
    
    // Controlled modification
    public void addMember(String member) {
        if (member != null && !member.isBlank()) {
            members.add(member);
        }
    }
}
```

### Accepting Mutable Objects

```java
// Bad - stores reference to passed object
public class BadEvent {
    private Date eventDate;
    
    public BadEvent(Date eventDate) {
        this.eventDate = eventDate;  // Stores original reference
    }
    
    public Date getEventDate() {
        return eventDate;  // Returns original reference
    }
}

Date date = new Date();
BadEvent event = new BadEvent(date);
date.setTime(0);  // Modifies event's internal state!

// Good - defensive copying
public class GoodEvent {
    private final Date eventDate;
    
    public GoodEvent(Date eventDate) {
        // Copy on input
        this.eventDate = new Date(eventDate.getTime());
    }
    
    public Date getEventDate() {
        // Copy on output
        return new Date(eventDate.getTime());
    }
}

// Best - use immutable types
public class BestEvent {
    private final LocalDateTime eventDateTime;
    
    public BestEvent(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;  // Already immutable
    }
    
    public LocalDateTime getEventDateTime() {
        return eventDateTime;  // Safe to return directly
    }
}
```

### Arrays

```java
public class Scores {
    private final int[] values;
    
    public Scores(int[] values) {
        // Defensive copy on input
        this.values = Arrays.copyOf(values, values.length);
    }
    
    public int[] getValues() {
        // Defensive copy on output
        return Arrays.copyOf(values, values.length);
    }
    
    public int getValue(int index) {
        return values[index];  // Safe - returns primitive
    }
    
    public int size() {
        return values.length;
    }
}
```

---

## Encapsulation Patterns

### JavaBeans Pattern

Standard pattern with no-arg constructor and getters/setters.

```java
public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    
    // No-arg constructor
    public Customer() { }
    
    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// Usage
Customer customer = new Customer();
customer.setFirstName("John");
customer.setLastName("Doe");
customer.setEmail("john@example.com");
```

### Immutable Objects Pattern

Once created, object state cannot change.

```java
public final class ImmutablePerson {
    private final String name;
    private final int age;
    private final List<String> hobbies;
    
    public ImmutablePerson(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        // Defensive copy of mutable collection
        this.hobbies = List.copyOf(hobbies);
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public List<String> getHobbies() { return hobbies; }  // Already immutable
    
    // "Setter" returns new instance
    public ImmutablePerson withAge(int newAge) {
        return new ImmutablePerson(this.name, newAge, this.hobbies);
    }
    
    public ImmutablePerson withHobby(String hobby) {
        List<String> newHobbies = new ArrayList<>(this.hobbies);
        newHobbies.add(hobby);
        return new ImmutablePerson(this.name, this.age, newHobbies);
    }
}

// Usage
ImmutablePerson p1 = new ImmutablePerson("Alice", 25, List.of("Reading"));
ImmutablePerson p2 = p1.withAge(26);  // New object, p1 unchanged
ImmutablePerson p3 = p2.withHobby("Hiking");  // New object
```

### Builder Pattern with Encapsulation

```java
public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeout;
    
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeout = builder.timeout;
    }
    
    // Only getters
    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public int getTimeout() { return timeout; }
    
    public static Builder builder(String url) {
        return new Builder(url);
    }
    
    public static class Builder {
        private final String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeout = 30000;
        
        public Builder(String url) {
            this.url = Objects.requireNonNull(url);
        }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public Builder timeout(int timeout) {
            if (timeout <= 0) throw new IllegalArgumentException();
            this.timeout = timeout;
            return this;
        }
        
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}

// Usage
HttpRequest request = HttpRequest.builder("https://api.example.com")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("{\"key\": \"value\"}")
    .timeout(5000)
    .build();
```

### Fluent Setters

Return `this` for method chaining (useful for mutable objects).

```java
public class QueryBuilder {
    private String table;
    private List<String> columns = new ArrayList<>();
    private String whereClause;
    private String orderBy;
    private Integer limit;
    
    public QueryBuilder from(String table) {
        this.table = table;
        return this;
    }
    
    public QueryBuilder select(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }
    
    public QueryBuilder where(String condition) {
        this.whereClause = condition;
        return this;
    }
    
    public QueryBuilder orderBy(String column) {
        this.orderBy = column;
        return this;
    }
    
    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public String build() {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(columns.isEmpty() ? "*" : String.join(", ", columns));
        sql.append(" FROM ").append(table);
        if (whereClause != null) sql.append(" WHERE ").append(whereClause);
        if (orderBy != null) sql.append(" ORDER BY ").append(orderBy);
        if (limit != null) sql.append(" LIMIT ").append(limit);
        return sql.toString();
    }
}

String query = new QueryBuilder()
    .from("users")
    .select("id", "name", "email")
    .where("active = true")
    .orderBy("name")
    .limit(10)
    .build();
```

---

## Encapsulating Collections

### Hiding Internal Collections

```java
public class Library {
    private final Map<String, Book> booksByIsbn = new HashMap<>();
    
    // Don't expose the map directly!
    
    // Provide controlled access methods
    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Invalid book");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }
    
    public Book findByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }
    
    public boolean removeBook(String isbn) {
        return booksByIsbn.remove(isbn) != null;
    }
    
    public int getBookCount() {
        return booksByIsbn.size();
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(booksByIsbn.values());  // Return copy
    }
    
    public boolean hasBook(String isbn) {
        return booksByIsbn.containsKey(isbn);
    }
}
```

### Encapsulated Collection with Events

```java
public class ObservableList<T> {
    private final List<T> items = new ArrayList<>();
    private final List<Consumer<T>> addListeners = new ArrayList<>();
    private final List<Consumer<T>> removeListeners = new ArrayList<>();
    
    public void add(T item) {
        items.add(item);
        notifyAddListeners(item);
    }
    
    public boolean remove(T item) {
        boolean removed = items.remove(item);
        if (removed) {
            notifyRemoveListeners(item);
        }
        return removed;
    }
    
    public T get(int index) {
        return items.get(index);
    }
    
    public int size() {
        return items.size();
    }
    
    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }
    
    public void onAdd(Consumer<T> listener) {
        addListeners.add(listener);
    }
    
    public void onRemove(Consumer<T> listener) {
        removeListeners.add(listener);
    }
    
    private void notifyAddListeners(T item) {
        for (Consumer<T> listener : addListeners) {
            listener.accept(item);
        }
    }
    
    private void notifyRemoveListeners(T item) {
        for (Consumer<T> listener : removeListeners) {
            listener.accept(item);
        }
    }
}

// Usage
ObservableList<String> names = new ObservableList<>();
names.onAdd(name -> System.out.println("Added: " + name));
names.add("Alice");  // Prints: Added: Alice
```

---

## Information Hiding

### Hiding Implementation Details

```java
// Interface exposes only what users need
public interface UserRepository {
    void save(User user);
    User findById(String id);
    List<User> findAll();
    void delete(String id);
}

// Implementation details hidden
public class DatabaseUserRepository implements UserRepository {
    private final Connection connection;
    private final String tableName = "users";
    
    // All database-specific code is hidden
    private PreparedStatement prepareStatement(String sql) { ... }
    private User mapResultSet(ResultSet rs) { ... }
    
    @Override
    public void save(User user) {
        // SQL INSERT/UPDATE - hidden from callers
    }
    
    @Override
    public User findById(String id) {
        // SQL SELECT - hidden from callers
    }
    
    // ... other implementations
}

// Users of the interface don't know or care about:
// - Database connection details
// - SQL queries
// - Result set mapping
// - Transaction handling
```

### Package-Private Helpers

```java
// In package com.example.payment

// Public API
public class PaymentProcessor {
    private final PaymentValidator validator;
    private final PaymentGateway gateway;
    
    public PaymentResult process(Payment payment) {
        validator.validate(payment);
        return gateway.submit(payment);
    }
}

// Package-private - not part of public API
class PaymentValidator {
    void validate(Payment payment) {
        // Validation logic
    }
}

// Package-private - not part of public API  
class PaymentGateway {
    PaymentResult submit(Payment payment) {
        // Gateway communication
    }
}

// External users only see PaymentProcessor
// Internal classes are hidden
```

---

## Practical Examples

### Bank Account with Full Encapsulation

```java
public class BankAccount {
    // Private fields
    private final String accountNumber;
    private final String holderName;
    private double balance;
    private final List<Transaction> transactions;
    private boolean frozen;
    
    // Private constructor - use factory method
    private BankAccount(String accountNumber, String holderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
        this.frozen = false;
        
        if (initialDeposit > 0) {
            recordTransaction("INITIAL_DEPOSIT", initialDeposit);
        }
    }
    
    // Factory method with validation
    public static BankAccount open(String holderName, double initialDeposit) {
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Holder name required");
        }
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        
        String accountNumber = generateAccountNumber();
        return new BankAccount(accountNumber, holderName, initialDeposit);
    }
    
    // Read-only properties
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public boolean isFrozen() { return frozen; }
    
    // Defensive copy for transactions
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    
    // Business operations with validation
    public void deposit(double amount) {
        validateNotFrozen();
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        recordTransaction("DEPOSIT", amount);
    }
    
    public void withdraw(double amount) {
        validateNotFrozen();
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
        recordTransaction("WITHDRAWAL", -amount);
    }
    
    public void transfer(BankAccount destination, double amount) {
        validateNotFrozen();
        destination.validateNotFrozen();
        
        this.withdraw(amount);
        destination.deposit(amount);
        recordTransaction("TRANSFER_OUT", -amount);
    }
    
    public void freeze() {
        this.frozen = true;
    }
    
    public void unfreeze() {
        this.frozen = false;
    }
    
    // Private helper methods
    private void validateNotFrozen() {
        if (frozen) {
            throw new IllegalStateException("Account is frozen");
        }
    }
    
    private void recordTransaction(String type, double amount) {
        transactions.add(new Transaction(type, amount, LocalDateTime.now()));
    }
    
    private static String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
    
    // Inner record for transaction
    public record Transaction(String type, double amount, LocalDateTime timestamp) { }
}

// Usage
BankAccount account = BankAccount.open("Alice Smith", 1000.0);
account.deposit(500);
account.withdraw(200);

System.out.println(account.getBalance());  // 1300.0
System.out.println(account.getTransactions().size());  // 3
```

### Configuration Manager

```java
public class ConfigurationManager {
    private static ConfigurationManager instance;
    
    private final Properties properties;
    private final Path configPath;
    private LocalDateTime lastLoaded;
    
    private ConfigurationManager(Path configPath) {
        this.configPath = configPath;
        this.properties = new Properties();
        reload();
    }
    
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager(Path.of("config.properties"));
        }
        return instance;
    }
    
    // Type-safe getters
    public String getString(String key) {
        return properties.getProperty(key);
    }
    
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
    
    public List<String> getList(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
            .map(String::trim)
            .toList();
    }
    
    // Controlled mutation
    public void reload() {
        try (InputStream in = Files.newInputStream(configPath)) {
            properties.clear();
            properties.load(in);
            lastLoaded = LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
    
    public LocalDateTime getLastLoaded() {
        return lastLoaded;
    }
    
    // No direct access to properties!
}
```

---

## Best Practices

### Encapsulation Guidelines

```java
// 1. Make fields private by default
public class Good {
    private int value;  // Private!
}

// 2. Provide getters/setters only when needed
public class User {
    private final String id;    // No setter - immutable
    private String name;        // Has setter - can change
    
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// 3. Validate in setters
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age");
    }
    this.age = age;
}

// 4. Use immutable types when possible
public class Event {
    private final LocalDateTime when;  // Immutable type
    // No defensive copying needed!
}

// 5. Return defensive copies of mutable objects
public List<String> getItems() {
    return new ArrayList<>(items);  // Or Collections.unmodifiableList()
}

// 6. Use the most restrictive access level
public class Service {
    private void internalHelper() { }      // Implementation detail
    void packageMethod() { }                // Shared in package
    protected void extensionPoint() { }     // For subclasses
    public void publicApi() { }             // External interface
}

// 7. Document public API
/**
 * Sets the user's email address.
 * @param email a valid email address
 * @throws IllegalArgumentException if email is null or invalid
 */
public void setEmail(String email) { ... }
```

---

## Summary

### Access Modifiers

| Modifier | Visibility |
|----------|------------|
| `private` | Same class only |
| (default) | Same package |
| `protected` | Same package + subclasses |
| `public` | Everywhere |

### Encapsulation Techniques

| Technique | Purpose |
|-----------|---------|
| Private fields | Hide internal state |
| Getters/Setters | Controlled access |
| Validation | Ensure valid state |
| Defensive copies | Protect mutable objects |
| Immutability | Prevent all changes |

### Common Patterns

| Pattern | Use Case |
|---------|----------|
| JavaBeans | Frameworks, serialization |
| Immutable | Thread safety, simplicity |
| Builder | Complex object construction |
| Fluent setters | Method chaining |

**Key Points:**
- Start with private, widen access only when necessary
- Always validate in setters
- Use defensive copying for mutable objects
- Prefer immutable objects when possible
- Hide implementation details
- Expose behavior, not data
- Keep fields private, provide controlled access

---

[<- Previous: Classes and Objects](10-classes.md) | [Next: Inheritance ->](12-inheritance.md) | [Back to Guide](../guide.md)
