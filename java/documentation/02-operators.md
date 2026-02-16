# 02. Operators

[<- Back: Variables](./01-variables.md) | [Back to Guide](../guide.md) | [Next: Control Flow ->](./03-control-flow.md)

**Quick Reference:** [Operators Cheatsheet](../cheatsheets/operators-cheatsheet.md)

---

## What Are Operators?

Operators are special symbols that tell Java to perform specific operations on values. They are the verbs of programming - they make things happen.

**In plain words:** Operators are symbols like `+`, `-`, `*`, `==` that do something with the values you give them.

**Real-world analogy:** Think of operators like buttons on a calculator. The `+` button adds numbers, the `=` button shows the result. Each button (operator) performs a specific action.

---

## Why Operators Matter

Without operators, you could not:
- Do any math (addition, subtraction, etc.)
- Compare values (is this greater than that?)
- Make decisions (is this true AND that true?)
- Store results in variables

**Example - What operators make possible:**
```java
int price = 100;          // = operator assigns value
int discount = 20;        // = operator assigns value
int finalPrice = price - discount;  // - operator subtracts

boolean isAffordable = finalPrice < 150;  // < operator compares
boolean hasStock = true;

if (isAffordable && hasStock) {    // && operator combines conditions
    System.out.println("Buy it!");
}
```

Output:
```
Buy it!
```

---

## Overview

Operators are special symbols that perform operations on variables and values. Java provides a rich set of operators grouped into several categories:

**Visual: Operator Categories:**

```
    JAVA OPERATORS
    ──────────────
    
    ┌─────────────────────────────────────────────────────────────┐
    │  ARITHMETIC          │  Do math with numbers               │
    │  + - * / %           │  5 + 3 = 8                          │
    ├─────────────────────────────────────────────────────────────┤
    │  ASSIGNMENT          │  Store values in variables          │
    │  = += -= *= /=       │  x = 10                             │
    ├─────────────────────────────────────────────────────────────┤
    │  COMPARISON          │  Compare two values (true/false)    │
    │  == != > < >= <=     │  5 > 3 is true                      │
    ├─────────────────────────────────────────────────────────────┤
    │  LOGICAL             │  Combine boolean conditions         │
    │  && || !             │  true && false is false             │
    ├─────────────────────────────────────────────────────────────┤
    │  BITWISE             │  Work with individual bits          │
    │  & | ^ ~ << >>       │  5 & 3 = 1                          │
    ├─────────────────────────────────────────────────────────────┤
    │  TERNARY             │  Shorthand if-else                  │
    │  ? :                 │  x > 0 ? "positive" : "negative"    │
    ├─────────────────────────────────────────────────────────────┤
    │  INSTANCEOF          │  Check object type                  │
    │  instanceof          │  obj instanceof String              │
    └─────────────────────────────────────────────────────────────┘
```

Understanding operators and their precedence is fundamental to writing correct Java expressions.

---

## Arithmetic Operators

Arithmetic operators perform mathematical calculations on numeric values.

### Basic Arithmetic

| Operator | Name | Description | Example |
|----------|------|-------------|---------|
| `+` | Addition | Adds two values | `5 + 3` = 8 |
| `-` | Subtraction | Subtracts right from left | `5 - 3` = 2 |
| `*` | Multiplication | Multiplies two values | `5 * 3` = 15 |
| `/` | Division | Divides left by right | `6 / 3` = 2 |
| `%` | Modulus | Remainder after division | `5 % 3` = 2 |

```java
int a = 10;
int b = 3;

int sum = a + b;        // 13
int difference = a - b; // 7
int product = a * b;    // 30
int quotient = a / b;   // 3 (integer division - truncates)
int remainder = a % b;  // 1

System.out.println("Sum: " + sum);
System.out.println("Difference: " + difference);
System.out.println("Product: " + product);
System.out.println("Quotient: " + quotient);
System.out.println("Remainder: " + remainder);
```

Output:
```
Sum: 13
Difference: 7
Product: 30
Quotient: 3
Remainder: 1
```

**Enterprise example - Calculating prices:**
```java
int quantity = 5;
double unitPrice = 29.99;
double subtotal = quantity * unitPrice;
double taxRate = 0.08;
double tax = subtotal * taxRate;
double total = subtotal + tax;

System.out.println("Subtotal: $" + subtotal);
System.out.println("Tax: $" + tax);
System.out.println("Total: $" + total);
```

Output:
```
Subtotal: $149.95
Tax: $11.996
Total: $161.946
```

### Integer Division vs Floating-Point Division

This is a common source of bugs. When both operands are integers, Java performs integer division, discarding any decimal portion.

**Visual: Integer vs Floating-Point Division:**

