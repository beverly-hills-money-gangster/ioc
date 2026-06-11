package com.demo.container;

import com.demo.decorator.Decorator;
import com.demo.factory.InstanceFactory;
import com.demo.registry.DecoratorRegistry;
import com.demo.registry.ProfileRegistry;

/**
 * Initializes a component container
 */
public class ContainerInitializer {

  private final ProfileRegistry profileRegistry = new ProfileRegistry();

  private final DecoratorRegistry decorators = new DecoratorRegistry();

  public ContainerInitializer(final String... profiles) {
    if (profiles != null) {
      for (String profile : profiles) {
        profileRegistry.activateProfile(profile);
      }
    }
  }

  /**
   * Initializes a container
   *
   * @param packageName package for components scan
   * @return a fully initialized container
   */
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

  public Container init(final Class<?> mainClass) {
    return init(mainClass.getPackageName());
  }

  public void addDecorator(Class<?> clazz, Decorator decorator) {
    decorators.addDecorator(clazz, decorator);
  }


}
