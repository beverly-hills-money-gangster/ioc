package com.demo.converter;

import com.demo.dependency.DependencyGraph;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class DependencyTreeToQueue implements
    Converter<DependencyGraph, Queue<Class<?>>> {

  @Override
  public Queue<Class<?>> convert(DependencyGraph dependencyGraph) {
    return traverseTree(dependencyGraph.clone(), new ArrayDeque<>());
  }

  private static Queue<Class<?>> traverseTree(
      final DependencyGraph dependencyGraph,
      final Queue<Class<?>> accumulator) {
    if (dependencyGraph.isEmpty()) {
      return accumulator;
    }
    var solvedDependencyClasses = dependencyGraph.stream()
        .filter(entry -> entry.getValue().isEmpty())
        .map(Map.Entry::getKey).collect(Collectors.toSet());
    if (solvedDependencyClasses.isEmpty()) {
      throw new IllegalStateException(
          "Can't traverse dependency graph. Likely a cycle detected. Check tree\n%s".formatted(
              dependencyGraph));
    }
    accumulator.addAll(solvedDependencyClasses);
    for (var solvedDependencyClass : solvedDependencyClasses) {
      dependencyGraph.remove(solvedDependencyClass);
    }
    dependencyGraph.stream().forEach(entry -> {
      for (var solvedDependencyClass : solvedDependencyClasses) {
        entry.getValue().remove(solvedDependencyClass);
      }
    });
    return traverseTree(dependencyGraph, accumulator);
  }


}
