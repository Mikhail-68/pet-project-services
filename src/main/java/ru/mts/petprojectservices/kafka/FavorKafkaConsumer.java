package ru.mts.petprojectservices.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.exception.ObjectProcessingException;
import ru.mts.petprojectservices.service.FavorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavorKafkaConsumer {

    private final FavorService favorService;
    private final ObjectMapper objectMapper;

    @Counted(value = "kafka.consumer", extraTags = {"method", "listenerSaveFavor"})
    @KafkaListener(topics = "favor-save", groupId = "group1")
    void listenerSaveFavor(String msg) {
        log.info("Kafka consumer start. Topic: favor-save");
        try {
            Request request = objectMapper.readValue(msg, Request.class);
            favorService.save(request).subscribe();
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации объекта");
            throw new ObjectProcessingException(e);
        }
    }
}
