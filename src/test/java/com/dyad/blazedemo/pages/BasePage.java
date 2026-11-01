package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator);
    }
}
