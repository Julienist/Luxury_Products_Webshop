package com.luxuryproductsholding.api.DAO;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.strategy.DiscountStrategy;
import org.springframework.stereotype.Component;

@Component
public class OrderMinimumStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {
        return order.getTotalAmount().compareTo(promocode.getMinimumOrderAmount()) >= 0;
    }
}

