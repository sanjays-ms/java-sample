# Stream API Cheatsheet

[Back to Full Documentation](../documentation/26-stream-api.md)

---

## Creating Streams

| Source | Code |
|--------|------|
| Collection | `list.stream()` |
| Parallel | `list.parallelStream()` |
| Array | `Arrays.stream(array)` |
| Array slice | `Arrays.stream(array, start, end)` |
| Values | `Stream.of("a", "b", "c")` |
| Empty | `Stream.empty()` |
| Nullable | `Stream.ofNullable(value)` |
| Generate | `Stream.generate(supplier)` |
| Iterate | `Stream.iterate(seed, unaryOp)` |
| Iterate bounded | `Stream.iterate(seed, predicate, unaryOp)` |
| Range (exclusive) | `IntStream.range(0, 10)` |
| Range (inclusive) | `IntStream.rangeClosed(1, 10)` |
| File lines | `Files.lines(path)` |
| Directory | `Files.list(dir)` |
| Directory tree | `Files.walk(dir)` |
| Concat | `Stream.concat(s1, s2)` |
| Builder | `Stream.builder().add(a).add(b).build()` |
| String chars | `"text".chars()` |
| String lines | `"line1\nline2".lines()` |
| Map entries | `map.entrySet().stream()` |
| Map keys | `map.keySet().stream()` |
| Map values | `map.values().stream()` |
| Optional | `optional.stream()` |
| Pattern split | `pattern.splitAsStream(input)` |
| Random ints | `new Random().ints(count, min, max)` |

---

## Intermediate Operations

| Operation | Purpose | Example |
|-----------|---------|---------|
| `filter(predicate)` | Keep matching | `.filter(x -> x > 0)` |
| `map(function)` | Transform | `.map(String::toUpperCase)` |
| `mapToInt/Long/Double` | To primitive | `.mapToInt(String::length)` |
| `flatMap(function)` | Flatten nested | `.flatMap(List::stream)` |
| `flatMapToInt/Long/Double` | Flatten to primitive | `.flatMapToInt(...)` |
| `distinct()` | Remove duplicates | `.distinct()` |
| `sorted()` | Natural order | `.sorted()` |
| `sorted(comparator)` | Custom order | `.sorted(Comparator.reverseOrder())` |
| `limit(n)` | First n elements | `.limit(5)` |
| `skip(n)` | Skip first n | `.skip(10)` |
| `takeWhile(predicate)` | Take until false (Java 9+) | `.takeWhile(x -> x < 10)` |
| `dropWhile(predicate)` | Drop until false (Java 9+) | `.dropWhile(x -> x < 10)` |
| `peek(consumer)` | Debug/side effect | `.peek(System.out::println)` |
| `boxed()` | Primitive to Object | `.boxed()` |
| `mapMulti(consumer)` | 0-to-many transform (Java 16+) | `.mapMulti((e, c) -> {...})` |
| `unordered()` | Remove ordering constraint | `.unordered()` |
| `asLongStream()` | IntStream to LongStream | `.asLongStream()` |
| `asDoubleStream()` | Int/LongStream to DoubleStream | `.asDoubleStream()` |
| `mapToObj(function)` | Primitive to Object | `.mapToObj(n -> "Number " + n)` |
| `parallel()` | Make parallel | `.parallel()` |
| `sequential()` | Make sequential | `.sequential()` |
| `onClose(handler)` | Register close handler | `.onClose(() -> cleanup())` |

---

## Terminal Operations

| Operation | Returns | Example |
|-----------|---------|---------|
| `collect(collector)` | Collection/Map | `.collect(Collectors.toList())` |
| `toList()` | Immutable List (Java 16+) | `.toList()` |
| `toArray()` | Object[] | `.toArray()` |
| `toArray(generator)` | T[] | `.toArray(String[]::new)` |
| `forEach(consumer)` | void | `.forEach(System.out::println)` |
| `forEachOrdered(consumer)` | void (preserves order) | `.forEachOrdered(...)` |
| `count()` | long | `.count()` |
| `reduce(identity, op)` | T | `.reduce(0, Integer::sum)` |
| `reduce(op)` | Optional | `.reduce(Integer::max)` |
| `reduce(identity, accumulator, combiner)` | T | `.reduce(0, (a,b)->a+b, (a,b)->a+b)` |
| `min(comparator)` | Optional | `.min(Comparator.naturalOrder())` |
| `max(comparator)` | Optional | `.max(Comparator.naturalOrder())` |
| `findFirst()` | Optional | `.findFirst()` |
| `findAny()` | Optional (faster parallel) | `.findAny()` |
| `anyMatch(predicate)` | boolean | `.anyMatch(x -> x > 0)` |
| `allMatch(predicate)` | boolean | `.allMatch(x -> x > 0)` |
| `noneMatch(predicate)` | boolean | `.noneMatch(x -> x < 0)` |
| `iterator()` | Iterator | `.iterator()` |
| `spliterator()` | Spliterator | `.spliterator()` |
| `close()` | void (for I/O streams) | `.close()` |

### Primitive Stream Terminals

| Operation | IntStream | LongStream | DoubleStream |
|-----------|-----------|------------|--------------|
| `sum()` | int | long | double |
| `average()` | OptionalDouble | OptionalDouble | OptionalDouble |
| `min()` | OptionalInt | OptionalLong | OptionalDouble |
| `max()` | OptionalInt | OptionalLong | OptionalDouble |
| `summaryStatistics()` | IntSummaryStatistics | LongSummaryStatistics | DoubleSummaryStatistics |

---

## Essential Collectors

### To Collections

