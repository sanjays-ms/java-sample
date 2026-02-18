# Spring Boot Annotations Reference -- Import Guide

## What is This Document?

A quick reference for **which annotation to import from which package**. One of the most common beginner mistakes is importing the wrong annotation -- for example, using `org.springframework.data.annotation.Id` instead of `jakarta.persistence.Id` for JPA entities.

This document lists every commonly used annotation in Spring Boot, organized by category, with the correct import path and common wrong imports to avoid.

---

## How to Use This Reference

1. Find the annotation you want to use in the tables below
2. Copy the **Correct Import** path
3. Avoid the **Wrong Imports** (they exist but are for different purposes)
4. Read the **Notes** column for context on when to use it

---

## JPA Entity Annotations (Database Mapping)

These annotations map Java classes to database tables. Always import from `jakarta.persistence.*`.

| Annotation | Correct Import | Wrong Import (Avoid) | Notes |
|---|---|---|---|
| `@Entity` | `jakarta.persistence.Entity` | `org.hibernate.annotations.Entity` | Marks a class as a JPA entity |
| `@Table` | `jakarta.persistence.Table` | - | Customizes table name, schema, unique constraints |
| `@Id` | `jakarta.persistence.Id` | `org.springframework.data.annotation.Id` | Marks the primary key (for JPA entities ONLY) |
| `@GeneratedValue` | `jakarta.persistence.GeneratedValue` | - | Auto-generates primary key values |
| `@Column` | `jakarta.persistence.Column` | - | Customizes column properties (length, nullable, etc.) |
| `@Transient` | `jakarta.persistence.Transient` | - | Excludes a field from being mapped to the database |
| `@Enumerated` | `jakarta.persistence.Enumerated` | - | Maps Java enums to database columns |
| `@Lob` | `jakarta.persistence.Lob` | - | Maps large objects (BLOB, CLOB) |
| `@Temporal` | `jakarta.persistence.Temporal` | - | Maps Date/Calendar (older API, use LocalDateTime instead) |

### JPA Relationship Annotations

| Annotation | Correct Import | Notes |
|---|---|---|
| `@OneToOne` | `jakarta.persistence.OneToOne` | One entity relates to exactly one other entity |
| `@OneToMany` | `jakarta.persistence.OneToMany` | One entity relates to many others (e.g., Project → Tasks) |
| `@ManyToOne` | `jakarta.persistence.ManyToOne` | Many entities relate to one (e.g., Task → Project) |
| `@ManyToMany` | `jakarta.persistence.ManyToMany` | Many entities relate to many (e.g., Tasks ↔ Labels) |
| `@JoinColumn` | `jakarta.persistence.JoinColumn` | Specifies the foreign key column name |
| `@JoinTable` | `jakarta.persistence.JoinTable` | Configures the join table for @ManyToMany |

---

## Hibernate-Specific Annotations

These are extras provided by Hibernate (not part of standard JPA). Import from `org.hibernate.annotations.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@CreationTimestamp` | `org.hibernate.annotations.CreationTimestamp` | Automatically sets timestamp on entity creation |
| `@UpdateTimestamp` | `org.hibernate.annotations.UpdateTimestamp` | Automatically sets timestamp on entity update |
| `@Formula` | `org.hibernate.annotations.Formula` | Maps a computed/calculated field using SQL |
| `@SQLDelete` | `org.hibernate.annotations.SQLDelete` | Custom SQL for soft deletes |
| `@Where` | `org.hibernate.annotations.Where` | Adds a WHERE clause to all queries for this entity |

**Note:** Hibernate annotations are specific to Hibernate. If you switch to EclipseLink or another JPA provider, these will not work. Stick to standard JPA annotations when possible.

---

## Spring Stereotype Annotations (Layer Markers)

