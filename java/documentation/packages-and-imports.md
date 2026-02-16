# Packages and Imports

[Back to Guide](../guide.md)

**Cheat Sheet:** [Packages and Imports Cheat Sheet](../cheatsheets/packages-cheatsheet.md)

**Related:** [Encapsulation (Access Modifiers)](11-encapsulation.md)

---

## Overview

A **package** in Java is a way to organize related classes and interfaces into groups. Think of it like folders on your computer that help you organize files. Packages serve multiple purposes:

1. **Organization** - Group related classes together
2. **Name Collision Prevention** - Two classes can have the same name if they are in different packages
3. **Access Control** - Package-private access restricts visibility to classes within the same package
4. **Encapsulation** - Hide implementation details from other packages

---

## What Is a Package?

In plain terms, a package is a named container for classes. When you write Java code, every class belongs to some package.

```java
// This class belongs to the "com.example.shop" package
package com.example.shop;

public class Product {
    private String name;
    private double price;
    
    // Class implementation...
}
```

**What this means:**
- The `package` statement at the top declares which package this class belongs to
- The full name of this class is `com.example.shop.Product` (called the **fully qualified name**)
- This class must be in a folder structure matching the package: `com/example/shop/Product.java`

---

## Why Packages Exist

### Problem 1: Name Collisions

Without packages, you could only have one class named `Date` in your entire project. But Java itself has multiple `Date` classes:

```java
java.util.Date      // General purpose date (legacy)
java.sql.Date       // Database date

// Your application might also have:
com.myapp.model.Date  // Your custom date class
```

Packages let all these classes coexist because their full names are different.

### Problem 2: Organization

Real applications have hundreds or thousands of classes. Without packages, finding the right class would be like having all files on your computer in one folder.

```
// Without packages - chaos!
User.java
Product.java
Order.java
UserController.java
ProductController.java
OrderController.java
UserService.java
...hundreds more...

// With packages - organized!
com.myapp.model/
    User.java
    Product.java
    Order.java
com.myapp.controller/
    UserController.java
    ProductController.java
    OrderController.java
com.myapp.service/
    UserService.java
    ...
```

### Problem 3: Access Control

Sometimes you want classes to share internal details with each other but hide them from outside code. Package-private access (no modifier) enables this.

---

## The Package Declaration

Every Java source file can have at most one package declaration. It must be the first statement in the file (before any imports or class declarations).

### Syntax

```java
package packagename;
```

### Examples

```java
// Simple package name
package utilities;

// Multi-level package name (most common)
package com.example.myapp;

// More specific package
package com.example.myapp.model;
package com.example.myapp.service;
package com.example.myapp.controller;
```

### Package Naming Conventions

| Rule | Example | Explanation |
|------|---------|-------------|
| All lowercase | `com.example` | Never use uppercase |
| Reverse domain name | `com.google.gson` | Start with reversed company domain |
| No Java keywords | `com.example.myclass` | Avoid `int`, `class`, `static`, etc. |
| Underscores for special chars | `com.example.my_app` | If domain has hyphens |
| Avoid starting with `java` or `javax` | Use your own prefix | Reserved for Java SE |

**Common package structures:**

```java
// Company domain reversed + project + module
com.companyname.projectname.module

// Examples:
com.google.gson                    // Google's JSON library
org.apache.commons.lang3           // Apache Commons Lang
com.mycompany.shop.model           // Your shop app's model classes
com.mycompany.shop.service         // Your shop app's service classes
com.mycompany.shop.repository      // Your shop app's data access
com.mycompany.shop.controller      // Your shop app's controllers
com.mycompany.shop.util            // Your shop app's utilities
```

---

## The Default Package

If you do not include a `package` statement, your class belongs to the **default package** (also called the unnamed package).

```java
// No package statement - this class is in the default package
public class SimpleClass {
    public static void main(String[] args) {
        System.out.println("I'm in the default package");
    }
}
```

**Why you should avoid the default package:**

| Issue | Explanation |
|-------|-------------|
| Cannot be imported | Other packages cannot import classes from the default package |
| No organization | All classes pile up together |
| Unprofessional | Real projects always use packages |
| IDE issues | Some tools have problems with default package |

