# Strings Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Strings](../documentation/06-strings.md)

Quick reference for Java String methods and operations.

---

## Creating Strings

```java
String s1 = "Hello";                      // String literal (uses pool)
String s2 = new String("Hello");          // New object (avoid)
String s3 = new String(charArray);        // From char[]
String s4 = new String(bytes, StandardCharsets.UTF_8);  // From bytes
String s5 = String.copyValueOf(charArray); // From char[] (static)
```

---

## Concatenation

```java
"Hello" + " " + "World"       // Using + operator
"Value: " + 42                // Auto-converts to String
"Sum: " + (10 + 20)           // Use () for arithmetic first
str1.concat(str2)             // Using concat method

// In loops, use StringBuilder!
StringBuilder sb = new StringBuilder();
for (...) { sb.append(x); }
String result = sb.toString();
```

---

## Length and Empty Check

```java
str.length()          // Number of characters
str.isEmpty()         // true if length == 0
str.isBlank()         // true if empty or whitespace only (Java 11+)
```

---

## Accessing Characters

```java
str.charAt(0)                 // First character
str.charAt(str.length() - 1)  // Last character
str.toCharArray()             // Convert to char[]
str.chars()                   // IntStream of chars (Java 9+)
str.codePoints()              // IntStream of Unicode code points
str.codePointAt(i)            // Unicode code point at index
str.codePointCount(0, len)    // Number of code points in range
```

---

## Comparing Strings

```java
s1.equals(s2)                 // Content equality (case-sensitive)
s1.equalsIgnoreCase(s2)       // Content equality (case-insensitive)
s1.compareTo(s2)              // Lexicographic: <0, 0, >0
s1.compareToIgnoreCase(s2)    // Lexicographic, ignore case
s1.contentEquals(charSeq)     // Compare with CharSequence
s1.regionMatches(0, s2, 0, 5) // Compare regions
"literal".equals(str)         // Null-safe comparison
Objects.equals(s1, s2)        // Null-safe comparison
str.hashCode()                // Hash code for HashMap/HashSet
```

---

## Searching

```java
str.indexOf("sub")            // First index of substring (-1 if not found)
str.indexOf("sub", 5)         // First index from position 5
str.lastIndexOf("sub")        // Last index of substring
str.contains("sub")           // true if contains substring
str.startsWith("prefix")      // true if starts with prefix
str.endsWith("suffix")        // true if ends with suffix
str.matches("regex")          // true if matches regex pattern
```

---

## Extracting Substrings

```java
str.substring(5)              // From index 5 to end
str.substring(0, 5)           // From index 0 to 5 (exclusive)
str.subSequence(0, 5)         // Returns CharSequence
```

---

## Modifying (Returns New String)

```java
str.concat(" World")          // Append string
str.replace('a', 'b')         // Replace all chars
str.replace("old", "new")     // Replace all substrings
str.replaceAll("regex", "x")  // Replace regex matches
str.replaceFirst("regex", "x") // Replace first match
str.toUpperCase()             // Convert to uppercase
str.toLowerCase()             // Convert to lowercase
str.trim()                    // Remove leading/trailing whitespace
str.strip()                   // Unicode-aware trim (Java 11+)
str.stripLeading()            // Remove leading whitespace (Java 11+)
str.stripTrailing()           // Remove trailing whitespace (Java 11+)
str.repeat(3)                 // Repeat 3 times (Java 11+)
str.indent(4)                 // Add 4 spaces indent (Java 12+)
```

---

## Splitting and Joining

```java
// Splitting
str.split(",")                // Split by comma
str.split(",", 3)             // Split into max 3 parts
str.split("\\s+")             // Split by whitespace
str.split("\\.")              // Split by dot (escape regex)

// Joining
String.join(",", arr)         // Join array with comma
String.join("-", list)        // Join list with dash
String.join("", s1, s2, s3)   // Join varargs

// StringJoiner
new StringJoiner(", ", "[", "]")  // With prefix/suffix
```

---

## Formatting

```java
String.format("Name: %s", name)           // String
String.format("Age: %d", age)             // Integer
String.format("Price: %.2f", price)       // Float with 2 decimals
String.format("%10s", str)                // Right-pad to 10 chars
String.format("%-10s", str)               // Left-pad to 10 chars
String.format("%05d", num)                // Zero-pad to 5 digits
template.formatted(arg1, arg2)            // Instance method (Java 15+)
```

| Specifier | Type | Example |
|-----------|------|---------|
| `%s` | String | `"Hello"` |
| `%d` | Integer | `42` |
| `%f` | Float | `3.14` |
| `%.2f` | Float (2 decimals) | `3.14` |
| `%n` | Newline | |
| `%b` | Boolean | `true` |
| `%c` | Character | `A` |
| `%x` | Hex | `2a` |

---

## Text Blocks (Java 15+)

```java
String json = """
    {
        "name": "Alice",
        "age": 30
    }
    """;

// Escape sequences
"""
    Line continues \
    here without newline
    Trailing spaces   \s
    """;
```

---

## StringBuilder

