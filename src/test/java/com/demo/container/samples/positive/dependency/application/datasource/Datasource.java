package com.demo.container.samples.positive.dependency.application.datasource;

public abstract class Datasource {

  public abstract void set(String key, Object value);

  public abstract <T> T get(String key);

  public abstract boolean isConnected();

}
