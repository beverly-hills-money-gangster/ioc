package com.demo.container.samples.positive.dependency.collection.list;

import com.demo.annotation.Component;
import java.util.List;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(List<AbstractDependencyComponent> components) {
    Objects.requireNonNull(components);
    if (components.isEmpty()) {
      throw new IllegalStateException("No component found");
    }
  }
}
