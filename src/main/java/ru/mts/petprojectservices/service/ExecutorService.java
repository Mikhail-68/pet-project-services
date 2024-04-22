package ru.mts.petprojectservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, String> kafkaTemplateString;
    private final ObjectMapper objectMapper;

    public Flux<Executor> getAll() {
        return executorRepository.findAll();
    }

    public Mono<Executor> getById(int id) {
        return executorRepository.findById(id);
    }

    public Mono<Void> deleteById(int id) {
        kafkaTemplateString.send("executor-delete", String.valueOf(id), String.valueOf(id));
        return Mono.empty();
    }

    public Mono<Void> save(Mono<ExecutorDto> executorDto) {
        return executorDto.map(x -> {
            try {
                String str = objectMapper.writeValueAsString(executorMapper.executorDtoToExecutor(x));
                return kafkaTemplateString.send("executor-save", x.getFio(), str);
            } catch (JsonProcessingException ignored) {
            }
            return null;
        }).then();
    }

}
