package com.dyad.blazedemo.base;

import com.aventstack.extentreports.ExtentTest;
import com.dyad.blazedemo.utils.DriverManager;
import com.dyad.blazedemo.utils.ReportManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;

    @BeforeSuite
    public void setupReport() {
        ReportManager.getExtentReports();
    }

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterSuite
    public void flushReport() {
        ReportManager.getExtentReports().flush();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        ExtentTest extentTest = ReportManager.getTest();
        if (extentTest != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                extentTest.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                extentTest.pass("Test passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                extentTest.skip("Test skipped");
            }
        }

        DriverManager.quitDriver();
        ReportManager.removeTest();
    }
}
