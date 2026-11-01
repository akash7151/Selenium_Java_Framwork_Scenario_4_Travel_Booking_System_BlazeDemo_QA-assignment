package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends BasePage {

    private static final By DEPARTURE_DROPDOWN = By.name("fromPort");
    private static final By DESTINATION_DROPDOWN = By.name("toPort");
    private static final By FIND_FLIGHTS_BUTTON = By.cssSelector("input[type='submit']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(ConfigReader.get("base.url"));
        waitForVisible(DEPARTURE_DROPDOWN);
        return this;
    }

    public HomePage selectDepartureCity(String city) {
        new Select(waitForVisible(DEPARTURE_DROPDOWN)).selectByVisibleText(city);
        return this;
    }

    public HomePage selectDestinationCity(String city) {
        new Select(waitForVisible(DESTINATION_DROPDOWN)).selectByVisibleText(city);
        return this;
    }

    public ReservePage clickFindFlights() {
        waitForClickable(FIND_FLIGHTS_BUTTON).click();
        return new ReservePage(driver);
    }
}
