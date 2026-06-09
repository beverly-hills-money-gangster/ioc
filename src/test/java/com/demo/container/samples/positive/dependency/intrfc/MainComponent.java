package com.demo.container.samples.positive.dependency.intrfc;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(InterfaceDependencyComponent component) {
    Objects.requireNonNull(component);
  }

}
