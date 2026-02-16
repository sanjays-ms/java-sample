# 05. Multi-Dimensional Arrays

[<- Back: Arrays](./04-arrays.md) | [Back to Guide](../guide.md) | [Next: Strings ->](./06-strings.md)

**Quick Reference:** [Multi-Dimensional Arrays Cheatsheet](../cheatsheets/multidimensional-arrays-cheatsheet.md)

---

## Overview

Multi-dimensional arrays are arrays of arrays. They allow you to store data in a grid-like structure with rows and columns (2D) or even higher dimensions (3D, 4D, etc.).

Common uses:
- **2D arrays** - Matrices, game boards, spreadsheets, images
- **3D arrays** - 3D graphics, scientific data, time-series of 2D data

In Java, multi-dimensional arrays are implemented as arrays containing references to other arrays, which allows for flexible structures like jagged arrays.

---

## Two-Dimensional Arrays

A 2D array is like a table with rows and columns.

### Declaring 2D Arrays

```java
// Preferred syntax
int[][] matrix;
String[][] grid;
double[][] table;

// Alternative syntax (valid but less common)
int matrix[][];
int[] matrix[];  // Mixed style - avoid
```

### Creating 2D Arrays

#### Using new Keyword

```java
// Syntax: type[][] name = new type[rows][columns];
int[][] matrix = new int[3][4];  // 3 rows, 4 columns

// All elements initialized to default values (0 for int)
// Visualize as:
// [0, 0, 0, 0]
// [0, 0, 0, 0]
// [0, 0, 0, 0]
```

#### Using Array Initializer

```java
// Direct initialization with values
int[][] matrix = {
    {1, 2, 3, 4},
    {5, 6, 7, 8},
    {9, 10, 11, 12}
};

// Visualize as:
// [1,  2,  3,  4]
// [5,  6,  7,  8]
// [9, 10, 11, 12]

// Strings
String[][] names = {
    {"Alice", "Bob"},
    {"Charlie", "David"},
    {"Eve", "Frank"}
};
```

### Accessing Elements

Elements are accessed using two indices: `[row][column]`.

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Access element at row 0, column 1
int value = matrix[0][1];       // 2

// Access element at row 2, column 2
int corner = matrix[2][2];      // 9

// Modify element
matrix[1][1] = 100;             // Change center to 100

// Matrix is now:
// [1,   2,   3]
// [4, 100,   6]
// [7,   8,   9]
```

### Array Dimensions

```java
int[][] matrix = {
    {1, 2, 3, 4},
    {5, 6, 7, 8},
    {9, 10, 11, 12}
};

// Number of rows
int rows = matrix.length;           // 3

// Number of columns (in first row)
int cols = matrix[0].length;        // 4

// Total elements
int total = rows * cols;            // 12

// Access specific row
int[] firstRow = matrix[0];         // {1, 2, 3, 4}
int[] lastRow = matrix[matrix.length - 1];  // {9, 10, 11, 12}
```

---

## Iterating Over 2D Arrays

### Nested For Loops

The most common way to iterate through a 2D array.

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Row by row iteration
for (int row = 0; row < matrix.length; row++) {
    for (int col = 0; col < matrix[row].length; col++) {
        System.out.print(matrix[row][col] + " ");
    }
    System.out.println();  // New line after each row
}
// Output:
// 1 2 3
// 4 5 6
// 7 8 9
```

### Enhanced For Loop (For-Each)

Cleaner syntax when you don't need indices.

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// For-each: each row is an int[]
for (int[] row : matrix) {
    for (int value : row) {
        System.out.print(value + " ");
    }
    System.out.println();
}
```

### Column-First Iteration

Sometimes you need to iterate by columns instead of rows.

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Assuming rectangular array
int rows = matrix.length;
int cols = matrix[0].length;

for (int col = 0; col < cols; col++) {
    for (int row = 0; row < rows; row++) {
        System.out.print(matrix[row][col] + " ");
    }
    System.out.println();
}
// Output:
// 1 4 7
// 2 5 8
// 3 6 9
```

### Single Loop with Row/Column Calculation

For some algorithms, a single loop is useful.

```java
int[][] matrix = new int[3][4];  // 3 rows, 4 columns
int rows = matrix.length;
int cols = matrix[0].length;

for (int i = 0; i < rows * cols; i++) {
    int row = i / cols;
    int col = i % cols;
    matrix[row][col] = i + 1;
}

// Result:
// [1,  2,  3,  4]
// [5,  6,  7,  8]
// [9, 10, 11, 12]
```

