package com.demo.container.samples.negative.invalid.structure.cycle.big;

import com.demo.annotation.Component;

@Component
public class ComponentC {

  public ComponentC(ComponentD component) {

  }
}
