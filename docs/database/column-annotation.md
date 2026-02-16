# @Column Annotation -- Controlling How Fields Map to Database Columns

## What is @Column?

`@Column` is a JPA annotation that lets you **customize how a Java field maps to a database column**. Without it, JPA still creates a column for each field in your entity -- but it uses default settings. `@Column` gives you control over things like the column name, maximum length, whether it allows null values, and more.

Think of it like this: JPA automatically creates a column for every field in your entity class. `@Column` is how you say "I want this column to behave differently from the defaults."

---

## Why Does It Matter?

In real-world applications, you almost always need to control your database schema:
- A `name` field should probably not allow null values
- An `email` field should be unique across all rows
- A `description` field might need more than the default 255 characters
- A `price` field needs specific decimal precision

Without `@Column`, JPA uses generic defaults that rarely match what your application actually needs. In professional environments, the database schema is carefully designed, and `@Column` is how you express that design in code.

---

## How It Works

`@Column` is placed directly on a field (or getter method) in an entity class. You pass it **attributes** to configure the column behavior.

```java
@Column(nullable = false, length = 100, unique = true)
private String email;
```

This tells JPA: "Create a column that cannot be null, allows up to 100 characters, and must be unique across all rows."

When Hibernate generates the SQL to create this column (using `ddl-auto=update` or `ddl-auto=create`), it translates these attributes into database-specific SQL. For MSSQL, the above would generate something like:

```sql
email NVARCHAR(100) NOT NULL UNIQUE
```

---

## All @Column Attributes

Here is every attribute you can use with `@Column`, what it does, and when to use it.

### Quick Reference Table

| Attribute | Type | Default | What It Does |
|---|---|---|---|
| `name` | String | field name | Sets the column name in the database |
| `nullable` | boolean | `true` | Whether the column allows NULL values |
| `length` | int | `255` | Maximum character length (only for String fields) |
| `unique` | boolean | `false` | Whether values must be unique across all rows |
| `updatable` | boolean | `true` | Whether the column can be changed after first insert |
| `insertable` | boolean | `true` | Whether the column is included in INSERT statements |
| `precision` | int | `0` | Total number of digits for decimal numbers |
| `scale` | int | `0` | Number of digits after the decimal point |
| `columnDefinition` | String | `""` | Raw SQL type definition (overrides everything else) |
| `table` | String | `""` | Which table this column belongs to (advanced, rarely used) |

---

### name

Sets the actual column name in the database. By default, JPA uses the Java field name.

```java
// Column will be called "project_name" in the database
@Column(name = "project_name")
private String name;

// Without @Column(name = ...), the column would just be called "name"
```

**When to use it:**
- When your database naming convention differs from Java naming
- When the database column already exists with a specific name
- Common pattern: Java uses `camelCase`, databases often use `snake_case`

**Important:** Hibernate automatically converts `camelCase` to `snake_case` by default in Spring Boot. So a field named `createdAt` becomes a column named `created_at` without you doing anything. You only need `name` when you want something different from this automatic conversion.

---

### nullable

Controls whether the column allows NULL values.

```java
// This field MUST have a value -- database will reject NULL
@Column(nullable = false)
private String name;

// This field CAN be NULL -- it is optional
@Column(nullable = true)  // true is the default, so this is the same as not writing it
private String description;
```

**Default:** `true` (columns allow NULL by default)

**What happens in the database:**

```sql
-- nullable = false generates:
name NVARCHAR(255) NOT NULL

-- nullable = true (or no @Column) generates:
description NVARCHAR(255) NULL
```

**What happens at runtime:** If you try to save an entity with a null value for a `nullable = false` column, the database will throw an error. However, this error happens at the **database level**, not in Java. For better error messages, combine it with Bean Validation (`@NotNull` or `@NotBlank`) which catches the problem **before** it reaches the database.

```java
// Good practice: use both @Column and validation together
@NotBlank(message = "Name is required")       // Catches it in Java (nice error message)
@Column(nullable = false, length = 100)        // Enforces it at database level (safety net)
private String name;
```

---

### length

Sets the maximum number of characters for `String` fields.

