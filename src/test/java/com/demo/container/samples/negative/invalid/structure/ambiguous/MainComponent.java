package com.demo.container.samples.negative.invalid.structure.ambiguous;

import com.demo.annotation.Component;

@Component
public class MainComponent {

  public MainComponent(InterfaceDependencyComponent component) {

  }
}
