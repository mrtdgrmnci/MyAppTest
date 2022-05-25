package com.mrt.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    public BasePage() {
        //PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "")
    public WebElement SEARCH_BOX;
}
