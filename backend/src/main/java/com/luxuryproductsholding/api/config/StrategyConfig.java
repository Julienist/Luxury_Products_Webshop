package com.luxuryproductsholding.api.config;

import com.luxuryproductsholding.api.services.DiscountCalculator;
import com.luxuryproductsholding.api.strategy.DiscountStrategy;
import com.luxuryproductsholding.api.strategy.FixedDiscountStrategy;
import com.luxuryproductsholding.api.strategy.OrderMinimumStrategy;
import com.luxuryproductsholding.api.strategy.PercentageDiscountStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StrategyConfig {


    @Bean
    @Qualifier("validationStrategies")
    public List<DiscountStrategy> validationStrategies(OrderMinimumStrategy orderMinimumStrategy) {
        return List.of(orderMinimumStrategy);
    }

    @Bean
    @Qualifier("discountCalculators")
    public List<DiscountCalculator> discountCalculators(
            PercentageDiscountStrategy percentageStrategy,
            FixedDiscountStrategy fixedStrategy
    ) {
        return List.of(percentageStrategy, fixedStrategy);
    }
}
