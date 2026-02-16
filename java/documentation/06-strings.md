# Strings

[<- Previous: Multi-Dimensional Arrays](05-multidimensional-arrays.md) | [Next: Methods ->](07-methods.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Strings Cheat Sheet](../cheatsheets/strings-cheatsheet.md)

---

## Overview

Strings are one of the most commonly used data types in Java. A String is a sequence of characters and is represented by the `java.lang.String` class. Understanding Strings thoroughly is essential for any Java developer.

**Key Characteristics:**
- Strings are **immutable** - once created, their content cannot be changed
- Strings are **objects** - they have methods and properties
- Strings are stored in a special memory area called the **String Pool**
- The String class is **final** - it cannot be extended

---

## Creating Strings

### String Literals

The most common way to create a String is using double quotes.

```java
String greeting = "Hello, World!";
String name = "Alice";
String empty = "";
```

When you create a String literal, Java checks the String Pool. If the same string already exists, it returns a reference to the existing string instead of creating a new one.

### Using the new Keyword

You can also create Strings using the `new` keyword, but this is generally not recommended.

```java
String str1 = new String("Hello");  // Creates new object on heap
String str2 = new String("Hello");  // Creates another new object

System.out.println(str1 == str2);        // false (different objects)
System.out.println(str1.equals(str2));   // true (same content)
```

### From Character Arrays

```java
char[] chars = {'H', 'e', 'l', 'l', 'o'};
String str = new String(chars);           // "Hello"
String partial = new String(chars, 1, 3); // "ell" (from index 1, length 3)
```

### From Byte Arrays

```java
byte[] bytes = {72, 101, 108, 108, 111};  // ASCII values
String str = new String(bytes);            // "Hello"

// With specific charset
String utf8 = new String(bytes, StandardCharsets.UTF_8);
```

### From StringBuilder/StringBuffer

```java
StringBuilder sb = new StringBuilder("Hello");
String str = sb.toString();
```

### Using copyValueOf()

Static method to create a String from a character array.

```java
char[] chars = {'H', 'e', 'l', 'l', 'o'};

// Same as new String(chars)
String str1 = String.copyValueOf(chars);  // "Hello"

// With offset and count
String str2 = String.copyValueOf(chars, 1, 3);  // "ell"
```

---

## String Pool (String Interning)

Java maintains a special memory region called the String Pool (or String Intern Pool) to store String literals. This optimizes memory usage.

```java
String s1 = "Hello";    // Created in String Pool
String s2 = "Hello";    // Returns reference to existing "Hello"
String s3 = new String("Hello");  // Created on heap, NOT in pool

System.out.println(s1 == s2);     // true (same reference)
System.out.println(s1 == s3);     // false (different objects)
System.out.println(s1.equals(s3)); // true (same content)
```

### The intern() Method

You can explicitly add a String to the pool using `intern()`.

```java
String s1 = new String("Hello");
String s2 = s1.intern();  // Adds to pool or returns existing
String s3 = "Hello";

System.out.println(s2 == s3);  // true (both reference pool)
```

---

## String Immutability

Strings in Java are immutable. Any operation that appears to modify a String actually creates a new String object.

```java
String original = "Hello";
String modified = original.concat(" World");

System.out.println(original);   // "Hello" (unchanged)
System.out.println(modified);   // "Hello World" (new String)
```

### Why Are Strings Immutable?

1. **Security** - Strings are used for passwords, network connections, and file paths
2. **Thread Safety** - Immutable objects are inherently thread-safe
3. **Caching** - String hashcodes can be cached for better performance
4. **String Pool** - Immutability allows safe sharing of String instances

---

## String Length and Empty Check

### length()

Returns the number of characters in the String.

```java
String str = "Hello";
int len = str.length();  // 5

String empty = "";
System.out.println(empty.length());  // 0

String unicode = "Hello 👋";
System.out.println(unicode.length());  // 8 (emoji counts as 2)
```

### isEmpty()

Returns `true` if the String has zero length.

```java
String empty = "";
String notEmpty = "Hello";

System.out.println(empty.isEmpty());     // true
System.out.println(notEmpty.isEmpty());  // false
```

### isBlank() (Java 11+)

Returns `true` if the String is empty or contains only whitespace.

```java
String empty = "";
String spaces = "   ";
String tabs = "\t\n";
String text = "Hello";

System.out.println(empty.isBlank());   // true
System.out.println(spaces.isBlank());  // true
System.out.println(tabs.isBlank());    // true
System.out.println(text.isBlank());    // false
```

---

## Accessing Characters

### charAt(int index)

Returns the character at the specified index (0-based).

```java
String str = "Hello";

char first = str.charAt(0);   // 'H'
char last = str.charAt(4);    // 'o'
char lastAlt = str.charAt(str.length() - 1);  // 'o'

// Throws StringIndexOutOfBoundsException if index is invalid
// str.charAt(10);  // Error!
```

### toCharArray()

Converts the String to a character array.

```java
String str = "Hello";
char[] chars = str.toCharArray();

for (char c : chars) {
    System.out.print(c + " ");  // H e l l o
}

// Modify the array (doesn't affect original String)
chars[0] = 'J';
System.out.println(new String(chars));  // "Jello"
System.out.println(str);                // "Hello" (unchanged)
```

### chars() (Java 9+)

Returns an IntStream of character values.

```java
String str = "Hello";

// Print each character
str.chars().forEach(c -> System.out.print((char) c + " "));
// Output: H e l l o

// Count vowels
long vowelCount = str.chars()
    .filter(c -> "aeiouAEIOU".indexOf(c) != -1)
    .count();
System.out.println(vowelCount);  // 2 (e, o)
```

### codePoints() (Java 9+)

Returns an IntStream of Unicode code points. Better for handling emojis and special characters.

```java
String str = "Hello 👋";

// chars() counts emoji as 2 characters
System.out.println(str.chars().count());       // 8

// codePoints() counts emoji as 1 code point
System.out.println(str.codePoints().count());  // 7
```

### codePointAt(int index)

Returns the Unicode code point at the specified index.

```java
String str = "Hello";
int codePoint = str.codePointAt(0);  // 72 (code point for 'H')
System.out.println((char) codePoint);  // 'H'

// For emoji
String emoji = "👋";
int emojiCodePoint = emoji.codePointAt(0);  // 128075
System.out.println(Integer.toHexString(emojiCodePoint));  // 1f44b
```

