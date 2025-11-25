package com.example.restfulbooker.tests;

import com.example.restfulbooker.client.AuthClient;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.with;

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
}