These tell Spring which layer a class belongs to. They all enable **component scanning** (Spring creates a bean automatically). Import from `org.springframework.stereotype.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Component` | `org.springframework.stereotype.Component` | Generic Spring-managed component (rarely used directly) |
| `@Controller` | `org.springframework.stereotype.Controller` | MVC controller (returns views, not REST) |
| `@RestController` | `org.springframework.web.bind.annotation.RestController` | REST controller (returns JSON/XML, not views) |
| `@Service` | `org.springframework.stereotype.Service` | Business logic layer (service classes) |
| `@Repository` | `org.springframework.stereotype.Repository` | Data access layer (DAO, repositories) |

**Note:** In Spring Boot, you almost never use `@Component` directly. Use the specific stereotype (`@Service`, `@Repository`, `@RestController`) instead -- it makes the code self-documenting.

**Why `@RestController` has a different package:** It's a composed annotation (`@Controller` + `@ResponseBody`), so it lives in the web package.

---

## Spring Web Annotations (REST Controllers)

These handle HTTP requests and responses. Import from `org.springframework.web.bind.annotation.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@RequestMapping` | `org.springframework.web.bind.annotation.RequestMapping` | Generic request mapping (can handle any HTTP method) |
| `@GetMapping` | `org.springframework.web.bind.annotation.GetMapping` | Maps HTTP GET requests (read data) |
| `@PostMapping` | `org.springframework.web.bind.annotation.PostMapping` | Maps HTTP POST requests (create data) |
| `@PutMapping` | `org.springframework.web.bind.annotation.PutMapping` | Maps HTTP PUT requests (full update) |
| `@PatchMapping` | `org.springframework.web.bind.annotation.PatchMapping` | Maps HTTP PATCH requests (partial update) |
| `@DeleteMapping` | `org.springframework.web.bind.annotation.DeleteMapping` | Maps HTTP DELETE requests (delete data) |
| `@RequestBody` | `org.springframework.web.bind.annotation.RequestBody` | Maps HTTP request body to a Java object |
| `@RequestParam` | `org.springframework.web.bind.annotation.RequestParam` | Maps query parameters (?name=value) |
| `@PathVariable` | `org.springframework.web.bind.annotation.PathVariable` | Maps URL path segments (/projects/{id}) |
| `@ResponseStatus` | `org.springframework.web.bind.annotation.ResponseStatus` | Sets the HTTP status code for a method |
| `@ResponseBody` | `org.springframework.web.bind.annotation.ResponseBody` | Tells Spring to serialize return value to JSON/XML |
| `@RestControllerAdvice` | `org.springframework.web.bind.annotation.RestControllerAdvice` | Global exception handler for REST controllers |
| `@ExceptionHandler` | `org.springframework.web.bind.annotation.ExceptionHandler` | Handles specific exceptions in controllers |
| `@CrossOrigin` | `org.springframework.web.bind.annotation.CrossOrigin` | Enables CORS for frontend access |

---

## Spring Dependency Injection Annotations

These control how Spring injects dependencies. Import from `org.springframework.beans.factory.annotation.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Autowired` | `org.springframework.beans.factory.annotation.Autowired` | Injects a dependency (prefer constructor injection) |
| `@Qualifier` | `org.springframework.beans.factory.annotation.Qualifier` | Specifies which bean to inject when multiple exist |
| `@Value` | `org.springframework.beans.factory.annotation.Value` | Injects values from application.properties |

**Note:** In modern Spring Boot, you should prefer **constructor injection** over `@Autowired` field injection. Lombok's `@RequiredArgsConstructor` makes this clean.

---

## Spring Configuration Annotations

These configure the Spring application. Import from `org.springframework.context.annotation.*` or `org.springframework.boot.autoconfigure.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Configuration` | `org.springframework.context.annotation.Configuration` | Marks a class as a configuration class (defines beans) |
| `@Bean` | `org.springframework.context.annotation.Bean` | Declares a method as a bean factory method |
| `@ComponentScan` | `org.springframework.context.annotation.ComponentScan` | Tells Spring where to scan for components |
| `@EnableAutoConfiguration` | `org.springframework.boot.autoconfigure.EnableAutoConfiguration` | Enables Spring Boot auto-configuration |
| `@SpringBootApplication` | `org.springframework.boot.autoconfigure.SpringBootApplication` | Combines @Configuration, @EnableAutoConfiguration, @ComponentScan |
| `@Profile` | `org.springframework.context.annotation.Profile` | Activates beans only in specific environments (dev, prod) |
| `@PropertySource` | `org.springframework.context.annotation.PropertySource` | Loads properties from a specific file |

