# Inner Classes

[<- Previous: Abstraction](14-abstraction.md) | [Next: Exception Handling ->](16-exception-handling.md) | [Back to Guide](../guide.md)

**Cheat Sheet:** [Inner Classes Cheat Sheet](../cheatsheets/inner-classes-cheatsheet.md)

---

## Overview

Inner classes are classes defined within another class. Java supports four types of nested classes, each with different characteristics and use cases.

**Types of Nested Classes:**

| Type | Description |
|------|-------------|
| Static Nested Class | Static class inside another class |
| Inner Class (Non-static) | Non-static class inside another class |
| Local Class | Class defined inside a method |
| Anonymous Class | Unnamed class defined and instantiated in one expression |

```java
public class OuterClass {
    // Static nested class
    static class StaticNested { }
    
    // Inner class (non-static nested class)
    class Inner { }
    
    void method() {
        // Local class
        class Local { }
        
        // Anonymous class
        Runnable r = new Runnable() {
            @Override
            public void run() { }
        };
    }
}
```

---

## Static Nested Classes

A static nested class is associated with its outer class, not with an instance of the outer class. It can access only static members of the outer class.

### Basic Static Nested Class

```java
public class University {
    private static String universityName = "State University";
    private String campus = "Main Campus";
    
    // Static nested class
    public static class Department {
        private String name;
        private int facultyCount;
        
        public Department(String name, int facultyCount) {
            this.name = name;
            this.facultyCount = facultyCount;
        }
        
        public void displayInfo() {
            // Can access static members of outer class
            System.out.println(universityName + " - " + name);
            
            // Cannot access non-static members
            // System.out.println(campus);  // Error!
        }
        
        public String getName() { return name; }
    }
    
    public static void main(String[] args) {
        // Create static nested class without outer instance
        University.Department cs = new University.Department("Computer Science", 25);
        cs.displayInfo();  // "State University - Computer Science"
    }
}

// From outside the class
University.Department math = new University.Department("Mathematics", 15);
```

### Static Nested Class with Private Access

```java
public class Database {
    private static String connectionString = "jdbc:mysql://localhost/db";
    
    // Private static nested class - only accessible within Database
    private static class Connection {
        void connect() {
            System.out.println("Connecting to: " + connectionString);
        }
    }
    
    // Public nested class that uses private nested class
    public static class ConnectionPool {
        private List<Connection> connections = new ArrayList<>();
        
        public void initialize(int size) {
            for (int i = 0; i < size; i++) {
                connections.add(new Connection());
            }
        }
    }
}
```

### Builder Pattern with Static Nested Class

```java
public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    
    // Private constructor - use Builder
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = new HashMap<>(builder.headers);
        this.body = builder.body;
    }
    
    // Getters
    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return Collections.unmodifiableMap(headers); }
    public String getBody() { return body; }
    
    // Static nested Builder class
    public static class Builder {
        private final String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body;
        
        public Builder(String url) {
            this.url = Objects.requireNonNull(url);
        }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}

// Usage
HttpRequest request = new HttpRequest.Builder("https://api.example.com")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("{\"key\": \"value\"}")
    .build();
```

### Static Nested Interface and Enum

```java
public class Order {
    private Status status;
    private List<Item> items;
    
    // Static nested enum
    public static enum Status {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }
    
    // Static nested interface
    public static interface PriceCalculator {
        double calculate(List<Item> items);
    }
    
    // Static nested record
    public static record Item(String name, int quantity, double price) {
        public double total() {
            return quantity * price;
        }
    }
    
    public Order() {
        this.status = Status.PENDING;
        this.items = new ArrayList<>();
    }
    
    public void addItem(String name, int quantity, double price) {
        items.add(new Item(name, quantity, price));
    }
    
    public double getTotal(PriceCalculator calculator) {
        return calculator.calculate(items);
    }
}

// Usage
Order order = new Order();
order.addItem("Book", 2, 29.99);
order.addItem("Pen", 5, 1.99);

Order.Status status = Order.Status.PENDING;
Order.Item item = new Order.Item("Notebook", 3, 4.99);

double total = order.getTotal(items -> 
    items.stream().mapToDouble(Order.Item::total).sum()
);
```

---

## Inner Classes (Non-Static)

An inner class is associated with an instance of the outer class and has access to all members of the outer class, including private ones.

### Basic Inner Class

