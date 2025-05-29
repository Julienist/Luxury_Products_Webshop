package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.strategy.DiscountStrategy;

import java.math.BigDecimal;

public interface DiscountCalculator extends DiscountStrategy {
    BigDecimal calculateDiscount(Promocode promocode, Order order);
}

