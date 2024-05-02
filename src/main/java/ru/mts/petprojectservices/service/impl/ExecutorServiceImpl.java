package ru.mts.petprojectservices.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.mapper.ExecutorMapper;
import ru.mts.petprojectservices.repository.ExecutorRepository;
import ru.mts.petprojectservices.service.ExecutorService;

@Service
@RequiredArgsConstructor
public class ExecutorServiceImpl implements ExecutorService {
    private final ExecutorRepository executorRepository;
    private final ExecutorMapper executorMapper;

    @Override
    public Flux<Executor> getAll() {
        return executorRepository.findAll();
    }

    @Override
    public Mono<Executor> getById(int id) {
        return executorRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return executorRepository.deleteById(id);
    }

    @Override
    public Mono<Executor> save(Mono<ExecutorDto> executorDto) {
        return executorDto.flatMap(x -> executorRepository.save(executorMapper.executorDtoToExecutor(x)));
    }

}
