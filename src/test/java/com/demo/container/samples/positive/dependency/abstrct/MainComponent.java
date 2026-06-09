package com.demo.container.samples.positive.dependency.abstrct;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(AbstractDependencyComponent component) {
    Objects.requireNonNull(component);
  }

}
