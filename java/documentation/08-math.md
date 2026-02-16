# Math in Java

[<- Previous: Methods](07-methods.md) | [Next: Date and Time ->](09-date-time.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Math Cheat Sheet](../cheatsheets/math-cheatsheet.md)

---

## Overview

Java provides the `Math` class for performing mathematical operations. This class is part of `java.lang` package, so no import is needed. All methods in the `Math` class are static, so you call them directly on the class.

```java
// No need to import - java.lang is automatically imported
double result = Math.sqrt(16);  // 4.0
```

---

## Basic Arithmetic Operations

### Absolute Value

Returns the absolute (positive) value of a number.

```java
// Math.abs() - works with int, long, float, double
int absInt = Math.abs(-10);          // 10
long absLong = Math.abs(-100L);      // 100
float absFloat = Math.abs(-3.14f);   // 3.14
double absDouble = Math.abs(-2.718); // 2.718

// Edge case: Integer.MIN_VALUE
int edge = Math.abs(Integer.MIN_VALUE);  // Returns Integer.MIN_VALUE (overflow!)
// Use Math.absExact() in Java 15+ to throw exception on overflow
```

### Minimum and Maximum

Find the smaller or larger of two values.

```java
// Math.min() and Math.max()
int minInt = Math.min(5, 10);         // 5
int maxInt = Math.max(5, 10);         // 10

double minDouble = Math.min(3.14, 2.71);  // 2.71
double maxDouble = Math.max(3.14, 2.71);  // 3.14

// Finding min/max of multiple values
int a = 5, b = 3, c = 8, d = 1;
int min = Math.min(Math.min(a, b), Math.min(c, d));  // 1
int max = Math.max(Math.max(a, b), Math.max(c, d));  // 8

// Alternative: Use streams for multiple values
int[] numbers = {5, 3, 8, 1, 9, 2};
int minValue = Arrays.stream(numbers).min().getAsInt();  // 1
int maxValue = Arrays.stream(numbers).max().getAsInt();  // 9
```

### Sum and Product (Java 8+)

```java
// Math.addExact() - throws ArithmeticException on overflow
int sum = Math.addExact(100, 200);           // 300
// Math.addExact(Integer.MAX_VALUE, 1);      // Throws ArithmeticException

// Math.subtractExact()
int diff = Math.subtractExact(500, 200);     // 300

// Math.multiplyExact()
int product = Math.multiplyExact(10, 20);    // 200

// Math.incrementExact() and Math.decrementExact()
int inc = Math.incrementExact(10);           // 11
int dec = Math.decrementExact(10);           // 9

// Math.negateExact()
int neg = Math.negateExact(10);              // -10
// Math.negateExact(Integer.MIN_VALUE);      // Throws ArithmeticException
```

### Division and Modulus

```java
// Math.floorDiv() - rounds toward negative infinity
int floorDiv1 = Math.floorDiv(7, 3);     // 2
int floorDiv2 = Math.floorDiv(-7, 3);    // -3 (not -2!)
int regularDiv = -7 / 3;                  // -2 (rounds toward zero)

// Math.floorMod() - floor modulus
int floorMod1 = Math.floorMod(7, 3);     // 1
int floorMod2 = Math.floorMod(-7, 3);    // 2 (not -1!)
int regularMod = -7 % 3;                  // -1

// Useful for cyclic operations (clock arithmetic)
// Hours on a 12-hour clock
int hour = -2;
int clockHour = Math.floorMod(hour, 12); // 10 (not -2)
```

---

## Power and Root Operations

### Power (Exponentiation)

```java
// Math.pow(base, exponent) - returns double
double power1 = Math.pow(2, 3);      // 8.0 (2^3)
double power2 = Math.pow(2, 10);     // 1024.0
double power3 = Math.pow(5, 0);      // 1.0 (anything^0 = 1)
double power4 = Math.pow(2, -1);     // 0.5 (2^-1 = 1/2)
double power5 = Math.pow(2, 0.5);    // 1.414... (square root of 2)

// For integer powers, consider using a loop for precision
long intPower = 1;
for (int i = 0; i < 10; i++) {
    intPower *= 2;  // 2^10 = 1024
}
```

