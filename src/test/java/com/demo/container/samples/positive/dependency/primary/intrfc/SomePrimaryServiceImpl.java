package com.demo.container.samples.positive.dependency.primary.intrfc;

import com.demo.annotation.Component;
import com.demo.annotation.Primary;

@Primary
@Component
public class SomePrimaryServiceImpl implements SomeService {

  @Override
  public void doSomething() {

  }
}
