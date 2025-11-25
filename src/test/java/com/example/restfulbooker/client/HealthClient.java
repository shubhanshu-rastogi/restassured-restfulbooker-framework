package com.example.restfulbooker.client;

import com.example.restfulbooker.specs.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Client for /ping health check.
 */
public class HealthClient extends BaseClient {

    private static final String PING_PATH = "/ping";

    public Response ping() {
        logRequestDetails("GET", PING_PATH);
        Response response = given()
                .spec(RequestSpecs.unauthenticated())
                .when()
                .get(PING_PATH);
        logResponseDetails(response);
        return response;
    }
}
