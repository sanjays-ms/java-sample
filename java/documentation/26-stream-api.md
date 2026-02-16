# Stream API

[Back to Guide](../guide.md) | [Cheatsheet](../cheatsheets/stream-api-cheatsheet.md)

---

## What Is a Stream?

A stream is a sequence of elements that supports operations to process data in a declarative way. Streams do not store data - they carry data from a source (like a collection) through a pipeline of operations.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Traditional approach - imperative (you describe HOW to do it)
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.length() > 3) {
        result.add(name.toUpperCase());
    }
}

// Stream approach - declarative (you describe WHAT you want)
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .toList();
// [ALICE, CHARLIE, DAVID]
```

**In plain words:** A stream is like a conveyor belt in a factory. Data goes in one end, passes through various stations (operations like filter, map, sort), and comes out the other end transformed or filtered. You describe what you want to happen at each station, not how to move items between stations.

### Stream vs Collection

| Aspect | Collection | Stream |
|--------|------------|--------|
| Purpose | Store data | Process data |
| Data storage | Yes | No (just a view) |
| Reusable | Yes | No (single use) |
| Size | Finite | Can be infinite |
| Evaluation | Eager | Lazy |
| Modification | Can modify | Cannot modify source |

---

## Why Streams Exist

### Problem 1: Verbose Collection Processing

Before streams, processing collections required lots of code:

```java
// Find average age of adults - traditional
int sum = 0;
int count = 0;
for (Person person : people) {
    if (person.getAge() >= 18) {
        sum += person.getAge();
        count++;
    }
}
double average = count > 0 ? (double) sum / count : 0;

// With streams - concise
double average = people.stream()
    .filter(p -> p.getAge() >= 18)
    .mapToInt(Person::getAge)
    .average()
    .orElse(0);
```

### Problem 2: Parallelism Was Hard

Making code parallel-safe was complex:

```java
// Parallel processing - traditional (complex, error-prone)
ExecutorService executor = Executors.newFixedThreadPool(4);
List<Future<Result>> futures = new ArrayList<>();
for (Data data : dataList) {
    futures.add(executor.submit(() -> process(data)));
}
// Collect results, handle exceptions, shutdown executor...

// With streams - simple
List<Result> results = dataList.parallelStream()
    .map(this::process)
    .toList();
```

### Problem 3: Mixing Concerns

Traditional loops mixed iteration logic with business logic:

```java
// Everything mixed together
for (int i = 0; i < orders.size(); i++) {
    Order order = orders.get(i);
    if (order.getStatus() == Status.PENDING) {
        double total = 0;
        for (Item item : order.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        if (total > 100) {
            order.applyDiscount(0.1);
            processedOrders.add(order);
        }
    }
}

// Streams separate concerns
List<Order> processedOrders = orders.stream()
    .filter(order -> order.getStatus() == Status.PENDING)
    .filter(order -> order.getTotal() > 100)
    .peek(order -> order.applyDiscount(0.1))
    .toList();
```

---

## Stream Characteristics

### Streams Don't Store Data

```java
List<String> names = List.of("Alice", "Bob");

Stream<String> stream = names.stream();
// The stream doesn't copy the list - it provides a view

names = null;  // Original list can be garbage collected
// But stream still references the data!
```

### Streams Are Lazy

Operations aren't executed until a terminal operation is called:

```java
Stream<String> stream = names.stream()
    .filter(name -> {
        System.out.println("Filtering: " + name);
        return name.length() > 3;
    })
    .map(name -> {
        System.out.println("Mapping: " + name);
        return name.toUpperCase();
    });

// Nothing printed yet! Stream is lazy.

List<String> result = stream.toList();  // NOW operations execute
```

### Streams Are Consumable

A stream can only be used once:

```java
Stream<String> stream = names.stream();

stream.forEach(System.out::println);  // OK

stream.forEach(System.out::println);  // Error! Stream already consumed
// IllegalStateException: stream has already been operated upon or closed
```

### Streams Can Be Infinite

```java
// Infinite stream of random numbers
Stream<Double> randoms = Stream.generate(Math::random);

// Infinite stream of integers
Stream<Integer> integers = Stream.iterate(0, n -> n + 1);

// Must limit before collecting!
List<Integer> first10 = integers.limit(10).toList();
```

---

## Creating Streams

### From Collections

```java
List<String> list = List.of("a", "b", "c");
Stream<String> stream = list.stream();

Set<Integer> set = Set.of(1, 2, 3);
Stream<Integer> setStream = set.stream();

// Parallel stream
Stream<String> parallel = list.parallelStream();
```

### From Arrays

```java
String[] array = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(array);

// Partial array
Stream<String> partial = Arrays.stream(array, 1, 3);  // ["b", "c"]

// Primitive arrays
int[] numbers = {1, 2, 3, 4, 5};
IntStream intStream = Arrays.stream(numbers);
```

### From Values

```java
Stream<String> stream = Stream.of("a", "b", "c");

Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);

// Empty stream
Stream<String> empty = Stream.empty();

// Single element
Stream<String> single = Stream.of("only one");

// Nullable element
String value = null;
Stream<String> nullable = Stream.ofNullable(value);  // Empty stream
```

### Generated Streams

```java
// Generate with Supplier (infinite) - creates elements on demand
Stream<Double> randoms = Stream.generate(Math::random);
Stream<String> constants = Stream.generate(() -> "Hello");
Stream<UUID> uuids = Stream.generate(UUID::randomUUID);

// Iterate with seed and UnaryOperator (infinite)
// First argument is the starting value, second is how to get the next value
Stream<Integer> counting = Stream.iterate(0, n -> n + 1);  // 0, 1, 2, 3...
Stream<Integer> evens = Stream.iterate(0, n -> n + 2);     // 0, 2, 4, 6...
Stream<Integer> powers = Stream.iterate(1, n -> n * 2);    // 1, 2, 4, 8, 16...

// Iterate with predicate (Java 9+) - finite, stops when predicate is false
// Similar to a for loop: for(int n = 0; n < 10; n++)
Stream<Integer> limited = Stream.iterate(0, n -> n < 10, n -> n + 1);
// 0, 1, 2, 3, 4, 5, 6, 7, 8, 9

// Fibonacci sequence example
Stream<long[]> fibonacci = Stream.iterate(
    new long[]{0, 1},
    arr -> new long[]{arr[1], arr[0] + arr[1]}
);
List<Long> first10Fib = fibonacci
    .limit(10)
    .map(arr -> arr[0])
    .toList();
// [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
```

### Using Stream.Builder

For building streams imperatively when you don't know all elements upfront:

```java
Stream.Builder<String> builder = Stream.builder();
builder.add("one");
builder.add("two");
builder.add("three");
Stream<String> stream = builder.build();
// ["one", "two", "three"]

// Fluent style
Stream<String> stream2 = Stream.<String>builder()
    .add("a")
    .add("b")
    .add("c")
    .build();

// Conditional building
Stream.Builder<String> builder2 = Stream.builder();
for (String item : someCollection) {
    if (someCondition(item)) {
        builder2.add(item);
    }
}
Stream<String> filtered = builder2.build();
```

### From Other Sources

```java
// From String - returns IntStream of character codes
IntStream chars = "Hello".chars();  
chars.forEach(c -> System.out.print((char) c + " "));  // H e l l o

// From String using codePoints (handles Unicode better)
IntStream codePoints = "Hello".codePoints();

// From BufferedReader
try (BufferedReader reader = Files.newBufferedReader(path)) {
    Stream<String> lines = reader.lines();
    lines.forEach(System.out::println);
}

// From Files - MUST be closed (use try-with-resources)
try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
    long lineCount = lines.count();
}

// From Files with charset
try (Stream<String> lines = Files.lines(Path.of("file.txt"), StandardCharsets.UTF_8)) {
    lines.filter(line -> !line.isEmpty()).forEach(System.out::println);
}

// List files in directory
try (Stream<Path> paths = Files.list(Path.of("/some/directory"))) {
    paths.filter(Files::isRegularFile).forEach(System.out::println);
}

// Walk directory tree recursively
try (Stream<Path> paths = Files.walk(Path.of("/some/directory"))) {
    List<Path> javaFiles = paths
        .filter(p -> p.toString().endsWith(".java"))
        .toList();
}

// Find files matching pattern
try (Stream<Path> paths = Files.find(
        Path.of("/some/directory"),
        10,  // max depth
        (path, attrs) -> attrs.isRegularFile() && path.toString().endsWith(".txt")
)) {
    paths.forEach(System.out::println);
}

// From Pattern - split string into stream
Pattern pattern = Pattern.compile(",");
Stream<String> parts = pattern.splitAsStream("a,b,c,d");
// ["a", "b", "c", "d"]

// From Pattern with regex
Pattern wordPattern = Pattern.compile("\\s+");
Stream<String> words = wordPattern.splitAsStream("Hello World Java Streams");
// ["Hello", "World", "Java", "Streams"]

// From Random - generate random numbers
Random random = new Random();
IntStream randomInts = random.ints(10, 0, 100);      // 10 random ints 0-99
LongStream randomLongs = random.longs(5);             // 5 random longs
DoubleStream randomDoubles = random.doubles(10, 0, 1); // 10 doubles 0.0-1.0

// Infinite random streams (must limit!)
IntStream infiniteInts = random.ints(0, 100);  // Infinite!
List<Integer> tenRandoms = infiniteInts.limit(10).boxed().toList();

// Range of numbers
IntStream range = IntStream.range(0, 10);           // 0-9 (exclusive end)
IntStream rangeClosed = IntStream.rangeClosed(1, 10);  // 1-10 (inclusive)
LongStream longRange = LongStream.range(0, 1_000_000L);

// From Optional
Optional<String> opt = Optional.of("value");
Stream<String> optStream = opt.stream();  // Stream with one element (Java 9+)

Optional<String> empty = Optional.empty();
Stream<String> emptyStream = empty.stream();  // Empty stream

// From Map entries
Map<String, Integer> map = Map.of("a", 1, "b", 2);
Stream<Map.Entry<String, Integer>> entries = map.entrySet().stream();
Stream<String> keys = map.keySet().stream();
Stream<Integer> values = map.values().stream();

// From CharSequence (Java 11+)
Stream<String> lineStream = "line1\nline2\nline3".lines();
```

### Concatenating Streams

```java
Stream<String> stream1 = Stream.of("a", "b");
Stream<String> stream2 = Stream.of("c", "d");

// Concat two streams
Stream<String> combined = Stream.concat(stream1, stream2);
// ["a", "b", "c", "d"]

// Concat multiple streams - use flatMap
Stream<String> s1 = Stream.of("a");
Stream<String> s2 = Stream.of("b");
Stream<String> s3 = Stream.of("c");

Stream<String> all = Stream.of(s1, s2, s3)
    .flatMap(Function.identity());
// ["a", "b", "c"]

// Alternative: reduce with concat (less efficient for many streams)
Stream<String> all2 = Stream.of(
    Stream.of("a", "b"),
    Stream.of("c", "d"),
    Stream.of("e", "f")
).reduce(Stream.empty(), Stream::concat);
```

---

## Stream Pipeline

A stream pipeline consists of three parts:

1. **Source** - where data comes from (collection, array, generator, etc.)
2. **Intermediate operations** - transform the stream (lazy, return new stream)
3. **Terminal operation** - produces a result or side-effect (triggers execution)

```java
List<String> result = names.stream()      // Source
    .filter(n -> n.length() > 3)          // Intermediate (keeps names longer than 3)
    .map(String::toUpperCase)             // Intermediate (converts to uppercase)
    .sorted()                             // Intermediate (sorts alphabetically)
    .toList();                            // Terminal (collects to list)
```

### How the Pipeline Works

```java
// This demonstrates lazy evaluation
List<String> names = List.of("Alice", "Bob", "Charlie");

Stream<String> stream = names.stream()
    .filter(name -> {
        System.out.println("Filtering: " + name);
        return name.length() > 3;
    })
    .map(name -> {
        System.out.println("Mapping: " + name);
        return name.toUpperCase();
    });

System.out.println("Stream created, nothing executed yet!");

