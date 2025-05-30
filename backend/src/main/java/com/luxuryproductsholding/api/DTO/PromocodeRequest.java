package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class PromocodeRequest {

    private String code;
//    private boolean active; // Indicates if the promocode is currently active
    private String scopeType; // 'CATEGORY' or 'PRODUCT'
    private String scopeValue; // The scope type is a category String or a Product String
    private String discountType; // 'PERCENTAGE' or 'FIXED'
    private BigDecimal discountValue; // Amount or percentage value
    private BigDecimal minOrderAmount; // Minimum order amount to apply the promocode
    private Integer usedCount; // Optional, logs the total number of uses
    private Integer maxUsesPerEmail; // Optional, maximum uses per user/email
    private LocalDateTime expiryDate; // Expiry date in string format (e.g., "2023-12-31")
    private LocalDateTime creationDate; // Creation date of the promocode
}
