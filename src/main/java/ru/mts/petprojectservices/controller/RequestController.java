package ru.mts.petprojectservices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.service.RequestService;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/all")
    public Flux<RequestOutDto> getAll() {
        return requestService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<RequestOutDto> getById(@PathVariable int id) {
        return requestService.getById(id);
    }

    @GetMapping("/client/{id}")
    public Flux<RequestOutDto> getByClientId(@PathVariable("id") int clientId) {
        return requestService.getByClientId(clientId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable int id) {
        return requestService.deleteById(id);
    }

    @DeleteMapping("/client/{id}")
    public Mono<Void> deleteByClientId(@PathVariable("id") int clientId) {
        return requestService.deleteByClientId(clientId);
    }

    @PostMapping
    public Mono<Void> save(@RequestBody Mono<RequestInDto> requestInDtoMono) {
        return requestService.save(requestInDtoMono);
    }

}