// Only NOW do the operations execute
List<String> result = stream.toList();
// Output:
// Stream created, nothing executed yet!
// Filtering: Alice
// Mapping: Alice
// Filtering: Bob
// Filtering: Charlie
// Mapping: Charlie
```

Notice: Elements are processed one at a time through the entire pipeline, not in batches.

---

## Intermediate Operations

Intermediate operations return a new stream and are **lazy** (not executed until a terminal operation is called).

### filter - Keep Matching Elements

The `filter` operation keeps only elements that match a condition (predicate).

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Keep only even numbers
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .toList();
// [2, 4, 6, 8, 10]

// Keep only positive numbers
List<Integer> positive = numbers.stream()
    .filter(n -> n > 0)
    .toList();

// Keep non-null values
List<String> nonNull = strings.stream()
    .filter(Objects::nonNull)
    .toList();

// Keep non-empty strings
List<String> nonEmpty = strings.stream()
    .filter(s -> s != null && !s.isEmpty())
    .toList();

// Using method reference with Predicate.not (Java 11+)
List<String> nonEmpty2 = strings.stream()
    .filter(Predicate.not(String::isEmpty))
    .toList();

// Multiple filters (equivalent to AND)
// These two are equivalent:
List<Integer> result = numbers.stream()
    .filter(n -> n > 3)
    .filter(n -> n < 8)
    .toList();
// [4, 5, 6, 7]

List<Integer> result2 = numbers.stream()
    .filter(n -> n > 3 && n < 8)
    .toList();
// [4, 5, 6, 7]

// Filter with complex objects
List<Person> adults = people.stream()
    .filter(person -> person.getAge() >= 18)
    .toList();

// Filter with multiple conditions
List<Person> eligibleVoters = people.stream()
    .filter(p -> p.getAge() >= 18)
    .filter(p -> p.getCitizenship().equals("US"))
    .filter(Person::isRegistered)
    .toList();
```

### map - Transform Elements

The `map` operation transforms each element into something else.

```java
List<String> names = List.of("alice", "bob", "charlie");

// Transform to uppercase
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .toList();
// [ALICE, BOB, CHARLIE]

// Transform to lowercase and trim
List<String> cleaned = names.stream()
    .map(String::toLowerCase)
    .map(String::trim)
    .toList();

// Transform to different type (String -> Integer)
List<Integer> lengths = names.stream()
    .map(String::length)
    .toList();
// [5, 3, 7]

// Transform with custom logic
List<String> formatted = names.stream()
    .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
    .toList();
// [Alice, Bob, Charlie]

// Extract property from objects
List<String> emails = people.stream()
    .map(Person::getEmail)
    .toList();

// Chain multiple maps
List<String> result = people.stream()
    .map(Person::getName)        // Person -> String
    .map(String::toUpperCase)    // String -> String
    .map(s -> s + "!")           // String -> String
    .toList();

// Map to wrapper object
List<PersonDTO> dtos = people.stream()
    .map(person -> new PersonDTO(
        person.getId(),
        person.getName(),
        person.getEmail()
    ))
    .toList();

// Null-safe mapping
List<String> safeEmails = people.stream()
    .map(Person::getEmail)
    .map(email -> email != null ? email : "no-email@example.com")
    .toList();
```

### mapToInt, mapToLong, mapToDouble - Transform to Primitives

These operations convert to primitive streams, avoiding boxing overhead and enabling primitive-specific operations like `sum()`.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// mapToInt - returns IntStream
IntStream lengths = names.stream()
    .mapToInt(String::length);

// Now we can use IntStream-specific methods
int totalLength = names.stream()
    .mapToInt(String::length)
    .sum();  // 15

int maxLength = names.stream()
    .mapToInt(String::length)
    .max()
    .orElse(0);  // 7

// mapToDouble - returns DoubleStream
List<Product> products = getProducts();

double totalPrice = products.stream()
    .mapToDouble(Product::getPrice)
    .sum();

double averagePrice = products.stream()
    .mapToDouble(Product::getPrice)
    .average()
    .orElse(0.0);

// mapToLong - returns LongStream
long totalBytes = files.stream()
    .mapToLong(File::length)
    .sum();

// Get statistics in one pass
IntSummaryStatistics stats = names.stream()
    .mapToInt(String::length)
    .summaryStatistics();

System.out.println("Count: " + stats.getCount());      // 3
System.out.println("Sum: " + stats.getSum());          // 15
System.out.println("Min: " + stats.getMin());          // 3
System.out.println("Max: " + stats.getMax());          // 7
System.out.println("Average: " + stats.getAverage());  // 5.0

// Why use primitive streams?
// Bad - boxing overhead (Integer objects created)
Integer sum1 = numbers.stream()
    .map(n -> n * 2)
    .reduce(0, Integer::sum);

// Good - no boxing (works with primitive int)
int sum2 = numbers.stream()
    .mapToInt(n -> n * 2)
    .sum();
```

// mapToLong
long total = orders.stream()
    .mapToLong(Order::getId)
    .sum();
```

### flatMap - Flatten Nested Structures

The `flatMap` operation is used when each element maps to multiple elements (or a stream of elements). It "flattens" the result into a single stream.

**When to use flatMap vs map:**
- Use `map` when: one input → one output
- Use `flatMap` when: one input → multiple outputs (or a stream)

```java
// Problem: List of lists - we want all elements in one list
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5),
    List.of(6, 7, 8, 9)
);

// Using map would give us Stream<List<Integer>> - not what we want!
// Using flatMap flattens it to Stream<Integer>
List<Integer> flat = nested.stream()
    .flatMap(List::stream)      // Each List becomes a Stream, then flattened
    .toList();
// [1, 2, 3, 4, 5, 6, 7, 8, 9]

// Split strings into words
List<String> sentences = List.of("Hello World", "Java Streams", "Are Awesome");
List<String> allWords = sentences.stream()
    .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
    .toList();
// [Hello, World, Java, Streams, Are, Awesome]

// Get all characters from all strings
List<String> names = List.of("Alice", "Bob");
List<Character> allChars = names.stream()
    .flatMap(name -> name.chars().mapToObj(c -> (char) c))
    .toList();
// [A, l, i, c, e, B, o, b]

// Get all items from all orders
List<Order> orders = getOrders();
List<Item> allItems = orders.stream()
    .flatMap(order -> order.getItems().stream())
    .toList();

// Get all tags from all posts (with duplicates removed)
Set<String> allTags = posts.stream()
    .flatMap(post -> post.getTags().stream())
    .collect(Collectors.toSet());

// FlatMap with Optional (unwrap optionals)
List<Optional<String>> optionals = List.of(
    Optional.of("a"),
    Optional.empty(),
    Optional.of("b")
);
List<String> values = optionals.stream()
    .flatMap(Optional::stream)  // Java 9+
    .toList();
// [a, b]

// Alternative for older Java versions
List<String> values2 = optionals.stream()
    .filter(Optional::isPresent)
    .map(Optional::get)
    .toList();

// Combine multiple operations with flatMap
List<Department> departments = getDepartments();
List<String> allEmployeeEmails = departments.stream()
    .flatMap(dept -> dept.getEmployees().stream())
    .map(Employee::getEmail)
    .filter(Objects::nonNull)
    .distinct()
    .toList();
```

### flatMapToInt, flatMapToLong, flatMapToDouble

Flatten to primitive streams for better performance:

```java
// Sum all numbers from nested lists
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5),
    List.of(6, 7, 8, 9)
);

int sum = nested.stream()
    .flatMapToInt(list -> list.stream().mapToInt(Integer::intValue))
    .sum();
// 45

// Average of all prices from all orders
double avgPrice = orders.stream()
    .flatMapToDouble(order -> 
        order.getItems().stream().mapToDouble(Item::getPrice))
    .average()
    .orElse(0.0);
```

### distinct - Remove Duplicates

Removes duplicate elements using `equals()` method.

```java
List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);

List<Integer> unique = numbers.stream()
    .distinct()
    .toList();
// [1, 2, 3, 4]

// With strings
List<String> words = List.of("apple", "banana", "apple", "cherry", "banana");
List<String> uniqueWords = words.stream()
    .distinct()
    .toList();
// [apple, banana, cherry]

// Distinct values from object property
List<String> uniqueCities = people.stream()
    .map(Person::getCity)
    .distinct()
    .toList();

// Distinct objects (uses equals() of the object)
// Make sure your class has proper equals() implementation!
List<Person> uniquePeople = people.stream()
    .distinct()
    .toList();

// Distinct by specific property (workaround - use map to key, then collect)
Map<String, Person> uniqueByEmail = people.stream()
    .collect(Collectors.toMap(
        Person::getEmail,
        Function.identity(),
        (existing, replacement) -> existing  // Keep first on duplicate
    ));
List<Person> uniqueByEmailList = new ArrayList<>(uniqueByEmail.values());

// Note: distinct() maintains encounter order for ordered streams
List<Integer> ordered = List.of(3, 1, 2, 1, 3, 2);
List<Integer> distinctOrdered = ordered.stream()
    .distinct()
    .toList();
// [3, 1, 2] - first occurrence order preserved
```

### sorted - Sort Elements

Sort elements either in natural order or using a custom comparator.

```java
List<String> names = List.of("Charlie", "Alice", "Bob", "David");

// Natural order (uses Comparable interface)
List<String> sorted = names.stream()
    .sorted()
    .toList();
// [Alice, Bob, Charlie, David]

// Reverse natural order
List<String> reversed = names.stream()
    .sorted(Comparator.reverseOrder())
    .toList();
// [David, Charlie, Bob, Alice]

// Custom comparator - by length
List<String> byLength = names.stream()
    .sorted((a, b) -> a.length() - b.length())
    .toList();
// [Bob, Alice, David, Charlie]

// Using Comparator.comparing - cleaner syntax
List<String> byLength2 = names.stream()
    .sorted(Comparator.comparing(String::length))
    .toList();

// Comparing with reversed
List<String> byLengthDesc = names.stream()
    .sorted(Comparator.comparingInt(String::length).reversed())
    .toList();
// [Charlie, Alice, David, Bob]

// Sort numbers
List<Integer> numbers = List.of(5, 2, 8, 1, 9, 3);
List<Integer> sortedNumbers = numbers.stream()
    .sorted()
    .toList();
// [1, 2, 3, 5, 8, 9]

// Sort objects by property
List<Person> byName = people.stream()
    .sorted(Comparator.comparing(Person::getName))
    .toList();

List<Person> byAge = people.stream()
    .sorted(Comparator.comparingInt(Person::getAge))
    .toList();

// Multiple criteria - first by last name, then by first name, then by age
List<Person> sortedPeople = people.stream()
    .sorted(Comparator
        .comparing(Person::getLastName)
        .thenComparing(Person::getFirstName)
        .thenComparingInt(Person::getAge))
    .toList();

// Handle nulls in sorting
List<String> withNulls = Arrays.asList("Bob", null, "Alice", null, "Charlie");
List<String> sortedWithNulls = withNulls.stream()
    .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
    .toList();
// [null, null, Alice, Bob, Charlie]

List<String> sortedNullsLast = withNulls.stream()
    .sorted(Comparator.nullsLast(Comparator.naturalOrder()))
    .toList();
// [Alice, Bob, Charlie, null, null]

// Sort by nullable property
List<Person> byEmailNullsLast = people.stream()
    .sorted(Comparator.comparing(
        Person::getEmail, 
        Comparator.nullsLast(Comparator.naturalOrder())
    ))
    .toList();

// Case-insensitive sorting
List<String> caseInsensitive = names.stream()
    .sorted(String.CASE_INSENSITIVE_ORDER)
    .toList();
```

### limit - Take First N Elements

Truncates the stream to contain at most N elements. This is a short-circuiting operation.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Take first 5 elements
List<Integer> firstFive = numbers.stream()
    .limit(5)
    .toList();
// [1, 2, 3, 4, 5]

// Take first 3 after filtering
List<Integer> firstThreeEvens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .limit(3)
    .toList();
// [2, 4, 6]

// CRITICAL for infinite streams - without limit, it runs forever!
List<Double> fiveRandoms = Stream.generate(Math::random)
    .limit(5)
    .toList();
// [0.123..., 0.456..., 0.789..., 0.234..., 0.567...]

