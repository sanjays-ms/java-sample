# Enums Cheatsheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/22-enums.md)

---

## Quick Reference

```java
// Basic enum
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

// Usage
Day today = Day.MONDAY;
if (today == Day.MONDAY) { }
```

---

## Built-in Methods

| Method | Returns | Example |
|--------|---------|---------|
| `name()` | String constant name | `Day.MONDAY.name()` → `"MONDAY"` |
| `ordinal()` | int position (0-based) | `Day.MONDAY.ordinal()` → `0` |
| `values()` | Array of all constants | `Day.values()` → `[MONDAY, ...]` |
| `valueOf(String)` | Enum from string | `Day.valueOf("MONDAY")` → `Day.MONDAY` |
| `toString()` | String (can override) | `Day.MONDAY.toString()` → `"MONDAY"` |
| `compareTo()` | int (by ordinal) | `MONDAY.compareTo(FRIDAY)` → negative |

---

## Enum with Fields

```java
public enum Size {
    SMALL("S", 10.00),
    MEDIUM("M", 15.00),
    LARGE("L", 20.00);
    
    private final String code;
    private final double price;
    
    Size(String code, double price) {
        this.code = code;
        this.price = price;
    }
    
    public String getCode() { return code; }
    public double getPrice() { return price; }
}

// Usage
Size.LARGE.getCode();   // "L"
Size.LARGE.getPrice();  // 20.00
```

---

## Enum with Abstract Method

```java
public enum Operation {
    ADD {
        @Override
        public double apply(double a, double b) {
            return a + b;
        }
    },
    SUBTRACT {
        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    };
    
    public abstract double apply(double a, double b);
}

// Usage
double result = Operation.ADD.apply(5, 3);  // 8.0
```

---

## Enum Implementing Interface

```java
public enum StringOp implements UnaryOperator<String> {
    UPPER {
        @Override
        public String apply(String s) {
            return s.toUpperCase();
        }
    },
    LOWER {
        @Override
        public String apply(String s) {
            return s.toLowerCase();
        }
    };
}

// Usage
String result = StringOp.UPPER.apply("hello");  // "HELLO"
```

---

## Switch with Enums

```java
// Traditional switch
switch (day) {
    case MONDAY:
        return "Start of week";
    case FRIDAY:
        return "Almost weekend";
    default:
        return "Regular day";
}

// Switch expression (Java 14+)
return switch (day) {
    case MONDAY -> "Start of week";
    case FRIDAY -> "Almost weekend";
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Regular day";
};
```

---

## EnumSet

```java
// Create
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
EnumSet<Day> allDays = EnumSet.allOf(Day.class);
EnumSet<Day> noDays = EnumSet.noneOf(Day.class);
EnumSet<Day> notWeekend = EnumSet.complementOf(weekend);

// Use
weekend.contains(Day.SATURDAY);  // true
weekend.add(Day.FRIDAY);
weekend.remove(Day.FRIDAY);

// Iteration
for (Day day : weekend) {
    System.out.println(day);
}
```

---

## EnumMap

```java
// Create
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);

// Use
schedule.put(Day.MONDAY, "Meeting");
schedule.put(Day.FRIDAY, "Demo");

String monday = schedule.get(Day.MONDAY);  // "Meeting"
String tuesday = schedule.getOrDefault(Day.TUESDAY, "Work");

// Iteration
for (Map.Entry<Day, String> entry : schedule.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

---

## Singleton Pattern

```java
public enum DatabaseConnection {
    INSTANCE;
    
    private Connection connection;
    
    DatabaseConnection() {
        // Initialize once
        connection = createConnection();
    }
    
    public Connection getConnection() {
        return connection;
    }
}

// Usage
Connection conn = DatabaseConnection.INSTANCE.getConnection();
```

---

## Conversions

| From | To | Code |
|------|-----|------|
| Enum | String | `day.name()` or `day.toString()` |
| String | Enum | `Day.valueOf("MONDAY")` |
| Enum | int | `day.ordinal()` (avoid) or custom field |
| int | Enum | `Day.values()[index]` (avoid) or lookup map |

### Safe String to Enum

```java
public static <T extends Enum<T>> T safeValueOf(Class<T> type, String name) {
    try {
        return Enum.valueOf(type, name.toUpperCase());
    } catch (Exception e) {
        return null;
    }
}

Day day = safeValueOf(Day.class, "monday");  // Day.MONDAY
Day invalid = safeValueOf(Day.class, "xyz"); // null
```

---

## Comparison

| Method | Null-Safe | Type-Safe | Recommended |
|--------|-----------|-----------|-------------|
| `==` | Yes | Yes | Yes |
| `equals()` | No* | No | Use constant on left |
| `compareTo()` | No | Yes | For ordering |

```java
Day day = null;

// Safe
if (day == Day.MONDAY) { }           // false

// Unsafe
if (day.equals(Day.MONDAY)) { }      // NullPointerException!

// Safe with equals
if (Day.MONDAY.equals(day)) { }      // false
```

---

## Common Patterns

### Lookup by Custom Value

```java
public enum Status {
    ACTIVE(1), INACTIVE(0);
    
    private final int code;
    private static final Map<Integer, Status> BY_CODE = new HashMap<>();
    
    static {
        for (Status s : values()) {
            BY_CODE.put(s.code, s);
        }
    }
    
    Status(int code) { this.code = code; }
    
    public static Status fromCode(int code) {
        return BY_CODE.get(code);
    }
}

Status s = Status.fromCode(1);  // Status.ACTIVE
```

### Override toString()

```java
public enum Priority {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");
    
    private final String display;
    
    Priority(String display) { this.display = display; }
    
    @Override
    public String toString() { return display; }
}

System.out.println(Priority.HIGH);  // "High"
Priority.HIGH.name();               // "HIGH"
```

---

## Best Practices

| Do | Don't |
|----|-------|
| Use `==` for comparison | Rely on `ordinal()` for persistence |
| Add meaningful fields/methods | Use mutable fields |
| Use `EnumSet` for flag sets | Create huge enums (100+ constants) |
| Use `EnumMap` for enum keys | Use `HashMap` with enum keys |
| Use `EnumType.STRING` in JPA | Use `EnumType.ORDINAL` in JPA |
| Handle `valueOf()` exceptions | Assume input is always valid |

---

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `valueOf("monday")` | Case mismatch | `valueOf(s.toUpperCase())` |
| Rely on `ordinal()` | Changes if reordered | Use explicit field |
| Mutable enum fields | Shared state bugs | Make fields `final` |
| Forget null check with `equals()` | NPE | Use `==` or constant on left |

---

## JPA/Database

```java
@Entity
public class Order {
    
    @Enumerated(EnumType.STRING)  // Recommended!
    private OrderStatus status;
    
    // NOT recommended - fragile if reordered
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
}
```

---

## JSON (Jackson)

```java
public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive");
    
    private final String json;
    
    Status(String json) { this.json = json; }
    
    @JsonValue
    public String toJson() { return json; }
    
    @JsonCreator
    public static Status fromJson(String value) {
        for (Status s : values()) {
            if (s.json.equals(value)) return s;
        }
        throw new IllegalArgumentException(value);
    }
}
```

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/22-enums.md)