```
    INTEGER DIVISION                 FLOATING-POINT DIVISION
    ────────────────                 ───────────────────────
    
    5 / 2 = 2                        5.0 / 2 = 2.5
           ^                                   ^
           |                                   |
    Decimal part DISCARDED           Decimal part KEPT
    
    
    WHY THIS HAPPENS:
    
    int / int = int      (no decimals possible)
    double / int = double  (result can have decimals)
    int / double = double  (result can have decimals)
```

```java
// Integer division - decimal part is LOST
int intResult = 5 / 2;          // 2, not 2.5
int anotherExample = 7 / 4;     // 1, not 1.75

System.out.println(intResult);      // Output: 2
System.out.println(anotherExample); // Output: 1

// To get decimal results, at least one operand must be floating-point
double doubleResult = 5.0 / 2;      // 2.5
double alsoWorks = 5 / 2.0;         // 2.5
double castingWorks = (double) 5 / 2;  // 2.5

System.out.println(doubleResult);   // Output: 2.5
System.out.println(alsoWorks);      // Output: 2.5
System.out.println(castingWorks);   // Output: 2.5
```

**Common mistake:**
```java
int x = 5;
int y = 2;
double wrong = x / y;           // 2.0 (division happens first as int!)
double correct = (double) x / y; // 2.5 (x cast to double first)

System.out.println(wrong);    // Output: 2.0
System.out.println(correct);  // Output: 2.5
```

**Enterprise example - Calculating percentages:**
```java
int completed = 75;
int total = 200;

// WRONG - integer division gives 0
double wrongPercent = completed / total * 100;
System.out.println(wrongPercent);  // Output: 0.0

// CORRECT - cast to double first
double correctPercent = (double) completed / total * 100;
System.out.println(correctPercent);  // Output: 37.5
```

### Modulus Operator

The modulus operator `%` returns the remainder after division. It's extremely useful for many programming tasks.

**Visual: How Modulus Works:**

```
    10 % 3 = 1
    
    10 / 3 = 3 remainder 1
    
         3
       ┌───┐
    3  │10 │
       │ 9 │  (3 x 3 = 9)
       ├───┤
       │ 1 │  ← This is the remainder (10 % 3)
       └───┘
    
    17 % 5 = 2
    
    17 / 5 = 3 remainder 2
    
         3
       ┌───┐
    5  │17 │
       │15 │  (5 x 3 = 15)
       ├───┤
       │ 2 │  ← This is the remainder (17 % 5)
       └───┘
```

```java
// Basic modulus
int remainder = 10 % 3;         // 1 (10 = 3*3 + 1)
System.out.println(remainder);  // Output: 1
```

**Check if number is even or odd:**
```java
int number = 17;
if (number % 2 == 0) {
    System.out.println("Even");
} else {
    System.out.println("Odd");
}
// Output: Odd
```

**Check divisibility:**
```java
int value = 15;
if (value % 5 == 0) {
    System.out.println("Divisible by 5");
}
// Output: Divisible by 5
```

**Enterprise example - Pagination:**
```java
int totalItems = 47;
int itemsPerPage = 10;

int totalPages = totalItems / itemsPerPage;  // 4
int remainingItems = totalItems % itemsPerPage;  // 7

// If there's a remainder, we need one more page
if (remainingItems > 0) {
    totalPages++;
}

System.out.println("Total pages: " + totalPages);  // Output: Total pages: 5
System.out.println("Items on last page: " + remainingItems);  // Output: Items on last page: 7
```

**Wrap around (cycling through values):**
```java
// Useful for circular arrays, rotating through options
int hour = 25;
int normalizedHour = hour % 24; // 1 (wraps around to valid hour)

System.out.println(normalizedHour);  // Output: 1
```

**Get last digit:**
```java
int num = 12345;
int lastDigit = num % 10;       // 5

System.out.println(lastDigit);  // Output: 5
```

**Modulus with negative numbers (sign follows dividend):**
```java
int negative = -10 % 3;         // -1
int alsoNegative = 10 % -3;     // 1

System.out.println(negative);      // Output: -1
System.out.println(alsoNegative);  // Output: 1
```

### Unary Operators

Unary operators work with a single operand (one value).

**In plain words:** Most operators work with two values (like `5 + 3`). Unary operators only need one value.

| Operator | Name | Description | Example |
|----------|------|-------------|---------|
| `+` | Unary plus | Indicates positive (rarely used) | `+5` |
| `-` | Unary minus | Negates a value | `-5` |
| `++` | Increment | Increases value by 1 | `count++` |
| `--` | Decrement | Decreases value by 1 | `count--` |
| `!` | Logical NOT | Inverts boolean | `!true` = false |

```java
int x = 5;

// Unary minus
int negative = -x;              // -5

// Unary plus (rarely used, just for symmetry)
int positive = +x;              // 5

System.out.println(negative);   // Output: -5
System.out.println(positive);   // Output: 5
```

