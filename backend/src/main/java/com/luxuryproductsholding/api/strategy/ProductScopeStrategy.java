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
        // Haal de sets op uit de promocode
        var allowedProducts   = promocode.getApplicableProducts();   // Set<Product>
        var allowedCategories = promocode.getApplicableCategories(); // Set<Category>

        // Als beide sets leeg/ null zijn: promocode geldt voor alle producten
        if ((allowedProducts == null || allowedProducts.isEmpty())
                && (allowedCategories == null || allowedCategories.isEmpty())) {
            return true;
        }

        // Anders: loop door alle orderItems en kijk of minstens één voldoet
        for (OrderItem item : order.getOrderItems()) {
            Product p = item.getProduct();
            if (allowedProducts != null && allowedProducts.contains(p)) {
                return true; // product expliciet toegestaan
            }
            if (allowedCategories != null && allowedCategories.stream()
                    .anyMatch(cat -> cat.getId().equals(p.getCategory().getId()))) {
                return true; // categorie toegestaan
            }
        }
        // Geen enkel item viel binnen scope
        return false;
    }
}
