# 01. Variables and Data Types

[Back to Guide](../guide.md) | [Next: Operators ->](./02-operators.md)

**Quick Reference:** [Variables Cheatsheet](../cheatsheets/variables-cheatsheet.md)

---

## What Is a Variable?

A variable is a named storage location in your computer's memory that holds a value. Think of it as a labeled box where you can put data and retrieve it later using the label (the variable name).

**In plain words:** A variable gives a name to a piece of data so you can use it throughout your program.

**Real-world analogy:** Imagine a filing cabinet with labeled folders. Each folder has a name (like "Customer Age" or "Product Price") and contains a specific piece of information. Variables work the same way in programming.

---

## Why Variables Exist

Without variables, you would have to hardcode every value directly into your code. This would make programs:
- Impossible to update (change one value, change it everywhere)
- Impossible to work with user input
- Impossible to store calculation results

**Example - Without variables (bad):**
```java
System.out.println(25 * 12);  // What does 25 mean? What does 12 mean?
```

**Example - With variables (good):**
```java
int hoursPerMonth = 25;
int months = 12;
int totalHours = hoursPerMonth * months;
System.out.println(totalHours);  // Clear what we're calculating
```

Output:
```
300
```

---

## Overview

Variables are containers that store data values. In Java, every variable must have a declared type that determines what kind of data it can hold.

Java is a **statically typed** language, meaning:
- You must declare the type of a variable before using it
- The type cannot change after declaration
- The compiler checks types at compile time

**Visual: How Java Handles Types:**

```
    Dynamically Typed (Python, JavaScript)     Statically Typed (Java)
    ──────────────────────────────────────     ───────────────────────
    
    x = 10          # x is a number            int x = 10;     // x is an int
    x = "hello"     # x is now a string        x = "hello";    // COMPILE ERROR!
    
    Type can change at runtime                 Type is fixed at compile time
    Errors found when program runs             Errors found before program runs
```

**Why static typing matters in enterprise:**
- Bugs are caught early (at compile time, not in production)
- IDEs can provide better autocomplete and refactoring
- Code is self-documenting (you can see what type each variable holds)
- Large teams can work together with fewer integration issues

---

## Declaring Variables

Basic syntax:

```java
// Syntax: type variableName = value;
int age = 25;
String name = "Alice";
boolean isActive = true;
```

**Line-by-line explanation:**
- `int age = 25;` - Creates a variable called `age` that can only hold whole numbers (integers). It starts with the value 25.
- `String name = "Alice";` - Creates a variable called `name` that holds text. It starts with "Alice".
- `boolean isActive = true;` - Creates a variable called `isActive` that can only be true or false.

You can declare without initializing, then assign later:

```java
int count;      // Declaration - reserves space but no value yet
count = 10;     // Assignment - puts the value 10 into count

System.out.println(count);  // Output: 10
```

```java
// Declare multiple variables of the same type
int x, y, z;
x = 1;
y = 2;
z = 3;

System.out.println(x + y + z);  // Output: 6
```

```java
// Declare and initialize multiple on one line
int a = 1, b = 2, c = 3;

System.out.println(a);  // Output: 1
System.out.println(b);  // Output: 2
System.out.println(c);  // Output: 3
```

---

## Primitive Data Types

Java has 8 primitive types. They store simple values directly in memory.

**Visual: Primitive Types Overview:**

```
    PRIMITIVE TYPES - Store actual values directly in memory
    ─────────────────────────────────────────────────────────
    
    INTEGER TYPES (whole numbers)
    ┌────────┬────────┬────────┬────────┐
    │  byte  │ short  │  int   │  long  │
    │ 1 byte │ 2 bytes│ 4 bytes│ 8 bytes│
    │  -128  │-32,768 │ -2.1B  │ huge   │
    │  to    │   to   │   to   │ range  │
    │  127   │ 32,767 │ +2.1B  │        │
    └────────┴────────┴────────┴────────┘
                          ^
                          |
                    DEFAULT CHOICE
    
    FLOATING-POINT TYPES (decimal numbers)
    ┌────────────────┬────────────────┐
    │     float      │    double      │
    │    4 bytes     │    8 bytes     │
    │  ~7 digits     │  ~15 digits    │
    │  precision     │  precision     │
    └────────────────┴────────────────┘
                            ^
                            |
                      DEFAULT CHOICE
    
    OTHER TYPES
    ┌────────────────┬────────────────┐
    │      char      │    boolean     │
    │    2 bytes     │    1 bit*      │
    │ single char    │  true/false    │
    └────────────────┴────────────────┘
```