### Increment and Decrement

The `++` and `--` operators can be placed before (prefix) or after (postfix) the variable. The position matters when the expression is used in a larger statement.

**Visual: Prefix vs Postfix:**

```
    POSTFIX (count++)                PREFIX (++count)
    ─────────────────                ────────────────
    
    1. Use current value             1. Increment first
    2. Then increment                2. Then use new value
    
    int a = count++;                 int b = ++count;
    
    count = 10                       count = 10
         │                                │
         ▼                                ▼
    a = 10 (use old value)           count = 11 (increment)
         │                                │
         ▼                                ▼
    count = 11 (then increment)      b = 11 (use new value)
```

```java
int count = 10;

// Postfix: use current value, THEN increment
int a = count++;    // a = 10, count = 11

System.out.println("a = " + a);         // Output: a = 10
System.out.println("count = " + count); // Output: count = 11

count = 10;  // reset

// Prefix: increment FIRST, then use new value
int b = ++count;    // b = 11, count = 11

System.out.println("b = " + b);         // Output: b = 11
System.out.println("count = " + count); // Output: count = 11
```

**Detailed breakdown:**

```java
int x = 5;

// Postfix increment
System.out.println(x++);        // Output: 5 (uses old value)
System.out.println(x);          // Output: 6 (x is now 6)

x = 5;  // reset

// Prefix increment
System.out.println(++x);        // Output: 6 (increments first)
System.out.println(x);          // Output: 6

// In loops, both often produce the same effect
for (int i = 0; i < 5; i++) { }     // Postfix
for (int i = 0; i < 5; ++i) { }     // Prefix - same result

// But in expressions, they differ
int i = 0;
int[] arr = {10, 20, 30};
System.out.println(arr[i++]);   // Output: 10, i becomes 1
System.out.println(arr[++i]);   // i becomes 2, Output: 30
```

**Decrement works the same way:**

```java
int count = 10;

int postDec = count--;          // postDec = 10, count = 9
System.out.println("postDec = " + postDec);  // Output: postDec = 10

count = 10;  // reset
int preDec = --count;           // preDec = 9, count = 9
System.out.println("preDec = " + preDec);    // Output: preDec = 9
```

---

## Assignment Operators

Assignment operators store values in variables.

### Simple Assignment

The basic assignment operator `=` assigns the value on the right to the variable on the left.

**Important:** `=` is assignment, `==` is comparison. This is a common source of bugs.

```java
int x = 10;                     // Assign 10 to x
String name = "Alice";          // Assign "Alice" to name
boolean active = true;          // Assign true to active
```

### Compound Assignment Operators

Compound operators combine an arithmetic operation with assignment. They're shorthand for longer expressions.

| Operator | Example | Equivalent To |
|----------|---------|---------------|
| `+=` | `x += 5` | `x = x + 5` |
| `-=` | `x -= 5` | `x = x - 5` |
| `*=` | `x *= 5` | `x = x * 5` |
| `/=` | `x /= 5` | `x = x / 5` |
| `%=` | `x %= 5` | `x = x % 5` |
| `&=` | `x &= 5` | `x = x & 5` |
| `\|=` | `x \|= 5` | `x = x \| 5` |
| `^=` | `x ^= 5` | `x = x ^ 5` |
| `<<=` | `x <<= 2` | `x = x << 2` |
| `>>=` | `x >>= 2` | `x = x >> 2` |
| `>>>=` | `x >>>= 2` | `x = x >>> 2` |

```java
int x = 10;

x += 5;     // x = 15 (was 10 + 5)
x -= 3;     // x = 12 (was 15 - 3)
x *= 2;     // x = 24 (was 12 * 2)
x /= 4;     // x = 6  (was 24 / 4)
x %= 4;     // x = 2  (was 6 % 4)

// Compound assignment with strings
String greeting = "Hello";
greeting += " World";           // "Hello World"
greeting += "!";                // "Hello World!"
```

**Important: Compound operators include implicit casting**

```java
// This works even though the result is computed as int
byte b = 10;
b += 5;         // OK - implicit cast to byte

// Without compound operator, you'd need explicit cast
// b = b + 5;   // Error! (b + 5) is int
b = (byte)(b + 5);  // Must cast explicitly
```

---

## Comparison (Relational) Operators

Comparison operators compare two values and return a boolean result (`true` or `false`).

| Operator | Name | Description | Example |
|----------|------|-------------|---------|
| `==` | Equal to | True if values are equal | `5 == 5` is true |
| `!=` | Not equal to | True if values are different | `5 != 3` is true |
| `>` | Greater than | True if left is greater | `5 > 3` is true |
| `<` | Less than | True if left is smaller | `3 < 5` is true |
| `>=` | Greater than or equal | True if left is >= right | `5 >= 5` is true |
| `<=` | Less than or equal | True if left is <= right | `3 <= 5` is true |