### Square Root

```java
// Math.sqrt() - returns double
double sqrt1 = Math.sqrt(16);     // 4.0
double sqrt2 = Math.sqrt(2);      // 1.4142135623730951
double sqrt3 = Math.sqrt(0);      // 0.0
double sqrt4 = Math.sqrt(-1);     // NaN (Not a Number)

// Check for valid input
double value = -4;
if (value >= 0) {
    double result = Math.sqrt(value);
} else {
    System.out.println("Cannot compute square root of negative number");
}
```

### Cube Root

```java
// Math.cbrt() - cube root, works with negative numbers
double cbrt1 = Math.cbrt(27);     // 3.0
double cbrt2 = Math.cbrt(-27);    // -3.0
double cbrt3 = Math.cbrt(8);      // 2.0
```

### Nth Root

```java
// No direct method, use pow with fractional exponent
// nth root of x = x^(1/n)
double fourthRoot = Math.pow(16, 1.0/4);  // 2.0 (4th root of 16)
double fifthRoot = Math.pow(32, 1.0/5);   // 2.0 (5th root of 32)

// For negative numbers, this doesn't work directly
// Math.pow(-27, 1.0/3) returns NaN
// Use cbrt() or handle sign separately
double negativeRoot = -Math.pow(Math.abs(-27), 1.0/3);  // -3.0
```

### Hypotenuse

```java
// Math.hypot(x, y) - returns sqrt(x^2 + y^2) without intermediate overflow
double hyp1 = Math.hypot(3, 4);   // 5.0
double hyp2 = Math.hypot(5, 12);  // 13.0

// More accurate than Math.sqrt(a*a + b*b) for very large values
double a = 3e200;
double b = 4e200;
// Math.sqrt(a*a + b*b) would overflow
double safe = Math.hypot(a, b);   // 5e200
```

---

## Exponential and Logarithmic Functions

### Exponential (e^x)

```java
// Math.E - Euler's number (approximately 2.71828)
double e = Math.E;  // 2.718281828459045

// Math.exp(x) - returns e^x
double exp1 = Math.exp(1);    // 2.718... (e^1 = e)
double exp2 = Math.exp(0);    // 1.0 (e^0 = 1)
double exp3 = Math.exp(2);    // 7.389... (e^2)
double exp4 = Math.exp(-1);   // 0.368... (e^-1 = 1/e)

// Math.expm1(x) - returns e^x - 1 (more accurate for small x)
double expm1 = Math.expm1(0.0001);  // More accurate than exp(0.0001) - 1
```

### Natural Logarithm (ln)

```java
// Math.log(x) - natural logarithm (base e)
double log1 = Math.log(Math.E);   // 1.0 (ln(e) = 1)
double log2 = Math.log(1);        // 0.0 (ln(1) = 0)
double log3 = Math.log(10);       // 2.302585...
double log4 = Math.log(0);        // -Infinity
double log5 = Math.log(-1);       // NaN

// Math.log1p(x) - returns ln(1 + x) (more accurate for small x)
double log1p = Math.log1p(0.0001);  // More accurate than log(1.0001)
```

### Base-10 Logarithm

```java
// Math.log10(x) - logarithm base 10
double log10_1 = Math.log10(10);     // 1.0
double log10_2 = Math.log10(100);    // 2.0
double log10_3 = Math.log10(1000);   // 3.0
double log10_4 = Math.log10(1);      // 0.0

// Count digits in a positive integer
int number = 12345;
int digitCount = (int) Math.log10(number) + 1;  // 5
```

### Logarithm with Custom Base

```java
// No direct method, use change of base formula
// log_b(x) = ln(x) / ln(b)
double logBase2 = Math.log(8) / Math.log(2);    // 3.0 (log2(8) = 3)
double logBase5 = Math.log(125) / Math.log(5);  // 3.0 (log5(125) = 3)

// Helper method for arbitrary base
public static double logBase(double value, double base) {
    return Math.log(value) / Math.log(base);
}

// Usage
double result = logBase(1024, 2);  // 10.0
```

