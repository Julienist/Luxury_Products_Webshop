package com.luxuryproductsholding.api.DTO;

public class AuthenticationDTO {
    public String email;
    public String password;

    public AuthenticationDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
