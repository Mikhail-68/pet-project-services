package ru.mts.petprojectservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.mapper.ClientMapper;
import ru.mts.petprojectservices.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public Flux<Client> getAll() {
        return clientRepository.findAll();
    }

    public Mono<Client> getById(int id) {
        return clientRepository.findById(id);
    }

    public Mono<Void> deleteById(int id) {
        return clientRepository.deleteById(id);
    }

    public Mono<Client> save(Mono<ClientDto> clientDto) {
        return clientDto.flatMap(x -> clientRepository.save(clientMapper.clientDtoToClient(x)));
    }

}