```java
public class ShoppingCart {
    private List<String> items = new ArrayList<>();
    private double discount = 0.0;
    
    public void addItem(String item) {
        items.add(item);
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    // Inner class - has access to outer class members
    public class CartIterator {
        private int index = 0;
        
        public boolean hasNext() {
            return index < items.size();  // Access outer's items
        }
        
        public String next() {
            return items.get(index++);  // Access outer's items
        }
        
        public double getDiscount() {
            return discount;  // Access outer's discount
        }
    }
    
    public CartIterator iterator() {
        return new CartIterator();
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
cart.addItem("Apple");
cart.addItem("Banana");
cart.setDiscount(0.1);

// Create inner class instance through outer instance
ShoppingCart.CartIterator iter = cart.iterator();
// Or: ShoppingCart.CartIterator iter = cart.new CartIterator();

while (iter.hasNext()) {
    System.out.println(iter.next());
}
```

### Accessing Outer Class Members

```java
public class Outer {
    private int value = 10;
    
    public class Inner {
        private int value = 20;
        
        public void display() {
            int value = 30;
            
            System.out.println(value);              // 30 (local)
            System.out.println(this.value);         // 20 (inner's field)
            System.out.println(Outer.this.value);   // 10 (outer's field)
        }
        
        public void modifyOuter() {
            Outer.this.value = 100;  // Modify outer's private field
        }
    }
}

Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();
inner.display();
inner.modifyOuter();
```

### Inner Class Instantiation

```java
public class OuterClass {
    public class InnerClass {
        public void hello() {
            System.out.println("Hello from inner!");
        }
    }
    
    public InnerClass createInner() {
        return new InnerClass();  // Inside outer, can use new directly
    }
}

// Method 1: Through outer instance method
OuterClass outer1 = new OuterClass();
OuterClass.InnerClass inner1 = outer1.createInner();

// Method 2: Using outer.new syntax
OuterClass outer2 = new OuterClass();
OuterClass.InnerClass inner2 = outer2.new InnerClass();

// Method 3: Chained (less common)
OuterClass.InnerClass inner3 = new OuterClass().new InnerClass();
```

### Multiple Levels of Nesting

```java
public class Level1 {
    private String l1 = "Level 1";
    
    public class Level2 {
        private String l2 = "Level 2";
        
        public class Level3 {
            private String l3 = "Level 3";
            
            public void printAll() {
                System.out.println(Level1.this.l1);  // Access Level 1
                System.out.println(Level2.this.l2);  // Access Level 2
                System.out.println(this.l3);         // Access Level 3
            }
        }
    }
}

Level1 l1 = new Level1();
Level1.Level2 l2 = l1.new Level2();
Level1.Level2.Level3 l3 = l2.new Level3();
l3.printAll();
```

### Inner Class Implementing Interface

```java
public class TaskManager {
    private List<Runnable> tasks = new ArrayList<>();
    private boolean running = true;
    
    public void addTask(String name, Runnable action) {
        tasks.add(new ManagedTask(name, action));
    }
    
    // Inner class implementing Runnable
    private class ManagedTask implements Runnable {
        private String name;
        private Runnable action;
        
        ManagedTask(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }
        
        @Override
        public void run() {
            if (running) {  // Access outer's running
                System.out.println("Executing: " + name);
                action.run();
            }
        }
    }
    
    public void executeAll() {
        for (Runnable task : tasks) {
            task.run();
        }
    }
    
    public void stop() {
        running = false;
    }
}
```

---

## Local Classes

A local class is defined inside a method, constructor, or initializer block. It has access to the enclosing scope but can only access local variables that are effectively final.

### Basic Local Class

```java
public class Calculator {
    public double calculate(double[] values, String operation) {
        // Local class defined inside method
        class Operation {
            private String op;
            
            Operation(String op) {
                this.op = op;
            }
            
            double apply(double[] vals) {
                return switch (op) {
                    case "sum" -> {
                        double sum = 0;
                        for (double v : vals) sum += v;
                        yield sum;
                    }
                    case "avg" -> {
                        double sum = 0;
                        for (double v : vals) sum += v;
                        yield sum / vals.length;
                    }
                    case "max" -> {
                        double max = Double.MIN_VALUE;
                        for (double v : vals) if (v > max) max = v;
                        yield max;
                    }
                    case "min" -> {
                        double min = Double.MAX_VALUE;
                        for (double v : vals) if (v < min) min = v;
                        yield min;
                    }
                    default -> throw new IllegalArgumentException("Unknown: " + op);
                };
            }
        }
        
        Operation op = new Operation(operation);
        return op.apply(values);
    }
}

Calculator calc = new Calculator();
double[] values = {1, 2, 3, 4, 5};
System.out.println(calc.calculate(values, "sum"));  // 15.0
System.out.println(calc.calculate(values, "avg"));  // 3.0
```

