# IoC — Simple Ultra-Lightweight Dependency Injection Framework

[![](https://jitpack.io/v/beverly-hills-money-gangster/ioc.svg)](https://jitpack.io/#beverly-hills-money-gangster/ioc)


A tiny, fast, and test-friendly dependency injection framework inspired by Spring.

## Features

* `@Component`, `@Profile` and `@Primary` annotation support
* Constructor-based dependency injection
* Automatic cyclic dependency detection
* Interface and abstract class resolution
* Collection injection (`List<T>` and `T[]`)
* Automatic resource cleanup
* Lightweight and easy to test

---

# Getting Started

Annotate your classes with `@Component` and the framework will automatically register and initialize them inside the IoC container.

## Example

```java
@Component
public class MyService {

    private final DAORepository repository;
    private final List<EventListener> listeners;

    public MyService(DAORepository repository,
                     List<EventListener> listeners) {
        this.repository = repository;
        this.listeners = listeners;
    }

    public void runMyLogic(Event event) {
        repository.save(event);

        for (var listener : listeners) {
            listener.notify(event);
        }
    }
}
```

```java
public class Main {

    public static void main(String[] args) {

        try (var container =
                     new ContainerInitializer().init(Main.class)) {

            var service = container.getInstance(MyService.class);

            service.runMyLogic(new Event());
        }
    }
}
```

---

# How It Works

The framework scans the classpath for classes annotated with `@Component`,
builds a dependency graph, validates it, and initializes all objects automatically.

---

# Constructor Rules

Each `@Component` class must contain exactly one public constructor.

Constructor arguments may be:

* Concrete classes
* Interfaces
* Abstract classes
* `List<T>`
* `T[]`

---

# Dependency Resolution Rules

## Single Dependency

If a constructor parameter is not a collection (`List` or array):

* exactly one matching component must exist
* if multiple candidates are found, an exception is thrown
* if no candidate is found, an exception is thrown

## Collection Dependency

For `List<T>` and `T[]` injections:

* all matching implementations are injected
* if no implementations are found, an exception is thrown

---

# Profiles

Use `@Profile` when multiple implementations of the same interface or abstract class exist.

This allows selecting different implementations for different environments or configurations.

Use the `ACTIVE_PROFILES` environment variable to specify active profiles.

The format is comma-separated:

```
ACTIVE_PROFILES=default,test
```

If multiple components implement the same interface or abstract class,
the framework will prefer the component whose `@Profile` matches one of the active profiles.

Example:

```java
@Component
@Profile(profiles = "test")
public class TestRepository implements Repository {
}
```

```java
@Component
@Profile(profiles = "prod")
public class ProdRepository implements Repository {
}
```

With:

```
ACTIVE_PROFILES=test
```

the `TestRepository` component will be selected.

Multiple profiles can also be specified on a single component:

```java
@Profile(profiles = {"test", "dev"})
```

This means the component will be created if either the `test` or `dev`
profile is active.


---

# Resource Management

All components implementing `Closeable` or `AutoCloseable`
are automatically closed when the container is closed.

```java
container.close();
```

or via try-with-resources:

```java
try (var container = new ContainerInitializer().init(Main.class)) {
    ...
}
```

---

# Limitations

* Primitive type injection is not supported
* Field injection is not supported
* Setter injection is not supported
* Components must use constructor injection only


# Sample project

https://github.com/beverly-hills-money-gangster/web