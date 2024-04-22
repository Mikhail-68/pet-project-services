package ru.mts.petprojectservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/send")
public class TestKafka {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public void sendMessage(@RequestParam String msg) {
        kafkaTemplate.send("message", msg);
    }
}
