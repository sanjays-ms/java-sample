# Multi-Dimensional Arrays Cheat Sheet

[<- Back to Guide](../guide.md) | **Full Documentation:** [Multi-Dimensional Arrays](../documentation/05-multidimensional-arrays.md)

Quick reference for Java multi-dimensional arrays.

---

## Creating 2D Arrays

```java
// With new keyword
int[][] matrix = new int[3][4];     // 3 rows, 4 columns

// With initializer
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Jagged array (different row lengths)
int[][] jagged = new int[3][];
jagged[0] = new int[2];
jagged[1] = new int[4];
jagged[2] = new int[3];
```

---

## Accessing Elements

```java
int[][] matrix = {{1,2,3}, {4,5,6}, {7,8,9}};

matrix[0][1]            // Row 0, Col 1 = 2
matrix[2][2]            // Row 2, Col 2 = 9
matrix[row][col]        // General access

matrix.length           // Number of rows (3)
matrix[0].length        // Columns in row 0 (3)
matrix[row].length      // Columns in any row
```

---

## Iterating

### Nested For Loop

```java
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}
```

### For-Each

```java
for (int[] row : matrix) {
    for (int val : row) {
        System.out.print(val + " ");
    }
    System.out.println();
}
```

### Column-First

```java
for (int col = 0; col < matrix[0].length; col++) {
    for (int row = 0; row < matrix.length; row++) {
        System.out.print(matrix[row][col] + " ");
    }
}
```

---

## Arrays Utility Methods

```java
import java.util.Arrays;

// Print (use deepToString for 2D+)
Arrays.deepToString(matrix)     // [[1,2,3], [4,5,6]]

// Compare (use deepEquals for 2D+)
Arrays.deepEquals(a, b)         // true if contents equal

// DO NOT use for 2D arrays:
// Arrays.toString()    - wrong output
// Arrays.equals()      - compares references
```

---

## Copying 2D Arrays

```java
// WRONG: Shallow copy (rows still reference original)
int[][] shallow = original.clone();

// CORRECT: Deep copy
int[][] deep = new int[original.length][];
for (int i = 0; i < original.length; i++) {
    deep[i] = Arrays.copyOf(original[i], original[i].length);
}

// Using streams
int[][] copy = Arrays.stream(original)
    .map(row -> Arrays.copyOf(row, row.length))
    .toArray(int[][]::new);
```

---

## Common Operations

### Sum All Elements

```java
int sum = 0;
for (int[] row : matrix) {
    for (int val : row) {
        sum += val;
    }
}

// Stream
int sum = Arrays.stream(matrix)
    .flatMapToInt(Arrays::stream)
    .sum();
```

### Find Max/Min

```java
int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;
for (int[] row : matrix) {
    for (int val : row) {
        max = Math.max(max, val);
        min = Math.min(min, val);
    }
}
```

### Row Sums

```java
for (int i = 0; i < matrix.length; i++) {
    int sum = Arrays.stream(matrix[i]).sum();
    System.out.println("Row " + i + ": " + sum);
}
```

### Column Sums

```java
for (int col = 0; col < matrix[0].length; col++) {
    int sum = 0;
    for (int row = 0; row < matrix.length; row++) {
        sum += matrix[row][col];
    }
    System.out.println("Col " + col + ": " + sum);
}
```

---

## Matrix Operations

### Transpose

```java
int[][] transpose = new int[cols][rows];
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        transpose[j][i] = matrix[i][j];
    }
}
```

### Addition

```java
int[][] result = new int[rows][cols];
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        result[i][j] = a[i][j] + b[i][j];
    }
}
```

### Multiplication

```java
// a[m][n] * b[n][p] = result[m][p]
int[][] result = new int[a.length][b[0].length];
for (int i = 0; i < a.length; i++) {
    for (int j = 0; j < b[0].length; j++) {
        for (int k = 0; k < a[0].length; k++) {
            result[i][j] += a[i][k] * b[k][j];
        }
    }
}
```

### Diagonals

```java
// Main diagonal (top-left to bottom-right)
for (int i = 0; i < n; i++) {
    System.out.print(matrix[i][i]);
}

// Anti-diagonal (top-right to bottom-left)
for (int i = 0; i < n; i++) {
    System.out.print(matrix[i][n - 1 - i]);
}
```

---

## 3D Arrays

```java
// Create
int[][][] cube = new int[2][3][4];  // 2 layers, 3 rows, 4 cols

// Initialize
int[][][] cube = {
    {{1, 2}, {3, 4}},   // Layer 0
    {{5, 6}, {7, 8}}    // Layer 1
};

// Access
cube[layer][row][col]

// Dimensions
cube.length             // Layers
cube[0].length          // Rows
cube[0][0].length       // Columns

// Print
Arrays.deepToString(cube)
```

---

## Jagged Arrays

```java
// Create with different row lengths
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7, 8, 9}
};

// Always use row's actual length
for (int i = 0; i < jagged.length; i++) {
    for (int j = 0; j < jagged[i].length; j++) {
        // ...
    }
}
```

---

## Common Mistakes

```java
// Wrong: Shallow copy
int[][] copy = original.clone();
copy[0][0] = 999;               // Also changes original!

// Wrong: Using equals for 2D
Arrays.equals(a, b)             // false (compares row refs)
Arrays.deepEquals(a, b)         // true (compares content)

// Wrong: Assuming rectangular
for (int j = 0; j < matrix[0].length; j++)  // May fail!
for (int j = 0; j < matrix[i].length; j++)  // Safe

// Wrong: Accessing before init
int[][] arr = new int[3][];
arr[0][0] = 1;                  // NullPointerException!
arr[0] = new int[3];            // Init row first
arr[0][0] = 1;                  // OK
```

---

## Quick Reference

| Task | Code |
|------|------|
| Create | `new int[rows][cols]` |
| Rows | `matrix.length` |
| Cols | `matrix[row].length` |
| Access | `matrix[row][col]` |
| Print | `Arrays.deepToString(m)` |
| Compare | `Arrays.deepEquals(a, b)` |
| Iterate | Nested loops or for-each |
| Deep copy | Loop with `Arrays.copyOf()` |
