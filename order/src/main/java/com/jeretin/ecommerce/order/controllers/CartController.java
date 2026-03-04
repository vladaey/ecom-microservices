package com.jeretin.ecommerce.order.controllers;

import com.jeretin.ecommerce.order.dtos.CartItemRequest;
import com.jeretin.ecommerce.order.models.CartItem;
import com.jeretin.ecommerce.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addItemToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest
            ) {
        boolean status = cartService.addToCart(userId, cartItemRequest);
        return status ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId) {
        boolean status = cartService.deleteItemFromCart(userId, productId);
        return status ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId
    ) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }
}
