# 03. Control Flow

[<- Back: Operators](./02-operators.md) | [Back to Guide](../guide.md) | [Next: Arrays ->](./04-arrays.md)

**Quick Reference:** [Control Flow Cheatsheet](../cheatsheets/control-flow-cheatsheet.md)

---

## What Is Control Flow?

Control flow is how you tell Java which code to run and when. Without control flow, Java would simply execute every line from top to bottom, one after another.

**In plain words:** Control flow lets your program make decisions ("if this, do that") and repeat actions ("do this 10 times").

**Real-world analogy:** Think of control flow like a flowchart. At each decision point, you check a condition and go down a different path. Loops are like "go back and repeat until done."

---

## Why Control Flow Matters

Without control flow, programs would be extremely limited:
- They could not make decisions based on data
- They could not repeat operations
- They could not handle different scenarios

**Example - What control flow enables:**
```java
// Without control flow - can only do one thing
System.out.println("Processing order...");

// With control flow - can handle different scenarios
int orderTotal = 150;

if (orderTotal >= 100) {
    System.out.println("Free shipping applied!");
} else {
    System.out.println("Shipping: $5.99");
}
```

Output:
```
Free shipping applied!
```

---

## Overview

Control flow statements determine the order in which code executes. Java provides several control flow constructs:

**Visual: Control Flow Categories:**

```
    CONTROL FLOW IN JAVA
    ────────────────────
    
    ┌─────────────────────────────────────────────────────────────┐
    │  CONDITIONAL STATEMENTS                                     │
    │  ───────────────────────                                    │
    │  Execute code based on conditions                           │
    │                                                             │
    │  if-else     →  "If this is true, do A, otherwise do B"    │
    │  switch      →  "Based on this value, do X, Y, or Z"       │
    ├─────────────────────────────────────────────────────────────┤
    │  LOOPS                                                      │
    │  ─────                                                      │
    │  Repeat code multiple times                                 │
    │                                                             │
    │  for         →  "Repeat exactly N times"                   │
    │  for-each    →  "Do this for each item in the collection"  │
    │  while       →  "Repeat while condition is true"           │
    │  do-while    →  "Do once, then repeat while true"          │
    ├─────────────────────────────────────────────────────────────┤
    │  BRANCHING STATEMENTS                                       │
    │  ────────────────────                                       │
    │  Alter the flow within loops                                │
    │                                                             │
    │  break       →  "Stop the loop immediately"                │
    │  continue    →  "Skip to the next iteration"               │
    │  return      →  "Exit the method entirely"                 │
    └─────────────────────────────────────────────────────────────┘
```

These constructs are fundamental to any Java program, allowing you to make decisions and repeat operations.

---

## If Statement

The `if` statement executes a block of code only if a condition is true.

**Visual: How If Works:**

```
                ┌───────────────┐
                │ Is condition  │
                │    true?      │
                └───────┬───────┘
                        │
              ┌─────────┴─────────┐
              │                   │
           YES (true)          NO (false)
              │                   │
              ▼                   ▼
    ┌─────────────────┐   ┌─────────────────┐
    │ Execute if      │   │ Skip if block   │
    │ block           │   │ (or run else)   │
    └─────────────────┘   └─────────────────┘
```

### Basic If

```java
int age = 20;

if (age >= 18) {
    System.out.println("You are an adult");
}
// Output: You are an adult
```

**Always use braces for clarity and to avoid bugs:**
```java
// Single statement (braces optional but recommended)
if (age >= 18)
    System.out.println("You are an adult");

// BETTER: Always use braces
if (age >= 18) {
    System.out.println("You are an adult");
    System.out.println("You can vote");  // Clearly part of the if block
}
// Output:
// You are an adult
// You can vote
```

### If-Else

The `else` block executes when the condition is false.

```java
int temperature = 15;

if (temperature > 30) {
    System.out.println("It's hot");
} else {
    System.out.println("It's not hot");
}
// Output: It's not hot
```

**Enterprise example - Handling API responses:**
```java
int statusCode = 404;

if (statusCode == 200) {
    System.out.println("Request successful");
} else {
    System.out.println("Request failed with code: " + statusCode);
}
// Output: Request failed with code: 404
```

### If-Else-If Chain

Use `else if` to check multiple conditions in sequence.

```java
int score = 85;

if (score >= 90) {
    System.out.println("Grade: A");
} else if (score >= 80) {
    System.out.println("Grade: B");
} else if (score >= 70) {
    System.out.println("Grade: C");
} else if (score >= 60) {
    System.out.println("Grade: D");
} else {
    System.out.println("Grade: F");
}
// Output: Grade: B
```

**Enterprise example - HTTP status code handling:**
```java
int statusCode = 404;

if (statusCode >= 200 && statusCode < 300) {
    System.out.println("Success");
} else if (statusCode >= 400 && statusCode < 500) {
    System.out.println("Client error");
} else if (statusCode >= 500) {
    System.out.println("Server error");
} else {
    System.out.println("Unknown status");
}
// Output: Client error
```

