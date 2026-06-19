package com.demo.container;

import com.demo.decorator.Decorator;
import com.demo.factory.InstanceFactory;
import com.demo.registry.DecoratorRegistry;
import com.demo.registry.ProfileRegistry;
import java.util.Arrays;
import javax.lang.model.SourceVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes a component container
 */
public class ContainerInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(ContainerInitializer.class);

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
   * @param packages packages for components scan
   * @return a fully initialized container
   */
  public Container init(final String... packages) {
    if (packages == null || packages.length == 0) {
      throw new IllegalArgumentException("At least one package should be specified");
    }
    for (String pack : packages) {
      if (StringUtils.isBlank(pack) || !SourceVersion.isName(pack)) {
        throw new IllegalArgumentException("Invalid package name %s".formatted(pack));
      }
    }
    var instanceFactory = new InstanceFactory(decorators);
    var container = new Container();
    var componentClassScanner = new ComponentClassScanner(profileRegistry);
    int componentsRegistered = 0;
    for (var componentClass : componentClassScanner.getAllComponentsClasses(
        Arrays.stream(packages).toList())) {
      try {
        container.register(instanceFactory.createObject(componentClass, container));
        componentsRegistered++;
      } catch (IllegalStateException e) {
        throw e;
      } catch (Exception e) {
        throw new IllegalStateException("Can't init container", e);
      }
    }
    LOG.info("Container initialized. {} component(s) created", componentsRegistered);
    return container;
  }

  public Container init(final Class<?>... sources) {
    if (sources == null || sources.length == 0) {
      throw new IllegalArgumentException("At least one source should be specified");
    }
    return init(Arrays.stream(sources).map(Class::getPackageName).toArray(String[]::new));
  }

  public void addDecorator(Class<?> clazz, Decorator decorator) {
    decorators.addDecorator(clazz, decorator);
  }


}