### Accessing Enclosing Scope

```java
public class MessageProcessor {
    private String prefix = "[INFO]";
    
    public void process(String message) {
        // Local variable - must be effectively final
        String timestamp = LocalDateTime.now().toString();
        
        // Local class
        class Formatter {
            String format() {
                // Can access:
                // - Outer class members (prefix)
                // - Effectively final local variables (timestamp)
                return prefix + " " + timestamp + " - " + message;
            }
        }
        
        Formatter formatter = new Formatter();
        System.out.println(formatter.format());
        
        // This would cause error - modifying makes timestamp not effectively final
        // timestamp = "modified";
    }
}
```

### Local Class vs Anonymous Class

```java
public class EventHandler {
    public void setupHandlers(Button button) {
        // Local class - named, reusable within method
        class ClickHandler implements ActionListener {
            private int clickCount = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                clickCount++;
                System.out.println("Clicked " + clickCount + " times");
            }
            
            public int getClickCount() {
                return clickCount;
            }
        }
        
        ClickHandler handler = new ClickHandler();
        button.addActionListener(handler);
        
        // Can access local class methods
        // handler.getClickCount();
    }
    
    public void setupSimpleHandler(Button button) {
        // Anonymous class - for simple one-off implementation
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked");
            }
        });
        
        // Or even simpler with lambda
        button.addActionListener(e -> System.out.println("Button clicked"));
    }
}
```

### Local Class in Constructor

```java
public class DataLoader {
    private List<String> data;
    
    public DataLoader(String source) {
        // Local class in constructor
        class SourceReader {
            List<String> read(String src) {
                List<String> lines = new ArrayList<>();
                // Read from source
                lines.add("Line 1 from " + src);
                lines.add("Line 2 from " + src);
                return lines;
            }
        }
        
        SourceReader reader = new SourceReader();
        this.data = reader.read(source);
    }
    
    public List<String> getData() {
        return new ArrayList<>(data);
    }
}
```

---

## Anonymous Classes

An anonymous class is a class without a name that is declared and instantiated in a single expression. It's typically used for one-time implementations of interfaces or abstract classes.

### Basic Anonymous Class

```java
public class AnonymousExample {
    public void demonstrate() {
        // Anonymous class implementing interface
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running anonymously!");
            }
        };
        
        new Thread(runnable).start();
        
        // Or inline
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Also running anonymously!");
            }
        }).start();
    }
}
```

### Anonymous Class Extending Class

```java
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public abstract void makeSound();
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

public class Zoo {
    public void createAnimals() {
        // Anonymous class extending abstract class
        Animal dog = new Animal("Buddy") {
            @Override
            public void makeSound() {
                System.out.println(name + " says: Woof!");
            }
            
            // Can add methods, but can't call from outside
            public void fetch() {
                System.out.println(name + " fetches the ball");
            }
        };
        
        dog.makeSound();  // "Buddy says: Woof!"
        dog.sleep();      // "Buddy is sleeping"
        // dog.fetch();   // Error! Method not in Animal type
        
        // Anonymous class extending concrete class
        Thread customThread = new Thread("CustomThread") {
            @Override
            public void run() {
                System.out.println("Custom thread running: " + getName());
            }
        };
        customThread.start();
    }
}
```

### Anonymous Class Implementing Multiple Methods

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
    boolean equals(Object obj);
}

public class SortExample {
    public void sortPeople(List<Person> people) {
        // Anonymous class implementing interface
        Comparator<Person> byName = new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().compareTo(p2.getName());
            }
            
            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
        };
        
        Collections.sort(people, byName);
    }
}
```

### Anonymous Class with Instance Initializer

```java
public class MapExample {
    public Map<String, Integer> createMap() {
        // Anonymous class with instance initializer (double brace initialization)
        // Note: Creates extra class, use with caution
        return new HashMap<String, Integer>() {{
            put("one", 1);
            put("two", 2);
            put("three", 3);
        }};
        
        // Preferred: Use Map.of() instead (Java 9+)
        // return Map.of("one", 1, "two", 2, "three", 3);
    }
}
```

### Anonymous Class Accessing Outer Scope

```java
public class EventPublisher {
    private List<EventListener> listeners = new ArrayList<>();
    
