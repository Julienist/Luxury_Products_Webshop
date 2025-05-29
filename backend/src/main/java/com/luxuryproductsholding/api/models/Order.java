package com.luxuryproductsholding.api.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    private String shippingAddress;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    @ManyToOne(optional = true) // Dit is belangrijk
    @JoinColumn(name = "promocode_id", nullable = true)
    private Promocode promocode;


    public Order() {
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
    }

    public Order(CustomUser user, String shippingAddress, BigDecimal totalPrice, LocalDateTime orderDate) {
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.promocode = null; // Standaard geen promotiecode
    }

    public Order(String shippingAddress, BigDecimal totalPrice) {
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
    }

}
