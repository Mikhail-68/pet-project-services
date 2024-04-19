package ru.mts.petprojectservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.service.ClientService;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public Flux<Client> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/{id}")
    public Mono<Client> getClientById(@PathVariable int id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteClientById(@PathVariable int id) {
        return clientService.deleteClientById(id);
    }

    @PostMapping
    public Mono<Client> saveClient(@RequestBody ClientDto clientDto) {
        return clientService.saveClient(clientDto);
    }
}
