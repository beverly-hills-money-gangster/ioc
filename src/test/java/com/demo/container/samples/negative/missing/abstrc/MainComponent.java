package com.demo.container.samples.negative.missing.abstrc;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(AbstractDependency component) {
    Objects.requireNonNull(component);
  }
}
