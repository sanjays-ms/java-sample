# Arrays Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Arrays](../documentation/04-arrays.md)

Quick reference for Java arrays and Arrays utility methods.

---

## Creating Arrays

```java
// With new keyword
int[] numbers = new int[5];         // 5 elements, all 0

// With initializer
int[] numbers = {1, 2, 3, 4, 5};

// Anonymous array
printArray(new int[] {1, 2, 3});
```

---

## Accessing Elements

```java
int[] arr = {10, 20, 30, 40, 50};

arr[0]              // First: 10
arr[arr.length - 1] // Last: 50
arr.length          // Size: 5 (property, not method)
```

---

## Iterating

```java
// For loop
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}

// For-each (read-only)
for (int num : arr) {
    System.out.println(num);
}
```

---

## Arrays Utility Class

```java
import java.util.Arrays;
```

### Print Array

```java
Arrays.toString(arr)    // [1, 2, 3, 4, 5]
```

### Sort

```java
Arrays.sort(arr);                    // Ascending
Arrays.sort(arr, 1, 4);              // Sort range [1, 4)
Arrays.sort(boxed, Collections.reverseOrder()); // Descending (Integer[])
Arrays.parallelSort(arr);            // Multi-threaded sort
```

### Search

```java
// Array MUST be sorted first!
Arrays.binarySearch(arr, key)        // Returns index or negative
```

### Compare

```java
Arrays.equals(arr1, arr2)            // Content equality
Arrays.compare(arr1, arr2)           // Lexicographic (-1, 0, 1)
Arrays.mismatch(arr1, arr2)          // First diff index or -1
```

### Fill

```java
Arrays.fill(arr, value)              // Fill all
Arrays.fill(arr, from, to, value)    // Fill range
```

### Copy

```java
Arrays.copyOf(arr, newLength)        // Copy with new size
Arrays.copyOfRange(arr, from, to)    // Copy range
arr.clone()                          // Clone array
System.arraycopy(src, srcPos, dest, destPos, len)  // Fast copy
```

### Transform

```java
Arrays.setAll(arr, i -> i * 2)       // Set by index function
Arrays.parallelSetAll(arr, i -> i)   // Parallel version
```

### Convert

```java
Arrays.asList(arr)                   // Array to List (fixed-size)
Arrays.stream(arr)                   // Array to Stream
```

---

## Stream Operations

```java
import java.util.Arrays;

int[] arr = {1, 2, 3, 4, 5};

Arrays.stream(arr).sum()                    // 15
Arrays.stream(arr).average().orElse(0)      // 3.0
Arrays.stream(arr).max().orElse(0)          // 5
Arrays.stream(arr).min().orElse(0)          // 1
Arrays.stream(arr).count()                  // 5

// Filter and collect
Arrays.stream(arr).filter(n -> n > 2).toArray()  // [3, 4, 5]

// Check conditions
Arrays.stream(arr).anyMatch(n -> n > 3)     // true
Arrays.stream(arr).allMatch(n -> n > 0)     // true
Arrays.stream(arr).noneMatch(n -> n < 0)    // true
```

---

## Common Operations

### Find Max/Min

```java
int max = Arrays.stream(arr).max().orElse(0);
int min = Arrays.stream(arr).min().orElse(0);
```

### Sum/Average

```java
int sum = Arrays.stream(arr).sum();
double avg = Arrays.stream(arr).average().orElse(0);
```

### Contains

```java
boolean found = Arrays.stream(arr).anyMatch(n -> n == target);

// For objects
boolean contains = Arrays.asList(names).contains("Bob");
```

### Reverse

```java
// In-place
for (int i = 0; i < arr.length / 2; i++) {
    int temp = arr[i];
    arr[i] = arr[arr.length - 1 - i];
    arr[arr.length - 1 - i] = temp;
}

// Using Collections (Integer[])
Collections.reverse(Arrays.asList(boxedArr));
```

### Remove Duplicates

```java
int[] unique = Arrays.stream(arr).distinct().toArray();
```