```java
int a = 10;
int b = 5;

boolean isEqual = (a == b);     // false
boolean isNotEqual = (a != b);  // true
boolean isGreater = (a > b);    // true
boolean isLess = (a < b);       // false
boolean isGreaterOrEqual = (a >= 10);  // true
boolean isLessOrEqual = (a <= 10);     // true

System.out.println("isEqual: " + isEqual);              // Output: isEqual: false
System.out.println("isNotEqual: " + isNotEqual);        // Output: isNotEqual: true
System.out.println("isGreater: " + isGreater);          // Output: isGreater: true
System.out.println("isLess: " + isLess);                // Output: isLess: false
System.out.println("isGreaterOrEqual: " + isGreaterOrEqual);  // Output: isGreaterOrEqual: true
System.out.println("isLessOrEqual: " + isLessOrEqual);  // Output: isLessOrEqual: true
```

**Commonly used in conditions:**
```java
int a = 10;
int b = 5;

if (a > b) {
    System.out.println("a is greater than b");
}
// Output: a is greater than b
```

**Enterprise example - Validation:**
```java
int age = 25;
int minAge = 18;
int maxAge = 65;

if (age >= minAge && age <= maxAge) {
    System.out.println("Eligible for employment");
}
// Output: Eligible for employment

// Checking stock levels
int stockQuantity = 5;
int reorderThreshold = 10;

if (stockQuantity <= reorderThreshold) {
    System.out.println("Low stock! Reorder needed.");
}
// Output: Low stock! Reorder needed.
```

### Comparing Objects vs Primitives

**For primitives:** `==` compares the actual values.

```java
int x = 5;
int y = 5;
System.out.println(x == y);     // Output: true (same value)
```

**For objects:** `==` compares memory references (whether they point to the same object), NOT the content.

**Visual: == with Objects:**

```
    String a = new String("hello");
    String b = new String("hello");
    
    
    Variable a          Memory               Variable b
    ┌────────┐         ┌─────────┐          ┌────────┐
    │ 0x1000 │ ──────> │ "hello" │          │ 0x2000 │
    └────────┘         └─────────┘          └────────┘
                                                  │
                       ┌─────────┐                │
                       │ "hello" │ <──────────────┘
                       └─────────┘
    
    a == b  →  Compares 0x1000 vs 0x2000  →  FALSE (different addresses)
    a.equals(b)  →  Compares "hello" vs "hello"  →  TRUE (same content)
```

```java
String a = new String("hello");
String b = new String("hello");

System.out.println(a == b);     // Output: false (different objects)
System.out.println(a.equals(b)); // Output: true (same content)

// String literals are special (interned)
String c = "hello";
String d = "hello";
System.out.println(c == d);     // Output: true (same pooled object)
System.out.println(c.equals(d)); // Output: true (same content)
```

**Rule of thumb:** Use `==` for primitives, use `.equals()` for objects.

**WARNING - Integer caching trap:**
```java
// Comparing wrapper objects
Integer num1 = 100;
Integer num2 = 100;
System.out.println(num1 == num2);       // Output: true (cached range -128 to 127)
System.out.println(num1.equals(num2));  // Output: true

Integer big1 = 200;
Integer big2 = 200;
System.out.println(big1 == big2);       // Output: false (outside cache range!)
System.out.println(big1.equals(big2));  // Output: true
```

**Enterprise tip:** Always use `.equals()` for wrapper classes to avoid subtle bugs.

---

## Logical Operators

Logical operators work with boolean values and are used to combine multiple conditions.

**Visual: Logical Operators Truth Table:**

```
    AND (&&)                    OR (||)                     NOT (!)
    ────────                    ───────                     ───────
    
    A     B     A && B          A     B     A || B          A       !A
    ─────────────────           ─────────────────           ────────────
    true  true  true            true  true  true            true    false
    true  false false           true  false true            false   true
    false true  false           false true  true
    false false false           false false false
    
    
    MEMORY AID:
    
    AND (&&): "Both must be true"     →  Strict (like a picky restaurant)
    OR (||):  "At least one true"     →  Lenient (like an inclusive party)
    NOT (!):  "Flip the value"        →  Opposite day
```

| Operator | Name | Description |
|----------|------|-------------|
| `&&` | Logical AND | True if BOTH operands are true |
| `\|\|` | Logical OR | True if AT LEAST ONE operand is true |
| `!` | Logical NOT | Inverts the boolean value |

### Logical AND (&&)

Returns `true` only if both conditions are true.

```java
boolean a = true;
boolean b = false;

System.out.println(a && a);     // Output: true  (true AND true)
System.out.println(a && b);     // Output: false (true AND false)
System.out.println(b && a);     // Output: false (false AND true)
System.out.println(b && b);     // Output: false (false AND false)
```