// First 10 fibonacci numbers
List<Long> first10Fib = Stream.iterate(
        new long[]{0, 1},
        arr -> new long[]{arr[1], arr[0] + arr[1]}
    )
    .limit(10)
    .map(arr -> arr[0])
    .toList();
// [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

// Top N pattern - get top 5 highest salaries
List<Employee> top5Paid = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
    .limit(5)
    .toList();

// Limit on empty or small streams - returns what's available
List<Integer> small = List.of(1, 2);
List<Integer> limited = small.stream()
    .limit(10)  // Only 2 elements available
    .toList();
// [1, 2]
```

### skip - Skip First N Elements

Discards the first N elements and returns the rest.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Skip first 3 elements
List<Integer> afterThree = numbers.stream()
    .skip(3)
    .toList();
// [4, 5, 6, 7, 8, 9, 10]

// Skip 0 elements - returns all
List<Integer> skipNone = numbers.stream()
    .skip(0)
    .toList();
// [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

// Skip more than available - returns empty
List<Integer> skipAll = numbers.stream()
    .skip(100)
    .toList();
// []

// Pagination pattern - page 2 with 5 items per page
int page = 2;
int pageSize = 5;
List<Item> pageItems = items.stream()
    .skip((long) (page - 1) * pageSize)  // Skip items from previous pages
    .limit(pageSize)                      // Take only pageSize items
    .toList();

// Skip and limit combined for window
List<Integer> window = numbers.stream()
    .skip(3)   // Start at index 3
    .limit(4)  // Take 4 elements
    .toList();
// [4, 5, 6, 7]

// Skip first few lines of a file
try (Stream<String> lines = Files.lines(Path.of("file.txt"))) {
    List<String> afterHeader = lines
        .skip(1)  // Skip header line
        .toList();
}
```

### takeWhile - Take While Condition Is True (Java 9+)

Takes elements from the start of the stream while the condition is true. Stops at the first element that doesn't match.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);

// Take elements while less than 4
List<Integer> taken = numbers.stream()
    .takeWhile(n -> n < 4)
    .toList();
// [1, 2, 3] - STOPS at first element >= 4 (which is 4)

// IMPORTANT: Different from filter!
// takeWhile stops at first non-match
// filter checks ALL elements
List<Integer> filtered = numbers.stream()
    .filter(n -> n < 4)
    .toList();
// [1, 2, 3, 1, 2, 3] - checks ALL elements, keeps all that match
// [1, 2, 3, 1, 2, 3] - checks ALL elements, keeps all that match

// Useful with sorted data
List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> lessThan5 = sorted.stream()
    .takeWhile(n -> n < 5)
    .toList();
// [1, 2, 3, 4]

// Efficient for early termination with sorted/ordered data
List<Transaction> recentTransactions = transactions.stream()
    .sorted(Comparator.comparing(Transaction::getDate).reversed())
    .takeWhile(t -> t.getDate().isAfter(cutoffDate))
    .toList();
```

### dropWhile - Drop While Condition Is True (Java 9+)

Drops elements from the start of the stream while the condition is true. Returns remaining elements.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);

// Drop elements while less than 4
List<Integer> dropped = numbers.stream()
    .dropWhile(n -> n < 4)
    .toList();
// [4, 5, 1, 2, 3] - drops until first n >= 4, keeps the rest

// Notice: 1, 2, 3 at the end are KEPT (condition only checked at start)

// Useful with sorted data - get elements >= 5
List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> fiveAndUp = sorted.stream()
    .dropWhile(n -> n < 5)
    .toList();
// [5, 6, 7, 8, 9, 10]

// Combine takeWhile and dropWhile for range
// Get elements where 3 <= n <= 7
List<Integer> range = sorted.stream()
    .dropWhile(n -> n < 3)   // Drop 1, 2
    .takeWhile(n -> n <= 7)  // Take until > 7
    .toList();
// [3, 4, 5, 6, 7]
```

### peek - Perform Action Without Modifying

Performs an action on each element without changing the stream. Primarily for debugging.

```java
// Useful for debugging - see what's flowing through the pipeline
List<String> result = names.stream()
    .peek(name -> System.out.println("Original: " + name))
    .filter(name -> name.length() > 3)
    .peek(name -> System.out.println("After filter: " + name))
    .map(String::toUpperCase)
    .peek(name -> System.out.println("After map: " + name))
    .toList();

// Output:
// Original: Alice
// After filter: Alice
// After map: ALICE
// Original: Bob
// Original: Charlie
// After filter: Charlie
// After map: CHARLIE

// WARNING: peek is for debugging only!
// Don't use it for production side effects because:
// 1. It won't execute if there's no terminal operation
// 2. In parallel streams, order is unpredictable
// 3. Some streams may not call peek on all elements

// BAD - side effect in peek
List<String> collected = new ArrayList<>();
names.stream()
    .peek(collected::add)  // Don't do this!
    .count();  // Might not trigger peek in all implementations

// GOOD - use forEach or collect for side effects
names.forEach(collected::add);
```

### boxed - Convert Primitives to Objects

Converts a primitive stream (IntStream, LongStream, DoubleStream) to an object stream.

```java
IntStream intStream = IntStream.of(1, 2, 3, 4, 5);

// Convert IntStream to Stream<Integer>
Stream<Integer> boxed = intStream.boxed();

// Common use: collect primitive stream to List
List<Integer> list = IntStream.range(0, 5)
    .boxed()
    .toList();
// [0, 1, 2, 3, 4]

// Required because toList() etc. work on object streams
List<Long> longs = LongStream.rangeClosed(1, 5)
    .boxed()
    .toList();
// [1, 2, 3, 4, 5]

// Convert to different collection types
Set<Integer> set = IntStream.range(0, 10)
    .boxed()
    .collect(Collectors.toSet());
```

### mapMulti - One-to-Many Mapping (Java 16+)

A more efficient alternative to flatMap when each element produces 0, 1, or a small fixed number of results.

```java
List<Integer> numbers = List.of(1, 2, 3);

// Each number becomes itself and its double
List<Integer> result = numbers.stream()
    .<Integer>mapMulti((num, consumer) -> {
        consumer.accept(num);          // Original
        consumer.accept(num * 2);      // Double
    })
    .toList();
// [1, 2, 2, 4, 3, 6]

// Filtering and transforming in one operation
List<String> values = objects.stream()
    .<String>mapMulti((obj, consumer) -> {
        if (obj instanceof String s) {
            consumer.accept(s.toUpperCase());
        }
    })
    .toList();

// mapMultiToInt, mapMultiToLong, mapMultiToDouble for primitives
int sum = numbers.stream()
    .mapMultiToInt((num, consumer) -> {
        consumer.accept(num);
        consumer.accept(num * 2);
    })
    .sum();
// 12 (1 + 2 + 2 + 4 + 3 + 6)

// When to use mapMulti vs flatMap:
// - mapMulti: small fixed number of outputs, no intermediate stream created
// - flatMap: outputs come from an existing collection/stream
```

### unordered - Mark Stream as Unordered

Tells the stream that element order doesn't matter, which can improve parallel performance.

```java
// When order doesn't matter, unordered() can speed up parallel operations
Set<String> uniqueNames = names.parallelStream()
    .unordered()  // Order doesn't matter for a set
    .map(String::toUpperCase)
    .collect(Collectors.toSet());

// Useful with distinct() in parallel streams
List<Integer> unique = numbers.parallelStream()
    .unordered()
    .distinct()  // Can be more efficient when unordered
    .toList();
```

### asLongStream / asDoubleStream - Convert Primitive Streams

Convert IntStream to LongStream or DoubleStream.

```java
IntStream intStream = IntStream.range(0, 5);

// Convert to LongStream
LongStream longStream = intStream.asLongStream();

// Convert to DoubleStream
IntStream intStream2 = IntStream.range(0, 5);
DoubleStream doubleStream = intStream2.asDoubleStream();

// Useful when you need the larger range/precision
long bigSum = IntStream.range(0, 1_000_000)
    .asLongStream()  // Avoid integer overflow
    .sum();
```

### mapToObj - Convert Primitives Back to Objects

Available on primitive streams to convert elements back to objects.

```java
// IntStream to Stream<String>
List<String> strings = IntStream.range(0, 5)
    .mapToObj(n -> "Number: " + n)
    .toList();
// [Number: 0, Number: 1, Number: 2, Number: 3, Number: 4]

// Convert characters to strings
List<String> chars = "Hello".chars()
    .mapToObj(c -> String.valueOf((char) c))
    .toList();
// [H, e, l, l, o]
```
        consumer.accept(num * 2);
    })
    .toList();
// [1, 2, 2, 4, 3, 6]
```

---

## Terminal Operations

Terminal operations trigger pipeline execution and produce a result.

**What This Means:**
- Until you call a terminal operation, no processing happens at all.
- Terminal operations "consume" the stream - you cannot use the stream again.
- They return a value (a collection, a number, a boolean, an Optional) or perform an action (like printing).

**Types of Terminal Operations:**

| Category | Operations | Returns |
|----------|-----------|---------|
| **Collection** | `collect()`, `toList()`, `toArray()` | Collection or array |
| **Reduction** | `reduce()`, `count()`, `sum()`, `min()`, `max()` | Single value |
| **Searching** | `findFirst()`, `findAny()` | Optional element |
| **Matching** | `anyMatch()`, `allMatch()`, `noneMatch()` | Boolean |
| **Action** | `forEach()`, `forEachOrdered()` | Void (side effect) |

---

### forEach - Perform Action on Each Element

The `forEach` method executes an action for each element in the stream.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Basic forEach with lambda
names.stream()
    .forEach(name -> System.out.println(name));
// Output:
// Alice
// Bob
// Charlie

// Method reference (cleaner syntax)
names.stream()
    .forEach(System.out::println);

// forEach with more complex action
names.stream()
    .forEach(name -> {
        String upper = name.toUpperCase();
        System.out.println("Hello, " + upper + "!");
    });
// Output:
// Hello, ALICE!
// Hello, BOB!
// Hello, CHARLIE!
```

**forEach vs forEachOrdered:**

With parallel streams, `forEach` does not guarantee order. Use `forEachOrdered` when order matters:

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Order NOT guaranteed with parallel + forEach
System.out.println("forEach with parallel:");
numbers.parallelStream()
    .forEach(n -> System.out.print(n + " "));
// Might print: 3 4 5 1 2 (random order)

System.out.println();

// Order IS guaranteed with forEachOrdered
System.out.println("forEachOrdered with parallel:");
numbers.parallelStream()
    .forEachOrdered(n -> System.out.print(n + " "));
// Always prints: 1 2 3 4 5
```

**Important:** `forEach` returns void - you cannot chain operations after it.

```java
// Common mistake: trying to chain after forEach
names.stream()
    .forEach(System.out::println)
    .count();  // Compilation error! forEach returns void
```

---

### collect - Gather Results Into a Collection

The `collect` operation is the most versatile terminal operation. It gathers stream elements into various containers.

**Basic Collecting to List:**

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Using Collectors.toList()
List<String> filtered = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());
// [Alice, Charlie, David]

// Using toList() - Java 16+ (simpler, returns unmodifiable list)
List<String> filtered2 = names.stream()
    .filter(n -> n.length() > 3)
    .toList();
// [Alice, Charlie, David] - cannot be modified!
```

**Difference Between toList() and Collectors.toList():**

| Feature | `toList()` (Java 16+) | `Collectors.toList()` |
|---------|----------------------|----------------------|
| Mutability | Unmodifiable (cannot add/remove) | Modifiable |
| Null elements | Allowed | Allowed |
| Syntax | Shorter | Longer |
| When to use | When you only read the list | When you need to modify |

```java
// toList() creates unmodifiable list
List<String> immutable = names.stream().toList();
// immutable.add("Eve");  // Throws UnsupportedOperationException!

// Collectors.toList() creates modifiable list
List<String> mutable = names.stream().collect(Collectors.toList());
mutable.add("Eve");  // OK!
```

**Collecting to Set (removes duplicates):**

```java
List<String> withDuplicates = List.of("Apple", "Banana", "Apple", "Cherry");

Set<String> uniqueFruits = withDuplicates.stream()
    .collect(Collectors.toSet());
// [Apple, Banana, Cherry] - order may vary

// For sorted set
TreeSet<String> sortedFruits = withDuplicates.stream()
    .collect(Collectors.toCollection(TreeSet::new));
// [Apple, Banana, Cherry] - sorted!
```

