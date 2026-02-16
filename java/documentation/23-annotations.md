# Annotations

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/annotations-cheatsheet.md)

---

## What Is an Annotation?

An annotation is a form of metadata that provides information about a program but is not part of the program itself. Annotations start with the `@` symbol.

```java
@Override
public String toString() {
    return "Hello";
}
```

**In plain words:** Annotations are like sticky notes attached to your code. They tell the compiler, tools, or runtime environment extra information about the code. The annotation itself does not change what the code does - it describes or marks the code for special handling.

---

## Why Annotations Exist

### Problem 1: Configuration in External Files

Before annotations, configuration was often in separate XML files:

```xml
<!-- Old way: XML configuration -->
<bean id="userService" class="com.example.UserService">
    <property name="repository" ref="userRepository"/>
</bean>
```

With annotations, configuration is in the code:

```java
// Modern way: Annotations
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
}
```

### Problem 2: Boilerplate Code

Annotations can reduce repetitive code:

```java
// Without annotations - must write getter/setter manually
public class User {
    private String name;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// With Lombok annotations - generated automatically
@Getter @Setter
public class User {
    private String name;
}
```

### Problem 3: Documentation That Gets Outdated

```java
// Comment can become outdated
// This method is deprecated, use newMethod() instead
public void oldMethod() { }

// Annotation is enforced by compiler
@Deprecated
public void oldMethod() { }
```

### Benefits of Annotations

| Benefit | Description |
|---------|-------------|
| Compile-time checking | Compiler can verify annotation usage |
| Reduced boilerplate | Tools can generate code |
| Self-documenting | Configuration is with the code |
| Framework integration | Spring, JPA, JUnit rely heavily on annotations |
| IDE support | Better autocomplete and error detection |

---

## Built-in Annotations

Java provides several annotations in the `java.lang` package:

### @Override

Indicates that a method overrides a superclass method or implements an interface method:

```java
public class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    
    @Override
    public void makeSound() {
        System.out.println("Bark!");
    }
    
    // Compile error if typo - @Override catches it!
    @Override
    public void makeSond() {  // Error: does not override anything
        System.out.println("Bark!");
    }
}
```

**Why use it:** Without `@Override`, a typo creates a new method instead of overriding. The annotation catches this error at compile time.

### @Deprecated

Marks a program element as obsolete and discouraged:

```java
public class Calculator {
    
    /**
     * @deprecated Use {@link #add(int, int)} instead.
     */
    @Deprecated
    public int sum(int a, int b) {
        return a + b;
    }
    
    public int add(int a, int b) {
        return a + b;
    }
}
```

Using deprecated elements generates compiler warnings:

```java
Calculator calc = new Calculator();
int result = calc.sum(1, 2);  // Warning: 'sum' is deprecated
```

### @Deprecated with forRemoval (Java 9+)

```java
@Deprecated(since = "1.5", forRemoval = true)
public void oldMethod() {
    // This method will be removed in a future version
}
```

| Element | Meaning |
|---------|---------|
| `since` | Version when deprecated |
| `forRemoval` | If true, will be removed eventually |

### @SuppressWarnings

Tells the compiler to suppress specific warnings:

```java
// Suppress single warning type
@SuppressWarnings("unchecked")
public void processRawList() {
    List rawList = new ArrayList();
    List<String> strings = rawList;  // Normally warns about unchecked conversion
}

// Suppress multiple warning types
@SuppressWarnings({"unchecked", "deprecation"})
public void legacyMethod() {
    // Uses raw types and deprecated methods
}
```

Common warning types:

| Type | Meaning |
|------|---------|
| `"unchecked"` | Unchecked type operations (generics) |
| `"deprecation"` | Use of deprecated elements |
| `"rawtypes"` | Use of raw types |
| `"unused"` | Unused variables, methods, etc. |
| `"serial"` | Missing serialVersionUID |
| `"all"` | All warnings (use sparingly) |

### @FunctionalInterface

