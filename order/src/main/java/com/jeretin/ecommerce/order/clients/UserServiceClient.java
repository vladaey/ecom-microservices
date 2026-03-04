package com.jeretin.ecommerce.order.clients;

import com.jeretin.ecommerce.order.dtos.UserDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {
    @GetExchange("/api/users/{id}")
    UserDTO getUserInfo(@PathVariable String id);
}
