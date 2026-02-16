# Exception Handling

[<- Previous: Inner Classes](15-inner-classes.md) | [Next: Collections - Lists ->](17-collections-lists.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Exception Handling Cheat Sheet](../cheatsheets/exception-handling-cheatsheet.md)

---

## Overview

An **exception** is an event that occurs during the execution of a program that disrupts the normal flow of instructions. When something unexpected happens (like dividing by zero, accessing a file that does not exist, or running out of memory), Java creates an exception object and "throws" it.

**In plain words:** An exception is Java's way of saying "something went wrong" and giving you a chance to handle the problem gracefully instead of crashing.

**Why Exception Handling Exists:**
- Programs encounter unexpected situations (bad input, missing files, network failures)
- Without exceptions, errors would cause crashes with no explanation
- Exception handling lets you detect problems and respond appropriately
- You can provide meaningful error messages to users
- You can recover from errors when possible
- You can clean up resources (close files, release connections) even when errors occur

---

## What Happens Without Exception Handling

```java
public class NoHandling {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3};
        System.out.println(numbers[10]);  // Index out of bounds!
        System.out.println("This line never executes");
    }
}
```

**Output:**
```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 3
    at NoHandling.main(NoHandling.java:4)
```

The program crashes immediately. The last print statement never runs.

**With exception handling:**

```java
public class WithHandling {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3};
        
        try {
            System.out.println(numbers[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid array index");
        }
        
        System.out.println("Program continues normally");
    }
}
```

**Output:**
```
Error: Invalid array index
Program continues normally
```

The program handles the error gracefully and continues executing.

---

## The Exception Hierarchy

All exceptions in Java inherit from `Throwable`. Understanding this hierarchy is essential.

```
java.lang.Object
    └── java.lang.Throwable
            ├── java.lang.Error                 (serious problems, don't catch)
            │       ├── OutOfMemoryError
            │       ├── StackOverflowError
            │       └── VirtualMachineError
            │
            └── java.lang.Exception             (problems you can handle)
                    ├── IOException
                    ├── SQLException
                    ├── ClassNotFoundException
                    │
                    └── java.lang.RuntimeException    (programming bugs)
                            ├── NullPointerException
                            ├── ArrayIndexOutOfBoundsException
                            ├── ArithmeticException
                            ├── IllegalArgumentException
                            ├── IllegalStateException
                            └── ClassCastException
```

### Key Classes Explained

| Class | Description | Should You Catch It? |
|-------|-------------|---------------------|
| `Throwable` | Root of all errors and exceptions | Rarely |
| `Error` | Serious JVM problems | No (usually unrecoverable) |
| `Exception` | Problems that can be handled | Yes |
| `RuntimeException` | Programming bugs, unchecked | Sometimes |

### Errors vs Exceptions

```java
// Error - serious JVM problem (don't catch)
public void causeStackOverflow() {
    causeStackOverflow();  // Infinite recursion
}
// Results in: StackOverflowError

// Exception - handleable problem (do catch)
public void readFile() {
    try {
        Files.readString(Path.of("missing.txt"));
    } catch (IOException e) {
        System.out.println("File not found, using defaults");
    }
}
```

---

## Checked vs Unchecked Exceptions

This is one of the most important concepts in Java exception handling.

### Checked Exceptions

Checked exceptions are exceptions that the compiler forces you to handle. They represent problems that are outside your control but are foreseeable (file not found, network failure, database error).

**You MUST either:**
1. Catch the exception with try-catch, OR
2. Declare it with `throws` in your method signature

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckedExample {
    
    // Option 1: Handle with try-catch
    public String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }
    
    // Option 2: Declare with throws (caller must handle)
    public String readFileOrThrow(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
```

**Common checked exceptions:**
- `IOException` - file/network I/O problems
- `SQLException` - database errors
- `ClassNotFoundException` - class loading failures
- `InterruptedException` - thread interruption
- `ParseException` - parsing failures

### Unchecked Exceptions (Runtime Exceptions)

Unchecked exceptions extend `RuntimeException`. The compiler does not force you to handle them. They usually indicate programming bugs that should be fixed in code.

```java
public class UncheckedExample {
    
    public void demonstrateUnchecked() {
        // NullPointerException - calling method on null
        String text = null;
        // text.length();  // Would throw NullPointerException
        
        // ArrayIndexOutOfBoundsException - bad index
        int[] arr = {1, 2, 3};
        // int x = arr[10];  // Would throw ArrayIndexOutOfBoundsException
        
        // ArithmeticException - division by zero
        // int y = 10 / 0;  // Would throw ArithmeticException
        
        // IllegalArgumentException - invalid argument
        // Thread.sleep(-1);  // Would throw IllegalArgumentException
    }
    
    // You CAN catch them if you want
    public int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
            return 0;
        }
    }
}
```

**Common unchecked exceptions:**
- `NullPointerException` - calling methods on null
- `ArrayIndexOutOfBoundsException` - invalid array index
- `ArithmeticException` - illegal math operations
- `IllegalArgumentException` - invalid method argument
- `IllegalStateException` - invalid object state
- `ClassCastException` - invalid type cast
- `NumberFormatException` - invalid string to number conversion

### Comparison Table

| Aspect | Checked Exception | Unchecked Exception |
|--------|-------------------|---------------------|
| Extends | `Exception` (not RuntimeException) | `RuntimeException` |
| Compiler enforcement | Yes (must handle or declare) | No |
| Typical cause | External problems | Programming bugs |
| Examples | `IOException`, `SQLException` | `NullPointerException` |
| Recovery | Often possible | Fix the bug instead |

---

## The try-catch Block

The `try-catch` block is the fundamental mechanism for handling exceptions.

### Basic Syntax

```java
try {
    // Code that might throw an exception
} catch (ExceptionType e) {
    // Code to handle the exception
}
```

### Simple Example

```java
public class TryCatchBasic {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;  // This throws ArithmeticException
            System.out.println("Result: " + result);  // Never reached
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero!");
        }
        
        System.out.println("Program continues...");
    }
}
```

**Output:**
```
Cannot divide by zero!
Program continues...
```

### What the Exception Object Contains

The caught exception `e` provides useful information:

```java
try {
    int[] arr = new int[3];
    int x = arr[10];
} catch (ArrayIndexOutOfBoundsException e) {
    // getMessage() - short description
    System.out.println("Message: " + e.getMessage());
    // Output: Message: Index 10 out of bounds for length 3
    
    // toString() - exception type + message
    System.out.println("String: " + e.toString());
    // Output: String: java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 3
    
    // printStackTrace() - full stack trace (for debugging)
    e.printStackTrace();
    // Shows the complete call stack where the error occurred
    
    // getClass().getName() - exception type
    System.out.println("Type: " + e.getClass().getName());
    // Output: Type: java.lang.ArrayIndexOutOfBoundsException
}
```

### Practical Examples

**Example 1: Parsing user input**

```java
import java.util.Scanner;

public class ParseInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a number: ");
        String input = scanner.nextLine();
        
        try {
            int number = Integer.parseInt(input);
            System.out.println("You entered: " + number);
            System.out.println("Doubled: " + (number * 2));
        } catch (NumberFormatException e) {
            System.out.println("'" + input + "' is not a valid number");
        }
        
        scanner.close();
    }
}
```

**Example 2: Array access**

```java
public class SafeArrayAccess {
    
    public static int getElement(int[] array, int index) {
        try {
            return array[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index " + index + " is out of bounds");
            return -1;  // Return default value
        } catch (NullPointerException e) {
            System.out.println("Array is null");
            return -1;
        }
    }
    
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30};
        
