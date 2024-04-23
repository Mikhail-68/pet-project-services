package ru.mts.petprojectservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.service.ClientService;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/all")
    public Flux<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Client> getById(@PathVariable int id) {
        return clientService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable int id) {
        return clientService.deleteById(id);
    }

    @PostMapping
    public Mono<Client> save(@RequestBody Mono<ClientDto> clientDto) {
        return clientService.save(clientDto);
    }

}
