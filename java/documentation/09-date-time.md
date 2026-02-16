# Date and Time in Java

[<- Previous: Math](08-math.md) | [Next: Classes and Objects ->](10-classes.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Date and Time Cheat Sheet](../cheatsheets/date-time-cheatsheet.md)

---

## Overview

Java 8 introduced the `java.time` package (also known as JSR-310), which is the modern way to handle date and time. This package is immutable, thread-safe, and addresses many issues with the older `java.util.Date` and `java.util.Calendar` classes.

```java
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
```

**Why use java.time?**
- Immutable and thread-safe
- Clear separation of concepts (date, time, datetime, timezone)
- Fluent API with method chaining
- Better naming conventions
- Built-in formatting and parsing

---

## Core Classes Overview

| Class | Description | Example |
|-------|-------------|---------|
| `LocalDate` | Date without time or timezone | 2024-01-15 |
| `LocalTime` | Time without date or timezone | 14:30:45 |
| `LocalDateTime` | Date and time without timezone | 2024-01-15T14:30:45 |
| `ZonedDateTime` | Date and time with timezone | 2024-01-15T14:30:45+05:30[Asia/Kolkata] |
| `Instant` | Timestamp (seconds since epoch) | 2024-01-15T09:00:45Z |
| `Duration` | Time-based amount (hours, minutes, seconds) | PT2H30M |
| `Period` | Date-based amount (years, months, days) | P1Y2M3D |
| `ZoneId` | Timezone identifier | Asia/Kolkata |
| `ZoneOffset` | Timezone offset from UTC | +05:30 |

---

## LocalDate

Represents a date without time or timezone. Ideal for birthdays, holidays, or any date-only data.

### Creating LocalDate

```java
// Current date
LocalDate today = LocalDate.now();  // e.g., 2024-01-15

// Specific date
LocalDate date1 = LocalDate.of(2024, 1, 15);           // January 15, 2024
LocalDate date2 = LocalDate.of(2024, Month.JANUARY, 15);  // Using Month enum

// Parse from string
LocalDate parsed = LocalDate.parse("2024-01-15");  // ISO format
LocalDate parsed2 = LocalDate.parse("15/01/2024", 
    DateTimeFormatter.ofPattern("dd/MM/yyyy"));

// From epoch day (days since 1970-01-01)
LocalDate fromEpoch = LocalDate.ofEpochDay(19738);  // 2024-01-15

// From year and day of year
LocalDate fromYearDay = LocalDate.ofYearDay(2024, 15);  // January 15, 2024
```

### Getting Date Components

```java
LocalDate date = LocalDate.of(2024, 1, 15);

int year = date.getYear();          // 2024
Month month = date.getMonth();      // JANUARY
int monthValue = date.getMonthValue();  // 1
int day = date.getDayOfMonth();     // 15
DayOfWeek dow = date.getDayOfWeek();    // MONDAY
int dayOfYear = date.getDayOfYear();    // 15

// Length information
int daysInMonth = date.lengthOfMonth();  // 31
int daysInYear = date.lengthOfYear();    // 366 (2024 is leap year)
boolean isLeap = date.isLeapYear();      // true
```

### Manipulating Dates

LocalDate is immutable. All operations return a new LocalDate.

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// Adding
LocalDate plusDays = date.plusDays(10);      // 2024-01-25
LocalDate plusWeeks = date.plusWeeks(2);     // 2024-01-29
LocalDate plusMonths = date.plusMonths(1);   // 2024-02-15
LocalDate plusYears = date.plusYears(1);     // 2025-01-15

// Subtracting
LocalDate minusDays = date.minusDays(10);    // 2024-01-05
LocalDate minusMonths = date.minusMonths(1); // 2023-12-15

// Using Period
LocalDate plusPeriod = date.plus(Period.ofMonths(3));  // 2024-04-15

// Setting specific fields
LocalDate withDay = date.withDayOfMonth(1);   // 2024-01-01
LocalDate withMonth = date.withMonth(6);      // 2024-06-15
LocalDate withYear = date.withYear(2025);     // 2025-01-15

// First/last day of month
LocalDate firstDay = date.withDayOfMonth(1);  // 2024-01-01
LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());  // 2024-01-31