```java
@Column(length = 100)     // VARCHAR(100) or NVARCHAR(100)
private String name;

@Column(length = 500)     // VARCHAR(500) or NVARCHAR(500)
private String description;

@Column(length = 2000)    // VARCHAR(2000) or NVARCHAR(2000)
private String content;
```

**Default:** `255`

**Common misconception:** 255 is the **default**, not the **maximum**. You can set length to any value your database supports.

**Database limits by type (MSSQL):**

| SQL Type | Maximum Length |
|---|---|
| `VARCHAR(n)` | Up to 8,000 characters |
| `NVARCHAR(n)` | Up to 4,000 characters (Unicode, 2 bytes per character) |
| `VARCHAR(MAX)` | Up to ~2 billion characters |
| `NVARCHAR(MAX)` | Up to ~1 billion characters |
| `TEXT` | Up to ~2 billion characters (deprecated, use VARCHAR(MAX)) |

**Which type does Hibernate use?** By default, Hibernate maps Java `String` to `NVARCHAR` on MSSQL (because Java strings are Unicode). So `length = 500` becomes `NVARCHAR(500)`.

**What if you need more than 4,000 characters?** Use `columnDefinition`:

```java
// For very large text (blog posts, articles, etc.)
@Column(columnDefinition = "NVARCHAR(MAX)")
private String content;
```

**Important:** `length` only applies to `String` fields. It has no effect on `Integer`, `Long`, `LocalDateTime`, or other types.

**Choosing the right length -- practical guidelines:**

| Field Type | Suggested Length | Reasoning |
|---|---|---|
| Person name | 100 | Longest real names are around 70-80 characters |
| Email | 255 | RFC standard allows up to 254 characters |
| Project/task name | 100-150 | Short titles, reasonable limit |
| Description | 500-2000 | Depends on use case |
| URL | 2048 | Maximum URL length in most browsers |
| Short code (status, etc.) | 20-50 | Enum-like values |
| Blog post / article | Use `columnDefinition = "NVARCHAR(MAX)"` | Unpredictable length |

---

### unique

Ensures no two rows can have the same value in this column.

```java
// No two projects can have the same name
@Column(nullable = false, length = 100, unique = true)
private String name;
```

**What happens in the database:**

```sql
name NVARCHAR(100) NOT NULL UNIQUE
```

**What happens at runtime:** If you try to save a row with a duplicate value, the database throws a `DataIntegrityViolationException`. You should catch this in your code and return a meaningful error message (like "A project with this name already exists").

**When to use it:**
- Email addresses (each user has a unique email)
- Usernames
- Slugs or URL-friendly identifiers
- Any field that must be one-of-a-kind

**When NOT to use it:**
- Names that can legitimately be duplicated (two people can have the same name)
- Fields that are part of a **composite unique constraint** (use `@Table(uniqueConstraints = ...)` instead)

**Composite unique constraint example:**

```java
// If the combination of name + owner must be unique (not each individually)
@Table(
    name = "projects",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "owner_id"})
)
@Entity
public class Project {
    // ...
}
```

---

### updatable

Controls whether the column can be changed after the row is first inserted.

```java
// Once set, this value can NEVER be changed
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

// This value CAN be changed (default behavior)
@Column(nullable = false)
private LocalDateTime updatedAt;
```

**Default:** `true` (columns are updatable by default)

**How it works:** When `updatable = false`, Hibernate will **exclude** this column from all UPDATE SQL statements. Even if you change the value in Java, Hibernate will ignore it when generating the SQL.

```sql
-- With updatable = false on createdAt, Hibernate generates:
UPDATE projects SET name = ?, description = ?, updated_at = ? WHERE id = ?
-- Notice: created_at is NOT in the SET clause
```

**Common use cases:**
- `createdAt` timestamps -- should never change after creation
- `createdBy` fields -- the original creator should not change
- Foreign keys that should not change once set

**Common mistake:**

```java
// WRONG: updatedAt should NOT have updatable = false
@UpdateTimestamp
@Column(nullable = false, updatable = false)  // BUG!
private LocalDateTime updatedAt;

// RIGHT: updatedAt needs to be updatable so @UpdateTimestamp can change it
@UpdateTimestamp
@Column(nullable = false)
private LocalDateTime updatedAt;
```

