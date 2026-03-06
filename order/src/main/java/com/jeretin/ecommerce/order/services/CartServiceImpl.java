package com.jeretin.ecommerce.order.services;

import com.jeretin.ecommerce.order.clients.ProductServiceClient;
import com.jeretin.ecommerce.order.clients.UserServiceClient;
import com.jeretin.ecommerce.order.dtos.CartItemRequest;
import com.jeretin.ecommerce.order.dtos.ProductResponse;
import com.jeretin.ecommerce.order.dtos.UserDTO;
import com.jeretin.ecommerce.order.models.CartItem;
import com.jeretin.ecommerce.order.repositories.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;

    private final ProductServiceClient productServiceClient;

    private final UserServiceClient userServiceClient;


    int attempt = 0;

    @Override
//    @CircuitBreaker(name="productService", fallbackMethod = "addToCartFallBack")
    @Retry(name="retryBreaker", fallbackMethod = "addToCartRetryFallBack")
    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        // 1.Fetch Product according to productId
        // 2. Validate if product exists and if stock quantity less than required one.
        // 3. Fetch user using userId input parameter
        // 4. Validate if user exists, and then check if user's cart already contains the item
        // 5. if item exists, update quantity and price, otherwise create new cart item.

        System.out.println("ATTEMPT COUNT: " + ++attempt);

        // Look for product
        ProductResponse productResponse = productServiceClient.getProductInfo(cartItemRequest.getProductId());
        if (productResponse == null || productResponse.getStockQuantity() < cartItemRequest.getQuantity())
            return false;

        UserDTO userResponse = userServiceClient.getUserInfo(userId);
        if (userResponse == null)
            return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setPrice(productResponse.getPrice());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setUserId(userId);
            cartItemRepository.save(cartItem);
        }
        return true;

    }

    public boolean addToCartFallBack(String userId, CartItemRequest cartItemRequest, Exception exception) {
        exception.printStackTrace();
        return false;
    }

    public boolean addToCartRetryFallBack(String userId, CartItemRequest cartItemRequest, Exception exception) {
        log.error("Add to Cart Retry fallback", exception);
        exception.printStackTrace();
        return false;
    }

    @Override
    public boolean deleteItemFromCart(String userId, String productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
        return true;
    }

    @Override
    public List<CartItem> getUserCart(String userId) {
        List<CartItem> cartItemList = cartItemRepository.findByUserId(userId);
        if (cartItemList.isEmpty()) {
            return new ArrayList<CartItem>();
        }
        return cartItemList;
    }

    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
