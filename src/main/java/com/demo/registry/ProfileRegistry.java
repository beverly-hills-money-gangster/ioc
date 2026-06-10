package com.demo.registry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Components profile registry
 */
public class ProfileRegistry implements ProfileRegistryReader {


  private final Set<String> activeProfiles = ConcurrentHashMap.newKeySet();

  public ProfileRegistry() {
    // by default, profiles are taken from env var
    var envProfiles = Optional.ofNullable(System.getenv("ACTIVE_PROFILES"))
        .filter(StringUtils::isNotBlank)
        .map(env -> Arrays.stream(env.split(",")).map(StringUtils::trim).toList())
        .map(HashSet::new)
        .orElse(new HashSet<>());
    activeProfiles.addAll(envProfiles);
  }

  @Override
  public Set<String> getActiveProfiles() {
    return new HashSet<>(activeProfiles);
  }

  public void activateProfile(final String profile) {
    if (StringUtils.isBlank(profile)) {
      throw new IllegalArgumentException("Can't activate empty profile");
    }
    activeProfiles.add(profile);
  }

}
