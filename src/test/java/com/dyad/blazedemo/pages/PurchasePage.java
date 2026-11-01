package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.config.ConfigReader;
import com.dyad.blazedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class PurchasePage extends BasePage {

    private static final By NAME_INPUT = By.name("inputName");
    private static final By ADDRESS_INPUT = By.name("address");
    private static final By CITY_INPUT = By.name("city");
    private static final By STATE_INPUT = By.name("state");
    private static final By ZIP_INPUT = By.name("zipCode");
    private static final By CARD_TYPE_DROPDOWN = By.name("cardType");
    private static final By CARD_NUMBER_INPUT = By.name("creditCardNumber");
    private static final By CARD_MONTH_INPUT = By.name("creditCardMonth");
    private static final By CARD_YEAR_INPUT = By.name("creditCardYear");
    private static final By NAME_ON_CARD_INPUT = By.name("nameOnCard");
    private static final By PURCHASE_BUTTON = By.cssSelector("input[type='submit'][value='Purchase Flight']");

    public PurchasePage(WebDriver driver) {
        super(driver);
        WaitUtils.waitForUrlContains(driver, "purchase");
        waitForVisible(NAME_INPUT);
    }

    public PurchasePage fillPassengerDetails() {
        waitForVisible(NAME_INPUT).sendKeys(ConfigReader.get("passenger.name"));
        waitForVisible(ADDRESS_INPUT).sendKeys(ConfigReader.get("passenger.address"));
        waitForVisible(CITY_INPUT).sendKeys(ConfigReader.get("passenger.city"));
        waitForVisible(STATE_INPUT).sendKeys(ConfigReader.get("passenger.state"));
        waitForVisible(ZIP_INPUT).sendKeys(ConfigReader.get("passenger.zip"));
        new Select(waitForVisible(CARD_TYPE_DROPDOWN)).selectByValue(ConfigReader.get("passenger.card.type"));
        waitForVisible(CARD_NUMBER_INPUT).sendKeys(ConfigReader.get("passenger.card.number"));
        waitForVisible(CARD_MONTH_INPUT).clear();
        waitForVisible(CARD_MONTH_INPUT).sendKeys(ConfigReader.get("passenger.card.month"));
        waitForVisible(CARD_YEAR_INPUT).clear();
        waitForVisible(CARD_YEAR_INPUT).sendKeys(ConfigReader.get("passenger.card.year"));
        waitForVisible(NAME_ON_CARD_INPUT).sendKeys(ConfigReader.get("passenger.name.on.card"));
        return this;
    }

    public ConfirmationPage clickPurchaseFlight() {
        waitForClickable(PURCHASE_BUTTON).click();
        return new ConfirmationPage(driver);
    }
}
