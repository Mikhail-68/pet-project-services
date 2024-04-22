package ru.mts.petprojectservices.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerTest {

    @KafkaListener(topics = "message", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    void listener(String msg) {
        System.out.println("message: " + msg);
    }
}
