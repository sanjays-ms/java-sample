# Enums

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/enums-cheatsheet.md)

---

## What Is an Enum?

An enum (short for "enumeration") is a special type that represents a fixed set of constants. Think of it as a list of predefined values that cannot change.

```java
// Define an enum with three constants
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

Using the enum:

```java
Day today = Day.MONDAY;

if (today == Day.MONDAY) {
    System.out.println("Start of the work week!");
}
```

**In plain words:** An enum is like a multiple-choice question where the answers are fixed. A `Day` can only be one of the seven days - you cannot create a new day like `Day.FUNDAY`.

---

## Why Enums Exist

### Problem 1: Magic Numbers

Without enums, you might use integers to represent choices:

```java
// Without enums - using magic numbers
public static final int STATUS_PENDING = 0;
public static final int STATUS_APPROVED = 1;
public static final int STATUS_REJECTED = 2;

int orderStatus = STATUS_PENDING;

// Problems:
// 1. Nothing stops you from using invalid values
orderStatus = 99;  // Compiles but is invalid!

// 2. No type safety
orderStatus = STATUS_APPROVED + STATUS_REJECTED;  // Makes no sense!

// 3. Hard to read in debugger (shows 0, 1, 2 instead of names)
```

With enums:

```java
// With enums - type safe
public enum OrderStatus {
    PENDING, APPROVED, REJECTED
}

OrderStatus status = OrderStatus.PENDING;

// status = 99;  // Compile error! Must be an OrderStatus
// status = OrderStatus.APPROVED + OrderStatus.REJECTED;  // Compile error!
```

### Problem 2: Magic Strings

Using strings has similar problems:

```java
// Without enums - using strings
String direction = "NORTH";

// Problems:
// 1. Typos compile but fail at runtime
direction = "NROTH";  // Typo compiles!

// 2. Case sensitivity issues
if (direction.equals("north")) { }  // Fails - case mismatch

// 3. No IDE autocomplete
```

With enums:

```java
// With enums - compiler catches errors
public enum Direction {
    NORTH, SOUTH, EAST, WEST
}

Direction dir = Direction.NORTH;
// dir = Direction.NROTH;  // Compile error - no such constant!
```

### Benefits of Enums

| Benefit | Description |
|---------|-------------|
| Type safety | Compiler ensures only valid values are used |
| Readable | Self-documenting code |
| IDE support | Autocomplete shows all options |
| Debugger friendly | Shows name, not number |
| Switch-compatible | Works great with switch statements |
| Singleton per constant | Each constant is a unique instance |

---

## Defining Enums

### Basic Enum

```java
// Simple enum with constants only
public enum Season {
    SPRING, SUMMER, FALL, WINTER
}
```

### Enum Naming Conventions

- Enum type name: PascalCase (like classes)
- Constants: UPPER_SNAKE_CASE (like static final fields)

```java
public enum HttpStatus {
    OK,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
    BAD_REQUEST
}
```

### Enum in Its Own File

Like classes, enums can be in their own file:

```java
// File: Priority.java
public enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

### Enum Inside a Class

Enums can be nested inside classes:

```java
public class Task {
    
    // Nested enum
    public enum Status {
        TODO, IN_PROGRESS, DONE, CANCELLED
    }
    
    private String name;
    private Status status;
    
    public Task(String name) {
        this.name = name;
        this.status = Status.TODO;
    }
    
    public void start() {
        this.status = Status.IN_PROGRESS;
    }
    
    public void complete() {
        this.status = Status.DONE;
    }
}
```

Using nested enum:

```java
Task task = new Task("Write documentation");
task.start();

// Access nested enum
Task.Status status = Task.Status.IN_PROGRESS;
```

---

## Using Enums

### Assignment and Comparison

```java
Day today = Day.FRIDAY;
Day tomorrow = Day.SATURDAY;

// Comparison with ==
if (today == Day.FRIDAY) {
    System.out.println("Almost weekend!");
}

// Comparison with equals() also works
if (today.equals(Day.FRIDAY)) {
    System.out.println("Same result");
}

// Comparing two enum variables
if (today != tomorrow) {
    System.out.println("Different days");
}
```