### codePointBefore(int index)

Returns the Unicode code point before the specified index.

```java
String str = "Hello";
int codePoint = str.codePointBefore(1);  // 72 (code point for 'H')
```

### codePointCount(int beginIndex, int endIndex)

Returns the number of Unicode code points in the specified range.

```java
String str = "Hello 👋 World";
int count = str.codePointCount(0, str.length());
System.out.println(count);  // 13 (emoji counts as 1)
```

### offsetByCodePoints(int index, int codePointOffset)

Returns the index offset by the given number of code points.

```java
String str = "Hello 👋 World";
int index = str.offsetByCodePoints(0, 7);  // Index after "Hello " and emoji
System.out.println(str.substring(index));  // " World"
```

---

## Comparing Strings

### equals(Object obj)

Compares content for equality (case-sensitive).

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = "hello";

System.out.println(s1.equals(s2));  // true
System.out.println(s1.equals(s3));  // false (case matters)
```

**Important:** Always use `equals()`, not `==`, to compare String content.

```java
String s1 = "Hello";
String s2 = new String("Hello");

System.out.println(s1 == s2);       // false (different objects)
System.out.println(s1.equals(s2));  // true (same content)
```

### equalsIgnoreCase(String str)

Compares content ignoring case differences.

```java
String s1 = "Hello";
String s2 = "HELLO";
String s3 = "hello";

System.out.println(s1.equalsIgnoreCase(s2));  // true
System.out.println(s1.equalsIgnoreCase(s3));  // true
```

### compareTo(String str)

Compares strings lexicographically. Returns:
- **Negative** if this string comes before the argument
- **Zero** if strings are equal
- **Positive** if this string comes after the argument

```java
String s1 = "apple";
String s2 = "banana";
String s3 = "apple";

System.out.println(s1.compareTo(s2));  // Negative (apple < banana)
System.out.println(s2.compareTo(s1));  // Positive (banana > apple)
System.out.println(s1.compareTo(s3));  // 0 (equal)

// Useful for sorting
String[] fruits = {"Banana", "Apple", "Cherry"};
Arrays.sort(fruits);  // Uses compareTo internally
System.out.println(Arrays.toString(fruits));  // [Apple, Banana, Cherry]
```

### compareToIgnoreCase(String str)

Compares lexicographically, ignoring case.

```java
String s1 = "Apple";
String s2 = "apple";
String s3 = "BANANA";

System.out.println(s1.compareTo(s2));           // Negative ('A' < 'a')
System.out.println(s1.compareToIgnoreCase(s2)); // 0 (equal ignoring case)
System.out.println(s1.compareToIgnoreCase(s3)); // Negative (a < b)
```

### contentEquals(CharSequence cs)

Compares String content with any CharSequence (String, StringBuilder, StringBuffer).

```java
String str = "Hello";
StringBuilder sb = new StringBuilder("Hello");
StringBuffer sbuf = new StringBuffer("Hello");

System.out.println(str.contentEquals(sb));    // true
System.out.println(str.contentEquals(sbuf));  // true
System.out.println(str.equals(sb));           // false (different types)
```

### regionMatches()

Compares a region of this string with a region of another string.

```java
String str1 = "Hello World";
String str2 = "Say Hello to everyone";

// regionMatches(thisOffset, other, otherOffset, length)
// Compare "Hello" in both strings
boolean match = str1.regionMatches(0, str2, 4, 5);  // true

// Case-insensitive version
// regionMatches(ignoreCase, thisOffset, other, otherOffset, length)
String str3 = "HELLO WORLD";
boolean matchIgnoreCase = str1.regionMatches(true, 0, str3, 0, 5);  // true
```

### hashCode()

Returns the hash code of the string. Important for HashMap and HashSet.

```java
String str = "Hello";
int hash = str.hashCode();
System.out.println(hash);  // 69609650

// Same content = same hash code
String str2 = "Hello";
System.out.println(str.hashCode() == str2.hashCode());  // true

// Hash code is cached (computed only once)
```

---

## Searching Within Strings

### indexOf(String str)

Returns the index of the first occurrence, or -1 if not found.

```java
String str = "Hello World";

System.out.println(str.indexOf("o"));      // 4 (first 'o')
System.out.println(str.indexOf("World"));  // 6
System.out.println(str.indexOf("xyz"));    // -1 (not found)
System.out.println(str.indexOf("o", 5));   // 7 (search from index 5)
```

### indexOf(int ch)

Search for a character by its Unicode value.

```java
String str = "Hello";

System.out.println(str.indexOf('e'));   // 1
System.out.println(str.indexOf('l'));   // 2 (first 'l')
System.out.println(str.indexOf('z'));   // -1
```

### lastIndexOf(String str)

Returns the index of the last occurrence, or -1 if not found.

```java
String str = "Hello World";

System.out.println(str.lastIndexOf("o"));      // 7 (last 'o')
System.out.println(str.lastIndexOf("l"));      // 9 (last 'l')
System.out.println(str.lastIndexOf("o", 6));   // 4 (search backward from index 6)
```

### contains(CharSequence seq)

Returns `true` if the String contains the specified sequence.

```java
String str = "Hello World";

System.out.println(str.contains("World"));  // true
System.out.println(str.contains("world"));  // false (case-sensitive)
System.out.println(str.contains(""));       // true (empty string)
```

### startsWith(String prefix)

Checks if the String starts with the specified prefix.

```java
String str = "Hello World";

System.out.println(str.startsWith("Hello"));  // true
System.out.println(str.startsWith("World"));  // false
System.out.println(str.startsWith("World", 6)); // true (from index 6)
```

### endsWith(String suffix)

Checks if the String ends with the specified suffix.

```java
String filename = "document.pdf";

System.out.println(filename.endsWith(".pdf"));   // true
System.out.println(filename.endsWith(".txt"));   // false

// Check file extension
if (filename.endsWith(".pdf") || filename.endsWith(".PDF")) {
    System.out.println("PDF file");
}
```

### matches(String regex)

Tests if the String matches a regular expression.

```java
String email = "test@example.com";
String phone = "123-456-7890";

// Simple email pattern
System.out.println(email.matches(".*@.*\\..*"));  // true

// Phone pattern
System.out.println(phone.matches("\\d{3}-\\d{3}-\\d{4}"));  // true

