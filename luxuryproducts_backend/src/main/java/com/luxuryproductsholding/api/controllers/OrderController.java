package com.luxuryproductsholding.api.controllers;

import com.luxuryproductsholding.api.DTO.OrderRequest;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable long id) {
        return orderService.getOrdersByUserId(id);
    }

}