**Note:** For enums, `==` and `equals()` are equivalent because each constant is a singleton. Using `==` is preferred as it is null-safe and faster.

### Null Safety with ==

```java
Day day = null;

// Using == is null-safe
if (day == Day.MONDAY) {  // false, no NullPointerException
    System.out.println("Monday");
}

// Using equals() would throw NullPointerException
// if (day.equals(Day.MONDAY)) { }  // NullPointerException!

// Safe way with equals()
if (Day.MONDAY.equals(day)) {  // OK - constant on left
    System.out.println("Monday");
}
```

---

## Built-in Enum Methods

Every enum automatically has these methods:

### name()

Returns the exact name of the constant as a String:

```java
Day day = Day.MONDAY;
String name = day.name();  // "MONDAY"
```

### ordinal()

Returns the position of the constant (0-based):

```java
Day day = Day.WEDNESDAY;
int position = day.ordinal();  // 2 (MONDAY=0, TUESDAY=1, WEDNESDAY=2)
```

**Warning:** Do not rely on `ordinal()` for persistent data. If you reorder constants, ordinal values change.

### values()

Returns an array of all constants in declaration order:

```java
Day[] allDays = Day.values();

for (Day day : allDays) {
    System.out.println(day);
}
// Output:
// MONDAY
// TUESDAY
// WEDNESDAY
// THURSDAY
// FRIDAY
// SATURDAY
// SUNDAY
```

### valueOf(String)

Converts a String to the enum constant (case-sensitive):

```java
Day day = Day.valueOf("MONDAY");  // Day.MONDAY

// Case must match exactly
// Day.valueOf("monday");  // IllegalArgumentException!
// Day.valueOf("Monday");  // IllegalArgumentException!

// Handle invalid input
try {
    Day invalid = Day.valueOf("FUNDAY");
} catch (IllegalArgumentException e) {
    System.out.println("Invalid day name");
}
```

### toString()

By default, returns the same as `name()`. Can be overridden:

```java
Day day = Day.MONDAY;
String str = day.toString();  // "MONDAY"
System.out.println(day);      // Uses toString() - prints "MONDAY"
```

### compareTo()

Compares by ordinal (declaration order):

```java
Day monday = Day.MONDAY;
Day friday = Day.FRIDAY;

int result = monday.compareTo(friday);  // Negative (MONDAY comes before FRIDAY)
result = friday.compareTo(monday);      // Positive (FRIDAY comes after MONDAY)
result = monday.compareTo(Day.MONDAY);  // 0 (same)
```

---

## Enums in Switch Statements

Enums work perfectly with switch statements:

```java
public String getSeasonMessage(Season season) {
    switch (season) {
        case SPRING:
            return "Flowers are blooming!";
        case SUMMER:
            return "Time for the beach!";
        case FALL:
            return "Leaves are falling!";
        case WINTER:
            return "Bundle up!";
        default:
            return "Unknown season";  // Unreachable if all cases covered
    }
}
```

**Note:** Inside `switch`, use the constant name without the enum type prefix. Write `case SPRING:` not `case Season.SPRING:`.

### Switch Expressions (Java 14+)

```java
public String getSeasonMessage(Season season) {
    return switch (season) {
        case SPRING -> "Flowers are blooming!";
        case SUMMER -> "Time for the beach!";
        case FALL -> "Leaves are falling!";
        case WINTER -> "Bundle up!";
    };
    // No default needed - compiler knows all cases are covered
}
```

### Pattern Matching in Switch (Java 21+)

```java
public String describe(Object obj) {
    return switch (obj) {
        case Season s when s == Season.SUMMER -> "Hot " + s;
        case Season s -> "Season: " + s;
        case Day d -> "Day: " + d;
        case null -> "Nothing";
        default -> "Unknown: " + obj;
    };
}
```

---

## Enums with Fields and Methods

