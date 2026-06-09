package com.demo.container.samples.negative.missing.component;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(SomeDependency component) {
    Objects.requireNonNull(component);
  }

}