---

## Trigonometric Functions

### Basic Trigonometric Functions

All trigonometric functions in Java use radians, not degrees.

```java
// Math.PI - the constant pi
double pi = Math.PI;  // 3.141592653589793

// Convert degrees to radians
double radians = Math.toRadians(90);  // 1.5707963267948966 (pi/2)

// Convert radians to degrees
double degrees = Math.toDegrees(Math.PI);  // 180.0

// Math.sin(x) - sine
double sin0 = Math.sin(0);                    // 0.0
double sin90 = Math.sin(Math.toRadians(90));  // 1.0
double sin45 = Math.sin(Math.toRadians(45));  // 0.7071...

// Math.cos(x) - cosine
double cos0 = Math.cos(0);                    // 1.0
double cos90 = Math.cos(Math.toRadians(90));  // ~0 (very small number)
double cos60 = Math.cos(Math.toRadians(60));  // 0.5

// Math.tan(x) - tangent
double tan0 = Math.tan(0);                    // 0.0
double tan45 = Math.tan(Math.toRadians(45));  // 1.0
```

### Inverse Trigonometric Functions

```java
// Math.asin(x) - arc sine, returns radians in [-pi/2, pi/2]
double asin1 = Math.asin(1);       // 1.5707... (pi/2, or 90 degrees)
double asin0 = Math.asin(0);       // 0.0
double asinHalf = Math.asin(0.5);  // 0.5235... (pi/6, or 30 degrees)

// Math.acos(x) - arc cosine, returns radians in [0, pi]
double acos1 = Math.acos(1);       // 0.0
double acos0 = Math.acos(0);       // 1.5707... (pi/2)
double acosHalf = Math.acos(0.5);  // 1.0471... (pi/3, or 60 degrees)

// Math.atan(x) - arc tangent, returns radians in (-pi/2, pi/2)
double atan1 = Math.atan(1);       // 0.7853... (pi/4, or 45 degrees)
double atan0 = Math.atan(0);       // 0.0

// Math.atan2(y, x) - angle from positive x-axis to point (x, y)
// Returns radians in (-pi, pi]
double angle = Math.atan2(1, 1);              // 0.7853... (45 degrees)
double angleNeg = Math.atan2(-1, -1);         // -2.356... (-135 degrees)
double angleDegrees = Math.toDegrees(angle);  // 45.0
```

### Hyperbolic Functions

```java
// Math.sinh(x) - hyperbolic sine
double sinh1 = Math.sinh(1);  // 1.1752011936438014

// Math.cosh(x) - hyperbolic cosine
double cosh1 = Math.cosh(1);  // 1.5430806348152437

// Math.tanh(x) - hyperbolic tangent
double tanh1 = Math.tanh(1);  // 0.7615941559557649

// Useful for machine learning activation functions
public static double sigmoid(double x) {
    return 1.0 / (1.0 + Math.exp(-x));
}
// Equivalent using tanh:
public static double sigmoidTanh(double x) {
    return 0.5 * (1 + Math.tanh(x / 2));
}
```

---

## Rounding Functions

### Rounding to Nearest Integer

```java
// Math.round() - rounds to nearest integer
// Returns long for double, int for float
long round1 = Math.round(2.3);    // 2
long round2 = Math.round(2.5);    // 3 (rounds up at .5)
long round3 = Math.round(2.7);    // 3
long round4 = Math.round(-2.5);   // -2 (rounds toward positive infinity)

int roundFloat = Math.round(2.5f);  // 3 (int for float input)
```

### Floor - Round Down

```java
// Math.floor() - rounds toward negative infinity
double floor1 = Math.floor(2.9);   // 2.0
double floor2 = Math.floor(2.1);   // 2.0
double floor3 = Math.floor(2.0);   // 2.0
double floor4 = Math.floor(-2.1);  // -3.0 (not -2!)
double floor5 = Math.floor(-2.9);  // -3.0
```

