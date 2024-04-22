package ru.mts.petprojectservices.service;

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
import ru.mts.petprojectservices.mapper.RequestMapper;
import ru.mts.petprojectservices.repository.RequestRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ClientService clientService;
    private final KafkaTemplate<String, String> kafkaTemplateString;
    private final ObjectMapper objectMapper;

    public Flux<RequestOutDto> getAll() {
        return requestToRequestOutDto(requestRepository.findAll());
    }

    public Mono<RequestOutDto> getById(int id) {
        return requestToRequestOutDto(requestRepository.findById(id).flux()).singleOrEmpty();
    }

    public Flux<RequestOutDto> getByClientId(int clientId) {
        return requestToRequestOutDto(requestRepository.findByClientId(clientId));
    }

    public Mono<Void> deleteById(int id) {
        return requestRepository.deleteById(id);
    }

    public Mono<Void> deleteByClientId(int clientId) {
        return requestRepository.deleteByClientId(clientId);
    }

    public Mono<Void> save(Mono<RequestInDto> requestMono) {
        return requestMono.map(x -> {
            Request request = Request.builder()
                    .clientId(x.getClientId())
                    .message(x.getMessage())
                    .address(x.getAddress())
                    .dateCreation(LocalDateTime.now())
                    .build();
            requestRepository.save(request)
                    .map(req -> {
                        try {
                            String str = objectMapper.writeValueAsString(req);
                            kafkaTemplateString.send("favor-save", String.valueOf(req.getId()), str);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        return req;
                    })
                    .subscribe();
            return x;
        }).then();
    }

    private Flux<RequestOutDto> requestToRequestOutDto(Flux<Request> requestFlux) {
        return requestFlux.map(requestMapper::requestToRequestOutDto)
                .flatMap(requestOutDto -> clientService.getById(requestOutDto.getClientId())
                        .map(client -> {
                            requestOutDto.setClient(client);
                            return requestOutDto;
                        }));
    }

}