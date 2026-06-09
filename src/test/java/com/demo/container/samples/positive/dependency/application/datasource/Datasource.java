package com.demo.container.samples.positive.dependency.application.datasource;

import java.io.Closeable;

public abstract class Datasource implements Closeable {

  public abstract void set(String key, Object value);

  public abstract <T> T get(String key);

  public abstract boolean isConnected();

}
