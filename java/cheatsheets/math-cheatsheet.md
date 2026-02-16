# Math Cheat Sheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/08-math.md)

---

## Basic Arithmetic

```java
// Absolute value
Math.abs(-10)       // 10
Math.abs(-3.14)     // 3.14

// Min and Max
Math.min(5, 10)     // 5
Math.max(5, 10)     // 10

// Exact arithmetic (throws on overflow)
Math.addExact(a, b)
Math.subtractExact(a, b)
Math.multiplyExact(a, b)

// Floor division (rounds toward -infinity)
Math.floorDiv(7, 3)     // 2
Math.floorDiv(-7, 3)    // -3
```

---

## Power and Roots

```java
// Power
Math.pow(2, 3)      // 8.0 (2^3)
Math.pow(2, 0.5)    // 1.414... (sqrt of 2)

// Square root
Math.sqrt(16)       // 4.0
Math.sqrt(-1)       // NaN

// Cube root
Math.cbrt(27)       // 3.0
Math.cbrt(-27)      // -3.0

// Nth root (use pow)
Math.pow(16, 1.0/4) // 2.0 (4th root)

// Hypotenuse
Math.hypot(3, 4)    // 5.0
```

---

## Exponential and Logarithmic

```java
// Constants
Math.E              // 2.718281828459045
Math.PI             // 3.141592653589793

// Exponential
Math.exp(1)         // e^1 = 2.718...
Math.expm1(x)       // e^x - 1 (precise for small x)

// Natural log
Math.log(Math.E)    // 1.0
Math.log1p(x)       // ln(1+x) (precise for small x)

// Base-10 log
Math.log10(100)     // 2.0

// Custom base log
Math.log(x) / Math.log(base)
```

---

## Trigonometry

```java
// All use RADIANS

// Convert
Math.toRadians(180) // PI
Math.toDegrees(Math.PI) // 180.0

// Basic trig
Math.sin(0)         // 0.0
Math.cos(0)         // 1.0
Math.tan(Math.PI/4) // 1.0

// Inverse trig
Math.asin(1)        // PI/2
Math.acos(0)        // PI/2
Math.atan(1)        // PI/4

// atan2 (handles quadrants)
Math.atan2(y, x)    // angle from +x axis
```

---

## Rounding

```java
// Round to nearest
Math.round(2.5)     // 3 (long for double)
Math.round(2.5f)    // 3 (int for float)

// Floor (toward -infinity)
Math.floor(2.9)     // 2.0
Math.floor(-2.1)    // -3.0

// Ceil (toward +infinity)
Math.ceil(2.1)      // 3.0
Math.ceil(-2.9)     // -2.0

// Round to decimal places
Math.round(value * 100.0) / 100.0  // 2 places

// Banker's rounding
Math.rint(2.5)      // 2.0 (nearest even)
Math.rint(3.5)      // 4.0 (nearest even)
```

---

## Random Numbers

```java
// Math.random() - [0.0, 1.0)
double r = Math.random();

// Random int [0, n)
int rand = (int)(Math.random() * n);

// Random int [min, max]
int rand = (int)(Math.random() * (max - min + 1)) + min;

// java.util.Random
Random random = new Random();
random.nextInt(100)    // 0-99
random.nextDouble()    // [0.0, 1.0)
random.nextBoolean()

// ThreadLocalRandom (multi-threaded)
ThreadLocalRandom.current().nextInt(1, 101)  // 1-100

// SecureRandom (cryptographic)
new SecureRandom().nextInt()
```

---

## Sign and Comparison

```java
// Sign
Math.signum(15.5)   // 1.0
Math.signum(-15.5)  // -1.0
Math.signum(0)      // 0.0

// Copy sign
Math.copySign(5.0, -1.0)  // -5.0

// Compare floats (use epsilon)
Math.abs(a - b) < 0.0001
```

---

## Special Values

```java
// Infinity
Double.POSITIVE_INFINITY
Double.NEGATIVE_INFINITY
1.0 / 0.0           // POSITIVE_INFINITY

// NaN
Double.NaN
0.0 / 0.0           // NaN
Math.sqrt(-1)       // NaN

// Check special values
Double.isInfinite(x)
Double.isNaN(x)
Double.isFinite(x)

// NaN comparisons always false!
nan == nan          // false
Double.isNaN(nan)   // true (correct check)
```

---

## BigDecimal (Precise Decimals)

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

// Create (use String for precision)
BigDecimal bd = new BigDecimal("123.45");

// Operations (immutable)
bd.add(other)
bd.subtract(other)
bd.multiply(other)
bd.divide(other, 2, RoundingMode.HALF_UP)

// Compare (NOT equals!)
bd1.compareTo(bd2)  // -1, 0, or 1

// Round
bd.setScale(2, RoundingMode.HALF_UP)
```

---

## BigInteger (Large Integers)

```java
import java.math.BigInteger;

// Create
BigInteger big = new BigInteger("12345678901234567890");

// Operations
big.add(other)
big.subtract(other)
big.multiply(other)
big.divide(other)
big.mod(other)
big.pow(10)
big.gcd(other)

// Constants
BigInteger.ZERO
BigInteger.ONE
BigInteger.TEN
```

---

## Common Formulas

```java
// Distance between points
Math.hypot(x2 - x1, y2 - y1)

// Clamp value to range
Math.max(min, Math.min(max, value))

// Degrees <-> Radians
Math.toRadians(degrees)
Math.toDegrees(radians)

// Circle area
Math.PI * radius * radius

// Check prime
for (int i = 2; i <= Math.sqrt(n); i++)

// GCD
while (b != 0) { int t = b; b = a % b; a = t; }
```

---

## Quick Reference Table

| Method | Description | Example |
|--------|-------------|---------|
| `abs(x)` | Absolute value | `abs(-5)` = 5 |
| `min(a,b)` | Smaller value | `min(3,7)` = 3 |
| `max(a,b)` | Larger value | `max(3,7)` = 7 |
| `pow(x,y)` | x^y | `pow(2,3)` = 8.0 |
| `sqrt(x)` | Square root | `sqrt(16)` = 4.0 |
| `cbrt(x)` | Cube root | `cbrt(27)` = 3.0 |
| `round(x)` | Nearest int | `round(2.5)` = 3 |
| `floor(x)` | Round down | `floor(2.9)` = 2.0 |
| `ceil(x)` | Round up | `ceil(2.1)` = 3.0 |
| `random()` | Random [0,1) | 0.7423... |

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/08-math.md)
