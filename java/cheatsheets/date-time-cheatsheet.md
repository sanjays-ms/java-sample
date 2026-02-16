# Date and Time Cheat Sheet

[Back to Guide](../guide.md) | [Full Documentation](../documentation/09-date-time.md)

---

## Core Classes

| Class | Description | Example |
|-------|-------------|---------|
| `LocalDate` | Date only | 2024-01-15 |
| `LocalTime` | Time only | 14:30:45 |
| `LocalDateTime` | Date + time | 2024-01-15T14:30:45 |
| `ZonedDateTime` | Date + time + zone | 2024-01-15T14:30+05:30[Asia/Kolkata] |
| `Instant` | Timestamp (epoch) | 2024-01-15T09:00:00Z |
| `Duration` | Time-based amount | PT2H30M |
| `Period` | Date-based amount | P1Y2M3D |

---

## LocalDate

```java
// Create
LocalDate.now()
LocalDate.of(2024, 1, 15)
LocalDate.of(2024, Month.JANUARY, 15)
LocalDate.parse("2024-01-15")

// Get components
date.getYear()          // 2024
date.getMonth()         // JANUARY
date.getMonthValue()    // 1
date.getDayOfMonth()    // 15
date.getDayOfWeek()     // MONDAY
date.getDayOfYear()     // 15

// Manipulate (immutable - returns new)
date.plusDays(10)
date.plusWeeks(2)
date.plusMonths(1)
date.minusYears(1)
date.withDayOfMonth(1)  // First of month
date.withMonth(6)

// Compare
date1.isBefore(date2)
date1.isAfter(date2)
date1.isEqual(date2)
```

---

## LocalTime

```java
// Create
LocalTime.now()
LocalTime.of(14, 30)          // 14:30
LocalTime.of(14, 30, 45)      // 14:30:45
LocalTime.parse("14:30:45")
LocalTime.MIDNIGHT            // 00:00
LocalTime.NOON                // 12:00

// Get components
time.getHour()         // 14
time.getMinute()       // 30
time.getSecond()       // 45

// Manipulate
time.plusHours(2)
time.plusMinutes(30)
time.withHour(10)
time.truncatedTo(ChronoUnit.MINUTES)  // 14:30:00
```

---

## LocalDateTime

```java
// Create
LocalDateTime.now()
LocalDateTime.of(2024, 1, 15, 14, 30)
LocalDateTime.of(date, time)
date.atTime(14, 30)
date.atStartOfDay()           // Midnight

// Get parts
dt.toLocalDate()
dt.toLocalTime()

// Add timezone
dt.atZone(ZoneId.of("Asia/Kolkata"))
```

---

## ZonedDateTime

```java
// Create
ZonedDateTime.now()
ZonedDateTime.now(ZoneId.of("America/New_York"))
ZonedDateTime.of(2024, 1, 15, 14, 30, 0, 0, zone)
localDateTime.atZone(zone)

// Convert timezone
zdt.withZoneSameInstant(ZoneId.of("UTC"))

// Get zone info
zdt.getZone()          // Asia/Kolkata
zdt.getOffset()        // +05:30

// Available zones
ZoneId.getAvailableZoneIds()
```

---

## Instant

```java
// Create
Instant.now()
Instant.ofEpochSecond(1705312845L)
Instant.ofEpochMilli(1705312845123L)
Instant.parse("2024-01-15T09:00:00Z")

// Get epoch
instant.getEpochSecond()
instant.toEpochMilli()

// Convert
instant.atZone(ZoneId.of("UTC"))

// Legacy interop
Date.from(instant)
date.toInstant()
```

---

## Duration (Time-Based)

```java
// Create
Duration.ofHours(2)
Duration.ofMinutes(30)
Duration.ofSeconds(45)
Duration.between(time1, time2)
Duration.parse("PT2H30M")     // 2 hours 30 minutes

// Get components
duration.toHours()            // Total hours
duration.toMinutes()          // Total minutes
duration.toHoursPart()        // Hours component
duration.toMinutesPart()      // Minutes component

// Manipulate
d1.plus(d2)
d1.multipliedBy(2)
```

---

## Period (Date-Based)