        System.out.println(getElement(numbers, 1));   // 20
        System.out.println(getElement(numbers, 10));  // Index 10 is out of bounds, returns -1
        System.out.println(getElement(null, 0));      // Array is null, returns -1
    }
}
```

---

## Multiple catch Blocks

When code might throw different types of exceptions, you can handle each type differently with multiple catch blocks.

### Syntax

```java
try {
    // Code that might throw multiple exception types
} catch (ExceptionType1 e) {
    // Handle ExceptionType1
} catch (ExceptionType2 e) {
    // Handle ExceptionType2
} catch (ExceptionType3 e) {
    // Handle ExceptionType3
}
```

### Example

```java
import java.io.*;
import java.nio.file.*;

public class MultipleCatch {
    
    public static void processFile(String filename) {
        try {
            // Could throw IOException
            String content = Files.readString(Path.of(filename));
            
            // Could throw NumberFormatException
            int number = Integer.parseInt(content.trim());
            
            // Could throw ArithmeticException
            int result = 100 / number;
            
            System.out.println("Result: " + result);
            
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("File does not contain a valid number");
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
        }
    }
    
    public static void main(String[] args) {
        processFile("data.txt");
    }
}
```

### Order Matters: Specific Before General

Catch blocks are checked in order. More specific exceptions must come before more general ones.

```java
// WRONG - won't compile!
try {
    // code
} catch (Exception e) {           // Too general - catches everything
    // handle
} catch (IOException e) {         // Error! Already caught by Exception
    // never reached
}

// CORRECT - specific first
try {
    // code
} catch (FileNotFoundException e) {   // Most specific
    System.out.println("File not found");
} catch (IOException e) {             // More general
    System.out.println("I/O error");
} catch (Exception e) {               // Most general (catch-all)
    System.out.println("Unexpected error");
}
```

### Handling Related Exceptions

```java
public class ExceptionHierarchy {
    
    public static void readData(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            // Read data...
            fis.close();
        } catch (FileNotFoundException e) {
            // FileNotFoundException extends IOException
            System.out.println("File not found: " + path);
            // Could try a default file or create the file
        } catch (IOException e) {
            // Catches other IOExceptions (read errors, etc.)
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
```

---

## Multi-catch (Java 7+)

When you want to handle multiple exception types the same way, use the multi-catch syntax with the pipe operator (`|`).

### Syntax

```java
try {
    // code
} catch (ExceptionType1 | ExceptionType2 | ExceptionType3 e) {
    // Handle all three the same way
}
```

### Example

```java
public class MultiCatch {
    
    public static void processValue(String input, int index) {
        String[] data = {"10", "20", "abc", "40"};
        
        try {
            String value = data[index];
            int number = Integer.parseInt(value);
            int result = 100 / number;
            System.out.println("Result: " + result);
            
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | ArithmeticException e) {
            // Handle all three exceptions the same way
            System.out.println("Error processing value: " + e.getMessage());
            System.out.println("Exception type: " + e.getClass().getSimpleName());
        }
    }
    
    public static void main(String[] args) {
        processValue("", 0);   // Result: 10
        processValue("", 2);   // Error: NumberFormatException (can't parse "abc")
        processValue("", 10);  // Error: ArrayIndexOutOfBoundsException
    }
}
```

### Before and After Comparison

```java
// Before Java 7 - repetitive
try {
    // code
} catch (IOException e) {
    logger.error("Operation failed", e);
    throw new ServiceException("Operation failed", e);
} catch (SQLException e) {
    logger.error("Operation failed", e);
    throw new ServiceException("Operation failed", e);
} catch (ClassNotFoundException e) {
    logger.error("Operation failed", e);
    throw new ServiceException("Operation failed", e);
}

// Java 7+ multi-catch - cleaner
try {
    // code
} catch (IOException | SQLException | ClassNotFoundException e) {
    logger.error("Operation failed", e);
    throw new ServiceException("Operation failed", e);
}
```

### Rules for Multi-catch

1. Exception types cannot be related (subclass/superclass)
2. The exception variable `e` is implicitly final (you cannot reassign it)

```java
// WRONG - IOException is superclass of FileNotFoundException
catch (IOException | FileNotFoundException e) { }  // Compile error!

// WRONG - cannot reassign e
catch (IOException | SQLException e) {
    e = new IOException();  // Compile error! e is final
}
```

---

## The finally Block

The `finally` block contains code that always executes, whether an exception occurred or not. It is used for cleanup operations.

### Syntax

```java
try {
    // Code that might throw exception
} catch (ExceptionType e) {
    // Handle exception
} finally {
    // Always executes (cleanup code)
}
```

### Basic Example

```java
public class FinallyExample {
    public static void main(String[] args) {
        try {
            System.out.println("In try block");
            int result = 10 / 0;  // Exception here
            System.out.println("After division");  // Never executes
        } catch (ArithmeticException e) {
            System.out.println("In catch block");
        } finally {
            System.out.println("In finally block");  // Always executes
        }
        System.out.println("After try-catch-finally");
    }
}
```

**Output:**
```
In try block
In catch block
In finally block
After try-catch-finally
```

### Finally Always Executes

```java
public class FinallyAlways {
    
    public static String test(boolean throwException) {
        try {
            if (throwException) {
                throw new RuntimeException("Error!");
            }
            return "Normal return";
        } catch (RuntimeException e) {
            return "Exception caught";
        } finally {
            System.out.println("Finally executed");
            // Note: cleanup code here, avoid return in finally
        }
    }
    
    public static void main(String[] args) {
        System.out.println(test(false));
        // Output: Finally executed
        //         Normal return
        
        System.out.println("---");
        
        System.out.println(test(true));
        // Output: Finally executed
        //         Exception caught
    }
}
```

### Resource Cleanup Example

```java
import java.io.*;

public class FinallyCleanup {
    
    public static void readFile(String path) {
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(path);
            int data;
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
        } catch (IOException e) {
            System.out.println("Error reading file");
        } finally {
            // Always close the file, even if exception occurred
            if (fis != null) {
                try {
                    fis.close();
                    System.out.println("\nFile closed");
                } catch (IOException e) {
                    System.out.println("Error closing file");
                }
            }
        }
    }
}
```

### try-finally (Without catch)

You can use `finally` without `catch` when you want cleanup but want the exception to propagate.

```java
public class TryFinally {
    
    public static void processData() throws IOException {
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream("data.txt");
            // Process file...
            // If exception occurs, it propagates to caller
        } finally {
            // But cleanup still happens
            if (fis != null) {
                fis.close();
            }
        }
    }
}
```

### When Finally Does NOT Execute

The only cases where `finally` does not execute:

```java
// 1. JVM exits during try/catch
try {
    System.exit(0);  // JVM terminates
} finally {
    System.out.println("Never printed");
}

// 2. Thread is killed
// 3. Infinite loop in try/catch
// 4. Power failure / system crash
```

---

## try-with-resources (Java 7+)

The try-with-resources statement automatically closes resources when the try block exits. This is the preferred way to handle resources.

### The Problem It Solves

```java
// Old way - verbose and error-prone
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("file.txt"));
    String line = reader.readLine();
    System.out.println(line);
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### The Solution

```java
// New way - clean and safe
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
    System.out.println(line);
} catch (IOException e) {
    e.printStackTrace();
}
// reader is automatically closed, even if exception occurs
```

### Syntax

```java
try (ResourceType resource = new ResourceType()) {
    // Use resource
} catch (ExceptionType e) {
    // Handle exception
}
// Resource automatically closed here
```

### Multiple Resources

```java
import java.io.*;
import java.nio.file.*;

public class MultipleResources {
    
    public static void copyFile(String source, String dest) {
        try (
            BufferedReader reader = new BufferedReader(new FileReader(source));
            BufferedWriter writer = new BufferedWriter(new FileWriter(dest))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("File copied successfully");
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
        // Both reader and writer are closed automatically
        // Resources are closed in reverse order of creation
    }
}
```

### What Can Be Used in try-with-resources

Any class that implements `AutoCloseable` or `Closeable` can be used.

