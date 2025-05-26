package com.luxuryproductsholding.api.DTO;

import com.luxuryproductsholding.api.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class LoginResponse {

//    private String userId;
    private String email;
    private Set<Role> roles;
    private String token;

}