**Important:** Only the first matching condition executes. Once a condition is true, the remaining conditions are skipped.

```java
int value = 15;

// Only "Greater than 5" prints, even though both conditions are true
if (value > 5) {
    System.out.println("Greater than 5");
} else if (value > 10) {
    System.out.println("Greater than 10");  // Never reached!
}
// Output: Greater than 5

// To check both independently, use separate if statements
if (value > 5) {
    System.out.println("Greater than 5");
}
if (value > 10) {
    System.out.println("Greater than 10");
}
```

### Nested If Statements

You can nest if statements inside other if statements.

```java
int age = 25;
boolean hasLicense = true;

if (age >= 18) {
    if (hasLicense) {
        System.out.println("You can drive");
    } else {
        System.out.println("You need a license");
    }
} else {
    System.out.println("You are too young to drive");
}
// Output: You can drive

// Often cleaner to combine conditions with &&
if (age >= 18 && hasLicense) {
    System.out.println("You can drive");
} else if (age >= 18) {
    System.out.println("You need a license");
} else {
    System.out.println("You are too young to drive");
}
// Output: You can drive
```

### Common Patterns (Enterprise)

**Null check before accessing:**
```java
String name = getUserName();  // might return null
if (name != null) {
    System.out.println(name.toUpperCase());
}
// Prevents NullPointerException
```

**Bounds checking:**
```java
int index = 5;
int[] array = {1, 2, 3};
if (index >= 0 && index < array.length) {
    System.out.println(array[index]);
} else {
    System.out.println("Index out of bounds");
}
// Output: Index out of bounds
```

**Early return pattern (guard clauses):**
```java
public void processUser(User user) {
    if (user == null) {
        return;  // Exit early if invalid
    }
    
    if (user.getId() <= 0) {
        return;  // Exit early if invalid ID
    }
    
    // Only reach here with valid user
    System.out.println("Processing: " + user.getName());
}
```

**Input validation:**
```java
public double divide(double a, double b) {
    if (b == 0) {
        throw new IllegalArgumentException("Cannot divide by zero");
    }
    return a / b;
}
```

---

## Switch Statement

The switch statement selects one of many code blocks to execute based on a value. It is often cleaner than long if-else-if chains when comparing a single variable against multiple values.

```
How Switch Works:
                                 
    value ──┬── case 1: ───► Execute block 1, break
            │
            ├── case 2: ───► Execute block 2, break
            │
            ├── case 3: ───► Execute block 3, break
            │
            └── default: ──► Execute default block
```

### Traditional Switch

```java
int day = 3;

switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    case 4:
        System.out.println("Thursday");
        break;
    case 5:
        System.out.println("Friday");
        break;
    case 6:
        System.out.println("Saturday");
        break;
    case 7:
        System.out.println("Sunday");
        break;
    default:
        System.out.println("Invalid day");
}
// Output: Wednesday
```

**The break statement is crucial.** Without it, execution "falls through" to the next case.

```java
int day = 2;

// Without break - falls through!
switch (day) {
    case 1:
        System.out.println("Monday");
    case 2:
        System.out.println("Tuesday");   // Prints
    case 3:
        System.out.println("Wednesday"); // Also prints (fall-through)
        break;
    case 4:
        System.out.println("Thursday");
}
// Output:
// Tuesday
// Wednesday
```

### Fall-Through (Intentional)

Sometimes fall-through is useful to group cases.

```java
int month = 2;
int year = 2024;
int days;

switch (month) {
    case 1: case 3: case 5: case 7: case 8: case 10: case 12:
        days = 31;
        break;
    case 4: case 6: case 9: case 11:
        days = 30;
        break;
    case 2:
        days = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
        break;
    default:
        days = 0;
}

System.out.println("Days in month: " + days);
// Output: Days in month: 29
```

### Switch with Strings

Since Java 7, switch works with Strings.

```java
String command = "start";

switch (command.toLowerCase()) {
    case "start":
        System.out.println("Starting...");
        break;
    case "stop":
        System.out.println("Stopping...");
        break;
    case "restart":
        System.out.println("Restarting...");
        break;
    default:
        System.out.println("Unknown command");
}
// Output: Starting...
```

### Switch with Enums

Switch works naturally with enums.

```java
enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

Day today = Day.WEDNESDAY;

switch (today) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println("Weekday");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println("Weekend");
        break;
}
// Output: Weekday
```

### Enterprise Example: Order Status Processing

