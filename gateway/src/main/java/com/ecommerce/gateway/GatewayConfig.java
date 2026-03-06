package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10,20,1);
    }

    @Bean
    public KeyResolver hostNameKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", predicateSpec -> predicateSpec
                        .path("/api/products/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .circuitBreaker(config -> config.setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/products"))
                                .retry(retryConfig -> retryConfig.setRetries(10)
                                        .setMethods(HttpMethod.GET))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(hostNameKeyResolver())))
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
