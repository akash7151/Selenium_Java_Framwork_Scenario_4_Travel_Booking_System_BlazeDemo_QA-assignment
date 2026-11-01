package com.dyad.blazedemo.utils;

import com.dyad.blazedemo.models.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public final class FlightAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightAnalyzer.class);

    private FlightAnalyzer() {
    }

    public static Flight findCheapestFlight(List<Flight> flights) {
        return flights.stream()
                .min(Comparator.comparing(Flight::getPrice))
                .orElseThrow(() -> new IllegalStateException("No flights available to analyze"));
    }

    public static void logAllFlights(List<Flight> flights) {
        LOGGER.info("=== Available Flights ({}) ===", flights.size());
        for (int i = 0; i < flights.size(); i++) {
            LOGGER.info("Flight {}: {}", i + 1, flights.get(i));
        }
    }

    public static void logCheapestFlight(Flight cheapest) {
        LOGGER.info("Cheapest flight identified: {}", cheapest);
    }

    public static BigDecimal parsePrice(String priceText) {
        String numeric = priceText.replace("$", "").replace(",", "").trim();
        return new BigDecimal(numeric);
    }
}
