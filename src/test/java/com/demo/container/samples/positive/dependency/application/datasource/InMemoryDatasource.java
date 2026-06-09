package com.demo.container.samples.positive.dependency.application.datasource;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;
import java.util.HashMap;
import java.util.Map;

@Profile(profiles = "default")
@Component
public class InMemoryDatasource extends Datasource {

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

  @Override
  public void close() {
    data.clear();
  }
}
