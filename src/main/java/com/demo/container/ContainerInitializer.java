package com.demo.container;

import com.demo.decorator.Decorator;
import com.demo.factory.InstanceFactory;
import com.demo.registry.ProfileRegistry;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Initializes a component container
 */
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

  /**
   * Add a component decorator. Pretty useful for mocking/stubbing components for testing.
   */
  public void addDecorator(final Class<?> clazz, Decorator decorator) {
    if (clazz == null) {
      throw new IllegalArgumentException("Can't decorate null");
    } else if (decorator == null) {
      throw new IllegalArgumentException("null can't be a decorator");
    }
    if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
      throw new IllegalStateException(
          "Can't decorate class %s. Only concrete implementation can be decorated.".formatted(
              clazz.getCanonicalName()));
    }
    Optional.ofNullable(decorators.putIfAbsent(clazz, decorator)).ifPresent(existingDecorator -> {
      throw new IllegalStateException(
          "Can't decorate class %s. Only one decorator per class is allowed. Check decorator %s"
              .formatted(clazz.getCanonicalName(),
                  existingDecorator.getClass().getCanonicalName()));
    });
  }

}
