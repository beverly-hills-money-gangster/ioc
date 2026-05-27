package com.demo.container.samples.positive.dependency.application.datasource;

import com.demo.annotation.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryDatasource implements Datasource {

  private final Map<String, Object> data = new HashMap<>();

  @Override
  public void set(String key, Object value) {
    data.put(key, value);
  }

  @Override
  public <T> T get(String key) {
    return (T) data.get(key);
  }

  @Override
  public boolean isConnected() {
    return true;
  }
}
