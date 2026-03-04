package com.jeretin.ecommerce.order.services;

import com.jeretin.ecommerce.order.dtos.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface OrderService {
    Optional<OrderResponse> placeOrder(String userId);
}