The whole purpose of `@UpdateTimestamp` is to change the value on every update. Setting `updatable = false` prevents that, so the timestamp would be stuck at the creation time forever.

---

### insertable

Controls whether the column is included in INSERT statements.

```java
// This column is NOT included when inserting a new row
@Column(insertable = false)
private String computedField;
```

**Default:** `true`

**When to use it:**
- Database-generated columns (computed columns, defaults set by the database)
- Read-only columns that should not be written to
- Rarely used in typical applications

---

### precision and scale

Controls the total digits and decimal places for numeric fields (`BigDecimal`).

```java
import java.math.BigDecimal;

// price can have up to 10 digits total, with 2 after the decimal point
// Examples: 12345678.99, 0.50, 99999999.99
@Column(precision = 10, scale = 2)
private BigDecimal price;
```

**What this means:**
- `precision = 10` -- total number of digits (both sides of the decimal combined)
- `scale = 2` -- digits after the decimal point

**Database result:** `DECIMAL(10,2)` or `NUMERIC(10,2)`

**When to use it:**
- Money/currency fields (always use `BigDecimal`, never `float` or `double` for money)
- Percentages, rates, measurements

**Why BigDecimal instead of double?** Because `double` has floating-point precision errors:

```java
// This prints 0.30000000000000004, not 0.3
System.out.println(0.1 + 0.2);

// BigDecimal handles this correctly
BigDecimal a = new BigDecimal("0.1");
BigDecimal b = new BigDecimal("0.2");
System.out.println(a.add(b));  // Prints 0.3
```

---

### columnDefinition

Lets you write the **raw SQL type** for the column, overriding everything else.

```java
// Use the exact SQL type you want
@Column(columnDefinition = "NVARCHAR(MAX)")
private String content;

@Column(columnDefinition = "DECIMAL(19,4)")
private BigDecimal amount;

@Column(columnDefinition = "BIT DEFAULT 0")
private Boolean isActive;
```

**When to use it:**
- When you need a type that `length`, `precision`, and `scale` cannot express
- When you need database-specific features (like DEFAULT values)
- When the automatic type mapping does not match what you need

**Warning:** `columnDefinition` is **database-specific**. If you write `NVARCHAR(MAX)`, that works on MSSQL but not on PostgreSQL or MySQL. This reduces portability. Use it only when the standard attributes are not enough.

**When columnDefinition is set, it overrides these attributes:** `length`, `precision`, `scale`, `nullable`. Everything is in the raw SQL string.

---

## Do You Always Need @Column?

**No.** JPA creates a column for every field in your entity even without `@Column`. The annotation is only needed when you want to customize the defaults.

```java
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // No @Column -- JPA creates a nullable VARCHAR(255) column named "name"
    private String name;

    // No @Column -- JPA creates a nullable VARCHAR(255) column named "description"
    private String description;
}
```

**When you should use @Column:**
- You need `nullable = false` (required fields)
- You need a specific `length` (longer or shorter than 255)
- You need `unique = true`
- You need `updatable = false` or `insertable = false`
- You need to set `precision` and `scale` for decimals
- You want to rename the column with `name`

**When you can skip @Column:**
- The defaults (nullable, 255 length, not unique, updatable) are fine
- The column name matches the field name (or you are happy with the automatic camelCase to snake_case conversion)

---

## @Column vs Bean Validation

This is a common point of confusion. `@Column` and Bean Validation annotations (like `@NotNull`, `@Size`) do **different things at different levels**.

| Feature | @Column | Bean Validation (@NotNull, @Size, etc.) |
|---|---|---|
| **Where it works** | Database level | Java/application level |
| **When it runs** | When SQL hits the database | Before the SQL is even generated |
| **Error message** | Generic database error | Custom, user-friendly message |
| **Enforces schema** | Yes (creates NOT NULL, VARCHAR(n), etc.) | No (does not affect the database schema) |
| **Catches problems early** | No (fails at the database) | Yes (fails in Java before the query) |

**Best practice: use both together.**

