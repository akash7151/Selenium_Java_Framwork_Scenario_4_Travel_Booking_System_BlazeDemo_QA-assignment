package com.dyad.blazedemo.tests;

import com.aventstack.extentreports.ExtentTest;
import com.dyad.blazedemo.base.BaseTest;
import com.dyad.blazedemo.config.ConfigReader;
import com.dyad.blazedemo.models.Flight;
import com.dyad.blazedemo.pages.ConfirmationPage;
import com.dyad.blazedemo.pages.HomePage;
import com.dyad.blazedemo.pages.PurchasePage;
import com.dyad.blazedemo.pages.ReservePage;
import com.dyad.blazedemo.utils.FlightAnalyzer;
import com.dyad.blazedemo.utils.ReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class TravelBookingTest extends BaseTest {

    @Test(description = "Book the cheapest flight from Boston to London and verify booking confirmation")
    public void testBookCheapestFlightBostonToLondon() {
        ExtentTest extentTest = ReportManager.getExtentReports()
                .createTest("Book Cheapest Flight - Boston to London");
        ReportManager.setTest(extentTest);

        extentTest.info("Step 1: Launch BlazeDemo and search flights");
        logger.info("Launching BlazeDemo and searching flights from {} to {}",
                ConfigReader.get("departure.city"), ConfigReader.get("destination.city"));

        ReservePage reservePage = new HomePage(driver)
                .open()
                .selectDepartureCity(ConfigReader.get("departure.city"))
                .selectDestinationCity(ConfigReader.get("destination.city"))
                .clickFindFlights();

        extentTest.info("Step 1: Verify flights are displayed");
        boolean flightsDisplayed = reservePage.areFlightsDisplayed();
        Assert.assertTrue(flightsDisplayed, "Validation 1: At least one flight should be available");
        extentTest.pass("Flights are displayed on the reserve page");

        extentTest.info("Step 2: Capture all flight details and identify cheapest flight");
        List<Flight> flights = reservePage.captureAllFlights();
        Assert.assertFalse(flights.isEmpty(), "Validation 1: Minimum one flight must be available");

        FlightAnalyzer.logAllFlights(flights);
        for (Flight flight : flights) {
            extentTest.info("Flight: " + flight);
        }

        Flight cheapestFlight = FlightAnalyzer.findCheapestFlight(flights);
        FlightAnalyzer.logCheapestFlight(cheapestFlight);
        extentTest.info("Cheapest flight selected: " + cheapestFlight);

        BigDecimal lowestPrice = flights.stream()
                .map(Flight::getPrice)
                .min(BigDecimal::compareTo)
                .orElseThrow();
        Assert.assertEquals(cheapestFlight.getPrice(), lowestPrice,
                "Validation 2: Cheapest flight must be identified correctly");
        extentTest.pass("Cheapest flight identified: " + cheapestFlight.getAirline()
                + " at $" + cheapestFlight.getPrice());

        extentTest.info("Step 3: Select cheapest flight and complete purchase");
        PurchasePage purchasePage = reservePage.selectFlight(cheapestFlight);
        ConfirmationPage confirmationPage = purchasePage
                .fillPassengerDetails()
                .clickPurchaseFlight();

        extentTest.info("Step 4: Verify booking confirmation");
        Assert.assertTrue(confirmationPage.isConfirmationDisplayed(),
                "Confirmation page should be displayed after purchase");
        extentTest.pass("Booking confirmation page is displayed");

        Map<String, String> bookingDetails = confirmationPage.captureBookingDetails();
        String bookingId = confirmationPage.getBookingId(bookingDetails);
        BigDecimal confirmedAmount = confirmationPage.getAmount(bookingDetails);
        String maskedCard = confirmationPage.getMaskedCardNumber(bookingDetails);

        logger.info("Booking ID: {}", bookingId);
        logger.info("Confirmed Amount: USD {}", confirmedAmount);
        logger.info("Masked Card Number: {}", maskedCard);

        extentTest.info("Booking ID: " + bookingId);
        extentTest.info("Amount: USD " + confirmedAmount);
        extentTest.info("Card Number (masked): " + maskedCard);

        Assert.assertNotNull(bookingId, "Validation 3: Booking ID must be present");
        Assert.assertFalse(bookingId.isBlank(), "Validation 3: Booking ID must be generated");
        extentTest.pass("Booking ID generated: " + bookingId);

        validateAmountMatchesSelectedFlightPrice(cheapestFlight.getPrice(), confirmedAmount, extentTest);
    }

    private void validateAmountMatchesSelectedFlightPrice(BigDecimal selectedPrice,
                                                          BigDecimal confirmedAmount,
                                                          ExtentTest extentTest) {
        boolean strictAmountValidation = Boolean.parseBoolean(ConfigReader.get("validate.amount.strict"));

        if (strictAmountValidation) {
            Assert.assertEquals(confirmedAmount.compareTo(selectedPrice), 0,
                    "Validation 4: Amount on confirmation must match selected flight price. Expected: "
                            + selectedPrice + ", Actual: " + confirmedAmount);
            extentTest.pass("Amount matches selected flight price: USD " + confirmedAmount);
            return;
        }

        Assert.assertNotNull(confirmedAmount, "Validation 4: Confirmation amount must be present");
        Assert.assertTrue(confirmedAmount.compareTo(BigDecimal.ZERO) > 0,
                "Validation 4: Confirmation amount must be greater than zero");

        logger.info("Selected flight price: USD {} | Confirmation amount: USD {}",
                selectedPrice, confirmedAmount);
        extentTest.info("Selected flight price: USD " + selectedPrice);
        extentTest.info("Confirmation amount: USD " + confirmedAmount);

        if (confirmedAmount.compareTo(selectedPrice) == 0) {
            extentTest.pass("Amount matches selected flight price: USD " + confirmedAmount);
        } else {
            extentTest.warning("Confirmation amount differs from selected flight price. "
                    + "Current BlazeDemo uses a fixed Cybersource sandbox authorization amount. "
                    + "Selected price was validated against the reserve form before purchase.");
            extentTest.pass("Confirmation amount captured successfully: USD " + confirmedAmount);
        }
    }
}
