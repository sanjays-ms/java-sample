# Spring Boot + MSSQL -- Learning Documentation

A collection of beginner-friendly documentation covering Java, Spring Boot, and Microsoft SQL Server. Written in plain language with real code examples, expected outputs, common mistakes, and better alternatives.

Anyone learning Spring Boot should be able to pick up any document here and follow along regardless of their current skill level.

---

## How to Use This Documentation

- Documents are organized by topic, not by difficulty or order
- Each document is standalone -- read whichever topic you need
- Every document includes working code examples with inline comments
- Common mistakes and pitfalls are called out with "Do this / Don't do this" comparisons
- Better alternatives and industry practices are noted where they exist
- Expected console output or API responses are shown so you know what to look for

---

## Table of Contents

### Spring Boot Fundamentals

| # | Topic | File |
|---|---|---|
| 1 | What is Spring Boot | [spring-boot-overview.md](spring-boot/spring-boot-overview.md) |
| 2 | Project Structure | [project-structure.md](spring-boot/project-structure.md) |
| 3 | Annotations Reference (Import Guide) | [annotations-reference.md](spring-boot/annotations-reference.md) |
| 4 | Application Configuration | [application-configuration.md](spring-boot/application-configuration.md) |
| 5 | Dependency Injection and Beans | [dependency-injection.md](spring-boot/dependency-injection.md) |
| 6 | Profiles and Environment Config | [profiles-and-environment.md](spring-boot/profiles-and-environment.md) |

### Maven

| # | Topic | File |
|---|---|---|
| 1 | Maven Fundamentals | [maven-fundamentals.md](maven/maven-fundamentals.md) |
| 2 | pom.xml Explained | [pom-xml-explained.md](maven/pom-xml-explained.md) |
| 3 | Common Maven Commands | [maven-commands.md](maven/maven-commands.md) |

### REST APIs

| # | Topic | File |
|---|---|---|
| 1 | REST API Basics | [rest-api-basics.md](rest-api/rest-api-basics.md) |
| 2 | Controllers and Request Mapping | [controllers.md](rest-api/controllers.md) |
| 3 | HTTP Methods and Status Codes | [http-methods-and-status-codes.md](rest-api/http-methods-and-status-codes.md) |
| 4 | Request and Response Handling | [request-response-handling.md](rest-api/request-response-handling.md) |
| 5 | ResponseEntity | [response-entity.md](rest-api/response-entity.md) |
| 6 | DTOs and Data Transfer | [dtos.md](rest-api/dtos.md) |
| 7 | Input Validation | [input-validation.md](rest-api/input-validation.md) |
| 8 | Pagination and Sorting | [pagination-and-sorting.md](rest-api/pagination-and-sorting.md) |
| 9 | API Versioning | [api-versioning.md](rest-api/api-versioning.md) |

### Database and JPA

| # | Topic | File |
|---|---|---|
| 1 | Relational Database Basics | [database-basics.md](database/database-basics.md) |
| 2 | JPA and Hibernate | [jpa-and-hibernate.md](database/jpa-and-hibernate.md) |
| 3 | Entities and Table Mapping | [entities.md](database/entities.md) |
| 4 | @Column Annotation | [column-annotation.md](database/column-annotation.md) |
| 5 | Repositories and Queries | [repositories.md](database/repositories.md) |
| 6 | Entity Relationships | [entity-relationships.md](database/entity-relationships.md) |
| 7 | N+1 Problem and Solutions | [n-plus-one-problem.md](database/n-plus-one-problem.md) |
| 8 | Database Migrations (Flyway) | [database-migrations.md](database/database-migrations.md) |

### Error Handling

| # | Topic | File |
|---|---|---|
| 1 | Exception Handling in Spring Boot | [exception-handling.md](error-handling/exception-handling.md) |
| 2 | Global Exception Handler | [global-exception-handler.md](error-handling/global-exception-handler.md) |
| 3 | Custom Exceptions | [custom-exceptions.md](error-handling/custom-exceptions.md) |

### Security

| # | Topic | File |
|---|---|---|
| 1 | Spring Security Basics | [spring-security-basics.md](security/spring-security-basics.md) |
| 2 | JWT Authentication | [jwt-authentication.md](security/jwt-authentication.md) |
| 3 | Role-Based Access Control | [role-based-access.md](security/role-based-access.md) |

### Testing

| # | Topic | File |
|---|---|---|
| 1 | Unit Testing with JUnit 5 | [unit-testing.md](testing/unit-testing.md) |
| 2 | Mocking with Mockito | [mocking-with-mockito.md](testing/mocking-with-mockito.md) |
| 3 | Integration Testing | [integration-testing.md](testing/integration-testing.md) |

### Performance and Async

| # | Topic | File |
|---|---|---|
| 1 | Async Processing | [async-processing.md](performance/async-processing.md) |
| 2 | Caching | [caching.md](performance/caching.md) |
| 3 | Performance Optimization | [performance-optimization.md](performance/performance-optimization.md) |

### Java Concepts

| # | Topic | File |
|---|---|---|
| 1 | OOP Basics | [oop-basics.md](java-concepts/oop-basics.md) |
| 2 | Generics | [generics.md](java-concepts/generics.md) |
| 3 | Optional | [optional.md](java-concepts/optional.md) |
| 4 | Streams and Lambdas | [streams-and-lambdas.md](java-concepts/streams-and-lambdas.md) |
| 5 | Collections | [collections.md](java-concepts/collections.md) |
| 6 | Exception Handling (Java) | [exception-handling-java.md](java-concepts/exception-handling-java.md) |
| 7 | Records | [records.md](java-concepts/records.md) |
| 8 | Enums | [enums.md](java-concepts/enums.md) |
| 9 | Concurrency | [concurrency.md](java-concepts/concurrency.md) |
| 10 | Functional Interfaces | [functional-interfaces.md](java-concepts/functional-interfaces.md) |
| 11 | Date and Time API | [date-time-api.md](java-concepts/date-time-api.md) |

### Production Readiness

| # | Topic | File |
|---|---|---|
| 1 | API Documentation (Swagger) | [api-documentation.md](production/api-documentation.md) |
| 2 | Logging | [logging.md](production/logging.md) |
| 3 | Monitoring (Actuator) | [monitoring.md](production/monitoring.md) |
| 4 | Deployment | [deployment.md](production/deployment.md) |

---

## Document Format

Every document follows this structure:

```
# Topic Title

## What is [Topic]?
Brief explanation in simple words.

## Why Does It Matter?
Why this concept exists and when you would use it on the job.

## How It Works
Step-by-step explanation with code examples.

## Code Examples
Working code with inline comments explaining every important line.

## Expected Output
What you should see when you run the code.

## Common Mistakes
Things that frequently go wrong and how to fix them.

## Better Alternatives
If there is a more professional or performant approach.

## Key Takeaways
3-5 bullet points summarizing the essentials.

## Related Topics
Links to other docs that connect to this one.
```

---

## Contributing

- Keep all documents **general-purpose** -- they should help anyone learning Spring Boot, not just one specific project
- Write in **simple words** -- if a beginner cannot understand it, simplify it
- Always include **working code examples**, not pseudocode
- Show **expected output** so readers can verify they are on the right track
- Note **common pitfalls** and **better alternatives** wherever applicable
- One document per topic -- do not mix unrelated concepts