### Integer Types

| Type | Size | Range | Use Case |
|------|------|-------|----------|
| `byte` | 1 byte | -128 to 127 | Small numbers, file data |
| `short` | 2 bytes | -32,768 to 32,767 | Rarely used |
| `int` | 4 bytes | -2.1 billion to 2.1 billion | Default for integers |
| `long` | 8 bytes | Very large numbers | Large calculations, IDs |

```java
byte smallNumber = 100;
short mediumNumber = 30000;
int regularNumber = 2000000000;
long bigNumber = 9223372036854775807L;  // Note the 'L' suffix

System.out.println(smallNumber);    // Output: 100
System.out.println(mediumNumber);   // Output: 30000
System.out.println(regularNumber);  // Output: 2000000000
System.out.println(bigNumber);      // Output: 9223372036854775807
```

**Enterprise example - Database IDs:**
```java
// In enterprise applications, database IDs often use long
// because they can exceed int's max value (2.1 billion)
long customerId = 98765432101L;
long orderId = 123456789012L;
long transactionId = System.currentTimeMillis();  // Timestamp as ID

System.out.println("Customer: " + customerId);     // Output: Customer: 98765432101
System.out.println("Transaction: " + transactionId); // Output: Transaction: 1706380800000 (example)
```

**Underscores for readability (Java 7+):**
```java
// Use underscores to make large numbers easier to read
int million = 1_000_000;
long creditCardNumber = 1234_5678_9012_3456L;
int hexColor = 0xFF_00_FF;  // Magenta in hex

System.out.println(million);  // Output: 1000000
```

### Floating-Point Types

| Type | Size | Precision | Use Case |
|------|------|-----------|----------|
| `float` | 4 bytes | ~7 decimal digits | Graphics, games |
| `double` | 8 bytes | ~15 decimal digits | Default for decimals |

```java
float price = 19.99f;           // Note the 'f' suffix required
double preciseValue = 3.141592653589793;

System.out.println(price);          // Output: 19.99
System.out.println(preciseValue);   // Output: 3.141592653589793
```

**Scientific notation:**
```java
double avogadro = 6.022e23;     // 6.022 x 10^23
float tiny = 1.5e-10f;          // 0.00000000015

System.out.println(avogadro);   // Output: 6.022E23
System.out.println(tiny);       // Output: 1.5E-10
```

**WARNING - Never use float/double for money:**
```java
// WRONG - floating-point has precision issues
double balance = 0.1 + 0.2;
System.out.println(balance);    // Output: 0.30000000000000004 (not 0.3!)

// RIGHT - use BigDecimal for money (covered later)
// or store as cents (integer)
int balanceCents = 10 + 20;     // 30 cents
System.out.println(balanceCents / 100.0);  // Output: 0.3
```

### Character Type

| Type | Size | Range | Use Case |
|------|------|-------|----------|
| `char` | 2 bytes | Unicode characters | Single characters |

```java
char letter = 'A';
char digit = '7';
char symbol = '@';
char unicode = '\u0041';        // 'A' in Unicode
char newline = '\n';            // Escape sequence

System.out.println(letter);     // Output: A
System.out.println(unicode);    // Output: A
System.out.println(digit);      // Output: 7
```

### Boolean Type

| Type | Size | Values | Use Case |
|------|------|--------|----------|
| `boolean` | 1 bit* | true/false | Conditions, flags |

```java
boolean isJavaFun = true;
boolean isFishTasty = false;

System.out.println(isJavaFun);   // Output: true
System.out.println(isFishTasty); // Output: false
```

**Enterprise example - Feature flags:**
```java
// Feature flags control which features are enabled
boolean isNewCheckoutEnabled = true;
boolean isMaintenanceMode = false;
boolean isDebugLogging = false;

if (isNewCheckoutEnabled) {
    System.out.println("Using new checkout flow");
}
// Output: Using new checkout flow
```

