package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;

public interface DiscountStrategy {
    boolean isApplicable(Promocode promocode, Order order, String email);
}
