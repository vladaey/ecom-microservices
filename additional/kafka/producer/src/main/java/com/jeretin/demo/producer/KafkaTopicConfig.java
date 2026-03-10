package com.jeretin.demo.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic createMyTopic() {
        return new NewTopic("my-topic-new", 3, (short) 1);
    }
}