### Ceiling - Round Up

```java
// Math.ceil() - rounds toward positive infinity
double ceil1 = Math.ceil(2.1);   // 3.0
double ceil2 = Math.ceil(2.9);   // 3.0
double ceil3 = Math.ceil(2.0);   // 2.0
double ceil4 = Math.ceil(-2.1);  // -2.0
double ceil5 = Math.ceil(-2.9);  // -2.0
```

### Truncation - Round Toward Zero

```java
// No direct truncate method, but casting to int truncates
double value = 2.9;
int truncated = (int) value;  // 2

// For negative numbers
int truncNeg = (int) -2.9;    // -2 (not -3)

// Using Math methods
double truncPositive = Math.floor(2.9);   // 2.0
double truncNegative = Math.ceil(-2.9);   // -2.0

// General truncate function
public static double truncate(double value) {
    return value >= 0 ? Math.floor(value) : Math.ceil(value);
}
```

### Round to Specific Decimal Places

```java
// Round to n decimal places
double value = 3.14159265359;

// Method 1: Using Math.round
double rounded2 = Math.round(value * 100.0) / 100.0;  // 3.14

// Method 2: Using BigDecimal (more precise)
import java.math.BigDecimal;
import java.math.RoundingMode;

BigDecimal bd = new BigDecimal(value);
bd = bd.setScale(2, RoundingMode.HALF_UP);
double result = bd.doubleValue();  // 3.14

// Method 3: Using String.format (for display)
String formatted = String.format("%.2f", value);  // "3.14"

// Helper method
public static double roundToPlaces(double value, int places) {
    double scale = Math.pow(10, places);
    return Math.round(value * scale) / scale;
}

double r1 = roundToPlaces(3.14159, 2);  // 3.14
double r2 = roundToPlaces(3.14159, 4);  // 3.1416
```

### rint - Round to Nearest Even

```java
// Math.rint() - rounds to nearest integer, ties go to even
double rint1 = Math.rint(2.5);   // 2.0 (2 is even)
double rint2 = Math.rint(3.5);   // 4.0 (4 is even)
double rint3 = Math.rint(4.5);   // 4.0 (4 is even)
double rint4 = Math.rint(2.4);   // 2.0
double rint5 = Math.rint(2.6);   // 3.0

// This is "banker's rounding" - reduces bias in statistical calculations
```

---

## Random Number Generation

### Math.random()

Returns a random double between 0.0 (inclusive) and 1.0 (exclusive).

```java
// Basic usage
double random = Math.random();  // e.g., 0.7423965312548765

// Random integer in range [0, n)
int randomInt = (int) (Math.random() * 10);  // 0-9

// Random integer in range [min, max]
int min = 5;
int max = 15;
int randomInRange = (int) (Math.random() * (max - min + 1)) + min;  // 5-15

// Random double in range [min, max)
double minD = 1.0;
double maxD = 10.0;
double randomDouble = Math.random() * (maxD - minD) + minD;  // 1.0-10.0
```

### java.util.Random Class

For more control and thread safety, use the Random class.

```java
import java.util.Random;

Random random = new Random();

// Random int in full range
int anyInt = random.nextInt();

// Random int in range [0, bound)
int randomBound = random.nextInt(100);  // 0-99

// Random int in range [min, max] (Java 17+)
int randomRange = random.nextInt(5, 16);  // 5-15

// Random long
long randomLong = random.nextLong();

// Random double [0.0, 1.0)
double randomDouble = random.nextDouble();

// Random float [0.0, 1.0)
float randomFloat = random.nextFloat();

// Random boolean
boolean randomBool = random.nextBoolean();

// Random Gaussian (normal distribution, mean 0, std dev 1)
double gaussian = random.nextGaussian();

// Seeded random (reproducible)
Random seeded = new Random(12345);
int r1 = seeded.nextInt(100);  // Always same sequence for same seed
```

### ThreadLocalRandom (Java 7+)

Better performance for multi-threaded applications.

