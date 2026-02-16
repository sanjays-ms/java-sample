# ResponseEntity -- Controlling HTTP Responses in Spring Boot

## What is ResponseEntity?

`ResponseEntity` is a class in Spring Boot that represents the **entire HTTP response** you send back to the client. It gives you full control over three things:

1. **Status code** -- the HTTP status (200 OK, 201 Created, 404 Not Found, etc.)
2. **Headers** -- metadata sent along with the response (content type, custom headers, etc.)
3. **Body** -- the actual data you are sending back (a string, an object, a list, etc.)

Without `ResponseEntity`, Spring Boot always returns a 200 OK status code. With it, you decide exactly what the client receives.

---

## Why Does It Matter?

In a real API, the status code tells the client **what happened** without them having to read the body:

- **200** -- "Here is the data you asked for"
- **201** -- "I created the new thing you sent me"
- **204** -- "Done, but there is nothing to send back" (like after deleting something)
- **400** -- "Your request is invalid"
- **404** -- "I could not find what you are looking for"
- **500** -- "Something went wrong on my end"

If you always return 200, the client has no way to know what actually happened without parsing the body. This breaks REST conventions and makes your API harder to use.

Every professional REST API uses proper status codes. Interviewers will notice if you return 200 for everything.

---

## The Generic Type -- What `<T>` Means

You will see `ResponseEntity` written as `ResponseEntity<T>`, where `T` is the **type of the body**.

```java
ResponseEntity<String>      // body is a String
ResponseEntity<Employee>    // body is an Employee object (converted to JSON automatically)
ResponseEntity<List<Task>>  // body is a list of Task objects
ResponseEntity<ErrorResponse> // body is a custom error object
ResponseEntity<Void>        // no body at all (used with 204 No Content, etc.)
```

The `<T>` is a Java feature called **generics**. Think of it as labeling a box:
- `ResponseEntity<String>` is a box that holds a String
- `ResponseEntity<Employee>` is a box that holds an Employee

When you return `ResponseEntity<Employee>`, Spring Boot automatically converts the Employee object into JSON before sending it to the client. You do not need to do that conversion yourself -- Spring handles it through a library called Jackson.

---

## How to Build a ResponseEntity

There are several ways to create a ResponseEntity. Here they are, from most common to least common.

### Method 1: Static Shorthand Methods (Recommended)

Spring provides shortcut methods for the most common status codes:

```java
// 200 OK with a body
return ResponseEntity.ok("everything is working");

// 200 OK with an object (automatically converted to JSON)
return ResponseEntity.ok(employee);

// 204 No Content, no body
return ResponseEntity.noContent().build();

// 404 Not Found, no body
return ResponseEntity.notFound().build();

// 400 Bad Request with an error message
return ResponseEntity.badRequest().body("Invalid input");
```

Use these whenever they are available. They are the cleanest and most readable.

### Method 2: Status + Body Chain

For status codes that do not have a shortcut method, use `.status()`:

```java
// 201 Created with the newly created object
return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);

// 409 Conflict with an error message
return ResponseEntity.status(HttpStatus.CONFLICT).body("Project name already exists");

// 403 Forbidden
return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access");
```

### Method 3: Constructor (Least Common)

This works but is more verbose. You will see it in older code:

```java
return new ResponseEntity<>("message", HttpStatus.OK);
return new ResponseEntity<>(employee, HttpStatus.CREATED);
```

**Prefer Method 1 or 2.** They are cleaner and more readable.

---

## Code Examples

### Example 1: Simple Health Check

```java
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    // GET /api/v1/health
    // Returns a simple text response with 200 OK
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("API is running");
    }
}
```

**Request:** `GET http://localhost:8080/api/v1/health`

**Response:**
```
Status: 200 OK
Body: API is running
```

---

### Example 2: Returning an Object as JSON

```java
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    // GET /api/v1/employees/1
    // Returns an Employee object -- Spring Boot converts it to JSON automatically
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok(employee);
    }
}
```

**Request:** `GET http://localhost:8080/api/v1/employees/1`

**Response:**
```json
Status: 200 OK
Body:
{
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com"
}
```

Spring Boot automatically converts the Java object to JSON using a library called Jackson. You do not need to write any conversion code.

---

### Example 3: Creating a Resource (POST with 201 Created)

```java
// POST /api/v1/projects
// Creates a new project and returns it with a 201 status
@PostMapping
public ResponseEntity<Project> createProject(@RequestBody ProjectRequest request) {
    Project saved = projectService.create(request);

    // 201 Created is the correct status for a successful creation
    // 200 OK would work, but 201 is the proper REST convention
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

**Request:** `POST http://localhost:8080/api/v1/projects`

**Response:**
```json
Status: 201 Created
Body:
{
    "id": 5,
    "name": "DevFlow",
    "description": "Task management API"
}
```

---

### Example 4: Deleting a Resource (204 No Content)

```java
// DELETE /api/v1/projects/5
// Deletes a project and returns no body
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
    projectService.delete(id);

    // 204 No Content -- "I did what you asked, but there is nothing to send back"
    // The <Void> generic type means this response has no body
    return ResponseEntity.noContent().build();
}
```

**Request:** `DELETE http://localhost:8080/api/v1/projects/5`

**Response:**
```
Status: 204 No Content
Body: (empty)
```

---

### Example 5: Resource Not Found (404)