```java
public class OrderService {
    
    enum OrderStatus { 
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED 
    }
    
    public void handleOrderStatus(OrderStatus status) {
        switch (status) {
            case PENDING:
                System.out.println("Order awaiting payment confirmation");
                notifyPaymentTeam();
                break;
            case PROCESSING:
                System.out.println("Order being prepared");
                notifyWarehouse();
                break;
            case SHIPPED:
                System.out.println("Order shipped to customer");
                sendTrackingEmail();
                break;
            case DELIVERED:
                System.out.println("Order delivered successfully");
                requestFeedback();
                break;
            case CANCELLED:
                System.out.println("Order cancelled - initiating refund");
                processRefund();
                break;
            case RETURNED:
                System.out.println("Order returned - processing");
                processReturn();
                break;
            default:
                System.out.println("Unknown status");
        }
    }
    
    // Helper methods (simplified)
    private void notifyPaymentTeam() { }
    private void notifyWarehouse() { }
    private void sendTrackingEmail() { }
    private void requestFeedback() { }
    private void processRefund() { }
    private void processReturn() { }
}
```

---

## Switch Expressions (Java 14+)

Java 14 introduced switch expressions, which are more concise and safer (no fall-through issues).

### Arrow Syntax

The arrow `->` syntax eliminates the need for break statements.

```java
int day = 3;

// Switch expression with arrow syntax
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    case 4 -> "Thursday";
    case 5 -> "Friday";
    case 6 -> "Saturday";
    case 7 -> "Sunday";
    default -> "Invalid";
};

System.out.println(dayName);
// Output: Wednesday
```

### Multiple Labels

Combine multiple cases with commas.

```java
int day = 6;

String dayType = switch (day) {
    case 1, 2, 3, 4, 5 -> "Weekday";
    case 6, 7 -> "Weekend";
    default -> "Invalid";
};

System.out.println(dayType);
// Output: Weekend
```

### Block with yield

Use a block and `yield` when you need multiple statements.

```java
int day = 5;

String description = switch (day) {
    case 1 -> "Start of work week";
    case 2, 3, 4 -> "Middle of work week";
    case 5 -> {
        System.out.println("TGIF!");  // Can have multiple statements
        yield "End of work week";      // yield returns the value
    }
    case 6, 7 -> "Weekend!";
    default -> "Invalid day";
};

System.out.println(description);
// Output:
// TGIF!
// End of work week
```

### Enterprise Example: User Role Permissions (Switch Expression)

```java
public class PermissionService {
    
    enum UserRole { GUEST, USER, MODERATOR, ADMIN, SUPER_ADMIN }
    
    public int getPermissionLevel(UserRole role) {
        return switch (role) {
            case GUEST -> 0;
            case USER -> 1;
            case MODERATOR -> 2;
            case ADMIN -> 3;
            case SUPER_ADMIN -> 4;
        };
        // No default needed - all enum values covered
    }
    
    public String getAccessibleFeatures(UserRole role) {
        return switch (role) {
            case GUEST -> "View public content only";
            case USER -> "View content, create posts, comment";
            case MODERATOR -> "All user features + delete posts, ban users";
            case ADMIN -> "All moderator features + manage roles, view analytics";
            case SUPER_ADMIN -> {
                logSuperAdminAccess();  // Additional logging for auditing
                yield "Full system access including system configuration";
            }
        };
    }
    
    private void logSuperAdminAccess() {
        System.out.println("Super admin access logged for audit");
    }
}

// Usage
PermissionService service = new PermissionService();
System.out.println(service.getPermissionLevel(UserRole.ADMIN));
// Output: 3

System.out.println(service.getAccessibleFeatures(UserRole.MODERATOR));
// Output: All moderator features + delete posts, ban users
```

### Exhaustiveness

Switch expressions must be exhaustive (cover all possible values). The compiler enforces this.

```java
enum Season { SPRING, SUMMER, FALL, WINTER }

Season season = Season.SUMMER;

// Must cover all enum values or have default
String weather = switch (season) {
    case SPRING -> "Mild";
    case SUMMER -> "Hot";
    case FALL -> "Cool";
    case WINTER -> "Cold";
    // No default needed - all enum values covered
};

System.out.println(weather);
// Output: Hot

// With other types, default is required
int value = 5;
String result = switch (value) {
    case 1 -> "One";
    case 2 -> "Two";
    default -> "Other";  // Required for non-enum types
};

System.out.println(result);
// Output: Other
```

### Switch Expression vs Statement

```java
int day = 3;

// Switch STATEMENT (traditional) - no value returned
switch (day) {
    case 1 -> System.out.println("Monday");
    case 2 -> System.out.println("Tuesday");
    default -> System.out.println("Other day");
}
// Output: Other day

// Switch EXPRESSION - returns a value
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    default -> "Other day";
};

System.out.println(dayName);
// Output: Other day
```

---

## Pattern Matching in Switch (Java 21+)

Java 21 introduced pattern matching for switch, allowing type patterns and guards.

### Type Patterns

```java
Object obj = "Hello";

String result = switch (obj) {
    case Integer i -> "Integer: " + i;
    case String s -> "String: " + s;
    case Double d -> "Double: " + d;
    case null -> "Null value";
    default -> "Unknown type";
};

System.out.println(result);  // String: Hello
```

### Guarded Patterns

Use `when` to add conditions to patterns.

