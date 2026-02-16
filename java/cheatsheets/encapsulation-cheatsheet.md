# Encapsulation Cheat Sheet

[Full Documentation](../documentation/11-encapsulation.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)

---

## Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (default) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

```java
public class Example {
    private int secret;        // This class only
    int packageLevel;          // Same package
    protected int forChildren; // Package + subclasses
    public int forEveryone;    // Everywhere
}
```

---

## Getters and Setters

### Basic Pattern

```java
public class Person {
    private String name;
    private int age;
    private boolean active;
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public boolean isActive() { return active; }  // boolean uses "is"
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setActive(boolean active) { this.active = active; }
}
```

### With Validation

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age");
    }
    this.age = age;
}

public void setEmail(String email) {
    if (email == null || !email.contains("@")) {
        throw new IllegalArgumentException("Invalid email");
    }
    this.email = email.toLowerCase();
}
```

### Read-Only Property

```java
private final String id;

public String getId() { return id; }  // No setter!
```

### Computed Property

```java
private double width, height;

public double getArea() {
    return width * height;  // Computed, not stored
}
```

---

## Defensive Copying

### Mutable Objects

```java
// Input: copy what comes in
public Event(Date date) {
    this.date = new Date(date.getTime());
}

// Output: copy what goes out
public Date getDate() {
    return new Date(date.getTime());
}
```

### Collections

```java
// Input
public Team(List<String> members) {
    this.members = new ArrayList<>(members);
}

// Output options
public List<String> getMembers() {
    return new ArrayList<>(members);           // Copy
    // OR
    return Collections.unmodifiableList(members);  // View
    // OR
    return List.copyOf(members);               // Immutable copy
}
```

### Arrays

```java
public Scores(int[] values) {
    this.values = Arrays.copyOf(values, values.length);
}

public int[] getValues() {
    return Arrays.copyOf(values, values.length);
}
```

---

## Encapsulation Patterns

### JavaBeans

```java
public class Customer {
    private String name;
    
    public Customer() { }  // No-arg constructor
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

### Immutable Object

```java
public final class Point {
    private final int x, y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public Point withX(int x) {
        return new Point(x, this.y);
    }
}
```

### Fluent Setters

```java
public class Builder {
    private String name;
    
    public Builder name(String name) {
        this.name = name;
        return this;
    }
}

// Usage
new Builder().name("Test").value(10).build();
```

---

## Encapsulating Collections

```java
public class Library {
    private final Map<String, Book> books = new HashMap<>();
    
    // Don't expose map! Provide methods instead
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }
    
    public Book findByIsbn(String isbn) {
        return books.get(isbn);
    }
    
    public int getBookCount() {
        return books.size();
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
}
```

---

## Quick Reference

### When to Use Each Modifier

| Modifier | Use For |
|----------|---------|
| `private` | Fields, helper methods |
| (default) | Package-internal classes/methods |
| `protected` | Extension points for subclasses |
| `public` | Public API |

### Encapsulation Checklist

- [ ] All fields are `private`
- [ ] Only necessary getters/setters exist
- [ ] Setters validate input
- [ ] Mutable objects are copied in/out
- [ ] Collections are never exposed directly
- [ ] Use most restrictive access level

### Common Mistakes

```java
// BAD: Public field
public String name;

// BAD: Returning internal collection
public List<String> getItems() {
    return items;  // Exposes internal state!
}

// BAD: No validation
public void setAge(int age) {
    this.age = age;  // Accepts invalid values!
}
```

### Quick Fixes

```java
// GOOD: Private with getter/setter
private String name;
public String getName() { return name; }

// GOOD: Return copy
public List<String> getItems() {
    return new ArrayList<>(items);
}

// GOOD: Validate
public void setAge(int age) {
    if (age < 0) throw new IllegalArgumentException();
    this.age = age;
}
```

---

[Full Documentation](../documentation/11-encapsulation.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)
