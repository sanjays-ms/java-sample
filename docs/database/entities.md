# JPA Entity Annotations -- Mapping Java Classes to Database Tables

## What is a JPA Entity?

A JPA entity is a **regular Java class** that JPA (Java Persistence API) maps to a **database table**. Each instance of the class represents one **row** in that table, and each field represents a **column**.

Think of it this way:
- The Java **class** = the database **table structure** (column names and types)
- A Java **object** (instance) = one **row** in the table
- A Java **field** = one **column** in the table

JPA uses **annotations** (special labels starting with `@`) to understand how your class should be mapped to the database.

---

## Why Does It Matter?

Without JPA entities, you would write raw SQL for every database operation:

```java
// Without JPA -- painful and error-prone
String sql = "INSERT INTO projects (name, description, created_at) VALUES (?, ?, ?)";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, "DevFlow");
stmt.setString(2, "A task manager");
stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
stmt.executeUpdate();
```

With JPA entities, you work with Java objects and JPA handles the SQL:

```java
// With JPA -- clean and simple
Project project = new Project();
project.setName("DevFlow");
project.setDescription("A task manager");
projectRepository.save(project);  // JPA generates the SQL for you
```

In professional environments, JPA (through Hibernate) is the **standard** way to interact with databases in Spring Boot applications. Understanding entity annotations is fundamental to every Spring Boot project you will ever work on.

---

## The Core Annotations

### @Entity

Marks a class as a JPA entity -- meaning "this class maps to a database table."

```java
import jakarta.persistence.Entity;

@Entity  // This class is now a database table
public class Project {
    // fields become columns
}
```

**Rules:**
- Every entity MUST have a field marked with `@Id` (primary key)
- Every entity MUST have a **no-argument constructor** (public or protected)
- The class must NOT be `final` (Hibernate creates proxy subclasses internally)
- Fields must NOT be `final` (Hibernate needs to set them via reflection)

**What package to import from:** Always use `jakarta.persistence.Entity`. There is also `org.hibernate.annotations.Entity` which is **deprecated** -- do not use it.

---

### @Table

Customizes which database table the entity maps to.

```java
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")  // Maps to a table called "projects"
public class Project {
    // ...
}
```

**Without @Table:** JPA uses the class name as the table name. A class called `Project` would map to a table called `project` (lowercase).

**When to use it:**
- When you want a **plural table name** (convention: table names are usually plural -- `projects`, `users`, `tasks`)
- When the table name differs from the class name
- When you want to specify the **schema**: `@Table(name = "projects", schema = "dbo")`

**Additional @Table features:**

```java
@Table(
    name = "projects",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "owner_id"})
    // This creates a composite unique constraint -- the COMBINATION of name + owner_id must be unique
)
```

---

### @Id

Marks a field as the **primary key** of the table.

```java
import jakarta.persistence.Id;

@Entity
@Table(name = "projects")
public class Project {
    @Id  // This field is the primary key
    private Long id;
}
```

**Important -- which @Id to import:**

| Import | Use For |
|---|---|
| `jakarta.persistence.Id` | JPA entities (this is what you want 99% of the time) |
| `org.springframework.data.annotation.Id` | Non-JPA Spring Data (MongoDB, Redis, etc.) |

For any class annotated with `@Entity`, always use `jakarta.persistence.Id`.

**Primary key types:** The most common types for primary keys are:

| Type | Description | When to Use |
|---|---|---|
| `Long` | Auto-incrementing number | Most common choice, simple and efficient |
| `Integer` | Auto-incrementing number | When you know you will have fewer than ~2 billion rows |
| `UUID` | Universally unique identifier | Distributed systems, when IDs must be globally unique |
| `String` | Text-based key | Natural keys like email or code (less common) |

**Industry standard:** `Long` is the most common choice for primary keys in Spring Boot applications.

---

### @GeneratedValue

Tells JPA **how to generate** the primary key value automatically.

```java
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Strategies:**

| Strategy | How It Works | Best For |
|---|---|---|
| `GenerationType.IDENTITY` | Database auto-increments the ID (e.g., `IDENTITY` in MSSQL) | MSSQL, MySQL, PostgreSQL |
| `GenerationType.SEQUENCE` | Uses a database sequence object | PostgreSQL, Oracle |
| `GenerationType.TABLE` | Uses a separate table to track IDs | Portable but slow (rarely used) |
| `GenerationType.AUTO` | JPA picks the best strategy for your database | When you want JPA to decide |
| `GenerationType.UUID` | Generates a UUID automatically | When you need globally unique IDs |

**For MSSQL:** Use `GenerationType.IDENTITY`. MSSQL has built-in support for identity columns that auto-increment.

**What happens behind the scenes:**

```sql
-- IDENTITY strategy generates:
CREATE TABLE projects (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    -- IDENTITY(1,1) means: start at 1, increment by 1
    ...
);
```

When you save a new entity, you do NOT set the id yourself:

```java
Project project = new Project();
// Do NOT set id -- the database generates it
project.setName("DevFlow");
projectRepository.save(project);  // After save, project.getId() returns the generated ID
```

---

### @CreationTimestamp

Automatically sets the field to the **current time when the entity is first saved**. This is a Hibernate-specific annotation (not part of the JPA standard).

```java
import org.hibernate.annotations.CreationTimestamp;

@CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;
```

**How it works:**
1. You create a new entity and call `save()`
2. Hibernate detects `@CreationTimestamp` and sets the field to `LocalDateTime.now()`
3. The value is included in the INSERT statement
4. The value never changes after that (especially if you add `updatable = false`)

**Why `updatable = false`?** Because a creation timestamp should never change. Adding `updatable = false` to `@Column` ensures that even if someone accidentally calls `setCreatedAt()`, Hibernate will ignore it in UPDATE statements.

**Supported types:**
- `LocalDateTime` (most common)
- `Instant`
- `Date` (java.util.Date -- outdated, prefer LocalDateTime)
- `Calendar` (outdated)

---

### @UpdateTimestamp

Automatically sets the field to the **current time every time the entity is saved** (insert and update). Also a Hibernate-specific annotation.

```java
import org.hibernate.annotations.UpdateTimestamp;

@UpdateTimestamp
@Column(nullable = false)
private LocalDateTime updatedAt;
```

**How it works:**
1. On INSERT: Hibernate sets it to `LocalDateTime.now()` (same as @CreationTimestamp)
2. On UPDATE: Hibernate updates it to `LocalDateTime.now()` again
3. This happens automatically -- you never need to set it manually

**Critical: Do NOT use `updatable = false` with `@UpdateTimestamp`:**

```java
// WRONG -- these contradict each other
@UpdateTimestamp
@Column(updatable = false)  // BUG! Hibernate cannot update this field!
private LocalDateTime updatedAt;

// RIGHT -- updatedAt must be updatable
@UpdateTimestamp
@Column(nullable = false)
private LocalDateTime updatedAt;
```

`@UpdateTimestamp` needs to change the value on every update. `updatable = false` prevents any changes. Together, the timestamp would be stuck at the creation time forever.

---

## Lombok Annotations for Entities

Lombok is a Java library that generates repetitive code (getters, setters, constructors) at compile time, so you do not have to write it yourself.

### @Getter and @Setter

Generate getter and setter methods for all fields.

```java
import lombok.Getter;
import lombok.Setter;

@Getter  // Generates getId(), getName(), getDescription(), etc.
@Setter  // Generates setId(), setName(), setDescription(), etc.
@Entity
public class Project {
    private Long id;
    private String name;
}
```

Without Lombok, you would need to write every getter and setter manually:

```java
// Without Lombok -- verbose and repetitive
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
// ... and so on for every field
```

### @NoArgsConstructor

Generates a constructor with no arguments.

```java
import lombok.NoArgsConstructor;

@NoArgsConstructor  // Generates: public Project() { }
@Entity
public class Project {
    // ...
}
```

**Why entities need this:** JPA/Hibernate creates entity objects using reflection. It calls the no-argument constructor first, then sets the fields. Without a no-args constructor, Hibernate cannot create instances of your entity and will throw an error.

If you do not use Lombok, Java provides a default no-args constructor automatically -- but only if you have **no other constructors**. The moment you add any custom constructor, the default one disappears and you must write it yourself.

```java
// You added a custom constructor...
public Project(String name) {
    this.name = name;
}

// ...now you MUST also add a no-args constructor (or use @NoArgsConstructor)
public Project() { }
```

### Warning: Do NOT Use @Data on Entities

`@Data` is a Lombok shortcut that generates `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, and `@RequiredArgsConstructor` all at once. **Do not use it on entities.** Here is why:

1. **`@EqualsAndHashCode` includes ALL fields by default** -- including lazy-loaded relationships. Accessing a lazy field triggers a database query, which can cause `LazyInitializationException` or N+1 query problems.

2. **`@ToString` includes ALL fields** -- same problem with lazy-loaded fields. Printing an entity can trigger unexpected database queries.

**Do this instead:**

```java
// GOOD: Use individual annotations -- full control
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project {
    // ...
}

// BAD: @Data on entities causes hidden problems
@Data  // DO NOT USE ON ENTITIES
@Entity
public class Project {
    // ...
}
```

If you need `equals()` and `hashCode()` on an entity (you will, eventually), write them manually based on the `id` field only. We will cover this when we get to entity relationships.

---

## No-Args Constructor: Why JPA Requires It

JPA and Hibernate use a technique called **reflection** to create objects. When Hibernate loads data from the database, it:

1. Creates an **empty object** using the no-args constructor
2. Sets each field's value using reflection (bypassing the setter)

