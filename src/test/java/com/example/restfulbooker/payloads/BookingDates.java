package com.example.restfulbooker.payloads;

public class BookingDates {
    private String checkin;
    private String checkout;

    public BookingDates() {}

    private BookingDates(Builder builder) {
        this.checkin = builder.checkin;
        this.checkout = builder.checkout;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public static class Builder {
        private String checkin;
        private String checkout;

        public Builder(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
        }

        public BookingDates build() {
            return new BookingDates(this);
        }
    }
}
