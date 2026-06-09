package com.demo.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtil {

  public static Constructor<?> getMainConstructor(final Class<?> clazz) {
    if (clazz == null) {
      throw new IllegalStateException("Can't get constructor from null");
    } else if (clazz.getConstructors().length == 0) {
      throw new IllegalStateException(
          "No constructor available for class %s".formatted(clazz.getCanonicalName()));
    } else if (clazz.getConstructors().length != 1) {
      throw new IllegalStateException(
          "Only one constructor is expected for class %s".formatted(clazz.getCanonicalName()));
    }
    return clazz.getConstructors()[0];
  }

  public static Class<?> getArrayType(final Class<?> arrayClass) {
    if (arrayClass == null) {
      throw new IllegalStateException("Array class can't be null");
    } else if (!arrayClass.isArray()) {
      throw new IllegalStateException(
          "Class %s is not array".formatted(arrayClass.getCanonicalName()));
    }
    return arrayClass.getComponentType();
  }

  public static Class<?> getCollectionTypeFromConstructor(
      final Constructor<?> constructor,
      final int argumentIndex) {
    if (constructor == null) {
      throw new IllegalArgumentException("Constructor can't be null");
    } else if (argumentIndex < 0) {
      throw new IllegalArgumentException("Constructor argument index can't be negative");
    } else if (argumentIndex >= constructor.getGenericParameterTypes().length) {
      throw new IllegalArgumentException("Constructor argument index is out of bounds");
    }
    var pt = (ParameterizedType) constructor.getGenericParameterTypes()[argumentIndex];
    return (Class<?>) pt.getActualTypeArguments()[0];
  }

  public static Set<Class<?>> getClassesForParent(
      final Class<?> parentClass,
      final Set<Class<?>> allClasses) {
    if (parentClass == null) {
      throw new IllegalArgumentException("Parent class can't be null");
    } else if (allClasses == null) {
      throw new IllegalArgumentException("Can't find classes due to a null pointer");
    }
    return allClasses.stream().filter(parentClass::isAssignableFrom)
        .collect(Collectors.toSet());
  }

}
