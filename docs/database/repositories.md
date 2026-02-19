# Spring Data JPA Repositories

## What is a Repository?

A **repository** is a layer that sits between your service layer and the database. It handles all database operations (create, read, update, delete) without you writing any SQL code.

Think of it as a **database assistant**: You tell it "save this project" or "find project with ID 5", and it handles all the SQL behind the scenes.

---

## Why Does It Matter?

Without a repository, you would write SQL for every single database operation:

```java
// Without repository - manual SQL (painful and error-prone)
String sql = "INSERT INTO projects (name, description, created_at, updated_at) VALUES (?, ?, ?, ?)";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, project.getName());
stmt.setString(2, project.getDescription());
stmt.setTimestamp(3, Timestamp.valueOf(project.getCreatedAt()));
stmt.setTimestamp(4, Timestamp.valueOf(project.getUpdatedAt()));
stmt.executeUpdate();
```

With Spring Data JPA repository, you just call a method:

```java
// With repository - clean and simple
projectRepository.save(project);
```

**In professional environments:** 90%+ of Java backend applications use Spring Data JPA repositories. It's the industry standard.

---

## How to Create a Repository

### Step 1: Create an Interface (Not a Class!)

```java
package com.example.demo.repository;

import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
                                                          ^^^^^^  ^^^^
                                                          Entity  ID Type
}
```

**Key points:**
- It's an **interface**, not a class
- No `@Repository` annotation needed (Spring auto-detects it)
- Extends `JpaRepository<EntityType, IdType>`
- You don't write any implementation - Spring creates it at runtime

### Step 2: Inject It Into Your Service

```java
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    // Constructor injection - Spring auto-provides the repository
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);  // Use the repository
    }
}
```

---

## Built-In Methods

When you extend `JpaRepository<Project, Long>`, you automatically get **18+ methods for free**. You don't write any code - Spring Data JPA generates the SQL for you.

### Create/Update Methods

| Method | What It Does | When to Use |
|---|---|---|
| `save(Project project)` | Insert new or update existing | Creating or updating a single entity |
| `saveAll(List<Project> projects)` | Insert/update multiple | Batch saving multiple entities |
| `saveAndFlush(Project project)` | Save and immediately flush to database | When you need the ID immediately |

### Read Methods

| Method | What It Does | Return Type |
|---|---|---|
| `findById(Long id)` | Find by primary key | `Optional<Project>` |
| `findAll()` | Get all records | `List<Project>` |
| `findAll(Sort sort)` | Get all, sorted | `List<Project>` |
| `findAll(Pageable pageable)` | Get paginated results | `Page<Project>` |
| `findAllById(List<Long> ids)` | Find multiple by IDs | `List<Project>` |
| `count()` | Count total records | `long` |
| `existsById(Long id)` | Check if record exists | `boolean` |

### Delete Methods

| Method | What It Does |
|---|---|
| `deleteById(Long id)` | Delete by ID |
| `delete(Project project)` | Delete the entity |
| `deleteAll()` | Delete all records (use with caution!) |
| `deleteAll(List<Project> projects)` | Delete multiple entities |
| `deleteAllInBatch()` | Delete all in a single SQL statement (faster) |

---

## Understanding save() Method

The `save()` method is **smart** - it handles both **insert** (new entity) and **update** (existing entity).

### How save() Decides: Insert vs Update

```java
// Scenario 1: ID is null → INSERT
Project newProject = new Project();
newProject.setName("DevFlow");  // ID is null
Project saved = projectRepository.save(newProject);
// SQL: INSERT INTO projects (name, description, created_at, updated_at) VALUES (?, ?, ?, ?)
// Result: saved.getId() returns the generated ID (e.g., 1)

// Scenario 2: ID exists → UPDATE
Project existing = projectRepository.findById(1L).orElseThrow();
existing.setName("DevFlow v2");  // ID is 1
projectRepository.save(existing);
// SQL: UPDATE projects SET name = ?, updated_at = ? WHERE id = ?
```

**Rule:** If the entity has an ID (non-null for Long/Integer, non-zero for primitives), `save()` performs UPDATE. Otherwise, it performs INSERT.

### What Object Does save() Accept?

**Answer:** An object of the **entity type** that the repository manages.

```java
// ProjectRepository manages <Project, Long>
public interface ProjectRepository extends JpaRepository<Project, Long> { }

// So save() accepts a Project object
Project project = new Project();
projectRepository.save(project);  // ✅ Correct

// You CANNOT pass other entity types
Task task = new Task();
projectRepository.save(task);  // ❌ COMPILE ERROR - expects Project, not Task
```

