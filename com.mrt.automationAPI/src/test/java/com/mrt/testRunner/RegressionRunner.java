package com.mrt.testRunner;

import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

/**
 * 
 * @author Murat.Degirmenci
 *
 */


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/mrt/steps")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value="com.mrt.steps")
@IncludeTags("RegressionTest")
@ExcludeTags("ignore")
public class RegressionRunner {

}
