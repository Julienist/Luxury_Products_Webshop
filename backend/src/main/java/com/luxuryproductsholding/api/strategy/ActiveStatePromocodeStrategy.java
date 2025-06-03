package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import org.springframework.stereotype.Component;

@Component
public class ActiveStatePromocodeStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {
    try {
            return promocode.isActive();
        } catch (Exception e) {
            return false;
        }
    }

}
