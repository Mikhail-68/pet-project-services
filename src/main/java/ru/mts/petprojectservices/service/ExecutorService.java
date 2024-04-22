package ru.mts.petprojectservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.mapper.ExecutorMapper;
import ru.mts.petprojectservices.repository.ExecutorRepository;

@Service
@RequiredArgsConstructor
public class ExecutorService {
    private final ExecutorRepository executorRepository;
    private final ExecutorMapper executorMapper;

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