```java
import java.util.concurrent.ThreadLocalRandom;

// Random int in range [min, max)
int random = ThreadLocalRandom.current().nextInt(1, 101);  // 1-100

// Random long in range
long randomLong = ThreadLocalRandom.current().nextLong(1000, 10000);

// Random double in range
double randomDouble = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

// Random boolean
boolean randomBool = ThreadLocalRandom.current().nextBoolean();
```

### SecureRandom (Cryptographic)

For security-sensitive random numbers (passwords, tokens, etc.).

```java
import java.security.SecureRandom;

SecureRandom secureRandom = new SecureRandom();

// Random bytes
byte[] bytes = new byte[16];
secureRandom.nextBytes(bytes);

// Random int
int secureInt = secureRandom.nextInt(1000);

// Generate secure token
public static String generateToken(int length) {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return Base64.getEncoder().encodeToString(bytes);
}
```

### Random Streams (Java 8+)

```java
import java.util.Random;

Random random = new Random();

// Stream of random ints
random.ints(5, 1, 101)  // 5 random ints from 1-100
    .forEach(System.out::println);

// Stream of random doubles
random.doubles(3, 0.0, 10.0)  // 3 random doubles from 0-10
    .forEach(System.out::println);

// Infinite stream (limit it!)
random.ints(1, 7)  // Infinite stream of 1-6 (dice)
    .limit(10)
    .forEach(System.out::println);
```

---

## Sign and Comparison Functions

### Sign Function

```java
// Math.signum() - returns sign as -1, 0, or 1
double sign1 = Math.signum(15.5);   // 1.0
double sign2 = Math.signum(-15.5);  // -1.0
double sign3 = Math.signum(0);      // 0.0

// Integer version
int signInt = Integer.signum(-100);  // -1

// Copy sign from one number to another
double copy1 = Math.copySign(5.0, -1.0);   // -5.0
double copy2 = Math.copySign(-5.0, 1.0);   // 5.0
double copy3 = Math.copySign(5.0, -0.0);   // -5.0
```

### Comparing Floating Point Numbers

```java
// Direct comparison can fail due to precision
double a = 0.1 + 0.2;
double b = 0.3;
System.out.println(a == b);  // false! (0.30000000000000004 != 0.3)

// Use epsilon comparison
public static boolean equals(double a, double b, double epsilon) {
    return Math.abs(a - b) < epsilon;
}

boolean result = equals(0.1 + 0.2, 0.3, 0.0001);  // true

// Using Math.ulp (unit in last place)
double ulp = Math.ulp(1.0);  // Smallest difference from 1.0

// Compare with tolerance based on magnitude
public static boolean nearlyEqual(double a, double b) {
    double epsilon = Math.max(Math.ulp(a), Math.ulp(b)) * 2;
    return Math.abs(a - b) <= epsilon;
}
```

---

## Special Values and Constants

### Constants

```java
// Math.PI - ratio of circumference to diameter
double pi = Math.PI;  // 3.141592653589793

// Math.E - Euler's number (base of natural logarithm)
double e = Math.E;    // 2.718281828459045

// No constant for golden ratio, but can calculate
double phi = (1 + Math.sqrt(5)) / 2;  // 1.618033988749895
```

### Special Floating Point Values

```java
// Positive and negative infinity
double posInf = Double.POSITIVE_INFINITY;
double negInf = Double.NEGATIVE_INFINITY;
double inf = 1.0 / 0.0;  // POSITIVE_INFINITY

// Not a Number (NaN)
double nan = Double.NaN;
double nanCalc = 0.0 / 0.0;  // NaN
double nanSqrt = Math.sqrt(-1);  // NaN

// Check for special values
boolean isInfinite = Double.isInfinite(posInf);  // true
boolean isNaN = Double.isNaN(nan);               // true
boolean isFinite = Double.isFinite(3.14);        // true

// NaN comparisons are always false
System.out.println(nan == nan);       // false!
System.out.println(nan < 1);          // false
System.out.println(nan > 1);          // false
System.out.println(Double.isNaN(nan)); // true (correct way to check)

// Max and min values
double maxValue = Double.MAX_VALUE;  // 1.7976931348623157E308
double minValue = Double.MIN_VALUE;  // 4.9E-324 (smallest positive)
```

