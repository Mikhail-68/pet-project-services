package ru.mts.petprojectservices.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.entity.Request;


public interface FavorService {

    Flux<FavorOutDto> getAll();

    Mono<FavorOutDto> getById(int id);

    Flux<FavorOutDto> getByClientId(int clientId);

    Flux<FavorOutDto> getByExecutorId(int executorId);

    Flux<FavorOutDto> getByStatus(String statusName);

    Mono<Void> deleteById(int id);

    Mono<Void> deleteByClientId(int clientId);

    Mono<Void> deleteByExecutorId(int executorId);

    Mono<FavorOutDto> updateExecutor(int favorId, int executorId);

    Mono<FavorOutDto> updateStatus(int favorId, String statusName);

    Mono<Void> save(Request request);

}
