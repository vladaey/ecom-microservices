package com.ecommerce.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                                authorizeExchangeSpec
//                                        .pathMatchers("/api/products/**").hasRole("PRODUCT")
//                                        .pathMatchers("/api/orders/**").hasRole("ORDER")
//                                        .pathMatchers("/api/users/**").hasRole("USER")
                                        .anyExchange().authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec.jwt(jwtSpec ->
                                        jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
                .build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsMap("resource_access")
                    .entrySet().stream()
                    .filter(stringObjectEntry -> stringObjectEntry.getKey().equals("oauth2-pkce"))
                    .flatMap(stringObjectEntry -> ((Map<String, List<String>>)stringObjectEntry.getValue())
                    .get("roles").stream()).toList();

            System.out.println("Extracted Roles: " + roles);
            return Flux.fromIterable(roles).map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        });
        return jwtAuthenticationConverter;
    }
}
