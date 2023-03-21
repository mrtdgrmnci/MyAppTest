package com.mrt.steps;

import io.cucumber.java.en.*;

public class TestStepDefinition {
    @Given("I want to write a step with precondition")
    public void i_want_to_write_a_step_with_precondition() {
        System.out.println("Test step 1. --started");
    }
    @Given("some other precondition")
    public void some_other_precondition() {
        System.out.println("Test step 2..");
    }
    @When("I complete action")
    public void i_complete_action() {
        System.out.println("Test step 3...");
    }
    @When("some other action")
    public void some_other_action() {
        System.out.println("Test step 4....");
    }
    @When("yet another action")
    public void yet_another_action() {
        System.out.println("Test step 5.....");
    }
    @Then("I validate the outcomes")
    public void i_validate_the_outcomes() {
        System.out.println("Test step 6......");
    }
    @Then("check more outcomes")
    public void check_more_outcomes() {
        System.out.println("Test step 7......  --completed successfully!!");
    }
}
