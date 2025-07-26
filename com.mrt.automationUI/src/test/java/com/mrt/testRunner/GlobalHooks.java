package com.mrt.testRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.messages.types.TestRunStarted;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

/**
 * 
 * @author Murat.Degirmenci
 *
 */


public class GlobalHooks implements ConcurrentEventListener {
	
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		eventPublisher.registerHandlerFor(TestRunStarted.class, event->{
			System.out.println("***** Before All API Tests *****");
		});
		eventPublisher.registerHandlerFor(TestRunStarted.class, event->{
			System.out.println("***** After All API Tests *****");
			File reportOutputDirectory=new File("target/cucumberReport");
			List<String> jsonFiles=new ArrayList<>();
			jsonFiles.add("target/cucumber-reports/Cucumber.json");
			String projectName="Mrt API Automation Framework";
			Configuration configuration =new Configuration(reportOutputDirectory,projectName);
			ReportBuilder reportBuilder =new ReportBuilder(jsonFiles,configuration);
			reportBuilder.generateReports();
			
		});
	
	}

}



