package ru.mts.petprojectservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.mapper.ExecutorMapper;
import ru.mts.petprojectservices.repository.ExecutorRepository;

@Service
public class ExecutorService {
    private final ExecutorRepository executorRepository;
    private final ExecutorMapper executorMapper;

    @Autowired
    public ExecutorService(ExecutorRepository executorRepository, ExecutorMapper executorMapper) {
        this.executorRepository = executorRepository;
        this.executorMapper = executorMapper;
    }

    public Flux<Executor> getAll() {
        return executorRepository.findAll();
    }

    public Mono<Executor> getById(int id) {
        return executorRepository.findById(id);
    }

    public Mono<Void> deleteById(int id) {
        return executorRepository.deleteById(id);
    }

    public Mono<Executor> save(Mono<ExecutorDto> executorDto) {
        return executorDto.flatMap(x -> executorRepository.save(executorMapper.executorDtoToExecutor(x)));
    }
}
