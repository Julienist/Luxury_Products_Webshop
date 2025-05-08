package com.luxuryproductsholding.api.DTO;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private String shippingAddress;
    private List<OrderItemDTO> orderItems;

    public Long getUserId() {
        return userId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
