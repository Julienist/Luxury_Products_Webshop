package com.luxuryproductsholding.api.config;

import com.luxuryproductsholding.api.services.DiscountCalculator;
import com.luxuryproductsholding.api.strategy.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StrategyConfig {


    @Bean
    @Qualifier("validationStrategies")
    public List<DiscountStrategy> validationStrategies(
            OrderMinimumStrategy orderMinimumStrategy,
            MaxUsesPerEmailStrategy maxPerEmail,
            ProductScopeStrategy productScope
    ) {
        return List.of(orderMinimumStrategy, maxPerEmail, productScope);
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