---

## Jagged Arrays

In Java, 2D arrays don't have to be rectangular. Each row can have a different length. These are called jagged (or ragged) arrays.

### Creating Jagged Arrays

```java
// Step 1: Create array of rows (without specifying column count)
int[][] jagged = new int[3][];

// Step 2: Create each row with different lengths
jagged[0] = new int[2];    // Row 0 has 2 elements
jagged[1] = new int[4];    // Row 1 has 4 elements
jagged[2] = new int[3];    // Row 2 has 3 elements

// Visualize:
// [0, 0]
// [0, 0, 0, 0]
// [0, 0, 0]
```

### Initializing Jagged Arrays

```java
// Direct initialization
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7, 8, 9}
};

// Triangle pattern
int[][] triangle = {
    {1},
    {1, 2},
    {1, 2, 3},
    {1, 2, 3, 4},
    {1, 2, 3, 4, 5}
};
```

### Iterating Jagged Arrays

Always use `matrix[row].length` for the inner loop, not a fixed column count.

```java
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7, 8, 9}
};

// Safe iteration - uses each row's actual length
for (int row = 0; row < jagged.length; row++) {
    for (int col = 0; col < jagged[row].length; col++) {
        System.out.print(jagged[row][col] + " ");
    }
    System.out.println();
}
// Output:
// 1 2
// 3 4 5 6
// 7 8 9

// For-each works naturally with jagged arrays
for (int[] row : jagged) {
    for (int value : row) {
        System.out.print(value + " ");
    }
    System.out.println();
}
```

### Checking Row Lengths

```java
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7, 8, 9}
};

for (int i = 0; i < jagged.length; i++) {
    System.out.println("Row " + i + " has " + jagged[i].length + " elements");
}
// Output:
// Row 0 has 2 elements
// Row 1 has 4 elements
// Row 2 has 3 elements
```

---

## Common 2D Array Operations

### Printing 2D Arrays

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};

// Using Arrays.deepToString() - shows nested structure
System.out.println(Arrays.deepToString(matrix));
// Output: [[1, 2, 3], [4, 5, 6]]

// Formatted table output
for (int[] row : matrix) {
    for (int val : row) {
        System.out.printf("%4d", val);  // Right-aligned, width 4
    }
    System.out.println();
}
// Output:
//    1   2   3
//    4   5   6
```

### Copying 2D Arrays

2D arrays require deep copying - a shallow copy only copies row references.

```java
int[][] original = {
    {1, 2, 3},
    {4, 5, 6}
};

// WRONG: Shallow copy - rows still reference original
int[][] shallowCopy = original.clone();
shallowCopy[0][0] = 999;
System.out.println(original[0][0]);  // 999 - original is modified!

// CORRECT: Deep copy - copy each row
int[][] deepCopy = new int[original.length][];
for (int i = 0; i < original.length; i++) {
    deepCopy[i] = Arrays.copyOf(original[i], original[i].length);
}
deepCopy[0][0] = 999;
System.out.println(original[0][0]);  // 1 - original unchanged

// Using streams (Java 8+)
int[][] streamCopy = Arrays.stream(original)
    .map(row -> Arrays.copyOf(row, row.length))
    .toArray(int[][]::new);
```

### Comparing 2D Arrays

```java
int[][] a = {{1, 2}, {3, 4}};
int[][] b = {{1, 2}, {3, 4}};
int[][] c = {{1, 2}, {3, 5}};

// Arrays.equals() doesn't work for 2D arrays
System.out.println(Arrays.equals(a, b));      // false (compares row references)

// Use Arrays.deepEquals() for multi-dimensional arrays
System.out.println(Arrays.deepEquals(a, b));  // true
System.out.println(Arrays.deepEquals(a, c));  // false
```

### Finding Sum of All Elements

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Using loops
int sum = 0;
for (int[] row : matrix) {
    for (int val : row) {
        sum += val;
    }
}
System.out.println("Sum: " + sum);  // 45

// Using streams
int streamSum = Arrays.stream(matrix)
    .flatMapToInt(Arrays::stream)
    .sum();
System.out.println("Sum: " + streamSum);  // 45
```

### Finding Maximum and Minimum