**Collecting to Any Collection Type:**

```java
// To LinkedList
LinkedList<String> linkedList = names.stream()
    .collect(Collectors.toCollection(LinkedList::new));

// To ArrayDeque
ArrayDeque<String> deque = names.stream()
    .collect(Collectors.toCollection(ArrayDeque::new));

// To LinkedHashSet (maintains insertion order, no duplicates)
LinkedHashSet<String> orderedSet = names.stream()
    .collect(Collectors.toCollection(LinkedHashSet::new));
```

**Collecting to Map:**

```java
record Person(long id, String name, int age) {}
List<Person> people = List.of(
    new Person(1, "Alice", 30),
    new Person(2, "Bob", 25),
    new Person(3, "Charlie", 35)
);

// Basic: ID to Person
Map<Long, Person> personById = people.stream()
    .collect(Collectors.toMap(
        Person::id,          // Key: the ID
        Function.identity()  // Value: the Person itself
    ));
// {1=Person[id=1, name=Alice, age=30], 2=..., 3=...}

// Name to Age
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::name,  // Key
        Person::age    // Value
    ));
// {Alice=30, Bob=25, Charlie=35}

// With lambda for value
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(
        name -> name,           // Key: the name itself
        name -> name.length()   // Value: length of name
    ));
// {Alice=5, Bob=3, Charlie=7, David=5}
```

**Joining Strings:**

```java
List<String> words = List.of("Hello", "World", "Java");

// Simple join (no separator)
String joined = words.stream()
    .collect(Collectors.joining());
// "HelloWorldJava"

// With separator
String csv = words.stream()
    .collect(Collectors.joining(", "));
// "Hello, World, Java"

// With separator, prefix, and suffix
String formatted = words.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[Hello, World, Java]"

// Building SQL IN clause
List<Integer> ids = List.of(1, 2, 3, 4, 5);
String inClause = ids.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "(", ")"));
// "(1, 2, 3, 4, 5)"
```

---

### toArray - Convert Stream to Array

The `toArray` method converts stream elements into an array.

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// To Object array (not ideal - loses type information)
Object[] objectArray = names.stream()
    .toArray();
// Type is Object[], not String[]

// To typed array (preferred - use generator function)
String[] stringArray = names.stream()
    .toArray(String[]::new);
// ["Alice", "Bob", "Charlie"] - proper String[]

// With filtering
String[] longNames = names.stream()
    .filter(n -> n.length() > 3)
    .toArray(String[]::new);
// ["Alice", "Charlie"]

// Integer array
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
Integer[] integerArray = numbers.stream()
    .toArray(Integer[]::new);
// [1, 2, 3, 4, 5]
```

**Primitive Arrays:**

For primitive arrays, use primitive streams:

```java
// int[] (not Integer[])
int[] primitiveIntArray = IntStream.range(0, 5)
    .toArray();
// [0, 1, 2, 3, 4]

// From List<Integer> to int[]
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
int[] intArray = numbers.stream()
    .mapToInt(Integer::intValue)
    .toArray();
// [1, 2, 3, 4, 5]

// double[]
double[] doubleArray = DoubleStream.of(1.5, 2.5, 3.5)
    .toArray();
// [1.5, 2.5, 3.5]
```

**Two-Dimensional Arrays:**

```java
// Stream of arrays to 2D array
int[][] matrix = Stream.of(
    new int[]{1, 2, 3},
    new int[]{4, 5, 6},
    new int[]{7, 8, 9}
).toArray(int[][]::new);
```

---

### count - Count Elements

The `count` method returns the number of elements in the stream.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Count all elements
long total = names.stream().count();
// 4

// Count after filtering
long longNames = names.stream()
    .filter(n -> n.length() > 3)
    .count();
// 3 (Alice, Charlie, David)

// Count with distinct
List<String> withDuplicates = List.of("a", "b", "a", "c", "b");
long uniqueCount = withDuplicates.stream()
    .distinct()
    .count();
// 3

// Practical example: Count active users
long activeUsers = users.stream()
    .filter(User::isActive)
    .count();
```

**Important:** `count` returns `long`, not `int`.

**Performance Note:**

For some streams, `count()` can be optimized:

```java
// This is optimized - doesn't iterate
List<String> list = List.of("a", "b", "c");
long count = list.stream().count();  // Knows size without iterating

// This must iterate every element
long filtered = list.stream()
    .filter(s -> s.length() > 0)
    .count();
```

---

### reduce - Combine All Elements Into One

The `reduce` operation combines all elements into a single result using an accumulator function.

**What This Means in Plain Words:**
- Start with a value (identity)
- Take each element one by one
- Combine the current result with the next element
- End with a single combined result

**Three Forms of Reduce:**

**1. With Identity Value (always returns a value):**

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Sum: start with 0, add each number
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
// Step by step:
// 0 + 1 = 1
// 1 + 2 = 3
// 3 + 3 = 6
// 6 + 4 = 10
// 10 + 5 = 15
// Result: 15

// Same thing using method reference
int sum2 = numbers.stream()
    .reduce(0, Integer::sum);
// 15

// Product: start with 1, multiply each number
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);
// 1 * 1 * 2 * 3 * 4 * 5 = 120

// String concatenation: start with "", append each string
List<String> words = List.of("Hello", " ", "World");
String sentence = words.stream()
    .reduce("", (a, b) -> a + b);
// "Hello World"

// Finding max (but prefer max() method)
int max = numbers.stream()
    .reduce(Integer.MIN_VALUE, Integer::max);
// 5
```

**2. Without Identity (returns Optional):**

```java
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);

// No identity - returns Optional because list might be empty
Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);
// Optional[9]

Optional<Integer> min = numbers.stream()
    .reduce(Integer::min);
// Optional[1]

// Safe extraction
int maxValue = max.orElse(0);  // 9
int defaultIfEmpty = List.<Integer>of().stream()
    .reduce(Integer::max)
    .orElse(0);  // 0 (empty list)

// Product without identity
Optional<Integer> product = numbers.stream()
    .reduce((a, b) -> a * b);
// Optional[540]
```

**3. With Identity, Accumulator, and Combiner (for parallel):**

```java
// Combiner is used to merge results from parallel processing
List<String> words = List.of("Hello", "World", "Java");

// Calculate total length of all strings
int totalLength = words.parallelStream()
    .reduce(
        0,                           // Identity
        (length, word) -> length + word.length(),  // Accumulator
        (len1, len2) -> len1 + len2  // Combiner for parallel
    );
// 14 (5 + 5 + 4)

// Why combiner is needed:
// Thread 1: 0 + "Hello".length() = 5
// Thread 2: 0 + "World".length() = 5
// Thread 3: 0 + "Java".length() = 4
// Combiner: 5 + 5 + 4 = 14
```

**Practical Examples:**

```java
record Product(String name, double price) {}
List<Product> products = List.of(
    new Product("Laptop", 999.99),
    new Product("Mouse", 29.99),
    new Product("Keyboard", 79.99)
);

// Total price
double totalPrice = products.stream()
    .map(Product::price)
    .reduce(0.0, Double::sum);
// 1109.97

// Most expensive product
Optional<Product> mostExpensive = products.stream()
    .reduce((p1, p2) -> p1.price() > p2.price() ? p1 : p2);
// Optional[Product[name=Laptop, price=999.99]]

// Concatenate all names
String allNames = products.stream()
    .map(Product::name)
    .reduce((a, b) -> a + ", " + b)
    .orElse("No products");
// "Laptop, Mouse, Keyboard"
```

---

### min / max - Find Smallest or Largest Element

These methods find the minimum or maximum element according to a comparator.

```java
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

// Min and max with natural ordering
Optional<Integer> min = numbers.stream()
    .min(Comparator.naturalOrder());
// Optional[1]

Optional<Integer> max = numbers.stream()
    .max(Comparator.naturalOrder());
// Optional[9]

// Shorter: just use Integer::compare
Optional<Integer> min2 = numbers.stream()
    .min(Integer::compare);
// Optional[1]

// Safe extraction
int minValue = min.orElse(0);
int maxValue = max.orElseThrow(() -> new NoSuchElementException("Empty list"));
```

**With Strings:**

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Shortest name
Optional<String> shortest = names.stream()
    .min(Comparator.comparingInt(String::length));
// Optional[Bob]

// Longest name
Optional<String> longest = names.stream()
    .max(Comparator.comparingInt(String::length));
// Optional[Charlie]

// Alphabetically first
Optional<String> first = names.stream()
    .min(Comparator.naturalOrder());
// Optional[Alice]

// Alphabetically last
Optional<String> last = names.stream()
    .max(Comparator.naturalOrder());
// Optional[David]
```

**With Objects:**

```java
record Person(String name, int age) {}
List<Person> people = List.of(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
);

// Youngest person
Optional<Person> youngest = people.stream()
    .min(Comparator.comparingInt(Person::age));
// Optional[Person[name=Bob, age=25]]

// Oldest person
Optional<Person> oldest = people.stream()
    .max(Comparator.comparingInt(Person::age));
// Optional[Person[name=Charlie, age=35]]

// Multiple criteria: by age, then by name
Optional<Person> result = people.stream()
    .min(Comparator.comparingInt(Person::age)
        .thenComparing(Person::name));
```

**With Primitive Streams:**

```java
// IntStream, LongStream, DoubleStream have simpler min/max
OptionalInt minInt = IntStream.of(3, 1, 4, 1, 5).min();
// OptionalInt[1]

OptionalInt maxInt = IntStream.of(3, 1, 4, 1, 5).max();
// OptionalInt[5]

// No comparator needed for primitives
int minValue = IntStream.of(3, 1, 4, 1, 5)
    .min()
    .orElse(0);
```

---

### findFirst / findAny - Get One Element

These methods return a single element wrapped in an Optional.

**findFirst - Get the First Element:**

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Get first element
Optional<String> first = names.stream()
    .findFirst();
// Optional[Alice]

// First matching element
Optional<String> firstWithA = names.stream()
    .filter(n -> n.startsWith("A"))
    .findFirst();
// Optional[Alice]

// First matching after sorting
Optional<String> shortestFirst = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .findFirst();
// Optional[Bob] (shortest name)
```

**findAny - Get Any Matching Element:**

```java
// For sequential streams, findAny usually returns first element
Optional<String> any = names.stream()
    .filter(n -> n.length() > 3)
    .findAny();
// Usually Optional[Alice]

// For parallel streams, findAny is faster than findFirst
Optional<String> anyParallel = names.parallelStream()
    .filter(n -> n.length() > 3)
    .findAny();
// Could be Alice, Charlie, or David - any matching element
```

**When to Use Which:**

| Use Case | Method |
|----------|--------|
| Need specifically the first matching | `findFirst()` |
| Any match is acceptable | `findAny()` |
| Parallel stream + order does not matter | `findAny()` (faster) |
| Parallel stream + must be first | `findFirst()` (slower) |

**Working with the Optional Result:**

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Check if exists and get value
Optional<String> result = names.stream()
    .filter(n -> n.startsWith("B"))
    .findFirst();

// Various ways to handle the Optional
String name1 = result.orElse("Not found");  // "Bob" or "Not found"
String name2 = result.orElseGet(() -> "Default");  // Lazy default
String name3 = result.orElseThrow();  // Throws NoSuchElementException if empty

// Execute action if present
result.ifPresent(System.out::println);  // Prints "Bob"

// Execute action or alternative
result.ifPresentOrElse(
    name -> System.out.println("Found: " + name),
    () -> System.out.println("Not found")
);

// Boolean check
if (result.isPresent()) {
    System.out.println(result.get());
}
```

**Practical Example - Find User:**

```java
record User(long id, String name, boolean active) {}
List<User> users = List.of(
    new User(1, "Alice", true),
    new User(2, "Bob", false),
    new User(3, "Charlie", true)
);

// Find first active user
Optional<User> firstActive = users.stream()
    .filter(User::active)
    .findFirst();

// Find user by ID
Optional<User> user = users.stream()
    .filter(u -> u.id() == 2)
    .findFirst();

// Method that returns user or throws
User getUser(List<User> users, long id) {
    return users.stream()
        .filter(u -> u.id() == id)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
}
```

