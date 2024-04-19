package ru.mts.petprojectservices.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.mts.petprojectservices.entity.Executor;

@Repository
public interface ExecutorRepository extends ReactiveCrudRepository<Executor, Integer> {
}
