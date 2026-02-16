# 04. Arrays

[<- Back: Control Flow](./03-control-flow.md) | [Back to Guide](../guide.md) | [Next: Multi-Dimensional Arrays ->](./05-multidimensional-arrays.md)

**Quick Reference:** [Arrays Cheatsheet](../cheatsheets/arrays-cheatsheet.md)

---

## Overview

An array is a container that holds a fixed number of values of a single type. Arrays in Java have the following characteristics:

- **Fixed size** - Once created, the size cannot change
- **Zero-indexed** - First element is at index 0
- **Homogeneous** - All elements must be of the same type
- **Contiguous memory** - Elements are stored in consecutive memory locations

Arrays are one of the most fundamental data structures in Java, providing fast access to elements by index.

---

## Declaring Arrays

There are two valid syntaxes for declaring arrays. The first style is preferred in Java.

```java
// Preferred Java style: brackets after type
int[] numbers;
String[] names;
double[] prices;

// C-style: brackets after variable name (valid but less common in Java)
int numbers[];
String names[];

// Multiple declarations
int[] a, b, c;      // All are int arrays
int d[], e;         // d is an array, e is just an int (confusing - avoid)
```

---

## Creating Arrays

### Using the new Keyword

```java
// Syntax: type[] name = new type[size];
int[] numbers = new int[5];     // Array of 5 integers
String[] names = new String[3]; // Array of 3 Strings

// Elements are initialized to default values
// int, long, short, byte: 0
// float, double: 0.0
// boolean: false
// char: '\u0000'
// Objects (including String): null

System.out.println(numbers[0]); // 0
System.out.println(names[0]);   // null
```

### Using Array Initializer

```java
// Declare and initialize in one line
int[] numbers = {1, 2, 3, 4, 5};
String[] fruits = {"Apple", "Banana", "Cherry"};
double[] prices = {19.99, 29.99, 39.99};
boolean[] flags = {true, false, true};

// Size is automatically determined from the values
System.out.println(numbers.length);  // 5
System.out.println(fruits.length);   // 3
```

### Anonymous Array

Create an array without assigning it to a variable (useful for method arguments).

```java
// Anonymous array
printArray(new int[] {1, 2, 3, 4, 5});

// Cannot use shorthand syntax for anonymous arrays
// printArray({1, 2, 3});  // Error!
```

### Separate Declaration and Initialization

```java
int[] numbers;              // Declaration
numbers = new int[5];       // Initialization

// Or with values
int[] values;
values = new int[] {1, 2, 3};  // Must use new keyword here

// Cannot use shorthand after declaration
// values = {1, 2, 3};      // Error!
```

---

## Accessing Elements

Array elements are accessed using their index (0-based).

```java
int[] numbers = {10, 20, 30, 40, 50};

// Reading elements
int first = numbers[0];     // 10
int third = numbers[2];     // 30
int last = numbers[numbers.length - 1];  // 50

// Writing elements
numbers[0] = 100;           // Change first element to 100
numbers[2] = 300;           // Change third element to 300

// Array is now {100, 20, 300, 40, 50}
```

### Array Length

The `length` property returns the number of elements. Note: it's a property, not a method (no parentheses).

```java
int[] arr = {1, 2, 3, 4, 5};
int size = arr.length;      // 5

// Common pattern: last element
int lastElement = arr[arr.length - 1];  // 5

// Common pattern: iterate all elements
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}
```

### ArrayIndexOutOfBoundsException

Accessing an invalid index throws an exception.

```java
int[] arr = {1, 2, 3};

// Valid indices: 0, 1, 2
System.out.println(arr[0]);   // OK: 1
System.out.println(arr[2]);   // OK: 3

// Invalid indices
System.out.println(arr[3]);   // ArrayIndexOutOfBoundsException
System.out.println(arr[-1]);  // ArrayIndexOutOfBoundsException

// Safe access pattern
int index = 5;
if (index >= 0 && index < arr.length) {
    System.out.println(arr[index]);
} else {
    System.out.println("Index out of bounds");
}
```

---

## Iterating Over Arrays

### For Loop

```java
int[] numbers = {10, 20, 30, 40, 50};

// Standard for loop
for (int i = 0; i < numbers.length; i++) {
    System.out.println("Index " + i + ": " + numbers[i]);
}

// Reverse iteration
for (int i = numbers.length - 1; i >= 0; i--) {
    System.out.println(numbers[i]);
}

// Every other element
for (int i = 0; i < numbers.length; i += 2) {
    System.out.println(numbers[i]);  // 10, 30, 50
}
```

### Enhanced For Loop (For-Each)

```java
int[] numbers = {10, 20, 30, 40, 50};

// Cleaner syntax when you don't need the index
for (int num : numbers) {
    System.out.println(num);
}

// Works with any array type
String[] names = {"Alice", "Bob", "Charlie"};
for (String name : names) {
    System.out.println(name.toUpperCase());
}
```

**Limitation:** Cannot modify array elements with for-each.

```java
int[] numbers = {1, 2, 3};

// This doesn't modify the array!
for (int num : numbers) {
    num = num * 2;  // Modifies local copy only
}
// Array is still {1, 2, 3}

// Use traditional for loop to modify
for (int i = 0; i < numbers.length; i++) {
    numbers[i] = numbers[i] * 2;
}
// Array is now {2, 4, 6}
```

### While Loop

```java
int[] numbers = {10, 20, 30, 40, 50};
int i = 0;

while (i < numbers.length) {
    System.out.println(numbers[i]);
    i++;
}
```

---

## The Arrays Utility Class