---

### anyMatch / allMatch / noneMatch - Test Conditions

These methods test if elements match a given condition and return a boolean.

**anyMatch - Does ANY Element Match?**

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Is there at least one even number?
boolean hasEven = numbers.stream()
    .anyMatch(n -> n % 2 == 0);
// true (2 and 4 are even)

// Is there a number greater than 10?
boolean hasLarge = numbers.stream()
    .anyMatch(n -> n > 10);
// false

// Empty stream: anyMatch always returns false
boolean emptyResult = Stream.<Integer>of().anyMatch(n -> n > 0);
// false (no elements to match)
```

**allMatch - Do ALL Elements Match?**

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Are all numbers positive?
boolean allPositive = numbers.stream()
    .allMatch(n -> n > 0);
// true

// Are all numbers even?
boolean allEven = numbers.stream()
    .allMatch(n -> n % 2 == 0);
// false (1, 3, 5 are odd)

// Empty stream: allMatch always returns true (vacuous truth)
boolean emptyResult = Stream.<Integer>of().allMatch(n -> n > 100);
// true (nothing to contradict the condition)
```

**noneMatch - Do NO Elements Match?**

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Are there no negative numbers?
boolean noNegative = numbers.stream()
    .noneMatch(n -> n < 0);
// true

// Are there no even numbers?
boolean noEven = numbers.stream()
    .noneMatch(n -> n % 2 == 0);
// false (2 and 4 are even)

// Empty stream: noneMatch always returns true
boolean emptyResult = Stream.<Integer>of().noneMatch(n -> n > 0);
// true (nothing to match)
```

**Short-Circuit Behavior:**

These methods stop processing as soon as the result is determined:

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// anyMatch stops at first match
boolean hasGreaterThan3 = numbers.stream()
    .peek(n -> System.out.println("Checking: " + n))
    .anyMatch(n -> n > 3);
// Output:
// Checking: 1
// Checking: 2
// Checking: 3
// Checking: 4
// (stops - found match)
// Result: true

// allMatch stops at first non-match
boolean allLessThan5 = numbers.stream()
    .peek(n -> System.out.println("Checking: " + n))
    .allMatch(n -> n < 5);
// Output:
// Checking: 1
// Checking: 2
// Checking: 3
// Checking: 4
// Checking: 5
// (stops - found non-match)
// Result: false
```

**Practical Examples:**

```java
record User(String email, boolean verified) {}
List<User> users = List.of(
    new User("alice@test.com", true),
    new User("bob@test.com", false),
    new User("charlie@test.com", true)
);

// Check if any user is unverified
boolean hasUnverified = users.stream()
    .anyMatch(u -> !u.verified());
// true

// Check if all users are verified
boolean allVerified = users.stream()
    .allMatch(User::verified);
// false

// Check if no email is null
boolean allHaveEmail = users.stream()
    .noneMatch(u -> u.email() == null);
// true

// Validation: check if list contains invalid data
List<String> emails = List.of("a@b.com", "c@d.com", "invalid");
boolean hasInvalid = emails.stream()
    .anyMatch(e -> !e.contains("@"));
// true
```

**Relationship Between Methods:**

```java
// These are logically equivalent:
boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
boolean allNonNegative = numbers.stream().allMatch(n -> n >= 0);
// Both true if no negative numbers

// anyMatch(predicate) == !noneMatch(predicate)
boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
boolean notNoneEven = !numbers.stream().noneMatch(n -> n % 2 == 0);
// hasEven == notNoneEven
```

---

### sum / average / summaryStatistics - Numeric Operations (Primitive Streams)

These methods are available on primitive streams (IntStream, LongStream, DoubleStream).

**sum - Add All Elements:**

```java
// IntStream sum
int sum = IntStream.of(1, 2, 3, 4, 5).sum();
// 15

// Sum from range
int rangeSum = IntStream.rangeClosed(1, 100).sum();
// 5050

// Sum from object stream
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
int listSum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
// 15

// Sum of prices
record Product(String name, double price) {}
List<Product> products = List.of(
    new Product("A", 10.0),
    new Product("B", 20.0),
    new Product("C", 30.0)
);
double totalPrice = products.stream()
    .mapToDouble(Product::price)
    .sum();
// 60.0
```

**average - Calculate Mean:**

```java
// Returns OptionalDouble (might be empty)
OptionalDouble avg = IntStream.of(1, 2, 3, 4, 5).average();
// OptionalDouble[3.0]

// Safe extraction
double averageValue = avg.orElse(0.0);
// 3.0

// Empty stream returns empty Optional
OptionalDouble emptyAvg = IntStream.of().average();
// OptionalDouble.empty

// Average age of users
double avgAge = users.stream()
    .mapToInt(User::getAge)
    .average()
    .orElse(0.0);
```

**summaryStatistics - All Statistics at Once:**

When you need multiple statistics, use `summaryStatistics()` to calculate them in one pass:

```java
IntSummaryStatistics stats = IntStream.of(1, 2, 3, 4, 5, 100)
    .summaryStatistics();

System.out.println("Count: " + stats.getCount());    // 6
System.out.println("Sum: " + stats.getSum());        // 115
System.out.println("Min: " + stats.getMin());        // 1
System.out.println("Max: " + stats.getMax());        // 100
System.out.println("Average: " + stats.getAverage()); // 19.166...

// For objects
record Employee(String name, int salary) {}
List<Employee> employees = List.of(
    new Employee("Alice", 50000),
    new Employee("Bob", 60000),
    new Employee("Charlie", 70000)
);

IntSummaryStatistics salaryStats = employees.stream()
    .mapToInt(Employee::salary)
    .summaryStatistics();

System.out.println("Employee count: " + salaryStats.getCount());
System.out.println("Total payroll: " + salaryStats.getSum());
System.out.println("Lowest salary: " + salaryStats.getMin());
System.out.println("Highest salary: " + salaryStats.getMax());
System.out.println("Average salary: " + salaryStats.getAverage());
```

**Combining Statistics:**

```java
// Combine statistics from different sources
IntSummaryStatistics stats1 = IntStream.of(1, 2, 3).summaryStatistics();
IntSummaryStatistics stats2 = IntStream.of(4, 5, 6).summaryStatistics();

stats1.combine(stats2);  // Merges stats2 into stats1

System.out.println("Combined count: " + stats1.getCount());  // 6
System.out.println("Combined sum: " + stats1.getSum());      // 21
```

**LongSummaryStatistics and DoubleSummaryStatistics:**

```java
// For long values
LongSummaryStatistics longStats = LongStream.of(1L, 2L, 3L)
    .summaryStatistics();

// For double values
DoubleSummaryStatistics doubleStats = DoubleStream.of(1.5, 2.5, 3.5)
    .summaryStatistics();

System.out.println("Sum: " + doubleStats.getSum());      // 7.5
System.out.println("Average: " + doubleStats.getAverage()); // 2.5
```

---

## Collectors

The `Collectors` class provides many predefined collectors for common use cases.

**What This Means:**
A Collector is a recipe that tells `collect()` how to combine stream elements into a result. Java provides many ready-to-use collectors in the `Collectors` utility class.

**Import:**
```java
import java.util.stream.Collectors;
```

**Categories of Collectors:**

| Category | Purpose | Examples |
|----------|---------|----------|
| **Collection** | Gather into collection | `toList()`, `toSet()`, `toCollection()` |
| **Map** | Build key-value mappings | `toMap()`, `toConcurrentMap()` |
| **Grouping** | Group by property | `groupingBy()`, `partitioningBy()` |
| **String** | Join elements | `joining()` |
| **Aggregating** | Calculate values | `counting()`, `summingInt()`, `averaging*()` |
| **Reducing** | Combine elements | `reducing()`, `maxBy()`, `minBy()` |
| **Transforming** | Modify before/after | `mapping()`, `filtering()`, `collectingAndThen()` |

---

### Basic Collection Collectors

**toList() and toSet():**

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "Alice");

// Collect to List (may contain duplicates)
List<String> list = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toList());
// [Alice, Charlie, Alice]

// Collect to Set (no duplicates)
Set<String> set = names.stream()
    .filter(n -> n.length() > 3)
    .collect(Collectors.toSet());
// [Alice, Charlie] - order may vary
```

**toUnmodifiableList(), toUnmodifiableSet(), toUnmodifiableMap() (Java 10+):**

```java
// Creates an unmodifiable list
List<String> immutableList = names.stream()
    .collect(Collectors.toUnmodifiableList());
// immutableList.add("Eve"); // Throws UnsupportedOperationException!

// Creates an unmodifiable set
Set<String> immutableSet = names.stream()
    .collect(Collectors.toUnmodifiableSet());

// Note: toList() in Java 16+ also creates unmodifiable list
List<String> alsoImmutable = names.stream().toList();
```

**toCollection() - Choose Your Collection Type:**

```java
// To LinkedList
LinkedList<String> linkedList = names.stream()
    .collect(Collectors.toCollection(LinkedList::new));

// To TreeSet (sorted, no duplicates)
TreeSet<String> sortedSet = names.stream()
    .collect(Collectors.toCollection(TreeSet::new));
// [Alice, Bob, Charlie] - sorted alphabetically

// To LinkedHashSet (maintains order, no duplicates)
LinkedHashSet<String> orderedSet = names.stream()
    .collect(Collectors.toCollection(LinkedHashSet::new));

// To ArrayDeque
ArrayDeque<String> deque = names.stream()
    .collect(Collectors.toCollection(ArrayDeque::new));

// To ArrayList with initial capacity
ArrayList<String> sizedList = names.stream()
    .collect(Collectors.toCollection(() -> new ArrayList<>(100)));
```

---

### toMap - Build Maps from Streams

The `toMap` collector creates a Map from stream elements.

**Basic Form - Key Mapper and Value Mapper:**

```java
record Person(long id, String name, int age) {}
List<Person> people = List.of(
    new Person(1, "Alice", 30),
    new Person(2, "Bob", 25),
    new Person(3, "Charlie", 35)
);

// Map person ID to name
Map<Long, String> idToName = people.stream()
    .collect(Collectors.toMap(
        Person::id,    // Key: the person's ID
        Person::name   // Value: the person's name
    ));
// {1=Alice, 2=Bob, 3=Charlie}

// Map person ID to the Person object itself
Map<Long, Person> idToPerson = people.stream()
    .collect(Collectors.toMap(
        Person::id,           // Key
        Function.identity()   // Value: the person itself
    ));

// Map name to age
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::name,
        Person::age
    ));
// {Alice=30, Bob=25, Charlie=35}
```

**Handling Duplicate Keys (IMPORTANT):**

If two elements have the same key, `toMap` throws `IllegalStateException` by default!

```java
record Person(String city, String name) {}
List<Person> people = List.of(
    new Person("NYC", "Alice"),
    new Person("NYC", "Bob"),     // Same city!
    new Person("LA", "Charlie")
);

// This throws IllegalStateException - duplicate key "NYC"!
Map<String, String> cityToName = people.stream()
    .collect(Collectors.toMap(
        Person::city,
        Person::name
    ));  // ERROR!

// Solution: Provide merge function for duplicates
Map<String, String> cityToNameMerged = people.stream()
    .collect(Collectors.toMap(
        Person::city,
        Person::name,
        (existing, replacement) -> existing  // Keep first
    ));
// {NYC=Alice, LA=Charlie}

// Or keep the later one
Map<String, String> keepLast = people.stream()
    .collect(Collectors.toMap(
        Person::city,
        Person::name,
        (existing, replacement) -> replacement  // Keep last
    ));
// {NYC=Bob, LA=Charlie}

// Or combine them
Map<String, String> combined = people.stream()
    .collect(Collectors.toMap(
        Person::city,
        Person::name,
        (a, b) -> a + ", " + b  // Combine
    ));
// {NYC=Alice, Bob, LA=Charlie}
```

**Specifying Map Type:**

```java
// Use LinkedHashMap to maintain insertion order
LinkedHashMap<Long, String> ordered = people.stream()
    .collect(Collectors.toMap(
        Person::id,
        Person::name,
        (a, b) -> a,           // Merge function (required)
        LinkedHashMap::new     // Map supplier
    ));