    public void subscribe(String subscriberName) {
        // subscriberName must be effectively final
        listeners.add(new EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(subscriberName + " received: " + event);
            }
        });
    }
    
    public void publish(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
```

### Anonymous Class vs Lambda

```java
public class ComparisonExample {
    public void demonstrate() {
        // Anonymous class - verbose
        Comparator<String> comp1 = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        };
        
        // Lambda - concise (preferred for functional interfaces)
        Comparator<String> comp2 = (s1, s2) -> s1.length() - s2.length();
        
        // Method reference - even more concise
        Comparator<String> comp3 = Comparator.comparingInt(String::length);
        
        // Anonymous class needed when:
        // 1. Implementing non-functional interface
        // 2. Need to override multiple methods
        // 3. Need to maintain state
        // 4. Need access to 'this' of the anonymous class
        
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(this.getClass().getName());  // Anonymous class
            }
        };
        
        Runnable r2 = () -> {
            System.out.println(this.getClass().getName());  // Enclosing class
        };
    }
}
```

---

## Practical Examples

### Iterator Implementation

```java
public class CustomList<E> implements Iterable<E> {
    private Object[] elements;
    private int size = 0;
    
    public CustomList(int capacity) {
        elements = new Object[capacity];
    }
    
    public void add(E element) {
        if (size < elements.length) {
            elements[size++] = element;
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        // Anonymous class implementing Iterator
        return new Iterator<E>() {
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < size;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[index++];
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

// Usage
CustomList<String> list = new CustomList<>(10);
list.add("A");
list.add("B");
list.add("C");

for (String item : list) {
    System.out.println(item);
}
```

### Event Handling System

```java
public class Button {
    private String label;
    private List<ClickListener> listeners = new ArrayList<>();
    
    // Inner interface
    public interface ClickListener {
        void onClick(Button button);
    }
    
    // Static nested class for event
    public static class ClickEvent {
        private final Button source;
        private final LocalDateTime timestamp;
        
        public ClickEvent(Button source) {
            this.source = source;
            this.timestamp = LocalDateTime.now();
        }
        
        public Button getSource() { return source; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public Button(String label) {
        this.label = label;
    }
    
    public void addClickListener(ClickListener listener) {
        listeners.add(listener);
    }
    
    public void click() {
        System.out.println("Button '" + label + "' clicked");
        for (ClickListener listener : listeners) {
            listener.onClick(this);
        }
    }
    
    public String getLabel() { return label; }
}

// Usage with different inner class types
public class Application {
    private int totalClicks = 0;
    
    public void setup() {
        Button button = new Button("Submit");
        
        // Anonymous class
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void onClick(Button b) {
                totalClicks++;  // Access outer field
                System.out.println("Total clicks: " + totalClicks);
            }
        });
        
        // Lambda (since ClickListener is functional interface)
        button.addClickListener(b -> System.out.println("Clicked: " + b.getLabel()));
        
        button.click();
    }
}
```

### State Machine with Inner Classes

```java
public class TrafficLight {
    private State currentState;
    
    // Inner interface for state
    public interface State {
        void enter(TrafficLight light);
        void exit(TrafficLight light);
        State next();
        String getColor();
    }
    
    // Static nested classes for each state
    public static class RedState implements State {
        @Override
        public void enter(TrafficLight light) {
            System.out.println("Light is RED - Stop!");
        }
        
        @Override
        public void exit(TrafficLight light) {
            System.out.println("Red light ending...");
        }
        
        @Override
        public State next() {
            return new GreenState();
        }
        
        @Override
        public String getColor() { return "RED"; }
    }
    
    public static class GreenState implements State {
        @Override
        public void enter(TrafficLight light) {
            System.out.println("Light is GREEN - Go!");
        }
        
        @Override
        public void exit(TrafficLight light) {
            System.out.println("Green light ending...");
        }
        
        @Override
        public State next() {
            return new YellowState();
        }
        
        @Override
        public String getColor() { return "GREEN"; }
    }
    
    public static class YellowState implements State {
        @Override
        public void enter(TrafficLight light) {
            System.out.println("Light is YELLOW - Caution!");
        }
        
        @Override
        public void exit(TrafficLight light) {
            System.out.println("Yellow light ending...");
        }
        
        @Override
        public State next() {
            return new RedState();
        }
        
        @Override
        public String getColor() { return "YELLOW"; }
    }
    
    public TrafficLight() {
        this.currentState = new RedState();
        currentState.enter(this);
    }
    
    public void change() {
        currentState.exit(this);
        currentState = currentState.next();
        currentState.enter(this);
    }
    
    public String getCurrentColor() {
        return currentState.getColor();
    }
}

// Usage
TrafficLight light = new TrafficLight();
light.change();  // RED -> GREEN
light.change();  // GREEN -> YELLOW
light.change();  // YELLOW -> RED
```

### Linked List with Inner Node Class

```java
public class LinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;
    
    // Private static nested class for node
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;
        
        Node(E data) {
            this.data = data;
        }
    }
    
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    
    public E removeFirst() {
        if (head == null) throw new NoSuchElementException();
        E data = head.data;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return data;
    }
    
    public int size() {
        return size;
    }
    
    // Inner class for iterator - needs access to head
    public class ListIterator implements Iterator<E> {
        private Node<E> current = head;
        
        @Override
        public boolean hasNext() {
            return current != null;
        }
        
        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E data = current.data;
            current = current.next;
            return data;
        }
    }
    
    public Iterator<E> iterator() {
        return new ListIterator();
    }
}
```

---

## Best Practices

### When to Use Each Type

```java
// 1. Static Nested Class - for logically grouped helper classes
public class Response {
    public static class Builder { }  // Related to Response, no outer access needed
    public static class Error { }    // Can exist without Response instance
}

// 2. Inner Class - when you need access to outer instance
public class Graph {
    private List<Node> nodes;
    