// Only digits
System.out.println("12345".matches("\\d+"));  // true
System.out.println("12a45".matches("\\d+"));  // false
```

---

## Extracting Substrings

### substring(int beginIndex)

Returns a substring from the specified index to the end.

```java
String str = "Hello World";

System.out.println(str.substring(0));   // "Hello World"
System.out.println(str.substring(6));   // "World"
System.out.println(str.substring(11));  // "" (empty string)
```

### substring(int beginIndex, int endIndex)

Returns a substring from beginIndex (inclusive) to endIndex (exclusive).

```java
String str = "Hello World";

System.out.println(str.substring(0, 5));   // "Hello"
System.out.println(str.substring(6, 11));  // "World"
System.out.println(str.substring(0, 1));   // "H"

// Extract middle portion
String middle = str.substring(3, 8);  // "lo Wo"
```

### subSequence(int beginIndex, int endIndex)

Similar to substring but returns a CharSequence.

```java
String str = "Hello World";
CharSequence cs = str.subSequence(0, 5);  // "Hello"
String sub = cs.toString();  // Convert to String if needed
```

---

## Modifying Strings (Creating New Strings)

Remember: All these methods return NEW strings. The original is unchanged.

### concat(String str)

Concatenates the specified string to the end.

```java
String s1 = "Hello";
String s2 = s1.concat(" World");

System.out.println(s1);  // "Hello" (unchanged)
System.out.println(s2);  // "Hello World"

// Equivalent to + operator
String s3 = s1 + " World";  // "Hello World"
```

### replace(char oldChar, char newChar)

Replaces all occurrences of a character.

```java
String str = "Hello";

System.out.println(str.replace('l', 'p'));  // "Heppo"
System.out.println(str.replace('H', 'J'));  // "Jello"
System.out.println(str.replace('z', 'x'));  // "Hello" (no change)
```

### replace(CharSequence target, CharSequence replacement)

Replaces all occurrences of a substring.

```java
String str = "Hello World World";

System.out.println(str.replace("World", "Java"));  // "Hello Java Java"
System.out.println(str.replace("o", "0"));         // "Hell0 W0rld W0rld"
```

### replaceAll(String regex, String replacement)

Replaces all matches of a regular expression.

```java
String str = "Hello 123 World 456";

// Remove all digits
System.out.println(str.replaceAll("\\d", ""));     // "Hello  World "
System.out.println(str.replaceAll("\\d+", "NUM")); // "Hello NUM World NUM"

// Remove extra spaces
String text = "Hello    World";
System.out.println(text.replaceAll("\\s+", " "));  // "Hello World"
```

### replaceFirst(String regex, String replacement)

Replaces only the first match of a regular expression.

```java
String str = "Hello World World";

System.out.println(str.replaceFirst("World", "Java"));  // "Hello Java World"
System.out.println(str.replaceFirst("\\d+", "NUM"));    // For "a1b2c3": "aNUMb2c3"
```

---

## Case Conversion

### toUpperCase()

Converts all characters to uppercase.

```java
String str = "Hello World";

System.out.println(str.toUpperCase());  // "HELLO WORLD"

// With specific locale
System.out.println(str.toUpperCase(Locale.ENGLISH));
```

### toLowerCase()

Converts all characters to lowercase.

```java
String str = "Hello World";

System.out.println(str.toLowerCase());  // "hello world"

