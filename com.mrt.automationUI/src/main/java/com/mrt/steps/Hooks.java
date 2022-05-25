package com.mrt.steps;

import com.mrt.utils.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @author Murat.Degirmenci
 */


public class Hooks {

    boolean isFailed;

//    @Before
//    public void setup(){
//        Driver.getDriver().manage().window().maximize();
//        RecordingUtils.startRecording();
//    }
//
//    @After(order = 1)
//    public void logs(){
//        isFailed=scenario.isFailed();
//        Driver.analyzelog(scenario);
//    }
//
//    @After
//    public void tearDown(Scenario scenario){
//        if (isFailed){
//            TakesScreenshot takesScreenshot =(TakesScreenshot) Driver.getDriver();
//            byte[] image =takesScreenshot.getScreenshotAs(OutputType.BYTES);
//            scenario.attach(image,"image/png",scenario.getName());
//            RecordingUtils.stopRecording();
//        }else {
//            RecordingUtils.stopRecordingAndDelete();
//        }
//        Driver.closeDriver();
//        System.out.println("Test clean up");
//        CreateReport.writeFileInfo(scenario);
//    }
}