**Generic signature:**
```java
<S extends T> S save(S entity)
```

Translation: "Give me an entity of type `T` (or a subclass of `T`), and I'll return the same type back to you with any generated values filled in."

### Why Does save() Return Something?

The returned object has **generated values** filled in:

1. **Generated ID** (when creating a new entity)
2. **Updated timestamps** (when `@UpdateTimestamp` is used)
3. **Database-generated fields** (defaults, computed columns)

```java
Project project = new Project();
project.setName("DevFlow");

// Before save
System.out.println(project.getId());         // null (not saved yet)
System.out.println(project.getCreatedAt());  // null

// After save
Project saved = projectRepository.save(project);
System.out.println(saved.getId());           // 1 (database generated)
System.out.println(saved.getCreatedAt());    // 2024-02-18T10:30:00 (Hibernate set this)

// If you DON'T capture the return value, you won't have the generated ID:
projectRepository.save(project);
System.out.println(project.getId());  // Still null! (You didn't capture the returned object)
```

**Best practice:** Always capture the return value of `save()`:

```java
// ✅ Good - capture the returned entity
Project saved = projectRepository.save(project);
return saved;  // Controller can return this with the generated ID

// ❌ Bad - lose the generated values
projectRepository.save(project);
return project;  // This has null ID!
```

---

## Custom Query Methods

Spring Data JPA can generate queries based on method names:

```java
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find projects by name (exact match)
    List<Project> findByName(String name);
    // SQL: SELECT * FROM projects WHERE name = ?

    // Find projects containing a keyword in the name
    List<Project> findByNameContaining(String keyword);
    // SQL: SELECT * FROM projects WHERE name LIKE %keyword%

    // Find projects created after a date
    List<Project> findByCreatedAtAfter(LocalDateTime date);
    // SQL: SELECT * FROM projects WHERE created_at > ?

    // Combine multiple conditions
    List<Project> findByNameAndCreatedAtAfter(String name, LocalDateTime date);
    // SQL: SELECT * FROM projects WHERE name = ? AND created_at > ?

    // Check if a project with this name exists
    boolean existsByName(String name);
    // SQL: SELECT COUNT(*) FROM projects WHERE name = ?

    // Count projects with a specific name
    long countByName(String name);
    // SQL: SELECT COUNT(*) FROM projects WHERE name = ?
}
```

**Keywords you can use:**

| Keyword | SQL Equivalent | Example |
|---|---|---|
| `findBy...` | SELECT WHERE | `findByName(String name)` |
| `...And...` | AND | `findByNameAndDescription(...)` |
| `...Or...` | OR | `findByNameOrDescription(...)` |
| `...Containing...` | LIKE %value% | `findByNameContaining(String keyword)` |
| `...StartingWith...` | LIKE value% | `findByNameStartingWith(String prefix)` |
| `...EndingWith...` | LIKE %value | `findByNameEndingWith(String suffix)` |
| `...IgnoreCase` | LOWER(...) = LOWER(...) | `findByNameIgnoreCase(String name)` |
| `...OrderBy...Asc` | ORDER BY ... ASC | `findByNameOrderByCreatedAtAsc()` |
| `...OrderBy...Desc` | ORDER BY ... DESC | `findByNameOrderByCreatedAtDesc()` |
| `...After...` | > | `findByCreatedAtAfter(LocalDateTime date)` |
| `...Before...` | < | `findByCreatedAtBefore(LocalDateTime date)` |
| `...Between...` | BETWEEN | `findByCreatedAtBetween(LocalDateTime start, LocalDateTime end)` |
| `...IsNull` | IS NULL | `findByDescriptionIsNull()` |
| `...IsNotNull` | IS NOT NULL | `findByDescriptionIsNotNull()` |

---

## Custom Queries with @Query

For complex queries, use `@Query`:

```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // JPQL query (uses entity names, not table names)
    @Query("SELECT p FROM Project p WHERE p.name LIKE %:keyword%")
    List<Project> searchByKeyword(@Param("keyword") String keyword);

    // Native SQL query (uses table/column names)
    @Query(value = "SELECT * FROM projects WHERE name LIKE %:keyword%", nativeQuery = true)
    List<Project> searchByKeywordNative(@Param("keyword") String keyword);

    // Query with pagination
    @Query("SELECT p FROM Project p WHERE p.createdAt > :date")
    Page<Project> findRecentProjects(@Param("date") LocalDateTime date, Pageable pageable);

    // Update query
    @Modifying  // Required for UPDATE/DELETE queries
    @Query("UPDATE Project p SET p.name = :newName WHERE p.id = :id")
    void updateProjectName(@Param("id") Long id, @Param("newName") String newName);
}
```