**Result of comparisons:**
```java
int age = 25;
int x = 10;
int y = 20;

boolean isAdult = age >= 18;
boolean isEqual = (x == y);

System.out.println(isAdult);  // Output: true
System.out.println(isEqual);  // Output: false
```

*Actual memory usage depends on JVM implementation.

---

## Reference Types

Reference types store references (addresses) to objects in memory, not the actual values.

**Visual: Primitive vs Reference Types in Memory:**

```
    PRIMITIVE TYPE                      REFERENCE TYPE
    ──────────────                      ──────────────
    
    Variable          Memory            Variable          Memory
    ┌──────────┐     ┌────────┐        ┌──────────┐     ┌────────────────┐
    │ int age  │ --> │   25   │        │ name     │ --> │   address      │
    └──────────┘     └────────┘        └──────────┘     │   0x7A3F       │
                                                        └───────┬────────┘
    The variable                                                │
    HOLDS the value                                             ▼
    directly                                            ┌────────────────┐
                                                        │   "Alice"      │
                                                        │  (String obj)  │
                                                        └────────────────┘
                                                        
                                        The variable holds
                                        an ADDRESS pointing
                                        to the actual object
```

### String

String is not a primitive but is commonly used like one:

```java
String greeting = "Hello, World!";
String empty = "";
String nullString = null;       // No object reference

// String concatenation
String fullName = "John" + " " + "Doe";

// Text blocks (Java 15+) for multi-line strings
String json = """
    {
        "name": "Alice",
        "age": 30
    }
    """;

System.out.println(greeting);   // Output: Hello, World!
System.out.println(fullName);   // Output: John Doe
```

**Enterprise example - Configuration and API responses:**
```java
// Common in REST APIs and configuration
String apiEndpoint = "https://api.example.com/v1/users";
String apiKey = "sk_live_abc123";
String contentType = "application/json";

// JSON response (using text blocks)
String jsonResponse = """
    {
        "status": "success",
        "data": {
            "userId": 12345,
            "email": "user@example.com"
        }
    }
    """;

System.out.println("Endpoint: " + apiEndpoint);
// Output: Endpoint: https://api.example.com/v1/users
```

### Arrays

```java
int[] numbers = {1, 2, 3, 4, 5};
String[] names = {"Alice", "Bob", "Charlie"};

// Access elements (0-indexed)
int first = numbers[0];
String lastName = names[2];

System.out.println(first);      // Output: 1
System.out.println(lastName);   // Output: Charlie
```

### Objects

```java
// Any class instance is a reference type
Scanner scanner = new Scanner(System.in);
ArrayList<String> list = new ArrayList<>();

// Enterprise example - Domain objects
// User user = new User("alice@example.com", "Alice");
// Order order = new Order(12345L, user);
```

---

## The var Keyword (Java 10+)

Use `var` for local variable type inference. The compiler determines the type from the assigned value.

**In plain words:** Instead of writing out the full type name, you let Java figure it out from what you assign.

```java
var count = 10;                 // Inferred as int
var name = "Alice";             // Inferred as String
var prices = new ArrayList<Double>();  // Inferred as ArrayList<Double>
var active = true;              // Inferred as boolean

System.out.println(count);      // Output: 10
System.out.println(name);       // Output: Alice
```

**When to use var:**
- When the type is obvious from the right side
- To reduce verbosity with long type names
- For local variables only (not fields or parameters)

**When NOT to use var:**
- When the type is not obvious
- When you want to document the type explicitly
- For public API or shared code

```java
// Good use of var - type is obvious from constructor
var users = new HashMap<String, User>();
var stream = list.stream().filter(x -> x > 0);

// Avoid - type not obvious
var result = someMethod();      // What type is result?
var data = parse(input);        // Unclear
```

**Enterprise example - reducing verbosity:**
```java
// Without var (verbose)
Map<String, List<OrderItem>> ordersByCustomer = new HashMap<String, List<OrderItem>>();

// With var (cleaner, type is obvious from right side)
var ordersByCustomer = new HashMap<String, List<OrderItem>>();

// With var in try-with-resources
try (var connection = dataSource.getConnection();
     var statement = connection.prepareStatement(sql);
     var resultSet = statement.executeQuery()) {
    // Process results
}
```

---

## Constants

Use `final` to create constants (values that cannot change).

