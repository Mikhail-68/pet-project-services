package ru.mts.petprojectservices.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.service.ExecutorService;

@RestController
@RequestMapping("/api/v1/executor")
public class ExecutorController {

    private final ExecutorService executorService;

    public ExecutorController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @GetMapping("/all")
    public Flux<Executor> getAll() {
        return executorService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Executor> getById(@PathVariable int id) {
        return executorService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable int id) {
        return executorService.deleteById(id);
    }

    @PostMapping
    public Mono<Executor> save(@RequestBody Mono<ExecutorDto> executorDto) {
        return executorService.save(executorDto);
    }
}