// Use TreeMap for sorted keys
TreeMap<Long, String> sorted = people.stream()
    .collect(Collectors.toMap(
        Person::id,
        Person::name,
        (a, b) -> a,
        TreeMap::new
    ));
```

**toConcurrentMap() - Thread-Safe Map for Parallel Streams:**

```java
ConcurrentMap<Long, String> concurrentMap = people.parallelStream()
    .collect(Collectors.toConcurrentMap(
        Person::id,
        Person::name
    ));
```

---

### groupingBy - Group Elements by Property

The `groupingBy` collector creates a Map where elements are grouped by a key.

**Basic Grouping:**

```java
record Person(String name, String city, int age) {}
List<Person> people = List.of(
    new Person("Alice", "NYC", 30),
    new Person("Bob", "NYC", 25),
    new Person("Charlie", "LA", 35),
    new Person("Diana", "LA", 28)
);

// Group by city
Map<String, List<Person>> byCity = people.stream()
    .collect(Collectors.groupingBy(Person::city));
// {NYC=[Alice, Bob], LA=[Charlie, Diana]}

// Access groups
List<Person> nycPeople = byCity.get("NYC");  // [Alice, Bob]
List<Person> laPeople = byCity.get("LA");    // [Charlie, Diana]
```

**With Downstream Collector:**

You can specify what to do with each group:

```java
// Count people in each city
Map<String, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.counting()
    ));
// {NYC=2, LA=2}

// Sum ages in each city
Map<String, Integer> totalAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.summingInt(Person::age)
    ));
// {NYC=55, LA=63}

// Average age in each city
Map<String, Double> avgAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.averagingInt(Person::age)
    ));
// {NYC=27.5, LA=31.5}

// Get names only (not full Person objects)
Map<String, List<String>> namesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.mapping(Person::name, Collectors.toList())
    ));
// {NYC=[Alice, Bob], LA=[Charlie, Diana]}

// Join names as string
Map<String, String> namesJoined = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.mapping(
            Person::name,
            Collectors.joining(", ")
        )
    ));
// {NYC=Alice, Bob, LA=Charlie, Diana}

// Find oldest in each city
Map<String, Optional<Person>> oldestByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.maxBy(Comparator.comparingInt(Person::age))
    ));
// {NYC=Optional[Bob], LA=Optional[Charlie]}

// Collect to Set instead of List
Map<String, Set<String>> uniqueNamesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.mapping(Person::name, Collectors.toSet())
    ));
```

**Multi-Level Grouping:**

```java
// Group by city, then by age range
Map<String, Map<String, List<Person>>> byCityAndAgeGroup = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.groupingBy(p -> p.age() < 30 ? "Young" : "Senior")
    ));
// {NYC={Senior=[Alice], Young=[Bob]}, LA={Senior=[Charlie, Diana]}}
```

**Specifying Map Type:**

```java
// Use TreeMap for sorted keys
TreeMap<String, List<Person>> sortedByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        TreeMap::new,
        Collectors.toList()
    ));
// Keys are alphabetically sorted: {LA=[...], NYC=[...]}
```

**groupingByConcurrent() - For Parallel Streams:**

```java
ConcurrentMap<String, List<Person>> concurrentByCity = people.parallelStream()
    .collect(Collectors.groupingByConcurrent(Person::city));
```

---

### partitioningBy - Split Into Two Groups

The `partitioningBy` collector splits elements into exactly two groups based on a predicate.

**What This Means:**
Unlike `groupingBy` which can create many groups, `partitioningBy` always creates exactly two groups: one for elements matching the predicate (true), one for non-matching elements (false).

```java
record Person(String name, int age) {}
List<Person> people = List.of(
    new Person("Alice", 30),
    new Person("Bob", 17),
    new Person("Charlie", 25),
    new Person("Diana", 15)
);

// Partition by adult status
Map<Boolean, List<Person>> adultPartition = people.stream()
    .collect(Collectors.partitioningBy(p -> p.age() >= 18));

List<Person> adults = adultPartition.get(true);   // [Alice, Charlie]
List<Person> minors = adultPartition.get(false);  // [Bob, Diana]

// With downstream collector
Map<Boolean, Long> countPartition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.age() >= 18,
        Collectors.counting()
    ));
// {true=2, false=2}

// Partition with names only
Map<Boolean, List<String>> namesPartition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.age() >= 18,
        Collectors.mapping(Person::name, Collectors.toList())
    ));
// {true=[Alice, Charlie], false=[Bob, Diana]}
```

**partitioningBy vs groupingBy:**

| Feature | `partitioningBy` | `groupingBy` |
|---------|-----------------|--------------|
| Number of groups | Always 2 (true/false) | Any number |
| Empty group handling | Both keys always present | Missing keys not in map |
| Key type | Boolean | Any type |

```java
List<Integer> numbers = List.of(1, 3, 5);  // All odd

// partitioningBy: both keys present
Map<Boolean, List<Integer>> partition = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// {true=[], false=[1, 3, 5]} - empty list for true

// groupingBy: missing key not present
Map<Boolean, List<Integer>> grouped = numbers.stream()
    .collect(Collectors.groupingBy(n -> n % 2 == 0));
// {false=[1, 3, 5]} - no 'true' key at all
```

---

### Aggregating Collectors

These collectors calculate aggregate values like counts, sums, and averages.

**counting():**

```java
long count = people.stream()
    .collect(Collectors.counting());
// 4

// More useful with groupingBy
Map<String, Long> countByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.counting()
    ));
```

**summingInt(), summingLong(), summingDouble():**

```java
// Sum of all ages
int totalAge = people.stream()
    .collect(Collectors.summingInt(Person::age));
// 110

// With grouping
Map<String, Integer> totalAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.summingInt(Person::age)
    ));
```

**averagingInt(), averagingLong(), averagingDouble():**

```java
double avgAge = people.stream()
    .collect(Collectors.averagingInt(Person::age));
// 27.5

// With grouping
Map<String, Double> avgAgeByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.averagingInt(Person::age)
    ));
```

**summarizingInt(), summarizingLong(), summarizingDouble():**

```java
IntSummaryStatistics ageStats = people.stream()
    .collect(Collectors.summarizingInt(Person::age));

System.out.println("Count: " + ageStats.getCount());
System.out.println("Sum: " + ageStats.getSum());
System.out.println("Min: " + ageStats.getMin());
System.out.println("Max: " + ageStats.getMax());
System.out.println("Average: " + ageStats.getAverage());
```

**maxBy() and minBy():**

```java
// Find oldest person
Optional<Person> oldest = people.stream()
    .collect(Collectors.maxBy(Comparator.comparingInt(Person::age)));

// Find youngest person
Optional<Person> youngest = people.stream()
    .collect(Collectors.minBy(Comparator.comparingInt(Person::age)));

// Most useful with grouping
Map<String, Optional<Person>> oldestByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.maxBy(Comparator.comparingInt(Person::age))
    ));
```

---

### String Collectors - joining()

The `joining()` collector concatenates strings.

```java
List<String> words = List.of("Hello", "World", "Java");

// Simple join (no separator)
String simple = words.stream()
    .collect(Collectors.joining());
// "HelloWorldJava"

// With separator
String commaSeparated = words.stream()
    .collect(Collectors.joining(", "));
// "Hello, World, Java"

// With separator, prefix, and suffix
String formatted = words.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[Hello, World, Java]"

// Building SQL IN clause
List<Integer> ids = List.of(1, 2, 3);
String inClause = ids.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "SELECT * FROM users WHERE id IN (", ")"));
// "SELECT * FROM users WHERE id IN (1, 2, 3)"

// HTML list
List<String> items = List.of("Apple", "Banana", "Cherry");
String htmlList = items.stream()
    .map(item -> "<li>" + item + "</li>")
    .collect(Collectors.joining("\n", "<ul>\n", "\n</ul>"));
// <ul>
// <li>Apple</li>
// <li>Banana</li>
// <li>Cherry</li>
// </ul>
```

---

### Transforming Collectors

These collectors transform elements before or after collection.

**mapping() - Transform Before Collecting:**

```java
// Extract names before collecting to list
List<String> names = people.stream()
    .collect(Collectors.mapping(
        Person::name,
        Collectors.toList()
    ));
// [Alice, Bob, Charlie, Diana]

// Usually used with groupingBy
Map<String, List<String>> namesByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.mapping(Person::name, Collectors.toList())
    ));
```

**filtering() (Java 9+) - Filter Before Collecting:**

```java
// Filter within each group
Map<String, List<Person>> adultsByCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::city,
        Collectors.filtering(
            p -> p.age() >= 18,
            Collectors.toList()
        )
    ));
// {NYC=[Alice, Charlie], LA=[...]} - only adults in each group
```

**flatMapping() (Java 9+) - Flatten Before Collecting:**

```java
record Employee(String name, String dept, List<String> skills) {}
List<Employee> employees = List.of(
    new Employee("Alice", "IT", List.of("Java", "Python")),
    new Employee("Bob", "IT", List.of("JavaScript", "Java")),
    new Employee("Charlie", "HR", List.of("Excel", "Word"))
);

// Get all unique skills per department
Map<String, Set<String>> skillsByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.flatMapping(
            e -> e.skills().stream(),
            Collectors.toSet()
        )
    ));
// {IT=[Java, Python, JavaScript], HR=[Excel, Word]}
```

**collectingAndThen() - Transform After Collecting:**

```java
// Make result unmodifiable
List<String> immutableNames = people.stream()
    .map(Person::name)
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList
    ));

// Convert to array
String[] nameArray = people.stream()
    .map(Person::name)
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        list -> list.toArray(String[]::new)
    ));

// Get size
int size = people.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        List::size
    ));

// Extract single element (or throw if not exactly one)
Person single = people.stream()
    .filter(p -> p.name().equals("Alice"))
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
            if (list.size() != 1) {
                throw new IllegalStateException("Expected exactly one");
            }
            return list.get(0);
        }
    ));
```

**teeing() (Java 12+) - Collect with Two Collectors and Combine:**

```java
// Calculate min and max in one pass
record Range(int min, int max) {}
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

Range range = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.minBy(Comparator.naturalOrder()),
        Collectors.maxBy(Comparator.naturalOrder()),
        (min, max) -> new Range(min.orElse(0), max.orElse(0))
    ));
// Range[min=1, max=9]

// Calculate sum and count, then average
record SumCount(int sum, long count) {
    double average() { return count == 0 ? 0 : (double) sum / count; }
}

SumCount sc = numbers.stream()
    .collect(Collectors.teeing(
        Collectors.summingInt(n -> n),
        Collectors.counting(),
        SumCount::new
    ));
System.out.println("Average: " + sc.average());

// Partition and count in one pass
record PartitionResult(List<Person> adults, List<Person> minors) {}
PartitionResult pr = people.stream()
    .collect(Collectors.teeing(
        Collectors.filtering(p -> p.age() >= 18, Collectors.toList()),
        Collectors.filtering(p -> p.age() < 18, Collectors.toList()),
        PartitionResult::new
    ));
```

---

### reducing Collector

The `reducing` collector performs reduction operations similar to `reduce()`.

```java
// With identity, mapper, and operation
int totalNameLength = people.stream()
    .collect(Collectors.reducing(
        0,                      // Identity
        Person::name,           // Mapper: Person -> String
        (len, name) -> len + name.length()  // BinaryOperator
    ));

// Alternative: just sum the lengths
int totalLength = people.stream()
    .collect(Collectors.reducing(
        0,
        p -> p.name().length(),
        Integer::sum
    ));

// Without identity (returns Optional)
Optional<Person> oldest = people.stream()
    .collect(Collectors.reducing(
        (p1, p2) -> p1.age() > p2.age() ? p1 : p2
    ));

// With identity
Person defaultPerson = new Person("Unknown", "None", 0);
Person eldest = people.stream()
    .collect(Collectors.reducing(
        defaultPerson,
        (p1, p2) -> p1.age() > p2.age() ? p1 : p2
    ));
