package ru.mts.petprojectservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.service.FavorService;

@RestController
@RequestMapping("/api/v1/favor")
public class FavorController {

    private final FavorService favorService;

    @Autowired
    public FavorController(FavorService favorService) {
        this.favorService = favorService;
    }

    @GetMapping("/all")
    public Flux<FavorOutDto> getAll() {
        return favorService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<FavorOutDto> getById(@PathVariable int id) {
        return favorService.getById(id);
    }

    @GetMapping("/client/{id}")
    public Flux<FavorOutDto> getByClientId(@PathVariable("id") int clientId) {
        return favorService.getByClientId(clientId);
    }

    @GetMapping("/executor/{id}")
    public Flux<FavorOutDto> getByExecutorId(@PathVariable("id") int executorId) {
        return favorService.getByExecutorId(executorId);
    }

    @GetMapping("/status")
    public Flux<FavorOutDto> getByStatus(@RequestParam String status) {
        return favorService.getByStatus(status);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable int id) {
        return favorService.deleteById(id);
    }

    @DeleteMapping("/client/{id}")
    public Mono<Void> deleteByClientId(@PathVariable("id") int clientId) {
        return favorService.deleteByClientId(clientId);
    }

    @DeleteMapping("/executor/{id}")
    public Mono<Void> deleteByExecutorId(@PathVariable("id") int executorId) {
        return favorService.deleteByExecutorId(executorId);
    }

    @PutMapping("/{requestId}/executor/{executorId}")
    public Mono<FavorOutDto> updateExecutor(@PathVariable int requestId, @PathVariable int executorId) {
        return favorService.updateExecutor(requestId, executorId);
    }

    @PutMapping("/{requestId}/status")
    public Mono<FavorOutDto> updateStatus(@PathVariable int requestId, @RequestParam String status) {
        return favorService.updateStatus(requestId, status);
    }

}
