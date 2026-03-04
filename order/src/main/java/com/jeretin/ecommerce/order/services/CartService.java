package com.jeretin.ecommerce.order.services;

import com.jeretin.ecommerce.order.dtos.CartItemRequest;
import com.jeretin.ecommerce.order.models.CartItem;

import java.util.List;

public interface CartService {
    boolean addToCart(String userId, CartItemRequest cartItemRequest);

    boolean deleteItemFromCart(String userId, String productId);

    List<CartItem> getUserCart(String userId);

    void clearCart(String userId);
}
