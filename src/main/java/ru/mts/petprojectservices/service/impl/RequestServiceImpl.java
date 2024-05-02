package ru.mts.petprojectservices.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.exception.ClientDoesNotExistException;
import ru.mts.petprojectservices.mapper.RequestMapper;
import ru.mts.petprojectservices.repository.RequestRepository;
import ru.mts.petprojectservices.service.ClientService;
import ru.mts.petprojectservices.service.RequestService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ClientService clientService;
    private final KafkaTemplate<String, String> kafkaTemplateString;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<RequestOutDto> getAll() {
        return requestMapper.requestToRequestOutDto(requestRepository.findAll());
    }

    @Override
    public Mono<RequestOutDto> getById(int id) {
        return requestMapper.requestToRequestOutDto(requestRepository.findById(id).flux()).singleOrEmpty();
    }

    @Override
    public Flux<RequestOutDto> getByClientId(int clientId) {
        return requestMapper.requestToRequestOutDto(requestRepository.findByClientId(clientId));
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return requestRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteByClientId(int clientId) {
        return requestRepository.deleteByClientId(clientId);
    }

    @Override
    public Mono<Request> save(Mono<RequestInDto> requestMono) {
        return requestMono.flatMap(requestInDto -> clientService.getById(requestInDto.getClientId())
                .switchIfEmpty(Mono.error(ClientDoesNotExistException::new))
                .flatMap(req -> {
                    Request request = Request.builder()
                            .clientId(requestInDto.getClientId())
                            .message(requestInDto.getMessage())
                            .address(requestInDto.getAddress())
                            .dateCreation(LocalDateTime.now())
                            .build();
                    return requestRepository.save(request)
                            .map(savedRequest -> {
                                try {
                                    String str = objectMapper.writeValueAsString(savedRequest);
                                    kafkaTemplateString.send("favor-save", String.valueOf(savedRequest.getId()), str);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                return savedRequest;
                            });
                })
        );
    }

}