Enums can have fields, constructors, and methods - they are full-fledged classes.

### Adding Fields to Enums

```java
public enum Planet {
    // Each constant calls the constructor with values
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6),
    JUPITER(1.9e+27, 7.1492e7),
    SATURN(5.688e+26, 6.0268e7),
    URANUS(8.686e+25, 2.5559e7),
    NEPTUNE(1.024e+26, 2.4746e7);
    
    // Fields - store data for each constant
    private final double mass;   // in kilograms
    private final double radius; // in meters
    
    // Constructor - private by default (cannot be public)
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    
    // Getter methods
    public double getMass() {
        return mass;
    }
    
    public double getRadius() {
        return radius;
    }
    
    // Calculated property
    public double getSurfaceGravity() {
        // G is the gravitational constant
        double G = 6.67300E-11;
        return G * mass / (radius * radius);
    }
    
    public double getSurfaceWeight(double otherMass) {
        return otherMass * getSurfaceGravity();
    }
}
```

Using enum with fields:

```java
Planet earth = Planet.EARTH;
System.out.println("Earth mass: " + earth.getMass());
System.out.println("Earth radius: " + earth.getRadius());

// Calculate weight on different planets
double massOnEarth = 75;  // kg
for (Planet p : Planet.values()) {
    System.out.printf("Weight on %s: %.2f N%n", 
        p, p.getSurfaceWeight(massOnEarth));
}
```

### Simpler Example with Fields

```java
public enum Size {
    SMALL("S", 10.00),
    MEDIUM("M", 15.00),
    LARGE("L", 20.00),
    EXTRA_LARGE("XL", 25.00);
    
    private final String abbreviation;
    private final double price;
    
    Size(String abbreviation, double price) {
        this.abbreviation = abbreviation;
        this.price = price;
    }
    
    public String getAbbreviation() {
        return abbreviation;
    }
    
    public double getPrice() {
        return price;
    }
}
```

```java
Size size = Size.LARGE;
System.out.println("Size: " + size.getAbbreviation());  // "L"
System.out.println("Price: $" + size.getPrice());       // "$20.0"
```

### Overriding toString()

```java
public enum Priority {
    LOW(1, "Low Priority"),
    MEDIUM(2, "Medium Priority"),
    HIGH(3, "High Priority"),
    CRITICAL(4, "Critical - Immediate Action Required");
    
    private final int level;
    private final String description;
    
    Priority(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return description;  // More readable than "LOW", "MEDIUM", etc.
    }
}
```

```java
Priority p = Priority.CRITICAL;
System.out.println(p);         // "Critical - Immediate Action Required"
System.out.println(p.name());  // "CRITICAL" (always returns constant name)
```

---

## Enums with Abstract Methods

Each enum constant can have its own implementation of a method:

```java
public enum Operation {
    ADD {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    SUBTRACT {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    MULTIPLY {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE {
        @Override
        public double apply(double x, double y) {
            if (y == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            return x / y;
        }
    };
    
    // Abstract method - each constant must implement
    public abstract double apply(double x, double y);
}
```

Using enum with abstract methods:

```java
double a = 10;
double b = 5;

for (Operation op : Operation.values()) {
    System.out.printf("%s: %.2f%n", op, op.apply(a, b));
}
// Output:
// ADD: 15.00
// SUBTRACT: 5.00
// MULTIPLY: 50.00
// DIVIDE: 2.00

// Use in calculations
Operation op = Operation.MULTIPLY;
double result = op.apply(7, 3);  // 21.0
```

### Combining Fields and Abstract Methods

