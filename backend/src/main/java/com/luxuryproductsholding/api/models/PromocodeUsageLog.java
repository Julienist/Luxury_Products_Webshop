package com.luxuryproductsholding.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class PromocodeUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
    private String email;

    private LocalDateTime usedAt;

    private BigDecimal discountApplied;

    @ManyToOne
    private Promocode promocode;

    public PromocodeUsageLog(String email,
                             LocalDateTime usedAt,
                             BigDecimal discountApplied,
                             Promocode promocode) {
        this.email = email;
        this.usedAt = usedAt;
        this.discountApplied = discountApplied;
        this.promocode = promocode;
    }
}