**In plain words:** The default package is fine for quick experiments, but always use a proper package for real code.

---

## Directory Structure

Java requires that the directory structure matches the package name. Each dot (`.`) in the package name represents a subdirectory.

### Package to Directory Mapping

```
Package name:        com.example.myapp.model
Directory structure: com/example/myapp/model/
File location:       com/example/myapp/model/User.java
```

### Complete Example

```
project/
├── src/
│   └── com/
│       └── example/
│           └── shop/
│               ├── model/
│               │   ├── Product.java      // package com.example.shop.model;
│               │   ├── Customer.java     // package com.example.shop.model;
│               │   └── Order.java        // package com.example.shop.model;
│               ├── service/
│               │   ├── ProductService.java   // package com.example.shop.service;
│               │   └── OrderService.java     // package com.example.shop.service;
│               └── Main.java             // package com.example.shop;
└── out/
    └── (compiled .class files mirror the same structure)
```

**Each file must declare its package:**

```java
// File: src/com/example/shop/model/Product.java
package com.example.shop.model;

public class Product {
    private String name;
    private double price;
    // ...
}
```

```java
// File: src/com/example/shop/service/ProductService.java
package com.example.shop.service;

import com.example.shop.model.Product;  // Import from another package

public class ProductService {
    public Product findById(int id) {
        // ...
    }
}
```

---

## Import Statements

When you want to use a class from another package, you have two options:

1. Use the fully qualified name every time
2. Import the class and use its simple name

### Without Import (Fully Qualified Name)

```java
package com.example.app;

public class Main {
    public static void main(String[] args) {
        // Using fully qualified names - verbose!
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.util.Scanner scanner = new java.util.Scanner(System.in);
    }
}
```

This works but is tedious when you use the same class multiple times.

### With Import (Recommended)

```java
package com.example.app;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Using simple names - clean!
        ArrayList<String> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Scanner scanner = new Scanner(System.in);
    }
}
```

### Import Syntax

```java
import packagename.ClassName;  // Import a specific class
```

**Order of statements in a Java file:**

```java
// 1. Package declaration (optional, at most one)
package com.example.myapp;

// 2. Import statements (optional, any number)
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

// 3. Class/interface/enum declaration(s)
public class MyClass {
    // ...
}
```

---

## Wildcard Imports

You can import all classes from a package using the asterisk (`*`) wildcard.

### Syntax

```java
import packagename.*;  // Import all classes from the package
```

### Example

```java
package com.example.app;

import java.util.*;  // Import all classes from java.util

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        Scanner scanner = new Scanner(System.in);
    }
}
```

### Specific vs Wildcard Imports

| Aspect | Specific Import | Wildcard Import |
|--------|-----------------|-----------------|
| Syntax | `import java.util.List;` | `import java.util.*;` |
| Clarity | Clear which classes are used | Less clear |
| Name conflicts | Easier to resolve | May cause ambiguity |
| Compile time | Slightly faster | Slightly slower |
| Best for | Production code | Quick prototyping |

**Important notes about wildcard imports:**

```java
import java.util.*;     // Imports classes from java.util
                        // Does NOT import subpackages!

// This does NOT work:
import java.*;          // Does NOT import java.util, java.time, etc.

// You must import subpackages separately:
import java.util.*;
import java.time.*;
import java.io.*;
```

### Resolving Name Conflicts

When two packages have classes with the same name, you must be explicit:

```java
import java.util.Date;   // Import one
import java.sql.*;       // Wildcard for the other

public class Example {
    public static void main(String[] args) {
        Date utilDate = new Date();          // Uses java.util.Date (imported)
        java.sql.Date sqlDate = new java.sql.Date(0);  // Fully qualified
    }
}
```

Or use fully qualified names for both:

```java
public class Example {
    public static void main(String[] args) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(0);
    }
}
```

---

## Static Imports

Static imports allow you to use static members (fields and methods) of a class without qualifying them with the class name.

### Without Static Import

```java
public class MathExample {
    public static void main(String[] args) {
        double result = Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2));
        double angle = Math.PI / 4;
        double sine = Math.sin(angle);
        System.out.println("Hypotenuse: " + result);  // 5.0
    }
}
```

