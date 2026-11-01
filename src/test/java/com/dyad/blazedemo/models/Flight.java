package com.dyad.blazedemo.models;

import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.Objects;

public class Flight {

    private final String airline;
    private final String flightNumber;
    private final BigDecimal price;
    private final WebElement chooseButton;

    public Flight(String airline, String flightNumber, BigDecimal price, WebElement chooseButton) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.price = price;
        this.chooseButton = chooseButton;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public WebElement getChooseButton() {
        return chooseButton;
    }

    @Override
    public String toString() {
        return String.format("Airline=%s, FlightNumber=%s, Price=$%s", airline, flightNumber, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Flight flight = (Flight) o;
        return Objects.equals(airline, flight.airline)
                && Objects.equals(flightNumber, flight.flightNumber)
                && Objects.equals(price, flight.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airline, flightNumber, price);
    }
}
