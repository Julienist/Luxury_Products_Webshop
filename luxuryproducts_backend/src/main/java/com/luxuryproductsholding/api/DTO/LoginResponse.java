package com.luxuryproductsholding.api.DTO;

public class LoginResponse {
    public String userId;
    public String email;
    public String token;

    public LoginResponse(Long userId, String email, String token) {
        this.userId = String.valueOf(userId);
        this.email = email;
        this.token = token;
    }
}
