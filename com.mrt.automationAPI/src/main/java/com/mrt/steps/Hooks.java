package com.mrt.steps;

import io.cucumber.java.*;
import static io.restassured.RestAssured.*;

import com.mrt.utils.ConfigReader;
/**
 * 
 * @author Murat.Degirmenci
 *
 */
public class Hooks {

@Before
public void setup() {

	System.out.println(baseURI = ConfigReader.getProperty("URL")+"baseee");
}

@After
public void tearDown() {
	reset();
}


}
