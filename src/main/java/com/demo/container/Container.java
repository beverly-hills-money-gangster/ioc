package com.demo.container;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Container implements ContainerReader, Closeable {

  private final Map<Class<?>, Object> container = new ConcurrentHashMap<>();

  @Override
  public <T> T getInstance(final Class<T> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Instance can't be null");
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
        // TODO log and go on
        throw new RuntimeException(e);
      }
    }
  }
}
