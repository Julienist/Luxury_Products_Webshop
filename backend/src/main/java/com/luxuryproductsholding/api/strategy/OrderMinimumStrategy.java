package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import org.springframework.stereotype.Component;

@Component
public class OrderMinimumStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {
        return order.getTotalPrice().compareTo(promocode.getMinimumOrderAmount()) >= 0;
    }
}