// With specific locale
System.out.println(str.toLowerCase(Locale.ENGLISH));
```

### Capitalize First Letter

Java does not have a built-in capitalize method, but you can create one:

```java
public static String capitalize(String str) {
    if (str == null || str.isEmpty()) {
        return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
}

// Usage
System.out.println(capitalize("hello"));  // "Hello"
System.out.println(capitalize("WORLD"));  // "World"
```

### Title Case

```java
public static String toTitleCase(String str) {
    if (str == null || str.isEmpty()) {
        return str;
    }
    
    StringBuilder result = new StringBuilder();
    boolean capitalizeNext = true;
    
    for (char c : str.toCharArray()) {
        if (Character.isWhitespace(c)) {
            capitalizeNext = true;
            result.append(c);
        } else if (capitalizeNext) {
            result.append(Character.toUpperCase(c));
            capitalizeNext = false;
        } else {
            result.append(Character.toLowerCase(c));
        }
    }
    
    return result.toString();
}

// Usage
System.out.println(toTitleCase("hello world"));  // "Hello World"
```

---

## Trimming and Stripping

### trim()

Removes leading and trailing whitespace (ASCII spaces, tabs, newlines).

```java
String str = "   Hello World   ";

System.out.println(str.trim());         // "Hello World"
System.out.println(str.trim().length()); // 11

String tabs = "\t\nHello\t\n";
System.out.println(tabs.trim());  // "Hello"
```

### strip() (Java 11+)

Removes leading and trailing whitespace (Unicode-aware).

```java
String str = "   Hello World   ";
System.out.println(str.strip());  // "Hello World"

// strip() handles Unicode whitespace that trim() doesn't
String unicode = "\u2003Hello\u2003";  // em space
System.out.println(unicode.strip());   // "Hello"
System.out.println(unicode.trim());    // "\u2003Hello\u2003" (unchanged)
```

### stripLeading() (Java 11+)

Removes only leading whitespace.

```java
String str = "   Hello World   ";
System.out.println(str.stripLeading());  // "Hello World   "
```

### stripTrailing() (Java 11+)

Removes only trailing whitespace.

```java
String str = "   Hello World   ";
System.out.println(str.stripTrailing());  // "   Hello World"
```

### stripIndent() (Java 15+)

Removes incidental indentation from each line.

```java
String text = """
        Hello
        World
        """;
System.out.println(text.stripIndent());
// Removes common leading whitespace from all lines
```

---

## Splitting Strings

### split(String regex)

Splits the string around matches of a regular expression.

```java
String str = "apple,banana,cherry";
String[] fruits = str.split(",");

System.out.println(Arrays.toString(fruits));  // [apple, banana, cherry]
System.out.println(fruits.length);            // 3

// Split by multiple delimiters
String data = "a,b;c:d";
String[] parts = data.split("[,;:]");  // Regex character class
System.out.println(Arrays.toString(parts));  // [a, b, c, d]

// Split by whitespace
String sentence = "Hello   World  Java";
String[] words = sentence.split("\\s+");  // One or more spaces
System.out.println(Arrays.toString(words));  // [Hello, World, Java]
```

### split(String regex, int limit)

Splits with a limit on the number of resulting parts.

```java
String str = "a,b,c,d,e";

// Limit to 3 parts
String[] parts = str.split(",", 3);
System.out.println(Arrays.toString(parts));  // [a, b, c,d,e]

// Limit of 0 - trailing empty strings are discarded
String str2 = "a,b,c,,,";
System.out.println(Arrays.toString(str2.split(",", 0)));   // [a, b, c]
System.out.println(Arrays.toString(str2.split(",", -1)));  // [a, b, c, , , ]
```

### Splitting Special Characters

Some characters have special meaning in regex and need escaping.

```java
// Split by period (. is regex for any character)
String filename = "file.name.txt";
String[] wrong = filename.split(".");     // Empty array!
String[] right = filename.split("\\.");   // [file, name, txt]

// Split by pipe
String data = "a|b|c";
String[] parts = data.split("\\|");  // [a, b, c]

// Split by backslash
String path = "C:\\Users\\Name";
String[] folders = path.split("\\\\");  // [C:, Users, Name]
```

---

## Joining Strings

### String.join(delimiter, elements) (Java 8+)

Joins multiple strings with a delimiter.

```java
// Join array elements
String[] fruits = {"apple", "banana", "cherry"};
String result = String.join(", ", fruits);
System.out.println(result);  // "apple, banana, cherry"

// Join varargs
String joined = String.join("-", "2024", "01", "15");
System.out.println(joined);  // "2024-01-15"

// Join List
List<String> list = Arrays.asList("a", "b", "c");
String joinedList = String.join(";", list);
System.out.println(joinedList);  // "a;b;c"
```

### Using StringJoiner (Java 8+)

More flexible joining with prefix and suffix.

```java
StringJoiner sj = new StringJoiner(", ", "[", "]");
sj.add("apple");
sj.add("banana");
sj.add("cherry");

System.out.println(sj.toString());  // "[apple, banana, cherry]"

// Empty value handling
StringJoiner empty = new StringJoiner(", ");
empty.setEmptyValue("No items");
System.out.println(empty.toString());  // "No items"
```

### Using Collectors.joining() (Java 8+)

Join elements from a stream.

```java
List<String> fruits = Arrays.asList("apple", "banana", "cherry");

// Simple join
String result = fruits.stream().collect(Collectors.joining(", "));
System.out.println(result);  // "apple, banana, cherry"

// With prefix and suffix
String formatted = fruits.stream()
    .collect(Collectors.joining(", ", "[", "]"));
System.out.println(formatted);  // "[apple, banana, cherry]"
```

---

## String Concatenation

### Using the + Operator

The simplest way to concatenate strings.

```java
String greeting = "Hello" + " " + "World";  // "Hello World"

// Concatenating with other types (automatically converted to String)
String result = "Value: " + 42;        // "Value: 42"
String mixed = "Sum: " + 10 + 20;      // "Sum: 1020" (left-to-right)
String calc = "Sum: " + (10 + 20);     // "Sum: 30" (parentheses force addition first)
```

### How + Concatenation Works

The compiler optimizes string concatenation:

```java
// This code:
String s = "Hello" + " " + "World";

// Is optimized to:
String s = "Hello World";  // Compile-time concatenation

// For variables, compiler uses StringBuilder (Java 5-8) or invokedynamic (Java 9+):
String name = "Alice";
String greeting = "Hello, " + name + "!";

// Internally becomes something like:
String greeting = new StringBuilder()
    .append("Hello, ")
    .append(name)
    .append("!")
    .toString();
```

### Concatenation in Loops (Performance)

```java
// BAD - Creates many temporary String objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result = result + i;  // New String each iteration!
}

// GOOD - Use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

### Using concat() Method

```java
String s1 = "Hello";
String s2 = s1.concat(" World");  // "Hello World"

// Difference from + operator:
// concat() throws NullPointerException if argument is null
// + operator converts null to "null" string
String nullStr = null;
String result1 = "Hello " + nullStr;        // "Hello null"
// String result2 = "Hello ".concat(nullStr);  // NullPointerException!
```

---

## Character Class Utilities

The `Character` class provides useful methods for working with individual characters.

### Character Type Checks

```java
char c = 'A';

Character.isLetter(c)       // true
Character.isDigit(c)        // false
Character.isLetterOrDigit(c) // true
Character.isWhitespace(' ') // true
Character.isUpperCase(c)    // true
Character.isLowerCase(c)    // false
Character.isAlphabetic(c)   // true
```

### Character Case Conversion

```java
Character.toUpperCase('a')  // 'A'
Character.toLowerCase('A')  // 'a'
```

### Numeric Value

```java
Character.getNumericValue('5')   // 5
Character.getNumericValue('A')   // 10 (hex value)
Character.getNumericValue('Z')   // 35
Character.digit('F', 16)         // 15 (hex digit)
```

### Character to String

```java
char c = 'A';
String s1 = String.valueOf(c);           // "A"
String s2 = Character.toString(c);       // "A"
String s3 = "" + c;                      // "A"
```

### Practical Example: Count Character Types

```java
public static void analyzeString(String str) {
    int letters = 0, digits = 0, spaces = 0, others = 0;
    
    for (char c : str.toCharArray()) {
        if (Character.isLetter(c)) {
            letters++;
        } else if (Character.isDigit(c)) {
            digits++;
        } else if (Character.isWhitespace(c)) {
            spaces++;
        } else {
            others++;
        }
    }
    
    System.out.println("Letters: " + letters);
    System.out.println("Digits: " + digits);
    System.out.println("Spaces: " + spaces);
    System.out.println("Others: " + others);
}

// Usage
analyzeString("Hello World! 123");
// Letters: 10, Digits: 3, Spaces: 2, Others: 1
```

---

## String Formatting

### format(String format, Object... args)

Creates a formatted string similar to printf.

```java
String name = "Alice";
int age = 30;
double salary = 50000.50;

// Basic formatting
String formatted = String.format("Name: %s, Age: %d", name, age);
System.out.println(formatted);  // "Name: Alice, Age: 30"

// Number formatting
String money = String.format("Salary: $%.2f", salary);
System.out.println(money);  // "Salary: $50000.50"

// Padding
String padded = String.format("%10s", "Hi");      // "        Hi"
String leftPad = String.format("%-10s", "Hi");    // "Hi        "
String zeroPad = String.format("%05d", 42);       // "00042"
```

