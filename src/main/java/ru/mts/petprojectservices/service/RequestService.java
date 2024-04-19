package ru.mts.petprojectservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.mapper.RequestMapper;
import ru.mts.petprojectservices.repository.RequestRepository;

import java.time.LocalDateTime;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ClientService clientService;
    private final ExecutorService executorService;

    @Autowired
    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper, ClientService clientService, ExecutorService executorService) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.clientService = clientService;
        this.executorService = executorService;
    }

    public Flux<RequestOutDto> getAll() {
        return requestToRequestOutDto(requestRepository.findAll());
    }

    public Mono<Request> getById(int id) {
        return requestRepository.findById(id);
    }

    public Flux<Request> getByClientId(int clientId) {
        return requestRepository.findByClientId(clientId);
    }

    public Flux<Request> getByExecutorId(int executorId) {
        return requestRepository.findByExecutorId(executorId);
    }

    public Flux<Request> getByStatus(String statusName) {
        try {
            return requestRepository.findByStatus(Request.TypeStatus.valueOf(statusName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Flux.empty();
        }
    }

    public Mono<Void> deleteById(int id) {
        return requestRepository.deleteById(id);
    }

    public Mono<Void> deleteByClientId(int clientId) {
        return requestRepository.deleteByClientId(clientId);
    }

    public Mono<Void> deleteByExecutorId(int executorId) {
        return requestRepository.deleteByClientId(executorId);
    }

    public Mono<Request> updateExecutor(int requestId, int executorId) {
        return requestRepository.findById(requestId)
                .flatMap(request -> {
                    request.setExecutorId(executorId);
                    return requestRepository.save(request);
                });
    }

    public Mono<Request> updateStatus(int requestId, String statusName) {
        return requestRepository.findById(requestId)
                .flatMap(request -> {
                    if(!Request.TypeStatus.valueOf(statusName).equals(request.getStatus())) {
                        request.setStatus(Request.TypeStatus.valueOf(statusName));
                        request.setDateLastModified(LocalDateTime.now());
                    }
                    return requestRepository.save(request);
                });
    }

    public Mono<Request> save(Mono<RequestInDto> requestMono) {
        return requestMono.flatMap(x -> {
            LocalDateTime date = LocalDateTime.now();
            return requestRepository.save(
                    Request.builder()
                            .clientId(x.getClientId())
                            .message(x.getMessage())
                            .address(x.getAddress())
                            .status(Request.TypeStatus.CREATED)
                            .dateCreation(date)
                            .dateLastModified(date)
                            .build()
            );
        });
    }

    private Flux<RequestOutDto> requestToRequestOutDto(Flux<Request> requestFlux) {
        return requestFlux.map(requestMapper::requestToRequestOutDto)
                .flatMap(requestOutDto -> clientService.getById(requestOutDto.getClientId())
                        .map(client -> {
                            requestOutDto.setClient(client);
                            return requestOutDto;
                        }))
                .flatMap(requestOutDto -> executorService.getById(requestOutDto.getExecutorId())
                        .defaultIfEmpty(new Executor(-1, ""))
                        .map(executor -> {
                            if (executor.getId() > 0)
                                requestOutDto.setExecutor(executor);
                            return requestOutDto;
                        }));
    }

}

