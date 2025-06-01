package com.luxuryproductsholding.api;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.luxuryproductsholding.api.testclasses")
@IncludeTags("validation")
public class ValidationSuite {
}