### With Static Import

```java
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class MathExample {
    public static void main(String[] args) {
        double result = sqrt(pow(3, 2) + pow(4, 2));  // No "Math." prefix!
        double angle = PI / 4;
        double sine = sin(angle);
        System.out.println("Hypotenuse: " + result);  // 5.0
    }
}
```

### Static Wildcard Import

```java
import static java.lang.Math.*;  // Import ALL static members from Math

public class MathExample {
    public static void main(String[] args) {
        double result = sqrt(pow(3, 2) + pow(4, 2));
        double radians = toRadians(45);
        double ceiling = ceil(3.2);
        int maximum = max(10, 20);
    }
}
```

### Common Static Imports

```java
// System.out for cleaner print statements
import static java.lang.System.out;

out.println("Hello!");  // Instead of System.out.println

// JUnit assertions
import static org.junit.jupiter.api.Assertions.*;

assertEquals(5, result);  // Instead of Assertions.assertEquals

// Collections utility methods
import static java.util.Collections.*;

List<Integer> numbers = new ArrayList<>();
sort(numbers);        // Instead of Collections.sort
reverse(numbers);     // Instead of Collections.reverse
```

### When to Use Static Imports

| Use Case | Recommendation |
|----------|----------------|
| Math calculations | Good - cleaner formulas |
| Test assertions | Good - standard practice |
| Utility constants | Good - like `PI`, `MAX_VALUE` |
| Overusing in business code | Bad - reduces readability |
| When method origin is unclear | Bad - confuses readers |

**Best practice:** Use static imports sparingly. They are most helpful for math and testing.

---

## The java.lang Package

The `java.lang` package is special: it is automatically imported into every Java file. You never need to write `import java.lang.*;`.

### Classes in java.lang (Always Available)

| Class | Purpose | Example |
|-------|---------|---------|
| `String` | Text strings | `String s = "hello";` |
| `System` | System utilities | `System.out.println()` |
| `Math` | Mathematical operations | `Math.sqrt(16)` |
| `Object` | Root of class hierarchy | All classes extend this |
| `Integer`, `Double`, etc. | Wrapper classes | `Integer.parseInt("42")` |
| `Boolean` | Boolean wrapper | `Boolean.TRUE` |
| `StringBuilder` | Mutable strings | `new StringBuilder()` |
| `Exception`, `RuntimeException` | Exception classes | `throw new Exception()` |
| `Thread` | Threading | `Thread.sleep(1000)` |
| `Class` | Runtime class info | `obj.getClass()` |
| `Enum` | Enum base class | All enums extend this |

```java
// These work without any import statement:
public class Example {
    public static void main(String[] args) {
        String text = "Hello";           // java.lang.String
        int length = text.length();
        
        double root = Math.sqrt(16);     // java.lang.Math
        
        System.out.println(root);        // java.lang.System
        
        Integer num = Integer.valueOf(42);  // java.lang.Integer
    }
}
```

---

## Common Java Packages

Here are the most commonly used packages in Java:

### Core Packages

| Package | Purpose | Key Classes |
|---------|---------|-------------|
| `java.lang` | Language fundamentals (auto-imported) | `String`, `Math`, `System`, `Object` |
| `java.util` | Collections and utilities | `List`, `Map`, `Set`, `ArrayList`, `HashMap` |
| `java.io` | Input/output operations | `File`, `InputStream`, `OutputStream`, `Reader` |
| `java.nio` | New I/O (buffers, channels) | `Path`, `Files`, `ByteBuffer` |
| `java.time` | Date and time (Java 8+) | `LocalDate`, `LocalTime`, `ZonedDateTime` |
| `java.math` | Arbitrary precision math | `BigDecimal`, `BigInteger` |

### Additional Common Packages

| Package | Purpose | Key Classes |
|---------|---------|-------------|
| `java.util.function` | Functional interfaces | `Predicate`, `Function`, `Consumer` |
| `java.util.stream` | Stream API | `Stream`, `Collectors` |
| `java.util.concurrent` | Concurrency utilities | `ExecutorService`, `Future`, `Lock` |
| `java.util.regex` | Regular expressions | `Pattern`, `Matcher` |
| `java.net` | Networking | `URL`, `HttpURLConnection`, `Socket` |
| `java.sql` | Database access | `Connection`, `Statement`, `ResultSet` |
| `java.text` | Text formatting | `NumberFormat`, `DateFormat` |