// Using TemporalAdjusters
LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
LocalDate lastOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
LocalDate nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
LocalDate firstMondayOfMonth = date.with(
    TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
```

### Comparing Dates

```java
LocalDate date1 = LocalDate.of(2024, 1, 15);
LocalDate date2 = LocalDate.of(2024, 6, 20);

boolean isBefore = date1.isBefore(date2);  // true
boolean isAfter = date1.isAfter(date2);    // false
boolean isEqual = date1.isEqual(date2);    // false

int comparison = date1.compareTo(date2);   // negative (date1 < date2)

// Check if date is in the past or future
LocalDate today = LocalDate.now();
boolean isPast = date1.isBefore(today);
boolean isFuture = date1.isAfter(today);
```

### Period Between Dates

```java
LocalDate start = LocalDate.of(2024, 1, 15);
LocalDate end = LocalDate.of(2025, 3, 20);

// Get period between dates
Period period = Period.between(start, end);
int years = period.getYears();   // 1
int months = period.getMonths(); // 2
int days = period.getDays();     // 5

// Total days between dates
long totalDays = ChronoUnit.DAYS.between(start, end);   // 430
long totalWeeks = ChronoUnit.WEEKS.between(start, end); // 61
long totalMonths = ChronoUnit.MONTHS.between(start, end); // 14
```

---

## LocalTime

Represents a time without date or timezone. Ideal for store hours, alarm times, etc.

### Creating LocalTime

```java
// Current time
LocalTime now = LocalTime.now();  // e.g., 14:30:45.123456789

// Specific time
LocalTime time1 = LocalTime.of(14, 30);         // 14:30
LocalTime time2 = LocalTime.of(14, 30, 45);     // 14:30:45
LocalTime time3 = LocalTime.of(14, 30, 45, 123456789);  // with nanos

// Parse from string
LocalTime parsed = LocalTime.parse("14:30:45");  // ISO format
LocalTime parsed2 = LocalTime.parse("2:30 PM", 
    DateTimeFormatter.ofPattern("h:mm a"));

// Constants
LocalTime midnight = LocalTime.MIDNIGHT;  // 00:00
LocalTime noon = LocalTime.NOON;          // 12:00
LocalTime min = LocalTime.MIN;            // 00:00
LocalTime max = LocalTime.MAX;            // 23:59:59.999999999
```

### Getting Time Components

```java
LocalTime time = LocalTime.of(14, 30, 45, 123456789);

int hour = time.getHour();         // 14
int minute = time.getMinute();     // 30
int second = time.getSecond();     // 45
int nano = time.getNano();         // 123456789

// Convert to seconds since midnight
int secondOfDay = time.toSecondOfDay();  // 52245
long nanoOfDay = time.toNanoOfDay();
```

### Manipulating Time

```java
LocalTime time = LocalTime.of(14, 30, 45);

// Adding
LocalTime plusHours = time.plusHours(2);      // 16:30:45
LocalTime plusMinutes = time.plusMinutes(45); // 15:15:45
LocalTime plusSeconds = time.plusSeconds(30); // 14:31:15

// Subtracting
LocalTime minusHours = time.minusHours(3);    // 11:30:45

// Time wraps around midnight
LocalTime late = LocalTime.of(23, 0);
LocalTime wrapped = late.plusHours(3);  // 02:00 (next day, but no date info)

// Setting specific fields
LocalTime withHour = time.withHour(10);     // 10:30:45
LocalTime withMinute = time.withMinute(0);  // 14:00:45
LocalTime withSecond = time.withSecond(0);  // 14:30:00

// Truncate to unit
LocalTime truncated = time.truncatedTo(ChronoUnit.MINUTES);  // 14:30:00
LocalTime truncHour = time.truncatedTo(ChronoUnit.HOURS);    // 14:00:00
```

### Comparing Times

```java
LocalTime time1 = LocalTime.of(9, 30);
LocalTime time2 = LocalTime.of(17, 0);

boolean isBefore = time1.isBefore(time2);  // true
boolean isAfter = time1.isAfter(time2);    // false

int comparison = time1.compareTo(time2);   // negative

// Check if time is within business hours
LocalTime openTime = LocalTime.of(9, 0);
LocalTime closeTime = LocalTime.of(17, 0);
LocalTime checkTime = LocalTime.now();

boolean isOpen = !checkTime.isBefore(openTime) && checkTime.isBefore(closeTime);
```

### Duration Between Times

```java
LocalTime start = LocalTime.of(9, 0);
LocalTime end = LocalTime.of(17, 30);

Duration duration = Duration.between(start, end);
long hours = duration.toHours();     // 8
long minutes = duration.toMinutes(); // 510
long seconds = duration.toSeconds(); // 30600

// Using ChronoUnit
long hoursBetween = ChronoUnit.HOURS.between(start, end);  // 8
long minutesBetween = ChronoUnit.MINUTES.between(start, end);  // 510
```

---

## LocalDateTime

Combines date and time without timezone. Ideal for timestamps, appointments, events.

### Creating LocalDateTime

```java
// Current date and time
LocalDateTime now = LocalDateTime.now();  // e.g., 2024-01-15T14:30:45

// Specific date and time
LocalDateTime dt1 = LocalDateTime.of(2024, 1, 15, 14, 30);
LocalDateTime dt2 = LocalDateTime.of(2024, 1, 15, 14, 30, 45);
LocalDateTime dt3 = LocalDateTime.of(2024, Month.JANUARY, 15, 14, 30, 45);

// From LocalDate and LocalTime
LocalDate date = LocalDate.of(2024, 1, 15);
LocalTime time = LocalTime.of(14, 30);
LocalDateTime dt4 = LocalDateTime.of(date, time);
LocalDateTime dt5 = date.atTime(time);
LocalDateTime dt6 = date.atTime(14, 30);
LocalDateTime dt7 = time.atDate(date);

// Parse from string
LocalDateTime parsed = LocalDateTime.parse("2024-01-15T14:30:45");
LocalDateTime parsed2 = LocalDateTime.parse("15/01/2024 14:30",
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

// Start of day
LocalDateTime startOfDay = date.atStartOfDay();  // 2024-01-15T00:00
```

### Getting Components

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30, 45);

// Date components
LocalDate date = dt.toLocalDate();     // 2024-01-15
int year = dt.getYear();               // 2024
Month month = dt.getMonth();           // JANUARY
int day = dt.getDayOfMonth();          // 15
DayOfWeek dow = dt.getDayOfWeek();     // MONDAY

// Time components
LocalTime time = dt.toLocalTime();     // 14:30:45
int hour = dt.getHour();               // 14
int minute = dt.getMinute();           // 30
int second = dt.getSecond();           // 45
```

### Manipulating LocalDateTime

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);

// Adding
LocalDateTime plusDays = dt.plusDays(5);
LocalDateTime plusHours = dt.plusHours(3);
LocalDateTime plusWeeks = dt.plusWeeks(2);

// Subtracting
LocalDateTime minusMonths = dt.minusMonths(1);
LocalDateTime minusMinutes = dt.minusMinutes(30);

// Setting fields
LocalDateTime withYear = dt.withYear(2025);
LocalDateTime withHour = dt.withHour(9);

// Using TemporalAdjusters
LocalDateTime endOfMonth = dt.with(TemporalAdjusters.lastDayOfMonth());
```

### Converting to ZonedDateTime

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);

// Add timezone
ZonedDateTime zdt = dt.atZone(ZoneId.of("Asia/Kolkata"));
// 2024-01-15T14:30+05:30[Asia/Kolkata]

// Add offset
OffsetDateTime odt = dt.atOffset(ZoneOffset.ofHours(5));
// 2024-01-15T14:30+05:00
```

---

## ZonedDateTime

Complete date-time with timezone. Essential for handling global time across different regions.

### Creating ZonedDateTime

```java
// Current time in system timezone
ZonedDateTime now = ZonedDateTime.now();

// Current time in specific timezone
ZonedDateTime nowIndia = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
ZonedDateTime nowNY = ZonedDateTime.now(ZoneId.of("America/New_York"));
ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

// Specific date/time in timezone
ZonedDateTime zdt = ZonedDateTime.of(2024, 1, 15, 14, 30, 0, 0, 
    ZoneId.of("Asia/Kolkata"));

// From LocalDateTime
LocalDateTime ldt = LocalDateTime.of(2024, 1, 15, 14, 30);
ZonedDateTime fromLocal = ldt.atZone(ZoneId.of("Europe/London"));

// Parse from string
ZonedDateTime parsed = ZonedDateTime.parse(
    "2024-01-15T14:30:45+05:30[Asia/Kolkata]");
```

### Available Timezones

```java
// Get all available zone IDs
Set<String> allZones = ZoneId.getAvailableZoneIds();
// Over 600 zones like: Asia/Kolkata, America/New_York, Europe/London, etc.

// Common zones
ZoneId india = ZoneId.of("Asia/Kolkata");
ZoneId newYork = ZoneId.of("America/New_York");
ZoneId london = ZoneId.of("Europe/London");
ZoneId tokyo = ZoneId.of("Asia/Tokyo");
ZoneId utc = ZoneId.of("UTC");  // or ZoneOffset.UTC

// System default timezone
ZoneId systemZone = ZoneId.systemDefault();

// Filter zones by region
allZones.stream()
    .filter(z -> z.startsWith("Asia/"))
    .sorted()
    .forEach(System.out::println);
```

### Converting Between Timezones

```java
ZonedDateTime indiaTime = ZonedDateTime.of(2024, 1, 15, 14, 30, 0, 0,
    ZoneId.of("Asia/Kolkata"));

// Convert to different timezone
ZonedDateTime newYorkTime = indiaTime.withZoneSameInstant(
    ZoneId.of("America/New_York"));
// 2024-01-15T04:00-05:00[America/New_York]

ZonedDateTime londonTime = indiaTime.withZoneSameInstant(
    ZoneId.of("Europe/London"));
// 2024-01-15T09:00Z[Europe/London]

// Keep same local time, change zone (different instant)
ZonedDateTime sameLocalDifferentZone = indiaTime.withZoneSameLocal(
    ZoneId.of("America/New_York"));
// 2024-01-15T14:30-05:00[America/New_York] (different instant!)
```

### Handling Daylight Saving Time (DST)

```java
// During DST transition, time may skip or repeat
ZoneId newYork = ZoneId.of("America/New_York");

// Spring forward (2:00 AM becomes 3:00 AM)
LocalDateTime springForward = LocalDateTime.of(2024, 3, 10, 2, 30);
ZonedDateTime zdt1 = springForward.atZone(newYork);
// 2:30 AM doesn't exist, adjusted to 3:30 AM

// Fall back (2:00 AM happens twice)
LocalDateTime fallBack = LocalDateTime.of(2024, 11, 3, 1, 30);
ZonedDateTime zdt2 = fallBack.atZone(newYork);
// Ambiguous time, picks earlier offset by default

// Explicitly handle DST overlap
ZonedDateTime earlier = ZonedDateTime.ofLocal(fallBack, newYork, 
    ZoneOffset.ofHours(-4));  // EDT
ZonedDateTime later = ZonedDateTime.ofLocal(fallBack, newYork, 
    ZoneOffset.ofHours(-5));  // EST
```

### Getting Offset Information

```java
ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

ZoneId zone = zdt.getZone();        // Asia/Kolkata
ZoneOffset offset = zdt.getOffset(); // +05:30

// Check if DST is in effect
ZoneRules rules = zone.getRules();
boolean isDST = rules.isDaylightSavings(zdt.toInstant());

// Get next DST transition
ZoneOffsetTransition nextTransition = rules.nextTransition(zdt.toInstant());
```

---

## Instant

Represents a point in time as seconds (and nanoseconds) since Unix epoch (January 1, 1970 UTC). Ideal for timestamps, logging, and machine-time operations.

### Creating Instant

```java
// Current instant
Instant now = Instant.now();  // e.g., 2024-01-15T09:00:45.123456789Z

// From epoch
Instant fromEpochSecond = Instant.ofEpochSecond(1705312845L);
Instant fromEpochMilli = Instant.ofEpochMilli(1705312845123L);
Instant withNanos = Instant.ofEpochSecond(1705312845L, 123456789);

// Constants
Instant epoch = Instant.EPOCH;  // 1970-01-01T00:00:00Z
Instant min = Instant.MIN;      // -1000000000-01-01T00:00:00Z
Instant max = Instant.MAX;      // +1000000000-12-31T23:59:59.999999999Z

// Parse from string
Instant parsed = Instant.parse("2024-01-15T09:00:45.123Z");
```

### Getting Epoch Values

```java
Instant instant = Instant.now();

long epochSecond = instant.getEpochSecond();  // Seconds since epoch
int nano = instant.getNano();                  // Nanoseconds (0-999999999)
long epochMilli = instant.toEpochMilli();     // Milliseconds since epoch
```

### Manipulating Instant

```java
Instant instant = Instant.now();

// Adding/subtracting duration
Instant plusSeconds = instant.plusSeconds(60);
Instant plusMillis = instant.plusMillis(1000);
Instant plusNanos = instant.plusNanos(1000000);
Instant minusHours = instant.minus(Duration.ofHours(2));

// Cannot add days/months directly (no date awareness)
// Use Duration for time-based operations
Instant plusDays = instant.plus(Duration.ofDays(1));
```

### Converting Instant

```java
Instant instant = Instant.now();

// To ZonedDateTime
ZonedDateTime zdt = instant.atZone(ZoneId.of("Asia/Kolkata"));

// To OffsetDateTime
OffsetDateTime odt = instant.atOffset(ZoneOffset.ofHours(5));

// From ZonedDateTime/LocalDateTime
Instant fromZdt = zdt.toInstant();
Instant fromLdt = LocalDateTime.now().toInstant(ZoneOffset.UTC);

// To/from java.util.Date (legacy interop)
java.util.Date date = java.util.Date.from(instant);
Instant fromDate = date.toInstant();

// To/from java.sql.Timestamp
java.sql.Timestamp timestamp = java.sql.Timestamp.from(instant);
Instant fromTimestamp = timestamp.toInstant();
```

### Comparing Instants

```java
Instant i1 = Instant.now();
Thread.sleep(100);
Instant i2 = Instant.now();

boolean isBefore = i1.isBefore(i2);  // true
boolean isAfter = i1.isAfter(i2);    // false

long millisBetween = Duration.between(i1, i2).toMillis();
```

---

## Duration

Represents a time-based amount (hours, minutes, seconds, nanoseconds). Used for measuring elapsed time or specifying time intervals.

### Creating Duration

```java
// From units
Duration hours = Duration.ofHours(2);        // PT2H
Duration minutes = Duration.ofMinutes(30);   // PT30M
Duration seconds = Duration.ofSeconds(45);   // PT45S
Duration millis = Duration.ofMillis(1500);   // PT1.5S
Duration nanos = Duration.ofNanos(1000000);  // PT0.001S

// Combining
Duration complex = Duration.ofHours(2)
    .plusMinutes(30)
    .plusSeconds(45);  // PT2H30M45S

// From two temporals
LocalTime start = LocalTime.of(9, 0);
LocalTime end = LocalTime.of(17, 30);
Duration workDay = Duration.between(start, end);  // PT8H30M

// Parse from string (ISO 8601 format)
Duration parsed = Duration.parse("PT2H30M");    // 2 hours 30 minutes
Duration parsed2 = Duration.parse("P1DT12H");   // 1 day 12 hours

// Constants
Duration zero = Duration.ZERO;  // PT0S
```

### Getting Duration Components

```java
Duration duration = Duration.ofHours(2).plusMinutes(30).plusSeconds(45);

// Total conversions
long totalSeconds = duration.toSeconds();   // 9045
long totalMinutes = duration.toMinutes();   // 150
long totalHours = duration.toHours();       // 2
long totalDays = duration.toDays();         // 0
long totalMillis = duration.toMillis();     // 9045000

// Part extraction (Java 9+)
long hoursPart = duration.toHoursPart();    // 2
int minutesPart = duration.toMinutesPart(); // 30
int secondsPart = duration.toSecondsPart(); // 45
int millisPart = duration.toMillisPart();   // 0
int nanosPart = duration.toNanosPart();     // 0
```

### Manipulating Duration

```java
Duration d1 = Duration.ofHours(2);
Duration d2 = Duration.ofMinutes(30);

// Arithmetic
Duration sum = d1.plus(d2);      // PT2H30M
Duration diff = d1.minus(d2);    // PT1H30M
Duration doubled = d1.multipliedBy(2);  // PT4H
Duration halved = d1.dividedBy(2);      // PT1H

// Negation and absolute
Duration negative = d1.negated();  // PT-2H
Duration absolute = negative.abs(); // PT2H

// Check properties
boolean isNegative = negative.isNegative();  // true
boolean isZero = Duration.ZERO.isZero();     // true
```

### Formatting Duration

```java
Duration duration = Duration.ofHours(2).plusMinutes(30).plusSeconds(45);

// Default toString
String iso = duration.toString();  // PT2H30M45S

// Custom formatting
long hours = duration.toHours();
long minutes = duration.toMinutesPart();
long seconds = duration.toSecondsPart();
String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
// 02:30:45

// Human readable
public static String formatDuration(Duration d) {
    long days = d.toDays();
    long hours = d.toHoursPart();
    long minutes = d.toMinutesPart();
    long seconds = d.toSecondsPart();
    
    StringBuilder sb = new StringBuilder();
    if (days > 0) sb.append(days).append(" day(s) ");
    if (hours > 0) sb.append(hours).append(" hour(s) ");
    if (minutes > 0) sb.append(minutes).append(" minute(s) ");
    if (seconds > 0) sb.append(seconds).append(" second(s)");
    return sb.toString().trim();
}
```

---

## Period

Represents a date-based amount (years, months, days). Used for calendar-based calculations.

### Creating Period

```java
// From units
Period years = Period.ofYears(2);     // P2Y
Period months = Period.ofMonths(6);   // P6M
Period weeks = Period.ofWeeks(3);     // P21D (converted to days)
Period days = Period.ofDays(10);      // P10D

// Combined
Period combined = Period.of(1, 6, 15);  // P1Y6M15D

// From two dates
LocalDate start = LocalDate.of(2024, 1, 15);
LocalDate end = LocalDate.of(2025, 8, 20);
Period period = Period.between(start, end);  // P1Y7M5D

// Parse from string
Period parsed = Period.parse("P1Y6M15D");

// Zero period
Period zero = Period.ZERO;
```

### Getting Period Components

```java
Period period = Period.of(1, 6, 15);

int years = period.getYears();    // 1
int months = period.getMonths();  // 6
int days = period.getDays();      // 15

// Total months (years * 12 + months)
long totalMonths = period.toTotalMonths();  // 18
```

### Period vs Duration

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 31, 12, 0);