### getExponent and scalb

```java
// Math.getExponent() - returns unbiased exponent
int exp1 = Math.getExponent(8.0);    // 3 (8 = 2^3)
int exp2 = Math.getExponent(0.25);   // -2 (0.25 = 2^-2)

// Math.scalb(d, scaleFactor) - returns d * 2^scaleFactor
double scaled1 = Math.scalb(3.0, 2);   // 12.0 (3 * 2^2)
double scaled2 = Math.scalb(4.0, -1);  // 2.0 (4 * 2^-1)
```

---

## IEEE 754 Operations

### nextUp and nextDown

```java
// Math.nextUp() - next larger floating-point value
double next = Math.nextUp(1.0);  // 1.0000000000000002

// Math.nextDown() - next smaller floating-point value
double prev = Math.nextDown(1.0);  // 0.9999999999999999

// Math.nextAfter(start, direction) - next value toward direction
double toward = Math.nextAfter(1.0, 2.0);   // 1.0000000000000002
double towardNeg = Math.nextAfter(1.0, 0.0); // 0.9999999999999999
```

### FMA (Fused Multiply-Add) - Java 9+

```java
// Math.fma(a, b, c) - computes (a * b) + c with single rounding
// More accurate than separate multiply and add
double fma = Math.fma(2.0, 3.0, 4.0);  // 10.0

// Useful for matrix operations, dot products
double[] a = {1.0, 2.0, 3.0};
double[] b = {4.0, 5.0, 6.0};
double dotProduct = 0;
for (int i = 0; i < a.length; i++) {
    dotProduct = Math.fma(a[i], b[i], dotProduct);
}
// Result: 32.0 (1*4 + 2*5 + 3*6)
```

---

## BigDecimal for Precision

For financial calculations or when exact decimal representation is needed.

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;

// Creating BigDecimal
BigDecimal bd1 = new BigDecimal("123.45");  // Use String for exact value
BigDecimal bd2 = BigDecimal.valueOf(123.45);  // From double
BigDecimal bd3 = new BigDecimal(100);  // From int

// Basic operations (BigDecimal is immutable - returns new object)
BigDecimal sum = bd1.add(bd2);
BigDecimal diff = bd1.subtract(bd2);
BigDecimal product = bd1.multiply(bd2);
BigDecimal quotient = bd1.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);

// Comparisons
int cmp = bd1.compareTo(bd2);  // -1, 0, or 1
boolean equal = bd1.compareTo(bd2) == 0;  // Don't use equals()!

// Rounding
BigDecimal rounded = bd1.setScale(1, RoundingMode.HALF_UP);  // 123.5

// Power
BigDecimal power = bd1.pow(2);  // 15239.9025

// Square root (Java 9+)
BigDecimal sqrt = bd1.sqrt(MathContext.DECIMAL64);

// Rounding modes
// HALF_UP - rounds toward nearest, .5 rounds up (3.5 -> 4)
// HALF_DOWN - rounds toward nearest, .5 rounds down (3.5 -> 3)
// HALF_EVEN - banker's rounding (3.5 -> 4, 2.5 -> 2)
// UP - rounds away from zero
// DOWN - rounds toward zero (truncate)
// CEILING - rounds toward positive infinity
// FLOOR - rounds toward negative infinity
```

### Precision Examples

```java
// Why use BigDecimal
double d1 = 0.1 + 0.2;
System.out.println(d1);  // 0.30000000000000004

BigDecimal b1 = new BigDecimal("0.1");
BigDecimal b2 = new BigDecimal("0.2");
BigDecimal b3 = b1.add(b2);
System.out.println(b3);  // 0.3 (exact!)

// Money calculation
BigDecimal price = new BigDecimal("19.99");
BigDecimal quantity = new BigDecimal("3");
BigDecimal tax = new BigDecimal("0.08");

