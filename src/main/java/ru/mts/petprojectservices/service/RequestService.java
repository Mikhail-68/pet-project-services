package ru.mts.petprojectservices.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Request;


public interface RequestService {

    Flux<RequestOutDto> getAll();

    Mono<RequestOutDto> getById(int id);

    Flux<RequestOutDto> getByClientId(int clientId);

    Mono<Void> deleteById(int id);

    Mono<Void> deleteByClientId(int clientId);

    Mono<Request> save(Mono<RequestInDto> requestMono);

}