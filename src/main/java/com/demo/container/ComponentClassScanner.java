package com.demo.container;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;
import com.demo.dependency.DependencyGraph;
import com.demo.registry.ProfileRegistryReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.SourceVersion;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

public class ComponentClassScanner {

  private final ProfileRegistryReader profileRegistry;

  public ComponentClassScanner(final ProfileRegistryReader profileRegistry) {
    this.profileRegistry = profileRegistry;
  }

  public Queue<Class<?>> getAllComponentsClasses(final String packageName) {
    if (StringUtils.isBlank(packageName)) {
      throw new IllegalArgumentException("Package can't be empty/null/blank");
    } else if (!SourceVersion.isName(packageName)) {
      throw new IllegalArgumentException("Invalid package name " + packageName);
    }
    return new DependencyGraph(getClassesToScan(packageName)).traverse();
  }

  private Set<Class<?>> getClassesToScan(final String packageName) {
    var reflections = new Reflections(packageName);
    var activeProfiles = profileRegistry.getActiveProfiles();
    var classes = new HashSet<>(reflections.getTypesAnnotatedWith(Component.class));
    return classes.stream().filter(clazz -> {
      if (!clazz.isAnnotationPresent(Profile.class)) {
        return true;
      }
      var profiles = clazz.getAnnotation(Profile.class).profiles();
      if (profiles == null || profiles.length == 0) {
        throw new IllegalStateException(
            "Class %s is profiled but no profile is specified".formatted(clazz.getCanonicalName()));
      }
      return Arrays.stream(profiles).anyMatch(activeProfiles::contains);
    }).collect(Collectors.toSet());
  }

}
