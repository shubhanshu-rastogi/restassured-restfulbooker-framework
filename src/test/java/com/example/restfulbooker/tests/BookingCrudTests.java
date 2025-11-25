package com.example.restfulbooker.tests;

import com.example.restfulbooker.client.BookingClient;
import com.example.restfulbooker.payloads.BookingDates;
import com.example.restfulbooker.payloads.BookingPayload;
import com.example.restfulbooker.specs.ResponseSpecs;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class BookingCrudTests extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    private BookingPayload buildDefaultBookingPayload() {
        BookingDates dates = new BookingDates.Builder("2025-01-01", "2025-01-05").build();
        return new BookingPayload.Builder("Shubhanshu", "Rastogi", 150, true, dates)
                .withAdditionalNeeds("Breakfast")
                .build();
    }

    @Test
    public void createBooking_shouldReturnBookingIdAndBookingObject() {
        BookingPayload payload = buildDefaultBookingPayload();

        Response response = bookingClient.createBooking(payload);

        response.then()
                .spec(ResponseSpecs.success2xx())
                .statusCode(200)
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(payload.getFirstname()))
                .body("booking.lastname", equalTo(payload.getLastname()))
                .body("booking.totalprice", equalTo(payload.getTotalprice()))
                .body("booking.depositpaid", equalTo(payload.isDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(payload.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(payload.getBookingdates().getCheckout()))
                .body("booking.additionalneeds", equalTo(payload.getAdditionalneeds()));
    }

    @Test
    public void getBookingIds_shouldReturnListOfIds() {
        Response response = bookingClient.getBookingIds();

        response.then()
                .spec(ResponseSpecs.success2xx())
                .statusCode(200)
                .body("bookingid", notNullValue());
    }

    @Test
    public void updateBooking_shouldRequireAuthAndReturnUpdatedBooking() {
        // Arrange: create booking first
        BookingPayload original = buildDefaultBookingPayload();
        Response createResponse = bookingClient.createBooking(original);
        int bookingId = createResponse.then().extract().path("bookingid");

        // Act: update the booking with a new lastname
        BookingPayload updatedPayload = new BookingPayload.Builder(
                original.getFirstname(),
                "UpdatedLastName",
                original.getTotalprice(),
                original.isDepositpaid(),
                original.getBookingdates()
        ).withAdditionalNeeds(original.getAdditionalneeds()).build();

        String token = getOrCreateToken();
        Response updateResponse = bookingClient.updateBooking(bookingId, updatedPayload, token);

        // Assert
        updateResponse.then()
                .spec(ResponseSpecs.success2xx())
                .statusCode(anyOf(equalTo(200), equalTo(201)))
                .body("lastname", equalTo("UpdatedLastName"));
    }

    @Test
    public void deleteBooking_shouldRemoveBooking() {
        // Arrange: create booking
        BookingPayload payload = buildDefaultBookingPayload();
        Response createResponse = bookingClient.createBooking(payload);
        int bookingId = createResponse.then().extract().path("bookingid");

        String token = getOrCreateToken();

        // Act: delete booking
        Response deleteResponse = bookingClient.deleteBooking(bookingId, token);

        deleteResponse.then()
                .statusCode(anyOf(equalTo(201), equalTo(200), equalTo(204)));

        // Assert: subsequent GET should return 404
        Response getResponse = bookingClient.getBookingById(bookingId);
        getResponse.then()
                .statusCode(404);
    }
}