// Period - calendar-aware (handles month lengths)
LocalDateTime plusMonth = dt.plus(Period.ofMonths(1));
// 2024-02-29 (not 2024-02-31, adjusted to valid date)

// Duration - fixed time
LocalDateTime plus30Days = dt.plus(Duration.ofDays(30));
// 2024-03-01 (exactly 30 days)

// When to use which:
// - Period: birthdays, subscriptions, monthly billing
// - Duration: timeouts, elapsed time, precise intervals
```

---

## Formatting and Parsing

### DateTimeFormatter

```java
// Predefined formatters
LocalDateTime dt = LocalDateTime.now();

String iso = dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
// 2024-01-15T14:30:45

String isoDate = dt.format(DateTimeFormatter.ISO_LOCAL_DATE);
// 2024-01-15

String basic = dt.format(DateTimeFormatter.BASIC_ISO_DATE);
// 20240115
```

### Custom Patterns

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30, 45);

// Define custom formatter
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
String formatted = dt.format(formatter);  // 15/01/2024 14:30:45

// Common patterns
DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");       // 2024-01-15
DateTimeFormatter f2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");      // 15-Jan-2024
DateTimeFormatter f3 = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"); // Monday, January 15, 2024
DateTimeFormatter f4 = DateTimeFormatter.ofPattern("h:mm a");           // 2:30 PM
DateTimeFormatter f5 = DateTimeFormatter.ofPattern("HH:mm:ss");         // 14:30:45
DateTimeFormatter f6 = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss a"); // 2024/01/15 02:30:45 PM
```

