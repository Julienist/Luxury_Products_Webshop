package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequest {

    private Long userId;
    private String shippingAddress;
    private List<OrderItemDTO> orderItems;
    private BigDecimal discountValue;

}
