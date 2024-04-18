package ru.mts.petprojectservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.repository.ClientRepository;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ClientRepository clientRepository;

    @Autowired
    public TestController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/{name}")
    public Mono<String> showName(@PathVariable String name) {
        return Mono.just("Hello, " + name);
    }

    @GetMapping("/clients")
    public Flux<Client> getClients() {
        return clientRepository.findAll();
    }
}