### Pattern Letters

| Letter | Meaning | Example |
|--------|---------|---------|
| y | Year | 2024, 24 |
| M | Month | 1, 01, Jan, January |
| d | Day of month | 15 |
| E | Day of week | Mon, Monday |
| H | Hour (0-23) | 14 |
| h | Hour (1-12) | 2 |
| m | Minute | 30 |
| s | Second | 45 |
| S | Millisecond | 123 |
| a | AM/PM | PM |
| z | Timezone name | IST |
| Z | Timezone offset | +0530 |
| VV | Timezone ID | Asia/Kolkata |

### Parsing Strings

```java
// Parse with predefined formatter
LocalDate date = LocalDate.parse("2024-01-15");  // ISO format by default

// Parse with custom formatter
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
LocalDate parsed = LocalDate.parse("15/01/2024", formatter);

// Parse date and time
DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
LocalDateTime dt = LocalDateTime.parse("15-01-2024 14:30:45", dtFormatter);

// Lenient parsing
DateTimeFormatter lenient = DateTimeFormatter.ofPattern("M/d/yyyy")
    .withResolverStyle(ResolverStyle.LENIENT);
LocalDate lenientDate = LocalDate.parse("1/5/2024", lenient);

// Handle parsing errors
String input = "invalid-date";
try {
    LocalDate date = LocalDate.parse(input);
} catch (DateTimeParseException e) {
    System.out.println("Invalid date format: " + e.getMessage());
}
```

