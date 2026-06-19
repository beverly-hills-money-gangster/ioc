package com.demo.container.samples.positive.dependency.primary.concrete;

import com.demo.annotation.Component;
import com.demo.annotation.Primary;

@Primary
@Component
public class SomePrimaryComponent extends SomeComponent {

  @Override
  public void doSomething() {
    // do nothing
  }

}
