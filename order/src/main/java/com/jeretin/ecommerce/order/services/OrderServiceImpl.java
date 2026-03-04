package com.jeretin.ecommerce.order.services;

import com.jeretin.ecommerce.order.dtos.OrderItemDTO;
import com.jeretin.ecommerce.order.dtos.OrderResponse;
import com.jeretin.ecommerce.order.models.CartItem;
import com.jeretin.ecommerce.order.models.Order;
import com.jeretin.ecommerce.order.models.OrderItem;
import com.jeretin.ecommerce.order.models.OrderStatus;
import com.jeretin.ecommerce.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderResponse> placeOrder(String userId) {
        // 1. Validacija item-a u cart
        List<CartItem> cartItems = cartService.getUserCart(userId);

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        // 2. Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 4. Clear the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}
