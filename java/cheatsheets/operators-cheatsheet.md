# Operators Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Operators](../documentation/02-operators.md)

Quick reference for Java operators.

---

## Arithmetic Operators

| Operator | Name | Example | Result |
|----------|------|---------|--------|
| `+` | Addition | `5 + 3` | 8 |
| `-` | Subtraction | `5 - 3` | 2 |
| `*` | Multiplication | `5 * 3` | 15 |
| `/` | Division | `6 / 3` | 2 |
| `%` | Modulus | `5 % 3` | 2 |

```java
// Integer division truncates!
5 / 2           // 2, not 2.5
5.0 / 2         // 2.5
(double) 5 / 2  // 2.5
```

---

## Increment / Decrement

| Operator | Name | Example | Effect |
|----------|------|---------|--------|
| `++x` | Pre-increment | `y = ++x` | Increment first, then use |
| `x++` | Post-increment | `y = x++` | Use first, then increment |
| `--x` | Pre-decrement | `y = --x` | Decrement first, then use |
| `x--` | Post-decrement | `y = x--` | Use first, then decrement |

```java
int x = 5;
int a = x++;    // a = 5, x = 6
int b = ++x;    // x = 7, b = 7
```

---

## Assignment Operators

| Operator | Example | Equivalent |
|----------|---------|------------|
| `=` | `x = 5` | Assign |
| `+=` | `x += 5` | `x = x + 5` |
| `-=` | `x -= 5` | `x = x - 5` |
| `*=` | `x *= 5` | `x = x * 5` |
| `/=` | `x /= 5` | `x = x / 5` |
| `%=` | `x %= 5` | `x = x % 5` |

---

## Comparison Operators

| Operator | Name | Example |
|----------|------|---------|
| `==` | Equal | `x == y` |
| `!=` | Not equal | `x != y` |
| `>` | Greater than | `x > y` |
| `<` | Less than | `x < y` |
| `>=` | Greater or equal | `x >= y` |
| `<=` | Less or equal | `x <= y` |

```java
// Primitives: use ==
int a = 5, b = 5;
a == b          // true

// Objects: use .equals()
String s1 = new String("hi");
String s2 = new String("hi");
s1 == s2        // false (different objects)
s1.equals(s2)   // true (same content)
```

---

## Logical Operators

| Operator | Name | Description |
|----------|------|-------------|
| `&&` | AND | True if both true |
| `\|\|` | OR | True if either true |
| `!` | NOT | Inverts value |

```java
true && true    // true
true && false   // false
true || false   // true
false || false  // false
!true           // false
```

**Short-circuit:** Second operand skipped if result known.

```java
// Safe null check (name.length() not called if null)
if (name != null && name.length() > 0) { }
```

---

## Bitwise Operators

| Operator | Name | Description |
|----------|------|-------------|
| `&` | AND | 1 if both bits 1 |
| `\|` | OR | 1 if either bit 1 |
| `^` | XOR | 1 if bits differ |
| `~` | NOT | Inverts all bits |
| `<<` | Left shift | Multiply by 2^n |
| `>>` | Right shift | Divide by 2^n (signed) |
| `>>>` | Unsigned right | Divide, fill with 0 |

```java
5 & 3           // 1    (0101 & 0011 = 0001)
5 | 3           // 7    (0101 | 0011 = 0111)
5 ^ 3           // 6    (0101 ^ 0011 = 0110)
~5              // -6
8 << 1          // 16   (8 * 2)
8 >> 1          // 4    (8 / 2)
```

---

## Ternary Operator

**Syntax:** `condition ? valueIfTrue : valueIfFalse`

```java
int max = (a > b) ? a : b;
String s = (name != null) ? name : "Guest";
```

---

## instanceof Operator

```java
obj instanceof String       // true if obj is a String

// Pattern matching (Java 16+)
if (obj instanceof String s) {
    // s is already cast to String
    System.out.println(s.toUpperCase());
}
```

---

## Operator Precedence

Highest to lowest:

| Level | Operators |
|-------|-----------|
| 1 | `()` `[]` `.` |
| 2 | `++` `--` `!` `~` unary `+` `-` |
| 3 | `*` `/` `%` |
| 4 | `+` `-` |
| 5 | `<<` `>>` `>>>` |
| 6 | `<` `<=` `>` `>=` `instanceof` |
| 7 | `==` `!=` |
| 8 | `&` |
| 9 | `^` |
| 10 | `\|` |
| 11 | `&&` |
| 12 | `\|\|` |
| 13 | `? :` |
| 14 | `=` `+=` `-=` etc. |

```java
2 + 3 * 4       // 14 (not 20)
(2 + 3) * 4     // 20
```

---

## String Concatenation

```java
"Hello" + " World"          // "Hello World"
"Age: " + 25                // "Age: 25"
"Sum: " + 1 + 2             // "Sum: 12"
"Sum: " + (1 + 2)           // "Sum: 3"
1 + 2 + " items"            // "3 items"
```

---

## Common Patterns

```java
// Check even/odd
n % 2 == 0                  // Even
n % 2 != 0                  // Odd
(n & 1) == 0                // Even (bitwise)

// Max/Min
int max = (a > b) ? a : b;
int min = (a < b) ? a : b;

// Swap without temp
a = a ^ b; b = a ^ b; a = a ^ b;

// Absolute value
int abs = (x < 0) ? -x : x;

// Divisibility check
n % 5 == 0                  // Divisible by 5

// Wrap around
hour % 24                   // 0-23 range

// Get last digit
n % 10

// Fast multiply/divide by 2
n << 1                      // n * 2
n >> 1                      // n / 2
```

---

## Common Mistakes

```java
// = vs ==
if (x = 5) { }              // Error!
if (x == 5) { }             // Correct

// Integer division
5 / 2                       // 2, not 2.5!

// Object comparison
str1 == str2                // Compares references
str1.equals(str2)           // Compares content

// Precedence
"x = " + 1 + 2              // "x = 12"
"x = " + (1 + 2)            // "x = 3"
```
