package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class GatewayConfig {

    //@Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", predicateSpec -> predicateSpec
                        .path("/api/products/**")
                        .uri("lb://PRODUCT-SERVICE"))
                .route("user-service", predicateSpec -> predicateSpec
                        .path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("order-service", predicateSpec -> predicateSpec
                        .path("/api/orders/**", "/api/cart/**")
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }
}
