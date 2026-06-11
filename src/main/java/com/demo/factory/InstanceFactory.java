package com.demo.factory;

import com.demo.container.ContainerReader;
import com.demo.registry.DecoratorRegistryReader;
import com.demo.util.ReflectionUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Component instance factory
 */
public class InstanceFactory {


  private final DecoratorRegistryReader decorators;

  public InstanceFactory(DecoratorRegistryReader decorators) {
    this.decorators = decorators;
  }

  /**
   * Creates an object instance. If the given class has dependencies then it's taken from the
   * container and injected into the created object.
   */
  public Object createObject(final Class<?> clazz,
      final ContainerReader containerReader)
      throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
    var constructor = ReflectionUtil.getMainConstructor(clazz);
    var arguments = new Object[constructor.getParameterTypes().length];
    for (int i = 0; i < constructor.getParameterTypes().length; i++) {
      var paramType = constructor.getParameterTypes()[i];
      if (paramType.isArray()) {
        var arrayType = ReflectionUtil.getArrayType(paramType);
        arguments[i] = createArray(arrayType, containerReader.getInstancesNonEmpty(arrayType));
      } else if (List.class.isAssignableFrom(paramType)) {
        var collectionItemType = ReflectionUtil.getCollectionTypeFromConstructor(constructor, i);
        var collection = createList(paramType,
            containerReader.getInstancesNonEmpty(collectionItemType));
        arguments[i] = collection;
      } else {
        arguments[i] = createSingularInstance(paramType, containerReader);
      }
    }
    var object = constructor.newInstance(arguments);
    return decorators.getDecorator(object.getClass()).map(
        decorator -> decorator.decorate(object)).orElse(object);
  }


  private static Object createSingularInstance(
      final Class<?> clazz,
      final ContainerReader containerReader) {
    return containerReader.getInstance(clazz);
  }

  private static Object createArray(final Class<?> clazz, final List<?> items) {
    Object array = java.lang.reflect.Array.newInstance(clazz, items.size());
    int index = 0;
    for (Object item : items) {
      java.lang.reflect.Array.set(array, index, item);
      index++;
    }
    return array;
  }

  private static List<Object> createList(
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