The `java.util.Arrays` class provides many useful static methods for array operations.

```java
import java.util.Arrays;
```

### Arrays.toString() - Print Array Contents

```java
int[] numbers = {5, 2, 8, 1, 9};

// Without Arrays.toString() - prints memory reference
System.out.println(numbers);           // [I@1b6d3586

// With Arrays.toString() - prints contents
System.out.println(Arrays.toString(numbers));  // [5, 2, 8, 1, 9]

// Works with all types
String[] names = {"Alice", "Bob"};
System.out.println(Arrays.toString(names));    // [Alice, Bob]

double[] prices = {19.99, 29.99};
System.out.println(Arrays.toString(prices));   // [19.99, 29.99]

boolean[] flags = {true, false};
System.out.println(Arrays.toString(flags));    // [true, false]
```

### Arrays.sort() - Sort Array

Sorts the array in ascending order (in-place, modifies the original array).

```java
// Sort integers
int[] numbers = {5, 2, 8, 1, 9, 3};
Arrays.sort(numbers);
System.out.println(Arrays.toString(numbers));  // [1, 2, 3, 5, 8, 9]

// Sort strings (alphabetically)
String[] names = {"Charlie", "Alice", "Bob"};
Arrays.sort(names);
System.out.println(Arrays.toString(names));    // [Alice, Bob, Charlie]

// Sort doubles
double[] prices = {29.99, 9.99, 19.99};
Arrays.sort(prices);
System.out.println(Arrays.toString(prices));   // [9.99, 19.99, 29.99]
```

### Arrays.sort() - Sort a Range

```java
int[] numbers = {5, 2, 8, 1, 9, 3};

// Sort only indices 1 to 4 (exclusive)
Arrays.sort(numbers, 1, 4);
System.out.println(Arrays.toString(numbers));  // [5, 1, 2, 8, 9, 3]
// Only elements at indices 1, 2, 3 were sorted
```

### Arrays.sort() - Custom Comparator

For descending order or custom sorting, use a Comparator (requires wrapper types).

```java
// Descending order (requires Integer[], not int[])
Integer[] numbers = {5, 2, 8, 1, 9, 3};
Arrays.sort(numbers, Collections.reverseOrder());
System.out.println(Arrays.toString(numbers));  // [9, 8, 5, 3, 2, 1]

// Custom comparator
String[] names = {"Alice", "Bob", "Charlie", "Dan"};
Arrays.sort(names, (a, b) -> a.length() - b.length());  // By length
System.out.println(Arrays.toString(names));  // [Bob, Dan, Alice, Charlie]

// Case-insensitive sort
String[] mixed = {"apple", "Banana", "cherry", "Date"};
Arrays.sort(mixed, String.CASE_INSENSITIVE_ORDER);
System.out.println(Arrays.toString(mixed));  // [apple, Banana, cherry, Date]
```

### Arrays.parallelSort() - Parallel Sorting

For large arrays, parallelSort uses multiple threads for better performance.

```java
int[] largeArray = new int[1000000];
// ... fill array ...

Arrays.parallelSort(largeArray);  // Uses multiple CPU cores

// Generally faster for arrays with more than ~4,700 elements
```

### Arrays.binarySearch() - Find Element

Searches for an element in a **sorted** array. Returns index if found, or negative value if not found.

```java
int[] numbers = {1, 3, 5, 7, 9, 11};  // Must be sorted!

// Find existing element
int index = Arrays.binarySearch(numbers, 7);
System.out.println(index);  // 3

// Element not found - returns negative insertion point
int notFound = Arrays.binarySearch(numbers, 6);
System.out.println(notFound);  // -4 (would be inserted at index 3)
// Formula: -(insertion point) - 1

// Search in range
int rangeSearch = Arrays.binarySearch(numbers, 0, 3, 5);
System.out.println(rangeSearch);  // 2

// With strings
String[] names = {"Alice", "Bob", "Charlie", "David"};  // Sorted
int strIndex = Arrays.binarySearch(names, "Charlie");
System.out.println(strIndex);  // 2
```

**Important:** Array must be sorted before using binarySearch. Behavior is undefined for unsorted arrays.

### Arrays.equals() - Compare Arrays

Compares two arrays for equality (same length and all elements equal).

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = {1, 2, 4};
int[] d = {1, 2, 3, 4};

// Using == compares references (wrong!)
System.out.println(a == b);              // false

// Using Arrays.equals() compares content
System.out.println(Arrays.equals(a, b)); // true
System.out.println(Arrays.equals(a, c)); // false (different values)
System.out.println(Arrays.equals(a, d)); // false (different length)

// Works with all types
String[] s1 = {"a", "b"};
String[] s2 = {"a", "b"};
System.out.println(Arrays.equals(s1, s2));  // true

// Compare ranges
int[] x = {1, 2, 3, 4, 5};
int[] y = {0, 2, 3, 4, 0};
System.out.println(Arrays.equals(x, 1, 4, y, 1, 4));  // true (indices 1-3)
```

### Arrays.fill() - Fill Array with Value

Fills all or part of an array with a specified value.

```java
// Fill entire array
int[] numbers = new int[5];
Arrays.fill(numbers, 42);
System.out.println(Arrays.toString(numbers));  // [42, 42, 42, 42, 42]

// Fill a range
int[] partial = new int[5];
Arrays.fill(partial, 1, 4, 99);  // Fill indices 1, 2, 3
System.out.println(Arrays.toString(partial));  // [0, 99, 99, 99, 0]

// Works with all types
String[] names = new String[3];
Arrays.fill(names, "Unknown");
System.out.println(Arrays.toString(names));  // [Unknown, Unknown, Unknown]

