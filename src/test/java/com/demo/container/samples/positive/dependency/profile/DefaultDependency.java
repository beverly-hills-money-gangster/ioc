package com.demo.container.samples.positive.dependency.profile;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;

@Component
@Profile(profiles = {"default"})
public class DefaultDependency extends AbstractDependency {

}
