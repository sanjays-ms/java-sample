# Annotations Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/23-annotations.md)

---

## Built-in Annotations

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Override` | Method overrides superclass | `@Override public String toString()` |
| `@Deprecated` | Element is obsolete | `@Deprecated public void old()` |
| `@SuppressWarnings` | Suppress warnings | `@SuppressWarnings("unchecked")` |
| `@FunctionalInterface` | Lambda interface | `@FunctionalInterface interface F {}` |
| `@SafeVarargs` | Safe generic varargs | `@SafeVarargs static <T> void f(T...)` |

---

## @Deprecated Options (Java 9+)

```java
@Deprecated(since = "2.0", forRemoval = true)
public void oldMethod() { }
```

---

## @SuppressWarnings Types

| Type | Meaning |
|------|---------|
| `"unchecked"` | Unchecked type operations |
| `"deprecation"` | Use of deprecated elements |
| `"rawtypes"` | Raw types usage |
| `"unused"` | Unused code |
| `"serial"` | Missing serialVersionUID |
| `"all"` | All warnings |

```java
@SuppressWarnings({"unchecked", "deprecation"})
```

---

## Meta-Annotations

| Annotation | Purpose |
|------------|---------|
| `@Target` | Where annotation can be used |
| `@Retention` | How long annotation is kept |
| `@Documented` | Include in Javadoc |
| `@Inherited` | Subclasses inherit annotation |
| `@Repeatable` | Can be applied multiple times |

---

## @Target Options

```java
@Target(ElementType.METHOD)           // Single
@Target({ElementType.FIELD, ElementType.PARAMETER})  // Multiple
```

| ElementType | Applies To |
|-------------|-----------|
| `TYPE` | Class, interface, enum, record |
| `FIELD` | Field |
| `METHOD` | Method |
| `PARAMETER` | Method parameter |
| `CONSTRUCTOR` | Constructor |
| `LOCAL_VARIABLE` | Local variable |
| `ANNOTATION_TYPE` | Annotation |
| `TYPE_PARAMETER` | Generic type parameter |
| `TYPE_USE` | Any type use |

---

## @Retention Options

```java
@Retention(RetentionPolicy.RUNTIME)
```

| Policy | Description | Use Case |
|--------|-------------|----------|
| `SOURCE` | Discarded by compiler | Code generation |
| `CLASS` | In .class, not runtime | Default |
| `RUNTIME` | Available via reflection | Frameworks |

---

## Creating Custom Annotations

### Marker Annotation (No Elements)

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}

// Usage
@Test
public void myTest() { }
```

### Annotation with Elements

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestInfo {
    String description();           // Required
    String author() default "";     // Optional
    int priority() default 1;       // Optional
    String[] tags() default {};     // Optional
}

// Usage
@TestInfo(description = "Test user creation", priority = 2)
public void testCreate() { }
```

### Single Element Named value

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value();
}

// Can omit "value ="
@Table("users")
public class User { }
```

---

## Element Types Allowed

```java
@interface Example {
    int number();                    // Primitive
    String text();                   // String
    Class<?> type();                 // Class
    Priority level();                // Enum
    Author author();                 // Annotation
    String[] tags();                 // Array
}
```

---

## Repeatable Annotations

```java
// Container
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedules {
    Schedule[] value();
}

// Repeatable annotation
@Repeatable(Schedules.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {
    String day();
}

// Usage
@Schedule(day = "Monday")
@Schedule(day = "Friday")
public void meeting() { }
```

---

## Reading Annotations (Reflection)

### Check Presence

```java
if (method.isAnnotationPresent(Test.class)) {
    // Has @Test
}
```

### Get Annotation

```java
TestInfo info = method.getAnnotation(TestInfo.class);
if (info != null) {
    String desc = info.description();
    int priority = info.priority();
}
```

### Get All Annotations

```java
Annotation[] all = clazz.getAnnotations();
```

### Get Repeatable Annotations

```java
Schedule[] schedules = method.getAnnotationsByType(Schedule.class);
```

---

## Processing Example

```java
public static void runTests(Class<?> testClass) throws Exception {
    Object instance = testClass.getDeclaredConstructor().newInstance();
    
    for (Method method : testClass.getDeclaredMethods()) {
        if (method.isAnnotationPresent(Test.class)) {
            method.invoke(instance);
        }
    }
}
```

---

## Common Framework Annotations

### JUnit 5

| Annotation | Purpose |
|------------|---------|
| `@Test` | Test method |
| `@BeforeEach` | Run before each test |
| `@AfterEach` | Run after each test |
| `@BeforeAll` | Run once before all |
| `@AfterAll` | Run once after all |
| `@Disabled` | Skip test |
| `@DisplayName` | Custom test name |

### Spring

| Annotation | Purpose |
|------------|---------|
| `@Component` | Spring-managed bean |
| `@Service` | Service layer bean |
| `@Repository` | Data access bean |
| `@Controller` | Web controller |
| `@RestController` | REST controller |
| `@Autowired` | Inject dependency |
| `@GetMapping` | HTTP GET |
| `@PostMapping` | HTTP POST |
| `@RequestBody` | Request JSON body |
| `@PathVariable` | URL path variable |

### JPA

| Annotation | Purpose |
|------------|---------|
| `@Entity` | JPA entity |
| `@Table` | Table name |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-generate ID |
| `@Column` | Column mapping |
| `@OneToMany` | Relationship |
| `@ManyToOne` | Relationship |
| `@Transient` | Not persisted |

### Lombok

| Annotation | Generates |
|------------|-----------|
| `@Getter` | Getters |
| `@Setter` | Setters |
| `@Data` | Getters, setters, toString, equals, hashCode |
| `@Builder` | Builder pattern |
| `@NoArgsConstructor` | No-arg constructor |
| `@AllArgsConstructor` | All-arg constructor |

### Validation

| Annotation | Validates |
|------------|-----------|
| `@NotNull` | Not null |
| `@NotBlank` | Not blank |
| `@Size` | Size range |
| `@Min` / `@Max` | Number range |
| `@Email` | Email format |
| `@Pattern` | Regex |
| `@Past` / `@Future` | Date |

---

## Type Annotations (Java 8+)

```java
@Target(ElementType.TYPE_USE)
public @interface NonNull { }

// Usage
@NonNull String name;
List<@NonNull String> items;
void process() throws @Critical IOException { }
```

---

## Best Practices

| Do | Don't |
|----|-------|
| Use `@Override` always | Forget to add `@Deprecated` |
| Use `RUNTIME` for reflection | Use wrong retention |
| Specify `@Target` | Forget to check if annotation present |
| Document custom annotations | Create overly complex annotations |
| Keep annotations simple | Expect method annotation inheritance |

---

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Wrong retention for reflection | Use `RetentionPolicy.RUNTIME` |
| NPE when reading annotation | Check `isAnnotationPresent()` first |
| Expect inherited method annotations | Add annotation to overriding method |
| Miss `@Target` | Always specify where annotation applies |

---

## Quick Template

```java
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value() default "";
}
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/23-annotations.md)
