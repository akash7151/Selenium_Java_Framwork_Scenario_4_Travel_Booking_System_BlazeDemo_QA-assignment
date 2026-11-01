package com.dyad.blazedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public final class ReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> EXTENT_TEST = new ThreadLocal<>();

    private ReportManager() {
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent-reports/BlazeDemo-Report.html");
            sparkReporter.config().setDocumentTitle("BlazeDemo Travel Booking Report");
            sparkReporter.config().setReportName("Scenario 4 - Travel Booking Automation");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "BlazeDemo Travel Booking");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
        }
        return extentReports;
    }

    public static void setTest(ExtentTest test) {
        EXTENT_TEST.set(test);
    }

    public static ExtentTest getTest() {
        return EXTENT_TEST.get();
    }

    public static void removeTest() {
        EXTENT_TEST.remove();
    }
}
