package com.jeretin.demo.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class KafkaConsumerStreams {

    @Bean
    public Consumer<RiderLocation> listenRiderLocation() {
        return location -> {
            System.out.println("Received rider location: " + location.getRiderId() +
                               " at (" + location.getLatitude() + ", " + location.getLongitude() + ")");
        };
    }

    @Bean
    public Consumer<String> listenRiderStatus() {
        return status -> {
            System.out.println("Received rider status: " + status);
        };
    }
}