### Common Format Specifiers

| Specifier | Description | Example |
|-----------|-------------|---------|
| `%s` | String | `"Hello"` |
| `%d` | Integer | `42` |
| `%f` | Floating point | `3.14159` |
| `%.2f` | Float with 2 decimals | `3.14` |
| `%n` | Platform-specific newline | |
| `%b` | Boolean | `true` |
| `%c` | Character | `'A'` |
| `%x` | Hexadecimal | `2a` |
| `%X` | Hexadecimal uppercase | `2A` |
| `%o` | Octal | `52` |
| `%e` | Scientific notation | `1.23e+02` |
| `%%` | Literal percent | `%` |

### Width and Precision

```java
// Width - minimum characters
System.out.println(String.format("[%10s]", "Hi"));    // [        Hi]
System.out.println(String.format("[%-10s]", "Hi"));   // [Hi        ]

// Precision for floats
System.out.println(String.format("%.3f", 3.14159));   // 3.142
System.out.println(String.format("%10.2f", 3.14159)); // "      3.14"

// Precision for strings (max characters)
System.out.println(String.format("%.5s", "Hello World")); // "Hello"
```

### Argument Index

```java
// Reuse arguments with index
String result = String.format("%1$s has %2$d items. %1$s is happy.", "Alice", 5);
System.out.println(result);  // "Alice has 5 items. Alice is happy."

// Previous argument with <
String format = String.format("%s: %d, again: %<d", "Value", 42);
System.out.println(format);  // "Value: 42, again: 42"
```

### formatted() (Java 15+)

Instance method for formatting.

```java
String template = "Hello, %s! You are %d years old.";
String result = template.formatted("Alice", 30);
System.out.println(result);  // "Hello, Alice! You are 30 years old."
```

---

## Text Blocks (Java 15+)

Text blocks provide a cleaner way to write multi-line strings.

### Basic Syntax

```java
// Traditional multi-line string
String traditional = "Line 1\n" +
                    "Line 2\n" +
                    "Line 3";

// Text block (Java 15+)
String textBlock = """
    Line 1
    Line 2
    Line 3
    """;

System.out.println(textBlock);
// Line 1
// Line 2
// Line 3
```

### Indentation Control

The closing `"""` position determines indentation.

```java
// No indentation (""" at start of content)
String noIndent = """
Left aligned
    Still left aligned
""";

// With indentation
String indented = """
    First line
    Second line
    """;
```

### Escape Sequences in Text Blocks

```java
// Line continuation with \
String longLine = """
    This is a very long line that \
    continues on the next line without a newline""";
// Result: "This is a very long line that continues on the next line without a newline"

// Preserve trailing spaces with \s
String preserved = """
    Trailing spaces here   \s
    More text""";
```

### Practical Examples

```java
// JSON
String json = """
    {
        "name": "Alice",
        "age": 30,
        "city": "New York"
    }
    """;

// SQL
String sql = """
    SELECT id, name, email
    FROM users
    WHERE active = true
    ORDER BY name
    """;

// HTML
String html = """
    <html>
        <body>
            <h1>Welcome</h1>
            <p>Hello, World!</p>
        </body>
    </html>
    """;
```

---

## Repeating Strings

### repeat(int count) (Java 11+)

Repeats the string a specified number of times.

```java
String str = "ab";

System.out.println(str.repeat(3));  // "ababab"
System.out.println(str.repeat(0));  // ""
System.out.println(str.repeat(1));  // "ab"

// Create separator line
String line = "-".repeat(50);
System.out.println(line);  // Prints 50 dashes

// Create padding
String padding = " ".repeat(10);
```

---

## String Transformation

### transform() (Java 12+)

Applies a function to the string and returns the result.

```java
String result = "hello"
    .transform(s -> s.toUpperCase())
    .transform(s -> s + "!")
    .transform(s -> s.replace("!", "!!!"));

System.out.println(result);  // "HELLO!!!"

// Useful for method chaining
int length = "  hello  "
    .transform(String::strip)
    .transform(String::length);
System.out.println(length);  // 5
```

### translateEscapes() (Java 15+)

Processes escape sequences in a string.

```java
String escaped = "Hello\\nWorld\\t!";
String translated = escaped.translateEscapes();
System.out.println(translated);
// Hello
// World	!
```

---

## Lines and Indentation

### lines() (Java 11+)

Returns a stream of lines.

```java
String multiline = "Line 1\nLine 2\nLine 3";

multiline.lines().forEach(System.out::println);
// Line 1
// Line 2
// Line 3

// Count lines
long lineCount = multiline.lines().count();
System.out.println(lineCount);  // 3

// Process lines
List<String> upperLines = multiline.lines()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### indent(int n) (Java 12+)

Adjusts indentation of each line.

```java
String text = "Line 1\nLine 2\nLine 3";

// Add 4 spaces
String indented = text.indent(4);
System.out.println(indented);
//     Line 1
//     Line 2
//     Line 3

// Remove indentation (negative value)
String dedented = indented.indent(-2);
```

---

## Converting to/from Other Types

### valueOf() Methods

Convert other types to String.

```java
// Primitives to String
String s1 = String.valueOf(42);        // "42"
String s2 = String.valueOf(3.14);      // "3.14"
String s3 = String.valueOf(true);      // "true"
String s4 = String.valueOf('A');       // "A"

// Array to String
char[] chars = {'H', 'i'};
String s5 = String.valueOf(chars);     // "Hi"

// Object to String
Object obj = new Integer(100);
String s6 = String.valueOf(obj);       // "100"

// Null safety (returns "null")
String s7 = String.valueOf(null);      // "null"
```

### Parsing Strings to Other Types

```java
// String to int
int num = Integer.parseInt("42");

// String to double
double d = Double.parseDouble("3.14");

// String to boolean
boolean b = Boolean.parseBoolean("true");

// String to long
long l = Long.parseLong("1234567890");

// With radix (base)
int binary = Integer.parseInt("1010", 2);   // 10
int hex = Integer.parseInt("FF", 16);       // 255
```

### getBytes()

Convert String to byte array.

```java
String str = "Hello";

// Default charset
byte[] bytes1 = str.getBytes();

// Specific charset
byte[] bytes2 = str.getBytes(StandardCharsets.UTF_8);
byte[] bytes3 = str.getBytes(StandardCharsets.ISO_8859_1);

