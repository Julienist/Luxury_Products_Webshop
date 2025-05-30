package com.luxuryproductsholding.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PromocodeRequest {

    private String code;
    private String scopeType; // 'CATEGORY' or 'PRODUCT'
    private String scopeValue; // Can be a number or string depending on the scope type
    private String discountType; // 'PERCENTAGE' or 'FIXED'
    private BigDecimal discountValue; // Amount or percentage value
    private BigDecimal minOrderAmount; // Minimum order amount to apply the promocode
    private Integer maxUsesPerUser; // Optional, maximum uses per user/email
    private String expiryDate; // Expiry date in string format (e.g., "2023-12-31")

//    code: string;
//    scopeType: 'CATEGORY' | 'PRODUCT';
//    scopeValue: number | string;
//    discountType: 'PERCENTAGE' | 'FIXED';
//    discountValue: number;
//    minOrderAmount: number;
//    maxUsesPerUser?: number;
//    expiryDate: string | Date;
}