**Practical example:**
```java
int age = 25;
boolean hasLicense = true;

if (age >= 18 && hasLicense) {
    System.out.println("Can drive");
}
// Output: Can drive
```

**Enterprise example - Multiple validation conditions:**
```java
int score = 85;
int attendance = 90;
boolean passedExam = true;

if (score >= 80 && attendance >= 75 && passedExam) {
    System.out.println("Eligible for honors");
}
// Output: Eligible for honors
```

### Logical OR (||)

Returns `true` if at least one condition is true.

```java
boolean a = true;
boolean b = false;

System.out.println(a || a);     // Output: true  (true OR true)
System.out.println(a || b);     // Output: true  (true OR false)
System.out.println(b || a);     // Output: true  (false OR true)
System.out.println(b || b);     // Output: false (false OR false)
```

**Practical example:**
```java
String day = "Saturday";

if (day.equals("Saturday") || day.equals("Sunday")) {
    System.out.println("It's the weekend!");
}
// Output: It's the weekend!
```

**Enterprise example - Checking for invalid input:**
```java
String input = "";
if (input == null || input.isEmpty()) {
    System.out.println("No input provided");
}
// Output: No input provided
```

### Logical NOT (!)

Inverts a boolean value.

```java
boolean active = true;

System.out.println(!active);    // Output: false
System.out.println(!false);     // Output: true
```

**Practical example:**
```java
boolean isLoggedIn = false;

if (!isLoggedIn) {
    System.out.println("Please log in");
}
// Output: Please log in
```

**Avoid double negation (hard to read):**
```java
// Don't do this
System.out.println(!!true);     // Output: true (confusing)

// Do this instead
System.out.println(true);       // Output: true (clear)
```

### Short-Circuit Evaluation

Java uses **short-circuit evaluation** for `&&` and `||`. This means:

- For `&&`: If the first operand is `false`, the second is NOT evaluated (result is already `false`)
- For `||`: If the first operand is `true`, the second is NOT evaluated (result is already `true`)

**Visual: Short-Circuit Evaluation:**

```
    SHORT-CIRCUIT AND (&&)              SHORT-CIRCUIT OR (||)
    ──────────────────────              ─────────────────────
    
    false && ???                        true || ???
         │                                   │
         ▼                                   ▼
    STOP! Result is false               STOP! Result is true
    (don't evaluate ???)                (don't evaluate ???)
    
    
    WHY THIS MATTERS:
    
    name != null && name.length() > 0
         │              │
         │              └── Only checked if name is NOT null
         │                  (safe - no NullPointerException)
         └── Checked first
```

```java
// Short-circuit AND
int x = 5;
if (x > 10 && x++ > 0) {        // x++ never executes because x > 10 is false
    // ...
}
System.out.println(x);          // Output: 5 (not incremented)

// Short-circuit OR
int y = 5;
if (y < 10 || y++ > 0) {        // y++ never executes because y < 10 is true
    // ...
}
System.out.println(y);          // Output: 5 (not incremented)
```

**Enterprise example - Safe null checking:**

```java
String name = null;

// Safe: if name is null, the second condition is never checked
if (name != null && name.length() > 0) {
    System.out.println("Name: " + name);
}
// No output, but no crash either!

// DANGEROUS ORDER - would throw NullPointerException:
// if (name.length() > 0 && name != null)  // DON'T DO THIS
```

### Non-Short-Circuit Operators (& and |)

Java also has `&` and `|` which evaluate BOTH operands regardless of the first result. These are rarely used for boolean logic.

```java
int x = 5;
if (false & x++ > 0) {          // x++ DOES execute
    // ...
}
System.out.println(x);          // 6 (incremented)
```

---

## Bitwise Operators

Bitwise operators work on individual bits of integer values. They're useful for low-level programming, flags, and performance optimization.

| Operator | Name | Description |
|----------|------|-------------|
| `&` | Bitwise AND | 1 if both bits are 1 |
| `\|` | Bitwise OR | 1 if at least one bit is 1 |
| `^` | Bitwise XOR | 1 if bits are different |
| `~` | Bitwise NOT | Inverts all bits |
| `<<` | Left shift | Shifts bits left, fills with 0 |
| `>>` | Right shift (signed) | Shifts bits right, preserves sign |
| `>>>` | Right shift (unsigned) | Shifts bits right, fills with 0 |

### Bitwise AND (&)

Each bit in the result is 1 only if both corresponding bits are 1.

