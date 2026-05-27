package com.demo.container.samples.negative.invalid.structure.cycle.small;

import com.demo.annotation.Component;

@Component
public class ComponentB {

  public ComponentB(ComponentA componentA) {

  }
}