### Example: Using Multiple Packages

```java
package com.example.app;

// Collections
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// Date/Time
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// I/O
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws IOException {
        // Using collections
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        
        Map<String, Integer> ages = new HashMap<>();
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        
        // Using date/time
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        // Using file I/O
        Path path = Path.of("example.txt");
        Files.writeString(path, "Hello, World!");
        String content = Files.readString(path);
        
        System.out.println("Names: " + names);
        System.out.println("Today: " + today);
        System.out.println("File content: " + content);
    }
}
```

---

## Access Modifiers and Packages

Access modifiers control visibility of classes, methods, and fields. Two of them are directly related to packages.

### Access Modifier Summary

| Modifier | Same Class | Same Package | Subclass (Different Package) | Anywhere |
|----------|------------|--------------|------------------------------|----------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (no modifier) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

### Package-Private (Default) Access

When you do not specify an access modifier, the member is **package-private** (also called default access). It is accessible only within the same package.

```java
// File: com/example/internal/Helper.java
package com.example.internal;

class Helper {  // Package-private class (no public modifier)
    
    int value = 10;  // Package-private field
    
    void doSomething() {  // Package-private method
        System.out.println("Helper doing something");
    }
}
```

```java
// File: com/example/internal/Service.java
package com.example.internal;  // Same package

public class Service {
    public void process() {
        Helper helper = new Helper();  // OK - same package
        helper.doSomething();          // OK - same package
        System.out.println(helper.value);  // OK - same package
    }
}
```

```java
// File: com/example/app/Main.java
package com.example.app;  // Different package

import com.example.internal.Helper;  // Error! Helper is not public

public class Main {
    public static void main(String[] args) {
        // Cannot access Helper from different package
    }
}
```

### Protected Access Across Packages

Protected members are accessible in subclasses, even in different packages.

```java
// File: com/example/base/Animal.java
package com.example.base;

public class Animal {
    protected String name;  // Protected field
    
    protected void speak() {  // Protected method
        System.out.println(name + " makes a sound");
    }
}
```

```java
// File: com/example/animals/Dog.java
package com.example.animals;  // Different package

import com.example.base.Animal;

public class Dog extends Animal {  // Subclass
    public void bark() {
        name = "Buddy";  // OK - protected, accessed in subclass
        speak();         // OK - protected, accessed in subclass
        System.out.println(name + " barks!");
    }
}
```

```java
// File: com/example/app/Main.java
package com.example.app;

import com.example.base.Animal;

public class Main {
    public static void main(String[] args) {
        Animal animal = new Animal();
        // animal.name = "Test";  // Error! Not a subclass
        // animal.speak();        // Error! Not a subclass
    }
}
```

### Public Classes and Files

Java has a rule: a `public` class must be in a file with the same name.

```java
// File MUST be named "Product.java"
package com.example.shop;

public class Product {
    // ...
}

// You can have non-public classes in the same file
class ProductHelper {  // Package-private, doesn't need its own file
    // ...
}
```

### Package Design Best Practices

```java
// Expose public API
package com.example.shop;

public class ShopService {  // Public - part of API
    public void placeOrder(Order order) {
        OrderValidator validator = new OrderValidator();  // Package-private
        if (validator.isValid(order)) {
            OrderProcessor processor = new OrderProcessor();  // Package-private
            processor.process(order);
        }
    }
}

// Hide implementation details
class OrderValidator {  // Package-private - internal use only
    boolean isValid(Order order) {
        // Validation logic
        return true;
    }
}

class OrderProcessor {  // Package-private - internal use only
    void process(Order order) {
        // Processing logic
    }
}
```

---

## Organizing a Real Project

Here is how packages are typically organized in a real Java project:

### Standard Project Structure