// Print byte values
System.out.println(Arrays.toString(bytes1));  // [72, 101, 108, 108, 111]
```

---

## StringBuilder

StringBuilder is a mutable sequence of characters. Use it when you need to modify strings frequently.

### Creating StringBuilder

```java
// Empty (default capacity 16)
StringBuilder sb1 = new StringBuilder();

// With initial capacity
StringBuilder sb2 = new StringBuilder(100);

// From String
StringBuilder sb3 = new StringBuilder("Hello");
```

### Common Methods

```java
StringBuilder sb = new StringBuilder("Hello");

// Append
sb.append(" World");
sb.append(42);
sb.append('!');
System.out.println(sb);  // "Hello World42!"

// Insert
sb.insert(5, " Java");   // Insert at index 5
System.out.println(sb);  // "Hello Java World42!"

// Delete
sb.delete(5, 10);        // Delete from index 5 to 10 (exclusive)
System.out.println(sb);  // "Hello World42!"

// Delete character
sb.deleteCharAt(sb.length() - 1);  // Remove last char
System.out.println(sb);  // "Hello World42"

// Replace
sb.replace(6, 11, "Java");
System.out.println(sb);  // "Hello Java42"

// Reverse
sb.reverse();
System.out.println(sb);  // "24avaJ olleH"

// Clear
sb.setLength(0);         // Empty the StringBuilder
```

### Chaining

StringBuilder methods return the same object, allowing chaining.

```java
String result = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World")
    .append("!")
    .toString();

System.out.println(result);  // "Hello World!"
```

### Capacity Management

```java
StringBuilder sb = new StringBuilder();

System.out.println(sb.capacity());  // 16 (default)

sb.append("Hello World, this is a long string");
System.out.println(sb.capacity());  // Automatically increased

// Ensure capacity
sb.ensureCapacity(100);

// Trim to size
sb.trimToSize();
```

### When to Use StringBuilder

```java
// BAD - Creates many intermediate String objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + ",";  // Creates new String each iteration!
}

// GOOD - Uses mutable StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(",");
}
String result = sb.toString();
```

---

## StringBuffer

StringBuffer is similar to StringBuilder but is thread-safe (synchronized). Use it when multiple threads need to modify the same string.

```java
StringBuffer sb = new StringBuffer("Hello");

// Same methods as StringBuilder
sb.append(" World");
sb.insert(0, "Say: ");
sb.reverse();

System.out.println(sb);
```

### StringBuilder vs StringBuffer

| Feature | StringBuilder | StringBuffer |
|---------|---------------|--------------|
| Thread-safe | No | Yes |
| Performance | Faster | Slower (due to synchronization) |
| Use case | Single-threaded | Multi-threaded |

**Recommendation:** Use StringBuilder unless you need thread safety.

---

## Regular Expressions with Strings

### Common Regex Patterns

| Pattern | Description | Example Matches |
|---------|-------------|-----------------|
| `\\d` | Digit | 0-9 |
| `\\D` | Non-digit | a, @, space |
| `\\w` | Word character | a-z, A-Z, 0-9, _ |
| `\\W` | Non-word character | @, #, space |
| `\\s` | Whitespace | space, tab, newline |
| `\\S` | Non-whitespace | a, 1, @ |
| `.` | Any character | anything |
| `*` | 0 or more | |
| `+` | 1 or more | |
| `?` | 0 or 1 | |
| `{n}` | Exactly n | |
| `{n,m}` | Between n and m | |
| `^` | Start of string | |
| `$` | End of string | |
| `[abc]` | Any of a, b, c | |
| `[^abc]` | Not a, b, or c | |

### Using Pattern and Matcher

```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;

String text = "The price is $19.99 and $29.99";
Pattern pattern = Pattern.compile("\\$\\d+\\.\\d{2}");
Matcher matcher = pattern.matcher(text);

// Find all matches
while (matcher.find()) {
    System.out.println(matcher.group());  // $19.99, $29.99
}

// Check if entire string matches
boolean fullMatch = Pattern.matches("\\d+", "12345");  // true

// Compile once, use many times
Pattern emailPattern = Pattern.compile("[\\w.]+@[\\w.]+\\.[a-z]{2,}");
boolean valid = emailPattern.matcher("test@example.com").matches();  // true
```

### Extracting Groups

```java
String text = "John Smith, age 30";
Pattern pattern = Pattern.compile("(\\w+) (\\w+), age (\\d+)");
Matcher matcher = pattern.matcher(text);

if (matcher.find()) {
    System.out.println(matcher.group(0));  // "John Smith, age 30" (full match)
    System.out.println(matcher.group(1));  // "John"
    System.out.println(matcher.group(2));  // "Smith"
    System.out.println(matcher.group(3));  // "30"
}
```

---

## Common String Operations

### Palindrome Check

```java
public static boolean isPalindrome(String str) {
    String clean = str.toLowerCase().replaceAll("[^a-z0-9]", "");
    String reversed = new StringBuilder(clean).reverse().toString();
    return clean.equals(reversed);
}

// Usage
System.out.println(isPalindrome("A man a plan a canal Panama"));  // true
System.out.println(isPalindrome("racecar"));  // true
System.out.println(isPalindrome("hello"));    // false
```

### Count Word Occurrences

```java
public static int countOccurrences(String text, String word) {
    int count = 0;
    int index = 0;
    while ((index = text.indexOf(word, index)) != -1) {
        count++;
        index += word.length();
    }
    return count;
}

// Or using split
public static int countOccurrences2(String text, String word) {
    return text.split(Pattern.quote(word), -1).length - 1;
}

// Usage
String text = "The cat sat on the cat mat with a cat";
System.out.println(countOccurrences(text, "cat"));  // 3
```

### Reverse Words

```java
public static String reverseWords(String str) {
    String[] words = str.trim().split("\\s+");
    StringBuilder result = new StringBuilder();
    
    for (int i = words.length - 1; i >= 0; i--) {
        result.append(words[i]);
        if (i > 0) {
            result.append(" ");
        }
    }
    
    return result.toString();
}

// Usage
System.out.println(reverseWords("Hello World Java"));  // "Java World Hello"
```

### Anagram Check

```java
public static boolean areAnagrams(String s1, String s2) {
    // Remove spaces and convert to lowercase
    char[] arr1 = s1.replaceAll("\\s", "").toLowerCase().toCharArray();
    char[] arr2 = s2.replaceAll("\\s", "").toLowerCase().toCharArray();
    
    Arrays.sort(arr1);
    Arrays.sort(arr2);
    
    return Arrays.equals(arr1, arr2);
}