### Localized Formatting

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);

// Localized formatters
DateTimeFormatter shortDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
DateTimeFormatter mediumDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
DateTimeFormatter longDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
DateTimeFormatter fullDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);

// With specific locale
DateTimeFormatter germanFormatter = DateTimeFormatter
    .ofLocalizedDate(FormatStyle.FULL)
    .withLocale(Locale.GERMAN);
String germanDate = dt.format(germanFormatter);  // Montag, 15. Januar 2024

DateTimeFormatter frenchFormatter = DateTimeFormatter
    .ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
    .withLocale(Locale.FRENCH);
String frenchDateTime = dt.format(frenchFormatter);  // 15 janvier 2024 14:30
```

---

## TemporalAdjusters

Utility class for common date adjustments.

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// First/last day of month
LocalDate firstOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());  // 2024-01-01
LocalDate lastOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());    // 2024-01-31

// First/last day of year
LocalDate firstOfYear = date.with(TemporalAdjusters.firstDayOfYear());    // 2024-01-01
LocalDate lastOfYear = date.with(TemporalAdjusters.lastDayOfYear());      // 2024-12-31

// First/last day of next/previous month
LocalDate firstOfNextMonth = date.with(TemporalAdjusters.firstDayOfNextMonth());
LocalDate firstOfNextYear = date.with(TemporalAdjusters.firstDayOfNextYear());

// Next/previous day of week
LocalDate nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
LocalDate prevFriday = date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
LocalDate nextOrSameMonday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

// Nth day of week in month
LocalDate secondTuesday = date.with(
    TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.TUESDAY));
LocalDate lastFriday = date.with(
    TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
LocalDate firstMonday = date.with(
    TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
```