```java
// Common AutoCloseable resources:
// - InputStream, OutputStream
// - Reader, Writer
// - Connection (database)
// - Socket
// - Scanner

import java.util.Scanner;
import java.sql.*;

// Scanner example
try (Scanner scanner = new Scanner(System.in)) {
    String input = scanner.nextLine();
}

// Database example
try (Connection conn = DriverManager.getConnection(url);
     PreparedStatement stmt = conn.prepareStatement(sql);
     ResultSet rs = stmt.executeQuery()) {
    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
}
```

### Creating Your Own AutoCloseable

```java
public class MyResource implements AutoCloseable {
    private String name;
    
    public MyResource(String name) {
        this.name = name;
        System.out.println("Resource " + name + " opened");
    }
    
    public void doWork() {
        System.out.println("Resource " + name + " working");
    }
    
    @Override
    public void close() {
        System.out.println("Resource " + name + " closed");
    }
}

// Usage
public class CustomResourceExample {
    public static void main(String[] args) {
        try (MyResource r1 = new MyResource("A");
             MyResource r2 = new MyResource("B")) {
            r1.doWork();
            r2.doWork();
        }
        // Output:
        // Resource A opened
        // Resource B opened
        // Resource A working
        // Resource B working
        // Resource B closed  (reverse order)
        // Resource A closed
    }
}
```

### Effectively Final Variables (Java 9+)

In Java 9+, you can use effectively final variables in try-with-resources:

```java
// Java 9+
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
try (reader) {  // Use existing variable
    String line = reader.readLine();
    System.out.println(line);
}
```

---

## Throwing Exceptions

Use the `throw` keyword to throw an exception yourself.

### Basic Syntax

```java
throw new ExceptionType("Error message");
```

### Examples

```java
public class ThrowExample {
    
    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age is unrealistic: " + age);
        }
        System.out.println("Age is valid: " + age);
    }
    
    public static void main(String[] args) {
        validateAge(25);   // Age is valid: 25
        validateAge(-5);   // Throws IllegalArgumentException
    }
}
```

### Input Validation

```java
public class UserService {
    
    public void createUser(String username, String email, int age) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        
        // Validate email
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Validate age
        if (age < 13) {
            throw new IllegalArgumentException("User must be at least 13 years old");
        }
        
        // Create user...
        System.out.println("User created: " + username);
    }
}
```

### Rethrowing Exceptions

Sometimes you want to catch an exception, do something, then throw it again:

```java
public class RethrowExample {
    
    public static void process() throws IOException {
        try {
            // Some operation
            throw new IOException("Original error");
        } catch (IOException e) {
            System.out.println("Logging error: " + e.getMessage());
            throw e;  // Rethrow the same exception
        }
    }
    
    public static void processWithNewException() throws ServiceException {
        try {
            // Some operation
            throw new IOException("Original error");
        } catch (IOException e) {
            // Wrap in a different exception
            throw new ServiceException("Operation failed", e);
        }
    }
}
```

---

## The throws Clause

The `throws` clause declares that a method might throw certain checked exceptions. The caller must handle or propagate these exceptions.

### Syntax

```java
public returnType methodName(parameters) throws ExceptionType1, ExceptionType2 {
    // method body
}
```

### Example

```java
import java.io.*;
import java.nio.file.*;

public class ThrowsExample {
    
    // This method declares it might throw IOException
    public static String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
    
    // Caller must handle or propagate
    public static void main(String[] args) {
        // Option 1: Handle with try-catch
        try {
            String content = readFile("data.txt");
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }
    
    // Option 2: Propagate with throws
    public static void anotherMethod() throws IOException {
        String content = readFile("data.txt");
        System.out.println(content);
    }
}
```

### Multiple Exception Types

```java
import java.io.*;
import java.sql.*;

public class MultipleThrows {
    
    public void complexOperation() throws IOException, SQLException, ClassNotFoundException {
        // Load driver
        Class.forName("com.mysql.jdbc.Driver");
        
        // Connect to database
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
        
        // Read configuration file
        Files.readString(Path.of("config.txt"));
    }
}
```

### throws vs throw

| Keyword | Purpose | Location | Example |
|---------|---------|----------|---------|
| `throw` | Actually throw an exception | Inside method body | `throw new Exception("msg");` |
| `throws` | Declare what exceptions a method might throw | Method signature | `void m() throws Exception {}` |

```java
public class ThrowVsThrows {
    
    // throws: declares the exception in signature
    public void readData() throws IOException {
        // throw: actually throws the exception
        throw new IOException("No data available");
    }
}
```

---

## Custom Exceptions

Creating your own exception classes gives you meaningful, domain-specific error handling.

### When to Create Custom Exceptions

- When built-in exceptions don't describe your error well enough
- When you need to include additional information
- When you want to distinguish your application's errors from library errors
- When creating reusable libraries

### Basic Custom Exception

```java
// Checked exception (extends Exception)
public class InsufficientFundsException extends Exception {
    
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// Usage
public class BankAccount {
    private double balance;
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Cannot withdraw " + amount + ", balance is only " + balance
            );
        }
        balance -= amount;
    }
}
```

### Custom RuntimeException

```java
// Unchecked exception (extends RuntimeException)
public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
    
    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Usage - no throws clause needed
public class OrderService {
    
    public void validateOrder(Order order) {
        if (order == null) {
            throw new InvalidOrderException("Order cannot be null");
        }
        if (order.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must have at least one item");
        }
    }
}
```

### Full-Featured Custom Exception

```java
public class UserNotFoundException extends Exception {
    
    private final String userId;
    private final String searchCriteria;
    
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
        this.userId = userId;
        this.searchCriteria = "id";
    }
    
    public UserNotFoundException(String message, String userId, String searchCriteria) {
        super(message);
        this.userId = userId;
        this.searchCriteria = searchCriteria;
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.userId = null;
        this.searchCriteria = null;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getSearchCriteria() {
        return searchCriteria;
    }
}

// Usage
public class UserService {
    
    public User findById(String id) throws UserNotFoundException {
        User user = database.findById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }
    
    public User findByEmail(String email) throws UserNotFoundException {
        User user = database.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(
                "No user with email: " + email,
                email,
                "email"
            );
        }
        return user;
    }
}

// Handling with extra info
try {
    User user = userService.findById("12345");
} catch (UserNotFoundException e) {
    System.out.println("Could not find user: " + e.getUserId());
    System.out.println("Searched by: " + e.getSearchCriteria());
}
```

### Exception Hierarchy for Your Application

```java
// Base exception for your application
public class ShopException extends Exception {
    public ShopException(String message) {
        super(message);
    }
    public ShopException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Specific exceptions extend the base
public class ProductNotFoundException extends ShopException {
    private final String productId;
    
    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
        this.productId = productId;
    }
    
    public String getProductId() {
        return productId;
    }
}

public class InsufficientStockException extends ShopException {
    private final String productId;
    private final int requested;
    private final int available;
    
    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for " + productId + 
              ": requested " + requested + ", available " + available);
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }
    
    // Getters...
}

public class PaymentFailedException extends ShopException {
    private final String reason;
    
    public PaymentFailedException(String reason) {
        super("Payment failed: " + reason);
        this.reason = reason;
    }
}

// Now you can catch at different levels
try {
    orderService.placeOrder(order);
} catch (ProductNotFoundException e) {
    System.out.println("Product not available: " + e.getProductId());
} catch (InsufficientStockException e) {
    System.out.println("Not enough stock, only " + e.getAvailable() + " left");
} catch (ShopException e) {
    // Catch-all for any shop-related exception
    System.out.println("Order failed: " + e.getMessage());
}
```

---

## Exception Chaining (Nested Exceptions)

Exception chaining (also called nested exceptions) preserves the original exception as the cause of a new exception. This creates a chain of exceptions that helps trace the root cause of problems.

### Why Chain Exceptions?

