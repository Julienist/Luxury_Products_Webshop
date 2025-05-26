package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationDTO {

    private String email;
    private String password;

}