```java
int[][] matrix = {
    {5, 2, 9},
    {1, 7, 4},
    {8, 3, 6}
};

int max = Integer.MIN_VALUE;
int min = Integer.MAX_VALUE;

for (int[] row : matrix) {
    for (int val : row) {
        if (val > max) max = val;
        if (val < min) min = val;
    }
}
System.out.println("Max: " + max + ", Min: " + min);  // Max: 9, Min: 1

// Using streams
int streamMax = Arrays.stream(matrix)
    .flatMapToInt(Arrays::stream)
    .max().orElse(0);

int streamMin = Arrays.stream(matrix)
    .flatMapToInt(Arrays::stream)
    .min().orElse(0);
```

### Row and Column Sums

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Row sums
for (int row = 0; row < matrix.length; row++) {
    int rowSum = 0;
    for (int col = 0; col < matrix[row].length; col++) {
        rowSum += matrix[row][col];
    }
    System.out.println("Row " + row + " sum: " + rowSum);
}
// Row 0 sum: 6
// Row 1 sum: 15
// Row 2 sum: 24

// Column sums (assuming rectangular)
int cols = matrix[0].length;
for (int col = 0; col < cols; col++) {
    int colSum = 0;
    for (int row = 0; row < matrix.length; row++) {
        colSum += matrix[row][col];
    }
    System.out.println("Column " + col + " sum: " + colSum);
}
// Column 0 sum: 12
// Column 1 sum: 15
// Column 2 sum: 18
```

---

## Matrix Operations

### Transpose Matrix

Swap rows and columns (rows become columns, columns become rows).

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};  // 2x3 matrix

int rows = matrix.length;
int cols = matrix[0].length;

// Transpose becomes 3x2
int[][] transpose = new int[cols][rows];

for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        transpose[j][i] = matrix[i][j];
    }
}

// Result:
// [1, 4]
// [2, 5]
// [3, 6]
```

### Matrix Addition

Add two matrices of the same dimensions.

```java
int[][] a = {
    {1, 2, 3},
    {4, 5, 6}
};

int[][] b = {
    {7, 8, 9},
    {10, 11, 12}
};

int rows = a.length;
int cols = a[0].length;
int[][] result = new int[rows][cols];

for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        result[i][j] = a[i][j] + b[i][j];
    }
}

// Result:
// [8, 10, 12]
// [14, 16, 18]
```

### Matrix Multiplication

Multiply two matrices (columns of first must equal rows of second).

```java
int[][] a = {
    {1, 2, 3},
    {4, 5, 6}
};  // 2x3

int[][] b = {
    {7, 8},
    {9, 10},
    {11, 12}
};  // 3x2

// Result will be 2x2
int[][] result = new int[a.length][b[0].length];

for (int i = 0; i < a.length; i++) {
    for (int j = 0; j < b[0].length; j++) {
        for (int k = 0; k < a[0].length; k++) {
            result[i][j] += a[i][k] * b[k][j];
        }
    }
}

// Result:
// [58, 64]
// [139, 154]
```

### Rotate Matrix 90 Degrees Clockwise

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

int n = matrix.length;
int[][] rotated = new int[n][n];

for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        rotated[j][n - 1 - i] = matrix[i][j];
    }
}

// Result:
// [7, 4, 1]
// [8, 5, 2]
// [9, 6, 3]
```

### Diagonal Elements

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

int n = matrix.length;

// Main diagonal (top-left to bottom-right)
System.out.print("Main diagonal: ");
for (int i = 0; i < n; i++) {
    System.out.print(matrix[i][i] + " ");  // 1 5 9
}
System.out.println();

// Anti-diagonal (top-right to bottom-left)
System.out.print("Anti-diagonal: ");
for (int i = 0; i < n; i++) {
    System.out.print(matrix[i][n - 1 - i] + " ");  // 3 5 7
}
System.out.println();

// Sum of diagonals
int mainDiagSum = 0;
int antiDiagSum = 0;
for (int i = 0; i < n; i++) {
    mainDiagSum += matrix[i][i];
    antiDiagSum += matrix[i][n - 1 - i];
}
```

---

## Three-Dimensional Arrays

3D arrays add another dimension - think of it as multiple 2D arrays stacked.

### Creating 3D Arrays

```java
// Syntax: type[][][] name = new type[depth][rows][cols];
int[][][] cube = new int[2][3][4];  // 2 layers, 3 rows, 4 columns

// Initialize with values
int[][][] data = {
    {   // Layer 0
        {1, 2, 3},
        {4, 5, 6}
    },
    {   // Layer 1
        {7, 8, 9},
        {10, 11, 12}
    }
};
```

