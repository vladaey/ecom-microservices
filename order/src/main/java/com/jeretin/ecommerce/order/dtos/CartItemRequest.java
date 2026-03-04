package com.jeretin.ecommerce.order.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequest {
    private String productId;
    private Integer quantity;
}
