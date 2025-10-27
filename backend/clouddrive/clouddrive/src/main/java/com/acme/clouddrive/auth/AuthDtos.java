package com.acme.clouddrive.auth;

public class AuthDtos {

    public static class RegisterRequest {
        public String email;
        public String password;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class TokenResponse {
        public String token;
        public TokenResponse(String token) { this.token = token; }
    }
}