Indicates that an interface is intended for use with lambdas:

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
    
    // Only ONE abstract method allowed
    // int anotherMethod();  // Error! Would break functional interface
    
    // Default and static methods are OK
    default int add(int a, int b) {
        return calculate(a, b);
    }
}
```

```java
// Can use with lambda
Calculator adder = (a, b) -> a + b;
int result = adder.calculate(5, 3);  // 8
```

### @SafeVarargs

Suppresses warnings for methods with generic varargs (covered in Generics):

```java
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    return new ArrayList<>(Arrays.asList(elements));
}
```

---

## Annotations for Other Annotations (Meta-Annotations)

Java provides annotations that are used to define other annotations:

### @Target

Specifies where an annotation can be used:

```java
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.METHOD)  // Can only be used on methods
public @interface MethodOnly {
}

@Target({ElementType.FIELD, ElementType.PARAMETER})  // Multiple targets
public @interface FieldOrParam {
}
```

Element types:

| ElementType | Can Annotate |
|-------------|--------------|
| `TYPE` | Class, interface, enum, record |
| `FIELD` | Field (including enum constants) |
| `METHOD` | Method |
| `PARAMETER` | Method parameter |
| `CONSTRUCTOR` | Constructor |
| `LOCAL_VARIABLE` | Local variable |
| `ANNOTATION_TYPE` | Another annotation |
| `PACKAGE` | Package declaration |
| `TYPE_PARAMETER` | Type parameter (generics) |
| `TYPE_USE` | Any type use |
| `MODULE` | Module declaration |
| `RECORD_COMPONENT` | Record component |

### @Retention

Specifies how long annotations are retained:

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)  // Available at runtime via reflection
public @interface RuntimeAnnotation {
}

@Retention(RetentionPolicy.CLASS)  // In bytecode but not at runtime
public @interface ClassAnnotation {
}

@Retention(RetentionPolicy.SOURCE)  // Discarded by compiler
public @interface SourceAnnotation {
}
```

| RetentionPolicy | Description | Use Case |
|-----------------|-------------|----------|
| `SOURCE` | Compiler discards | Documentation, code generation |
| `CLASS` | In .class file, not runtime | Default, bytecode tools |
| `RUNTIME` | Available via reflection | Frameworks, runtime processing |

### @Documented

Includes annotation in Javadoc:

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    String name();
}
```

### @Inherited

Makes annotation inherited by subclasses:

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Audited {
}

@Audited
public class BaseEntity {
}

// UserEntity automatically has @Audited because it inherits it
public class UserEntity extends BaseEntity {
}
```

### @Repeatable (Java 8+)

Allows an annotation to be applied multiple times:

```java
// Container annotation
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedules {
    Schedule[] value();
}

// Repeatable annotation
@Repeatable(Schedules.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedule {
    String day();
    String time();
}

// Can now apply multiple times
@Schedule(day = "Monday", time = "9:00")
@Schedule(day = "Wednesday", time = "14:00")
@Schedule(day = "Friday", time = "9:00")
public void weeklyMeeting() {
}
```

---

## Creating Custom Annotations

### Basic Custom Annotation

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)  // Available at runtime
@Target(ElementType.METHOD)           // Can only be on methods
public @interface Test {
    // No elements - marker annotation
}
```

Using the annotation:

```java
public class MyTests {
    
    @Test
    public void testAddition() {
        assert 1 + 1 == 2;
    }
    
    @Test
    public void testSubtraction() {
        assert 5 - 3 == 2;
    }
    
    public void helperMethod() {
        // Not a test - no @Test annotation
    }
}
```

### Annotation with Elements

Elements are like parameters for annotations:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestInfo {
    // Element with no default - required
    String description();
    
    // Elements with defaults - optional
    String author() default "Unknown";
    int priority() default 1;
    String[] tags() default {};
}
```

Using annotation with elements:

```java
public class UserTests {
    
    @TestInfo(
        description = "Verifies user creation works correctly",
        author = "Alice",
        priority = 1,
        tags = {"user", "creation", "critical"}
    )
    public void testUserCreation() {
        // Test code
    }
    
    // Only required elements needed
    @TestInfo(description = "Tests user deletion")
    public void testUserDeletion() {
        // Uses defaults for author, priority, tags
    }
}
```

