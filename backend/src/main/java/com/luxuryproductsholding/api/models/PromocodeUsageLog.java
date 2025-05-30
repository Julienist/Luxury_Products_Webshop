package com.luxuryproductsholding.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@Entity
public class PromocodeUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CustomUser user;

    private LocalDateTime usedAt;

    private BigDecimal discountApplied;

    @ManyToOne
    private Promocode promocode;
}

