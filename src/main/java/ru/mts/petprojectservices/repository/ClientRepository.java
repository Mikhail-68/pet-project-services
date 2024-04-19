package ru.mts.petprojectservices.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.mts.petprojectservices.entity.Client;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Integer> {
}
