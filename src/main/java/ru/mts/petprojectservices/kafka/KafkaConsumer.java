package ru.mts.petprojectservices.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.repository.ClientRepository;
import ru.mts.petprojectservices.repository.ExecutorRepository;
import ru.mts.petprojectservices.repository.RequestRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final ExecutorRepository executorRepository;

    @KafkaListener(topics = "request-save", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    private void listenerSaveRequest(String msg) {
        try {
            Request request = objectMapper.readValue(msg, Request.class);
            requestRepository.save(request).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "client-save", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    private void listenerSaveClient(String msg) {
        try {
            Client client = objectMapper.readValue(msg, Client.class);
            clientRepository.save(client).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "client-delete", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    private void listenerDeleteClient(String msg) {
        clientRepository.deleteById(Integer.parseInt(msg)).subscribe();
    }

    @KafkaListener(topics = "executor-save", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    private void listenerSaveExecutor(String msg) {
        try {
            Executor executor = objectMapper.readValue(msg, Executor.class);
            executorRepository.save(executor).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "executor-delete", groupId = "group1", containerFactory = "kafkaListenerContainerFactoryString")
    private void listenerDeleteExecutor(String msg) {
        executorRepository.deleteById(Integer.parseInt(msg)).subscribe();
    }

}