**Why this matters:** Constants prevent accidental changes to values that should never change. They also make code more readable by giving meaningful names to magic numbers.

```java
final int MAX_SIZE = 100;
final double PI = 3.14159;
final String COMPANY_NAME = "Acme Inc";

System.out.println(MAX_SIZE);       // Output: 100
System.out.println(COMPANY_NAME);   // Output: Acme Inc

// Convention: uppercase with underscores
final int MAX_RETRY_COUNT = 3;
final String DEFAULT_ENCODING = "UTF-8";
```

**Enterprise example - Application constants:**
```java
public class AppConstants {
    // API configuration
    public static final String API_VERSION = "v1";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // Timeouts (in milliseconds)
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 30000;
    
    // HTTP status codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_SERVER_ERROR = 500;
    
    // Error messages
    public static final String ERR_USER_NOT_FOUND = "User not found";
    public static final String ERR_INVALID_INPUT = "Invalid input provided";
}

// Usage
if (statusCode == AppConstants.HTTP_NOT_FOUND) {
    System.out.println(AppConstants.ERR_USER_NOT_FOUND);
}
// Output: User not found
```

Attempting to reassign throws a compile error:

```java
final int x = 10;
x = 20;  // COMPILE ERROR: cannot assign a value to final variable x
```

---

## Type Casting

Converting one type to another.

### Widening Casting (Automatic)

Smaller type to larger type - no data loss:

```java
// byte -> short -> int -> long -> float -> double
int myInt = 100;
long myLong = myInt;            // Automatic
double myDouble = myLong;       // Automatic

System.out.println(myInt);      // 100
System.out.println(myLong);     // 100
System.out.println(myDouble);   // 100.0
```

### Narrowing Casting (Manual)

Larger type to smaller type - may lose data:

```java
// double -> float -> long -> int -> short -> byte
double myDouble = 9.78;
int myInt = (int) myDouble;     // Manual cast required

System.out.println(myDouble);   // 9.78
System.out.println(myInt);      // 9 (decimal part lost)

// Be careful with overflow
int bigValue = 130;
byte smallValue = (byte) bigValue;  // -126 (overflow!)
```

### String Conversions

```java
// Primitive to String
int num = 42;
String str1 = String.valueOf(num);      // "42"
String str2 = Integer.toString(num);    // "42"
String str3 = num + "";                 // "42" (concatenation)

System.out.println(str1);  // Output: 42
System.out.println(str2);  // Output: 42
System.out.println(str3);  // Output: 42
```

```java
// String to primitive
String text = "123";
int parsed = Integer.parseInt(text);           // 123
double decimal = Double.parseDouble("3.14");   // 3.14
boolean flag = Boolean.parseBoolean("true");   // true

System.out.println(parsed);   // Output: 123
System.out.println(decimal);  // Output: 3.14
System.out.println(flag);     // Output: true
```

**WARNING - Handle parsing exceptions:**
```java
// Be careful with invalid input
try {
    int bad = Integer.parseInt("abc");  // NumberFormatException!
} catch (NumberFormatException e) {
    System.out.println("Invalid number format");
}
// Output: Invalid number format
```

**Enterprise example - Parsing configuration values:**
```java
// Reading configuration from environment or properties
String portStr = System.getenv("SERVER_PORT");  // e.g., "8080"
String timeoutStr = System.getenv("TIMEOUT");   // e.g., "30000"

// Safe parsing with defaults
int port = 8080;  // default
int timeout = 5000;  // default

try {
    if (portStr != null) {
        port = Integer.parseInt(portStr);
    }
    if (timeoutStr != null) {
        timeout = Integer.parseInt(timeoutStr);
    }
} catch (NumberFormatException e) {
    System.out.println("Invalid config value, using defaults");
}

System.out.println("Server port: " + port);
System.out.println("Timeout: " + timeout);
// Output: Server port: 8080
// Output: Timeout: 5000
```

---

## Default Values

Instance variables (fields) get default values if not initialized. Local variables do not.

| Type | Default Value |
|------|---------------|
| `byte`, `short`, `int`, `long` | 0 |
| `float`, `double` | 0.0 |
| `char` | '\u0000' (null character) |
| `boolean` | false |
| Reference types | null |