// Usage
System.out.println(areAnagrams("listen", "silent"));  // true
System.out.println(areAnagrams("hello", "world"));    // false
```

### Remove Duplicate Characters

```java
public static String removeDuplicates(String str) {
    StringBuilder result = new StringBuilder();
    Set<Character> seen = new LinkedHashSet<>();
    
    for (char c : str.toCharArray()) {
        if (seen.add(c)) {
            result.append(c);
        }
    }
    
    return result.toString();
}

// Usage
System.out.println(removeDuplicates("aabbccdd"));  // "abcd"
System.out.println(removeDuplicates("hello"));     // "helo"
```

### Count Vowels and Consonants

```java
public static int[] countVowelsConsonants(String str) {
    int vowels = 0, consonants = 0;
    String vowelSet = "aeiouAEIOU";
    
    for (char c : str.toCharArray()) {
        if (Character.isLetter(c)) {
            if (vowelSet.indexOf(c) != -1) {
                vowels++;
            } else {
                consonants++;
            }
        }
    }
    
    return new int[] {vowels, consonants};
}

// Usage
int[] result = countVowelsConsonants("Hello World");
System.out.println("Vowels: " + result[0]);      // 3
System.out.println("Consonants: " + result[1]);  // 7
```

### Find Longest Word

```java
public static String findLongestWord(String sentence) {
    String[] words = sentence.split("\\s+");
    String longest = "";
    
    for (String word : words) {
        // Remove punctuation
        String clean = word.replaceAll("[^a-zA-Z]", "");
        if (clean.length() > longest.length()) {
            longest = clean;
        }
    }
    
    return longest;
}

// Usage
System.out.println(findLongestWord("The quick brown fox jumps over the lazy dog"));
// Output: "jumps" or "quick" or "brown" (5 letters)
```

### Compress String (Run-Length Encoding)

```java
public static String compress(String str) {
    StringBuilder result = new StringBuilder();
    int count = 1;
    
    for (int i = 0; i < str.length(); i++) {
        if (i + 1 < str.length() && str.charAt(i) == str.charAt(i + 1)) {
            count++;
        } else {
            result.append(str.charAt(i));
            if (count > 1) {
                result.append(count);
            }
            count = 1;
        }
    }
    
    // Return compressed only if shorter
    String compressed = result.toString();
    return compressed.length() < str.length() ? compressed : str;
}

// Usage
System.out.println(compress("aabbbcccc"));  // "a2b3c4"
System.out.println(compress("abc"));        // "abc" (not shorter)
```

### Email Validation

```java
public static boolean isValidEmail(String email) {
    if (email == null || email.isBlank()) {
        return false;
    }
    
    // Simple pattern check
    String pattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    return email.matches(pattern);
}

// More robust validation
public static boolean isValidEmailRobust(String email) {
    if (email == null || email.isBlank()) {
        return false;
    }
    
    // Check for exactly one @
    int atIndex = email.indexOf('@');
    if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
        return false;
    }
    
    // Check local part (before @)
    String local = email.substring(0, atIndex);
    if (local.isEmpty() || local.startsWith(".") || local.endsWith(".")) {
        return false;
    }
    
    // Check domain part (after @)
    String domain = email.substring(atIndex + 1);
    if (domain.isEmpty() || !domain.contains(".")) {
        return false;
    }
    
    return true;
}

// Usage
System.out.println(isValidEmail("test@example.com"));     // true
System.out.println(isValidEmail("invalid.email"));        // false
System.out.println(isValidEmail("test@test@example.com")); // false
```

### URL Parsing

```java
public static Map<String, String> parseQueryString(String url) {
    Map<String, String> params = new HashMap<>();
    
    int queryStart = url.indexOf('?');
    if (queryStart == -1) {
        return params;  // No query string
    }
    
    String query = url.substring(queryStart + 1);
    String[] pairs = query.split("&");
    
    for (String pair : pairs) {
        int eqIndex = pair.indexOf('=');
        if (eqIndex > 0) {
            String key = pair.substring(0, eqIndex);
            String value = eqIndex < pair.length() - 1 
                ? pair.substring(eqIndex + 1) 
                : "";
            params.put(key, value);
        }
    }
    
    return params;
}

// Usage
String url = "https://example.com/page?name=Alice&age=30&city=NYC";
Map<String, String> params = parseQueryString(url);
System.out.println(params);  // {name=Alice, age=30, city=NYC}
```

### CSV Line Parsing

```java
public static String[] parseCSVLine(String line) {
    List<String> fields = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inQuotes = false;
    
    for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        
        if (c == '"') {
            inQuotes = !inQuotes;
        } else if (c == ',' && !inQuotes) {
            fields.add(current.toString().trim());
            current = new StringBuilder();
        } else {
            current.append(c);
        }
    }
    fields.add(current.toString().trim());
    
    return fields.toArray(new String[0]);
}

// Usage
String csvLine = "Alice,30,\"New York, NY\",Engineer";
String[] fields = parseCSVLine(csvLine);
System.out.println(Arrays.toString(fields));
// [Alice, 30, New York, NY, Engineer]
```

### Slug Generation (URL-friendly strings)

```java
public static String toSlug(String input) {
    if (input == null || input.isBlank()) {
        return "";
    }
    
    return input
        .toLowerCase()                          // lowercase
        .trim()                                 // remove leading/trailing spaces
        .replaceAll("[^a-z0-9\\s-]", "")       // remove special chars
        .replaceAll("\\s+", "-")               // spaces to hyphens
        .replaceAll("-+", "-")                 // collapse multiple hyphens
        .replaceAll("^-|-$", "");              // remove leading/trailing hyphens
}

// Usage
System.out.println(toSlug("Hello World!"));           // "hello-world"
System.out.println(toSlug("  Java Programming  "));   // "java-programming"
System.out.println(toSlug("100% Pure Java!!!"));      // "100-pure-java"
```

### Truncate with Ellipsis

```java
public static String truncate(String str, int maxLength) {
    if (str == null || str.length() <= maxLength) {
        return str;
    }
    if (maxLength <= 3) {
        return str.substring(0, maxLength);
    }
    return str.substring(0, maxLength - 3) + "...";
}

