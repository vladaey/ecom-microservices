package com.jeretin.ecommerce.order.clients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UserServiceClientConfig {
    @Bean
    public UserServiceClient userServiceInterface(@Qualifier("loadBalancedRestClient") RestClient.Builder restClientbuilder) {
        RestClient restClient = restClientbuilder.baseUrl("http://user-service").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(UserServiceClient.class);
    }
}