- Preserve the root cause for debugging
- Convert low-level exceptions to high-level ones
- Add context while keeping original error information
- Create meaningful exception hierarchies in layered architectures

### Basic Chaining

```java
public class ExceptionChaining {
    
    public void loadConfiguration() throws ConfigurationException {
        try {
            String content = Files.readString(Path.of("config.json"));
            // Parse JSON...
        } catch (IOException e) {
            // Chain: IOException is the cause of ConfigurationException
            throw new ConfigurationException("Failed to load configuration", e);
        }
    }
}

// Custom exception with cause
public class ConfigurationException extends Exception {
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Accessing the Cause

```java
try {
    loadConfiguration();
} catch (ConfigurationException e) {
    System.out.println("Error: " + e.getMessage());
    
    // Get the original cause
    Throwable cause = e.getCause();
    if (cause != null) {
        System.out.println("Caused by: " + cause.getMessage());
    }
    
    // Print full stack trace (shows chain)
    e.printStackTrace();
}
```

**Output:**
```
Error: Failed to load configuration
Caused by: config.json (No such file or directory)

ConfigurationException: Failed to load configuration
    at Example.loadConfiguration(Example.java:10)
    at Example.main(Example.java:5)
Caused by: java.io.FileNotFoundException: config.json (No such file or directory)
    at java.io.FileInputStream.<init>(FileInputStream.java:123)
    ...
```

### Multi-Layer Chaining

```java
// Repository layer
public class UserRepository {
    public User findById(String id) throws DataAccessException {
        try {
            // Database query
            return executeQuery(id);
        } catch (SQLException e) {
            throw new DataAccessException("Database error finding user", e);
        }
    }
}

// Service layer
public class UserService {
    private UserRepository repository;
    
    public User getUser(String id) throws ServiceException {
        try {
            return repository.findById(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Could not retrieve user", e);
        }
    }
}

// Controller layer
public class UserController {
    private UserService service;
    
    public Response handleGetUser(String id) {
        try {
            User user = service.getUser(id);
            return Response.ok(user);
        } catch (ServiceException e) {
            // Log full chain for debugging
            logger.error("Request failed", e);
            
            // Return user-friendly message
            return Response.error("Unable to load user data");
        }
    }
}
```

### Finding the Root Cause

When exceptions are deeply nested, you need to traverse the chain to find the original problem.

```java
public class ExceptionUtils {
    
    /**
     * Finds the root cause of an exception chain.
     * Traverses the chain until it finds an exception with no cause.
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
    
    /**
     * Gets all exceptions in the chain as a list.
     * First element is the top-level exception, last is the root cause.
     */
    public static List<Throwable> getExceptionChain(Throwable throwable) {
        List<Throwable> chain = new ArrayList<>();
        Throwable current = throwable;
        while (current != null) {
            chain.add(current);
            current = current.getCause();
        }
        return chain;
    }
    
