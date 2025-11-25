package com.example.restfulbooker.client;

import com.example.restfulbooker.config.ConfigManager;
import com.example.restfulbooker.payloads.AuthPayload;
import com.example.restfulbooker.specs.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Client for /auth endpoint to create tokens.
 */
public class AuthClient extends BaseClient {

    private static final String AUTH_PATH = "/auth";

    public Response createToken(String username, String password) {
        AuthPayload payload = new AuthPayload.Builder(username, password).build();
        logRequestDetails("POST", AUTH_PATH);

        Response response = given()
                .spec(RequestSpecs.unauthenticated())
                .body(payload)
                .when()
                .post(AUTH_PATH);

        logResponseDetails(response);
        return response;
    }

    public Response createDefaultAdminToken() {
        return createToken(ConfigManager.getUsername(), ConfigManager.getPassword());
    }
}
