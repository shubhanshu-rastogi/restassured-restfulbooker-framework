package com.example.restfulbooker.client;

import com.example.restfulbooker.payloads.BookingPayload;
import com.example.restfulbooker.specs.RequestSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Client for /booking endpoints (CRUD operations).
 */
public class BookingClient extends BaseClient {

    private static final String BOOKING_PATH = "/booking";

    public Response getBookingIds() {
        logRequestDetails("GET", BOOKING_PATH);
        Response response = given()
                .spec(RequestSpecs.unauthenticated())
                .when()
                .get(BOOKING_PATH);
        logResponseDetails(response);
        return response;
    }

    public Response getBookingById(int bookingId) {
        String path = BOOKING_PATH + "/" + bookingId;
        logRequestDetails("GET", path);
        Response response = given()
                .spec(RequestSpecs.unauthenticated())
                .when()
                .get(path);
        logResponseDetails(response);
        return response;
    }

    public Response createBooking(BookingPayload payload) {
        logRequestDetails("POST", BOOKING_PATH);
        Response response = given()
                .spec(RequestSpecs.unauthenticated())
                .body(payload)
                .when()
                .post(BOOKING_PATH);
        logResponseDetails(response);
        return response;
    }

    public Response updateBooking(int bookingId, BookingPayload payload, String token) {
        String path = BOOKING_PATH + "/" + bookingId;
        logRequestDetails("PUT", path);
        Response response = given()
                .spec(RequestSpecs.withToken(token))
                .body(payload)
                .when()
                .put(path);
        logResponseDetails(response);
        return response;
    }

    public Response partialUpdateBooking(int bookingId, String jsonPatchBody, String token) {
        String path = BOOKING_PATH + "/" + bookingId;
        logRequestDetails("PATCH", path);
        Response response = given()
                .spec(RequestSpecs.withToken(token))
                .body(jsonPatchBody)
                .when()
                .patch(path);
        logResponseDetails(response);
        return response;
    }

    public Response deleteBooking(int bookingId, String token) {
        String path = BOOKING_PATH + "/" + bookingId;
        logRequestDetails("DELETE", path);
        Response response = given()
                .spec(RequestSpecs.withToken(token))
                .when()
                .delete(path);
        logResponseDetails(response);
        return response;
    }
}
