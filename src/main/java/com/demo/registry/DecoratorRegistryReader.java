package com.demo.registry;

import com.demo.decorator.Decorator;
import java.util.Optional;

public interface DecoratorRegistryReader {

  Optional<Decorator> getDecorator(Class<?> clazz);
}
