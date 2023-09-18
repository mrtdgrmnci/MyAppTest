package com.mrt.testRunner;
import io.cucumber.plugin.ConcurrentEventListener;

import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GlobalHooks implements ConcurrentEventListener {

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		eventPublisher.registerHandlerFor(TestRunStarted.class, this::beforeAllApiTests);
		eventPublisher.registerHandlerFor(TestRunFinished.class, this::afterAllApiTests);
	}

	private void beforeAllApiTests(TestRunStarted event) {
		System.out.println("***** Before All API Tests *****");
	}

	private void afterAllApiTests(TestRunFinished event) {
		System.out.println("***** After All API Tests *****");

		File reportOutputDirectory = new File("target/cucumber-reports");
		if (!reportOutputDirectory.exists()) {
			reportOutputDirectory.mkdirs();
		}

		List<String> jsonFiles = new ArrayList<>();
		jsonFiles.add("target/cucumber-reports/cucumber-report.json");
		String projectName = "Mrt API Automation Framework";

		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		reportBuilder.generateReports();
	}
}






