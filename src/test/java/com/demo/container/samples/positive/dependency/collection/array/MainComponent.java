package com.demo.container.samples.positive.dependency.collection.array;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(AbstractDependencyComponent[] components) {
    Objects.requireNonNull(components);
    if (components.length == 0) {
      throw new IllegalStateException("No component found");
    }
  }
}
