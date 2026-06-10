package com.demo.container;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Container implements ContainerReader, Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(Container.class);

  private final Map<Class<?>, Object> container = new ConcurrentHashMap<>();

  @Override
  public <T> T getInstance(final Class<T> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Instance can't be null");
    }

    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
      var components = getInstances(clazz);
      if (components.isEmpty()) {
        throw new IllegalStateException(
            "No component of type %s found".formatted(clazz.getCanonicalName()));
      } else if (components.size() > 1) {
        throw new IllegalStateException(
            "Ambiguous dependency %s. Expected 1 candidate but found %s. See classes: %s".formatted(
                clazz.getCanonicalName(), components.size(),
                components.stream().map(object -> object.getClass().getCanonicalName())
                    .collect(Collectors.toSet())));
      }
      return components.getFirst();
    }

    return (T) Optional.ofNullable(container.get(clazz))
        .orElseThrow(()
            -> new IllegalStateException("No instance of class %s found"
            .formatted(clazz.getCanonicalName())));
  }

  @Override
  public <T> List<T> getInstances(final Class<T> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Instance can't be null");
    }
    return container.entrySet().stream()
        .filter(
            classObjectEntry -> clazz.isAssignableFrom(classObjectEntry.getKey()))
        .map(classObjectEntry -> (T) classObjectEntry.getValue())
        .collect(Collectors.toList());
  }

  @Override
  public <T> List<T> getInstancesNonEmpty(Class<T> clazz) {
    var instances = getInstances(clazz);
    if (instances == null || instances.isEmpty()) {
      throw new IllegalStateException(
          "Can't find instance of class %s".formatted(clazz.getCanonicalName()));
    }
    return instances;
  }

  @Override
  public boolean isEmpty() {
    return container.isEmpty();
  }


  public void register(final Object object) {
    var previousObject = container.putIfAbsent(object.getClass(), object);
    if (previousObject != null) {
      throw new IllegalArgumentException(
          "Can't register object of class %s because instance already exists in container"
              .formatted(object.getClass().getCanonicalName())
      );
    }
  }

  @Override
  public void close() {
    var componentsToClose = getInstances(Closeable.class);
    for (var closeable : componentsToClose) {
      try {
        closeable.close();
      } catch (IOException e) {
        LOG.error("Can't close component {}", closeable.getClass().getCanonicalName()e);
      }
    }
  }

  @Override
  public String toString() {
    return container.keySet().toString();
  }
}
