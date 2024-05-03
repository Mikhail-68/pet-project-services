package ru.mts.petprojectservices.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.mapper.ClientMapper;
import ru.mts.petprojectservices.repository.ClientRepository;
import ru.mts.petprojectservices.service.ClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final DatabaseClient databaseClient;

    @Override
    public Flux<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Mono<Client> getById(int id) {
        return clientRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return clientRepository.deleteById(id);
    }

    @Override
    public Mono<Client> save(Mono<ClientDto> clientDto) {
        return clientDto.flatMap(x -> clientRepository.save(clientMapper.clientDtoToClient(x)));
    }

    @Override
    public Mono<Integer> getClientIdWhoHasMinimumOrders() {
        return databaseClient.sql("select e.id as exec_id " +
                        "from favor f right join executor e on f.executor_id = e.id " +
                        "group by e.id " +
                        "order by count(f.id) asc " +
                        "limit 1 ")
                .map((x, y) -> x.get("exec_id", Integer.class))
                .first();
    }

}