```

---

## Primitive Streams

Java provides specialized streams for primitives to avoid boxing overhead.

**What This Means:**
When you use `Stream<Integer>`, each number is wrapped in an `Integer` object (boxing). This creates extra objects and slows things down. Primitive streams (`IntStream`, `LongStream`, `DoubleStream`) work directly with primitive values, making them faster and more memory-efficient.

**When to Use Primitive Streams:**
- When working with numbers
- When calculating sums, averages, statistics
- When processing large amounts of numeric data
- When performance matters

---

### IntStream

`IntStream` works with primitive `int` values.

**Creating IntStream:**

```java
// From values
IntStream values = IntStream.of(1, 2, 3, 4, 5);

// Range: 0 to 9 (end exclusive)
IntStream range = IntStream.range(0, 10);
// [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

// Range: 1 to 10 (end inclusive)
IntStream rangeClosed = IntStream.rangeClosed(1, 10);
// [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

// From array
int[] array = {1, 2, 3, 4, 5};
IntStream fromArray = Arrays.stream(array);

// From array slice
IntStream slice = Arrays.stream(array, 1, 4);  // [2, 3, 4]

// Infinite stream (use with limit!)
IntStream infinite = IntStream.iterate(0, n -> n + 2);  // 0, 2, 4, 6, ...

// Generate random ints
IntStream random = new Random().ints(10, 1, 100);  // 10 random ints 1-99

// From String chars
IntStream chars = "Hello".chars();  // Unicode code points: 72, 101, 108, 108, 111
```

**IntStream Operations:**

```java
// Sum
int sum = IntStream.of(1, 2, 3, 4, 5).sum();
// 15

// Average (returns OptionalDouble)
OptionalDouble avg = IntStream.of(1, 2, 3, 4, 5).average();
// OptionalDouble[3.0]

// Min and Max (returns OptionalInt)
OptionalInt min = IntStream.of(3, 1, 4, 1, 5).min();
// OptionalInt[1]

OptionalInt max = IntStream.of(3, 1, 4, 1, 5).max();
// OptionalInt[5]

// Count
long count = IntStream.range(0, 100).count();
// 100

// Summary statistics (all at once)
IntSummaryStatistics stats = IntStream.of(1, 2, 3, 4, 5).summaryStatistics();
stats.getCount();    // 5
stats.getSum();      // 15
stats.getMin();      // 1
stats.getMax();      // 5
stats.getAverage();  // 3.0
```

**Common IntStream Patterns:**

```java
// Sum of 1 to 100
int sum1to100 = IntStream.rangeClosed(1, 100).sum();
// 5050

// Check if any element matches
boolean hasEven = IntStream.of(1, 3, 5, 6, 7)
    .anyMatch(n -> n % 2 == 0);
// true

// Find first match
OptionalInt first = IntStream.range(1, 100)
    .filter(n -> n > 50)
    .findFirst();
// OptionalInt[51]

// Count elements matching condition
long evenCount = IntStream.range(1, 100)
    .filter(n -> n % 2 == 0)
    .count();
// 49

// Collect to array
int[] squared = IntStream.range(1, 6)
    .map(n -> n * n)
    .toArray();
// [1, 4, 9, 16, 25]

// Convert to List<Integer>
List<Integer> list = IntStream.range(1, 6)
    .boxed()
    .toList();
// [1, 2, 3, 4, 5]
```

---

### LongStream

`LongStream` works with primitive `long` values. Useful for large numbers or when dealing with IDs.

```java
// Creation
LongStream range = LongStream.range(0, 1_000_000_000L);
LongStream rangeClosed = LongStream.rangeClosed(1, 10);
LongStream values = LongStream.of(100L, 200L, 300L);

// From array
long[] array = {1L, 2L, 3L};
LongStream fromArray = Arrays.stream(array);

// Sum
long sum = LongStream.rangeClosed(1, 100).sum();
// 5050

// Statistics
LongSummaryStatistics stats = LongStream.of(1L, 2L, 3L, 4L, 5L)
    .summaryStatistics();

// Convert to array
long[] longArray = LongStream.range(0, 5).toArray();
// [0, 1, 2, 3, 4]

// Convert to List<Long>
List<Long> longList = LongStream.range(0, 5)
    .boxed()
    .toList();
```

---

### DoubleStream

`DoubleStream` works with primitive `double` values. Useful for decimal calculations.

```java
// Creation
DoubleStream values = DoubleStream.of(1.5, 2.5, 3.5);

// Random doubles
DoubleStream random = new Random().doubles(10, 0.0, 1.0);  // 10 doubles in [0, 1)

// From array
double[] array = {1.1, 2.2, 3.3};
DoubleStream fromArray = Arrays.stream(array);

// Generate infinite stream
DoubleStream infinite = DoubleStream.iterate(0.0, d -> d + 0.1);

// Sum
double sum = DoubleStream.of(1.5, 2.5, 3.5).sum();
// 7.5

// Average
OptionalDouble avg = DoubleStream.of(1.5, 2.5, 3.5).average();
// OptionalDouble[2.5]

// Statistics
DoubleSummaryStatistics stats = DoubleStream.of(1.5, 2.5, 3.5)
    .summaryStatistics();

// Convert to array
double[] doubleArray = DoubleStream.of(1.1, 2.2, 3.3).toArray();

// Convert to List<Double>
List<Double> doubleList = DoubleStream.of(1.1, 2.2, 3.3)
    .boxed()
    .toList();
```

---

### Converting Between Stream Types

**Object Stream to Primitive Stream:**

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

// Stream<String> to IntStream
IntStream lengths = names.stream()
    .mapToInt(String::length);
// [5, 3, 7]

// Stream<Product> to DoubleStream
List<Product> products = getProducts();
DoubleStream prices = products.stream()
    .mapToDouble(Product::getPrice);

// Stream<Order> to LongStream
List<Order> orders = getOrders();
LongStream orderIds = orders.stream()
    .mapToLong(Order::getId);
```

**Primitive Stream to Object Stream (boxing):**

```java
// IntStream to Stream<Integer>
Stream<Integer> boxedInts = IntStream.range(0, 5).boxed();
// 0, 1, 2, 3, 4 as Integer objects

// IntStream to Stream<String>
Stream<String> strings = IntStream.range(1, 4)
    .mapToObj(n -> "Number " + n);
// "Number 1", "Number 2", "Number 3"

// LongStream to Stream<Long>
Stream<Long> boxedLongs = LongStream.range(0, 5).boxed();

// DoubleStream to Stream<Double>
Stream<Double> boxedDoubles = DoubleStream.of(1.5, 2.5, 3.5).boxed();
```

**Primitive Stream to Primitive Stream:**

```java
// IntStream to LongStream
LongStream longs = IntStream.range(0, 5).asLongStream();
// 0L, 1L, 2L, 3L, 4L

// IntStream to DoubleStream
DoubleStream doubles = IntStream.range(0, 5).asDoubleStream();
// 0.0, 1.0, 2.0, 3.0, 4.0

// LongStream to DoubleStream
DoubleStream fromLong = LongStream.range(0, 5).asDoubleStream();
```

**Summary of Conversion Methods:**

| From | To | Method |
|------|-----|--------|
| `Stream<T>` | `IntStream` | `mapToInt()` |
| `Stream<T>` | `LongStream` | `mapToLong()` |
| `Stream<T>` | `DoubleStream` | `mapToDouble()` |
| `IntStream` | `Stream<T>` | `mapToObj()` or `boxed()` |
| `IntStream` | `LongStream` | `asLongStream()` |
| `IntStream` | `DoubleStream` | `asDoubleStream()` |
| `LongStream` | `Stream<T>` | `mapToObj()` or `boxed()` |
| `LongStream` | `DoubleStream` | `asDoubleStream()` |
| `DoubleStream` | `Stream<T>` | `mapToObj()` or `boxed()` |

---

## Parallel Streams

Parallel streams divide work across multiple threads to speed up processing.

**What This Means:**
Instead of processing elements one at a time (sequential), parallel streams split the data and process multiple elements at the same time using multiple CPU cores.

**Visual Comparison:**

```
Sequential: [1] -> [2] -> [3] -> [4] -> [5] -> [6] (one by one)

Parallel:   [1] -> [2]    (Thread 1)
            [3] -> [4]    (Thread 2)   -> Combine results
            [5] -> [6]    (Thread 3)
```

---

### Creating Parallel Streams

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

// Method 1: parallelStream() from collection
Stream<String> parallel1 = names.parallelStream();

// Method 2: parallel() on existing stream
Stream<String> parallel2 = names.stream().parallel();

// Check if a stream is parallel
boolean isParallel = parallel1.isParallel();  // true

// Convert parallel back to sequential
Stream<String> sequential = parallel1.sequential();

// IntStream parallel
IntStream parallelInts = IntStream.range(0, 1000).parallel();
```

---

### When to Use Parallel Streams

**Good Use Cases (parallel helps):**

```java
// Large data sets
long sum = IntStream.range(0, 10_000_000)
    .parallel()
    .sum();

// CPU-intensive operations
long primeCount = IntStream.range(2, 1_000_000)
    .parallel()
    .filter(this::isPrime)  // Complex calculation
    .count();

// Independent operations (no shared state)
List<String> results = names.parallelStream()
    .map(name -> expensiveTransform(name))
    .toList();
```

**Bad Use Cases (parallel hurts or is risky):**

```java
// Small data sets - overhead > benefit
List<String> small = List.of("a", "b", "c");
small.stream().map(String::toUpperCase).toList();  // Sequential is faster

// I/O operations - network/disk don't benefit
urls.stream()  // Don't use parallel for HTTP calls
    .map(this::fetchUrl)
    .toList();

// Operations that require order
// (parallel can mess up order)

// Shared mutable state (causes bugs!)
List<Integer> results = new ArrayList<>();
numbers.parallelStream()
    .forEach(n -> results.add(n));  // BUG: race condition!
```

**Rule of Thumb:**

| Situation | Use |
|-----------|-----|
| < 10,000 elements | Sequential |
| > 100,000 elements + CPU-intensive | Parallel |
| I/O bound operations | Sequential |
| Need to maintain order | Sequential (or use `forEachOrdered`) |
| Shared mutable state | Sequential |

---

### Order in Parallel Streams

Parallel streams may not preserve order by default:

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// forEach: order NOT guaranteed
System.out.println("forEach:");
numbers.parallelStream()
    .forEach(n -> System.out.print(n + " "));
// Might print: 6 7 8 9 10 1 2 3 4 5 (random order)

System.out.println();

// forEachOrdered: order IS guaranteed
System.out.println("forEachOrdered:");
numbers.parallelStream()
    .forEachOrdered(n -> System.out.print(n + " "));
// Always prints: 1 2 3 4 5 6 7 8 9 10
```

**Ordered Operations:**

Some operations preserve order even in parallel streams:

```java
// collect() preserves order for ordered sources
List<Integer> collected = numbers.parallelStream()
    .map(n -> n * 2)
    .toList();
// [2, 4, 6, 8, 10, 12, 14, 16, 18, 20] - order preserved

// sorted() ensures order
List<Integer> sorted = numbers.parallelStream()
    .sorted()
    .toList();
// [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

// findFirst() finds the first in stream order (slower than findAny)
Optional<Integer> first = numbers.parallelStream()
    .filter(n -> n > 5)
    .findFirst();
// Always 6

// findAny() returns any match (faster for parallel)
Optional<Integer> any = numbers.parallelStream()
    .filter(n -> n > 5)
    .findAny();
// Could be 6, 7, 8, 9, or 10
```

---

### Parallel Stream Safety - Avoiding Bugs

**Danger: Shared Mutable State**

```java
// WRONG - Race condition!
List<Integer> results = new ArrayList<>();  // Not thread-safe
numbers.parallelStream()
    .forEach(n -> results.add(n * 2));  // Multiple threads modify same list
// Results: missing elements, duplicates, or exceptions

// CORRECT - Use collect()
List<Integer> results = numbers.parallelStream()
    .map(n -> n * 2)
    .collect(Collectors.toList());
// Safe!

// CORRECT - Use thread-safe collection
List<Integer> results = Collections.synchronizedList(new ArrayList<>());
numbers.parallelStream()
    .forEach(n -> results.add(n * 2));
// Works, but collect() is preferred
```

**Danger: Stateful Lambda**