### Custom TemporalAdjuster

```java
// Custom adjuster: next working day (skip weekends)
TemporalAdjuster nextWorkingDay = temporal -> {
    LocalDate date = LocalDate.from(temporal);
    DayOfWeek dow = date.getDayOfWeek();
    
    if (dow == DayOfWeek.FRIDAY) {
        return date.plusDays(3);  // Skip to Monday
    } else if (dow == DayOfWeek.SATURDAY) {
        return date.plusDays(2);  // Skip to Monday
    } else {
        return date.plusDays(1);  // Next day
    }
};

LocalDate friday = LocalDate.of(2024, 1, 19);  // Friday
LocalDate nextWorking = friday.with(nextWorkingDay);  // 2024-01-22 (Monday)
```

---

## Clock

Abstraction for the current instant, useful for testing.

```java
// Default system clock
Clock systemClock = Clock.systemDefaultZone();
Clock utcClock = Clock.systemUTC();

// Get instant/time from clock
Instant instant = systemClock.instant();
LocalDateTime now = LocalDateTime.now(systemClock);

// Fixed clock (for testing)
Instant fixedInstant = Instant.parse("2024-01-15T10:00:00Z");
Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

LocalDateTime fixedTime = LocalDateTime.now(fixedClock);  // Always 2024-01-15T10:00:00

// Offset clock (shifted time)
Clock offsetClock = Clock.offset(systemClock, Duration.ofHours(2));

// Tick clock (updates at intervals)
Clock tickClock = Clock.tickSeconds(ZoneId.systemDefault());  // Updates every second
Clock tickMinutes = Clock.tickMinutes(ZoneId.systemDefault());  // Updates every minute
```

