# Inner Classes Cheat Sheet

[Full Documentation](../documentation/15-inner-classes.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)

---

## Types Overview

| Type | Access Outer Instance | Access Local Vars | Has Name |
|------|----------------------|-------------------|----------|
| Static Nested | No | N/A | Yes |
| Inner (Non-static) | Yes | N/A | Yes |
| Local | Yes | Effectively final | Yes |
| Anonymous | Yes | Effectively final | No |

---

## Static Nested Class

No access to outer instance members.

```java
public class Outer {
    private static String staticField = "static";
    private String instanceField = "instance";
    
    public static class Nested {
        void access() {
            System.out.println(staticField);    // OK
            // System.out.println(instanceField); // Error!
        }
    }
}

// Instantiation - no outer instance needed
Outer.Nested obj = new Outer.Nested();
```

### Common Use: Builder Pattern

```java
public class Request {
    private final String url;
    
    private Request(Builder b) { this.url = b.url; }
    
    public static class Builder {
        private String url;
        public Builder url(String url) { this.url = url; return this; }
        public Request build() { return new Request(this); }
    }
}

Request req = new Request.Builder().url("http://...").build();
```

---

## Inner Class (Non-static)

Has access to all outer members.

```java
public class Outer {
    private int value = 10;
    
    public class Inner {
        public void access() {
            System.out.println(value);        // Access outer's private
            System.out.println(Outer.this.value); // Explicit outer reference
        }
    }
}

// Instantiation - requires outer instance
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();
```

---

## Local Class

Defined inside a method.

```java
public void method() {
    final int localVar = 5;  // Must be effectively final
    
    class Local {
        void print() {
            System.out.println(localVar);  // Access local var
        }
    }
    
    Local local = new Local();
    local.print();
}
```

---

## Anonymous Class

No name, declared and instantiated together.

```java
// Implementing interface
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};

// Extending class
Thread t = new Thread("MyThread") {
    @Override
    public void run() {
        System.out.println("Custom: " + getName());
    }
};

// With constructor arguments
Animal dog = new Animal("Buddy") {
    @Override
    public void speak() {
        System.out.println(name + " barks");
    }
};
```

---

## Anonymous Class vs Lambda

```java
// Anonymous class - verbose
Comparator<String> c1 = new Comparator<String>() {
    public int compare(String a, String b) {
        return a.length() - b.length();
    }
};

// Lambda - concise (preferred)
Comparator<String> c2 = (a, b) -> a.length() - b.length();

// Use anonymous class when:
// - Multiple methods to implement
// - Need to maintain state
// - Need access to 'this' of the class itself
```

---

## Access Outer from Inner

```java
public class Outer {
    private int x = 1;
    
    class Inner {
        private int x = 2;
        
        void print() {
            int x = 3;
            System.out.println(x);            // 3 (local)
            System.out.println(this.x);       // 2 (inner's)
            System.out.println(Outer.this.x); // 1 (outer's)
        }
    }
}
```

---

## Quick Reference

### Instantiation

```java
// Static nested
Outer.StaticNested s = new Outer.StaticNested();

// Inner class
Outer outer = new Outer();
Outer.Inner i = outer.new Inner();
```

### When to Use

| Type | Use Case |
|------|----------|
| Static Nested | Builder, helper classes, no outer access |
| Inner | Need outer instance access |
| Local | Complex one-time logic in method |
| Anonymous | Simple one-time implementation |
| Lambda | Functional interface (preferred) |

### Common Patterns

```java
// Iterator as inner class
public class MyList<E> {
    private class MyIterator implements Iterator<E> {
        // Access to MyList's internal data
    }
}

// Static nested enum/record
public class Order {
    public static enum Status { PENDING, SHIPPED }
    public static record Item(String name, int qty) { }
}
```

---

[Full Documentation](../documentation/15-inner-classes.md) | [Back to Cheatsheet Index](../guide.md#cheatsheets)