```java
Collectors.toList()
Collectors.toSet()
Collectors.toCollection(TreeSet::new)
Collectors.toUnmodifiableList()     // Java 10+
```

### To Map

```java
Collectors.toMap(keyFn, valueFn)
Collectors.toMap(keyFn, valueFn, mergeFunction)
Collectors.toMap(keyFn, valueFn, mergeFunction, mapSupplier)
```

### Grouping

```java
Collectors.groupingBy(classifier)
Collectors.groupingBy(classifier, downstream)
Collectors.groupingBy(classifier, mapFactory, downstream)
Collectors.partitioningBy(predicate)
Collectors.partitioningBy(predicate, downstream)
```

### Aggregating

```java
Collectors.counting()
Collectors.summingInt/Long/Double(fn)
Collectors.averagingInt/Long/Double(fn)
Collectors.summarizingInt/Long/Double(fn)
Collectors.maxBy(comparator)
Collectors.minBy(comparator)
```

### Strings

```java
Collectors.joining()                    // "abc"
Collectors.joining(", ")                // "a, b, c"
Collectors.joining(", ", "[", "]")      // "[a, b, c]"
```

### Transforming

```java
Collectors.mapping(mapper, downstream)
Collectors.filtering(predicate, downstream)    // Java 9+
Collectors.flatMapping(mapper, downstream)     // Java 9+
Collectors.collectingAndThen(downstream, finisher)
Collectors.teeing(d1, d2, merger)              // Java 12+
Collectors.reducing(identity, accumulator)
```

---

## Common Patterns

### Filter and Collect

```java
List<T> result = list.stream()
    .filter(predicate)
    .toList();
```

### Transform

```java
List<R> result = list.stream()
    .map(function)
    .toList();
```

### Flatten

```java
List<R> result = list.stream()
    .flatMap(item -> item.getList().stream())
    .toList();
```

### Group

```java
Map<K, List<T>> grouped = list.stream()
    .collect(Collectors.groupingBy(keyFn));
```

### Count by Group

```java
Map<K, Long> counts = list.stream()
    .collect(Collectors.groupingBy(keyFn, Collectors.counting()));
```

### Sum by Group

```java
Map<K, Integer> sums = list.stream()
    .collect(Collectors.groupingBy(
        keyFn,
        Collectors.summingInt(valueFn)
    ));
```

### To Map

```java
Map<K, V> map = list.stream()
    .collect(Collectors.toMap(keyFn, valueFn));

// Handle duplicates
Map<K, V> map = list.stream()
    .collect(Collectors.toMap(keyFn, valueFn, (a, b) -> a));
```

### Find First Match

```java
Optional<T> found = list.stream()
    .filter(predicate)
    .findFirst();
```

### Check Condition

```java
boolean any = list.stream().anyMatch(predicate);
boolean all = list.stream().allMatch(predicate);
boolean none = list.stream().noneMatch(predicate);
```

### Top N

```java
List<T> topN = list.stream()
    .sorted(comparator.reversed())
    .limit(n)
    .toList();
```

### Distinct Values

```java
List<R> unique = list.stream()
    .map(extractor)
    .distinct()
    .toList();
```

---

## Primitive Streams

| Type | Creation | Sum | Average | Box |
|------|----------|-----|---------|-----|
| `IntStream` | `IntStream.range(0, 10)` | `.sum()` | `.average()` | `.boxed()` |
| `LongStream` | `LongStream.range(0, 10)` | `.sum()` | `.average()` | `.boxed()` |
| `DoubleStream` | `DoubleStream.of(1.0, 2.0)` | `.sum()` | `.average()` | `.boxed()` |

### Conversions

```java
// Object to primitive
stream.mapToInt(fn)
stream.mapToLong(fn)
stream.mapToDouble(fn)

// Primitive to object
intStream.boxed()

// Primitive to primitive
intStream.asLongStream()
intStream.asDoubleStream()
```

---

## Parallel Streams

```java
// Create parallel
list.parallelStream()
stream.parallel()

// Convert to sequential
parallelStream.sequential()

// Check
stream.isParallel()

// Ordered parallel
parallelStream.forEachOrdered(action)
```

**When to use parallel:**
- Large data sets (10,000+ elements)
- CPU-intensive operations
- Independent element processing

**When NOT to use parallel:**
- Small data sets
- I/O operations
- Stateful operations
- When order matters

---

## Comparator Helpers

```java
Comparator.comparing(keyFn)
Comparator.comparingInt/Long/Double(keyFn)
Comparator.naturalOrder()
Comparator.reverseOrder()
comparator.reversed()
comparator.thenComparing(keyFn)
Comparator.nullsFirst(comparator)
Comparator.nullsLast(comparator)
```

---

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Reusing stream | IllegalStateException | Create new stream |
| Modifying source | ConcurrentModificationException | Collect first, then modify |
| Infinite without limit | Runs forever | Add `.limit(n)` |
| Duplicate map keys | IllegalStateException | Add merge function |
| No terminal op | Nothing happens | Add terminal operation |
| Side effects in map | Unpredictable | Use peek for side effects |
| forEach with index | No index available | Use IntStream.range() |

---

## Quick Reference

```java
// Complete pipeline
List<Result> results = source.stream()
    .filter(predicate)      // keep matching
    .map(transformer)       // transform
    .sorted(comparator)     // order
    .limit(n)               // first n
    .collect(Collectors.toList());

// Aggregation pipeline
Map<Key, Stats> stats = items.stream()
    .filter(predicate)
    .collect(Collectors.groupingBy(
        Item::getKey,
        Collectors.summarizingDouble(Item::getValue)
    ));
```
