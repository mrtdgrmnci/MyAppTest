package com.mrt.testRunner;

import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;




@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/mrt/steps")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value="com.mrt.steps")
@IncludeTags("SmokeTest")
@ExcludeTags("ignore")
public class TestRunner {

}
