package com.demo.container.samples.positive.dependency.application.datasource;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;

@Component
@Profile(profiles = "fail")
public class FailingDatasource extends Datasource {

  @Override
  public void set(String key, Object value) {
    throw new IllegalStateException("Failure");
  }

  @Override
  public <T> T get(String key) {
    throw new IllegalStateException("Failure");
  }

  @Override
  public boolean isConnected() {
    return false;
  }
}
