package com.demo.converter;

import com.demo.dependency.DependencyGraph;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Converts a dependency graph to a queue(FIFO). First component - first to be initialized.
 */
public class DependencyTreeToQueue implements
    Converter<DependencyGraph, Queue<Class<?>>> {

  @Override
  public Queue<Class<?>> convert(DependencyGraph dependencyGraph) {
    return traverseGraph(dependencyGraph.deepCopy(), new ArrayDeque<>());
  }

  /**
   * Traverses a dependency graph
   */
  private static Queue<Class<?>> traverseGraph(
      final DependencyGraph dependencyGraph,
      final Queue<Class<?>> accumulator) {
    // if graph is empty, then traversal is complete
    if (dependencyGraph.isEmpty()) {
      return accumulator;
    }
    // find all classes that don't have any dependency
    var solvedDependencyClasses = dependencyGraph.stream()
        .filter(entry -> entry.getValue().isEmpty())
        .map(Map.Entry::getKey).collect(Collectors.toSet());
    // if the graph is non-empty, and we can't find a class with no dependency,
    // then we likely have a cyclic dependency
    if (solvedDependencyClasses.isEmpty()) {
      throw new IllegalStateException(
          "Can't traverse dependency graph. Likely a cycle detected. Check tree %n%s".formatted(
              dependencyGraph));
    }
    // add all classes to the accumulator
    accumulator.addAll(solvedDependencyClasses);

    // remove added classes from the graph as we have already traversed them
    for (var solvedDependencyClass : solvedDependencyClasses) {
      dependencyGraph.remove(solvedDependencyClass);
    }
    // remove added classes from other classes dependencies as we have already traversed them
    dependencyGraph.stream().forEach(entry -> {
      for (var solvedDependencyClass : solvedDependencyClasses) {
        entry.getValue().remove(solvedDependencyClass);
      }
    });
    // recursion
    return traverseGraph(dependencyGraph, accumulator);
  }


}