```java
int a = 5;      // Binary: 0101
int b = 3;      // Binary: 0011
int c = a & b;  // Binary: 0001 = 1

//   0101 (5)
// & 0011 (3)
// ------
//   0001 (1)

System.out.println(c);  // 1

// Practical use: Check if a number is odd
int num = 7;
if ((num & 1) == 1) {
    System.out.println("Odd");  // Prints
}

// Masking: Extract specific bits
int value = 0b11011010;  // 218 in binary
int mask = 0b00001111;   // Mask for lower 4 bits
int lower4 = value & mask;  // 0b00001010 = 10
```

### Bitwise OR (|)

Each bit in the result is 1 if at least one corresponding bit is 1.

```java
int a = 5;      // Binary: 0101
int b = 3;      // Binary: 0011
int c = a | b;  // Binary: 0111 = 7

//   0101 (5)
// | 0011 (3)
// ------
//   0111 (7)

System.out.println(c);  // 7

// Practical use: Setting flags
int permissions = 0b0000;
int READ = 0b0001;
int WRITE = 0b0010;
int EXECUTE = 0b0100;

permissions = permissions | READ | WRITE;  // 0b0011 = 3
System.out.println(Integer.toBinaryString(permissions));  // 11
```

### Bitwise XOR (^)

Each bit in the result is 1 if the corresponding bits are different.

```java
int a = 5;      // Binary: 0101
int b = 3;      // Binary: 0011
int c = a ^ b;  // Binary: 0110 = 6

//   0101 (5)
// ^ 0011 (3)
// ------
//   0110 (6)

System.out.println(c);  // 6

// Practical use: Swap without temp variable
int x = 10;
int y = 20;
x = x ^ y;      // x = 30
y = x ^ y;      // y = 10 (original x)
x = x ^ y;      // x = 20 (original y)
System.out.println("x=" + x + ", y=" + y);  // x=20, y=10

// Practical use: Toggle a bit
int flags = 0b0101;
int toggleMask = 0b0010;
flags = flags ^ toggleMask;  // 0b0111
flags = flags ^ toggleMask;  // 0b0101 (back to original)
```

### Bitwise NOT (~)

Inverts all bits (0 becomes 1, 1 becomes 0). This produces the two's complement.

```java
int a = 5;      // Binary: 00000000 00000000 00000000 00000101
int b = ~a;     // Binary: 11111111 11111111 11111111 11111010 = -6

System.out.println(b);  // -6

// Formula: ~n = -(n + 1)
System.out.println(~0);     // -1
System.out.println(~1);     // -2
System.out.println(~(-1));  // 0
```

### Left Shift (<<)

Shifts all bits left by specified positions. Fills with zeros on the right. Equivalent to multiplying by 2^n.

```java
int a = 5;          // Binary: 0101
int b = a << 1;     // Binary: 1010 = 10

//   0101 (5)
// << 1
// ------
//   1010 (10)

System.out.println(b);  // 10

// Each left shift multiplies by 2
int x = 3;
System.out.println(x << 1);  // 6  (3 * 2)
System.out.println(x << 2);  // 12 (3 * 4)
System.out.println(x << 3);  // 24 (3 * 8)

// Fast multiplication by powers of 2
int value = 7;
int times8 = value << 3;     // 56 (7 * 8)
```

### Right Shift (Signed) (>>)

Shifts all bits right by specified positions. Preserves the sign bit (leftmost bit). Equivalent to dividing by 2^n (with rounding toward negative infinity).

```java
int a = 20;         // Binary: 00010100
int b = a >> 2;     // Binary: 00000101 = 5

System.out.println(b);  // 5 (20 / 4)

// Preserves sign for negative numbers
int neg = -8;       // Binary: 11111111 11111111 11111111 11111000
int shifted = neg >> 1;  // Binary: 11111111 11111111 11111111 11111100 = -4

System.out.println(shifted);  // -4

// Fast division by powers of 2
int value = 64;
System.out.println(value >> 1);  // 32 (64 / 2)
System.out.println(value >> 2);  // 16 (64 / 4)
System.out.println(value >> 3);  // 8  (64 / 8)
```

### Unsigned Right Shift (>>>)

Shifts all bits right by specified positions. Always fills with zeros on the left (ignores sign).

```java
int a = -1;         // Binary: 11111111 11111111 11111111 11111111
int b = a >>> 1;    // Binary: 01111111 11111111 11111111 11111111

System.out.println(b);  // 2147483647 (Integer.MAX_VALUE)

// Compare signed vs unsigned right shift
int neg = -8;
System.out.println(neg >> 1);   // -4 (preserves sign)
System.out.println(neg >>> 1);  // 2147483644 (fills with 0)
```

---

## Ternary Operator

The ternary operator `? :` is a shorthand for simple if-else statements. It's the only operator that takes three operands.

**Syntax:** `condition ? valueIfTrue : valueIfFalse`

```java
// Basic syntax
int age = 20;
String status = (age >= 18) ? "Adult" : "Minor";
System.out.println(status);  // "Adult"

// Equivalent if-else
String statusLong;
if (age >= 18) {
    statusLong = "Adult";
} else {
    statusLong = "Minor";
}
```