```java
StringBuilder sb = new StringBuilder();
sb.append("Hello");           // Append
sb.append(" ").append("World"); // Chain appends
sb.insert(5, " Java");        // Insert at index
sb.delete(0, 5);              // Delete range
sb.deleteCharAt(0);           // Delete single char
sb.replace(0, 5, "Hi");       // Replace range
sb.reverse();                 // Reverse
sb.setLength(0);              // Clear
sb.toString();                // Convert to String
```

Use StringBuilder for:
- String concatenation in loops
- Building strings dynamically
- Frequent modifications

---

## Type Conversions

```java
// To String
String.valueOf(42)            // "42"
String.valueOf(3.14)          // "3.14"
String.valueOf(true)          // "true"
String.valueOf(charArray)     // String from chars
Integer.toString(42)          // "42"
obj.toString()                // Object to String

// From String
Integer.parseInt("42")        // 42
Double.parseDouble("3.14")    // 3.14
Boolean.parseBoolean("true")  // true
Long.parseLong("123")         // 123L
Integer.parseInt("FF", 16)    // 255 (hex)

// To/From bytes
str.getBytes(StandardCharsets.UTF_8)  // String to bytes
new String(bytes, StandardCharsets.UTF_8)  // Bytes to String
```

---

## Lines and Streaming (Java 11+)

```java
str.lines()                   // Stream<String> of lines
str.lines().count()           // Count lines
str.lines().forEach(System.out::println)  // Process each line
str.transform(String::strip)  // Apply function (Java 12+)
```

---

## Character Class Utilities

```java
Character.isLetter(c)         // Check if letter
Character.isDigit(c)          // Check if digit
Character.isLetterOrDigit(c)  // Check if alphanumeric
Character.isWhitespace(c)     // Check if whitespace
Character.isUpperCase(c)      // Check if uppercase
Character.isLowerCase(c)      // Check if lowercase
Character.toUpperCase(c)      // Convert to uppercase
Character.toLowerCase(c)      // Convert to lowercase
Character.getNumericValue(c)  // Get numeric value
```

---

## Regex Methods

```java
str.matches("regex")          // Check if matches
str.replaceAll("regex", "x")  // Replace all matches
str.replaceFirst("regex", "x") // Replace first match
str.split("regex")            // Split by pattern

// Pattern and Matcher
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher(str);
m.find()                      // Find next match
m.group()                     // Get matched text
m.matches()                   // Check full match
```

---

## Common Patterns

### Null/Empty Check
```java
if (str != null && !str.isEmpty()) { }
if (str != null && !str.isBlank()) { }  // Java 11+
```

### Null-Safe Comparison
```java
"expected".equals(str)        // Won't throw if str is null
Objects.equals(s1, s2)        // Handles null
```

### Capitalize
```java
str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase()
```

### Palindrome Check
```java
str.equals(new StringBuilder(str).reverse().toString())
```

### Remove Whitespace
```java
str.replaceAll("\\s+", "")    // Remove all whitespace
str.replaceAll("\\s+", " ")   // Collapse to single space
```

### Extract Numbers
```java
str.replaceAll("[^0-9]", "")  // Keep only digits
str.replaceAll("\\D", "")     // Same result
```

---

## String Pool

```java
String s1 = "Hello";          // Uses pool
String s2 = "Hello";          // Same reference as s1
String s3 = new String("Hello"); // New object
String s4 = s3.intern();      // Add to pool, get pooled reference

s1 == s2                      // true (same pool reference)
s1 == s3                      // false (different objects)
s1 == s4                      // true (s4 is pooled)
```

---

## Quick Reference

| Task | Code |
|------|------|
| Length | `str.length()` |
| First char | `str.charAt(0)` |
| Last char | `str.charAt(str.length() - 1)` |
| Check empty | `str.isEmpty()` |
| Check blank | `str.isBlank()` |
| Equals | `s1.equals(s2)` |
| Contains | `str.contains("sub")` |
| Starts with | `str.startsWith("pre")` |
| Ends with | `str.endsWith("suf")` |
| Find index | `str.indexOf("sub")` |
| Substring | `str.substring(0, 5)` |
| Replace | `str.replace("a", "b")` |
| Split | `str.split(",")` |
| Join | `String.join(",", arr)` |
| Uppercase | `str.toUpperCase()` |
| Lowercase | `str.toLowerCase()` |
| Trim | `str.trim()` |
| Strip | `str.strip()` |
| Format | `String.format("%s", val)` |
| Repeat | `str.repeat(3)` |
| Reverse | `new StringBuilder(str).reverse().toString()` |

---

## Common Mistakes

```java
// Wrong: using == for content comparison
if (s1 == s2)                 // Compares references!
if (s1.equals(s2))            // Correct: compares content

// Wrong: forgetting immutability
str.toUpperCase();            // Does nothing (result discarded)
str = str.toUpperCase();      // Correct: assign result

// Wrong: null reference
str.equals(null)              // NullPointerException if str is null
"literal".equals(str)         // Safe: handles null str

// Wrong: split special chars
str.split(".")                // Matches any char (regex)
str.split("\\.")              // Correct: escape dot

// Wrong: String concatenation in loop
for (...) { s += "x"; }       // Creates many objects
StringBuilder sb = ...;       // Use StringBuilder instead
```
