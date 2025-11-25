package com.example.restfulbooker.tests;

import com.example.restfulbooker.client.AuthClient;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.example.restfulbooker.reporting.ExtentManager;
import com.example.restfulbooker.reporting.ExtentTestManager;

/**
 * Base test class that:
 *  - enables logging
 *  - can lazily create and cache an auth token for tests that need it
 */
public abstract class BaseTest {

    protected static String token;

    @BeforeClass
    public static void setupRestAssured() {
        // Global logging filters
        RestAssured.filters(
                new RequestLoggingFilter(LogDetail.URI),
                new RequestLoggingFilter(LogDetail.BODY),
                new ResponseLoggingFilter(LogDetail.STATUS),
                new ResponseLoggingFilter(LogDetail.BODY)
        );
    }

    protected static String getOrCreateToken() {
        if (token == null) {
            AuthClient authClient = new AuthClient();
            token = authClient.createDefaultAdminToken()
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
        }
        return token;
    }

    @Rule
    public TestWatcher extentWatcher = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            String testName = description.getClassName() + "." + description.getMethodName();
            ExtentTest test = ExtentTestManager.startTest(testName);
            test.log(Status.INFO, "Starting test: " + testName);
        }

        @Override
        protected void succeeded(Description description) {
            ExtentTest test = ExtentTestManager.getTest();
            if (test != null) {
                test.log(Status.PASS, "Test passed");
            }
            ExtentTestManager.endTest();
        }

        @Override
        protected void failed(Throwable e, Description description) {
            ExtentTest test = ExtentTestManager.getTest();
            if (test != null) {
                test.log(Status.FAIL, e);
            }
            ExtentTestManager.endTest();
        }

        @Override
        protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
            ExtentTest test = ExtentTestManager.getTest();
            if (test != null) {
                test.log(Status.SKIP, "Test skipped: " + e.getMessage());
            }
            ExtentTestManager.endTest();
        }
    };

    @AfterClass
    public static void tearDownReport() {
        ExtentManager.flush();
    }

}