```java
public class Example {
    // Instance variables get defaults
    int count;              // Default: 0
    String name;            // Default: null
    boolean active;         // Default: false
    
    public void printDefaults() {
        System.out.println("count: " + count);
        System.out.println("name: " + name);
        System.out.println("active: " + active);
    }
    
    public void method() {
        int localVar;       // No default! Must initialize before use
        // System.out.println(localVar);  // COMPILE ERROR: variable not initialized
        
        localVar = 10;      // Now it's initialized
        System.out.println(localVar);  // Output: 10
    }
}

// Usage
Example ex = new Example();
ex.printDefaults();
// Output:
// count: 0
// name: null
// active: false
```

---

## Wrapper Classes

Each primitive has a corresponding wrapper class for use with collections and generics.

**Why wrapper classes exist:**
- Collections (like ArrayList) can only hold objects, not primitives
- Wrapper classes provide useful utility methods
- They allow null values (primitives cannot be null)

| Primitive | Wrapper Class |
|-----------|---------------|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

```java
// Autoboxing: primitive to wrapper (automatic)
Integer wrapped = 100;          // int -> Integer
Double boxed = 3.14;            // double -> Double

System.out.println(wrapped);    // Output: 100
System.out.println(boxed);      // Output: 3.14

// Unboxing: wrapper to primitive (automatic)
int unwrapped = wrapped;        // Integer -> int
double unboxed = boxed;         // Double -> double

System.out.println(unwrapped);  // Output: 100
System.out.println(unboxed);    // Output: 3.14
```

**Useful wrapper class methods:**
```java
// Integer utility methods
int max = Integer.MAX_VALUE;    // 2147483647
int min = Integer.MIN_VALUE;    // -2147483648
int parsed = Integer.parseInt("42");
String binary = Integer.toBinaryString(10);  // "1010"
String hex = Integer.toHexString(255);       // "ff"

System.out.println("Max int: " + max);       // Output: Max int: 2147483647
System.out.println("Binary of 10: " + binary); // Output: Binary of 10: 1010
System.out.println("Hex of 255: " + hex);    // Output: Hex of 255: ff
```

**Nullable wrappers:**
```java
// Wrappers can be null, primitives cannot
Integer nullableCount = null;   // OK - useful for "no value" scenarios
// int count = null;            // COMPILE ERROR!

// Enterprise example - database values that might be null
Integer userId = getUserIdFromDatabase();  // might return null
if (userId != null) {
    System.out.println("User ID: " + userId);
} else {
    System.out.println("User not found");
}
```

---

## Scope

Variables are only accessible within their scope (the block where they are declared).

**Visual: Variable Scope:**

```
    public class ScopeExample {
        int classLevel = 1;                    <-- CLASS SCOPE (anywhere in class)
        
        public void method() {
            int methodLevel = 2;               <-- METHOD SCOPE (only in this method)
            
            if (true) {
                int blockLevel = 3;            <-- BLOCK SCOPE (only in this if block)
                
                classLevel   // OK
                methodLevel  // OK
                blockLevel   // OK
            }
            
            classLevel   // OK
            methodLevel  // OK
            blockLevel   // ERROR - out of scope
        }
        
        public void anotherMethod() {
            classLevel   // OK
            methodLevel  // ERROR - belongs to other method
        }
    }
```

```java
public class ScopeExample {
    int classLevel = 1;         // Accessible anywhere in class
    
    public void method() {
        int methodLevel = 2;    // Accessible within this method
        
        if (true) {
            int blockLevel = 3; // Accessible only in this block
            System.out.println(classLevel);   // Output: 1
            System.out.println(methodLevel);  // Output: 2
            System.out.println(blockLevel);   // Output: 3
        }
        
        // System.out.println(blockLevel);   // COMPILE ERROR! Out of scope
    }
}
```

---

## Naming Conventions

Java naming conventions:

```java
// Variables: camelCase (start lowercase, capitalize each new word)
int itemCount;
String firstName;
boolean isVisible;
double accountBalance;

// Constants: UPPER_SNAKE_CASE (all caps with underscores)
final int MAX_SIZE = 100;
final String API_KEY = "abc123";
final double TAX_RATE = 0.08;

// Classes: PascalCase (capitalize each word including first)
class UserAccount { }
class HttpConnection { }
class OrderService { }

// Packages: lowercase (all lowercase, no underscores)
package com.example.myapp;
package org.company.service.user;
```

