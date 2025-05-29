package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.strategy.DiscountStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromocodeValidatorService {

    private final List<DiscountStrategy> discountStrategies;

    @Autowired
    public PromocodeValidatorService(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    public boolean validate(Promocode promocode, Order order, String email) {
        return discountStrategies.stream()
                .allMatch(strategy -> strategy.isApplicable(promocode, order, email));
    }

    public BigDecimal applyDiscount(Promocode promocode, Order order, String email) {
        if (!validate(promocode, order, email)) {
            throw new IllegalArgumentException("Promocode is niet geldig voor deze bestelling.");
        }

        // Eén strategie is verantwoordelijk voor de korting zelf
        return discountStrategies.stream()
                .filter(strategy -> strategy instanceof DiscountCalculator)
                .map(strategy -> ((DiscountCalculator) strategy).calculateDiscount(promocode, order))
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
}

