package com.mrt.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import static io.restassured.RestAssured.*;

public class Hooks {
    @Before
    public void setup() {
        // Set up RestAssured base URI or any other setup logic
        baseURI = "http://localhost"; // Change as needed
        System.out.println("Setup complete: " + baseURI);
    }

    @After
    public void tearDown() {
        // Reset RestAssured or any other cleanup logic
        reset();
        System.out.println("Teardown complete");
    }
} 