BigDecimal subtotal = price.multiply(quantity);  // 59.97
BigDecimal taxAmount = subtotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);  // 4.80
BigDecimal total = subtotal.add(taxAmount);  // 64.77
```

---

## BigInteger for Large Numbers

For arbitrarily large integers beyond long range.

```java
import java.math.BigInteger;

// Creating BigInteger
BigInteger big1 = new BigInteger("123456789012345678901234567890");
BigInteger big2 = BigInteger.valueOf(1000000L);
BigInteger big3 = new BigInteger("FF", 16);  // From hex: 255

// Constants
BigInteger zero = BigInteger.ZERO;
BigInteger one = BigInteger.ONE;
BigInteger two = BigInteger.TWO;  // Java 9+
BigInteger ten = BigInteger.TEN;

// Basic operations
BigInteger sum = big1.add(big2);
BigInteger diff = big1.subtract(big2);
BigInteger product = big1.multiply(big2);
BigInteger quotient = big1.divide(big2);
BigInteger remainder = big1.mod(big2);

// Power
BigInteger power = BigInteger.valueOf(2).pow(100);  // 2^100

// GCD and LCM
BigInteger gcd = big1.gcd(big2);
// LCM = |a * b| / gcd(a, b)
BigInteger lcm = big1.multiply(big2).divide(big1.gcd(big2)).abs();

// Modular arithmetic
BigInteger modPow = big1.modPow(big2, BigInteger.valueOf(1000));

// Primality testing
boolean isProbablePrime = big1.isProbablePrime(10);  // 10 = certainty

// Bit operations
int bitLength = big1.bitLength();
int bitCount = big1.bitCount();  // Number of 1 bits
BigInteger shifted = big1.shiftLeft(10);

// Comparison
int cmp = big1.compareTo(big2);  // -1, 0, or 1

// Conversion
long longValue = big2.longValue();
String string = big1.toString();
String hex = big1.toString(16);
```

### Factorial with BigInteger

```java
public static BigInteger factorial(int n) {
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
        result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
}

BigInteger fact100 = factorial(100);
// 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000
```

---

## Practical Examples

### Distance Between Two Points

```java
public static double distance(double x1, double y1, double x2, double y2) {
    return Math.hypot(x2 - x1, y2 - y1);
}

double dist = distance(0, 0, 3, 4);  // 5.0
```

### Angle Between Two Points

```java
public static double angleDegrees(double x1, double y1, double x2, double y2) {
    return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
}

double angle = angleDegrees(0, 0, 1, 1);  // 45.0
```

### Compound Interest

```java
// A = P(1 + r/n)^(nt)
// P = principal, r = annual rate, n = compounds per year, t = years
public static double compoundInterest(double principal, double rate, 
                                       int compounds, int years) {
    return principal * Math.pow(1 + rate / compounds, compounds * years);
}

double amount = compoundInterest(1000, 0.05, 12, 10);  // ~1647.01
```

### Quadratic Formula

```java
public static double[] solveQuadratic(double a, double b, double c) {
    double discriminant = b * b - 4 * a * c;
    
    if (discriminant < 0) {
        return new double[0];  // No real solutions
    } else if (discriminant == 0) {
        return new double[] { -b / (2 * a) };  // One solution
    } else {
        double sqrtD = Math.sqrt(discriminant);
        return new double[] {
            (-b + sqrtD) / (2 * a),
            (-b - sqrtD) / (2 * a)
        };
    }
}

// Solve x^2 - 5x + 6 = 0
double[] roots = solveQuadratic(1, -5, 6);  // [3.0, 2.0]
```

### Circle Calculations

```java
public class Circle {
    public static double area(double radius) {
        return Math.PI * radius * radius;
    }
    
    public static double circumference(double radius) {
        return 2 * Math.PI * radius;
    }
    
    public static double diameter(double radius) {
        return 2 * radius;
    }
    