### The value() Element

If an annotation has a single element named `value`, you can omit the element name:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value();  // Named "value"
}

// Can omit "value ="
@Table("users")
public class User {
}

// Same as
@Table(value = "users")
public class User {
}
```

If there are other elements, you must use `value =` when setting multiple:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value();
    String schema() default "public";
}

// One element - can omit name
@Table("users")
public class User {
}

// Multiple elements - must use name
@Table(value = "users", schema = "app")
public class User {
}
```

### Element Types

Annotation elements can only be:

| Type | Example |
|------|---------|
| Primitives | `int priority()` |
| String | `String name()` |
| Class | `Class<?> type()` |
| Enum | `Priority level()` |
| Annotation | `Author author()` |
| Array of above | `String[] tags()` |

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
    String name();
    int timeout() default 30;
    boolean enabled() default true;
    Priority priority() default Priority.MEDIUM;
    Class<?> handler() default DefaultHandler.class;
    Author[] authors() default {};
    String[] tags() default {};
}
```

### Marker Annotations

Annotations with no elements are called marker annotations:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
    // No elements - just marks the method
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    // Marks class as a database entity
}
```

```java
@Entity
public class User {
    
    @Transactional
    public void save() {
        // Framework knows to wrap in transaction
    }
}
```

---

## Processing Annotations at Runtime

Use reflection to read annotations at runtime:

### Checking for Annotations

```java
import java.lang.reflect.Method;

public class AnnotationProcessor {
    
    public static void main(String[] args) throws Exception {
        Class<?> clazz = MyTests.class;
        
        for (Method method : clazz.getDeclaredMethods()) {
            // Check if method has @Test annotation
            if (method.isAnnotationPresent(Test.class)) {
                System.out.println("Test method: " + method.getName());
                
                // Run the test
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
```

### Reading Annotation Values

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestInfo {
    String description();
    int priority() default 1;
}

public class TestRunner {
    
    public static void processTests(Class<?> testClass) throws Exception {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TestInfo.class)) {
                // Get the annotation
                TestInfo info = method.getAnnotation(TestInfo.class);
                
                // Read values
                System.out.println("Test: " + method.getName());
                System.out.println("Description: " + info.description());
                System.out.println("Priority: " + info.priority());
                
                // Run tests by priority
                if (info.priority() >= 2) {
                    method.invoke(testClass.getDeclaredConstructor().newInstance());
                }
            }
        }
    }
}
```

### Reading Annotations on Classes

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    String table() default "";
}

public class EntityProcessor {
    
    public static String getTableName(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(Entity.class)) {
            Entity entity = entityClass.getAnnotation(Entity.class);
            String tableName = entity.table();
            
            // Use class name if table not specified
            if (tableName.isEmpty()) {
                tableName = entityClass.getSimpleName().toLowerCase();
            }
            return tableName;
        }
        throw new IllegalArgumentException("Not an entity: " + entityClass);
    }
}

@Entity(table = "users")
public class User {
}

@Entity  // Uses default - table name will be "product"
public class Product {
}
```

### Reading Annotations on Fields

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
    boolean nullable() default true;
    int length() default 255;
}

@Entity
public class User {
    
    @Column(name = "user_id", nullable = false)
    private Long id;
    
    @Column(name = "user_name", length = 100)
    private String name;
    
    @Column  // Uses all defaults
    private String email;
}

public class ColumnProcessor {
    
    public static void processColumns(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                
                String columnName = column.name().isEmpty() 
                    ? field.getName() 
                    : column.name();
                
                System.out.printf("Field %s -> Column %s (nullable=%b, length=%d)%n",
                    field.getName(), columnName, column.nullable(), column.length());
            }
        }
    }
}
```

### Getting All Annotations

```java
// Get all annotations on a class
Annotation[] classAnnotations = User.class.getAnnotations();

// Get all annotations on a method
Method method = User.class.getMethod("save");
Annotation[] methodAnnotations = method.getAnnotations();

