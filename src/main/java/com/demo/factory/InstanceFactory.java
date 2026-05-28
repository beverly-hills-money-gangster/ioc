package com.demo.factory;

import com.demo.container.ContainerReader;
import com.demo.util.ReflectionUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceFactory {

  public static Object createObject(final Class<?> clazz,
      final ContainerReader containerReader)
      throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    var constructor = ReflectionUtil.getMainConstructor(clazz);
    var arguments = new Object[constructor.getParameterTypes().length];
    for (int i = 0; i < constructor.getParameterTypes().length; i++) {
      var paramType = constructor.getParameterTypes()[i];
      if (paramType.isArray()) {
        var arrayType = ReflectionUtil.getArrayType(paramType);
        arguments[i] = createArray(arrayType, containerReader.getInstances(arrayType));
      } else if (List.class.isAssignableFrom(paramType)) {
        var collectionItemType = ReflectionUtil.getCollectionTypeFromConstructor(constructor, i);
        var collection = createList(paramType, containerReader.getInstances(collectionItemType));
        arguments[i] = collection;
      } else {
        arguments[i] = createSingularInstance(paramType, containerReader);
      }
    }
    return constructor.newInstance(arguments);
  }


  static Object createSingularInstance(final Class<?> clazz,
      final ContainerReader containerReader) {
    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
      var components = containerReader.getInstances(clazz);
      if (components.isEmpty()) {
        throw new IllegalStateException(
            "No component of type %s found".formatted(clazz.getCanonicalName()));
      } else if (components.size() > 1) {
        throw new IllegalStateException(
            "Ambiguous dependency %s. Expected 1 candidate but found %s. See classes: %s".formatted(
                clazz.getCanonicalName(), components.size(),
                components.stream().map(object -> object.getClass().getCanonicalName())
                    .collect(Collectors.toSet())));
      }
      return components.getFirst();
    } else {
      return containerReader.getInstance(clazz);
    }
  }

  static Object createArray(final Class<?> clazz, final List<?> items) {
    Object array = java.lang.reflect.Array.newInstance(clazz, items.size());
    int index = 0;
    for (Object item : items) {
      java.lang.reflect.Array.set(array, index, item);
      index++;
    }
    return array;
  }

  static List<Object> createList(
      final Class<?> clazz,
      final List<?> items)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    List<Object> collection;
    if (clazz.isInterface()) {
      collection = new ArrayList<>();
    } else {
      collection = (List) clazz.getDeclaredConstructor().newInstance();
    }
    collection.addAll(items);
    return collection;
  }


}