```java
Object obj = 15;

String result = switch (obj) {
    case Integer i when i < 0 -> "Negative integer";
    case Integer i when i == 0 -> "Zero";
    case Integer i when i > 0 -> "Positive integer";
    case String s when s.isEmpty() -> "Empty string";
    case String s -> "String: " + s;
    case null -> "Null";
    default -> "Other";
};

System.out.println(result);  // Positive integer
```

### Record Patterns

Deconstruct records directly in switch.

```java
record Point(int x, int y) {}

Object obj = new Point(3, 4);

String result = switch (obj) {
    case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
    case Point(int x, int y) when x == y -> "On diagonal";
    case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
    default -> "Not a point";
};

System.out.println(result);  // Point at (3, 4)
```

---

## For Loop

The for loop repeats a block of code a specific number of times.

```
How a For Loop Works:

    ┌─────────────────────────────────────────────┐
    │  for (int i = 0; i < 5; i++) { ... }        │
    │       ─────────  ─────  ───                 │
    │          │         │     │                  │
    │    Initialize   Condition  Update           │
    │    (runs once)  (each loop) (after body)    │
    └─────────────────────────────────────────────┘
    
    Execution flow:
    
    [Initialize] ──► [Check Condition] ──► false ──► EXIT
                            │
                           true
                            │
                            ▼
                     [Execute Body]
                            │
                            ▼
                     [Update Counter] ───► [Check Condition] ...
```

### Basic For Loop

```java
// Syntax: for (initialization; condition; update)
for (int i = 0; i < 5; i++) {
    System.out.println("i = " + i);
}
// Output:
// i = 0
// i = 1
// i = 2
// i = 3
// i = 4

// Counting down
for (int i = 5; i > 0; i--) {
    System.out.println("Countdown: " + i);
}
// Output:
// Countdown: 5
// Countdown: 4
// Countdown: 3
// Countdown: 2
// Countdown: 1

// Different step
for (int i = 0; i <= 10; i += 2) {
    System.out.println(i);
}
// Output: 0, 2, 4, 6, 8, 10
```

### Loop Components

Each part of the for loop is optional:

```java
// All parts
for (int i = 0; i < 5; i++) { }

// Variable declared outside
int i = 0;
for (; i < 5; i++) {
    System.out.println(i);
}
// Output: 0, 1, 2, 3, 4

// Infinite loop (be careful!)
for (;;) {
    // Runs forever unless break is used
    break;  // Exit the loop
}

// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println("i=" + i + ", j=" + j);
}
// Output:
// i=0, j=10
// i=1, j=9
// i=2, j=8
// i=3, j=7
// i=4, j=6
```

### Iterating Over Arrays

```java
int[] numbers = {10, 20, 30, 40, 50};

// Using index
for (int i = 0; i < numbers.length; i++) {
    System.out.println("Index " + i + ": " + numbers[i]);
}
// Output:
// Index 0: 10
// Index 1: 20
// Index 2: 30
// Index 3: 40
// Index 4: 50

// Reverse iteration
for (int i = numbers.length - 1; i >= 0; i--) {
    System.out.println(numbers[i]);
}
// Output: 50, 40, 30, 20, 10
```

### Enterprise Example: Batch Processing

```java
import java.util.List;
import java.util.ArrayList;

public class BatchProcessor {
    
    private static final int BATCH_SIZE = 100;
    
    public void processRecordsInBatches(List<String> records) {
        int totalRecords = records.size();
        int processedCount = 0;
        
        // Process in batches of BATCH_SIZE
        for (int i = 0; i < totalRecords; i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, totalRecords);
            List<String> batch = records.subList(i, endIndex);
            
            processBatch(batch);
            processedCount += batch.size();
            
            System.out.println("Processed: " + processedCount + "/" + totalRecords);
        }
        
        System.out.println("Batch processing complete");
    }
    
    private void processBatch(List<String> batch) {
        // Simulate batch processing
        System.out.println("Processing batch of " + batch.size() + " records");
    }
}

// Usage
List<String> records = new ArrayList<>();
for (int i = 0; i < 350; i++) {
    records.add("Record-" + i);
}

BatchProcessor processor = new BatchProcessor();
processor.processRecordsInBatches(records);
// Output:
// Processing batch of 100 records
// Processed: 100/350
// Processing batch of 100 records
// Processed: 200/350
// Processing batch of 100 records
// Processed: 300/350
// Processing batch of 50 records
// Processed: 350/350
// Batch processing complete
```

---

## Enhanced For Loop (For-Each)

The enhanced for loop (for-each) provides a simpler way to iterate over arrays and collections.

### Syntax

```java
// Syntax: for (type element : arrayOrCollection)
int[] numbers = {1, 2, 3, 4, 5};

for (int num : numbers) {
    System.out.println(num);
}
// Output:
// 1
// 2
// 3
// 4
// 5

// With collections
List<String> names = List.of("Alice", "Bob", "Charlie");

for (String name : names) {
    System.out.println(name);
}
// Output:
// Alice
// Bob
// Charlie
```

### When to Use For-Each vs Traditional For

**Use for-each when:**
- You only need to read elements
- You do not need the index
- You want cleaner, more readable code

