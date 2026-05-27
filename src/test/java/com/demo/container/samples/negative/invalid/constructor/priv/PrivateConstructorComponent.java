package com.demo.container.samples.negative.invalid.constructor.priv;

import com.demo.annotation.Component;

// missing public constructor
@Component
public class PrivateConstructorComponent {


  private PrivateConstructorComponent() {

  }

}