---

## Practical Examples

### Age Calculator

```java
public static int calculateAge(LocalDate birthDate) {
    return Period.between(birthDate, LocalDate.now()).getYears();
}

public static String formatAge(LocalDate birthDate) {
    Period age = Period.between(birthDate, LocalDate.now());
    return String.format("%d years, %d months, %d days",
        age.getYears(), age.getMonths(), age.getDays());
}

LocalDate birthDate = LocalDate.of(1990, 5, 15);
int age = calculateAge(birthDate);  // e.g., 33
String formatted = formatAge(birthDate);  // e.g., "33 years, 8 months, 0 days"
```

### Days Until Event

```java
public static long daysUntil(LocalDate eventDate) {
    return ChronoUnit.DAYS.between(LocalDate.now(), eventDate);
}

public static String countdown(LocalDate eventDate) {
    Period period = Period.between(LocalDate.now(), eventDate);
    if (period.isNegative()) {
        return "Event has passed";
    }
    return String.format("%d months and %d days remaining",
        period.getMonths(), period.getDays());
}

LocalDate newYear = LocalDate.of(2025, 1, 1);
long days = daysUntil(newYear);  // e.g., 351
```

### Working Days Calculator

```java
public static long countWorkingDays(LocalDate start, LocalDate end) {
    return start.datesUntil(end)
        .filter(date -> {
            DayOfWeek dow = date.getDayOfWeek();
            return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY;
        })
        .count();
}

// With holidays
public static long countWorkingDays(LocalDate start, LocalDate end, 
                                     Set<LocalDate> holidays) {
    return start.datesUntil(end)
        .filter(date -> {
            DayOfWeek dow = date.getDayOfWeek();
            return dow != DayOfWeek.SATURDAY 
                && dow != DayOfWeek.SUNDAY
                && !holidays.contains(date);
        })
        .count();
}
```

### Meeting Time Across Timezones

```java
public static void printMeetingTimes(ZonedDateTime meeting, String... zoneIds) {
    System.out.println("Meeting scheduled:");
    for (String zoneId : zoneIds) {
        ZonedDateTime converted = meeting.withZoneSameInstant(ZoneId.of(zoneId));
        String formatted = converted.format(
            DateTimeFormatter.ofPattern("EEEE, MMM d 'at' h:mm a z"));
        System.out.println("  " + zoneId + ": " + formatted);
    }
}

ZonedDateTime meeting = ZonedDateTime.of(2024, 1, 15, 14, 0, 0, 0,
    ZoneId.of("America/New_York"));

printMeetingTimes(meeting,
    "America/New_York",
    "America/Los_Angeles",
    "Europe/London",
    "Asia/Kolkata",
    "Asia/Tokyo"
);
// Meeting scheduled:
//   America/New_York: Monday, Jan 15 at 2:00 PM EST
//   America/Los_Angeles: Monday, Jan 15 at 11:00 AM PST
//   Europe/London: Monday, Jan 15 at 7:00 PM GMT
//   Asia/Kolkata: Tuesday, Jan 16 at 12:30 AM IST
//   Asia/Tokyo: Tuesday, Jan 16 at 4:00 AM JST
```

### Recurring Events

```java
// Generate dates for every Monday in a month
public static List<LocalDate> getMondaysInMonth(YearMonth month) {
    LocalDate first = month.atDay(1);
    LocalDate firstMonday = first.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    
    List<LocalDate> mondays = new ArrayList<>();
    LocalDate current = firstMonday;
    
    while (current.getMonth() == month.getMonth()) {
        mondays.add(current);
        current = current.plusWeeks(1);
    }
    return mondays;
}

// Monthly billing dates
public static List<LocalDate> getBillingDates(LocalDate start, int months) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate current = start;
    
    for (int i = 0; i < months; i++) {
        dates.add(current);
        current = current.plusMonths(1);
    }
    return dates;
}
```