### Accessing 3D Array Elements

```java
int[][][] cube = {
    {
        {1, 2, 3},
        {4, 5, 6}
    },
    {
        {7, 8, 9},
        {10, 11, 12}
    }
};

// Access element: [layer][row][column]
int value = cube[0][1][2];      // Layer 0, Row 1, Column 2 = 6
int another = cube[1][0][0];    // Layer 1, Row 0, Column 0 = 7

// Dimensions
int layers = cube.length;        // 2
int rows = cube[0].length;       // 2
int cols = cube[0][0].length;    // 3
```

### Iterating 3D Arrays

```java
int[][][] cube = {
    {{1, 2}, {3, 4}},
    {{5, 6}, {7, 8}}
};

// Nested loops
for (int layer = 0; layer < cube.length; layer++) {
    System.out.println("Layer " + layer + ":");
    for (int row = 0; row < cube[layer].length; row++) {
        for (int col = 0; col < cube[layer][row].length; col++) {
            System.out.print(cube[layer][row][col] + " ");
        }
        System.out.println();
    }
    System.out.println();
}

// For-each
for (int[][] layer : cube) {
    for (int[] row : layer) {
        for (int value : row) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
    System.out.println();
}
```

### Printing 3D Arrays

```java
int[][][] cube = {
    {{1, 2}, {3, 4}},
    {{5, 6}, {7, 8}}
};

// Arrays.deepToString() works for any dimension
System.out.println(Arrays.deepToString(cube));
// Output: [[[1, 2], [3, 4]], [[5, 6], [7, 8]]]
```

---

## Practical Examples

### Tic-Tac-Toe Board

```java
char[][] board = {
    {' ', ' ', ' '},
    {' ', ' ', ' '},
    {' ', ' ', ' '}
};

// Make moves
board[0][0] = 'X';
board[1][1] = 'O';
board[2][2] = 'X';

// Print board
System.out.println("-------------");
for (char[] row : board) {
    System.out.print("| ");
    for (char cell : row) {
        System.out.print(cell + " | ");
    }
    System.out.println("\n-------------");
}

// Check for winner
public static char checkWinner(char[][] board) {
    // Check rows
    for (int i = 0; i < 3; i++) {
        if (board[i][0] != ' ' && 
            board[i][0] == board[i][1] && 
            board[i][1] == board[i][2]) {
            return board[i][0];
        }
    }
    
    // Check columns
    for (int i = 0; i < 3; i++) {
        if (board[0][i] != ' ' && 
            board[0][i] == board[1][i] && 
            board[1][i] == board[2][i]) {
            return board[0][i];
        }
    }
    
    // Check diagonals
    if (board[0][0] != ' ' && 
        board[0][0] == board[1][1] && 
        board[1][1] == board[2][2]) {
        return board[0][0];
    }
    if (board[0][2] != ' ' && 
        board[0][2] == board[1][1] && 
        board[1][1] == board[2][0]) {
        return board[0][2];
    }
    
    return ' ';  // No winner
}
```

### Image as 2D Array (Grayscale)

```java
// Grayscale image: values 0-255
int[][] image = {
    {100, 150, 200},
    {50, 100, 150},
    {0, 50, 100}
};

// Invert image
int[][] inverted = new int[image.length][image[0].length];
for (int i = 0; i < image.length; i++) {
    for (int j = 0; j < image[i].length; j++) {
        inverted[i][j] = 255 - image[i][j];
    }
}

// Brighten image
int brightness = 50;
for (int i = 0; i < image.length; i++) {
    for (int j = 0; j < image[i].length; j++) {
        image[i][j] = Math.min(255, image[i][j] + brightness);
    }
}
```

### Spreadsheet Data

```java
// Student grades: rows = students, columns = assignments
double[][] grades = {
    {85.5, 90.0, 78.5, 92.0},  // Student 0
    {75.0, 88.5, 82.0, 79.5},  // Student 1
    {92.5, 95.0, 88.5, 91.0}   // Student 2
};

String[] students = {"Alice", "Bob", "Charlie"};
String[] assignments = {"HW1", "HW2", "HW3", "HW4"};

// Calculate each student's average
for (int i = 0; i < grades.length; i++) {
    double sum = 0;
    for (double grade : grades[i]) {
        sum += grade;
    }
    double avg = sum / grades[i].length;
    System.out.printf("%s: %.2f%n", students[i], avg);
}

// Calculate class average for each assignment
for (int j = 0; j < assignments.length; j++) {
    double sum = 0;
    for (int i = 0; i < grades.length; i++) {
        sum += grades[i][j];
    }
    double avg = sum / grades.length;
    System.out.printf("%s class average: %.2f%n", assignments[j], avg);
}
```