### Common Use Cases

```java
// Finding max/min
int a = 10, b = 20;
int max = (a > b) ? a : b;      // 20
int min = (a < b) ? a : b;      // 10

// Handling null
String name = null;
String displayName = (name != null) ? name : "Guest";
System.out.println(displayName);  // "Guest"

// Absolute value
int x = -5;
int abs = (x < 0) ? -x : x;     // 5

// Formatting output
int count = 1;
String message = "You have " + count + " item" + (count != 1 ? "s" : "");
System.out.println(message);  // "You have 1 item"

count = 5;
message = "You have " + count + " item" + (count != 1 ? "s" : "");
System.out.println(message);  // "You have 5 items"
```

### Nested Ternary (Use Sparingly)

You can nest ternary operators, but this can reduce readability.

```java
// Nested ternary
int score = 85;
String grade = (score >= 90) ? "A" :
               (score >= 80) ? "B" :
               (score >= 70) ? "C" :
               (score >= 60) ? "D" : "F";

System.out.println(grade);  // "B"

// Better to use if-else or switch for complex conditions
```

---

## instanceof Operator

The `instanceof` operator checks if an object is an instance of a specific class or implements a specific interface.

```java
String text = "Hello";
Integer number = 42;
Object obj = "World";

// Basic instanceof
System.out.println(text instanceof String);   // true
System.out.println(number instanceof Integer); // true
System.out.println(number instanceof Number);  // true (Integer extends Number)
System.out.println(obj instanceof String);     // true (runtime type is String)

// Check interface implementation
List<String> list = new ArrayList<>();
System.out.println(list instanceof List);       // true
System.out.println(list instanceof ArrayList);  // true
System.out.println(list instanceof Collection); // true
```

### Pattern Matching instanceof (Java 16+)

Java 16 introduced pattern matching for `instanceof`, combining the type check and cast.

```java
Object obj = "Hello, World!";

// Old way (before Java 16)
if (obj instanceof String) {
    String str = (String) obj;  // Explicit cast needed
    System.out.println(str.toUpperCase());
}

// New way (Java 16+)
if (obj instanceof String str) {  // Cast and assign in one step
    System.out.println(str.toUpperCase());
}

// Works with negation too
if (!(obj instanceof String str)) {
    System.out.println("Not a string");
    return;
}
// str is in scope here
System.out.println(str.length());
```

### Null Safety

`instanceof` always returns `false` for null values.

```java
String text = null;
System.out.println(text instanceof String);  // false (null is never instanceof)

// This is safe
if (text instanceof String) {
    // text is guaranteed non-null here
}
```

---

## Operator Precedence

When multiple operators appear in an expression, precedence determines the order of evaluation.

### Precedence Table (Highest to Lowest)

| Precedence | Operators | Description |
|------------|-----------|-------------|
| 1 | `()` `[]` `.` | Parentheses, array access, member access |
| 2 | `++` `--` `!` `~` `+` `-` | Unary operators |
| 3 | `*` `/` `%` | Multiplication, division, modulus |
| 4 | `+` `-` | Addition, subtraction |
| 5 | `<<` `>>` `>>>` | Shift operators |
| 6 | `<` `<=` `>` `>=` `instanceof` | Comparison operators |
| 7 | `==` `!=` | Equality operators |
| 8 | `&` | Bitwise AND |
| 9 | `^` | Bitwise XOR |
| 10 | `\|` | Bitwise OR |
| 11 | `&&` | Logical AND |
| 12 | `\|\|` | Logical OR |
| 13 | `? :` | Ternary operator |
| 14 | `=` `+=` `-=` etc. | Assignment operators |

### Examples

```java
// Multiplication before addition
int result = 2 + 3 * 4;         // 14, not 20
int withParens = (2 + 3) * 4;   // 20

// Comparison before logical
boolean check = 5 > 3 && 2 < 4; // true (both comparisons happen first)

// Assignment is last
int x = 5 + 3 * 2;              // x = 11 (multiplication, then addition, then assignment)

// Unary operators are high precedence
int a = 5;
int b = -a * 2;                 // -10 (negation happens first)

// Increment/decrement
int c = 3;
int d = 2 * c++;                // 6 (c is used as 3, then incremented to 4)
```

### Use Parentheses for Clarity

Even when precedence gives the correct result, parentheses improve readability.

```java
// Technically correct, but confusing
boolean result = a > b && c < d || e == f;

// Much clearer
boolean result = ((a > b) && (c < d)) || (e == f);

// Or split into meaningful variables
boolean aGreaterThanB = a > b;
boolean cLessThanD = c < d;
boolean eEqualsF = e == f;
boolean result = (aGreaterThanB && cLessThanD) || eEqualsF;
```

