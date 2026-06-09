package com.demo.container.samples.positive.dependency.profile;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;

@Component
@Profile(profiles = {"test"})
public class TestDependency extends AbstractDependency {

}
