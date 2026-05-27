package com.demo.container;

import java.util.List;

public interface ContainerReader {

  <T> T getInstance(Class<T> clazz);

  <T> List<T> getInstances(Class<T> clazz);
}
