package ru.mts.petprojectservices.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.entity.Request;

public interface FavorRepository extends ReactiveCrudRepository<Favor, Integer> {
    Mono<Favor> findByRequestId(int requestId);
    Flux<Favor> findByExecutorId(int executorId);
    Flux<Favor> findByStatus(Favor.TypeStatus typeStatus);
    Mono<Void> deleteByExecutorId(int executorId);
    Mono<Void> deleteByRequestId(int requestId);
}
