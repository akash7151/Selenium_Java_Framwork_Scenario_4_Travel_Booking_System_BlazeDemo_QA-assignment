package com.dyad.blazedemo.utils;

import com.dyad.blazedemo.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtils {

    private WaitUtils() {
    }

    public static WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return createWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return createWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static List<WebElement> waitForAllVisible(WebDriver driver, By locator) {
        return createWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String partialUrl) {
        return createWait(driver).until(ExpectedConditions.urlContains(partialUrl));
    }
}
