package com.luxuryproductsholding.api.services;

import com.luxuryproductsholding.api.DAO.OrderItemRepository;
import com.luxuryproductsholding.api.DAO.OrderRepository;
import com.luxuryproductsholding.api.DAO.ProductRepository;
import com.luxuryproductsholding.api.DAO.UserRepository;
import com.luxuryproductsholding.api.DTO.OrderItemDTO;
import com.luxuryproductsholding.api.DTO.OrderRequest;
import com.luxuryproductsholding.api.models.CustomUser;
import com.luxuryproductsholding.api.models.Order;
import com.luxuryproductsholding.api.models.OrderItem;
import com.luxuryproductsholding.api.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity<Order> createOrder(OrderRequest orderRequest) {
        Optional<CustomUser> user = userRepository.findById(orderRequest.getUserId());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Order order = new Order();
        order.setUser(user.get());
        order.setShippingAddress(orderRequest.getShippingAddress());

        orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : orderRequest.getOrderItems()) {
            Optional<Product> productOpt = productRepository.findById(itemDTO.getProductId());

            if (productOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }

            Product product = productOpt.get();
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductName(itemDTO.getProductName());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(itemDTO.getPrice()));
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalPrice = totalPrice.add(BigDecimal.valueOf(itemDTO.getPrice())
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        orderItemRepository.saveAll(orderItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


    public ResponseEntity<List<Order>> getOrdersByUserId(long id) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String authenticatedEmail = ((UserDetails) principal).getUsername();
        CustomUser authenticatedUser = userRepository.findByEmail(authenticatedEmail);

        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (authenticatedUser.getId() != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<Order> orders = orderRepository.findByUserId(id);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(orders);
        }
    }





}
