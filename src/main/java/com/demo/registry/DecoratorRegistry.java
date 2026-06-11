package com.demo.registry;

import com.demo.decorator.Decorator;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DecoratorRegistry implements DecoratorRegistryReader {

  private final Map<Class<?>, Decorator> decorators = new ConcurrentHashMap<>();

  /**
   * Add a component decorator. Pretty useful for mocking/stubbing components for testing.
   */
  public void addDecorator(final Class<?> clazz, Decorator decorator) {
    if (clazz == null) {
      throw new IllegalArgumentException("Can't decorate null");
    } else if (decorator == null) {
      throw new IllegalArgumentException("null can't be a decorator");
    }
    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
      throw new IllegalStateException(
          "Can't decorate class %s. Only concrete implementation can be decorated.".formatted(
              clazz.getCanonicalName()));
    }
    Optional.ofNullable(decorators.putIfAbsent(clazz, decorator)).ifPresent(existingDecorator -> {
      throw new IllegalStateException(
          "Can't decorate class %s. Only one decorator per class is allowed. Check decorator %s"
              .formatted(clazz.getCanonicalName(),
                  existingDecorator.getClass().getCanonicalName()));
    });
  }

  @Override
  public Optional<Decorator> getDecorator(Class<?> clazz) {
    return Optional.ofNullable(decorators.get(clazz));
  }
}
