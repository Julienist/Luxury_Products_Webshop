package com.luxuryproductsholding.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Promocode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT

    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;

    private Integer maxUsesTotal;
    private Integer maxUsesPerEmail;

    @ManyToMany
    private Set<Product> applicableProducts;

    @ManyToMany
    private Set<Category> applicableCategories;

    // Logging komt in een aparte table
}