```
my-application/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── mycompany/
│                   └── myapp/
│                       ├── Application.java          // Main entry point
│                       ├── config/
│                       │   └── AppConfig.java        // Configuration
│                       ├── model/
│                       │   ├── User.java             // Domain objects
│                       │   ├── Product.java
│                       │   └── Order.java
│                       ├── repository/
│                       │   ├── UserRepository.java   // Data access
│                       │   └── ProductRepository.java
│                       ├── service/
│                       │   ├── UserService.java      // Business logic
│                       │   └── ProductService.java
│                       ├── controller/
│                       │   ├── UserController.java   // HTTP handlers
│                       │   └── ProductController.java
│                       ├── dto/
│                       │   ├── UserDTO.java          // Data transfer objects
│                       │   └── ProductDTO.java
│                       ├── exception/
│                       │   ├── NotFoundException.java // Custom exceptions
│                       │   └── ValidationException.java
│                       └── util/
│                           ├── StringUtils.java      // Utilities
│                           └── DateUtils.java
└── src/
    └── test/
        └── java/
            └── com/
                └── mycompany/
                    └── myapp/
                        └── service/
                            └── UserServiceTest.java  // Tests mirror main
```

### Package Responsibilities

| Package | Purpose | Examples |
|---------|---------|----------|
| `model` (or `domain`, `entity`) | Business objects | `User`, `Product`, `Order` |
| `repository` (or `dao`) | Data access | `UserRepository`, `ProductRepository` |
| `service` | Business logic | `UserService`, `OrderService` |
| `controller` (or `web`, `api`) | HTTP/API handlers | `UserController` |
| `dto` | Data transfer objects | `UserDTO`, `CreateUserRequest` |
| `exception` | Custom exceptions | `NotFoundException` |
| `config` | Configuration classes | `AppConfig`, `DatabaseConfig` |
| `util` (or `helper`, `common`) | Utility classes | `StringUtils`, `DateUtils` |

### Example: Model Package

```java
// File: com/mycompany/myapp/model/User.java
package com.mycompany.myapp.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    
    public User() {}
    
    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', name='" + name + "'}";
    }
}
```

### Example: Service Package

```java
// File: com/mycompany/myapp/service/UserService.java
package com.mycompany.myapp.service;

import com.mycompany.myapp.model.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
    
    public List<User> findAll() {
        return repository.findAll();
    }
    
    public User create(String email, String name) {
        User user = new User(email, name);
        return repository.save(user);
    }
}
```

---

## Java Module System (Java 9+)

Java 9 introduced the **Java Platform Module System (JPMS)**. Modules are a higher level of organization above packages.

### What Are Modules?

A module is a collection of packages with:
- A unique name
- Explicit declarations of what it exports (makes public to other modules)
- Explicit declarations of what it requires (dependencies on other modules)

### Module Declaration

Modules are defined in a file called `module-info.java` at the root of the source directory.

```java
// File: module-info.java
module com.mycompany.myapp {
    // Export packages for other modules to use
    exports com.mycompany.myapp.api;
    exports com.mycompany.myapp.model;
    
    // Require other modules
    requires java.sql;
    requires java.logging;
    
    // Require with transitive (dependencies get this too)
    requires transitive java.desktop;
}
```

### Module Keywords

| Keyword | Purpose | Example |
|---------|---------|---------|
| `module` | Declare a module | `module com.example {}` |
| `exports` | Make package available to other modules | `exports com.example.api;` |
| `requires` | Declare dependency on another module | `requires java.sql;` |
| `requires transitive` | Dependency inherited by dependent modules | `requires transitive java.base;` |
| `opens` | Allow reflection access | `opens com.example.internal;` |
| `provides...with` | Provide service implementation | `provides Service with Impl;` |
| `uses` | Consume a service | `uses com.example.Service;` |

### Basic Module Example

```
my-module/
├── src/
│   ├── module-info.java
│   └── com/
│       └── example/
│           ├── api/
│           │   └── Calculator.java
│           └── internal/
│               └── CalculatorImpl.java
```

```java
// module-info.java
module com.example.calculator {
    exports com.example.api;  // Public API
    // internal package is NOT exported - hidden from other modules
}
```