```java
// Good for-each use case
String[] fruits = {"Apple", "Banana", "Cherry"};
for (String fruit : fruits) {
    System.out.println(fruit);
}
// Output:
// Apple
// Banana
// Cherry
```

**Use traditional for when:**
- You need the index
- You need to modify elements
- You need to iterate in a specific way (backwards, skip elements)

```java
String[] fruits = {"Apple", "Banana", "Cherry"};

// Need index
for (int i = 0; i < fruits.length; i++) {
    System.out.println((i + 1) + ". " + fruits[i]);
}
// Output:
// 1. Apple
// 2. Banana
// 3. Cherry

// Need to modify array
int[] nums = {1, 2, 3};
for (int i = 0; i < nums.length; i++) {
    nums[i] = nums[i] * 2;  // Can not do this with for-each
}
System.out.println(Arrays.toString(nums));
// Output: [2, 4, 6]

// Skip elements
for (int i = 0; i < nums.length; i += 2) {
    System.out.println(nums[i]);  // Every other element
}
// Output: 2, 6
```

### Limitation: Cannot Modify Collection

The for-each loop gives you a copy of each element. Modifying it does not affect the original.

```java
int[] numbers = {1, 2, 3};

for (int num : numbers) {
    num = num * 2;  // This modifies the local copy, not the array!
}

// Array is unchanged
System.out.println(Arrays.toString(numbers));
// Output: [1, 2, 3]

// To modify, use traditional for loop
for (int i = 0; i < numbers.length; i++) {
    numbers[i] = numbers[i] * 2;
}
System.out.println(Arrays.toString(numbers));
// Output: [2, 4, 6]
```

---

## While Loop

The while loop repeats as long as a condition is true. Use it when you do not know in advance how many iterations you need.

```
How a While Loop Works:

    ┌──────────────────────────────────┐
    │  while (condition) { ... }       │
    └──────────────────────────────────┘
    
    Execution flow:
    
    [Check Condition] ──► false ──► EXIT
           │
          true
           │
           ▼
    [Execute Body] ───► [Check Condition] ...
```

### Basic While Loop

```java
int count = 0;

while (count < 5) {
    System.out.println("Count: " + count);
    count++;
}
// Output:
// Count: 0
// Count: 1
// Count: 2
// Count: 3
// Count: 4
```

### Common Patterns

```java
// Reading until a condition
Scanner scanner = new Scanner(System.in);
String input = "";

while (!input.equals("quit")) {
    System.out.print("Enter command (quit to exit): ");
    input = scanner.nextLine();
    System.out.println("You entered: " + input);
}
// User types: hello, world, quit
// Output:
// Enter command (quit to exit): hello
// You entered: hello
// Enter command (quit to exit): world
// You entered: world
// Enter command (quit to exit): quit
// You entered: quit

// Processing a queue
Queue<String> tasks = new LinkedList<>();
tasks.add("Task 1");
tasks.add("Task 2");
tasks.add("Task 3");

while (!tasks.isEmpty()) {
    String task = tasks.poll();
    System.out.println("Processing: " + task);
}
// Output:
// Processing: Task 1
// Processing: Task 2
// Processing: Task 3

// Finding a value
int[] numbers = {4, 7, 2, 9, 1, 5};
int target = 9;
int index = 0;

while (index < numbers.length && numbers[index] != target) {
    index++;
}

if (index < numbers.length) {
    System.out.println("Found at index: " + index);
}
// Output: Found at index: 3
```

### Enterprise Example: Retry Logic with Exponential Backoff

```java
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    
    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_DELAY_MS = 1000;  // 1 second
    
    public String callApiWithRetry(String apiUrl) throws Exception {
        int attempt = 0;
        long delayMs = INITIAL_DELAY_MS;
        Exception lastException = null;
        
        while (attempt < MAX_RETRIES) {
            try {
                System.out.println("Attempt " + (attempt + 1) + " of " + MAX_RETRIES);
                String response = callApi(apiUrl);
                System.out.println("Success on attempt " + (attempt + 1));
                return response;
                
            } catch (Exception e) {
                lastException = e;
                attempt++;
                
                if (attempt < MAX_RETRIES) {
                    System.out.println("Failed. Retrying in " + delayMs + "ms...");
                    Thread.sleep(delayMs);
                    delayMs *= 2;  // Exponential backoff: 1s, 2s, 4s, 8s...
                }
            }
        }
        
        System.out.println("All " + MAX_RETRIES + " attempts failed");
        throw lastException;
    }
    
    private String callApi(String urlString) throws Exception {
        // Simplified API call
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP error: " + responseCode);
        }
        
        return "Response data";
    }
}

// Example output when API fails initially:
// Attempt 1 of 5
// Failed. Retrying in 1000ms...
// Attempt 2 of 5
// Failed. Retrying in 2000ms...
// Attempt 3 of 5
// Success on attempt 3
```

### Infinite Loop

