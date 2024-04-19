package ru.mts.petprojectservices.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.entity.Request;

@Repository
public interface RequestRepository extends ReactiveCrudRepository<Request, Integer> {
    Flux<Request> findByClientId(int clientId);

    Flux<Request> findByExecutorId(int executorId);

    Flux<Request> findByStatus(Request.TypeStatus typeStatus);

    Mono<Void> deleteByClientId(int clientId);

    Mono<Void> deleteByExecutorId(int executorId);
}
