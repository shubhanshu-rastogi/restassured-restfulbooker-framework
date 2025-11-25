package com.example.restfulbooker.client;

import io.restassured.response.Response;

public abstract class BaseClient {

    protected void logRequestDetails(String action, String endpoint) {
        System.out.println("[API] " + action + " -> " + endpoint);
    }

    protected void logResponseDetails(Response response) {
        System.out.println("[API] Status: " + response.statusCode());
    }
}
