# Exception Handling Cheat Sheet

[Full Documentation](../documentation/16-exception-handling.md) | [Back to Guide](../guide.md)

---

## Exception Hierarchy

```
Throwable
├── Error (don't catch - JVM problems)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception (catch these)
    ├── IOException (checked)
    ├── SQLException (checked)
    └── RuntimeException (unchecked)
        ├── NullPointerException
        ├── ArrayIndexOutOfBoundsException
        ├── IllegalArgumentException
        └── ArithmeticException
```

---

## Checked vs Unchecked

| Type | Extends | Compiler Forces | Examples |
|------|---------|-----------------|----------|
| Checked | `Exception` | Yes - must handle or declare | `IOException`, `SQLException` |
| Unchecked | `RuntimeException` | No | `NullPointerException`, `IllegalArgumentException` |

---

## try-catch-finally

```java
try {
    // Code that might throw exception
} catch (SpecificException e) {
    // Handle specific exception
} catch (Exception e) {
    // Handle other exceptions
} finally {
    // Always runs (cleanup)
}
```

---

## Multi-catch (Java 7+)

```java
try {
    // code
} catch (IOException | SQLException | ParseException e) {
    // Handle all the same way
    System.out.println("Error: " + e.getMessage());
}
```

---

## try-with-resources (Java 7+)

```java
// Automatically closes resources
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
     BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt"))) {
    
    String line = reader.readLine();
    writer.write(line);
    
}  // Both closed automatically, even if exception occurs
```

**Works with any `AutoCloseable`:**
- `InputStream`, `OutputStream`
- `Reader`, `Writer`
- `Connection`, `Statement`, `ResultSet`
- `Scanner`

---

## throw vs throws

| Keyword | Purpose | Location | Example |
|---------|---------|----------|---------|
| `throw` | Throw an exception | Method body | `throw new Exception("msg");` |
| `throws` | Declare exceptions | Signature | `void m() throws IOException {}` |

```java
// throws in signature
public void readFile(String path) throws IOException {
    if (path == null) {
        throw new IllegalArgumentException("Path required");  // throw
    }
    Files.readString(Path.of(path));  // might throw IOException
}
```

---

## Custom Exceptions

```java
// Checked exception
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Unchecked exception
public class MyRuntimeException extends RuntimeException {
    public MyRuntimeException(String message) {
        super(message);
    }
}
```

---

## Exception Object Methods

| Method | Returns | Use |
|--------|---------|-----|
| `getMessage()` | `String` | Error description |
| `getCause()` | `Throwable` | Original exception |
| `printStackTrace()` | void | Print stack trace |
| `getSuppressed()` | `Throwable[]` | Suppressed exceptions |

```java
catch (Exception e) {
    System.out.println(e.getMessage());  // Error description
    e.printStackTrace();                  // Full stack trace
    Throwable cause = e.getCause();       // Original cause
}
```

---

## Exception Chaining

```java
try {
    readFile();
} catch (IOException e) {
    // Wrap with context, preserve cause
    throw new ServiceException("Config load failed", e);
}
```

---

## Common Exceptions Quick Reference

### Unchecked (RuntimeException)

| Exception | Cause | Prevention |
|-----------|-------|------------|
| `NullPointerException` | Method on null | Check != null |
| `ArrayIndexOutOfBoundsException` | Bad array index | Check bounds |
| `NumberFormatException` | Invalid parse | Validate input |
| `IllegalArgumentException` | Bad argument | Validate params |
| `ClassCastException` | Invalid cast | Use instanceof |
| `ArithmeticException` | Divide by zero | Check divisor |

### Checked

| Exception | Cause | Handling |
|-----------|-------|----------|
| `IOException` | I/O failure | Retry, default, report |
| `FileNotFoundException` | Missing file | Create or use default |
| `SQLException` | Database error | Rollback, report |
| `ParseException` | Parse failure | Validate format |

---

## Best Practices

| Do | Don't |
|----|-------|
| Catch specific exceptions | Catch `Exception` broadly |
| Use try-with-resources | Manually close in finally |
| Log with stack trace | Swallow exceptions silently |
| Throw early, catch late | Catch and ignore |
| Provide helpful messages | Use vague error text |
| Document with `@throws` | Leave exceptions undocumented |

---

## Patterns

### Validation with Exceptions

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
    this.age = age;
}
```

### Wrapping Exceptions

```java
public User findUser(String id) throws ServiceException {
    try {
        return repository.findById(id);
    } catch (SQLException e) {
        throw new ServiceException("Could not find user", e);
    }
}
```

### Default on Failure

```java
public int parseOrDefault(String s, int defaultValue) {
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}
```

### Retry Pattern

```java
public String fetchWithRetry(String url, int maxAttempts) throws ApiException {
    for (int i = 1; i <= maxAttempts; i++) {
        try {
            return fetch(url);
        } catch (IOException e) {
            if (i == maxAttempts) throw new ApiException("Failed", e);
            sleep(1000 * i);  // Backoff
        }
    }
    throw new ApiException("Unreachable");
}
```

---

## Quick Examples

```java
// Basic handling
try {
    int result = Integer.parseInt(input);
} catch (NumberFormatException e) {
    System.out.println("Invalid number");
}

// Resource management
try (var reader = new BufferedReader(new FileReader("data.txt"))) {
    return reader.readLine();
}

// Throw with message
if (name == null) {
    throw new IllegalArgumentException("Name required");
}

// Declare checked exception
public void save(Data data) throws IOException {
    Files.writeString(path, data.toString());
}

// Custom exception
throw new OrderNotFoundException("Order " + id + " not found");
```