    /**
     * Checks if a specific exception type exists anywhere in the chain.
     */
    public static boolean containsExceptionType(Throwable throwable, 
                                                  Class<? extends Throwable> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
    
    /**
     * Finds the first exception of a specific type in the chain.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T findExceptionOfType(Throwable throwable,
                                                                Class<T> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return (T) current;
            }
            current = current.getCause();
        }
        return null;
    }
}

// Usage examples
try {
    service.processOrder(order);
} catch (ServiceException e) {
    // Get the root cause
    Throwable root = ExceptionUtils.getRootCause(e);
    System.out.println("Root cause: " + root.getClass().getSimpleName());
    System.out.println("Root message: " + root.getMessage());
    
    // Check if it was a database problem
    if (ExceptionUtils.containsExceptionType(e, SQLException.class)) {
        System.out.println("Database error occurred");
        // Maybe retry or use fallback
    }
    
    // Get the full chain for logging
    List<Throwable> chain = ExceptionUtils.getExceptionChain(e);
    System.out.println("Exception chain depth: " + chain.size());
    for (int i = 0; i < chain.size(); i++) {
        System.out.println("  Level " + i + ": " + chain.get(i).getClass().getSimpleName());
    }
}
```

### Printing the Exception Chain

```java
public static void printExceptionChain(Throwable throwable) {
    System.out.println("Exception Chain:");
    System.out.println("=================");
    
    Throwable current = throwable;
    int level = 0;
    
    while (current != null) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "Level " + level + ": " + current.getClass().getName());
        System.out.println(indent + "Message: " + current.getMessage());
        
        // Print first few stack trace elements
        StackTraceElement[] stack = current.getStackTrace();
        for (int i = 0; i < Math.min(3, stack.length); i++) {
            System.out.println(indent + "  at " + stack[i]);
        }
        if (stack.length > 3) {
            System.out.println(indent + "  ... " + (stack.length - 3) + " more");
        }
        
        current = current.getCause();
        level++;
        
        if (current != null) {
            System.out.println(indent + "Caused by:");
        }
    }
}

// Example output:
// Exception Chain:
// =================
// Level 0: com.example.ServiceException
// Message: Could not process order
//   at com.example.OrderService.process(OrderService.java:45)
//   at com.example.OrderController.handleOrder(OrderController.java:23)
//   at com.example.Main.main(Main.java:15)
//   ... 2 more
// Caused by:
//   Level 1: com.example.DataAccessException
//   Message: Database error
//     at com.example.OrderRepository.save(OrderRepository.java:78)
//     at com.example.OrderService.process(OrderService.java:42)
//     ... 3 more
//   Caused by:
//     Level 2: java.sql.SQLException
//     Message: Connection refused
//       at com.mysql.jdbc.ConnectionImpl.connect(ConnectionImpl.java:456)
//       ... 5 more
```

### Using initCause() Method

Sometimes you need to set the cause after creating the exception. Use `initCause()`:

```java
public class InitCauseExample {
    
    public void process() throws ProcessingException {
        try {
            // Some operation
        } catch (IOException e) {
            // Create exception first, then set cause
            ProcessingException pe = new ProcessingException("Processing failed");
            pe.initCause(e);  // Set the cause
            throw pe;
        }
    }
}

// Note: initCause() can only be called once!
// Calling it again throws IllegalStateException

try {
    Exception e = new Exception("Error");
    e.initCause(new IOException("First cause"));
    e.initCause(new SQLException("Second cause"));  // IllegalStateException!
} catch (IllegalStateException ex) {
    System.out.println("Can't set cause twice: " + ex.getMessage());
}
```

### Handling Nested Exceptions in Different Scenarios

```java
public class NestedExceptionHandling {
    
    // Scenario 1: Retry only for specific root causes
    public void processWithRetry(String data) throws ProcessingException {
        int attempts = 0;
        int maxAttempts = 3;
        
        while (attempts < maxAttempts) {
            try {
                doProcess(data);
                return;  // Success
            } catch (ProcessingException e) {
                attempts++;
                
                // Only retry if root cause is a transient error
                Throwable root = getRootCause(e);
                if (root instanceof SocketTimeoutException || 
                    root instanceof ConnectException) {
                    System.out.println("Transient error, retrying... (" + attempts + ")");
                    sleep(1000 * attempts);
                } else {
                    // Non-retryable error, fail immediately
                    throw e;
                }
            }
        }
        throw new ProcessingException("Failed after " + maxAttempts + " attempts");
    }
    
    // Scenario 2: Convert nested exception to user-friendly message
    public String getUserFriendlyMessage(Throwable e) {
        // Check the chain for known error types
        if (containsExceptionType(e, FileNotFoundException.class)) {
            return "The requested file could not be found.";
        }
        if (containsExceptionType(e, SQLException.class)) {
            return "A database error occurred. Please try again later.";
        }
        if (containsExceptionType(e, SocketTimeoutException.class)) {
            return "The server is not responding. Please check your connection.";
        }
        if (containsExceptionType(e, AuthenticationException.class)) {
            return "Authentication failed. Please log in again.";
        }
        
        // Default message
        return "An unexpected error occurred: " + e.getMessage();
    }
    
    // Scenario 3: Log with full context
    public void logWithContext(String operation, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Operation '").append(operation).append("' failed\n");
        sb.append("Exception chain:\n");
        
        Throwable current = e;
        int level = 0;
        while (current != null) {
            sb.append(String.format("  [%d] %s: %s%n", 
                level, 
                current.getClass().getSimpleName(), 
                current.getMessage()));
            current = current.getCause();
            level++;
        }
        
        sb.append("Root cause: ").append(getRootCause(e).getClass().getName());
        
        logger.error(sb.toString(), e);  // Include full stack trace
    }
    
    private Throwable getRootCause(Throwable t) {
        while (t.getCause() != null) t = t.getCause();
        return t;
    }
    
    private boolean containsExceptionType(Throwable t, Class<?> type) {
        while (t != null) {
            if (type.isInstance(t)) return true;
            t = t.getCause();
        }
        return false;
    }
}
```

---

## Stack Trace Manipulation

The stack trace shows the path of method calls that led to the exception. Java provides methods to access and manipulate stack traces.

### Understanding Stack Traces

```java
public class StackTraceExample {
    
    public static void main(String[] args) {
        try {
            methodA();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static void methodA() throws Exception {
        methodB();
    }
    
    static void methodB() throws Exception {
        methodC();
    }
    
    static void methodC() throws Exception {
        throw new Exception("Error in methodC");
    }
}

// Output:
// java.lang.Exception: Error in methodC
//     at StackTraceExample.methodC(StackTraceExample.java:20)   <- where thrown
//     at StackTraceExample.methodB(StackTraceExample.java:16)
//     at StackTraceExample.methodA(StackTraceExample.java:12)
//     at StackTraceExample.main(StackTraceExample.java:6)       <- entry point
```

### Accessing Stack Trace Elements

```java
public class StackTraceAccess {
    
    public static void analyzeException() {
        try {
            throw new RuntimeException("Test error");
        } catch (RuntimeException e) {
            // Get the stack trace as an array
            StackTraceElement[] stackTrace = e.getStackTrace();
            
            System.out.println("Stack trace has " + stackTrace.length + " elements");
            System.out.println();
            
            for (int i = 0; i < stackTrace.length; i++) {
                StackTraceElement element = stackTrace[i];
                
                System.out.println("Frame " + i + ":");
                System.out.println("  Class: " + element.getClassName());
                System.out.println("  Method: " + element.getMethodName());
                System.out.println("  File: " + element.getFileName());
                System.out.println("  Line: " + element.getLineNumber());
                System.out.println("  Native: " + element.isNativeMethod());
                System.out.println();
            }
        }
    }
    
    // Get just the immediate caller
    public static String getCallerInfo() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // stack[0] = getStackTrace
        // stack[1] = getCallerInfo (this method)
        // stack[2] = the actual caller
        if (stack.length > 2) {
            StackTraceElement caller = stack[2];
            return caller.getClassName() + "." + caller.getMethodName() + 
                   "(" + caller.getFileName() + ":" + caller.getLineNumber() + ")";
        }
        return "Unknown caller";
    }
}
```

### Creating Exceptions Without Stack Trace (Performance)

Creating exceptions is expensive because of stack trace generation. For high-performance scenarios, you can skip it:

```java
public class HighPerformanceException extends RuntimeException {
    
    public HighPerformanceException(String message) {
        super(message, null, true, false);  // Last param: writableStackTrace = false
    }
    
    // Alternative: override fillInStackTrace
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // Don't fill in stack trace
    }
}

// Use case: Control flow exceptions (not recommended, but sometimes used)
public class StopProcessingException extends RuntimeException {
    public static final StopProcessingException INSTANCE = new StopProcessingException();
    
    private StopProcessingException() {
        super(null, null, false, false);  // No message, no cause, no stack trace
    }
}
```

### Modifying Stack Traces

```java
public class ModifyStackTrace {
    
    // Filter out framework classes from stack trace
    public static void filterStackTrace(Throwable t, String... packagesToKeep) {
        StackTraceElement[] original = t.getStackTrace();
        List<StackTraceElement> filtered = new ArrayList<>();
        
        for (StackTraceElement element : original) {
            for (String pkg : packagesToKeep) {
                if (element.getClassName().startsWith(pkg)) {
                    filtered.add(element);
                    break;
                }
            }
        }
        
        t.setStackTrace(filtered.toArray(new StackTraceElement[0]));
    }
    
    // Add context to exception
    public static RuntimeException withContext(RuntimeException e, String context) {
        RuntimeException wrapped = new RuntimeException(context + ": " + e.getMessage());
        wrapped.setStackTrace(e.getStackTrace());  // Preserve original stack trace
        wrapped.initCause(e.getCause());  // Preserve original cause if any
        return wrapped;
    }
}

// Usage
try {
    processData();
} catch (RuntimeException e) {
    ModifyStackTrace.filterStackTrace(e, "com.mycompany");
    throw e;  // Stack trace now only shows your code
}
```

### Refreshing Stack Trace with fillInStackTrace()

```java
public class FillInStackTraceExample {
    
    public static void main(String[] args) {
        try {
            methodA();
        } catch (Exception e) {
            System.out.println("Original stack trace:");
            e.printStackTrace();
            
            System.out.println("\nAfter fillInStackTrace:");
            e.fillInStackTrace();  // Refreshes stack trace from current location
            e.printStackTrace();
        }
    }
    
    static void methodA() throws Exception {
        methodB();
    }
    
    static void methodB() throws Exception {
        throw new Exception("Error");
    }
}

// Original shows: main -> methodA -> methodB (where thrown)
// After fillInStackTrace: main (where fillInStackTrace was called)
```

---

## Handling InterruptedException

`InterruptedException` is special and requires careful handling. It signals that a thread should stop what it's doing.

### The Wrong Way

```java
// BAD - swallows the interruption
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Do nothing - WRONG!
}

// BAD - wraps without preserving interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    throw new RuntimeException(e);  // Interrupt status is lost!
}
```

### The Right Way

```java
// Option 1: Propagate the exception
public void doWork() throws InterruptedException {
    Thread.sleep(1000);  // Let it propagate naturally
}

// Option 2: Restore the interrupt status, then handle
public void doWorkSafely() {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        // Restore the interrupt status
        Thread.currentThread().interrupt();
        
        // Then handle appropriately
        System.out.println("Thread was interrupted, stopping work");
        return;
    }
}

// Option 3: Wrap but preserve interrupt status
public void doWorkOrThrow() {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // Restore status FIRST
        throw new RuntimeException("Operation interrupted", e);
    }
}
```

### Complete Example

```java
public class InterruptibleTask implements Runnable {
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Do some work
                doWork();
                
                // Sleep between iterations
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            // Restore interrupt status so callers know we were interrupted
            Thread.currentThread().interrupt();
            System.out.println("Task interrupted, cleaning up...");
        } finally {
            cleanup();
        }
    }
    
    private void doWork() throws InterruptedException {
        // Check for interruption in long-running operations
        for (int i = 0; i < 1000; i++) {
            if (Thread.interrupted()) {  // Clears the flag
                throw new InterruptedException("Interrupted during work");
            }
            // Process item i
        }
    }
    
    private void cleanup() {
        System.out.println("Cleanup complete");
    }
}

// Usage
Thread worker = new Thread(new InterruptibleTask());
worker.start();

// Later, request the thread to stop
worker.interrupt();
```

### InterruptedException in try-with-resources

```java
public class InterruptibleResource implements AutoCloseable {
    
    public void doBlockingWork() throws InterruptedException {
        // Some blocking operation
        Thread.sleep(5000);
    }
    
    @Override
    public void close() {
        System.out.println("Resource closed");
    }
}

// Proper handling
public void useResource() {
    try (InterruptibleResource resource = new InterruptibleResource()) {
        resource.doBlockingWork();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // Restore status
        // Resource is still properly closed by try-with-resources
    }
}
```

### RuntimeException (Unchecked)

| Exception | Cause | How to Prevent |
|-----------|-------|----------------|
| `NullPointerException` | Calling method on null reference | Check for null before using |
| `ArrayIndexOutOfBoundsException` | Invalid array index | Check array bounds |
| `StringIndexOutOfBoundsException` | Invalid string index | Check string length |
| `ArithmeticException` | Division by zero | Check divisor |
| `NumberFormatException` | Invalid string to number | Validate input format |
| `ClassCastException` | Invalid type cast | Use instanceof check |
| `IllegalArgumentException` | Invalid method argument | Validate parameters |
| `IllegalStateException` | Object in wrong state | Check state before operation |
| `UnsupportedOperationException` | Operation not supported | Check documentation |
| `ConcurrentModificationException` | Modifying collection during iteration | Use iterator.remove() |

### Checked Exceptions

| Exception | Cause | How to Handle |
|-----------|-------|---------------|
| `IOException` | I/O operation failure | Retry, use defaults, report |
| `FileNotFoundException` | File does not exist | Create file or use default |
| `SQLException` | Database error | Retry, rollback, report |
| `ClassNotFoundException` | Class not on classpath | Check dependencies |
| `InterruptedException` | Thread interrupted | Handle interruption gracefully |
| `ParseException` | Parsing failure | Validate format first |
| `MalformedURLException` | Invalid URL format | Validate URL format |
| `SocketException` | Network socket error | Retry with backoff |
| `TimeoutException` | Operation timed out | Retry or fail gracefully |

### Examples of Each

```java
public class CommonExceptions {
    
    // NullPointerException
    public void nullPointer() {
        String s = null;
        // s.length();  // NullPointerException!
        
        // Prevention:
        if (s != null) {
            System.out.println(s.length());
        }
        // Or use Optional
    }
    
    // ArrayIndexOutOfBoundsException
    public void arrayIndex() {
        int[] arr = {1, 2, 3};
        // arr[10];  // ArrayIndexOutOfBoundsException!
        
        // Prevention:
        int index = 10;
        if (index >= 0 && index < arr.length) {
            System.out.println(arr[index]);
        }
    }
    
    // NumberFormatException
    public void numberFormat() {
        String input = "abc";
        // Integer.parseInt(input);  // NumberFormatException!
        
        // Prevention:
        try {
            int num = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
    }
    
    // ClassCastException
    public void classCast() {
        Object obj = "Hello";
        // Integer num = (Integer) obj;  // ClassCastException!
        
        // Prevention:
        if (obj instanceof Integer) {
            Integer num = (Integer) obj;
        }
    }
    
    // IllegalArgumentException
    public void illegalArgument(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}
```

---

## Exception Handling Best Practices

### 1. Catch Specific Exceptions

```java
// BAD - too broad
try {
    doSomething();
} catch (Exception e) {
    // What went wrong? Who knows!
    e.printStackTrace();
}

// GOOD - specific
try {
    doSomething();
} catch (FileNotFoundException e) {
    System.out.println("File not found, using defaults");
    useDefaults();
} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}
```

### 2. Don't Swallow Exceptions

```java
// BAD - exception is swallowed, problem is hidden
try {
    processData();
} catch (Exception e) {
    // Nothing! Problem is silently ignored
}

// BAD - only prints, doesn't handle
try {
    processData();
} catch (Exception e) {
    e.printStackTrace();  // Just printing is not handling
}

// GOOD - actually handle or propagate
try {
    processData();
} catch (DataException e) {
    logger.error("Data processing failed", e);
    notifyAdministrator(e);
    throw new ServiceException("Processing failed", e);
}
```

### 3. Use try-with-resources for Closeable Resources

```java
// BAD - manual close, error-prone
FileInputStream fis = null;
try {
    fis = new FileInputStream("file.txt");
    // use fis
} finally {
    if (fis != null) {
        try {
            fis.close();
        } catch (IOException e) {
            // Another exception to handle!
        }
    }
}

// GOOD - automatic close
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // use fis
}  // Automatically closed
```

### 4. Provide Meaningful Error Messages

```java
// BAD - no context
throw new IllegalArgumentException("Invalid");

// GOOD - specific and helpful
throw new IllegalArgumentException(
    "Email must contain @ symbol, got: '" + email + "'"
);
```

### 5. Log Exceptions Properly

```java
// BAD - loses stack trace
catch (Exception e) {
    logger.error("Error: " + e.getMessage());  // Stack trace lost!
}

// GOOD - preserves stack trace
catch (Exception e) {
    logger.error("Failed to process order " + orderId, e);  // e is second arg
}
```

### 6. Clean Up in finally or try-with-resources

```java
// Resources should be cleaned up even when exceptions occur
public void processFile(String path) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
        // Process file
    }  // Reader closed even if exception occurs
}
```

### 7. Don't Use Exceptions for Flow Control

```java
// BAD - using exception for normal flow
public boolean contains(int[] array, int value) {
    try {
        for (int i = 0; ; i++) {  // No bounds check
            if (array[i] == value) return true;
        }
    } catch (ArrayIndexOutOfBoundsException e) {
        return false;
    }
}

// GOOD - proper loop control
public boolean contains(int[] array, int value) {
    for (int element : array) {
        if (element == value) return true;
    }
    return false;
}
```

### 8. Throw Early, Catch Late

```java
// Throw early - validate at the beginning
public void transfer(Account from, Account to, double amount) {
    // Validate early
    if (from == null) throw new IllegalArgumentException("Source account required");
    if (to == null) throw new IllegalArgumentException("Target account required");
    if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
    
    // Proceed with operation
    from.withdraw(amount);
    to.deposit(amount);
}

// Catch late - handle at appropriate level
public class TransferController {
    public Response handleTransfer(TransferRequest request) {
        try {
            transferService.transfer(request.from(), request.to(), request.amount());
            return Response.success();
        } catch (IllegalArgumentException e) {
            return Response.badRequest(e.getMessage());
        } catch (InsufficientFundsException e) {
            return Response.error("Insufficient funds");
        }
    }
}
```

### 9. Use Custom Exceptions for Domain Logic

```java
// Instead of generic exceptions
throw new RuntimeException("Order validation failed");

// Use domain-specific exceptions
throw new OrderValidationException("Order must have at least one item");
throw new PaymentDeclinedException("Card expired");
throw new InventoryException("Product out of stock: " + productId);
```

### 10. Document Exceptions with Javadoc

```java
/**
 * Transfers money between accounts.
 *
 * @param from Source account
 * @param to Target account  
 * @param amount Amount to transfer
 * @throws IllegalArgumentException if any parameter is null or amount is not positive
 * @throws InsufficientFundsException if source account has insufficient balance
 * @throws AccountLockedException if either account is locked
 */
public void transfer(Account from, Account to, double amount) 
        throws InsufficientFundsException, AccountLockedException {
    // implementation
}
```

---

## Real-World Examples

### Example 1: User Registration with Validation

```java
public class UserRegistrationService {
    
    private UserRepository userRepository;
    private EmailService emailService;
    
    public User registerUser(String email, String password, String name) 
            throws RegistrationException {
        
        // Validate inputs
        validateEmail(email);
        validatePassword(password);
        validateName(name);
        
        // Check if user exists
        try {
            if (userRepository.findByEmail(email) != null) {
                throw new RegistrationException("Email already registered: " + email);
            }
        } catch (DataAccessException e) {
            throw new RegistrationException("Could not check email availability", e);
        }
        
        // Create user
        User user = new User(email, hashPassword(password), name);
        
        try {
            user = userRepository.save(user);
        } catch (DataAccessException e) {
            throw new RegistrationException("Could not create user account", e);
        }
        
        // Send welcome email (don't fail registration if email fails)
        try {
            emailService.sendWelcomeEmail(user);
        } catch (EmailException e) {
            logger.warn("Could not send welcome email to " + email, e);
            // Continue anyway - user is registered
        }
        
        return user;
    }
    
    private void validateEmail(String email) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }
    
    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain an uppercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new ValidationException("Password must contain a digit");
        }
    }
    
