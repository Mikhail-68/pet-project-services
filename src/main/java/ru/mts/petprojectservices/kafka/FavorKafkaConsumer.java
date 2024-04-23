package ru.mts.petprojectservices.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.service.FavorService;

@Service
@RequiredArgsConstructor
public class FavorKafkaConsumer {

    private final FavorService favorService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "favor-save", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    void listenerSaveFavor(String msg) {
        try {
            Request request = objectMapper.readValue(msg, Request.class);
            favorService.save(request).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
