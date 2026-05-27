package com.demo.container;

import com.demo.factory.InstanceFactory;

public class ContainerInitializer {

  public static ContainerReader init(final Class<?> mainClass) {
    return init(mainClass.getPackageName());
  }

  public static ContainerReader init(final String packageName) {
    Container container = new Container();
    var allComponentsClasses = ComponentClassScanner.getAllComponentsClasses(packageName);
    for (Class<?> componentClazz : allComponentsClasses) {
      try {
        container.register(InstanceFactory.createObject(componentClazz, container));
      } catch (Exception e) {
        throw new IllegalStateException("Can't init container", e);
      }
    }
    return container;
  }

}
