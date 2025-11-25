package com.example.restfulbooker.tests;

import com.example.restfulbooker.client.HealthClient;
import com.example.restfulbooker.specs.ResponseSpecs;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/**
 * Simple health check tests for /ping endpoint.
 */
public class HealthCheckTests extends BaseTest {

    private final HealthClient healthClient = new HealthClient();

    @Test
    public void ping_shouldReturn201Or200() {
        Response response = healthClient.ping();

        response.then()
                .spec(ResponseSpecs.success2xx())
                .statusCode(org.hamcrest.Matchers.anyOf(equalTo(201), equalTo(200)));
    }
}