```java
// Intentional infinite loop
while (true) {
    // Server loop, game loop, etc.
    // Must have a break condition or run until program stops
    
    if (shouldStop) {
        break;
    }
}
```

### Watch Out for Infinite Loops

```java
// Bug: i never changes, loop runs forever
int i = 0;
while (i < 5) {
    System.out.println(i);
    // Forgot to increment i! This will print 0 forever
}

// Fix: increment the counter
int i = 0;
while (i < 5) {
    System.out.println(i);
    i++;  // Do not forget this!
}
// Output: 0, 1, 2, 3, 4
```

---

## Do-While Loop

The do-while loop executes the block at least once, then repeats while the condition is true.

```
How a Do-While Loop Works:

    ┌──────────────────────────────────────────┐
    │  do { ... } while (condition);           │
    └──────────────────────────────────────────┘
    
    Execution flow:
    
    [Execute Body] ───► [Check Condition] ──► false ──► EXIT
                               │
                              true
                               │
                               └──────► [Execute Body] ...
    
    Key difference: Body executes at least ONCE before condition check
```

### Basic Do-While

```java
int count = 0;

do {
    System.out.println("Count: " + count);
    count++;
} while (count < 5);
// Output:
// Count: 0
// Count: 1
// Count: 2
// Count: 3
// Count: 4
```

### Difference from While

The key difference: do-while always executes at least once, even if the condition is initially false.

```java
int x = 10;

// While: condition checked FIRST - never executes
while (x < 5) {
    System.out.println("While: " + x);
}
// Output: (nothing - condition is false)

// Do-while: executes ONCE, then checks condition
do {
    System.out.println("Do-while: " + x);
} while (x < 5);
// Output: Do-while: 10
// (executes once even though condition is false)
```

### Common Use Cases

```java
// Menu-driven programs
Scanner scanner = new Scanner(System.in);
int choice;

do {
    System.out.println("\n1. Add");
    System.out.println("2. Delete");
    System.out.println("3. View");
    System.out.println("0. Exit");
    System.out.print("Enter choice: ");
    choice = scanner.nextInt();
    
    switch (choice) {
        case 1 -> System.out.println("Adding...");
        case 2 -> System.out.println("Deleting...");
        case 3 -> System.out.println("Viewing...");
        case 0 -> System.out.println("Goodbye!");
        default -> System.out.println("Invalid choice");
    }
} while (choice != 0);
// Sample Output:
// 1. Add
// 2. Delete
// 3. View
// 0. Exit
// Enter choice: 1
// Adding...
// (menu repeats until user enters 0)

// Input validation
int number;
do {
    System.out.print("Enter a positive number: ");
    number = scanner.nextInt();
} while (number <= 0);

System.out.println("You entered: " + number);
// If user enters -5, 0, 3:
// Enter a positive number: -5
// Enter a positive number: 0
// Enter a positive number: 3
// You entered: 3
```

### Enterprise Example: Polling for Job Completion

```java
import java.util.UUID;

public class JobPoller {
    
    private static final long POLL_INTERVAL_MS = 2000;  // 2 seconds
    private static final long TIMEOUT_MS = 60000;       // 1 minute
    
    public boolean waitForJobCompletion(String jobId) {
        long startTime = System.currentTimeMillis();
        String status;
        
        do {
            status = checkJobStatus(jobId);
            System.out.println("Job " + jobId + " status: " + status);
            
            if (status.equals("COMPLETED")) {
                System.out.println("Job completed successfully");
                return true;
            }
            
            if (status.equals("FAILED")) {
                System.out.println("Job failed");
                return false;
            }
            
            // Wait before next poll
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            
        } while (System.currentTimeMillis() - startTime < TIMEOUT_MS);
        
        System.out.println("Job timed out after " + TIMEOUT_MS + "ms");
        return false;
    }
    
    private String checkJobStatus(String jobId) {
        // Simulate checking job status from database or API
        // In real code, this would query a database or call an API
        return "PROCESSING";  // or "COMPLETED", "FAILED"
    }
}

// Example output:
// Job abc123 status: PROCESSING
// Job abc123 status: PROCESSING
// Job abc123 status: PROCESSING
// Job abc123 status: COMPLETED
// Job completed successfully
```

---

## Break Statement

The `break` statement exits a loop or switch immediately.

### Break in Loops

```java
// Exit when condition met
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // Exit loop when i is 5
    }
    System.out.println(i);
}
// Output:
// 0
// 1
// 2
// 3
// 4

// Search and exit
int[] numbers = {4, 7, 2, 9, 1, 5};
int target = 9;
boolean found = false;

for (int num : numbers) {
    if (num == target) {
        found = true;
        break;  // No need to continue searching
    }
}

System.out.println("Found: " + found);
// Output: Found: true
```

### Break Only Exits Innermost Loop

In nested loops, break only exits the innermost loop.

```java
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) {
            break;  // Only exits inner loop
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
// Output:
// i=0, j=0
// i=1, j=0
// i=2, j=0
```

### Labeled Break

Use a label to break out of outer loops.