// Word-aware truncation
public static String truncateWords(String str, int maxLength) {
    if (str == null || str.length() <= maxLength) {
        return str;
    }
    
    String truncated = str.substring(0, maxLength - 3);
    int lastSpace = truncated.lastIndexOf(' ');
    
    if (lastSpace > 0) {
        truncated = truncated.substring(0, lastSpace);
    }
    
    return truncated + "...";
}

// Usage
System.out.println(truncate("Hello World", 8));           // "Hello..."
System.out.println(truncateWords("Hello World Java", 12)); // "Hello..."
```

### Mask Sensitive Data

```java
public static String maskEmail(String email) {
    int atIndex = email.indexOf('@');
    if (atIndex <= 2) {
        return email;  // Too short to mask
    }
    
    String local = email.substring(0, atIndex);
    String domain = email.substring(atIndex);
    
    String masked = local.charAt(0) 
        + "*".repeat(local.length() - 2) 
        + local.charAt(local.length() - 1);
    
    return masked + domain;
}

public static String maskCreditCard(String number) {
    String digits = number.replaceAll("[^0-9]", "");
    if (digits.length() < 4) {
        return number;
    }
    
    String lastFour = digits.substring(digits.length() - 4);
    return "**** **** **** " + lastFour;
}

// Usage
System.out.println(maskEmail("john.doe@example.com"));  // "j******e@example.com"
System.out.println(maskCreditCard("1234-5678-9012-3456")); // "**** **** **** 3456"
```

---

## Performance Tips

### String Concatenation

```java
// 1. For a few strings, + is fine
String result = "Hello" + " " + "World";  // OK

// 2. In loops, use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}

// 3. For joining collections, use String.join
String joined = String.join(",", list);
```

### String Comparison

```java
// Use equals(), not ==
if (str1.equals(str2)) { }

// For null-safe comparison, put literal first
if ("expected".equals(str)) { }  // Won't throw if str is null

// Or use Objects.equals()
if (Objects.equals(str1, str2)) { }  // Null-safe
```

### Avoid Creating Unnecessary Strings

```java
// Bad - creates 3 String objects
String s = new String("Hello");

// Good - uses String pool
String s = "Hello";

// Bad - creates intermediate String
String upper = str.toLowerCase().toUpperCase();

// Consider if you really need both transformations
```

---

## Summary

### String Information Methods

| Method | Description |
|--------|-------------|
| `length()` | Number of characters |
| `isEmpty()` | True if length is 0 |
| `isBlank()` | True if empty or only whitespace (Java 11+) |
| `hashCode()` | Returns hash code for HashMap/HashSet |

### Character Access Methods

| Method | Description |
|--------|-------------|
| `charAt(i)` | Character at index i |
| `toCharArray()` | Convert to char array |
| `chars()` | IntStream of character values (Java 9+) |
| `codePoints()` | IntStream of Unicode code points (Java 9+) |
| `codePointAt(i)` | Unicode code point at index |
| `codePointCount(begin, end)` | Number of code points in range |

### Comparison Methods

| Method | Description |
|--------|-------------|
| `equals(str)` | Content equality (case-sensitive) |
| `equalsIgnoreCase(str)` | Content equality (case-insensitive) |
| `compareTo(str)` | Lexicographic comparison |
| `compareToIgnoreCase(str)` | Lexicographic, ignore case |
| `contentEquals(cs)` | Compare with CharSequence |
| `regionMatches(...)` | Compare string regions |

### Search Methods

| Method | Description |
|--------|-------------|
| `indexOf(str)` | First occurrence index |
| `lastIndexOf(str)` | Last occurrence index |
| `contains(str)` | True if contains substring |
| `startsWith(prefix)` | True if starts with prefix |
| `endsWith(suffix)` | True if ends with suffix |
| `matches(regex)` | True if matches regex pattern |

### Extraction Methods

| Method | Description |
|--------|-------------|
| `substring(begin)` | Substring from begin to end |
| `substring(begin, end)` | Substring from begin to end (exclusive) |
| `subSequence(begin, end)` | Returns CharSequence |

### Modification Methods (return new String)

| Method | Description |
|--------|-------------|
| `concat(str)` | Concatenate strings |
| `replace(old, new)` | Replace all occurrences |
| `replaceAll(regex, new)` | Replace regex matches |
| `replaceFirst(regex, new)` | Replace first regex match |
| `toUpperCase()` | Convert to uppercase |
| `toLowerCase()` | Convert to lowercase |
| `trim()` | Remove leading/trailing whitespace |
| `strip()` | Remove whitespace (Unicode-aware, Java 11+) |
| `stripLeading()` | Remove leading whitespace (Java 11+) |
| `stripTrailing()` | Remove trailing whitespace (Java 11+) |
| `repeat(n)` | Repeat n times (Java 11+) |
| `indent(n)` | Adjust indentation (Java 12+) |

### Splitting and Joining

| Method | Description |
|--------|-------------|
| `split(regex)` | Split into array |
| `split(regex, limit)` | Split with limit |
| `String.join(delim, elements)` | Join with delimiter (Java 8+) |

### Formatting

| Method | Description |
|--------|-------------|
| `format(format, args)` | Formatted string (static) |
| `formatted(args)` | Formatted string (instance, Java 15+) |

### Conversion Methods

| Method | Description |
|--------|-------------|
| `valueOf(x)` | Convert any type to String (static) |
| `copyValueOf(chars)` | Create String from char array (static) |
| `getBytes()` | Convert to byte array |
| `intern()` | Add to String pool |

### Stream and Lines (Java 11+)

| Method | Description |
|--------|-------------|
| `lines()` | Stream of lines |
| `transform(fn)` | Apply function (Java 12+) |
| `translateEscapes()` | Process escape sequences (Java 15+) |

**Key Points:**
- Strings are immutable - methods return new String objects
- Use `equals()` for content comparison, not `==`
- Use StringBuilder for frequent modifications
- String Pool optimizes memory for string literals
- Text blocks (Java 15+) simplify multi-line strings
- Use `isBlank()` instead of `isEmpty()` when whitespace should be considered empty
- For Unicode/emoji handling, use `codePoints()` instead of `chars()`
- In loops, use StringBuilder instead of + concatenation

---

[<- Previous: Multi-Dimensional Arrays](05-multidimensional-arrays.md) | [Next: Methods ->](07-methods.md) | [Back to Guide](../guide.md)
