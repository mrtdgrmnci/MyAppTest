package com.mrt.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Driver {

    public synchronized static WebDriver getDriver(){

        WebDriver webDriver=new ChromeDriver();
        return webDriver ;
    }
}
