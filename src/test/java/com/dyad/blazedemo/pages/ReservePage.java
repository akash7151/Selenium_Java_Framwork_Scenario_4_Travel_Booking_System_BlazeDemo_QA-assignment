package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.models.Flight;
import com.dyad.blazedemo.utils.FlightAnalyzer;
import com.dyad.blazedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ReservePage extends BasePage {

    private static final By FLIGHT_ROWS = By.cssSelector("table.table tbody tr");
    private static final By FLIGHTS_TABLE = By.cssSelector("table.table");

    public ReservePage(WebDriver driver) {
        super(driver);
    }

    public boolean areFlightsDisplayed() {
        waitForVisible(FLIGHTS_TABLE);
        List<WebElement> rows = WaitUtils.waitForAllVisible(driver, FLIGHT_ROWS);
        return !rows.isEmpty();
    }

    public List<Flight> captureAllFlights() {
        waitForVisible(FLIGHTS_TABLE);
        List<WebElement> rows = WaitUtils.waitForAllVisible(driver, FLIGHT_ROWS);
        List<Flight> flights = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 6) {
                continue;
            }

            String flightNumber = cells.get(1).getText().trim();
            String airline = cells.get(2).getText().trim();
            String priceText = cells.get(5).getText().trim();
            WebElement chooseButton = cells.get(0).findElement(By.cssSelector("input[type='submit']"));

            flights.add(new Flight(
                    airline,
                    flightNumber,
                    FlightAnalyzer.parsePrice(priceText),
                    chooseButton
            ));
        }

        return flights;
    }

    public PurchasePage selectFlight(Flight flight) {
        waitForVisible(FLIGHTS_TABLE);
        List<WebElement> rows = WaitUtils.waitForAllVisible(driver, FLIGHT_ROWS);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 6) {
                continue;
            }

            String flightNumber = cells.get(1).getText().trim();
            String airline = cells.get(2).getText().trim();
            var tablePrice = FlightAnalyzer.parsePrice(cells.get(5).getText().trim());

            if (airline.equals(flight.getAirline())
                    && flightNumber.equals(flight.getFlightNumber())
                    && tablePrice.compareTo(flight.getPrice()) == 0) {
                WebElement hiddenPriceInput = row.findElement(By.cssSelector("input[name='price']"));
                var formPrice = new java.math.BigDecimal(hiddenPriceInput.getAttribute("value"));
                if (formPrice.compareTo(flight.getPrice()) != 0) {
                    throw new IllegalStateException("Form price " + formPrice
                            + " does not match selected flight price " + flight.getPrice());
                }

                WebElement chooseButton = cells.get(0).findElement(By.cssSelector("input[type='submit']"));
                WaitUtils.waitForClickable(driver, By.cssSelector("input.btn.btn-small[value='Choose This Flight']"));
                chooseButton.click();
                return new PurchasePage(driver);
            }
        }

        throw new IllegalStateException("Could not locate flight row to select: " + flight);
    }
}
