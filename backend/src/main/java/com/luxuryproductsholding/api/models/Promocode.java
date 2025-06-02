package com.luxuryproductsholding.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime creationDate;
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;

    private Integer usedCount;
    private Integer maxUsesPerEmail;

    @ManyToMany
    private Set<Product> applicableProducts;

    @ManyToMany
    private Set<Category> applicableCategories;

    public Promocode(String code, boolean active, LocalDate creationDate, LocalDate expiryDate, DiscountType discountType, BigDecimal discountValue, BigDecimal minimumOrderAmount, Integer usedCount, Integer maxUsesPerEmail, Set<Product> applicableProducts, Set<Category> applicableCategories) {
        this.code = code;
        this.active = active;
        this.creationDate = creationDate.atStartOfDay();
        this.expiryDate = expiryDate.atStartOfDay();
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.usedCount = usedCount;
        this.maxUsesPerEmail = maxUsesPerEmail;
        this.applicableProducts = applicableProducts;
        this.applicableCategories = applicableCategories;
    }
}

