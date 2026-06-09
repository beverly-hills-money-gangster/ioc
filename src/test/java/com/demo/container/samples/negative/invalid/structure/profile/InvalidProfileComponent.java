package com.demo.container.samples.negative.invalid.structure.profile;

import com.demo.annotation.Component;
import com.demo.annotation.Profile;

// missing profile
@Profile(profiles = {})
@Component
public class InvalidProfileComponent {

}
