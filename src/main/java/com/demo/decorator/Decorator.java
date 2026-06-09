package com.demo.decorator;

@FunctionalInterface
public interface Decorator {

  Object decorate(Object object);

}
