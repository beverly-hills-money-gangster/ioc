package com.demo.container.samples.positive.dependency.profile;

import com.demo.annotation.Component;
import java.util.Objects;

@Component
public class MainComponent {

  public MainComponent(AbstractDependency abstractDependency) {
    Objects.requireNonNull(abstractDependency);
  }

}