### Count Occurrences

```java
long count = Arrays.stream(arr).filter(n -> n == target).count();
```

---

## Merging and Concatenating

```java
int[] arr1 = {1, 2, 3};
int[] arr2 = {4, 5, 6};

// Using System.arraycopy
int[] merged = new int[arr1.length + arr2.length];
System.arraycopy(arr1, 0, merged, 0, arr1.length);
System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);

// Using streams
int[] merged = IntStream.concat(Arrays.stream(arr1), Arrays.stream(arr2)).toArray();
```

---

## Shuffling

```java
// Object arrays
Integer[] arr = {1, 2, 3, 4, 5};
Collections.shuffle(Arrays.asList(arr));

// Primitive arrays (Fisher-Yates)
Random rand = new Random();
for (int i = arr.length - 1; i > 0; i--) {
    int j = rand.nextInt(i + 1);
    int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
}
```

---

## Rotating

```java
// Rotate right by k (use reverse helper)
reverse(arr, 0, arr.length - 1);
reverse(arr, 0, k - 1);
reverse(arr, k, arr.length - 1);
```

---

## Type Conversions

```java
// Primitive to wrapper
Integer[] boxed = Arrays.stream(intArr).boxed().toArray(Integer[]::new);

// Wrapper to primitive
int[] primitive = Arrays.stream(boxed).mapToInt(Integer::intValue).toArray();

// Array to ArrayList
ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));

// ArrayList to Array
String[] arr = list.toArray(new String[0]);
```

---

## Default Values

| Type | Default |
|------|---------|
| `int`, `long`, etc. | 0 |
| `double`, `float` | 0.0 |
| `boolean` | false |
| `char` | '\u0000' |
| Objects | null |

---

## Varargs

```java
void print(int... numbers) {
    for (int n : numbers) {
        System.out.print(n + " ");
    }
}

print(1, 2, 3);         // 1 2 3
print(new int[]{4, 5}); // 4 5
```

---

## Common Mistakes

```java
// Comparing arrays
a == b                      // Wrong (references)
Arrays.equals(a, b)         // Correct (content)

// Printing arrays
System.out.println(arr)     // [I@1234 (wrong)
Arrays.toString(arr)        // [1, 2, 3] (correct)

// Off-by-one
arr[arr.length]             // ArrayIndexOutOfBoundsException
arr[arr.length - 1]         // Correct (last element)

// Length syntax
arr.length                  // Array (property)
str.length()                // String (method)
list.size()                 // List (method)

// asList with primitives
Arrays.asList(new int[]{1,2,3}).size()  // 1 (wrong!)
Arrays.asList(new Integer[]{1,2,3}).size()  // 3 (correct)
```

---

## Quick Reference

| Task | Code |
|------|------|
| Create | `int[] a = {1, 2, 3};` |
| Length | `a.length` |
| First | `a[0]` |
| Last | `a[a.length - 1]` |
| Print | `Arrays.toString(a)` |
| Sort | `Arrays.sort(a)` |
| Parallel sort | `Arrays.parallelSort(a)` |
| Copy | `Arrays.copyOf(a, len)` |
| Copy range | `Arrays.copyOfRange(a, from, to)` |
| Compare | `Arrays.equals(a, b)` |
| Search | `Arrays.binarySearch(a, key)` |
| Fill | `Arrays.fill(a, val)` |
| Mismatch | `Arrays.mismatch(a, b)` |
| Hash code | `Arrays.hashCode(a)` |
| To List | `Arrays.asList(a)` |
| To Stream | `Arrays.stream(a)` |
| Sum | `Arrays.stream(a).sum()` |
| Max | `Arrays.stream(a).max()` |
| Min | `Arrays.stream(a).min()` |
| Unique | `Arrays.stream(a).distinct().toArray()` |
| Concat | `IntStream.concat(s1, s2).toArray()` |
| Box | `Arrays.stream(a).boxed().toArray(Integer[]::new)` |
| Unbox | `stream.mapToInt(Integer::intValue).toArray()` |