```java
public enum PaymentMethod {
    CREDIT_CARD("Credit Card", 0.03) {
        @Override
        public double calculateFee(double amount) {
            return amount * getFeeRate();
        }
    },
    DEBIT_CARD("Debit Card", 0.01) {
        @Override
        public double calculateFee(double amount) {
            return amount * getFeeRate();
        }
    },
    CASH("Cash", 0.0) {
        @Override
        public double calculateFee(double amount) {
            return 0;  // No fee for cash
        }
    },
    BANK_TRANSFER("Bank Transfer", 0.0) {
        @Override
        public double calculateFee(double amount) {
            return 5.00;  // Flat fee
        }
    };
    
    private final String displayName;
    private final double feeRate;
    
    PaymentMethod(String displayName, double feeRate) {
        this.displayName = displayName;
        this.feeRate = feeRate;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public double getFeeRate() {
        return feeRate;
    }
    
    public abstract double calculateFee(double amount);
    
    public double calculateTotal(double amount) {
        return amount + calculateFee(amount);
    }
}
```

```java
double amount = 100.00;

for (PaymentMethod method : PaymentMethod.values()) {
    double fee = method.calculateFee(amount);
    double total = method.calculateTotal(amount);
    System.out.printf("%s: Fee=%.2f, Total=%.2f%n",
        method.getDisplayName(), fee, total);
}
// Output:
// Credit Card: Fee=3.00, Total=103.00
// Debit Card: Fee=1.00, Total=101.00
// Cash: Fee=0.00, Total=100.00
// Bank Transfer: Fee=5.00, Total=105.00
```

---

## Enums Implementing Interfaces

Enums can implement interfaces:

```java
// Interface definition
public interface Describable {
    String getDescription();
    String getShortCode();
}

// Enum implementing interface
public enum TaskStatus implements Describable {
    TODO("To Do", "TD", "Task has not been started"),
    IN_PROGRESS("In Progress", "IP", "Task is being worked on"),
    REVIEW("In Review", "RV", "Task is being reviewed"),
    DONE("Done", "DN", "Task is completed"),
    CANCELLED("Cancelled", "CN", "Task was cancelled");
    
    private final String displayName;
    private final String shortCode;
    private final String description;
    
    TaskStatus(String displayName, String shortCode, String description) {
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.description = description;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String getShortCode() {
        return shortCode;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

Using enum with interface:

```java
TaskStatus status = TaskStatus.IN_PROGRESS;
System.out.println(status.getDisplayName());   // "In Progress"
System.out.println(status.getShortCode());     // "IP"
System.out.println(status.getDescription());   // "Task is being worked on"

// Can be used as Describable
Describable d = TaskStatus.DONE;
System.out.println(d.getDescription());
```

### Functional Interface Implementation

```java
// Enum implementing functional interface
public enum StringOperation implements UnaryOperator<String> {
    UPPERCASE {
        @Override
        public String apply(String s) {
            return s.toUpperCase();
        }
    },
    LOWERCASE {
        @Override
        public String apply(String s) {
            return s.toLowerCase();
        }
    },
    TRIM {
        @Override
        public String apply(String s) {
            return s.trim();
        }
    },
    REVERSE {
        @Override
        public String apply(String s) {
            return new StringBuilder(s).reverse().toString();
        }
    };
}
```

```java
String text = "  Hello World  ";

// Use as function
String upper = StringOperation.UPPERCASE.apply(text);  // "  HELLO WORLD  "
String trimmed = StringOperation.TRIM.apply(text);     // "Hello World"

// Chain operations
String result = StringOperation.TRIM
    .andThen(StringOperation.UPPERCASE)
    .apply(text);  // "HELLO WORLD"
```

---

## EnumSet

`EnumSet` is a specialized Set implementation for enums. It is very fast and memory-efficient.

### Creating EnumSet

```java
// Empty set
EnumSet<Day> noDay = EnumSet.noneOf(Day.class);

// All values
EnumSet<Day> allDays = EnumSet.allOf(Day.class);

// Specific values
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);

// Range of values
EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);

// Complement (all except specified)
EnumSet<Day> weekdaysAlt = EnumSet.complementOf(weekend);
```

### Using EnumSet

```java
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);

// Check membership
boolean isWeekend = weekend.contains(Day.SATURDAY);  // true

