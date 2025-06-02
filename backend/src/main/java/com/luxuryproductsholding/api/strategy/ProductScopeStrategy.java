package com.luxuryproductsholding.api.strategy;

import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.OrderItem;
import com.luxuryproductsholding.api.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductScopeStrategy implements DiscountStrategy {

    @Override
    public boolean isApplicable(Promocode promocode, Order order, String email) {

        var allowedProducts   = promocode.getApplicableProducts();
        var allowedCategories = promocode.getApplicableCategories();
        if ((allowedProducts == null || allowedProducts.isEmpty())
                && (allowedCategories == null || allowedCategories.isEmpty())) {
            return true;
        }

        for (OrderItem item : order.getOrderItems()) {
            Product p = item.getProduct();
            if (allowedProducts != null && allowedProducts.contains(p)) {
                return true;
            }
            if (allowedCategories != null && allowedCategories.stream()
                    .anyMatch(cat -> cat.getId().equals(p.getCategory().getId()))) {
                return true;
            }
        }
        return false;
    }
}
