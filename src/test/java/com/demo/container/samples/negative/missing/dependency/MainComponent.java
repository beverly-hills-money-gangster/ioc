package com.demo.container.samples.negative.missing.dependency;

import com.demo.annotation.Component;

@Component
public class MainComponent {

  public MainComponent(MissingComponent missingComponent) {

  }

}