// Add and remove
EnumSet<Day> holidays = EnumSet.noneOf(Day.class);
holidays.add(Day.FRIDAY);    // Add single
holidays.addAll(weekend);    // Add multiple
holidays.remove(Day.FRIDAY); // Remove

// Iteration
for (Day day : weekend) {
    System.out.println(day + " is a weekend day");
}

// Size
int count = weekend.size();  // 2
```

### Practical Example: Permission Flags

```java
public enum Permission {
    READ,
    WRITE,
    EXECUTE,
    DELETE,
    ADMIN
}

public class User {
    private String name;
    private EnumSet<Permission> permissions;
    
    public User(String name) {
        this.name = name;
        this.permissions = EnumSet.noneOf(Permission.class);
    }
    
    public void grantPermission(Permission permission) {
        permissions.add(permission);
    }
    
    public void revokePermission(Permission permission) {
        permissions.remove(permission);
    }
    
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
    
    public boolean hasAllPermissions(EnumSet<Permission> required) {
        return permissions.containsAll(required);
    }
    
    public void grantAll() {
        permissions = EnumSet.allOf(Permission.class);
    }
    
    public void revokeAll() {
        permissions.clear();
    }
}
```

```java
User user = new User("Alice");
user.grantPermission(Permission.READ);
user.grantPermission(Permission.WRITE);

if (user.hasPermission(Permission.READ)) {
    System.out.println("User can read");
}

EnumSet<Permission> editorPerms = EnumSet.of(Permission.READ, Permission.WRITE);
if (user.hasAllPermissions(editorPerms)) {
    System.out.println("User has editor permissions");
}
```

---

## EnumMap

`EnumMap` is a specialized Map implementation with enum keys. It is very fast and memory-efficient.

### Creating EnumMap

```java
// Create with enum class
EnumMap<Day, String> dayActivities = new EnumMap<>(Day.class);

// Create from another EnumMap
EnumMap<Day, String> copy = new EnumMap<>(dayActivities);

// Create from another Map
Map<Day, String> regularMap = new HashMap<>();
regularMap.put(Day.MONDAY, "Work");
EnumMap<Day, String> fromMap = new EnumMap<>(regularMap);
```

### Using EnumMap

```java
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);

// Put values
schedule.put(Day.MONDAY, "Team meeting");
schedule.put(Day.TUESDAY, "Code review");
schedule.put(Day.WEDNESDAY, "Sprint planning");
schedule.put(Day.FRIDAY, "Demo day");

// Get value
String monday = schedule.get(Day.MONDAY);  // "Team meeting"
String thursday = schedule.get(Day.THURSDAY);  // null

// Get with default
String thursday2 = schedule.getOrDefault(Day.THURSDAY, "Regular work");

// Check key/value
boolean hasMonday = schedule.containsKey(Day.MONDAY);  // true
boolean hasDemo = schedule.containsValue("Demo day");   // true