```java
outer:  // Label
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break outer;  // Breaks out of both loops
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
// Output:
// i=0, j=0
// i=0, j=1
// i=0, j=2
// i=1, j=0
```

---

## Continue Statement

The `continue` statement skips the rest of the current iteration and moves to the next iteration.

### Basic Continue

```java
// Skip even numbers
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // Skip to next iteration
    }
    System.out.println(i);
}
// Output:
// 1
// 3
// 5
// 7
// 9

// Skip invalid data
String[] items = {"apple", "", "banana", null, "cherry"};

for (String item : items) {
    if (item == null || item.isEmpty()) {
        continue;  // Skip invalid items
    }
    System.out.println(item.toUpperCase());
}
// Output:
// APPLE
// BANANA
// CHERRY
```

### Continue in While Loop

```java
int i = 0;
while (i < 10) {
    i++;
    if (i % 2 == 0) {
        continue;  // Skip even numbers
    }
    System.out.println(i);
}
// Output:
// 1
// 3
// 5
// 7
// 9
```

### Labeled Continue

Use a label to continue an outer loop.

```java
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) {
            continue outer;  // Continue outer loop
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
// Output:
// i=0, j=0
// i=1, j=0
// i=2, j=0
```

---

## Return Statement

The `return` statement exits the current method. In loops, it exits both the loop and the method.

```java
public int findIndex(int[] array, int target) {
    for (int i = 0; i < array.length; i++) {
        if (array[i] == target) {
            return i;  // Found - exit method with result
        }
    }
    return -1;  // Not found
}

public void processItems(List<String> items) {
    if (items == null || items.isEmpty()) {
        return;  // Early return - exit method
    }
    
    for (String item : items) {
        System.out.println(item);
    }
}
```

---

## Nested Loops

Loops can be nested inside other loops.

### Basic Nested Loop

```java
// Multiplication table
for (int i = 1; i <= 5; i++) {
    for (int j = 1; j <= 5; j++) {
        System.out.print(i * j + "\t");
    }
    System.out.println();
}
// Output:
// 1	2	3	4	5	
// 2	4	6	8	10	
// 3	6	9	12	15	
// 4	8	12	16	20	
// 5	10	15	20	25	
```

### 2D Array Iteration

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Using indices
for (int row = 0; row < matrix.length; row++) {
    for (int col = 0; col < matrix[row].length; col++) {
        System.out.print(matrix[row][col] + " ");
    }
    System.out.println();
}
// Output:
// 1 2 3 
// 4 5 6 
// 7 8 9 

// Using for-each
for (int[] row : matrix) {
    for (int value : row) {
        System.out.print(value + " ");
    }
    System.out.println();
}
// Output:
// 1 2 3 
// 4 5 6 
// 7 8 9 
```

### Pattern Printing

```java
// Right triangle
int n = 5;
for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= i; j++) {
        System.out.print("* ");
    }
    System.out.println();
}
// Output:
// * 
// * * 
// * * * 
// * * * * 
// * * * * * 

// Pyramid
for (int i = 1; i <= n; i++) {
    // Print spaces
    for (int j = n - i; j > 0; j--) {
        System.out.print(" ");
    }
    // Print stars
    for (int j = 1; j <= (2 * i - 1); j++) {
        System.out.print("*");
    }
    System.out.println();
}
// Output:
//     *
//    ***
//   *****
//  *******
// *********
```

### Enterprise Example: Processing Related Data

```java
import java.util.*;

public class OrderReportGenerator {
    
    public void generateOrderReport(List<Customer> customers) {
        double grandTotal = 0;
        
        for (Customer customer : customers) {
            System.out.println("Customer: " + customer.getName());
            double customerTotal = 0;
            
            for (Order order : customer.getOrders()) {
                System.out.println("  Order #" + order.getId());
                double orderTotal = 0;
                
                for (OrderItem item : order.getItems()) {
                    double itemTotal = item.getPrice() * item.getQuantity();
                    orderTotal += itemTotal;
                    System.out.println("    - " + item.getName() + 
                                       " x" + item.getQuantity() + 
                                       " = $" + itemTotal);
                }
                
                System.out.println("  Order Total: $" + orderTotal);
                customerTotal += orderTotal;
            }
            
            System.out.println("Customer Total: $" + customerTotal);
            System.out.println();
            grandTotal += customerTotal;
        }
        
        System.out.println("===================");
        System.out.println("Grand Total: $" + grandTotal);
    }
}

// Sample Output:
// Customer: John Doe
//   Order #1001
//     - Widget x2 = $50.0
//     - Gadget x1 = $75.0
//   Order Total: $125.0
//   Order #1002
//     - Tool x3 = $90.0
//   Order Total: $90.0
// Customer Total: $215.0
//
// Customer: Jane Smith
//   Order #1003
//     - Widget x1 = $25.0
//   Order Total: $25.0
// Customer Total: $25.0
//
// ===================
// Grand Total: $240.0
```

---

## Loop Performance Considerations

### Move Invariants Outside the Loop

```java
List<String> list = new ArrayList<>(List.of("a", "b", "c", "d", "e"));

