package com.jeretin.demo.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, RiderLocation> kafkaTemplate;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        RiderLocation riderLocation = new RiderLocation("rider123", 28.61, 77.23);
        kafkaTemplate.send("my-topic-new", riderLocation);
        return "Rider Location is sent: " + riderLocation.getRiderId();
    }
}*/