### Stopwatch

```java
public class Stopwatch {
    private Instant startTime;
    private Instant endTime;
    private boolean running;
    
    public void start() {
        startTime = Instant.now();
        running = true;
    }
    
    public void stop() {
        endTime = Instant.now();
        running = false;
    }
    
    public Duration getElapsed() {
        if (running) {
            return Duration.between(startTime, Instant.now());
        }
        return Duration.between(startTime, endTime);
    }
    
    public String getElapsedFormatted() {
        Duration d = getElapsed();
        return String.format("%d:%02d:%02d.%03d",
            d.toHours(),
            d.toMinutesPart(),
            d.toSecondsPart(),
            d.toMillisPart()
        );
    }
}

// Usage
Stopwatch sw = new Stopwatch();
sw.start();
// ... do work ...
sw.stop();
System.out.println("Elapsed: " + sw.getElapsedFormatted());
```

### Parsing Multiple Date Formats

```java
public static LocalDate parseFlexibleDate(String input) {
    String[] patterns = {
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "MM/dd/yyyy",
        "d-MMM-yyyy",
        "MMMM d, yyyy",
        "dd.MM.yyyy"
    };
    
    for (String pattern : patterns) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            // Try next pattern
        }
    }
    throw new IllegalArgumentException("Cannot parse date: " + input);
}

// All these work
LocalDate d1 = parseFlexibleDate("2024-01-15");
LocalDate d2 = parseFlexibleDate("15/01/2024");
LocalDate d3 = parseFlexibleDate("01/15/2024");
LocalDate d4 = parseFlexibleDate("15-Jan-2024");
LocalDate d5 = parseFlexibleDate("January 15, 2024");
```

---

## Legacy Date/Time Interoperability

### Converting Between Old and New APIs

```java
// java.util.Date <-> Instant
java.util.Date oldDate = new java.util.Date();
Instant instant = oldDate.toInstant();
java.util.Date backToDate = java.util.Date.from(instant);

// java.util.Date <-> LocalDateTime
LocalDateTime ldt = LocalDateTime.ofInstant(oldDate.toInstant(), ZoneId.systemDefault());
java.util.Date fromLdt = java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

// java.util.Calendar <-> ZonedDateTime
java.util.Calendar calendar = java.util.Calendar.getInstance();
ZonedDateTime zdt = ZonedDateTime.ofInstant(calendar.toInstant(), 
    calendar.getTimeZone().toZoneId());
java.util.GregorianCalendar gc = java.util.GregorianCalendar.from(zdt);

// java.sql.Date <-> LocalDate
java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
LocalDate fromSqlDate = sqlDate.toLocalDate();

// java.sql.Time <-> LocalTime
java.sql.Time sqlTime = java.sql.Time.valueOf(LocalTime.now());
LocalTime fromSqlTime = sqlTime.toLocalTime();

// java.sql.Timestamp <-> LocalDateTime
java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(LocalDateTime.now());
LocalDateTime fromTimestamp = timestamp.toLocalDateTime();
```

---

## Summary

### Choosing the Right Class

| Use Case | Class |
|----------|-------|
| Date only (birthdays, holidays) | `LocalDate` |
| Time only (opening hours) | `LocalTime` |
| Date and time (appointments) | `LocalDateTime` |
| Date/time with timezone (global events) | `ZonedDateTime` |
| Machine timestamp (logging, databases) | `Instant` |
| Time interval (elapsed time) | `Duration` |
| Calendar interval (age, subscription) | `Period` |

### Common Operations

| Operation | Method |
|-----------|--------|
| Get current | `now()` |
| Create specific | `of()`, `parse()` |
| Add/subtract | `plus...()`, `minus...()` |
| Modify field | `with...()` |
| Compare | `isBefore()`, `isAfter()`, `compareTo()` |
| Format | `format(DateTimeFormatter)` |
| Convert timezone | `withZoneSameInstant()` |
| Get difference | `between()`, `ChronoUnit.X.between()` |

### Key Points

- Use `java.time` package (Java 8+), not `java.util.Date`
- All classes are immutable and thread-safe
- Use `LocalDate`/`LocalTime`/`LocalDateTime` when timezone is not needed
- Use `ZonedDateTime` for timezone-aware operations
- Use `Instant` for timestamps and machine time
- Use `Period` for date-based amounts (years, months, days)
- Use `Duration` for time-based amounts (hours, minutes, seconds)
- Use `DateTimeFormatter` for formatting and parsing
- Be aware of DST transitions with `ZonedDateTime`

---

[<- Previous: Math](08-math.md) | [Next: Classes and Objects ->](10-classes.md) | [Back to Guide](../guide.md)