// Get all annotations on a field
Field field = User.class.getDeclaredField("name");
Annotation[] fieldAnnotations = field.getAnnotations();

// Print annotation details
for (Annotation ann : classAnnotations) {
    System.out.println("Annotation type: " + ann.annotationType().getName());
}
```

### Processing Repeatable Annotations

```java
@Repeatable(Roles.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
public @interface Roles {
    Role[] value();
}

@Role("ADMIN")
@Role("USER")
@Role("MANAGER")
public class MultiRoleUser {
}

// Reading repeatable annotations
public static void processRoles(Class<?> clazz) {
    // Get all @Role annotations
    Role[] roles = clazz.getAnnotationsByType(Role.class);
    
    for (Role role : roles) {
        System.out.println("Role: " + role.value());
    }
    
    // Or get the container
    if (clazz.isAnnotationPresent(Roles.class)) {
        Roles rolesContainer = clazz.getAnnotation(Roles.class);
        for (Role role : rolesContainer.value()) {
            System.out.println("Role: " + role.value());
        }
    }
}
```

---

## Common Framework Annotations

### JUnit 5 Annotations

```java
import org.junit.jupiter.api.*;

public class CalculatorTest {
    
    private Calculator calc;
    
    @BeforeAll
    static void setupAll() {
        // Run once before all tests
    }
    
    @BeforeEach
    void setup() {
        // Run before each test
        calc = new Calculator();
    }
    
    @Test
    @DisplayName("Addition should work correctly")
    void testAddition() {
        assertEquals(4, calc.add(2, 2));
    }
    
    @Test
    @Disabled("Not implemented yet")
    void testDivision() {
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testPositiveNumbers(int number) {
        assertTrue(number > 0);
    }
    
    @AfterEach
    void tearDown() {
        // Run after each test
    }
    
    @AfterAll
    static void tearDownAll() {
        // Run once after all tests
    }
}
```

| Annotation | Purpose |
|------------|---------|
| `@Test` | Marks a test method |
| `@BeforeEach` | Run before each test |
| `@AfterEach` | Run after each test |
| `@BeforeAll` | Run once before all tests |
| `@AfterAll` | Run once after all tests |
| `@Disabled` | Skip this test |
| `@DisplayName` | Custom test name |
| `@ParameterizedTest` | Run test multiple times with different inputs |

### Spring Framework Annotations

```java
// Component scanning
@Component          // Generic component
@Service            // Service layer
@Repository         // Data access layer
@Controller         // Web controller
@RestController     // REST API controller

// Dependency injection
@Autowired          // Inject dependency
@Qualifier("name")  // Specify which bean
@Value("${property}") // Inject property value

// Configuration
@Configuration      // Java-based config class
@Bean              // Method produces a bean
@PropertySource    // Load properties file

// Web
@RequestMapping("/path")   // Map to URL
@GetMapping("/path")       // GET request
@PostMapping("/path")      // POST request
@PathVariable              // URL path variable
@RequestParam              // Query parameter
@RequestBody               // Request body (JSON)
@ResponseBody              // Return as response body
```

Example:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
    
    @GetMapping
    public List<User> searchUsers(@RequestParam(required = false) String name) {
        return userService.search(name);
    }
}
```

### JPA/Hibernate Annotations

```java
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;
    
    @Column(unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    
    @Transient  // Not persisted to database
    private String tempData;
}
```

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as JPA entity |
| `@Table` | Specifies table name |
| `@Id` | Primary key field |
| `@GeneratedValue` | Auto-generate ID |
| `@Column` | Column mapping details |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |
| `@ManyToMany` | Many-to-many relationship |
| `@JoinColumn` | Foreign key column |
| `@Transient` | Not persisted |
| `@Enumerated` | How to store enum |

### Jackson JSON Annotations

```java
import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    @JsonProperty("user_id")
    private Long id;
    
    private String name;
    
    @JsonIgnore  // Not included in JSON
    private String password;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;  // Only include if not null
}
```

### Lombok Annotations

```java
import lombok.*;

@Data  // Generates getters, setters, toString, equals, hashCode
public class User {
    private String name;
    private int age;
}

@Getter @Setter  // Only getters and setters
public class Product {
    private String name;
    private double price;
}

@NoArgsConstructor   // No-arg constructor
@AllArgsConstructor  // All-arg constructor
@Builder             // Builder pattern
public class Order {
    private Long id;
    private String item;
    private int quantity;
}

// Usage with @Builder
Order order = Order.builder()
    .id(1L)
    .item("Laptop")
    .quantity(2)
    .build();
```

### Validation Annotations (Jakarta Validation)

```java
import jakarta.validation.constraints.*;

public class UserDto {
    
    @NotNull(message = "ID is required")
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be realistic")
    private int age;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;
    
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
    
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;
}
```

| Annotation | Validates |
|------------|-----------|
| `@NotNull` | Not null |
| `@NotBlank` | Not null, not empty, not whitespace |
| `@NotEmpty` | Not null, not empty |
| `@Size` | String/collection size |
| `@Min` / `@Max` | Numeric range |
| `@Email` | Email format |
| `@Pattern` | Regex pattern |
| `@Past` / `@Future` | Date constraints |
| `@Positive` / `@Negative` | Number sign |

---

## Annotation Inheritance

### @Inherited Meta-Annotation

By default, annotations on classes are NOT inherited by subclasses:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NotInherited {
}

@NotInherited
public class Parent {
}

public class Child extends Parent {
}

// Check
Parent.class.isAnnotationPresent(NotInherited.class);  // true
Child.class.isAnnotationPresent(NotInherited.class);   // false
```

With `@Inherited`:

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Auditable {
}

@Auditable
public class Parent {
}

public class Child extends Parent {
}

// Check
Parent.class.isAnnotationPresent(Auditable.class);  // true
Child.class.isAnnotationPresent(Auditable.class);   // true - inherited!
```

**Note:** `@Inherited` only works with class annotations, not method or field annotations.

### Method Annotations and Overriding

Method annotations are NOT inherited when overriding:

```java
public class Parent {
    @Deprecated
    public void method() { }
}

public class Child extends Parent {
    @Override
    public void method() { }  // Not deprecated unless you add @Deprecated
}
```

---

## Type Annotations (Java 8+)

Annotations can be used on any type use:

```java
// On type parameter
public class Box<@NonNull T> {
}

// On type use
@NonNull String name;
List<@NonNull String> names;
String @NonNull [] array;

// On throws
void process() throws @Critical IOException {
}

// On casts
String s = (@NonNull String) object;

// On instanceof
if (obj instanceof @NonNull String) {
}

// On constructor reference
Supplier<@Immutable User> supplier = @Immutable User::new;
```

### Creating Type Annotations

```java
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
}

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Immutable {
}
```

---

## Compile-Time Annotation Processing

Annotations can be processed at compile time to generate code:

### The Annotation Processor API

```java
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.SourceVersion;
import java.util.Set;

@SupportedAnnotationTypes("com.example.GenerateBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class BuilderProcessor extends AbstractProcessor {
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, 
                          RoundEnvironment roundEnv) {
        
        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateBuilder.class)) {
            // Generate builder class for each annotated class
            generateBuilderClass(element);
        }
        
        return true;  // Annotation processed
    }
    
    private void generateBuilderClass(Element element) {
        // Use Filer to create new source files
        // processingEnv.getFiler().createSourceFile(...)
    }
}
```

### Registering the Processor

Create file: `META-INF/services/javax.annotation.processing.Processor`

```
com.example.BuilderProcessor
```

### Example: Simple Code Generation

```java
// Annotation
@Retention(RetentionPolicy.SOURCE)  // Only needed at compile time
@Target(ElementType.TYPE)
public @interface GenerateBuilder {
}

// Usage
@GenerateBuilder
public class User {
    private String name;
    private int age;
}

// Processor generates UserBuilder class at compile time
```

---

## Best Practices

### 1. Use Built-in Annotations

```java
// Always use @Override when overriding
@Override
public String toString() {
    return "...";
}

// Mark deprecated methods properly
@Deprecated(since = "2.0", forRemoval = true)
public void oldMethod() {
}

// Use @FunctionalInterface for lambda interfaces
@FunctionalInterface
public interface Handler {
    void handle(Event e);
}
```

### 2. Document Custom Annotations

```java
/**
 * Marks a method as a scheduled task.
 * 
 * <p>Methods annotated with @Scheduled will be executed
 * periodically according to the specified interval.
 * 
 * @see TaskScheduler
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Scheduled {
    /**
     * The interval between executions in milliseconds.
     */
    long interval();
    
    /**
     * Initial delay before first execution in milliseconds.
     */
    long initialDelay() default 0;
}
```

### 3. Use Appropriate Retention

```java
// SOURCE - for compile-time tools, not needed at runtime
@Retention(RetentionPolicy.SOURCE)
public @interface Generated {
}

// RUNTIME - for frameworks that need reflection
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}
```

### 4. Keep Annotations Simple

```java
// Good - simple and focused
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    String key() default "";
    int ttlSeconds() default 3600;
}

// Avoid - too complex
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    String key() default "";
    int ttlSeconds() default 3600;
    Class<? extends CacheProvider> provider() default DefaultCache.class;
    boolean async() default false;
    String region() default "default";
    // ... too many options
}
```

### 5. Prefer Annotations Over Marker Interfaces

```java
// Old way - marker interface
public interface Serializable {
}

// Modern way - annotation (more flexible)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serializable {
    String format() default "json";  // Can have elements
}
```

---

## Common Mistakes

### Mistake 1: Wrong Retention Policy

```java
// Bug: Cannot read at runtime!
@Retention(RetentionPolicy.SOURCE)  // Wrong for runtime processing
public @interface MyAnnotation {
}

// Fix: Use RUNTIME for reflection-based processing
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
}
```

### Mistake 2: Forgetting @Target

```java
// Bug: Can be used anywhere - probably not intended
public @interface MethodAnnotation {
}

// Fix: Specify where it can be used
@Target(ElementType.METHOD)
public @interface MethodAnnotation {
}
```

### Mistake 3: Mutable Default Values

```java
// Bug: Arrays are returned by reference
public @interface Config {
    String[] values() default {"a", "b"};  // OK but be careful
}

// Reading annotation values always returns a copy, so this is actually safe
// But be aware when using arrays
```

### Mistake 4: Not Checking for Annotation Presence

```java
// Bug: NullPointerException if annotation not present
MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class);
String value = ann.value();  // NPE if annotation not present!

// Fix: Check first
if (clazz.isAnnotationPresent(MyAnnotation.class)) {
    MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class);
    String value = ann.value();
}
```

### Mistake 5: Expecting Method Annotation Inheritance

```java
public class Parent {
    @Transactional
    public void save() { }
}

public class Child extends Parent {
    @Override
    public void save() { }  // NOT transactional! Annotations not inherited
}

// Fix: Add annotation to child method
public class Child extends Parent {
    @Override
    @Transactional  // Must add explicitly
    public void save() { }
}
```

---

## Cheat Sheet

| Annotation | Purpose |
|------------|---------|
| `@Override` | Method overrides superclass |
| `@Deprecated` | Element is obsolete |
| `@SuppressWarnings` | Suppress compiler warnings |
| `@FunctionalInterface` | Interface for lambdas |
| `@SafeVarargs` | Safe generic varargs |

| Meta-Annotation | Purpose |
|-----------------|---------|
| `@Target` | Where annotation can be used |
| `@Retention` | How long annotation is kept |
| `@Documented` | Include in Javadoc |
| `@Inherited` | Subclasses inherit annotation |
| `@Repeatable` | Can be applied multiple times |

| RetentionPolicy | Availability |
|-----------------|--------------|
| `SOURCE` | Compile time only |
| `CLASS` | In .class file |
| `RUNTIME` | Available via reflection |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Enums](./22-enums.md) | [Guide](../guide.md) | [Lambda Expressions](./24-lambda-expressions.md) |
