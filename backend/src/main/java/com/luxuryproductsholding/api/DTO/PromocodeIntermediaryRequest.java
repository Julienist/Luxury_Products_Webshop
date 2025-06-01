package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PromocodeIntermediaryRequest {

    private String code;
    private String email;
    private List<OrderItemDTO> cartItems;
    private BigDecimal totalPrice;

}