**Enterprise naming patterns:**
```java
// Entity/Model classes
public class User { }
public class Order { }
public class Product { }

// Service classes
public class UserService { }
public class OrderService { }

// Repository/DAO classes
public class UserRepository { }
public class OrderDao { }

// Controller classes
public class UserController { }
public class OrderController { }

// DTOs (Data Transfer Objects)
public class UserDto { }
public class CreateOrderRequest { }
public class OrderResponse { }

// Boolean variables start with is/has/can/should
boolean isActive;
boolean hasPermission;
boolean canEdit;
boolean shouldRetry;
```

---

## Common Mistakes

### 1. Integer Division

```java
// Integer division truncates (removes decimal part)
int result = 5 / 2;
System.out.println(result);  // Output: 2 (not 2.5!)

// Fix: use double or cast
double correct = 5.0 / 2;
double alsoCorrect = (double) 5 / 2;

System.out.println(correct);      // Output: 2.5
System.out.println(alsoCorrect);  // Output: 2.5
```

### 2. Comparing Strings

```java
String a = "hello";
String b = "hello";
String c = new String("hello");

// == compares references (memory addresses), not content
System.out.println(a == b);     // Output: true (same string pool reference)
System.out.println(a == c);     // Output: false (different objects!)

// ALWAYS use .equals() for content comparison
System.out.println(a.equals(c)); // Output: true

// Enterprise tip: put constant first to avoid NullPointerException
String userInput = null;
// userInput.equals("admin")   // NullPointerException!
"admin".equals(userInput)      // false (safe)
```

### 3. Uninitialized Variables

```java
int count;
// System.out.println(count);  // COMPILE ERROR: variable not initialized

count = 0;  // Initialize first
System.out.println(count);     // Output: 0
```

### 4. Overflow

```java
int max = Integer.MAX_VALUE;    // 2147483647
int overflow = max + 1;         // -2147483648 (wraps around!)

System.out.println(max);        // Output: 2147483647
System.out.println(overflow);   // Output: -2147483648

// Fix: Use long for larger values
long safe = (long) max + 1;
System.out.println(safe);       // Output: 2147483648
```

### 5. NullPointerException with Wrappers

```java
Integer count = null;
// int value = count;  // NullPointerException during unboxing!

// Safe approach
if (count != null) {
    int value = count;
    System.out.println(value);
}
```

---

## Summary

| Concept | Description |
|---------|-------------|
| Primitive types | 8 basic types: byte, short, int, long, float, double, char, boolean |
| Reference types | Objects, arrays, String - store memory addresses |
| var keyword | Type inference for local variables (Java 10+) |
| final | Creates constants that cannot be reassigned |
| Widening cast | Automatic: smaller to larger type |
| Narrowing cast | Manual: larger to smaller type (may lose data) |
| Wrapper classes | Object versions of primitives for collections |
| Autoboxing | Automatic conversion between primitives and wrappers |

---

## Cheat Sheet

| Goal | Syntax | Example |
|------|--------|---------|
| Declare integer | `int name = value;` | `int age = 25;` |
| Declare decimal | `double name = value;` | `double price = 19.99;` |
| Declare text | `String name = "value";` | `String name = "Alice";` |
| Declare boolean | `boolean name = true/false;` | `boolean isActive = true;` |
| Declare constant | `final TYPE NAME = value;` | `final int MAX = 100;` |
| Use type inference | `var name = value;` | `var count = 10;` |
| Parse string to int | `Integer.parseInt(str)` | `Integer.parseInt("42")` |
| Convert to string | `String.valueOf(value)` | `String.valueOf(42)` |

| Common Mistake | Problem | Fix |
|----------------|---------|-----|
| `5 / 2` | Returns 2, not 2.5 | Use `5.0 / 2` |
| `str1 == str2` | Compares references | Use `str1.equals(str2)` |
| Uninitialized local var | Compile error | Always initialize |
| `MAX_VALUE + 1` | Overflow wraps around | Use `long` |
| Unbox null wrapper | NullPointerException | Check for null first |

---

[Back to Guide](../guide.md) | [Next: Operators ->](./02-operators.md)
