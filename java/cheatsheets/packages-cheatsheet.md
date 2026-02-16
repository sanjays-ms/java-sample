# Packages and Imports Cheat Sheet

[Full Documentation](../documentation/packages-and-imports.md) | [Back to Guide](../guide.md)

---

## Package Declaration

```java
// Syntax
package packagename;

// Examples
package utilities;
package com.example.myapp;
package com.example.myapp.model;
```

**Must be first statement in file (before imports).**

---

## Import Statements

| Type | Syntax | Example |
|------|--------|---------|
| Single class | `import pkg.Class;` | `import java.util.List;` |
| Wildcard | `import pkg.*;` | `import java.util.*;` |
| Static member | `import static pkg.Class.member;` | `import static java.lang.Math.PI;` |
| Static wildcard | `import static pkg.Class.*;` | `import static java.lang.Math.*;` |

```java
// File order
package com.example.app;          // 1. Package (optional)

import java.util.List;            // 2. Imports (optional)
import java.util.ArrayList;
import static java.lang.Math.*;

public class MyClass {            // 3. Class definition
    // ...
}
```

---

## Package Naming Conventions

| Rule | Example |
|------|---------|
| All lowercase | `com.example.myapp` |
| Reverse domain | `com.google.gson` |
| No Java keywords | Avoid `int`, `class`, etc. |
| Avoid `java.*`, `javax.*` | Reserved for Java SE |

```java
// Common structure
com.company.project.module
com.company.project.model
com.company.project.service
com.company.project.controller
com.company.project.util
```

---

## Directory Structure

Package name maps to folder structure:

```
package com.example.shop.model;
  -->   com/example/shop/model/Product.java
```

```
project/
└── src/
    └── com/
        └── example/
            └── shop/
                ├── model/
                │   └── Product.java
                └── service/
                    └── ProductService.java
```

---

## Access Modifiers Quick Reference

| Modifier | Same Class | Same Package | Subclass | Anywhere |
|----------|:----------:|:------------:|:--------:|:--------:|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (none) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

```java
public class Example {
    public int a;       // Everywhere
    protected int b;    // Package + subclasses
    int c;              // Package only (default)
    private int d;      // This class only
}
```

---

## Common Java Packages

| Package | Purpose | Auto-imported? |
|---------|---------|:--------------:|
| `java.lang` | Core (String, Math, System) | Yes |
| `java.util` | Collections, utilities | No |
| `java.time` | Date and time | No |
| `java.io` | Input/output | No |
| `java.nio.file` | File operations | No |
| `java.util.stream` | Stream API | No |
| `java.math` | BigDecimal, BigInteger | No |

---

## Static Imports

```java
// Without static import
double result = Math.sqrt(Math.pow(3, 2) + Math.pow(4, 2));

// With static import
import static java.lang.Math.*;
double result = sqrt(pow(3, 2) + pow(4, 2));
```

**Common static imports:**
```java
import static java.lang.Math.*;
import static java.lang.System.out;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
```

---

## Resolving Name Conflicts

```java
// Two classes with same name
import java.util.Date;

public class Example {
    Date utilDate = new Date();                    // java.util.Date
    java.sql.Date sqlDate = new java.sql.Date(0); // Fully qualified
}
```

---

## Wildcard Import Notes

```java
import java.util.*;     // Imports java.util classes only

// Does NOT import subpackages!
// Must import each subpackage separately:
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

// This does NOT work:
import java.*;  // WRONG - doesn't import anything useful
```

---

## Default Package (Avoid!)

```java
// No package statement = default package
public class QuickTest {  // Can't be imported!
}
```

| Issue | Problem |
|-------|---------|
| Cannot be imported | Other packages can't use it |
| No organization | All files mixed together |
| Unprofessional | Real projects use packages |

---

## Module Basics (Java 9+)

```java
// module-info.java
module com.example.myapp {
    exports com.example.myapp.api;
    requires java.sql;
    requires java.logging;
}
```

| Keyword | Purpose |
|---------|---------|
| `exports` | Make package available to other modules |
| `requires` | Depend on another module |
| `opens` | Allow reflection access |

**Optional for learning - focus on packages first.**

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Forgetting package statement | Add `package com.example.app;` |
| Package doesn't match directory | Ensure folder structure matches |
| Importing same package | Unnecessary - remove import |
| `import java.*;` | Use `import java.util.*;` etc. |
| Making everything public | Use package-private for internals |

---

## Quick Reference

```java
// Complete file structure
package com.example.app;                    // Package

import java.util.List;                      // Regular imports
import java.util.ArrayList;
import java.time.LocalDate;
import static java.lang.Math.sqrt;          // Static imports

public class MyService {                    // Public class (matches filename)
    
    private List<String> items;             // Private field
    
    public void process() {                 // Public method
        double result = sqrt(16);           // Using static import
    }
}

class Helper {                              // Package-private class
    void help() { }                         // Package-private method
}
```
