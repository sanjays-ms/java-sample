# Variables and Data Types Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Variables](../documentation/01-variables.md)

Quick reference for Java variables and data types.

---

## What Problem Types Solve

Java is statically typed - every variable must have a declared type. This catches errors at compile time rather than runtime, making code safer and more predictable.

---

## Declaring Variables

```java
// type variableName = value;
int age = 25;
String name = "Alice";
boolean active = true;

// Declare then assign
int count;
count = 10;

// Multiple variables
int x = 1, y = 2, z = 3;
```

---

## Primitive Types

### Integer Types

| Type | Size | Range | Example |
|------|------|-------|---------|
| `byte` | 1 byte | -128 to 127 | `byte b = 100;` |
| `short` | 2 bytes | -32,768 to 32,767 | `short s = 30000;` |
| `int` | 4 bytes | ~2.1 billion | `int i = 2000000000;` |
| `long` | 8 bytes | Very large | `long l = 9223372036854775807L;` |

```java
int million = 1_000_000;        // Underscores for readability
long big = 100L;                // L suffix for long
```

### Floating-Point Types

| Type | Size | Precision | Example |
|------|------|-----------|---------|
| `float` | 4 bytes | ~7 digits | `float f = 3.14f;` |
| `double` | 8 bytes | ~15 digits | `double d = 3.14159;` |

```java
float price = 19.99f;           // f suffix required
double precise = 3.141592653589793;
double scientific = 6.022e23;   // Scientific notation
```

### Other Primitives

| Type | Size | Values | Example |
|------|------|--------|---------|
| `char` | 2 bytes | Unicode chars | `char c = 'A';` |
| `boolean` | 1 bit | true/false | `boolean b = true;` |

```java
char letter = 'A';
char unicode = '\u0041';        // 'A'
boolean flag = true;
```

---

## Reference Types

```java
// String
String name = "Alice";
String empty = "";
String nullStr = null;

// Text blocks (Java 15+)
String json = """
    {
        "key": "value"
    }
    """;

// Arrays
int[] numbers = {1, 2, 3};
String[] names = {"A", "B"};

// Objects
ArrayList<String> list = new ArrayList<>();
```

---

## var Keyword (Java 10+)

```java
var count = 10;                 // int
var name = "Alice";             // String
var list = new ArrayList<Integer>();  // ArrayList<Integer>
var active = true;              // boolean
```

Use when type is obvious. Local variables only.

---

## Constants (final)

```java
final int MAX_SIZE = 100;
final double PI = 3.14159;
final String APP_NAME = "MyApp";

// Cannot reassign
// MAX_SIZE = 200;  // Error!
```

Convention: UPPER_SNAKE_CASE

---

## Type Casting

### Widening (Automatic)

Smaller to larger - no data loss:

```java
int myInt = 100;
long myLong = myInt;            // OK
double myDouble = myLong;       // OK
```

Order: byte -> short -> int -> long -> float -> double

### Narrowing (Manual)

Larger to smaller - may lose data:

```java
double d = 9.78;
int i = (int) d;                // 9 (truncated)

int big = 130;
byte small = (byte) big;        // -126 (overflow!)
```

---

## String Conversions

### Primitive to String

```java
int num = 42;
String s1 = String.valueOf(num);      // "42"
String s2 = Integer.toString(num);    // "42"
String s3 = num + "";                 // "42"
```

### String to Primitive

```java
int i = Integer.parseInt("123");           // 123
double d = Double.parseDouble("3.14");     // 3.14
boolean b = Boolean.parseBoolean("true");  // true
```

---

## Default Values

Instance variables (not local):

| Type | Default |
|------|---------|
| `byte`, `short`, `int`, `long` | 0 |
| `float`, `double` | 0.0 |
| `char` | '\u0000' |
| `boolean` | false |
| Reference types | null |

Local variables have NO default - must initialize.

---

## Wrapper Classes

| Primitive | Wrapper |
|-----------|---------|
| `int` | `Integer` |
| `double` | `Double` |
| `boolean` | `Boolean` |
| `char` | `Character` |
| `long` | `Long` |
| `float` | `Float` |
| `byte` | `Byte` |
| `short` | `Short` |

```java
// Autoboxing (automatic)
Integer wrapped = 100;          // int -> Integer
int unwrapped = wrapped;        // Integer -> int

// Useful methods
int max = Integer.MAX_VALUE;
int min = Integer.MIN_VALUE;
String binary = Integer.toBinaryString(10);  // "1010"
```

---

## Naming Conventions

```java
// Variables: camelCase
int itemCount;
String firstName;

// Constants: UPPER_SNAKE_CASE
final int MAX_SIZE = 100;

// Classes: PascalCase
class UserAccount { }
```

---

## Common Mistakes

```java
// Integer division truncates
int result = 5 / 2;             // 2, not 2.5
double fix = 5.0 / 2;           // 2.5

// String comparison
String a = "hello";
String b = new String("hello");
a == b;                         // false (references)
a.equals(b);                    // true (content)

// Overflow
int max = Integer.MAX_VALUE;
int overflow = max + 1;         // Negative! Wraps around
```

---

## Quick Reference

```java
// Integers
byte b = 100;
short s = 30000;
int i = 2000000000;
long l = 100L;

// Decimals
float f = 3.14f;
double d = 3.14;

// Other
char c = 'A';
boolean flag = true;
String str = "text";

// Constants
final int MAX = 100;

// Type inference
var x = 10;

// Casting
int narrowed = (int) 3.14;      // 3
double widened = 10;            // 10.0
```
