package com.demo.container.samples.positive.dependency.application.datasource;

public interface Datasource {

  void set(String key, Object value);

  <T> T get(String key);

  boolean isConnected();

}