    private void validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }
        if (name.length() < 2) {
            throw new ValidationException("Name must be at least 2 characters");
        }
    }
}
```

### Example 2: File Processing with Recovery

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigurationLoader {
    
    private static final String DEFAULT_CONFIG = "defaults.json";
    private static final String USER_CONFIG = "config.json";
    
    public Configuration load() {
        Configuration config = new Configuration();
        
        // Try to load default configuration first
        try {
            loadFromFile(DEFAULT_CONFIG, config);
            System.out.println("Loaded default configuration");
        } catch (ConfigurationException e) {
            System.out.println("Warning: Could not load defaults: " + e.getMessage());
            // Continue with empty defaults
        }
        
        // Try to load user configuration (overrides defaults)
        try {
            loadFromFile(USER_CONFIG, config);
            System.out.println("Loaded user configuration");
        } catch (FileNotFoundException e) {
            System.out.println("No user configuration found, using defaults");
        } catch (ConfigurationException e) {
            System.out.println("Error in user configuration: " + e.getMessage());
            System.out.println("Using default values");
        }
        
        return config;
    }
    
    private void loadFromFile(String filename, Configuration config) 
            throws ConfigurationException, FileNotFoundException {
        
        Path path = Path.of(filename);
        
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Configuration file not found: " + filename);
        }
        
        try {
            String content = Files.readString(path);
            parseConfiguration(content, config);
        } catch (IOException e) {
            throw new ConfigurationException("Could not read " + filename, e);
        } catch (JsonParseException e) {
            throw new ConfigurationException("Invalid JSON in " + filename, e);
        }
    }
    
    private void parseConfiguration(String json, Configuration config) 
            throws JsonParseException {
        // Parse JSON and populate config
    }
}
```

