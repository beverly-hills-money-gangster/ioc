package com.demo.container.samples.positive.dependency.application.health;

import com.demo.annotation.Component;

@Component
public class PingHealthCheck implements HealthCheck {

  @Override
  public boolean isHealthy() {
    return true;
  }
}
