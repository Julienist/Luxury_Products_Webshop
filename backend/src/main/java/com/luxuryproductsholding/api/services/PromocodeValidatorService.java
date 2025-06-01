package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.CategoryRepository;
import com.luxuryproductsholding.api.DAO.ProductRepository;
import com.luxuryproductsholding.api.DAO.PromocodeRepository;
import com.luxuryproductsholding.api.DTO.PromocodeRequest;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.Promocode;
import com.luxuryproductsholding.api.strategy.DiscountStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromocodeValidatorService {

//    private final List<DiscountStrategy> strategies;
    private final List<DiscountStrategy> validationStrategies;
    private final List<DiscountCalculator> discountCalculators;
    private final PromocodeRepository promocodeRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

//    @Autowired
//    public PromocodeValidatorService(List<DiscountStrategy> strategies,
//                                     PromocodeRepository promocodeRepository,
//                                     ProductRepository productRepository,
//                                     CategoryRepository categoryRepository) {
//        this.strategies = strategies;
//        this.promocodeRepository = promocodeRepository;
//        this.productRepository = productRepository;
//        this.categoryRepository = categoryRepository;
//    }

    @Autowired
    public PromocodeValidatorService(
            PromocodeRepository promocodeRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            @Qualifier("validationStrategies") List<DiscountStrategy> validationStrategies,
            @Qualifier("discountCalculators") List<DiscountCalculator> discountCalculators
    ) {
        this.validationStrategies = validationStrategies;
        this.discountCalculators = discountCalculators;
        this.promocodeRepository = promocodeRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

//    public boolean validate(Promocode promocode, Order order, String email) {
//        for (DiscountStrategy strategy : strategies) {
//            if (!strategy.isApplicable(promocode, order, email)) {
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean validate(Promocode promocode, Order order, String email) {
        for (DiscountStrategy strategy : validationStrategies) {
            if (!strategy.isApplicable(promocode, order, email)) {
                return false;
            }
            boolean result = strategy.isApplicable(promocode, order, email);
            System.out.println("Strategy: " + strategy.getClass().getSimpleName() + " → " + result);
            if (!result) return false;

        }
        return true;
    }


//    public BigDecimal applyDiscount(Promocode promocode, Order order, String email) {
//        for (DiscountStrategy strategy : strategies) {
//            if (strategy instanceof DiscountCalculator calc
//                    && strategy.isApplicable(promocode, order, email)) {
//                return calc.calculateDiscount(promocode, order);
//            }
//        }
//        return BigDecimal.ZERO;
//    }

    public BigDecimal applyDiscount(Promocode promocode, Order order, String email) {
        for (DiscountCalculator calculator : discountCalculators) {
            if (calculator.isApplicable(promocode, order, email)) {
                return calculator.calculateDiscount(promocode, order);
            }
        }
        return BigDecimal.ZERO;
    }

    public void validatePromocodeOnCreation(@NotNull PromocodeRequest promocode) {
        try {
            validatePromocodeCode(promocode.getCode());
            validatePromocodeScopeType(promocode);
            validatePromocodeScopeValue(promocode);
            validatePromocodeDiscountType(promocode);
            validatePromocodeDiscountValue(promocode);
            validatePromocodeMinOrderAmount(promocode);
            validatePromocodeMaxUses(promocode);
            validatePromocodeExpiryDate(promocode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Fout bij het aanmaken van de promocode: " + e.getMessage());
        }
    }

    public void validatePromocodeCode(@NotNull String code) {
        if (code.isEmpty()) {
            throw new IllegalArgumentException("Promocode mag niet leeg zijn.");
        } if (promocodeRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Promocode bestaat al.");
        } else if (code.length() < 3) {
            throw new IllegalArgumentException("Promocode moet tenminste 3 tekens lang zijn.");
        } else if (!code.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Promocode mag alleen alfanumerieke tekens bevatten.");
        }
    }

    public void validatePromocodeScopeType(@NotNull PromocodeRequest promocode) {
        if (promocode.getScopeType() == null) {
            throw new IllegalArgumentException("Scope type mag niet leeg zijn.");
        }
        if (!List.of("PRODUCT", "CATEGORY").contains(promocode.getScopeType())) {
            throw new IllegalArgumentException("Ongeldig scope type. Kies uit 'PRODUCT' of 'CATEGORY'.");
        }
    }

    // onnodige logica in onderstaande functie, omdat scopeValue altijd een geldige waarde moet zijn.
    public void validatePromocodeScopeValue(@NotNull PromocodeRequest promocode) {
        if (promocode.getScopeValue() == null || promocode.getScopeValue().isEmpty()) {
            throw new IllegalArgumentException("Scope waarde mag niet leeg zijn.");
        }
        // Controleer of de scope waarde overeenkomt met het scope type.
        // en of het een geldige entiteit is, zoals een Product of Category.
        // Ervanuitgaande dat juiste product/categorie data altijd naar frontend wordt gestuurd.
        // is onderstaande check niet nodig, maar voor de zekerheid:
        boolean exists;
        if ("PRODUCT".equalsIgnoreCase(promocode.getScopeType())) {
            exists = productRepository.existsByName(promocode.getScopeValue());
        } else if ("CATEGORY".equalsIgnoreCase(promocode.getScopeType())) {
            exists = categoryRepository.existsByName(promocode.getScopeValue());
        } else {
            throw new IllegalArgumentException("Ongeldig scopeType: " + promocode.getScopeType());
        }

        if (!exists) {
            throw new IllegalArgumentException("De opgegeven " + promocode.getScopeType().toLowerCase() + " '" + promocode.getScopeValue() + "' bestaat niet.");
        }
    }

    public void validatePromocodeDiscountType(@NotNull PromocodeRequest promocode) {
        if (promocode.getDiscountType() == null) {
            throw new IllegalArgumentException("Kortingstype mag niet leeg zijn.");
        }
        if (!List.of("PERCENTAGE", "FIXED").contains(promocode.getDiscountType())) {
            throw new IllegalArgumentException("Ongeldig kortingstype. Kies uit 'PERCENTAGE' of 'FIXED'.");
        }
    }

    public void validatePromocodeDiscountValue(@NotNull PromocodeRequest promocode) {
        if (promocode.getDiscountValue() == null || promocode.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Korting moet groter zijn dan 0.");
        }
    }

    public void validatePromocodeMinOrderAmount(@NotNull PromocodeRequest promocode) {
        if (promocode.getMinOrderAmount() == null || promocode.getMinOrderAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimale bestelwaarde moet groter zijn dan of gelijk aan 0.");
        }
    }

    public void validatePromocodeMaxUses(@NotNull PromocodeRequest promocode) {
        if (promocode.getUsedCount() != null && promocode.getMaxUsesPerEmail() <= 0) {
            throw new IllegalArgumentException("Maximaal aantal keren dat de promocode per gebruiker gebruikt kan worden moet groter zijn dan 0.");
        }
    }

    public void validatePromocodeExpiryDate(@NotNull PromocodeRequest promocode) {
        if (promocode.getExpiryDate() == null) {
            throw new IllegalArgumentException("Vervaldatum mag niet leeg zijn.");
        }
        if (promocode.getCreationDate() == null) {
            throw new IllegalArgumentException("Aanmaakdatum mag niet leeg zijn.");
        }
        if (promocode.getExpiryDate().isBefore(promocode.getCreationDate())) {
            throw new IllegalArgumentException("Vervaldatum kan niet voor de aanmaakdatum liggen.");
        }
        if (promocode.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Promocode is verlopen.");
        }
    }

}

