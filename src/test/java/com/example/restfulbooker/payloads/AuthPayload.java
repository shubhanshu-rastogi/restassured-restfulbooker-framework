package com.example.restfulbooker.payloads;

public class AuthPayload {
    private String username;
    private String password;

    private AuthPayload(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String username;
        private String password;

        public Builder(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public AuthPayload build() {
            return new AuthPayload(this);
        }
    }
}
