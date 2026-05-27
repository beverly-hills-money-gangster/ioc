package com.demo.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ProfileUtil {

  // TODO test it
  public static Set<String> getActiveProfiles() {
    return Optional.ofNullable(System.getenv("ACTIVE_PROFILES"))
        .filter(StringUtils::isNotBlank)
        .map(env -> Arrays.stream(env.split(",")).map(StringUtils::trim).toList())
        .map(HashSet::new)
        .orElse(new HashSet<>());
  }

}
