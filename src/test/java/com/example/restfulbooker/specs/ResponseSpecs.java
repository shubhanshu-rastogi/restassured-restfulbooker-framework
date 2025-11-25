package com.example.restfulbooker.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

/**
 * Common response specifications.
 */
public class ResponseSpecs {

    private static final long MAX_RESPONSE_TIME_MS = 5000L;

    private static final ResponseSpecification SUCCESS_2XX = new ResponseSpecBuilder()
            .log(LogDetail.STATUS)
            .log(LogDetail.BODY)
            .expectResponseTime(lessThan(MAX_RESPONSE_TIME_MS))
            .build();

    private ResponseSpecs() {}

    public static ResponseSpecification success2xx() {
        return SUCCESS_2XX;
    }
}
