package ru.mts.petprojectservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.service.RequestService;

@RestController
@RequestMapping("/api/v1/request")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/all")
    public Flux<RequestOutDto> getAll() {
        return requestService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Request> getById(@PathVariable int id) {
        return requestService.getById(id);
    }

    @GetMapping("/client/{id}")
    public Flux<Request> getByClientId(@PathVariable("id") int clientId) {
        return requestService.getByClientId(clientId);
    }

    @GetMapping("/executor/{id}")
    public Flux<Request> getByExecutorId(@PathVariable("id") int executorId) {
        return requestService.getByExecutorId(executorId);
    }

    @GetMapping("/status")
    public Flux<Request> getByStatus(@RequestParam String status) {
        return requestService.getByStatus(status);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable int id) {
        return requestService.deleteById(id);
    }

    @DeleteMapping("/client/{id}")
    public Mono<Void> deleteByClientId(@PathVariable("id") int clientId) {
        return requestService.deleteByClientId(clientId);
    }

    @DeleteMapping("/executor/{id}")
    public Mono<Void> deleteByExecutorId(@PathVariable("id") int executorId) {
        return requestService.deleteByExecutorId(executorId);
    }

    @PostMapping
    public Mono<Void> save(@RequestBody Mono<RequestInDto> requestInDtoMono) {
        return requestService.save(requestInDtoMono);
    }

    @PutMapping("/{requestId}/executor/{executorId}")
    public Mono<Request> updateExecutor(@PathVariable int requestId, @PathVariable int executorId) {
        return requestService.updateExecutor(requestId, executorId);
    }

    @PutMapping("/{requestId}/status")
    public Mono<Request> updateStatus(@PathVariable int requestId, @RequestParam String status) {
        return requestService.updateStatus(requestId, status);
    }

}