boolean[] flags = new boolean[4];
Arrays.fill(flags, true);
System.out.println(Arrays.toString(flags));  // [true, true, true, true]
```

### Arrays.copyOf() - Copy Array

Creates a new array that is a copy of the original. Can change the size.

```java
int[] original = {1, 2, 3, 4, 5};

// Exact copy
int[] copy = Arrays.copyOf(original, original.length);
System.out.println(Arrays.toString(copy));  // [1, 2, 3, 4, 5]

// Shorter copy (truncates)
int[] shorter = Arrays.copyOf(original, 3);
System.out.println(Arrays.toString(shorter));  // [1, 2, 3]

// Longer copy (pads with default values)
int[] longer = Arrays.copyOf(original, 8);
System.out.println(Arrays.toString(longer));  // [1, 2, 3, 4, 5, 0, 0, 0]

// Works with objects
String[] names = {"Alice", "Bob"};
String[] namesCopy = Arrays.copyOf(names, names.length);
```

### Arrays.copyOfRange() - Copy Range

Copies a portion of an array.

```java
int[] original = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

// Copy indices 3 to 7 (exclusive)
int[] portion = Arrays.copyOfRange(original, 3, 7);
System.out.println(Arrays.toString(portion));  // [3, 4, 5, 6]

// Copy from index 5 to end
int[] tail = Arrays.copyOfRange(original, 5, original.length);
System.out.println(Arrays.toString(tail));  // [5, 6, 7, 8, 9]

// If end index exceeds length, pads with defaults
int[] padded = Arrays.copyOfRange(original, 7, 12);
System.out.println(Arrays.toString(padded));  // [7, 8, 9, 0, 0]
```

### Arrays.mismatch() - Find First Difference

Returns the index of the first mismatch between two arrays, or -1 if they're equal.

```java
int[] a = {1, 2, 3, 4, 5};
int[] b = {1, 2, 9, 4, 5};
int[] c = {1, 2, 3, 4, 5};

int mismatchIndex = Arrays.mismatch(a, b);
System.out.println(mismatchIndex);  // 2 (first difference at index 2)

int noMismatch = Arrays.mismatch(a, c);
System.out.println(noMismatch);  // -1 (arrays are equal)

// With range
int[] x = {0, 1, 2, 3, 0};
int[] y = {9, 1, 2, 3, 9};
int rangeMismatch = Arrays.mismatch(x, 1, 4, y, 1, 4);
System.out.println(rangeMismatch);  // -1 (ranges are equal)
```

### Arrays.compare() - Compare Arrays Lexicographically

Compares arrays element by element, like comparing strings. Returns negative, zero, or positive.

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 4};
int[] c = {1, 2, 3};
int[] d = {1, 2};

System.out.println(Arrays.compare(a, b));  // -1 (a < b, because 3 < 4)
System.out.println(Arrays.compare(b, a));  // 1  (b > a)
System.out.println(Arrays.compare(a, c));  // 0  (equal)
System.out.println(Arrays.compare(a, d));  // 1  (a > d, a is longer)
System.out.println(Arrays.compare(d, a));  // -1 (d < a, d is shorter)
```

### Arrays.setAll() - Set Elements Using Function

Sets each element based on a function of its index.

```java
int[] numbers = new int[5];

// Set each element to its index
Arrays.setAll(numbers, i -> i);
System.out.println(Arrays.toString(numbers));  // [0, 1, 2, 3, 4]

// Set each element to index squared
Arrays.setAll(numbers, i -> i * i);
System.out.println(Arrays.toString(numbers));  // [0, 1, 4, 9, 16]

// Set with more complex logic
Arrays.setAll(numbers, i -> i % 2 == 0 ? i : -i);
System.out.println(Arrays.toString(numbers));  // [0, -1, 2, -3, 4]

// For double arrays
double[] doubles = new double[5];
Arrays.setAll(doubles, i -> Math.sqrt(i));
System.out.println(Arrays.toString(doubles));  // [0.0, 1.0, 1.414..., 1.732..., 2.0]
```

### Arrays.parallelSetAll() - Parallel Version

For large arrays, uses multiple threads.

```java
int[] large = new int[1000000];
Arrays.parallelSetAll(large, i -> i * 2);
```

### Arrays.stream() - Convert to Stream

Creates a stream from an array for functional operations.

```java
int[] numbers = {1, 2, 3, 4, 5};

// Create IntStream
int sum = Arrays.stream(numbers).sum();
System.out.println(sum);  // 15

double avg = Arrays.stream(numbers).average().orElse(0);
System.out.println(avg);  // 3.0

int max = Arrays.stream(numbers).max().orElse(0);
System.out.println(max);  // 5

// Filter and collect
int[] evens = Arrays.stream(numbers)
                    .filter(n -> n % 2 == 0)
                    .toArray();
System.out.println(Arrays.toString(evens));  // [2, 4]

// Stream from range of array
int sumRange = Arrays.stream(numbers, 1, 4).sum();  // Indices 1, 2, 3
System.out.println(sumRange);  // 9 (2 + 3 + 4)

// Object arrays
String[] names = {"Alice", "Bob", "Charlie"};
String joined = Arrays.stream(names)
                      .filter(s -> s.length() > 3)
                      .reduce("", (a, b) -> a + b + " ");
System.out.println(joined);  // "Alice Charlie "
```

### Arrays.asList() - Convert to List

Creates a fixed-size List backed by the array.

