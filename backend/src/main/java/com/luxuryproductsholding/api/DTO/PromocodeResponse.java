package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class PromocodeResponse {

    private BigDecimal discountValue; // The discount value applied by the promocode
    private boolean isValid; // Indicates if the promocode is valid for the current order
    private String message; // Message indicating the result of the promocode application

}
