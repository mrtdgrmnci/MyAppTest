package com.mrt.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
import com.mrt.utils.Driver;

public class TestStepDefinition {
    WebDriver driver = Driver.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Given("I want to write a step with precondition")
    public void i_want_to_write_a_step_with_precondition() {
        driver.get("https://www.youtube.com");
        String title = driver.getTitle();
        System.out.println("Navigated to YouTube. Title: " + title);
        assertTrue(title.contains("YouTube"));
    }

    @Given("some other precondition")
    public void some_other_precondition() {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search_query")));
        assertNotNull(searchBox);
        System.out.println("Search box is present.");
    }

    @When("I complete action")
    public void i_complete_action() {
        WebElement logo = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#logo-icon")));
        logo.click();
        System.out.println("Clicked on YouTube logo.");
    }

    @When("some other action")
    public void some_other_action() {
        WebElement trending = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Trending' or contains(@href, '/feed/trending')]")));
        assertNotNull(trending);
        System.out.println("Trending link is present.");
    }

    @When("yet another action")
    public void yet_another_action() {
        WebElement menuButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("guide-button")));
        assertNotNull(menuButton);
        System.out.println("Menu button is present.");
    }

    @Then("I validate the outcomes")
    public void i_validate_the_outcomes() {
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#logo-icon")));
        assertTrue(logo.isDisplayed());
        System.out.println("YouTube logo is displayed.");
    }

    @Then("check more outcomes")
    public void check_more_outcomes() {
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-icon-legacy")));
        assertTrue(searchButton.isDisplayed());
        System.out.println("Search button is displayed.");
    }

    // Scenario Outline steps
    @Given("I want to write a step with {string}")
    public void i_want_to_write_a_step_with_name(String name) {
        driver.get("https://www.youtube.com");
        System.out.println("Navigated to YouTube for name: " + name);
    }

    @When("I check for the {string} in step")
    public void i_check_for_the_value_in_step(String value) {
        assertTrue(driver.getPageSource().contains(value));
        System.out.println("Checked for value: " + value);
    }

    @Then("I verify the {string} in step")
    public void i_verify_the_status_in_step(String status) {
        System.out.println("Status to verify: " + status);
        assertNotNull(status);
    }
} 