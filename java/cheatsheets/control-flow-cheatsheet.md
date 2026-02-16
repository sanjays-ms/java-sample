# Control Flow Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Control Flow](../documentation/03-control-flow.md)

Quick reference for Java control flow statements.

---

## If-Else

```java
if (condition) {
    // code
}

if (condition) {
    // code
} else {
    // code
}

if (condition1) {
    // code
} else if (condition2) {
    // code
} else {
    // code
}
```

---

## Switch Statement (Traditional)

```java
switch (variable) {
    case value1:
        // code
        break;
    case value2:
    case value3:
        // code (fall-through)
        break;
    default:
        // code
}
```

---

## Switch Expression (Java 14+)

```java
// Arrow syntax - no break needed
String result = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 6, 7 -> "Weekend";
    default -> "Other";
};

// Block with yield
String result = switch (day) {
    case 1 -> {
        System.out.println("Start of week");
        yield "Monday";
    }
    default -> "Other";
};
```

---

## Pattern Matching Switch (Java 21+)

```java
String result = switch (obj) {
    case Integer i -> "Int: " + i;
    case String s -> "String: " + s;
    case null -> "Null";
    default -> "Other";
};

// With guards
switch (obj) {
    case Integer i when i > 0 -> "Positive";
    case Integer i -> "Non-positive";
    default -> "Not integer";
}
```

---

## For Loop

```java
// Basic
for (int i = 0; i < 10; i++) {
    // code
}

// Counting down
for (int i = 10; i > 0; i--) { }

// Step by 2
for (int i = 0; i < 10; i += 2) { }

// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) { }
```

---

## For-Each Loop

```java
// Arrays
for (int num : numbers) {
    System.out.println(num);
}

// Collections
for (String name : names) {
    System.out.println(name);
}
```

Use when: Don't need index, only reading values.

---

## While Loop

```java
while (condition) {
    // code
}

// Common pattern
int i = 0;
while (i < 10) {
    // code
    i++;
}
```

---

## Do-While Loop

```java
do {
    // executes at least once
} while (condition);
```

Difference: Executes body BEFORE checking condition.

---

## Break

```java
// Exit loop
for (int i = 0; i < 10; i++) {
    if (i == 5) break;
}

// Labeled break (nested loops)
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (condition) break outer;
    }
}
```

---

## Continue

```java
// Skip iteration
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) continue;
    System.out.println(i);  // Odd only
}

// Labeled continue
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (condition) continue outer;
    }
}
```

---

## Common Patterns

```java
// Early return / Guard clause
if (param == null) return;

// Search and exit
for (int num : array) {
    if (num == target) {
        found = true;
        break;
    }
}

// Skip invalid items
for (String s : items) {
    if (s == null || s.isEmpty()) continue;
    process(s);
}

// Input validation
do {
    input = scanner.nextInt();
} while (input <= 0);

// Infinite loop with exit
while (true) {
    if (shouldStop) break;
}
```

---

## 2D Array Iteration

```java
// With index
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j]);
    }
}

// For-each
for (int[] row : matrix) {
    for (int val : row) {
        System.out.print(val);
    }
}
```

---

## Loop Comparison

| Loop | Use When |
|------|----------|
| `for` | Known iterations, need index |
| `for-each` | Iterate all elements, no index needed |
| `while` | Unknown iterations, check first |
| `do-while` | Need at least one iteration |

---

## Common Mistakes

```java
// Off-by-one (use < not <=)
for (int i = 0; i <= arr.length; i++)  // Wrong!
for (int i = 0; i < arr.length; i++)   // Correct

// Missing break in switch
case 1:
    doSomething();
    // Missing break - falls through!

// Infinite loop
while (i < 10) {
    // Forgot i++
}

// Modifying while iterating
for (String s : list) {
    list.remove(s);  // ConcurrentModificationException!
}
// Use Iterator.remove() or removeIf()
```

---

## Quick Comparison

```java
// If vs Ternary
if (a > b) max = a; else max = b;
max = (a > b) ? a : b;

// For vs While
for (int i = 0; i < n; i++) { }
int i = 0; while (i < n) { i++; }

// Traditional vs Enhanced for
for (int i = 0; i < arr.length; i++) { }
for (int x : arr) { }

// Traditional vs Arrow switch
switch (x) { case 1: return "A"; }
switch (x) { case 1 -> "A"; }
```