**Note:** `@SpringBootApplication` is the main annotation on your entry point class (the one with `main()`). It's a convenience that combines three annotations.

---

## Spring Data JPA Repository Annotations

These are used on repository interfaces. Import from `org.springframework.data.jpa.repository.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Query` | `org.springframework.data.jpa.repository.Query` | Defines a custom JPQL or native SQL query |
| `@Modifying` | `org.springframework.data.jpa.repository.Modifying` | Marks a query as an INSERT/UPDATE/DELETE |
| `@EntityGraph` | `org.springframework.data.jpa.repository.EntityGraph` | Solves N+1 problem by specifying eager loading |

**Note:** The `@Id` annotation for MongoDB/Redis repositories is `org.springframework.data.annotation.Id`, NOT the JPA one. Use the JPA `jakarta.persistence.Id` for SQL databases.

---

## Transaction Management Annotations

These control database transactions. Import from `org.springframework.transaction.annotation.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Transactional` | `org.springframework.transaction.annotation.Transactional` | Wraps method in a database transaction (commit/rollback) |
| `@EnableTransactionManagement` | `org.springframework.transaction.annotation.EnableTransactionManagement` | Enables transaction management (auto-enabled in Spring Boot) |

**Best practice:** Use `@Transactional` on **service methods**, not on repository or controller methods.

---

## Bean Validation Annotations (Input Validation)

These validate input data. Import from `jakarta.validation.constraints.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Valid` | `jakarta.validation.Valid` | Triggers validation on a method parameter or field |
| `@NotNull` | `jakarta.validation.constraints.NotNull` | Field must not be null (but can be empty string) |
| `@NotEmpty` | `jakarta.validation.constraints.NotEmpty` | String/Collection must not be null or empty |
| `@NotBlank` | `jakarta.validation.constraints.NotBlank` | String must not be null, empty, or whitespace-only |
| `@Size` | `jakarta.validation.constraints.Size` | String/Collection must be within min/max size |
| `@Min` | `jakarta.validation.constraints.Min` | Number must be >= specified value |
| `@Max` | `jakarta.validation.constraints.Max` | Number must be <= specified value |
| `@Email` | `jakarta.validation.constraints.Email` | String must be a valid email format |
| `@Pattern` | `jakarta.validation.constraints.Pattern` | String must match a regex pattern |
| `@Positive` | `jakarta.validation.constraints.Positive` | Number must be > 0 |
| `@PositiveOrZero` | `jakarta.validation.constraints.PositiveOrZero` | Number must be >= 0 |
| `@Negative` | `jakarta.validation.constraints.Negative` | Number must be < 0 |
| `@Past` | `jakarta.validation.constraints.Past` | Date must be in the past |
| `@Future` | `jakarta.validation.constraints.Future` | Date must be in the future |

**Important:** These only work when you use `@Valid` in your controller. They do NOT automatically validate -- you must trigger validation.

---

## Lombok Annotations

These generate boilerplate code at compile time. Import from `lombok.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Getter` | `lombok.Getter` | Generates getter methods for all fields |
| `@Setter` | `lombok.Setter` | Generates setter methods for all fields |
| `@NoArgsConstructor` | `lombok.NoArgsConstructor` | Generates a no-argument constructor |
| `@AllArgsConstructor` | `lombok.AllArgsConstructor` | Generates a constructor with all fields |
| `@RequiredArgsConstructor` | `lombok.RequiredArgsConstructor` | Generates constructor for final/non-null fields |
| `@ToString` | `lombok.ToString` | Generates toString() method |
| `@EqualsAndHashCode` | `lombok.EqualsAndHashCode` | Generates equals() and hashCode() methods |
| `@Data` | `lombok.Data` | Combines @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor |
| `@Builder` | `lombok.Builder` | Generates a builder pattern for the class |
| `@Slf4j` | `lombok.extern.slf4j.Slf4j` | Creates a logger field: `private static final Logger log` |

