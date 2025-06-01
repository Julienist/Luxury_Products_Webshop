package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.models.DiscountType;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.services.DiscountCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedDiscountStrategy implements DiscountCalculator {

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {
        return promocode.getDiscountType() == DiscountType.FIXED;
    }

    @Override
    public BigDecimal calculateDiscount(Promocode promocode, Order order) {
        BigDecimal discount = promocode.getDiscountValue();
        return discount.min(order.getTotalPrice());
    }

}
