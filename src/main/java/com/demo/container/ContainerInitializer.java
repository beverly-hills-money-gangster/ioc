package com.demo.container;

import com.demo.decorator.Decorator;
import com.demo.factory.InstanceFactory;
import com.demo.registry.ProfileRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContainerInitializer {

  private final ProfileRegistry profileRegistry = new ProfileRegistry();

  private final Map<Class<?>, Decorator> decorators = new ConcurrentHashMap<>();

  public ContainerInitializer(final String... profiles) {
    if (profiles != null) {
      for (String profile : profiles) {
        profileRegistry.activateProfile(profile);
      }
    }
  }

  public void addDecorator(final Class<?> clazz, Decorator decorator) {
    decorators.put(clazz, decorator);
  }

  public Container init(final Class<?> mainClass) {
    return init(mainClass.getPackageName());
  }

  public Container init(final String packageName) {
    var instanceFactory = new InstanceFactory(decorators);
    Container container = new Container();
    var componentClassScanner = new ComponentClassScanner(profileRegistry);

    for (var componentClass : componentClassScanner.getAllComponentsClasses(packageName)) {
      try {
        container.register(instanceFactory.createObject(componentClass, container));
      } catch (IllegalStateException e) {
        throw e;
      } catch (Exception e) {
        throw new IllegalStateException("Can't init container", e);
      }
    }
    return container;
  }

}