---

## String Concatenation Operator (+)

The `+` operator is overloaded for strings to perform concatenation.

```java
String greeting = "Hello" + " " + "World";  // "Hello World"
System.out.println(greeting);  // Output: Hello World

// Concatenating with other types (automatic conversion to String)
int age = 25;
String message = "Age: " + age;             // "Age: 25"
System.out.println(message);   // Output: Age: 25

double price = 19.99;
String label = "Price: $" + price;          // "Price: $19.99"
System.out.println(label);     // Output: Price: $19.99
```

**WARNING - Order matters with mixed types:**
```java
// Order matters with mixed types
System.out.println("Sum: " + 1 + 2);        // Output: Sum: 12 (string concat!)
System.out.println("Sum: " + (1 + 2));      // Output: Sum: 3 (addition first)
System.out.println(1 + 2 + " is the sum");  // Output: 3 is the sum (addition first)
```

**Enterprise tip - Use StringBuilder for multiple concatenations:**
```java
// Bad for performance in loops
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + ", ";  // Creates new String each iteration
}

// Good for performance
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(", ");
}
String result = sb.toString();
```

---

## Common Mistakes and Pitfalls

### 1. Confusing = and ==

```java
int x = 5;

// Assignment (=) vs comparison (==)
// if (x = 10) { }     // COMPILE ERROR! (x = 10) returns int, not boolean
if (x == 10) { }       // Correct: comparison
```

### 2. Integer Division

```java
double result = 5 / 2;          // 2.0, not 2.5!
double correct = 5.0 / 2;       // 2.5

System.out.println(result);     // Output: 2.0
System.out.println(correct);    // Output: 2.5
```

### 3. Short-Circuit Side Effects

```java
int i = 0;
if (false && i++ > 0) { }
System.out.println(i);          // Output: 0 (i++ was never executed)
```

### 4. Comparing Objects with ==

```java
String a = new String("hello");
String b = new String("hello");
System.out.println(a == b);     // Output: false (different objects!)
System.out.println(a.equals(b)); // Output: true (same content)
```

### 5. Operator Precedence Surprises

```java
int x = 1;
int y = 2;
System.out.println(x + y + " items");  // Output: 3 items
System.out.println("items: " + x + y); // Output: items: 12 (not 3!)
```

### 6. Modulus with Negative Numbers

```java
System.out.println(-5 % 3);     // Output: -2 (not 1!)
System.out.println(5 % -3);     // Output: 2
```

---

## Summary

| Category | Operators | Key Points |
|----------|-----------|------------|
| Arithmetic | `+` `-` `*` `/` `%` | Watch for integer division |
| Increment/Decrement | `++` `--` | Prefix vs postfix matters |
| Assignment | `=` `+=` `-=` etc. | Compound operators include implicit cast |
| Comparison | `==` `!=` `<` `>` `<=` `>=` | Use `.equals()` for objects |
| Logical | `&&` `\|\|` `!` | Short-circuit evaluation |
| Bitwise | `&` `\|` `^` `~` `<<` `>>` `>>>` | Work on individual bits |
| Ternary | `? :` | Shorthand if-else |
| instanceof | `instanceof` | Type checking |

---

## Cheat Sheet

| Goal | Operator | Example | Result |
|------|----------|---------|--------|
| Add | `+` | `5 + 3` | `8` |
| Subtract | `-` | `5 - 3` | `2` |
| Multiply | `*` | `5 * 3` | `15` |
| Divide | `/` | `6 / 3` | `2` |
| Remainder | `%` | `5 % 3` | `2` |
| Increment | `++` | `x++` or `++x` | Adds 1 |
| Decrement | `--` | `x--` or `--x` | Subtracts 1 |
| Equal | `==` | `5 == 5` | `true` |
| Not equal | `!=` | `5 != 3` | `true` |
| Greater | `>` | `5 > 3` | `true` |
| Less | `<` | `3 < 5` | `true` |
| AND | `&&` | `true && false` | `false` |
| OR | `\|\|` | `true \|\| false` | `true` |
| NOT | `!` | `!true` | `false` |
| Ternary | `? :` | `x > 0 ? "pos" : "neg"` | Conditional value |

| Common Mistake | Problem | Fix |
|----------------|---------|-----|
| `5 / 2` | Returns `2`, not `2.5` | Use `5.0 / 2` |
| `str1 == str2` | Compares references | Use `str1.equals(str2)` |
| `"Sum: " + 1 + 2` | Returns `"Sum: 12"` | Use `"Sum: " + (1 + 2)` |
| `x = 5` in `if` | Assignment, not comparison | Use `x == 5` |

---

[<- Back: Variables](./01-variables.md) | [Back to Guide](../guide.md) | [Next: Control Flow ->](./03-control-flow.md)
