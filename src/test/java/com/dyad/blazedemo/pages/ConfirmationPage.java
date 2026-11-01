package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmationPage extends BasePage {

    private static final By CONFIRMATION_HEADER = By.cssSelector("h1, h2");
    private static final By CONFIRMATION_TABLE_ROWS = By.cssSelector("table.table tbody tr");

    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isConfirmationDisplayed() {
        WaitUtils.waitForUrlContains(driver, "confirmation");
        WebElement header = waitForVisible(CONFIRMATION_HEADER);
        return header.getText().toLowerCase().contains("thank you");
    }

    public Map<String, String> captureBookingDetails() {
        waitForVisible(CONFIRMATION_TABLE_ROWS);
        List<WebElement> rows = driver.findElements(CONFIRMATION_TABLE_ROWS);
        Map<String, String> details = new HashMap<>();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                String label = cells.get(0).getText().trim();
                String value = cells.get(1).getText().trim();
                details.put(label, value);
            }
        }

        return details;
    }

    public String getBookingId(Map<String, String> details) {
        return details.get("Id");
    }

    public BigDecimal getAmount(Map<String, String> details) {
        String amountText = details.get("Amount");
        if (amountText == null) {
            throw new IllegalStateException("Amount not found on confirmation page");
        }
        String numeric = amountText.replace("USD", "").replace("$", "").replace(",", "").trim();
        numeric = numeric.replaceAll("[^0-9.]", "").trim();
        return new BigDecimal(numeric);
    }

    public String getMaskedCardNumber(Map<String, String> details) {
        return details.get("Card Number");
    }
}
