package com.demo.validation;

import java.lang.reflect.Modifier;

public class ComponentValidator implements Validator<Class<?>> {

  @Override
  public void validate(Class<?> clazz) {
    if (clazz.isInterface()) {
      throw new IllegalStateException(
          "Interface %s can't be a component".formatted(clazz.getCanonicalName()));
    } else if (clazz.isEnum()) {
      throw new IllegalStateException(
          "Enum %s can't be a component".formatted(clazz.getCanonicalName()));
    } else if (Modifier.isAbstract(clazz.getModifiers())) {
      throw new IllegalStateException(
          "Abstract class %s can't be a component".formatted(clazz.getCanonicalName()));
    } else if (Modifier.isStatic(clazz.getModifiers())) {
      throw new IllegalStateException(
          "Static class %s can't be a component".formatted(clazz.getCanonicalName()));
    } else if (Modifier.isPrivate(clazz.getModifiers())) {
      throw new IllegalStateException(
          "Private class %s can't be a component".formatted(clazz.getCanonicalName()));
    }
  }
}