### Example 3: API Client with Retries

```java
import java.net.http.*;
import java.time.Duration;

public class ApiClient {
    
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.ofSeconds(1);
    
    private HttpClient httpClient;
    
    public String fetchData(String url) throws ApiException {
        ApiException lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return doFetch(url);
            } catch (ApiException e) {
                lastException = e;
                
                if (!isRetryable(e)) {
                    throw e;  // Don't retry non-retryable errors
                }
                
                if (attempt < MAX_RETRIES) {
                    System.out.println("Attempt " + attempt + " failed, retrying...");
                    sleep(RETRY_DELAY.multipliedBy(attempt));  // Exponential backoff
                }
            }
        }
        
        throw new ApiException("Failed after " + MAX_RETRIES + " attempts", lastException);
    }
    
    private String doFetch(String url) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
            );
            
            if (response.statusCode() >= 400) {
                throw new ApiException(
                    "API returned error: " + response.statusCode(),
                    response.statusCode()
                );
            }
            
            return response.body();
            
        } catch (IOException e) {
            throw new ApiException("Network error", e, true);  // Retryable
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Request interrupted", e, false);
        }
    }
    
    private boolean isRetryable(ApiException e) {
        if (e.isRetryable()) return true;
        
        // Retry on server errors (5xx)
        int status = e.getStatusCode();
        return status >= 500 && status < 600;
    }
    
    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Custom API exception
public class ApiException extends Exception {
    private final int statusCode;
    private final boolean retryable;
    
    public ApiException(String message) {
        super(message);
        this.statusCode = -1;
        this.retryable = false;
    }
    
    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.retryable = false;
    }
    
    public ApiException(String message, Throwable cause, boolean retryable) {
        super(message, cause);
        this.statusCode = -1;
        this.retryable = retryable;
    }
    
    public ApiException(String message, Throwable cause) {
        this(message, cause, false);
    }
    
    public int getStatusCode() { return statusCode; }
    public boolean isRetryable() { return retryable; }
}
```

### Example 4: Database Transaction with Rollback

```java
import java.sql.*;

public class OrderRepository {
    
    private DataSource dataSource;
    
    public void createOrder(Order order) throws OrderException {
        Connection conn = null;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);  // Start transaction
            
            // Insert order
            long orderId = insertOrder(conn, order);
            order.setId(orderId);
            
            // Insert order items
            for (OrderItem item : order.getItems()) {
                insertOrderItem(conn, orderId, item);
                updateInventory(conn, item.getProductId(), -item.getQuantity());
            }
            
            // Update customer stats
            updateCustomerStats(conn, order.getCustomerId(), order.getTotal());
            
            conn.commit();  // All succeeded, commit
            
        } catch (SQLException e) {
            // Something failed, rollback everything
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);  // Add rollback failure info
                }
            }
            throw new OrderException("Failed to create order", e);
            
        } finally {
            // Always close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Reset
                    conn.close();
                } catch (SQLException closeEx) {
                    // Log but don't throw - original exception is more important
                    logger.warn("Error closing connection", closeEx);
                }
            }
        }
    }
    
    // Better version with try-with-resources
    public void createOrderModern(Order order) throws OrderException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                long orderId = insertOrder(conn, order);
                order.setId(orderId);
                
                for (OrderItem item : order.getItems()) {
                    insertOrderItem(conn, orderId, item);
                    updateInventory(conn, item.getProductId(), -item.getQuantity());
                }
                
                updateCustomerStats(conn, order.getCustomerId(), order.getTotal());
                
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            throw new OrderException("Failed to create order", e);
        }
    }
}
```

---

## Suppressed Exceptions

When using try-with-resources, if both the try block and the close() method throw exceptions, the close() exception is "suppressed".

```java
public class SuppressedExample {
    
    public static void main(String[] args) {
        try (ProblematicResource resource = new ProblematicResource()) {
            throw new RuntimeException("Exception in try block");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
            
            // Get suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable t : suppressed) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}

class ProblematicResource implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new RuntimeException("Exception in close()");
    }
}
```

**Output:**
```
Caught: Exception in try block
Suppressed: Exception in close()
```

### Adding Suppressed Exceptions Manually

```java
public void processAll(List<Task> tasks) throws ProcessingException {
    ProcessingException mainException = null;
    
    for (Task task : tasks) {
        try {
            task.process();
        } catch (Exception e) {
            if (mainException == null) {
                mainException = new ProcessingException("Processing failed");
            }
            mainException.addSuppressed(e);
        }
    }
    
    if (mainException != null) {
        throw mainException;  // Contains all failures as suppressed
    }
}
```

---

## Assertions

Assertions are a debugging tool used to verify assumptions in your code during development. They are disabled by default at runtime.

### Basic Assertion Syntax

```java
assert condition;
assert condition : errorMessage;
```

### Examples

```java
public class AssertionExample {
    
    public double calculateAverage(int[] numbers) {
        // Assert precondition
        assert numbers != null : "Array cannot be null";
        assert numbers.length > 0 : "Array cannot be empty";
        
        int sum = 0;
        for (int n : numbers) {
            sum += n;
        }
        
        double average = (double) sum / numbers.length;
        
        // Assert postcondition
        assert average >= Integer.MIN_VALUE && average <= Integer.MAX_VALUE 
            : "Average out of range: " + average;
        
        return average;
    }
    
    public void processAge(int age) {
        // Assert invariant
        assert age >= 0 && age <= 150 : "Invalid age: " + age;
        
        // Process...
    }
}
```

### Enabling Assertions

Assertions are disabled by default. Enable them with the `-ea` flag:

```bash
# Enable all assertions
java -ea MyProgram

# Enable assertions in specific package
java -ea:com.mycompany.mypackage... MyProgram

# Enable in specific class
java -ea:com.mycompany.MyClass MyProgram

# Disable assertions (default)
java -da MyProgram
```

