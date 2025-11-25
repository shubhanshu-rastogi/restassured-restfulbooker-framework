package com.example.restfulbooker.specs;

import com.example.restfulbooker.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

/**
 * Central place for building reusable RequestSpecifications.
 */
public class RequestSpecs {

    private static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(ConfigManager.getBaseUri())
            .setContentType("application/json")
            .log(LogDetail.URI)
            .log(LogDetail.METHOD)
            .build();

    private RequestSpecs() {}

    public static RequestSpecification unauthenticated() {
        return with().spec(BASE_SPEC);
    }

    /**
     * For endpoints that require cookie-based auth ("token=abc123").
     */
    public static RequestSpecification withToken(String token) {
        return with()
                .spec(BASE_SPEC)
                .cookie("token", token);
    }
}
