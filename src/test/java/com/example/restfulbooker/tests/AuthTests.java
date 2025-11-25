package com.example.restfulbooker.tests;

import com.example.restfulbooker.client.AuthClient;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class AuthTests extends BaseTest {

    private final AuthClient authClient = new AuthClient();

    @Test
    public void createToken_withValidCredentials_shouldReturnToken() {
        Response response = authClient.createDefaultAdminToken();

        response.then()
                .statusCode(200)
                .body("token", allOf(notNullValue(), instanceOf(String.class)));
    }

  @Test
public void createToken_withInvalidCredentials_shouldReturnBadCredentialsReason() {
    Response response = authClient.createToken("invalid", "invalid");

    response.then()
            .statusCode(200)
            .body("reason", equalTo("Bad credentials"));
}
}