// Iterate
for (Map.Entry<Day, String> entry : schedule.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

### Practical Example: Configuration by Environment

```java
public enum Environment {
    DEVELOPMENT,
    TESTING,
    STAGING,
    PRODUCTION
}

public class DatabaseConfig {
    private static final EnumMap<Environment, String> DB_URLS = new EnumMap<>(Environment.class);
    private static final EnumMap<Environment, Integer> POOL_SIZES = new EnumMap<>(Environment.class);
    
    static {
        DB_URLS.put(Environment.DEVELOPMENT, "jdbc:h2:mem:devdb");
        DB_URLS.put(Environment.TESTING, "jdbc:h2:mem:testdb");
        DB_URLS.put(Environment.STAGING, "jdbc:postgresql://staging:5432/app");
        DB_URLS.put(Environment.PRODUCTION, "jdbc:postgresql://prod:5432/app");
        
        POOL_SIZES.put(Environment.DEVELOPMENT, 5);
        POOL_SIZES.put(Environment.TESTING, 3);
        POOL_SIZES.put(Environment.STAGING, 10);
        POOL_SIZES.put(Environment.PRODUCTION, 50);
    }
    
    public static String getDbUrl(Environment env) {
        return DB_URLS.get(env);
    }
    
    public static int getPoolSize(Environment env) {
        return POOL_SIZES.get(env);
    }
}
```

```java
Environment env = Environment.PRODUCTION;
String url = DatabaseConfig.getDbUrl(env);      // production URL
int poolSize = DatabaseConfig.getPoolSize(env); // 50
```

---

## Singleton Pattern with Enum

Enums provide the simplest way to create a singleton in Java:

```java
public enum DatabaseConnection {
    INSTANCE;  // The single instance
    
    private Connection connection;
    
    // Called once when INSTANCE is first accessed
    DatabaseConnection() {
        // Initialize connection
        try {
            connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/mydb",
                "user",
                "password"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void executeQuery(String sql) {
        // Use connection
    }
}
```

Using the singleton:

```java
// Get the instance
DatabaseConnection db = DatabaseConnection.INSTANCE;
Connection conn = db.getConnection();

// Or directly
DatabaseConnection.INSTANCE.executeQuery("SELECT * FROM users");
```

### Why Enum Singletons Are Best

| Traditional Singleton | Enum Singleton |
|----------------------|----------------|
| Complex implementation | Simple - just one constant |
| Serialization issues | Serialization handled automatically |
| Reflection attacks possible | Reflection cannot create new instances |
| Thread safety must be handled | Thread-safe by default |

### Enum Singleton with Interface

```java
public interface Cache {
    void put(String key, Object value);
    Object get(String key);
    void clear();
}

public enum ApplicationCache implements Cache {
    INSTANCE;
    
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }
    
    @Override
    public Object get(String key) {
        return cache.get(key);
    }
    
    @Override
    public void clear() {
        cache.clear();
    }
}
```

```java
Cache cache = ApplicationCache.INSTANCE;
cache.put("user", new User("Alice"));
User user = (User) cache.get("user");
```

---

## Converting Between Enums and Other Types

### Enum to String

```java
Day day = Day.MONDAY;

// Using name() - exact constant name
String name = day.name();      // "MONDAY"

// Using toString() - can be overridden
String str = day.toString();   // "MONDAY" (unless overridden)

// Using custom method
public enum Day {
    MONDAY("Monday"),
    // ...
    ;
    private final String displayName;
    
    Day(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

### String to Enum

```java
// Using valueOf() - case sensitive
Day day = Day.valueOf("MONDAY");  // Day.MONDAY
// Day.valueOf("monday");  // IllegalArgumentException!

// Safe conversion with error handling
public static <T extends Enum<T>> T safeValueOf(Class<T> enumType, String name) {
    try {
        return Enum.valueOf(enumType, name);
    } catch (IllegalArgumentException | NullPointerException e) {
        return null;
    }
}

Day day = safeValueOf(Day.class, "INVALID");  // null

// Case-insensitive conversion
public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumType, String name) {
    for (T constant : enumType.getEnumConstants()) {
        if (constant.name().equalsIgnoreCase(name)) {
            return constant;
        }
    }
    return null;
}

Day day = valueOfIgnoreCase(Day.class, "monday");  // Day.MONDAY
```

### Enum to int (ordinal)

```java
Day day = Day.WEDNESDAY;
int ordinal = day.ordinal();  // 2

// Custom mapping is safer
public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);
    
    private final int value;
    
    Priority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
```

### int to Enum

```java
// Using ordinal (not recommended)
Day day = Day.values()[2];  // Day.WEDNESDAY

// Using custom value (recommended)
public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);
    
    private final int value;
    private static final Map<Integer, Priority> BY_VALUE = new HashMap<>();
    
    static {
        for (Priority p : values()) {
            BY_VALUE.put(p.value, p);
        }
    }
    
    Priority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static Priority fromValue(int value) {
        return BY_VALUE.get(value);
    }
}

Priority p = Priority.fromValue(2);  // Priority.MEDIUM
```

---

## Enums in Collections

### List of Enums

```java
List<Day> workDays = List.of(
    Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY
);

