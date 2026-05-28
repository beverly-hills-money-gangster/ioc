package com.demo.container;

import com.demo.factory.InstanceFactory;
import com.demo.registry.ProfileRegistry;

public class ContainerInitializer {

  private final ProfileRegistry profileRegistry = new ProfileRegistry();

  public ContainerInitializer(final String... profiles) {
    if (profiles != null) {
      for (String profile : profiles) {
        profileRegistry.activateProfile(profile);
      }
    }
  }

  public Container init(final Class<?> mainClass) {
    return init(mainClass.getPackageName());
  }

  public Container init(final String packageName) {
    var componentClassScanner = new ComponentClassScanner(profileRegistry);
    Container container = new Container();
    for (var componentClass : componentClassScanner.getAllComponentsClasses(packageName)) {
      try {
        container.register(InstanceFactory.createObject(componentClass, container));
      } catch (Exception e) {
        throw new IllegalStateException("Can't init container", e);
      }
    }
    return container;
  }

}
