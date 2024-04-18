package ru.mts.petprojectservices.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.mts.petprojectservices.entity.Client;

public interface ClientRepository extends ReactiveCrudRepository<Client, Integer> {
}