// Bad: length calculated every iteration
for (int i = 0; i < list.size(); i++) {
    // list.size() called on every iteration
    System.out.println(list.get(i));
}

// Better: calculate once
int size = list.size();
for (int i = 0; i < size; i++) {
    // size is a local variable, no method call
    System.out.println(list.get(i));
}

// Best for reading: use for-each
for (String item : list) {
    // Clean and efficient
    System.out.println(item);
}
// Output (all three produce same output):
// a
// b
// c
// d
// e
```

### Avoid Object Creation in Loops

```java
// Bad: creates new StringBuilder each iteration
for (int i = 0; i < 1000; i++) {
    StringBuilder sb = new StringBuilder();  // Created 1000 times!
    sb.append(i);
    // ...
}

// Better: reuse the object
StringBuilder sb = new StringBuilder();  // Created once
for (int i = 0; i < 1000; i++) {
    sb.setLength(0);  // Clear instead of recreating
    sb.append(i);
    // ...
}
```

---

## Common Mistakes

### 1. Off-by-One Errors

```java
int[] arr = {1, 2, 3, 4, 5};

// Wrong: ArrayIndexOutOfBoundsException
for (int i = 0; i <= arr.length; i++) {  // Should be <, not <=
    System.out.println(arr[i]);
}
// Error: Index 5 is out of bounds for array of length 5

// Correct
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}
// Output: 1, 2, 3, 4, 5
```

### 2. Modifying Collection While Iterating

```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));

// Wrong: ConcurrentModificationException
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // Modifying while iterating - throws exception!
    }
}

// Correct: Use Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) {
        it.remove();  // Safe removal
    }
}
System.out.println(list);
// Output: [a, c]

// Or use removeIf (Java 8+)
list.removeIf(s -> s.equals("b"));
System.out.println(list);
// Output: [a, c]
```

### 3. Forgetting break in Switch

```java
int day = 1;

// Wrong: falls through to all cases
switch (day) {
    case 1:
        System.out.println("Monday");
        // Missing break!
    case 2:
        System.out.println("Tuesday");  // Also prints due to fall-through!
        break;
}
// Output:
// Monday
// Tuesday

// Correct: use break or arrow syntax
switch (day) {
    case 1 -> System.out.println("Monday");
    case 2 -> System.out.println("Tuesday");
}
// Output: Monday
```

### 4. Infinite Loops

```java
// Bug: condition never becomes false
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Forgot i++ - this runs forever!
}

// Bug: wrong direction
for (int i = 10; i > 0; i++) {  // Should be i-- not i++
    System.out.println(i);
    // i keeps increasing, never reaches 0
}
```

---

## Cheat Sheet

| Construct | Use When | Example |
|-----------|----------|---------|
| `if-else` | Simple conditions | `if (x > 0) { ... } else { ... }` |
| `switch` (traditional) | Multiple values, one variable | `switch (day) { case 1: ...; break; }` |
| `switch` expression | Need a return value (Java 14+) | `String s = switch (x) { case 1 -> "one"; };` |
| `for` | Known number of iterations | `for (int i = 0; i < 10; i++) { ... }` |
| `for-each` | Iterating collections/arrays | `for (String s : list) { ... }` |
| `while` | Unknown iterations, check first | `while (condition) { ... }` |
| `do-while` | At least one iteration needed | `do { ... } while (condition);` |
| `break` | Exit loop/switch early | `if (found) break;` |
| `continue` | Skip to next iteration | `if (invalid) continue;` |
| Labeled `break` | Exit outer loops | `outer: for (...) { break outer; }` |

### Quick Reference

| Task | How |
|------|-----|
| Compare single value against many | Use `switch` |
| Early exit from method | Use `return` |
| Iterate with index | Use traditional `for` |
| Iterate without index | Use `for-each` |
| Retry until success | Use `while` with break |
| Validate input at least once | Use `do-while` |
| Exit nested loops | Use labeled `break` |

### Common Mistakes to Avoid

| Mistake | Problem | Fix |
|---------|---------|-----|
| `i <= arr.length` | ArrayIndexOutOfBounds | Use `i < arr.length` |
| Missing `break` in switch | Fall-through to next case | Add `break` or use arrow syntax |
| Modifying collection in for-each | ConcurrentModificationException | Use Iterator or removeIf |
| Forgetting counter increment | Infinite loop | Always update loop variable |

---

## Summary

**Key Points:**
- Always use braces for if/else blocks (even for single statements)
- Use switch expressions (Java 14+) for cleaner code
- For-each is cleaner when you do not need the index
- Be careful with infinite loops - always ensure termination
- Use labeled break/continue for nested loops
- Do not modify collections while iterating with for-each
- Move invariant calculations outside loops for better performance
- Use guard clauses (early returns) for cleaner validation code

---

[<- Back: Operators](./02-operators.md) | [Back to Guide](../guide.md) | [Next: Arrays ->](./04-arrays.md)