```java
// GET /api/v1/projects/999
// Returns 404 if the project does not exist
@GetMapping("/{id}")
public ResponseEntity<Project> getProject(@PathVariable Long id) {
    Optional<Project> project = projectService.findById(id);

    if (project.isPresent()) {
        return ResponseEntity.ok(project.get());
    } else {
        return ResponseEntity.notFound().build();
    }
}
```

**Note:** This is the basic approach. Later, you will learn to throw a custom exception instead, and a global exception handler will return the 404 for you. That is cleaner and avoids repeating if/else checks in every controller method.

---

### Example 6: Adding Custom Headers

```java
@GetMapping("/health")
public ResponseEntity<String> checkHealth() {
    return ResponseEntity.ok()
            .header("X-App-Version", "1.0.0")      // custom header
            .header("X-Response-Time", "15ms")      // another custom header
            .body("API is running");
}
```

**Response:**
```
Status: 200 OK
Headers:
  X-App-Version: 1.0.0
  X-Response-Time: 15ms
Body: API is running
```

You will not need custom headers often, but it is good to know the option exists. Some common use cases: API versioning, rate limit info, pagination metadata.

---

## .build() vs .body() -- When to Use Each

This is a common point of confusion:

| Method | What it does | When to use it |
|---|---|---|
| `.body(content)` | Sends the status code AND the content | When you have data to return (GET, POST responses) |
| `.build()` | Sends ONLY the status code, no body | When there is no data to return (DELETE, some errors) |

```java
// .body() -- sends data back
return ResponseEntity.ok().body("here is your data");

// .build() -- sends just the status, empty body
return ResponseEntity.noContent().build();
```

**Rule of thumb:** if the client needs to see something, use `.body()`. If the action is done and there is nothing to show, use `.build()`.

---

## Quick Reference

| Situation | Code |
|---|---|
| Return data successfully | `ResponseEntity.ok(data)` |
| Created a new resource | `ResponseEntity.status(HttpStatus.CREATED).body(data)` |
| Deleted successfully | `ResponseEntity.noContent().build()` |
| Bad request | `ResponseEntity.badRequest().body(errorDetails)` |
| Not found | `ResponseEntity.notFound().build()` |
| Conflict (duplicate) | `ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails)` |
| Forbidden | `ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails)` |
| Server error | `ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails)` |

---

## Common Mistakes

### Mistake 1: Using .build() when you meant to send a body

```java
// WRONG -- sends 200 with an empty body, client sees nothing
return ResponseEntity.ok().build();

// RIGHT -- sends 200 with actual content
return ResponseEntity.ok("data here");

// ALSO RIGHT
return ResponseEntity.ok().body("data here");
```

### Mistake 2: Always returning 200 for everything

```java
// WRONG -- returns 200 even when creating a resource
@PostMapping
public ResponseEntity<Project> create(@RequestBody ProjectRequest request) {
    Project saved = projectService.create(request);
    return ResponseEntity.ok(saved);  // should be 201 Created
}

// RIGHT -- 201 tells the client a new resource was created
@PostMapping
public ResponseEntity<Project> create(@RequestBody ProjectRequest request) {
    Project saved = projectService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

### Mistake 3: Not using ResponseEntity at all

```java
// WORKS but you lose control over the status code
@GetMapping("/{id}")
public Employee getEmployee(@PathVariable Long id) {
    return employeeService.findById(id);  // always 200, even if you wanted something else
}

// BETTER -- explicit control
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
    Employee employee = employeeService.findById(id);
    return ResponseEntity.ok(employee);
}
```

### Mistake 4: Hardcoding status code numbers

```java
// WRONG -- magic number, hard to read
return ResponseEntity.status(201).body(saved);

// RIGHT -- use the HttpStatus enum, self-documenting
return ResponseEntity.status(HttpStatus.CREATED).body(saved);
```

---

## Better Alternatives

For simple endpoints, `ResponseEntity` is the standard approach. As your application grows, you will learn these patterns that work alongside ResponseEntity:

- **Global exception handlers** (`@RestControllerAdvice`) -- instead of returning 404 manually in every method, throw a `ResourceNotFoundException` and let a central handler convert it to a 404 response. This is cleaner and avoids repetitive code.
- **Custom response wrapper** -- some teams wrap all responses in a standard structure like `ApiResponse<T>` that includes a status, message, and data field. This gives the client a consistent format across all endpoints.

These are covered in their own documents. For now, `ResponseEntity` is exactly the right tool.

---

## Key Takeaways

- `ResponseEntity<T>` gives you control over the HTTP status code, headers, and body of your response
- The `<T>` generic type specifies what kind of data the body contains (String, an object, a list, Void for no body)
- Use static shorthand methods when available: `ResponseEntity.ok()`, `noContent()`, `notFound()`, `badRequest()`
- Use `.status(HttpStatus.CREATED).body(data)` for status codes without shortcuts
- `.body()` sends content, `.build()` sends just the status with an empty body
- Always use proper HTTP status codes -- do not return 200 for everything
- Use `HttpStatus` enum values, not magic numbers

---

## Related Topics

- [HTTP Methods and Status Codes](http-methods-and-status-codes.md) -- when to use each HTTP status code
- [Controllers and Request Mapping](controllers.md) -- how controllers work with ResponseEntity
- [DTOs and Data Transfer](dtos.md) -- what types you should use inside ResponseEntity
- [Request and Response Handling](request-response-handling.md) -- headers, content types, and more
