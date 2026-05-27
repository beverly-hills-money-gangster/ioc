package com.demo.container.samples.positive.dependency.application.health;

import com.demo.annotation.Component;
import com.demo.container.samples.positive.dependency.application.datasource.InMemoryDatasource;

@Component
public class InMemoryDatasourceHealthCheck implements HealthCheck {

  private final InMemoryDatasource inMemoryDatasource;

  public InMemoryDatasourceHealthCheck(InMemoryDatasource inMemoryDatasource) {
    this.inMemoryDatasource = inMemoryDatasource;
  }

  @Override
  public boolean isHealthy() {
    return inMemoryDatasource.isConnected();
  }
}
