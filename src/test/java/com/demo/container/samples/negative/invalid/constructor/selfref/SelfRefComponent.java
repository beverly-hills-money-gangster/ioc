package com.demo.container.samples.negative.invalid.constructor.selfref;

import com.demo.annotation.Component;


@Component
public class SelfRefComponent {

  // components can't reference themselves
  public SelfRefComponent(SelfRefComponent refComponent) {

  }

}
