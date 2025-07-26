package com.mrt.testRunner;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Murat.Degirmenci
 *
 */


public class FailedRunner {

	public static void main(String[] args) {
		
		LauncherDiscoveryRequest request = request().selectors(selectDirectory("src/test/resources/com/mrt/steps/Features/Smoke.feature")).build();
		
		Launcher launcher = LauncherFactory.create();
		SummaryGeneratingListener listener = new SummaryGeneratingListener();
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
		
		TestExecutionSummary summary =listener.getSummary();
		System.out.println("failed count "+summary.getTestsFailedCount());
		System.out.println("test found count "+summary.getTestsFoundCount());
		System.out.println("rerun starting");
		
		List<UniqueIdSelector> failures= summary.getFailures().stream().map(Failure::getTestIdentifier).filter(TestIdentifier::isTest).
				map(TestIdentifier::getUniqueId).map(DiscoverySelectors::selectUniqueId).collect(Collectors.toList());
		
		
		LauncherDiscoveryRequest rerunRequest =request().selectors(failures).build();
		launcher.execute(rerunRequest);
		TestExecutionSummary rerunSummary =listener.getSummary();
		System.out.println("rerun failed count :"+rerunSummary.getTestsFailedCount());
		
		//MVN = mvn -Dsurefire.rerunFailingTestCount=2 test
		
		
	}
}