    // Point on circle
    public static double[] pointOnCircle(double centerX, double centerY, 
                                          double radius, double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        return new double[] {
            centerX + radius * Math.cos(radians),
            centerY + radius * Math.sin(radians)
        };
    }
}
```

### Prime Number Check

```java
public static boolean isPrime(int n) {
    if (n < 2) return false;
    if (n == 2) return true;
    if (n % 2 == 0) return false;
    
    int sqrt = (int) Math.sqrt(n);
    for (int i = 3; i <= sqrt; i += 2) {
        if (n % i == 0) return false;
    }
    return true;
}

// Check primes
System.out.println(isPrime(17));  // true
System.out.println(isPrime(18));  // false
```

### GCD and LCM

```java
// Greatest Common Divisor (Euclidean algorithm)
public static int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return Math.abs(a);
}

// Least Common Multiple
public static int lcm(int a, int b) {
    return Math.abs(a * b) / gcd(a, b);
}

System.out.println(gcd(48, 18));  // 6
System.out.println(lcm(4, 6));    // 12
```

### Clamp Value to Range

```java
// Clamp value between min and max
public static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
}

public static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
}

// Java 21+ has Math.clamp()
int clamped = Math.clamp(15, 0, 10);  // 10

int result1 = clamp(5, 0, 10);   // 5
int result2 = clamp(-5, 0, 10);  // 0
int result3 = clamp(15, 0, 10);  // 10
```

### Linear Interpolation

```java
// Lerp between two values
public static double lerp(double start, double end, double t) {
    return start + t * (end - start);
}

double result = lerp(0, 100, 0.5);  // 50.0
double result2 = lerp(0, 100, 0.25);  // 25.0
```

### Map Value from One Range to Another

```java
public static double map(double value, double fromMin, double fromMax, 
                         double toMin, double toMax) {
    return (value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
}

// Map 50 from range 0-100 to range 0-1
double result = map(50, 0, 100, 0, 1);  // 0.5

// Map temperature from Celsius to Fahrenheit
double fahrenheit = map(20, 0, 100, 32, 212);  // 68.0
```

---

## Summary

### Core Math Methods

| Method | Description | Example |
|--------|-------------|---------|
| `abs(x)` | Absolute value | `abs(-5)` = 5 |
| `min(a, b)` | Smaller value | `min(3, 7)` = 3 |
| `max(a, b)` | Larger value | `max(3, 7)` = 7 |
| `pow(x, y)` | x to the power y | `pow(2, 3)` = 8.0 |
| `sqrt(x)` | Square root | `sqrt(16)` = 4.0 |
| `cbrt(x)` | Cube root | `cbrt(27)` = 3.0 |

### Rounding Methods

| Method | Description | Example |
|--------|-------------|---------|
| `round(x)` | Round to nearest | `round(2.5)` = 3 |
| `floor(x)` | Round down | `floor(2.9)` = 2.0 |
| `ceil(x)` | Round up | `ceil(2.1)` = 3.0 |
| `rint(x)` | Round to nearest even | `rint(2.5)` = 2.0 |

### Trigonometric Methods

| Method | Description |
|--------|-------------|
| `sin(x)`, `cos(x)`, `tan(x)` | Basic trig (radians) |
| `asin(x)`, `acos(x)`, `atan(x)` | Inverse trig |
| `toRadians(x)`, `toDegrees(x)` | Conversion |

### Random Numbers

| Class | Use Case |
|-------|----------|
| `Math.random()` | Simple random [0, 1) |
| `Random` | General purpose |
| `ThreadLocalRandom` | Multi-threaded |
| `SecureRandom` | Cryptographic |

### Precision Classes

| Class | Use Case |
|-------|----------|
| `BigDecimal` | Exact decimal math, money |
| `BigInteger` | Arbitrarily large integers |

**Key Points:**
- All Math methods are static
- Trigonometric functions use radians
- Use BigDecimal for financial calculations
- Use BigInteger for numbers beyond long range
- Be careful with floating-point comparisons
- Use appropriate random class for your use case

---

[<- Previous: Methods](07-methods.md) | [Next: Date and Time ->](09-date-time.md) | [Back to Guide](../guide.md)