**When to use @Query:**
- Complex joins or subqueries
- Performance optimization (custom projections)
- Native SQL features not supported by method naming

---

## Pagination and Sorting

For lists that could be large, always use pagination:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Page<Project> getAllProjects(int page, int size, String sortBy) {
        // Create a pageable request: page 0, size 10, sorted by name ascending
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return projectRepository.findAll(pageable);
    }
}
```

**Page object provides:**
- `getContent()` - List of entities on this page
- `getTotalElements()` - Total count across all pages
- `getTotalPages()` - How many pages exist
- `getNumber()` - Current page number
- `getSize()` - Page size
- `hasNext()` - Is there a next page?
- `hasPrevious()` - Is there a previous page?

---

## Return Types: Optional vs List

### Use Optional for Single Results

```java
// ✅ Good - returns Optional<Project>
Optional<Project> findById(Long id);

// Usage in service
@Transactional(readOnly = true)
public Project getProjectById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
}
```

**Why Optional?** It forces you to handle the "not found" case explicitly. You can't accidentally cause a `NullPointerException`.

### Lists Never Return Null

```java
// Always returns a list (empty if no results, never null)
List<Project> findAll();
List<Project> findByName(String name);

// Safe to use without null check
List<Project> projects = projectRepository.findAll();
if (projects.isEmpty()) {
    // No projects found
}
```

---

## Complete CRUD Example

```java
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // CREATE
    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // READ - single
    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    // READ - all (with pagination)
    @Transactional(readOnly = true)
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    // UPDATE
    @Transactional
    public Project updateProject(Long id, Project updatedProject) {
        Project existing = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());

        return projectRepository.save(existing);  // save() performs UPDATE
    }

    // DELETE
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
```

---

## Common Mistakes

### 1. Not Using Optional Properly

```java
// ❌ Bad - can cause NullPointerException
Project project = projectRepository.findById(id).get();

// ✅ Good - handles "not found" case
Project project = projectRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
```

### 2. Using findAll() Without Pagination

```java
// ❌ Bad - loads ALL rows into memory (can crash with large datasets)
List<Project> projects = projectRepository.findAll();

// ✅ Good - paginated (load 10 at a time)
Page<Project> projects = projectRepository.findAll(PageRequest.of(0, 10));
```

### 3. Not Capturing save() Return Value

```java
// ❌ Bad - loses the generated ID
Project project = new Project();
project.setName("DevFlow");
projectRepository.save(project);
return project;  // ID is null!

// ✅ Good - capture the returned entity with generated values
Project project = new Project();
project.setName("DevFlow");
Project saved = projectRepository.save(project);
return saved;  // ID is populated
```

### 4. Forgetting @Transactional on Service Methods

```java
// ❌ Bad - no transaction context
public Project updateProject(Long id, Project updatedProject) {
    Project project = projectRepository.findById(id).orElseThrow();
    project.setName(updatedProject.getName());
    return projectRepository.save(project);
}

// ✅ Good - wrapped in transaction
@Transactional
public Project updateProject(Long id, Project updatedProject) {
    Project project = projectRepository.findById(id).orElseThrow();
    project.setName(updatedProject.getName());
    return projectRepository.save(project);
}
```

---

## Key Takeaways

- Repositories are **interfaces** that extend `JpaRepository<EntityType, IdType>`
- You get 18+ built-in methods for free (save, find, delete, etc.) without writing any code
- `save()` is smart - it performs INSERT for new entities (ID is null) and UPDATE for existing entities (ID exists)
- Always capture the return value of `save()` to get generated IDs and timestamps
- Use `Optional` for single results (`findById`) and handle the "not found" case properly
- Always use pagination (`Pageable`) for methods that return lists - never load all rows into memory
- Custom query methods can be created by method naming convention or with `@Query`
- Always use `@Transactional` on service methods that call repository methods

---

## Related Topics

- [Entities and Table Mapping](entities.md) - Understanding what repositories manage
- [Transactions](transactions.md) - Why repositories should be called within `@Transactional` methods
- [Pagination and Sorting](pagination-and-sorting.md) - How to handle large result sets
- [Custom Queries](custom-queries.md) - Advanced querying with @Query and JPQL