```java
String[] names = {"Alice", "Bob", "Charlie"};

// Convert to List
List<String> nameList = Arrays.asList(names);
System.out.println(nameList);  // [Alice, Bob, Charlie]

// Can modify elements
nameList.set(0, "Alex");
System.out.println(Arrays.toString(names));  // [Alex, Bob, Charlie] (array modified!)

// CANNOT add or remove (fixed size)
// nameList.add("David");     // UnsupportedOperationException
// nameList.remove(0);        // UnsupportedOperationException

// For a mutable list, wrap it
List<String> mutableList = new ArrayList<>(Arrays.asList(names));
mutableList.add("David");  // OK
```

**Warning with primitives:** asList doesn't work as expected with primitive arrays.

```java
int[] numbers = {1, 2, 3};
List<int[]> wrong = Arrays.asList(numbers);  // List with one element (the array!)
System.out.println(wrong.size());  // 1

// Use Integer[] instead
Integer[] boxed = {1, 2, 3};
List<Integer> correct = Arrays.asList(boxed);
System.out.println(correct.size());  // 3
```

### Arrays.hashCode() - Generate Hash Code

Generates a hash code based on array contents.

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = {1, 2, 4};

System.out.println(Arrays.hashCode(a));  // 30817
System.out.println(Arrays.hashCode(b));  // 30817 (same as a)
System.out.println(Arrays.hashCode(c));  // 30818 (different)

// Useful for using arrays as map keys (though not recommended)
```

---

## System.arraycopy() - Fast Array Copy

The `System.arraycopy()` method is the fastest way to copy arrays.

```java
// Syntax: System.arraycopy(src, srcPos, dest, destPos, length)

int[] source = {1, 2, 3, 4, 5};
int[] dest = new int[5];

// Copy entire array
System.arraycopy(source, 0, dest, 0, source.length);
System.out.println(Arrays.toString(dest));  // [1, 2, 3, 4, 5]

// Copy portion
int[] partial = new int[3];
System.arraycopy(source, 1, partial, 0, 3);  // Copy indices 1, 2, 3
System.out.println(Arrays.toString(partial));  // [2, 3, 4]

// Copy to middle of destination
int[] target = new int[7];
System.arraycopy(source, 0, target, 1, source.length);
System.out.println(Arrays.toString(target));  // [0, 1, 2, 3, 4, 5, 0]

// Shift elements within same array
int[] arr = {1, 2, 3, 4, 5};
System.arraycopy(arr, 0, arr, 1, 4);  // Shift right by 1
arr[0] = 0;
System.out.println(Arrays.toString(arr));  // [0, 1, 2, 3, 4]
```

---

## Object.clone() - Clone Array

Arrays have a built-in `clone()` method.

```java
int[] original = {1, 2, 3, 4, 5};

// Clone creates a shallow copy
int[] cloned = original.clone();
System.out.println(Arrays.toString(cloned));  // [1, 2, 3, 4, 5]

// Modifying clone doesn't affect original (for primitives)
cloned[0] = 100;
System.out.println(Arrays.toString(original));  // [1, 2, 3, 4, 5] (unchanged)
System.out.println(Arrays.toString(cloned));    // [100, 2, 3, 4, 5]
```

**Shallow copy warning for object arrays:**

```java
StringBuilder[] original = {new StringBuilder("A"), new StringBuilder("B")};
StringBuilder[] cloned = original.clone();

// Both arrays reference the same objects!
cloned[0].append("BC");
System.out.println(original[0]);  // "ABC" (also modified!)
```

---

## Common Array Operations

### Finding Maximum and Minimum

```java
int[] numbers = {5, 2, 8, 1, 9, 3};

// Using loop
int max = numbers[0];
int min = numbers[0];
for (int num : numbers) {
    if (num > max) max = num;
    if (num < min) min = num;
}
System.out.println("Max: " + max + ", Min: " + min);  // Max: 9, Min: 1

// Using streams
int maxStream = Arrays.stream(numbers).max().orElse(0);
int minStream = Arrays.stream(numbers).min().orElse(0);
```

### Calculating Sum and Average

```java
int[] numbers = {1, 2, 3, 4, 5};

// Using loop
int sum = 0;
for (int num : numbers) {
    sum += num;
}
double average = (double) sum / numbers.length;

// Using streams
int sumStream = Arrays.stream(numbers).sum();
double avgStream = Arrays.stream(numbers).average().orElse(0);
```

### Checking if Array Contains Value

```java
int[] numbers = {1, 2, 3, 4, 5};
int target = 3;

// Using loop
boolean found = false;
for (int num : numbers) {
    if (num == target) {
        found = true;
        break;
    }
}

// Using stream
boolean foundStream = Arrays.stream(numbers).anyMatch(n -> n == target);

// Using binarySearch (array must be sorted)
Arrays.sort(numbers);
boolean foundBinary = Arrays.binarySearch(numbers, target) >= 0;

// For object arrays, use List
String[] names = {"Alice", "Bob", "Charlie"};
boolean containsBob = Arrays.asList(names).contains("Bob");
```

### Finding Index of Element

```java
int[] numbers = {5, 2, 8, 2, 9};
int target = 2;

// Find first occurrence
int firstIndex = -1;
for (int i = 0; i < numbers.length; i++) {
    if (numbers[i] == target) {
        firstIndex = i;
        break;
    }
}
System.out.println("First index: " + firstIndex);  // 1

// Find last occurrence
int lastIndex = -1;
for (int i = numbers.length - 1; i >= 0; i--) {
    if (numbers[i] == target) {
        lastIndex = i;
        break;
    }
}
System.out.println("Last index: " + lastIndex);  // 3