    public class Node {  // Needs access to Graph's nodes
        public List<Node> neighbors() {
            return nodes.stream()/*...*/;
        }
    }
}

// 3. Local Class - for complex one-off logic within method
public void processData(List<Item> items) {
    class ItemProcessor {
        // Complex processing logic used only here
    }
}

// 4. Anonymous Class - for simple one-off implementations
list.sort(new Comparator<String>() {
    public int compare(String a, String b) { return a.length() - b.length(); }
});

// 5. Lambda - preferred over anonymous class for functional interfaces
list.sort((a, b) -> a.length() - b.length());
```

### Guidelines

```java
// DO: Use static nested class when outer access not needed
public class Container {
    public static class Item { }  // Good
}

// DON'T: Use inner class when static would work
public class Container {
    public class Item { }  // Unnecessary outer reference
}

// DO: Keep nested classes small and focused
public class Order {
    public static record LineItem(String product, int quantity) { }
}

// DON'T: Create deeply nested hierarchies
public class A {
    class B {
        class C {
            class D { }  // Too deep!
        }
    }
}

// DO: Use lambdas for functional interfaces
Runnable r = () -> System.out.println("Hello");

// DON'T: Use anonymous class when lambda works
Runnable r = new Runnable() {  // Verbose
    public void run() { System.out.println("Hello"); }
};

// DO: Make nested classes private when possible
public class Cache {
    private static class Entry { }  // Implementation detail
}
```

---

## Summary

### Comparison Table

| Feature | Static Nested | Inner | Local | Anonymous |
|---------|---------------|-------|-------|-----------|
| Access to outer instance | No | Yes | Yes | Yes |
| Access to outer static | Yes | Yes | Yes | Yes |
| Access to local vars | N/A | N/A | Final only | Final only |
| Can have static members | Yes | No* | No | No |
| Can be instantiated outside | Yes | With outer | No | N/A |
| Has name | Yes | Yes | Yes | No |

*Inner classes can have static final constants

### Access Syntax

```java
// Static nested
OuterClass.StaticNested obj = new OuterClass.StaticNested();

// Inner class
OuterClass outer = new OuterClass();
OuterClass.Inner inner = outer.new Inner();

// Access outer from inner
OuterClass.this.field

// Local and anonymous
// Declared and used within their scope only
```

### When to Use

| Type | Use When |
|------|----------|
| Static Nested | Helper class, no outer access needed |
| Inner | Need access to outer instance members |
| Local | One-time use within a method, complex logic |
| Anonymous | One-time implementation, simple logic |
| Lambda | Functional interface, single method |

**Key Points:**
- Static nested classes don't need outer instance
- Inner classes have implicit reference to outer
- Local variables accessed by local/anonymous classes must be effectively final
- Prefer lambdas over anonymous classes for functional interfaces
- Keep nesting shallow for readability
- Make nested classes static when possible

---

[<- Previous: Abstraction](14-abstraction.md) | [Next: Exception Handling ->](16-exception-handling.md) | [Back to Guide](../guide.md)