// Mutable list
List<Day> myDays = new ArrayList<>();
myDays.add(Day.MONDAY);
myDays.add(Day.FRIDAY);
```

### Sorting Enums

Enums are sorted by ordinal (declaration order) by default:

```java
List<Day> days = new ArrayList<>(List.of(
    Day.FRIDAY, Day.MONDAY, Day.WEDNESDAY
));

Collections.sort(days);  // Sorted by ordinal
// [MONDAY, WEDNESDAY, FRIDAY]

// Custom sort
days.sort(Comparator.comparing(Day::name));  // Alphabetical
// [FRIDAY, MONDAY, WEDNESDAY]
```

### Grouping by Enum

```java
public class Task {
    private String name;
    private TaskStatus status;
    
    // Constructor, getters...
}

List<Task> tasks = // ... get tasks

// Group by status
Map<TaskStatus, List<Task>> byStatus = tasks.stream()
    .collect(Collectors.groupingBy(Task::getStatus));

List<Task> doneTasks = byStatus.get(TaskStatus.DONE);
```

---

## Enum and JSON/Serialization

### Basic Serialization

Enums are serializable by default:

```java
// Serialize
Day day = Day.MONDAY;
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("day.ser"));
out.writeObject(day);
out.close();

// Deserialize
ObjectInputStream in = new ObjectInputStream(new FileInputStream("day.ser"));
Day loaded = (Day) in.readObject();
in.close();
// loaded == Day.MONDAY (same instance)
```

### JSON with Jackson

```java
public enum Status {
    ACTIVE,
    INACTIVE,
    PENDING
}

// Default: serializes as "ACTIVE", "INACTIVE", etc.

// Custom serialization with @JsonValue
public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive"),
    PENDING("pending");
    
    private final String jsonValue;
    
    Status(String jsonValue) {
        this.jsonValue = jsonValue;
    }
    
    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
    
    @JsonCreator
    public static Status fromJson(String value) {
        for (Status s : values()) {
            if (s.jsonValue.equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
```

### Database Persistence with JPA

```java
@Entity
public class Order {
    
    @Id
    private Long id;
    
    // Store as string (recommended)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    // Or store as ordinal (not recommended)
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
}

public enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED
}
```

**Recommendation:** Always use `EnumType.STRING` to avoid issues when enum constants are reordered.

---

## Comparing Enums

### Using == (Preferred)

```java
Day day = Day.MONDAY;

if (day == Day.MONDAY) {
    System.out.println("It's Monday");
}
```

### Using equals()

```java
if (day.equals(Day.MONDAY)) {
    System.out.println("It's Monday");
}
```

### Using compareTo()

```java
Day d1 = Day.MONDAY;
Day d2 = Day.FRIDAY;

if (d1.compareTo(d2) < 0) {
    System.out.println(d1 + " comes before " + d2);
}
```

### Why == Is Preferred

| `==` | `equals()` |
|------|-----------|
| Null-safe | Throws NPE if left side is null |
| Faster | Method call overhead |
| Compile-time type check | Runtime check |
| Cannot accidentally compare different enum types | Can compare any objects |

```java
Day day = null;
Priority p = Priority.HIGH;

// Safe
if (day == Day.MONDAY) { }  // false, no exception

// Unsafe
// if (day.equals(Day.MONDAY)) { }  // NullPointerException!

// Type safety with ==
// if (day == p) { }  // Compile error! Different enum types

// No type safety with equals
if (day != null && day.equals(p)) { }  // Compiles, always false
```

---

## Enum Best Practices

### 1. Use Enums Instead of Constants

```java
// Bad - magic numbers/strings
public static final int STATUS_PENDING = 0;
public static final String STATUS_ACTIVE = "active";

// Good - enum
public enum Status {
    PENDING, ACTIVE, INACTIVE
}
```

### 2. Add Useful Methods

```java
public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "Internal Server Error");
    
    private final int code;
    private final String message;
    
    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() { return code; }
    public String getMessage() { return message; }
    
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }
    
    public boolean isError() {
        return code >= 400;
    }
}
```

### 3. Consider Using EnumSet for Flags

```java
// Instead of bit flags
public static final int FLAG_READ = 1;
public static final int FLAG_WRITE = 2;
public static final int FLAG_EXECUTE = 4;
int permissions = FLAG_READ | FLAG_WRITE;