**Warning:** Do NOT use `@Data` on JPA entities. It generates `equals()`/`hashCode()` using ALL fields, including lazy-loaded relationships, causing performance issues and errors.

**For entities, use this instead:**
```java
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project { ... }
```

---

## Testing Annotations

These are used in JUnit 5 and Spring Boot tests. Multiple packages.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Test` | `org.junit.jupiter.api.Test` | Marks a method as a test |
| `@BeforeEach` | `org.junit.jupiter.api.BeforeEach` | Runs before each test method |
| `@AfterEach` | `org.junit.jupiter.api.AfterEach` | Runs after each test method |
| `@BeforeAll` | `org.junit.jupiter.api.BeforeAll` | Runs once before all tests (must be static) |
| `@AfterAll` | `org.junit.jupiter.api.AfterAll` | Runs once after all tests (must be static) |
| `@Disabled` | `org.junit.jupiter.api.Disabled` | Disables a test (it will not run) |
| `@DisplayName` | `org.junit.jupiter.api.DisplayName` | Sets a custom name for the test |
| `@SpringBootTest` | `org.springframework.boot.test.context.SpringBootTest` | Loads full Spring context for integration tests |
| `@WebMvcTest` | `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest` | Tests controllers with mocked dependencies |
| `@DataJpaTest` | `org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest` | Tests JPA repositories with in-memory database |
| `@MockBean` | `org.springframework.boot.test.mock.mockito.MockBean` | Creates a Mockito mock and adds it to Spring context |
| `@Mock` | `org.mockito.Mock` | Creates a Mockito mock (plain Mockito, not Spring) |
| `@InjectMocks` | `org.mockito.InjectMocks` | Creates an instance and injects @Mock dependencies |

---

## Asynchronous Processing Annotations

Import from `org.springframework.scheduling.annotation.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Async` | `org.springframework.scheduling.annotation.Async` | Runs a method asynchronously (in background thread) |
| `@EnableAsync` | `org.springframework.scheduling.annotation.EnableAsync` | Enables async method execution (add to config class) |
| `@Scheduled` | `org.springframework.scheduling.annotation.Scheduled` | Runs a method on a schedule (cron job) |
| `@EnableScheduling` | `org.springframework.scheduling.annotation.EnableScheduling` | Enables scheduled tasks |

---

## Caching Annotations

Import from `org.springframework.cache.annotation.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@Cacheable` | `org.springframework.cache.annotation.Cacheable` | Caches method result (reads from cache if available) |
| `@CachePut` | `org.springframework.cache.annotation.CachePut` | Updates cache with method result |
| `@CacheEvict` | `org.springframework.cache.annotation.CacheEvict` | Removes entries from cache |
| `@EnableCaching` | `org.springframework.cache.annotation.EnableCaching` | Enables caching (add to config class) |

---

## Security Annotations (Spring Security)

Import from `org.springframework.security.access.prepost.*` or `org.springframework.security.config.annotation.web.configuration.*`.

| Annotation | Correct Import | Notes |
|---|---|---|
| `@EnableWebSecurity` | `org.springframework.security.config.annotation.web.configuration.EnableWebSecurity` | Enables Spring Security |
| `@PreAuthorize` | `org.springframework.security.access.prepost.PreAuthorize` | Checks authorization before method execution |
| `@PostAuthorize` | `org.springframework.security.access.prepost.PostAuthorize` | Checks authorization after method execution |
| `@Secured` | `org.springframework.security.access.annotation.Secured` | Restricts access by role (older, prefer @PreAuthorize) |

---

## Common Import Mistakes -- What NOT to Do

### 1. Using the Wrong @Id

```java
// WRONG: This is for MongoDB, Redis, Cassandra (non-JPA databases)
import org.springframework.data.annotation.Id;

