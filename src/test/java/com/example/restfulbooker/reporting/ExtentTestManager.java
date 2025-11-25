package com.example.restfulbooker.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Manages ExtentTest instances per thread/test.
 */
public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> TEST_HOLDER = new ThreadLocal<>();

    public static ExtentTest startTest(String testName) {
        ExtentReports extent = ExtentManager.getReporter();
        ExtentTest test = extent.createTest(testName);
        TEST_HOLDER.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return TEST_HOLDER.get();
    }

    public static void endTest() {
        TEST_HOLDER.remove();
    }
}