```java
// Create
Period.ofYears(1)
Period.ofMonths(6)
Period.ofDays(10)
Period.of(1, 6, 15)           // 1 year, 6 months, 15 days
Period.between(date1, date2)
Period.parse("P1Y6M15D")

// Get components
period.getYears()
period.getMonths()
period.getDays()
period.toTotalMonths()
```

---

## ChronoUnit

```java
// Calculate difference
ChronoUnit.DAYS.between(date1, date2)
ChronoUnit.HOURS.between(time1, time2)
ChronoUnit.MONTHS.between(date1, date2)

// Common units
ChronoUnit.NANOS
ChronoUnit.MILLIS
ChronoUnit.SECONDS
ChronoUnit.MINUTES
ChronoUnit.HOURS
ChronoUnit.DAYS
ChronoUnit.WEEKS
ChronoUnit.MONTHS
ChronoUnit.YEARS
```

---

## Formatting

```java
// Predefined formatters
DateTimeFormatter.ISO_LOCAL_DATE       // 2024-01-15
DateTimeFormatter.ISO_LOCAL_TIME       // 14:30:45
DateTimeFormatter.ISO_LOCAL_DATE_TIME  // 2024-01-15T14:30:45

// Custom pattern
DateTimeFormatter.ofPattern("dd/MM/yyyy")
DateTimeFormatter.ofPattern("dd-MMM-yyyy")      // 15-Jan-2024
DateTimeFormatter.ofPattern("EEEE, MMMM d")     // Monday, January 15
DateTimeFormatter.ofPattern("h:mm a")           // 2:30 PM
DateTimeFormatter.ofPattern("HH:mm:ss")         // 14:30:45

// Format
date.format(formatter)
dt.format(formatter)

// Parse
LocalDate.parse("15/01/2024", formatter)
LocalDateTime.parse("15-01-2024 14:30", formatter)
```

---

## Pattern Letters

| Letter | Meaning | Example |
|--------|---------|---------|
| y | Year | 2024 |
| M | Month | 1, 01, Jan, January |
| d | Day of month | 15 |
| E | Day of week | Mon, Monday |
| H | Hour (0-23) | 14 |
| h | Hour (1-12) | 2 |
| m | Minute | 30 |
| s | Second | 45 |
| a | AM/PM | PM |
| z | Zone name | IST |
| Z | Zone offset | +0530 |

---

## TemporalAdjusters

```java
import java.time.temporal.TemporalAdjusters;

date.with(TemporalAdjusters.firstDayOfMonth())
date.with(TemporalAdjusters.lastDayOfMonth())
date.with(TemporalAdjusters.firstDayOfYear())
date.with(TemporalAdjusters.lastDayOfYear())
date.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))
date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
date.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY))
```

---

## Common Operations

```java
// Age calculation
Period.between(birthDate, LocalDate.now()).getYears()

// Days between
ChronoUnit.DAYS.between(date1, date2)

// Is weekend?
dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY

// Is leap year?
date.isLeapYear()

// Days in month
date.lengthOfMonth()

// Compare
date1.isBefore(date2)
date1.isAfter(date2)

// Is past/future?
date.isBefore(LocalDate.now())
date.isAfter(LocalDate.now())
```

---

## Legacy Conversion

```java
// Date <-> Instant
Date.from(instant)
date.toInstant()

// Date <-> LocalDateTime
LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())

// sql.Date <-> LocalDate
java.sql.Date.valueOf(localDate)
sqlDate.toLocalDate()

// Timestamp <-> LocalDateTime
Timestamp.valueOf(localDateTime)
timestamp.toLocalDateTime()
```

---

## Quick Reference

| Task | Code |
|------|------|
| Current date | `LocalDate.now()` |
| Current time | `LocalTime.now()` |
| Current datetime | `LocalDateTime.now()` |
| Parse date | `LocalDate.parse("2024-01-15")` |
| Format date | `date.format(formatter)` |
| Add days | `date.plusDays(10)` |
| Subtract months | `date.minusMonths(1)` |
| Days between | `ChronoUnit.DAYS.between(d1, d2)` |
| Age in years | `Period.between(birth, now).getYears()` |
| Convert timezone | `zdt.withZoneSameInstant(zone)` |
| First of month | `date.withDayOfMonth(1)` |
| Last of month | `date.with(TemporalAdjusters.lastDayOfMonth())` |

---

[Back to Guide](../guide.md) | [Full Documentation](../documentation/09-date-time.md)
