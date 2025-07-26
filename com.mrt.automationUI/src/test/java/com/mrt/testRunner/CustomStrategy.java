package com.mrt.testRunner;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

/**
 * 
 * @author Murat.Degirmenci
 *
 */


public class CustomStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

	@Override
	public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
	
		return this;
	}

	@Override
	public int getParallelism() {
		
		return 8;
	}

	@Override
	public int getMinimumRunnable() {
	
		return 8;
	}

	@Override
	public int getMaxPoolSize() {
		
		return 8;
	}

	@Override
	public int getCorePoolSize() {
		
		return 8;
	}

	@Override
	public int getKeepAliveSeconds() {
		
		return 8;
	}

}