```java
// WRONG - Counter is shared state
AtomicInteger counter = new AtomicInteger(0);
List<Integer> numbered = names.parallelStream()
    .map(name -> counter.getAndIncrement() + ": " + name)
    .toList();
// Numbers may be out of order or duplicated!

// CORRECT - Use indices from stream
List<String> numbered = IntStream.range(0, names.size())
    .mapToObj(i -> i + ": " + names.get(i))
    .toList();
// Predictable ordering
```

---

### Controlling Parallelism

**Default Thread Pool:**

Parallel streams use the common `ForkJoinPool` by default:
- Number of threads = `Runtime.getRuntime().availableProcessors()` (typically CPU cores)

```java
// Check number of processors
int processors = Runtime.getRuntime().availableProcessors();
System.out.println("Available processors: " + processors);

// Set common pool parallelism (JVM-wide)
// Add to JVM arguments: -Djava.util.concurrent.ForkJoinPool.common.parallelism=4
```

**Custom Thread Pool:**

For more control, submit parallel stream to a custom ForkJoinPool:

```java
// Create custom pool with 4 threads
ForkJoinPool customPool = new ForkJoinPool(4);

try {
    List<String> result = customPool.submit(() ->
        names.parallelStream()
            .map(name -> {
                // This runs in the custom pool
                return name.toUpperCase();
            })
            .toList()
    ).get();  // Wait for completion
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    customPool.shutdown();
}
```

**When to Use Custom Pool:**
- When you want to limit threads used by a specific operation
- When different parts of your application need different parallelism levels
- When you need to isolate stream processing from other parallel operations

---

## Practical Examples

### Data Filtering and Transformation

```java
// Find active users' emails
List<String> activeEmails = users.stream()
    .filter(User::isActive)
    .map(User::getEmail)
    .filter(Objects::nonNull)
    .distinct()
    .sorted()
    .toList();

// Get top 5 highest-paid employees
List<Employee> topPaid = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
    .limit(5)
    .toList();
```

### Aggregations

```java
// Calculate total order value
double total = orders.stream()
    .flatMap(order -> order.getItems().stream())
    .mapToDouble(item -> item.getPrice() * item.getQuantity())
    .sum();

// Count items by category
Map<String, Long> countByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.counting()
    ));

// Average rating per product
Map<String, Double> avgRatings = reviews.stream()
    .collect(Collectors.groupingBy(
        Review::getProductId,
        Collectors.averagingDouble(Review::getRating)
    ));
```

### Searching and Matching

```java
// Find first available product
Optional<Product> available = products.stream()
    .filter(p -> p.getStock() > 0)
    .findFirst();

// Check if any order is overdue
boolean hasOverdue = orders.stream()
    .anyMatch(order -> order.getDueDate().isBefore(LocalDate.now()));

// Verify all users have email
boolean allHaveEmail = users.stream()
    .allMatch(user -> user.getEmail() != null);
```

### Building Maps

```java
// User ID to User
Map<Long, User> userById = users.stream()
    .collect(Collectors.toMap(User::getId, Function.identity()));

// Product to its category name
Map<Product, String> productCategories = products.stream()
    .collect(Collectors.toMap(
        Function.identity(),
        p -> p.getCategory().getName()
    ));

// Group users by role
Map<Role, List<User>> usersByRole = users.stream()
    .collect(Collectors.groupingBy(User::getRole));
```

### Flattening Nested Data

```java
// Get all tags from all posts
Set<String> allTags = posts.stream()
    .flatMap(post -> post.getTags().stream())
    .collect(Collectors.toSet());

// Get all items from all orders for a customer
List<Item> customerItems = customer.getOrders().stream()
    .flatMap(order -> order.getItems().stream())
    .toList();
```

### Complex Grouping

```java
// Group orders by status, then by customer
Map<OrderStatus, Map<Customer, List<Order>>> grouped = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getStatus,
        Collectors.groupingBy(Order::getCustomer)
    ));

// Get total sales by category
Map<String, Double> salesByCategory = orders.stream()
    .flatMap(o -> o.getItems().stream())
    .collect(Collectors.groupingBy(
        Item::getCategory,
        Collectors.summingDouble(item -> item.getPrice() * item.getQuantity())
    ));
```

---

## Best Practices

### 1. Prefer Method References

```java
// Good
names.stream().map(String::toUpperCase);

// Less good
names.stream().map(name -> name.toUpperCase());
```

### 2. Avoid Side Effects

```java
// Bad - side effect
List<String> results = new ArrayList<>();
names.stream()
    .filter(n -> n.length() > 3)
    .forEach(n -> results.add(n));

// Good - use collect
List<String> results = names.stream()
    .filter(n -> n.length() > 3)
    .toList();
```

### 3. Use Primitive Streams

```java
// Bad - boxing overhead
Integer sum = numbers.stream()
    .reduce(0, Integer::sum);

// Good - primitive stream
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
```

### 4. Don't Overuse Streams

```java
// Overkill for simple iteration
names.stream().forEach(System.out::println);

// Just use enhanced for-loop
for (String name : names) {
    System.out.println(name);
}
```

### 5. Close Streams from I/O

```java
// Streams from Files must be closed
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(line -> !line.isEmpty())
         .forEach(System.out::println);
}
```

### 6. Check for Empty Results

```java
// Handle empty results
Optional<Product> product = products.stream()
    .filter(p -> p.getId().equals(id))
    .findFirst();

product.ifPresentOrElse(
    p -> processProduct(p),
    () -> handleNotFound()
);
```

---

## Common Mistakes

### Mistake 1: Stream Already Consumed

```java
Stream<String> stream = names.stream();
stream.forEach(System.out::println);
stream.count();  // Error! Stream already consumed

// Fix: Create new stream
names.stream().forEach(System.out::println);
long count = names.stream().count();
```

### Mistake 2: Modifying Source During Stream

```java
// Dangerous!
names.stream()
    .filter(n -> n.length() > 3)
    .forEach(n -> names.remove(n));  // ConcurrentModificationException!

// Fix: Collect first
List<String> toRemove = names.stream()
    .filter(n -> n.length() > 3)
    .toList();
names.removeAll(toRemove);

// Or use removeIf
names.removeIf(n -> n.length() > 3);
```

### Mistake 3: Infinite Stream Without Limit

```java
// Runs forever!
Stream.generate(Math::random)
    .forEach(System.out::println);

// Fix: Add limit
Stream.generate(Math::random)
    .limit(10)
    .forEach(System.out::println);
```

### Mistake 4: Wrong Collector for Duplicates

```java
// Throws exception on duplicate keys!
Map<String, Person> byName = people.stream()
    .collect(Collectors.toMap(Person::getName, Function.identity()));

// Fix: Handle duplicates
Map<String, Person> byName = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Function.identity(),
        (existing, replacement) -> existing
    ));
```

### Mistake 5: Expecting Side Effects in Intermediate Operations

```java
// peek might not execute if no terminal operation
names.stream()
    .filter(n -> n.length() > 3)
    .peek(System.out::println);  // Nothing printed!

// Fix: Add terminal operation
names.stream()
    .filter(n -> n.length() > 3)
    .peek(System.out::println)
    .count();  // Now peek executes
```

---

## Cheat Sheet

### Creating Streams

| Source | How to Create |
|--------|--------------|
| Collection | `list.stream()`, `list.parallelStream()` |
| Array | `Arrays.stream(array)` |
| Values | `Stream.of(a, b, c)` |
| Range | `IntStream.range(0, 10)`, `IntStream.rangeClosed(1, 10)` |
| Generate | `Stream.generate(supplier)`, `Stream.iterate(seed, func)` |
| File | `Files.lines(path)` |
| Empty | `Stream.empty()` |

### Intermediate Operations

| Operation | Purpose | Example |
|-----------|---------|---------|
| `filter(pred)` | Keep matching | `.filter(n -> n > 0)` |
| `map(func)` | Transform | `.map(String::toUpperCase)` |
| `flatMap(func)` | Flatten nested | `.flatMap(list -> list.stream())` |
| `distinct()` | Remove duplicates | `.distinct()` |
| `sorted()` | Sort | `.sorted()`, `.sorted(comparator)` |
| `limit(n)` | Take first n | `.limit(5)` |
| `skip(n)` | Skip first n | `.skip(10)` |
| `takeWhile(pred)` | Take while true | `.takeWhile(n -> n < 10)` |
| `dropWhile(pred)` | Skip while true | `.dropWhile(n -> n < 10)` |
| `peek(action)` | Debug/side effect | `.peek(System.out::println)` |
| `mapToInt(func)` | To IntStream | `.mapToInt(String::length)` |

### Terminal Operations

| Operation | Returns | Purpose |
|-----------|---------|---------|
| `collect(collector)` | Collection/Value | Gather results |
| `toList()` | List | Collect to unmodifiable list (Java 16+) |
| `toArray()` | Array | Convert to array |
| `forEach(action)` | void | Process each element |
| `count()` | long | Count elements |
| `reduce(identity, op)` | Value | Combine all elements |
| `min(comparator)` | Optional | Find minimum |
| `max(comparator)` | Optional | Find maximum |
| `findFirst()` | Optional | Get first element |
| `findAny()` | Optional | Get any element |
| `anyMatch(pred)` | boolean | Any element matches? |
| `allMatch(pred)` | boolean | All elements match? |
| `noneMatch(pred)` | boolean | No element matches? |

### Common Collectors

| Collector | Result | Example |
|-----------|--------|---------|
| `toList()` | List | `collect(Collectors.toList())` |
| `toSet()` | Set | `collect(Collectors.toSet())` |
| `toMap(key, value)` | Map | `collect(Collectors.toMap(k, v))` |
| `joining()` | String | `collect(Collectors.joining(", "))` |
| `groupingBy(func)` | Map<K, List> | `collect(Collectors.groupingBy(Person::city))` |
| `partitioningBy(pred)` | Map<Boolean, List> | `collect(Collectors.partitioningBy(p -> p.age() > 18))` |
| `counting()` | Long | `collect(Collectors.counting())` |
| `summingInt(func)` | Integer | `collect(Collectors.summingInt(Person::age))` |
| `averagingInt(func)` | Double | `collect(Collectors.averagingInt(Person::age))` |

### Primitive Stream Operations

| Operation | IntStream | LongStream | DoubleStream |
|-----------|-----------|------------|--------------|
| Sum | `sum()` | `sum()` | `sum()` |
| Average | `average()` | `average()` | `average()` |
| Min/Max | `min()`, `max()` | `min()`, `max()` | `min()`, `max()` |
| Statistics | `summaryStatistics()` | `summaryStatistics()` | `summaryStatistics()` |
| To Object | `boxed()` | `boxed()` | `boxed()` |
| Range | `range()`, `rangeClosed()` | `range()`, `rangeClosed()` | - |

### Quick Patterns

```java
// Filter and collect
List<T> filtered = list.stream().filter(pred).toList();

// Transform
List<R> mapped = list.stream().map(func).toList();

// Group by property
Map<K, List<T>> grouped = list.stream().collect(Collectors.groupingBy(T::property));

// Sum numbers
int sum = list.stream().mapToInt(T::getValue).sum();

// Find first match
Optional<T> first = list.stream().filter(pred).findFirst();

// Check condition
boolean any = list.stream().anyMatch(pred);

// Join strings
String joined = list.stream().collect(Collectors.joining(", "));

// Count by group
Map<K, Long> counts = list.stream()
    .collect(Collectors.groupingBy(T::property, Collectors.counting()));
```

### Common Mistakes to Avoid

| Mistake | Problem | Solution |
|---------|---------|----------|
| Reusing stream | `IllegalStateException` | Create new stream each time |
| Modifying source | `ConcurrentModificationException` | Collect first, then modify |
| Infinite without limit | Runs forever | Add `.limit(n)` |
| Duplicate map keys | `IllegalStateException` | Add merge function to `toMap()` |
| No terminal operation | Nothing executes | Always end with terminal op |
| Shared mutable state in parallel | Race conditions | Use `collect()` instead |

---

## Navigation

| Previous | Up | Next |
|----------|----|----- |
| [Functional Interfaces](./25-functional-interfaces.md) | [Guide](../guide.md) | [Multithreading](./27-multithreading.md) |
