package com.demo.validation;

import java.util.Map;
import java.util.Set;

public class DependencyGraphValidator implements Validator<Map<Class<?>, Set<Class<?>>>> {

  @Override
  public void validate(final Map<Class<?>, Set<Class<?>>> dependencyGraph) {
    dependencyGraph.forEach((clazz, dependencies) -> {
      if (dependencies.contains(clazz)) {
        throw new IllegalStateException("Component can't be dependent on itself. Check class: %s"
            .formatted(clazz.getCanonicalName()));
      }
      for (var dependency : dependencies) {
        if (dependencyGraph.keySet().stream().noneMatch(dependency::isAssignableFrom)) {
          throw new IllegalStateException("Class %s dependency %s is not registered".formatted(
              clazz.getCanonicalName(), dependency.getCanonicalName()));
        }
      }
    });
  }
}
