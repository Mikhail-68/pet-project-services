package ru.mts.petprojectservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.mapper.ClientMapper;
import ru.mts.petprojectservices.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Flux<Client> getClients() {
        return clientRepository.findAll();
    }

    public Mono<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }

    public Mono<Void> deleteClientById(int id) {
        return clientRepository.deleteById(id);
    }

    public Mono<Client> saveClient(ClientDto clientDto) {
        return clientRepository.save(clientMapper.clientToClientDto(clientDto));
    }
}
