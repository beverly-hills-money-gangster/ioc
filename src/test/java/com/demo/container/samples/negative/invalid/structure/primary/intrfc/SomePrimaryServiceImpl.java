package com.demo.container.samples.negative.invalid.structure.primary.intrfc;

import com.demo.annotation.Component;
import com.demo.annotation.Primary;

@Primary
@Component
public class SomePrimaryServiceImpl implements SomeService {

  @Override
  public void doSomething() {

  }
}
