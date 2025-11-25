package com.example.restfulbooker.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Creates and manages a singleton ExtentReports instance.
 */
public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            // HTML report will be generated here
            String reportPath = "target/extent-report/RestAssured-ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("RestAssured API Test Report");
            spark.config().setReportName("Restful-Booker API Suite");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Project", "Restful-Booker");
            extent.setSystemInfo("Framework", "Rest Assured + JUnit 4");
        }
        return extent;
    }

    public synchronized static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
