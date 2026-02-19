# Database Transactions and @Transactional

## What is a Transaction?

A **transaction** is a group of database operations that must **all succeed or all fail together**. It's an "all-or-nothing" approach to database changes.

Think of it like a recipe: if you're halfway through baking a cake and run out of eggs, you don't serve half a cake. You either complete the whole recipe or start over.

In database terms, a transaction ensures that multiple SQL statements are treated as a single unit of work. Either all of them complete successfully, or none of them do.

---

## Why Does It Matter?

Without transactions, your database can end up in an **inconsistent state** when errors occur.

### Real-World Example: Bank Transfer

Imagine transferring $100 from Account A to Account B:

```
Step 1: Subtract $100 from Account A
Step 2: Add $100 to Account B
```

**Without a transaction:**
- Step 1 succeeds (Account A loses $100)
- Server crashes or network fails
- Step 2 never happens (Account B never receives the $100)
- **Result: $100 disappeared!** The bank is out of balance.

**With a transaction:**
- Step 1 executes
- Server crashes before Step 2
- Transaction is **rolled back** (Step 1 is undone)
- **Result: Account A still has the original amount.** Data is consistent.

This is called **ACID properties** (Atomicity, Consistency, Isolation, Durability) - a fundamental concept in database systems.

---

## How Transactions Work

A transaction has three possible outcomes:

1. **Commit** - All operations succeeded, changes are saved permanently
2. **Rollback** - Something went wrong, all changes are undone (as if nothing happened)
3. **Timeout** - Transaction took too long, automatically rolled back

```sql
-- SQL transaction example
BEGIN TRANSACTION;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;  -- Subtract from Account A
UPDATE accounts SET balance = balance + 100 WHERE id = 2;  -- Add to Account B

-- If both succeed:
COMMIT;  -- Save changes permanently

-- If either fails:
ROLLBACK;  -- Undo everything
```

---

## @Transactional in Spring Boot

Spring Boot provides the `@Transactional` annotation to manage transactions automatically. You don't write `BEGIN TRANSACTION`, `COMMIT`, or `ROLLBACK` - Spring handles it for you.

### Basic Usage

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Creates a new project.
     * @Transactional ensures that if save() fails, no partial data is left in the database.
     */
    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
```

**What happens behind the scenes:**

1. User calls `createProject()`
2. Spring intercepts the call (using a proxy)
3. Spring starts a transaction: `BEGIN TRANSACTION`
4. Your method runs: `projectRepository.save(project)`
5. **If no exception occurs:** Spring commits: `COMMIT`
6. **If an exception occurs:** Spring rolls back: `ROLLBACK`
7. Spring closes the database connection

---

## When to Use @Transactional

### Always Use on Write Operations

Any method that **creates, updates, or deletes** data should be transactional:

```java
@Transactional
public Project createProject(Project project) {
    return projectRepository.save(project);
}

@Transactional
public Project updateProject(Long id, Project updatedProject) {
    Project project = projectRepository.findById(id).orElseThrow();
    project.setName(updatedProject.getName());
    return projectRepository.save(project);
}

@Transactional
public void deleteProject(Long id) {
    projectRepository.deleteById(id);
}
```

### Use (readOnly = true) for Read Operations

For methods that only **read** data, use `@Transactional(readOnly = true)`:

```java
@Transactional(readOnly = true)
public List<Project> getAllProjects() {
    return projectRepository.findAll();
}

@Transactional(readOnly = true)
public Project getProjectById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
}
```

**Why mark reads as `readOnly`?**
- Performance optimization - Hibernate doesn't track entity changes (no "dirty checking")
- Database can optimize read-only queries
- Prevents accidental writes
- Makes intent clear to other developers

---

## Complex Example: Multiple Operations in One Transaction

Transactions shine when you have multiple database operations that must happen together:

```java
@Transactional
public void assignTaskToProject(Long projectId, Long taskId) {
    // Operation 1: Load the project
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

    // Operation 2: Load the task
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    // Operation 3: Check if task is already assigned
    if (task.getProject() != null) {
        throw new IllegalStateException("Task is already assigned to another project");
    }

    // Operation 4: Assign the task
    task.setProject(project);
    taskRepository.save(task);

    // Operation 5: Update project task count
    project.setTaskCount(project.getTaskCount() + 1);
    projectRepository.save(project);

    // If ANY of these operations fail (exception thrown),
    // ALL changes are rolled back - the task is never assigned,
    // and the project task count is not incremented.
}
```

**Without `@Transactional`:** If operation 4 succeeds but operation 5 fails, the task is assigned but the project's task count is wrong. Data is inconsistent.

**With `@Transactional`:** If operation 5 fails, operation 4 is also undone. Either both succeed or neither succeeds.

---

## Transaction Attributes

`@Transactional` has several configuration options:

### readOnly

```java
@Transactional(readOnly = true)  // Optimize for read operations
public List<Project> getAllProjects() {
    return projectRepository.findAll();
}
```

### timeout

```java
@Transactional(timeout = 30)  // Rollback if transaction takes longer than 30 seconds
public void longRunningOperation() {
    // ...
}
```

### rollbackFor / noRollbackFor

By default, Spring rolls back on **unchecked exceptions** (RuntimeException) but NOT on checked exceptions.

```java
// Rollback on ANY exception, including checked exceptions
@Transactional(rollbackFor = Exception.class)
public void processPayment(Payment payment) throws PaymentException {
    // ...
}

// Don't rollback for specific exceptions (rare use case)
@Transactional(noRollbackFor = MinorException.class)
public void operation() {
    // ...
}
```

### propagation

Controls how transactions interact when one transactional method calls another:

```java
// REQUIRED (default): Use existing transaction or create new one
@Transactional(propagation = Propagation.REQUIRED)
public void methodA() { }

