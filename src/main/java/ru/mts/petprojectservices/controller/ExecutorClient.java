package ru.mts.petprojectservices.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.service.ExecutorService;

@RestController
@RequestMapping("/api/v1/executor")
public class ExecutorClient {

    private final ExecutorService executorService;

    public ExecutorClient(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @GetMapping("/all")
    public Flux<Executor> getClients() {
        return executorService.getExecutors();
    }

    @GetMapping("/{id}")
    public Mono<Executor> getClientById(@PathVariable int id) {
        return executorService.getExecutorById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteClientById(@PathVariable int id) {
        return executorService.deleteExecutorById(id);
    }

    @PostMapping
    public Mono<Executor> saveClient(@RequestBody ExecutorDto executorDto) {
        return executorService.saveExecutor(executorDto);
    }
}
