package com.demo.validation;

import com.demo.util.ReflectionUtil;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    var constructor = ReflectionUtil.getMainConstructor(clazz);
    for (var parameterType : constructor.getParameterTypes()) {
      if (parameterType.isPrimitive()) {
        throw new IllegalStateException("%s constructor can't have primitive params".formatted(
            parameterType.getCanonicalName()));
      } else if (Collection.class.isAssignableFrom(parameterType) && !List.class.isAssignableFrom(
          parameterType)) {
        throw new IllegalStateException(
            "%s constructor can't have any type of collection parameter other than List".formatted(
                parameterType.getCanonicalName()));
      } else if (parameterType.isEnum()) {
        throw new IllegalStateException("%s constructor can't have enum params".formatted(
            parameterType.getCanonicalName()));
      } else if (Map.class.isAssignableFrom(parameterType)) {
        throw new IllegalStateException("%s constructor can't have Map params".formatted(
            parameterType.getCanonicalName()));
      }
    }
  }
}
