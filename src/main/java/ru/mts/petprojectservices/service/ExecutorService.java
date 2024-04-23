package ru.mts.petprojectservices.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;


public interface ExecutorService {

    Flux<Executor> getAll();

    Mono<Executor> getById(int id);

    Mono<Void> deleteById(int id);

    Mono<Executor> save(Mono<ExecutorDto> executorDto);

}