// REQUIRES_NEW: Always create a new transaction (suspend existing one)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void auditLog() {
    // This transaction is independent - it commits even if the outer transaction rolls back
}

// NESTED: Create a nested transaction (savepoint)
@Transactional(propagation = Propagation.NESTED)
public void methodB() { }
```

**Common use case:** Audit logging with `REQUIRES_NEW`. Even if the main operation fails and rolls back, you still want to log that it was attempted.

---

## Where to Place @Transactional

**Best practice: Use on service layer methods.**

```
Controller → Service (with @Transactional) → Repository
```

**Do this:**
```java
// ✅ Service layer
@Service
public class ProjectService {
    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
```

**Don't do this:**
```java
// ❌ Don't put it on controllers
@RestController
public class ProjectController {
    @Transactional  // WRONG! Controllers shouldn't manage transactions
    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }
}

// ❌ Don't put it on repositories (they're already transactional)
@Repository
@Transactional  // Redundant - Spring Data JPA handles this
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
```

---

## Common Mistakes

### 1. Forgetting @Transactional on Write Operations

```java
// BAD: No @Transactional
public void updateUser(Long userId, String newEmail) {
    User user = userRepository.findById(userId).orElseThrow();

    user.setEmail(newEmail);  // Change is tracked
    userRepository.save(user);

    // If save() fails, the in-memory user object still has the new email
    // but the database doesn't - your app state is inconsistent
}

// GOOD: With @Transactional
@Transactional
public void updateUser(Long userId, String newEmail) {
    User user = userRepository.findById(userId).orElseThrow();
    user.setEmail(newEmail);
    userRepository.save(user);
    // If save() fails, Spring rolls back and throws an exception
}
```

### 2. Calling @Transactional Method from Same Class

```java
@Service
public class ProjectService {

    public void publicMethod() {
        this.transactionalMethod();  // ❌ Transaction NOT applied!
    }

    @Transactional
    private void transactionalMethod() {
        // Transaction doesn't work here!
    }
}
```

**Why?** Spring uses **proxies** to intercept `@Transactional` methods. When you call `this.transactionalMethod()`, you're calling the real object, not the proxy, so the transaction logic is bypassed.

**Solution:** Extract the transactional method to a separate service, or call it from outside the class.

### 3. Catching Exceptions Without Rethrowing

```java
@Transactional
public void processPayment(Payment payment) {
    try {
        paymentRepository.save(payment);
        // Some operation that might fail
        externalPaymentService.charge(payment);
    } catch (Exception e) {
        // ❌ Exception caught and silenced - transaction WON'T rollback!
        System.out.println("Payment failed");
    }
}
```

**Problem:** Spring only rolls back when an exception propagates out of the method. If you catch it, Spring thinks everything succeeded.

**Solution:** Rethrow the exception or manually trigger rollback:

```java
@Transactional
public void processPayment(Payment payment) {
    try {
        paymentRepository.save(payment);
        externalPaymentService.charge(payment);
    } catch (Exception e) {
        // Log it and rethrow
        log.error("Payment failed", e);
        throw e;  // ✅ Let Spring handle rollback
    }
}
```

### 4. Using @Transactional on Private Methods

```java
@Service
public class ProjectService {

    @Transactional  // ❌ DOESN'T WORK - private methods can't be proxied
    private void helperMethod() {
        // ...
    }
}
```

**Why?** Spring creates proxies using either JDK dynamic proxies or CGLIB. Both require methods to be `public` (or at least `protected` for CGLIB).

**Solution:** Make the method `public`.

---

## Expected Behavior

With SQL logging enabled (`spring.jpa.show-sql=true`), you can see transactions in action:

```java
@Transactional
public Project createProject(Project project) {
    return projectRepository.save(project);
}
```

**Console output:**
```
Hibernate: insert into projects (created_at, description, name, updated_at, id) values (?, ?, ?, ?, default)
```

If an exception occurs before the method completes, you'll see the transaction roll back and the insert will NOT appear in the database.

---

## Performance Considerations

### Keep Transactions Short

```java
// BAD: Long-running transaction holds database connection
@Transactional
public void processProjects() {
    List<Project> projects = projectRepository.findAll();  // Fast

    for (Project project : projects) {
        // Slow external API call - transaction is open the whole time!
        externalService.notify(project);
    }
}

// GOOD: Only wrap database operations in transaction
public void processProjects() {
    List<Project> projects = getProjects();  // Transactional

    for (Project project : projects) {
        externalService.notify(project);  // Non-transactional
    }
}

@Transactional(readOnly = true)
public List<Project> getProjects() {
    return projectRepository.findAll();
}
```

**Why?** Transactions hold database connections. If your transaction does slow external calls, you're blocking other users from accessing the database.

---

## Key Takeaways

- A **transaction** ensures multiple database operations succeed or fail together (all-or-nothing)
- Use `@Transactional` on **service layer methods** that create, update, or delete data
- Use `@Transactional(readOnly = true)` for read-only methods (performance optimization)
- Spring automatically commits on success and rolls back on exceptions
- **Import from `org.springframework.transaction.annotation.Transactional`**, not the Jakarta version
- Keep transactions short - avoid long-running operations inside transactional methods
- Don't catch exceptions without rethrowing - it prevents rollback

---

## Related Topics

- [JPA Repositories](repositories.md) - Understanding repository methods and how they interact with transactions
- [Entities and Table Mapping](entities.md) - How entity changes are tracked within transactions
- [Service Layer Best Practices](../spring-boot/service-layer.md) - Where and how to use @Transactional