```java
// com/example/api/Calculator.java
package com.example.api;

import com.example.internal.CalculatorImpl;

public interface Calculator {
    int add(int a, int b);
    
    static Calculator create() {
        return new CalculatorImpl();
    }
}
```

```java
// com/example/internal/CalculatorImpl.java
package com.example.internal;

import com.example.api.Calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
```

### When to Use Modules

| Situation | Use Modules? |
|-----------|--------------|
| Small personal projects | Usually not necessary |
| Learning Java | Start without modules |
| Large enterprise applications | Recommended |
| Creating libraries for others | Recommended |
| Existing projects | Migrate gradually |

**In plain words:** Modules are optional for most learning and small projects. Focus on packages first. When you work on larger applications or create libraries, modules become valuable.

---

## Common Mistakes

### Mistake 1: Forgetting the Package Statement

```java
// Bad - in default package
public class MyClass {
    // Can't be imported by other packages
}

// Good - proper package
package com.example.myapp;

public class MyClass {
    // Can be imported anywhere
}
```

### Mistake 2: Package Name Doesn't Match Directory

```java
// File is in: src/com/example/app/Service.java
// But declares:
package com.example.service;  // Wrong! Should be com.example.app

// Correct:
package com.example.app;
```

### Mistake 3: Importing Classes from Same Package

```java
package com.example.app;

import com.example.app.Helper;  // Unnecessary! Same package

public class Service {
    private Helper helper;  // Works without import
}
```

### Mistake 4: Wildcard Import Confusion

```java
import java.util.*;    // Imports java.util classes
import java.util.stream.*;  // Must import subpackages separately!

// Wrong assumption:
import java.*;  // Does NOT import java.util, java.io, etc.
```

### Mistake 5: Exposing Too Much

```java
// Bad - everything is public
public class OrderService {
    public OrderValidator validator;  // Should be private
    
    public boolean validateOrder(Order order) {  // OK
        return validator.validate(order);
    }
}

// Good - hide implementation details
public class OrderService {
    private final OrderValidator validator;  // Hidden
    
    public boolean validateOrder(Order order) {  // Public API
        return validator.validate(order);
    }
}
```

---

## Cheat Sheet

### Quick Reference Table

| Task | Syntax | Example |
|------|--------|---------|
| Declare package | `package name;` | `package com.example.app;` |
| Import class | `import package.Class;` | `import java.util.List;` |
| Wildcard import | `import package.*;` | `import java.util.*;` |
| Static import | `import static package.Class.member;` | `import static java.lang.Math.PI;` |
| Static wildcard | `import static package.Class.*;` | `import static java.lang.Math.*;` |

### File Order

```java
// 1. Package (optional, one only)
package com.example.app;

// 2. Imports (optional, many allowed)
import java.util.List;
import java.time.LocalDate;
import static java.lang.Math.sqrt;

// 3. Class definition
public class MyClass {
    // ...
}
```

### Access Modifiers Quick Reference

| Modifier | Syntax | Visible To |
|----------|--------|------------|
| public | `public class X {}` | Everyone |
| protected | `protected void m() {}` | Same package + subclasses |
| (default) | `void m() {}` | Same package only |
| private | `private int x;` | Same class only |

### Common Packages to Know

| Package | Purpose |
|---------|---------|
| `java.lang` | Core classes (auto-imported) |
| `java.util` | Collections, utilities |
| `java.time` | Date and time |
| `java.io` | Input/output |
| `java.nio.file` | File operations |
| `java.util.stream` | Stream API |
| `java.util.function` | Functional interfaces |
| `java.math` | BigDecimal, BigInteger |

---

## Summary

- **Packages** organize classes into groups, like folders for files
- **Package declaration** must be the first statement: `package com.example.app;`
- **Directory structure** must match the package name
- **Import statements** let you use classes without typing full names
- **Wildcard imports** (`import java.util.*;`) import all classes in a package
- **Static imports** let you use static members without the class name
- **java.lang** is automatically imported (String, Math, System, etc.)
- **Package-private** (no modifier) restricts access to the same package
- **Modules** (Java 9+) are optional higher-level organization for large projects
- Always use meaningful package names based on your organization/project

---

[Back to Guide](../guide.md)
