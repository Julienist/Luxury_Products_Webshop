package com.luxuryproductsholding.api;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.test.context.ActiveProfiles;

@Suite
@SelectPackages("com.luxuryproductsholding.api.testclasses")
@ActiveProfiles("test")
@IncludeTags("validation")
public class ValidationSuite {
}
