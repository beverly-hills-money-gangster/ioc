package com.demo.dependency;

import com.demo.converter.DependencyTreeToQueue;
import com.demo.util.ReflectionUtil;
import com.demo.validation.ComponentValidator;
import com.demo.validation.DependencyGraphValidator;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyGraph implements Cloneable {

  private final DependencyTreeToQueue dependencyTreeToQueue = new DependencyTreeToQueue();

  private final ComponentValidator componentClassValidator = new ComponentValidator();

  private final Map<Class<?>, Set<Class<?>>> dependencyGraph;

  private DependencyGraph(Map<Class<?>, Set<Class<?>>> dependencyGraph) {
    this.dependencyGraph = dependencyGraph;
  }

  public DependencyGraph(final Set<Class<?>> classes) {
    // key -> node, values -> adjacent nodes
    dependencyGraph = new HashMap<>();
    classes.forEach(clazz -> {
      componentClassValidator.validate(clazz);
      var currentDependencies = new HashSet<Class<?>>();
      var previous = dependencyGraph.putIfAbsent(clazz, currentDependencies);
      if (previous != null) {
        throw new IllegalStateException(
            "Can't handle same class twice. Check class: %s".formatted(clazz.getCanonicalName()));
      }
      var constructor = ReflectionUtil.getMainConstructor(clazz);
      var parameterTypes = constructor.getParameterTypes();
      for (int i = 0; i < parameterTypes.length; i++) {
        var parameterType = parameterTypes[i];
        if (parameterType.isArray()) {
          var arrayType = ReflectionUtil.getArrayType(parameterType);
          currentDependencies.addAll(ReflectionUtil.getClassesForParent(arrayType, classes));
        } else if (Collection.class.isAssignableFrom(parameterType)) {
          var collectionItemClass = ReflectionUtil.getCollectionTypeFromConstructor(constructor, i);
          currentDependencies.addAll(
              ReflectionUtil.getClassesForParent(collectionItemClass, classes));
        } else if (parameterType.isInterface() || Modifier.isAbstract(
            parameterType.getModifiers())) {
          var dependencies = ReflectionUtil.getClassesForParent(parameterType, classes);
          if (dependencies.size() != 1) {
            throw new IllegalStateException(
                "Only 1 instance of %s expected. Got %s registered.".formatted(
                    parameterType.getCanonicalName(), dependencies.size()));
          } else {
            currentDependencies.add(dependencies.stream().findFirst().orElseThrow());
          }
        } else {
          currentDependencies.add(parameterType);
        }
      }
    });
    DependencyGraphValidator dependencyGraphValidator = new DependencyGraphValidator();
    dependencyGraphValidator.validate(dependencyGraph);
  }

  public Queue<Class<?>> traverse() {
    return dependencyTreeToQueue.convert(this);
  }

  public boolean isEmpty() {
    return dependencyGraph.isEmpty();
  }

  public Stream<Entry<Class<?>, Set<Class<?>>>> stream() {
    return dependencyGraph.entrySet().stream();
  }

  public void remove(Class<?> clazz) {
    dependencyGraph.remove(clazz);
  }

  @Override
  public String toString() {
    List<String> dependenciesStrings = new ArrayList<>();
    for (Map.Entry<Class<?>, Set<Class<?>>> entry : dependencyGraph.entrySet()) {
      String builder = entry.getKey().getCanonicalName() + "->" +
          entry.getValue().stream().map(Class::getCanonicalName).collect(Collectors.toSet());
      dependenciesStrings.add(builder);
    }
    return String.join("\n", dependenciesStrings);
  }

  @Override
  public DependencyGraph clone() {
    var dependencyHardCopy = new HashMap<Class<?>, Set<Class<?>>>();
    dependencyGraph.forEach(
        (key, values) -> dependencyHardCopy.put(key, new HashSet<>(values)));
    return new DependencyGraph(dependencyHardCopy);
  }
}