// RIGHT: This is for JPA entities with SQL databases
import jakarta.persistence.Id;
```

**How to remember:** If your class has `@Entity`, use `jakarta.persistence.Id`.

### 2. Using Old javax.* Instead of jakarta.*

Java EE became Jakarta EE in 2019. Spring Boot 3+ uses `jakarta.*` packages. Older Spring Boot versions (2.x) used `javax.*`.

```java
// WRONG (old Java EE package from Spring Boot 2.x)
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

// RIGHT (Jakarta EE package for Spring Boot 3+)
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
```

**How to remember:** If you're using Spring Boot 3 or newer (you are -- you have Spring Boot 4.0.2), always use `jakarta.*`, never `javax.*`.

### 3. Using org.hibernate.annotations.Entity

```java
// WRONG: This is deprecated Hibernate-specific annotation
import org.hibernate.annotations.Entity;

// RIGHT: Use standard JPA annotation
import jakarta.persistence.Entity;
```

### 4. Mixing Spring @Transactional with Jakarta @Transactional

```java
// WRONG: This is the Jakarta version (less flexible)
import jakarta.transaction.Transactional;

// RIGHT: Use Spring's version (more features)
import org.springframework.transaction.annotation.Transactional;
```

Spring's `@Transactional` has more configuration options (propagation, isolation, rollback rules) than the Jakarta one.

---

## Quick Reference: Package Prefixes

Here's a quick way to remember which package to import from:

| Package Prefix | What It's For |
|---|---|
| `jakarta.persistence.*` | JPA entities and table mapping |
| `jakarta.validation.*` | Bean Validation (input validation) |
| `org.hibernate.annotations.*` | Hibernate-specific extras (@CreationTimestamp, etc.) |
| `org.springframework.stereotype.*` | Layer markers (@Service, @Repository, @Component, @Controller) |
| `org.springframework.web.bind.annotation.*` | REST controllers and HTTP handling |
| `org.springframework.boot.autoconfigure.*` | Spring Boot configuration (@SpringBootApplication) |
| `org.springframework.data.jpa.repository.*` | Spring Data JPA repository features |
| `org.springframework.transaction.annotation.*` | Transaction management |
| `org.springframework.context.annotation.*` | Bean configuration (@Configuration, @Bean) |
| `org.springframework.beans.factory.annotation.*` | Dependency injection (@Autowired, @Value) |
| `lombok.*` | Lombok code generation (@Getter, @Setter, @Data, etc.) |
| `org.junit.jupiter.api.*` | JUnit 5 testing annotations |
| `org.mockito.*` | Mockito mocking framework |

---

## Key Takeaways

- **JPA entities:** Always use `jakarta.persistence.*` annotations, and use `jakarta.persistence.Id` for the primary key
- **Spring Boot 3+:** Use `jakarta.*` packages, not the old `javax.*` packages
- **Layer markers:** `@Service`, `@Repository`, `@RestController` -- all from `org.springframework.*`
- **Validation:** `jakarta.validation.constraints.*` for Bean Validation, triggered by `@Valid`
- **Lombok on entities:** Use `@Getter`, `@Setter`, `@NoArgsConstructor` -- never `@Data`
- **Transactions:** Use `org.springframework.transaction.annotation.Transactional`, not the Jakarta one
- **When in doubt:** Look at the package name in the auto-import suggestion -- if it says `javax.*` or `org.hibernate.annotations.Entity`, it's probably wrong

---

## Related Topics

- [Entities and Table Mapping](../database/entities.md) -- deep dive into JPA entity annotations
- [@Column Annotation](../database/column-annotation.md) -- controlling database column properties
- [Input Validation](../rest-api/input-validation.md) -- using Bean Validation annotations
- [Dependency Injection](dependency-injection.md) -- understanding @Autowired, @Service, and constructor injection