### Maze Representation

```java
// 0 = path, 1 = wall
int[][] maze = {
    {0, 1, 0, 0, 0},
    {0, 1, 0, 1, 0},
    {0, 0, 0, 1, 0},
    {1, 1, 0, 0, 0},
    {0, 0, 0, 1, 0}
};

// Print maze
for (int[] row : maze) {
    for (int cell : row) {
        System.out.print(cell == 1 ? "#" : ".");
    }
    System.out.println();
}
// Output:
// .#...
// .#.#.
// ...#.
// ##...
// ...#.

// Check if position is valid
public static boolean isValid(int[][] maze, int row, int col) {
    return row >= 0 && row < maze.length &&
           col >= 0 && col < maze[0].length &&
           maze[row][col] == 0;
}
```

---

## Common Mistakes

### 1. ArrayIndexOutOfBoundsException

```java
int[][] matrix = new int[3][4];  // 3 rows, 4 columns

// Wrong: accessing non-existent row
matrix[3][0] = 1;  // Exception! (valid rows: 0, 1, 2)

// Wrong: accessing non-existent column
matrix[0][4] = 1;  // Exception! (valid columns: 0, 1, 2, 3)

// Correct bounds checking
if (row >= 0 && row < matrix.length && 
    col >= 0 && col < matrix[row].length) {
    matrix[row][col] = 1;
}
```

### 2. Shallow Copy vs Deep Copy

```java
int[][] original = {{1, 2}, {3, 4}};

// Wrong: shallow copy
int[][] shallow = original.clone();
shallow[0][0] = 999;
System.out.println(original[0][0]);  // 999 - modified!

// Correct: deep copy
int[][] deep = new int[original.length][];
for (int i = 0; i < original.length; i++) {
    deep[i] = original[i].clone();
}
```

### 3. Assuming Rectangular Array

```java
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7, 8, 9}
};

// Wrong: assumes all rows have same length
int cols = jagged[0].length;
for (int i = 0; i < jagged.length; i++) {
    for (int j = 0; j < cols; j++) {  // May cause exception!
        System.out.print(jagged[i][j]);
    }
}

// Correct: use each row's length
for (int i = 0; i < jagged.length; i++) {
    for (int j = 0; j < jagged[i].length; j++) {
        System.out.print(jagged[i][j]);
    }
}
```

### 4. Using Arrays.equals() Instead of deepEquals()

```java
int[][] a = {{1, 2}, {3, 4}};
int[][] b = {{1, 2}, {3, 4}};

// Wrong for 2D arrays
System.out.println(Arrays.equals(a, b));      // false

// Correct
System.out.println(Arrays.deepEquals(a, b));  // true
```

### 5. Null Rows in Jagged Arrays

```java
int[][] jagged = new int[3][];  // Rows are null!

// Wrong: NullPointerException
jagged[0][0] = 1;  // jagged[0] is null!

// Correct: initialize each row first
jagged[0] = new int[3];
jagged[0][0] = 1;  // OK now
```

---

## Summary

| Operation | Syntax/Method |
|-----------|---------------|
| Declare 2D | `int[][] matrix;` |
| Create 2D | `new int[rows][cols]` or `{{1,2},{3,4}}` |
| Access | `matrix[row][col]` |
| Rows count | `matrix.length` |
| Columns count | `matrix[row].length` |
| Print | `Arrays.deepToString(matrix)` |
| Compare | `Arrays.deepEquals(a, b)` |
| Create jagged | `new int[rows][]` then init each row |
| Declare 3D | `int[][][] cube;` |
| Create 3D | `new int[depth][rows][cols]` |

**Key Points:**
- 2D arrays are arrays of arrays
- Rows can have different lengths (jagged arrays)
- Use `matrix[row].length` for each row's column count
- Use `Arrays.deepToString()` to print multi-dimensional arrays
- Use `Arrays.deepEquals()` to compare multi-dimensional arrays
- Clone creates shallow copies - manually copy for deep copy
- Always check bounds before accessing elements

---

[<- Back: Arrays](./04-arrays.md) | [Back to Guide](../guide.md) | [Next: Strings ->](./06-strings.md)