If there is no no-args constructor, step 1 fails and you get an error like:

```
org.hibernate.InstantiationException: No default constructor for entity: com.example.demo.model.Project
```

**Three ways to provide a no-args constructor:**

```java
// Option 1: Do nothing (Java provides it automatically if you have NO other constructors)
@Entity
public class Project {
    // Java auto-generates public Project() { } here
}

// Option 2: Use Lombok
@NoArgsConstructor
@Entity
public class Project {
    // Lombok generates public Project() { } at compile time
}

// Option 3: Write it manually
@Entity
public class Project {
    public Project() { }
    // ... or protected Project() { } -- protected is enough for JPA
}
```

**Access level:** JPA requires the constructor to be `public` or `protected`. Using `protected` is a better practice because it signals "this constructor is for JPA, not for application code."

---

## Complete Example

Here is a complete, well-annotated entity showing all the core annotations together:

```java
package com.example.demo.model;

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

// @Entity: Tells JPA this class represents a database table
@Entity

// @Table: Sets the actual table name in the database (plural by convention)
@Table(name = "projects")

// Lombok: Generates getters, setters, and no-args constructor
@Getter
@Setter
@NoArgsConstructor
public class Project {

    // @Id: This field is the primary key
    // @GeneratedValue(IDENTITY): Database auto-generates the ID (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Required field, up to 100 characters
    @Column(nullable = false, length = 100)
    private String name;

    // Optional field (nullable = true is the default), up to 500 characters
    @Column(length = 500)
    private String description;

    // Automatically set to current time on INSERT, never changed after
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Automatically set to current time on every INSERT and UPDATE
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

**SQL generated by Hibernate (MSSQL):**

```sql
CREATE TABLE projects (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    created_at DATETIME2(6) NOT NULL,
    updated_at DATETIME2(6) NOT NULL,
    PRIMARY KEY (id)
);
```

---

## Expected Output

When you run your Spring Boot application with `spring.jpa.show-sql=true` and `ddl-auto=update`, you should see Hibernate create (or update) the table:

```
Hibernate:
    create table projects (
        id bigint identity(1,1) not null,
        created_at datetime2(6) not null,
        description nvarchar(500),
        name nvarchar(100) not null,
        updated_at datetime2(6) not null,
        primary key (id)
    )
```

If the table already exists and matches, Hibernate will not print anything for that table.

---

## Common Mistakes

### 1. Forgetting @Id

```java
// WRONG: No @Id -- JPA will throw an error on startup
@Entity
public class Project {
    private Long id;
    private String name;
}
// Error: No identifier specified for entity: Project
```

### 2. Using the wrong @Id import

```java
// WRONG: This is for MongoDB/Redis, not JPA
import org.springframework.data.annotation.Id;

// RIGHT: This is for JPA entities
import jakarta.persistence.Id;
```

### 3. Missing no-args constructor

```java
// WRONG: Custom constructor exists, no-args constructor is gone
@Entity
public class Project {
    private Long id;
    private String name;

    public Project(String name) {  // Java removes the default no-args constructor
        this.name = name;
    }
}
// Error: No default constructor for entity

// RIGHT: Add @NoArgsConstructor or write one manually
@NoArgsConstructor
@Entity
public class Project {
    // ...
    public Project(String name) {
        this.name = name;
    }
}
```

### 4. Using @Data on entities

```java
// BAD: @Data generates equals/hashCode/toString using ALL fields
@Data
@Entity
public class Project { ... }

// GOOD: Use individual Lombok annotations
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project { ... }
```

### 5. Making entity class or fields final

```java
// WRONG: Hibernate cannot create proxies for final classes
@Entity
public final class Project { ... }

// WRONG: Hibernate cannot set final fields via reflection
@Entity
public class Project {
    private final String name;  // Cannot be final
}
```

---

## Key Takeaways

- `@Entity` marks a Java class as a database table, and every entity must have `@Id` and a no-args constructor
- `@Table(name = "...")` lets you set the actual table name -- convention is to use plural names (`projects`, not `project`)
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` is the standard setup for auto-incrementing primary keys on MSSQL
- `@CreationTimestamp` + `updatable = false` for creation time; `@UpdateTimestamp` WITHOUT `updatable = false` for update time
- Use Lombok's `@Getter`, `@Setter`, and `@NoArgsConstructor` on entities -- but never `@Data`
- Always import from `jakarta.persistence` for JPA Entity annotations, not from `org.springframework.data.annotation`

---

## Related Topics

- [@Column Annotation](column-annotation.md) -- controlling column length, nullability, uniqueness, and more
- [Input Validation](../rest-api/input-validation.md) -- Bean Validation annotations(@NotNull, @Size, etc.)
- [ResponseEntity](../rest-api/response-entity.md) -- controlling HTTP responses
