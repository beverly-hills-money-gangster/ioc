package com.demo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReflectionUtilTest {


  @Test
  public void testGetArrayType() {
    Map<Class<?>, Object> arrayClasses
        = Map.of(
        Integer.class, new Integer[1],
        String.class, new String[1],
        List.class, new List[1],
        Boolean.class, new Boolean[1],
        Object.class, new Object[1]);
    arrayClasses.forEach(
        (expectedClass, object) ->
            assertEquals(expectedClass, ReflectionUtil.getArrayType(object.getClass())));

  }

  @Test
  public void testGetCollectionTypeFromConstructor() {
    Map<Class<?>, Class<?>> collectionClasses
        = Map.of(
        Integer.class, ClassIntArrayList.class,
        String.class, ClassStringHashSet.class,
        Boolean.class, ClassBooleanDeque.class);

    collectionClasses.forEach((expectedClass, classWithCollection) -> {
      var constructor = classWithCollection.getConstructors()[0];
      Assertions.assertEquals(expectedClass,
          ReflectionUtil.getCollectionTypeFromConstructor(constructor, 0));
    });
  }

  @Test
  public void testGetClassesForParent() {
    var clazz = List.class;
    Set<Class<?>> classes = Set.of(Object.class, String.class, Integer.class, ArrayList.class,
        LinkedList.class, HashSet.class, Map.class);

    assertEquals(Set.of(ArrayList.class, LinkedList.class),
        ReflectionUtil.getClassesForParent(clazz, classes));
  }

  private static class ClassIntArrayList {

    public ClassIntArrayList(ArrayList<Integer> collection) {

    }
  }

  private static class ClassStringHashSet {

    public ClassStringHashSet(HashSet<String> collection) {

    }
  }

  private static class ClassBooleanDeque {

    public ClassBooleanDeque(ArrayDeque<Boolean> collection) {

    }
  }

}