// Using IntStream
int indexStream = java.util.stream.IntStream.range(0, numbers.length)
    .filter(i -> numbers[i] == target)
    .findFirst()
    .orElse(-1);
```

### Reversing an Array

```java
int[] numbers = {1, 2, 3, 4, 5};

// In-place reversal
for (int i = 0; i < numbers.length / 2; i++) {
    int temp = numbers[i];
    numbers[i] = numbers[numbers.length - 1 - i];
    numbers[numbers.length - 1 - i] = temp;
}
System.out.println(Arrays.toString(numbers));  // [5, 4, 3, 2, 1]

// Using Collections (for Integer[])
Integer[] boxed = {1, 2, 3, 4, 5};
Collections.reverse(Arrays.asList(boxed));
System.out.println(Arrays.toString(boxed));  // [5, 4, 3, 2, 1]
```

### Removing Duplicates

```java
int[] numbers = {1, 2, 2, 3, 3, 3, 4, 5, 5};

// Using streams
int[] unique = Arrays.stream(numbers).distinct().toArray();
System.out.println(Arrays.toString(unique));  // [1, 2, 3, 4, 5]

// For sorted array (manual approach)
int[] sorted = {1, 2, 2, 3, 3, 4};
int[] temp = new int[sorted.length];
int j = 0;
for (int i = 0; i < sorted.length; i++) {
    if (i == 0 || sorted[i] != sorted[i - 1]) {
        temp[j++] = sorted[i];
    }
}
int[] uniqueManual = Arrays.copyOf(temp, j);
```

### Counting Occurrences

```java
int[] numbers = {1, 2, 2, 3, 3, 3, 4, 5, 5};
int target = 3;

// Using loop
int count = 0;
for (int num : numbers) {
    if (num == target) count++;
}
System.out.println("Count of " + target + ": " + count);  // 3

// Using stream
long countStream = Arrays.stream(numbers).filter(n -> n == target).count();
```

---

## Passing Arrays to Methods

Arrays are passed by reference (the reference is passed by value). Changes to array elements inside the method affect the original array.

```java
public static void main(String[] args) {
    int[] numbers = {1, 2, 3};
    
    modifyArray(numbers);
    System.out.println(Arrays.toString(numbers));  // [100, 2, 3]
    
    // But reassigning the parameter doesn't affect original
    replaceArray(numbers);
    System.out.println(Arrays.toString(numbers));  // [100, 2, 3] (unchanged)
}

static void modifyArray(int[] arr) {
    arr[0] = 100;  // Modifies original array
}

static void replaceArray(int[] arr) {
    arr = new int[] {9, 9, 9};  // Only changes local reference
}
```

### Returning Arrays from Methods

```java
public static int[] createArray(int size) {
    int[] result = new int[size];
    for (int i = 0; i < size; i++) {
        result[i] = i + 1;
    }
    return result;
}

public static int[] doubleValues(int[] original) {
    int[] doubled = new int[original.length];
    for (int i = 0; i < original.length; i++) {
        doubled[i] = original[i] * 2;
    }
    return doubled;
}
```

### Varargs (Variable Arguments)

Methods can accept a variable number of arguments using varargs, which is internally an array.

```java
public static int sum(int... numbers) {
    int total = 0;
    for (int num : numbers) {
        total += num;
    }
    return total;
}

// Can call with any number of arguments
System.out.println(sum(1, 2));          // 3
System.out.println(sum(1, 2, 3, 4, 5)); // 15
System.out.println(sum());              // 0

// Can also pass an array
int[] arr = {1, 2, 3};
System.out.println(sum(arr));           // 6
```

---

## Arrays of Objects

```java
// Array of Strings
String[] names = new String[3];
names[0] = "Alice";
names[1] = "Bob";
names[2] = "Charlie";

// Array initializer
String[] fruits = {"Apple", "Banana", "Cherry"};

// Calling methods on elements
for (String name : names) {
    if (name != null) {  // Always check for null with object arrays
        System.out.println(name.toUpperCase());
    }
}