// Use EnumSet
EnumSet<Permission> permissions = EnumSet.of(Permission.READ, Permission.WRITE);
```

### 4. Use EnumMap for Enum-Keyed Maps

```java
// Instead of HashMap
Map<Day, String> schedule = new HashMap<>();

// Use EnumMap - faster and more memory efficient
Map<Day, String> schedule = new EnumMap<>(Day.class);
```

### 5. Document Enum Constants

```java
public enum OrderStatus {
    /**
     * Order has been placed but not yet processed.
     */
    PENDING,
    
    /**
     * Order is being prepared for shipment.
     */
    PROCESSING,
    
    /**
     * Order has been shipped and is in transit.
     */
    SHIPPED,
    
    /**
     * Order has been delivered to the customer.
     */
    DELIVERED,
    
    /**
     * Order was cancelled before shipment.
     */
    CANCELLED
}
```

### 6. Use valueOf() Carefully

```java
// Unsafe - throws exception
Day day = Day.valueOf(userInput);

// Safe - handle invalid input
public static Day parseDay(String input) {
    if (input == null || input.isBlank()) {
        return null;
    }
    try {
        return Day.valueOf(input.toUpperCase().trim());
    } catch (IllegalArgumentException e) {
        return null;
    }
}
```

---

## Common Mistakes

### Mistake 1: Relying on ordinal()

```java
// Bad - ordinal changes if constants reordered
int priority = taskPriority.ordinal();
database.savePriority(priority);

// Good - use explicit value
public enum Priority {
    LOW(1), MEDIUM(2), HIGH(3);
    
    private final int value;
    
    Priority(int value) { this.value = value; }
    
    public int getValue() { return value; }
}

database.savePriority(taskPriority.getValue());
```

### Mistake 2: Using == with Different Enum Types

```java
// This won't even compile - that's the point!
Day day = Day.MONDAY;
Priority priority = Priority.HIGH;
// if (day == priority) { }  // Compile error!
```

### Mistake 3: Forgetting Case Sensitivity in valueOf()

```java
// Throws IllegalArgumentException
Day day = Day.valueOf("monday");

// Fix: convert to uppercase
Day day = Day.valueOf(input.toUpperCase());
```

### Mistake 4: Modifying Enum Fields

```java
// Bad - mutable field
public enum Status {
    ACTIVE, INACTIVE;
    
    private int count;  // Mutable - bad!
    
    public void increment() {
        count++;  // Shared across all uses!
    }
}

// Good - immutable fields
public enum Status {
    ACTIVE, INACTIVE;
    
    private final String description;  // Final - good!
    
    Status(String description) {
        this.description = description;
    }
}
```

### Mistake 5: Creating Too Many Constants

If you have dozens of constants, consider if an enum is the right choice:

```java
// Too many - might be better as a class
public enum Country {
    AFGHANISTAN, ALBANIA, ALGERIA, // ... 190+ more
}

// Consider a class instead
public class Country {
    private final String code;
    private final String name;
    // ...
}
```

---

## Cheat Sheet

| Feature | Syntax |
|---------|--------|
| Define | `enum Name { A, B, C }` |
| Use | `Name.A` |
| Get name | `name()` |
| Get ordinal | `ordinal()` |
| Get all | `values()` |
| From string | `valueOf("A")` |
| With field | `A(value)` |
| Abstract method | Each constant implements |
| Implement interface | `enum Name implements I` |

| Collection | Usage |
|------------|-------|
| `EnumSet` | Set of enum constants |
| `EnumMap` | Map with enum keys |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Generics](./21-generics.md) | [Guide](../guide.md) | [Annotations](./23-annotations.md) |