### When AssertionError is Thrown

```java
public class AssertionDemo {
    public static void main(String[] args) {
        int value = -5;
        
        // This will throw AssertionError if assertions are enabled
        assert value >= 0 : "Value must be non-negative, got: " + value;
        
        System.out.println("Value: " + value);
    }
}

// With assertions enabled (-ea):
// Exception in thread "main" java.lang.AssertionError: Value must be non-negative, got: -5
//     at AssertionDemo.main(AssertionDemo.java:6)

// With assertions disabled (default):
// Value: -5
```

### Assertions vs Exceptions

| Aspect | Assertions | Exceptions |
|--------|------------|------------|
| Purpose | Catch programming bugs | Handle runtime errors |
| Enabled by default | No | Yes |
| Use in production | Usually disabled | Always enabled |
| Recovery | Not expected | Often possible |
| Use for | Internal invariants | Public API validation |

### When to Use Assertions

```java
public class AssertionGuidelines {
    
    // GOOD: Internal invariants
    private void processInternalState() {
        assert internalList != null : "Internal list should never be null";
        // Process...
    }
    
    // GOOD: Postconditions
    public int calculate(int input) {
        int result = doComplexCalculation(input);
        assert result >= 0 : "Result should never be negative";
        return result;
    }
    
    // BAD: Don't use for argument validation in public methods
    public void setAge(int age) {
        // assert age >= 0;  // WRONG - use exception instead
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.age = age;
    }
    
    // BAD: Don't use for conditions that can happen at runtime
    public void readFile(String path) throws IOException {
        // assert Files.exists(Path.of(path));  // WRONG - file might not exist
        if (!Files.exists(Path.of(path))) {
            throw new FileNotFoundException(path);
        }
        // Read file...
    }
    
    // GOOD: Check unreachable code
    public String getDayType(DayOfWeek day) {
        return switch (day) {
            case SATURDAY, SUNDAY -> "Weekend";
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            // If enum is extended, this assertion catches it
            default -> {
                assert false : "Unknown day: " + day;
                yield "Unknown";
            }
        };
    }
}
```

---

## Exception Performance Considerations

Creating exceptions is expensive because of stack trace generation. Understanding performance implications helps write efficient code.

### Why Exceptions Are Expensive

```java
public class ExceptionPerformance {
    
    // Stack trace generation is the expensive part
    public static void measureExceptionCost() {
        int iterations = 100_000;
        
        // Measure creating exception
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Exception e = new Exception("Error");
            // Stack trace is generated in constructor
        }
        long time = System.nanoTime() - start;
        System.out.println("Exception creation: " + time / 1_000_000 + " ms");
        
        // Measure throwing and catching
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try {
                throw new Exception("Error");
            } catch (Exception e) {
                // Caught
            }
        }
        time = System.nanoTime() - start;
        System.out.println("Throw and catch: " + time / 1_000_000 + " ms");
        
        // Compare to normal flow
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String result = (i % 2 == 0) ? "even" : "odd";
        }
        time = System.nanoTime() - start;
        System.out.println("Normal flow: " + time / 1_000_000 + " ms");
    }
}
```

### Avoiding Exception Overhead

```java
public class PerformanceOptimization {
    
    // BAD - using exception for control flow
    public Integer parseOrNull_Bad(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // BETTER - check before parsing (for frequent invalid input)
    public Integer parseOrNull_Better(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) && c != '-' && c != '+') {
                return null;
            }
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;  // Edge cases like overflow
        }
    }
    
    // ALTERNATIVE - use Optional parsing methods
    public OptionalInt parseOptional(String s) {
        try {
            return OptionalInt.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
}
```

### Pre-allocated Exception for Performance

For hot paths where you need exception semantics but can't afford the cost:

```java
public class PreallocatedException {
    
    // Pre-allocate exception without stack trace
    private static final ValidationException INVALID_INPUT = 
        new ValidationException("Invalid input") {
            @Override
            public synchronized Throwable fillInStackTrace() {
                return this;  // Don't fill stack trace
            }
        };
    
    public void validate(String input) throws ValidationException {
        if (input == null || input.isEmpty()) {
            throw INVALID_INPUT;  // Reuse same instance
        }
    }
    
    // Note: Only use this when:
    // 1. Performance is critical
    // 2. You don't need stack trace information
    // 3. The exception message is always the same
}
```

### Return Codes vs Exceptions

```java
public class ReturnCodesVsExceptions {
    
    // Using return codes (faster for expected failures)
    public static class ParseResult {
        public final boolean success;
        public final int value;
        public final String error;
        
        private ParseResult(boolean success, int value, String error) {
            this.success = success;
            this.value = value;
            this.error = error;
        }
        
        public static ParseResult success(int value) {
            return new ParseResult(true, value, null);
        }
        
        public static ParseResult failure(String error) {
            return new ParseResult(false, 0, error);
        }
    }
    
    public ParseResult parseNumber(String s) {
        if (s == null || s.isEmpty()) {
            return ParseResult.failure("Input is empty");
        }
        try {
            return ParseResult.success(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return ParseResult.failure("Not a valid number: " + s);
        }
    }
    
    // Usage
    public void process(String input) {
        ParseResult result = parseNumber(input);
        if (result.success) {
            System.out.println("Parsed: " + result.value);
        } else {
            System.out.println("Error: " + result.error);
        }
    }
}
```

### Exception Caching Anti-Pattern

```java
// NEVER DO THIS
public class BadExceptionCaching {
    // Cached exception - DO NOT DO THIS
    private static final Exception CACHED = new Exception("Error");
    
    public void doSomething() throws Exception {
        throw CACHED;  // Stack trace is wrong and never changes!
    }
}

// If you must cache, disable stack trace
public class AcceptableExceptionCaching {
    private static final MyException INSTANCE = new MyException();
    
    private static class MyException extends RuntimeException {
        MyException() {
            super("Validation failed", null, false, false);
        }
    }
    
    public void validate(String s) {
        if (s == null) {
            throw INSTANCE;  // OK only if you don't need stack trace
        }
    }
}
```

---

## Summary

### Key Concepts

| Concept | Description |
|---------|-------------|
| Exception | Event that disrupts normal program flow |
| try-catch | Handle exceptions locally |
| finally | Code that always runs (cleanup) |
| try-with-resources | Automatic resource cleanup |
| throw | Create and throw an exception |
| throws | Declare exceptions a method might throw |
| Checked exception | Must be handled or declared (IOException) |
| Unchecked exception | Optional handling (RuntimeException) |
| Custom exception | Domain-specific exception class |
| Exception chaining | Wrap exception with cause (nested exceptions) |
| Suppressed exceptions | Exceptions from close() in try-with-resources |
| Root cause | Original exception at the bottom of the chain |
| Assertions | Debug-time checks for programming bugs |

### Quick Reference

```java
// Basic try-catch
try {
    riskyOperation();
} catch (SpecificException e) {
    handleError(e);
}

// Multiple catch
try {
    riskyOperation();
} catch (TypeA | TypeB e) {
    handleError(e);
}

// try-catch-finally
try {
    riskyOperation();
} catch (Exception e) {
    handleError(e);
} finally {
    cleanup();
}

// try-with-resources
try (Resource r = new Resource()) {
    r.use();
}

// Throwing
throw new IllegalArgumentException("Invalid input");

// Method declaration
public void method() throws IOException {
    // ...
}

// Custom exception
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## Related Topics

- [Inner Classes](15-inner-classes.md) - Previous topic
- [Collections - Lists](17-collections-lists.md) - Next topic
- [Packages and Imports](packages-and-imports.md) - Package structure

---

[<- Previous: Inner Classes](15-inner-classes.md) | [Next: Collections - Lists ->](17-collections-lists.md) | [Back to Guide](../guide.md)