// Array of custom objects
class Person {
    String name;
    int age;
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

Person[] people = new Person[2];
people[0] = new Person("Alice", 30);
people[1] = new Person("Bob", 25);

// Or with initializer
Person[] team = {
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
};
```

---

## Common Mistakes

### 1. Confusing Length Property

```java
int[] arr = {1, 2, 3};

// Array uses .length (property)
int size = arr.length;

// String uses .length() (method)
String str = "hello";
int len = str.length();

// ArrayList uses .size() (method)
ArrayList<Integer> list = new ArrayList<>();
int listSize = list.size();
```

### 2. Off-by-One Errors

```java
int[] arr = {1, 2, 3, 4, 5};

// Wrong: ArrayIndexOutOfBoundsException
for (int i = 0; i <= arr.length; i++) { }  // <= should be <

// Wrong: ArrayIndexOutOfBoundsException
int last = arr[arr.length];  // Should be arr.length - 1

// Correct
for (int i = 0; i < arr.length; i++) { }
int last = arr[arr.length - 1];
```

### 3. Comparing Arrays with ==

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

// Wrong: compares references
if (a == b) { }  // false!

// Correct: use Arrays.equals()
if (Arrays.equals(a, b)) { }  // true
```

### 4. Printing Arrays Directly

```java
int[] arr = {1, 2, 3};

// Wrong: prints memory address
System.out.println(arr);  // [I@1b6d3586

// Correct: use Arrays.toString()
System.out.println(Arrays.toString(arr));  // [1, 2, 3]
```

### 5. Null Elements in Object Arrays

```java
String[] names = new String[3];
names[0] = "Alice";
// names[1] and names[2] are null!

// Wrong: NullPointerException
for (String name : names) {
    System.out.println(name.toUpperCase());  // Fails on null
}

// Correct: check for null
for (String name : names) {
    if (name != null) {
        System.out.println(name.toUpperCase());
    }
}
```

---

## Merging and Concatenating Arrays

### Concatenating Two Arrays

```java
int[] arr1 = {1, 2, 3};
int[] arr2 = {4, 5, 6};

// Using System.arraycopy
int[] merged = new int[arr1.length + arr2.length];
System.arraycopy(arr1, 0, merged, 0, arr1.length);
System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);
System.out.println(Arrays.toString(merged));  // [1, 2, 3, 4, 5, 6]

// Using streams
int[] streamMerged = java.util.stream.IntStream
    .concat(Arrays.stream(arr1), Arrays.stream(arr2))
    .toArray();
System.out.println(Arrays.toString(streamMerged));  // [1, 2, 3, 4, 5, 6]

// For object arrays using streams
String[] names1 = {"Alice", "Bob"};
String[] names2 = {"Charlie", "David"};
String[] allNames = java.util.stream.Stream
    .concat(Arrays.stream(names1), Arrays.stream(names2))
    .toArray(String[]::new);
```

### Merging Multiple Arrays

```java
// Using streams for multiple arrays
int[] a = {1, 2};
int[] b = {3, 4};
int[] c = {5, 6};

int[] all = java.util.stream.Stream.of(a, b, c)
    .flatMapToInt(Arrays::stream)
    .toArray();
System.out.println(Arrays.toString(all));  // [1, 2, 3, 4, 5, 6]
```

### Merge Sorted Arrays

Efficiently merge two already-sorted arrays into a sorted result.

```java
public static int[] mergeSorted(int[] arr1, int[] arr2) {
    int[] result = new int[arr1.length + arr2.length];
    int i = 0, j = 0, k = 0;
    
    while (i < arr1.length && j < arr2.length) {
        if (arr1[i] <= arr2[j]) {
            result[k++] = arr1[i++];
        } else {
            result[k++] = arr2[j++];
        }
    }
    
    // Copy remaining elements
    while (i < arr1.length) {
        result[k++] = arr1[i++];
    }
    while (j < arr2.length) {
        result[k++] = arr2[j++];
    }
    
    return result;
}

// Usage
int[] sorted1 = {1, 3, 5, 7};
int[] sorted2 = {2, 4, 6, 8};
int[] merged = mergeSorted(sorted1, sorted2);
System.out.println(Arrays.toString(merged));  // [1, 2, 3, 4, 5, 6, 7, 8]
```

---

## Shuffling Arrays

### Using Collections.shuffle()

```java
// For object arrays (Integer[], String[], etc.)
Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
List<Integer> list = Arrays.asList(numbers);
Collections.shuffle(list);
System.out.println(Arrays.toString(numbers));  // Random order
```

### Fisher-Yates Shuffle (for primitive arrays)

```java
public static void shuffle(int[] array) {
    Random random = new Random();
    for (int i = array.length - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        // Swap array[i] and array[j]
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

// Usage
int[] numbers = {1, 2, 3, 4, 5};
shuffle(numbers);
System.out.println(Arrays.toString(numbers));  // Random order
```

---

## Rotating Arrays

### Rotate Left

```java
// Rotate array left by k positions
public static void rotateLeft(int[] arr, int k) {
    int n = arr.length;
    k = k % n;  // Handle k > n
    
    reverse(arr, 0, k - 1);
    reverse(arr, k, n - 1);
    reverse(arr, 0, n - 1);
}

private static void reverse(int[] arr, int start, int end) {
    while (start < end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}

// Usage
int[] arr = {1, 2, 3, 4, 5};
rotateLeft(arr, 2);
System.out.println(Arrays.toString(arr));  // [3, 4, 5, 1, 2]
```

### Rotate Right

```java
// Rotate array right by k positions
public static void rotateRight(int[] arr, int k) {
    int n = arr.length;
    k = k % n;  // Handle k > n
    
    reverse(arr, 0, n - 1);
    reverse(arr, 0, k - 1);
    reverse(arr, k, n - 1);
}

// Usage
int[] arr = {1, 2, 3, 4, 5};
rotateRight(arr, 2);
System.out.println(Arrays.toString(arr));  // [4, 5, 1, 2, 3]
```

---

## Partitioning Arrays

### Partition Around a Pivot

```java
// Partition array: elements < pivot on left, >= pivot on right
public static int partition(int[] arr, int pivot) {
    int left = 0;
    int right = arr.length - 1;
    
    while (left <= right) {
        while (left <= right && arr[left] < pivot) {
            left++;
        }
        while (left <= right && arr[right] >= pivot) {
            right--;
        }
        if (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }
    }
    return left;  // Partition index
}

// Usage
int[] arr = {5, 2, 8, 1, 9, 3, 7, 4, 6};
int pivotIndex = partition(arr, 5);
System.out.println(Arrays.toString(arr));  // Elements < 5 on left
System.out.println("Partition index: " + pivotIndex);
```

### Separate Even and Odd Numbers

```java
public static void separateEvenOdd(int[] arr) {
    int left = 0;
    int right = arr.length - 1;
    
    while (left < right) {
        while (left < right && arr[left] % 2 == 0) {
            left++;
        }
        while (left < right && arr[right] % 2 != 0) {
            right--;
        }
        if (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }
    }
}

// Usage
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
separateEvenOdd(arr);
System.out.println(Arrays.toString(arr));  // Evens first, then odds
```

---

## Converting Between Types

### Primitive Array to Wrapper Array

```java
int[] primitiveArray = {1, 2, 3, 4, 5};

// Using streams (Java 8+)
Integer[] wrapperArray = Arrays.stream(primitiveArray)
    .boxed()
    .toArray(Integer[]::new);

// Manual conversion
Integer[] manual = new Integer[primitiveArray.length];
for (int i = 0; i < primitiveArray.length; i++) {
    manual[i] = primitiveArray[i];
}
```

### Wrapper Array to Primitive Array

```java
Integer[] wrapperArray = {1, 2, 3, 4, 5};

// Using streams
int[] primitiveArray = Arrays.stream(wrapperArray)
    .mapToInt(Integer::intValue)
    .toArray();

// Manual conversion
int[] manual = new int[wrapperArray.length];
for (int i = 0; i < wrapperArray.length; i++) {
    manual[i] = wrapperArray[i];  // Auto-unboxing
}
```

### Array to ArrayList

```java
// Object arrays
String[] names = {"Alice", "Bob", "Charlie"};
ArrayList<String> nameList = new ArrayList<>(Arrays.asList(names));
nameList.add("David");  // Now mutable

// Primitive arrays - must box first
int[] numbers = {1, 2, 3, 4, 5};
ArrayList<Integer> numList = new ArrayList<>();
for (int num : numbers) {
    numList.add(num);
}

// Or using streams
ArrayList<Integer> streamList = Arrays.stream(numbers)
    .boxed()
    .collect(Collectors.toCollection(ArrayList::new));
```

### ArrayList to Array

```java
ArrayList<String> list = new ArrayList<>();
list.add("Alice");
list.add("Bob");
list.add("Charlie");

// Convert to array
String[] array = list.toArray(new String[0]);
// Or with exact size
String[] array2 = list.toArray(new String[list.size()]);

// For Integer to int[]
ArrayList<Integer> intList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
int[] intArray = intList.stream().mapToInt(Integer::intValue).toArray();
```

---

## Splitting Arrays

### Split into Chunks

```java
public static int[][] splitIntoChunks(int[] array, int chunkSize) {
    int numChunks = (int) Math.ceil((double) array.length / chunkSize);
    int[][] chunks = new int[numChunks][];
    
    for (int i = 0; i < numChunks; i++) {
        int start = i * chunkSize;
        int end = Math.min(start + chunkSize, array.length);
        chunks[i] = Arrays.copyOfRange(array, start, end);
    }
    
    return chunks;
}

// Usage
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
int[][] chunks = splitIntoChunks(arr, 3);
for (int[] chunk : chunks) {
    System.out.println(Arrays.toString(chunk));
}
// Output:
// [1, 2, 3]
// [4, 5, 6]
// [7, 8, 9]
// [10]
```

### Split at Index

```java
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
int splitIndex = 4;

int[] left = Arrays.copyOfRange(arr, 0, splitIndex);
int[] right = Arrays.copyOfRange(arr, splitIndex, arr.length);

System.out.println(Arrays.toString(left));   // [1, 2, 3, 4]
System.out.println(Arrays.toString(right));  // [5, 6, 7, 8, 9, 10]
```

---

## Memory and Performance

### Array Memory Layout

Arrays in Java are objects stored on the heap. Memory usage includes:

```java
// Memory = header (12-16 bytes) + length (4 bytes) + elements

int[] arr = new int[1000];
// Memory ~ 16 + 4 + (1000 * 4) = 4020 bytes for int array

String[] strings = new String[1000];
// Memory ~ 16 + 4 + (1000 * 8) = 8020 bytes for references
// Plus the String objects themselves
```

### Performance Characteristics

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Access by index | O(1) | Constant time |
| Update by index | O(1) | Constant time |
| Search (unsorted) | O(n) | Linear scan |
| Search (sorted) | O(log n) | Binary search |
| Insert at end | O(1)* | Only if space available |
| Insert at middle | O(n) | Requires shifting |
| Delete | O(n) | Requires shifting |
| Sort | O(n log n) | Using Arrays.sort() |

### Best Practices

```java
// 1. Pre-size arrays when possible
int[] arr = new int[expectedSize];  // Better than resizing

// 2. Use System.arraycopy for bulk operations
System.arraycopy(src, 0, dest, 0, length);  // Faster than loop

// 3. Consider ArrayList for dynamic sizing
ArrayList<Integer> list = new ArrayList<>();  // Grows automatically

// 4. Use primitive arrays over wrapper arrays when possible
int[] primitives = new int[1000];      // More memory efficient
Integer[] wrappers = new Integer[1000]; // Each Integer is an object

// 5. Avoid unnecessary array creation in loops
// Bad
for (int i = 0; i < 1000; i++) {
    int[] temp = new int[10];  // Creates 1000 arrays!
}

// Good
int[] temp = new int[10];
for (int i = 0; i < 1000; i++) {
    Arrays.fill(temp, 0);  // Reuse the array
}
```

---

## Practical Examples

### Finding Second Largest Element

```java
public static int secondLargest(int[] arr) {
    if (arr.length < 2) {
        throw new IllegalArgumentException("Array must have at least 2 elements");
    }
    
    int first = Integer.MIN_VALUE;
    int second = Integer.MIN_VALUE;
    
    for (int num : arr) {
        if (num > first) {
            second = first;
            first = num;
        } else if (num > second && num != first) {
            second = num;
        }
    }
    
    if (second == Integer.MIN_VALUE) {
        throw new IllegalArgumentException("No second largest element");
    }
    
    return second;
}

// Usage
int[] arr = {5, 2, 8, 1, 9, 3};
System.out.println(secondLargest(arr));  // 8
```

### Finding Missing Number in Sequence

```java
// Array contains n-1 numbers from 1 to n, find missing
public static int findMissing(int[] arr, int n) {
    int expectedSum = n * (n + 1) / 2;
    int actualSum = Arrays.stream(arr).sum();
    return expectedSum - actualSum;
}

// Usage
int[] arr = {1, 2, 4, 5, 6};  // Missing 3
System.out.println(findMissing(arr, 6));  // 3
```

### Finding Duplicates

```java
// Find all duplicate elements
public static int[] findDuplicates(int[] arr) {
    Set<Integer> seen = new HashSet<>();
    Set<Integer> duplicates = new HashSet<>();
    
    for (int num : arr) {
        if (!seen.add(num)) {
            duplicates.add(num);
        }
    }
    
    return duplicates.stream().mapToInt(Integer::intValue).toArray();
}

// Usage
int[] arr = {1, 2, 3, 2, 4, 3, 5};
System.out.println(Arrays.toString(findDuplicates(arr)));  // [2, 3]
```

### Moving Zeros to End

```java
public static void moveZerosToEnd(int[] arr) {
    int insertPos = 0;
    
    // Move all non-zero elements forward
    for (int num : arr) {
        if (num != 0) {
            arr[insertPos++] = num;
        }
    }
    
    // Fill remaining positions with zeros
    while (insertPos < arr.length) {
        arr[insertPos++] = 0;
    }
}

// Usage
int[] arr = {0, 1, 0, 3, 12, 0, 5};
moveZerosToEnd(arr);
System.out.println(Arrays.toString(arr));  // [1, 3, 12, 5, 0, 0, 0]
```

### Two Sum Problem

```java
// Find two numbers that add up to target
public static int[] twoSum(int[] arr, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    
    for (int i = 0; i < arr.length; i++) {
        int complement = target - arr[i];
        if (map.containsKey(complement)) {
            return new int[] {map.get(complement), i};
        }
        map.put(arr[i], i);
    }
    
    return new int[] {-1, -1};  // Not found
}

// Usage
int[] arr = {2, 7, 11, 15};
int[] result = twoSum(arr, 9);
System.out.println(Arrays.toString(result));  // [0, 1]
```

### Kadane's Algorithm (Maximum Subarray Sum)

```java
public static int maxSubarraySum(int[] arr) {
    int maxSoFar = arr[0];
    int maxEndingHere = arr[0];
    
    for (int i = 1; i < arr.length; i++) {
        maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }
    
    return maxSoFar;
}

// Usage
int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
System.out.println(maxSubarraySum(arr));  // 6 (subarray [4, -1, 2, 1])
```

### Frequency Count

```java
// Count frequency of each element
public static Map<Integer, Integer> frequencyCount(int[] arr) {
    Map<Integer, Integer> freq = new HashMap<>();
    
    for (int num : arr) {
        freq.put(num, freq.getOrDefault(num, 0) + 1);
    }
    
    return freq;
}

// Using streams
Map<Integer, Long> streamFreq = Arrays.stream(arr)
    .boxed()
    .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

// Usage
int[] arr = {1, 2, 2, 3, 3, 3, 4};
System.out.println(frequencyCount(arr));  // {1=1, 2=2, 3=3, 4=1}
```

---

## Summary

| Operation | Method/Syntax |
|-----------|---------------|
| Create array | `int[] arr = new int[5];` or `{1, 2, 3}` |
| Access element | `arr[index]` |
| Get length | `arr.length` |
| Print array | `Arrays.toString(arr)` |
| Sort | `Arrays.sort(arr)` |
| Parallel sort | `Arrays.parallelSort(arr)` |
| Binary search | `Arrays.binarySearch(arr, key)` |
| Compare arrays | `Arrays.equals(arr1, arr2)` |
| Fill array | `Arrays.fill(arr, value)` |
| Copy array | `Arrays.copyOf(arr, length)` |
| Copy range | `Arrays.copyOfRange(arr, from, to)` |
| Fast copy | `System.arraycopy(src, srcPos, dest, destPos, len)` |
| Clone | `arr.clone()` |
| Convert to List | `Arrays.asList(arr)` |
| Convert to Stream | `Arrays.stream(arr)` |
| Set with function | `Arrays.setAll(arr, i -> expression)` |
| Find mismatch | `Arrays.mismatch(arr1, arr2)` |
| Lexicographic compare | `Arrays.compare(arr1, arr2)` |
| Hash code | `Arrays.hashCode(arr)` |
| Concatenate | `IntStream.concat(stream1, stream2).toArray()` |
| Shuffle | Fisher-Yates or `Collections.shuffle()` |
| Rotate | Reverse-based algorithm |
| Partition | Two-pointer technique |
| To ArrayList | `new ArrayList<>(Arrays.asList(arr))` |
| To primitive array | `stream.mapToInt(Integer::intValue).toArray()` |

**Key Points:**
- Arrays have fixed size once created
- Indices start at 0
- Use `Arrays` utility class for common operations
- Array elements default to zero/false/null
- Object arrays hold references - be aware of shallow copies
- Use `Arrays.toString()` to print array contents
- Use `Arrays.equals()` to compare array contents
- For dynamic sizing, consider ArrayList
- Primitive arrays are more memory efficient than wrapper arrays
- Access and update are O(1), search in unsorted is O(n), sorted is O(log n)

