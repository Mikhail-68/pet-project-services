package ru.mts.petprojectservices.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;

public interface ClientService {

    Flux<Client> getAll();

    Mono<Client> getById(int id);

    Mono<Void> deleteById(int id);

    Mono<Client> save(Mono<ClientDto> clientDto);

    Mono<Integer> getClientIdWhoHasMinimumOrders();

}