```java
// Bean Validation catches it first with a clear message
@NotBlank(message = "Project name is required")
@Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")

// @Column enforces it at the database level as a safety net
@Column(nullable = false, length = 100)
private String name;
```

This way:
1. If invalid data comes through the API, Bean Validation catches it with a nice error message
2. If something bypasses validation (like a direct database insert), `@Column` constraints still protect the data

---

## Complete Example

Here is a realistic entity using various `@Column` configurations:

```java
package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Required, max 150 characters, must be unique
    @Column(nullable = false, length = 150, unique = true)
    private String name;

    // Optional, up to 1000 characters
    @Column(length = 1000)
    private String description;

    // Required, 10 digits total, 2 decimal places
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Required, database column named "sku_code"
    @Column(name = "sku_code", nullable = false, length = 50, unique = true)
    private String skuCode;

    // Optional, defaults to true in the database
    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean isActive;

    // Set once, never changed
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Updated automatically on every save
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

**Expected SQL generated by Hibernate (MSSQL):**

```sql
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name NVARCHAR(150) NOT NULL UNIQUE,
    description NVARCHAR(1000),
    price DECIMAL(10,2) NOT NULL,
    sku_code NVARCHAR(50) NOT NULL UNIQUE,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NOT NULL,
    PRIMARY KEY (id)
);
```

---

## Common Mistakes

### 1. Setting length on non-String fields

```java
// WRONG: length has no effect on Long, Integer, LocalDateTime, etc.
@Column(length = 20)
private Long id;

// RIGHT: length only works with String fields
@Column(length = 20)
private String code;
```

### 2. Using updatable = false on fields that need to change

```java
// WRONG: updatedAt MUST be updatable for @UpdateTimestamp to work
@UpdateTimestamp
@Column(updatable = false)
private LocalDateTime updatedAt;

// RIGHT: only createdAt should be non-updatable
@CreationTimestamp
@Column(updatable = false)
private LocalDateTime createdAt;
```

### 3. Relying only on @Column for validation

```java
// NOT ENOUGH: This only catches the error at the database level
@Column(nullable = false, length = 100)
private String name;

// BETTER: Add Bean Validation for clear error messages
@NotBlank(message = "Name is required")
@Size(max = 100, message = "Name must not exceed 100 characters")
@Column(nullable = false, length = 100)
private String name;
```

### 4. Not matching length with @Size

```java
// INCONSISTENT: @Size allows 200 but database only stores 100
@Size(max = 200)
@Column(length = 100)
private String name;
// Result: Validation passes, but database throws an error at 101+ characters

// CORRECT: Keep them in sync
@Size(max = 100)
@Column(length = 100)
private String name;
```

### 5. Using columnDefinition without understanding portability

```java
// This works on MSSQL but breaks on PostgreSQL or MySQL
@Column(columnDefinition = "NVARCHAR(MAX)")
private String content;

// More portable (works on most databases)
@Column(columnDefinition = "TEXT")
private String content;
// But TEXT is deprecated in MSSQL -- so pick based on your actual database
```

---

## Key Takeaways

- `@Column` customizes how a Java field maps to a database column -- it controls name, length, nullability, uniqueness, and more
- The **default length is 255**, not the maximum -- you can use any value your database supports (MSSQL: up to 8,000 for VARCHAR, 4,000 for NVARCHAR)
- Use `nullable = false` for required fields and `unique = true` for fields that must be one-of-a-kind
- `updatable = false` prevents a field from being changed after first insert -- perfect for `createdAt`, wrong for `updatedAt`
- Always pair `@Column` with Bean Validation (`@NotNull`, `@Size`, etc.) for a proper two-layer defense
- `precision` and `scale` are only for decimal numbers (`BigDecimal`) -- always use `BigDecimal` for money, never `double`
- `columnDefinition` is a powerful escape hatch but makes your code database-specific -- use it sparingly

---

## Related Topics

- [Entities and Table Mapping](entities.md) -- how to create JPA entities with @Entity, @Table, @Id, and more
- [Input Validation](../rest-api/input-validation.md) -- Bean Validation annotations (@NotNull, @Size, @Email, etc.)
- [ResponseEntity](../rest-api/response-entity.md) -- controlling HTTP responses from your controllers
