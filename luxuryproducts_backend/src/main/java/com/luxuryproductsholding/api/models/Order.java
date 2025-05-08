package com.luxuryproductsholding.api.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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


    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    public Order(String shippingAddress, BigDecimal totalPrice) {
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public CustomUser getUser() {
        return user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

}
