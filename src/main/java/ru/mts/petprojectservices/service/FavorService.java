package ru.mts.petprojectservices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.mapper.FavorMapper;
import ru.mts.petprojectservices.repository.FavorRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavorService {
    private final FavorRepository favorRepository;
    private final FavorMapper favorMapper;

    private final RequestService requestService;
    private final ExecutorService executorService;

    public Flux<FavorOutDto> getAll() {
        return favorToFavorOutDto(favorRepository.findAll());
    }

    public Mono<FavorOutDto> getById(int id) {
        return favorToFavorOutDto(favorRepository.findById(id).flux()).singleOrEmpty();
    }

    public Flux<FavorOutDto> getByClientId(int clientId) {
        return favorToFavorOutDto(favorRepository.findAll())
                .flatMap(favor -> requestService.getByClientId(clientId)
                        .filter(requestOutDto -> Objects.equals(requestOutDto.getId(), favor.getRequestId()))
                        .map(requestOutDto -> favor));
    }

    public Flux<FavorOutDto> getByExecutorId(int executorId) {
        return favorToFavorOutDto(favorRepository.findByExecutorId(executorId));
    }

    public Flux<FavorOutDto> getByStatus(String statusName) {
        try {
            return favorToFavorOutDto(favorRepository.findByStatus(Favor.TypeStatus.valueOf(statusName.toUpperCase())));
        } catch (IllegalArgumentException e) {
            return Flux.empty();
        }
    }

    public Mono<Void> deleteById(int id) {
        return favorRepository.deleteById(id);
    }

    public Mono<Void> deleteByClientId(int clientId) {
        return favorRepository.findAll()
                .flatMap(favor -> requestService.getByClientId(clientId)
                        .any(requestOutDto -> Objects.equals(requestOutDto.getId(), favor.getRequestId()))
                        .filter(aBoolean -> aBoolean)
                        .flatMap(aBoolean -> favorRepository.deleteById(favor.getId())
                        )
                ).singleOrEmpty();
    }

    public Mono<Void> deleteByExecutorId(int executorId) {
        return favorRepository.deleteByExecutorId(executorId);
    }

    public Mono<FavorOutDto> updateExecutor(int favorId, int executorId) {
        return favorToFavorOutDto(favorRepository.findById(favorId).flux()
                .flatMap(request -> {
                    request.setExecutorId(executorId);
                    return favorRepository.save(request);
                })).singleOrEmpty();
    }

    public Mono<FavorOutDto> updateStatus(int favorId, String statusName) {
        return favorToFavorOutDto(favorRepository.findById(favorId).flux()
                .flatMap(favorOutDto -> {
                    if (!Favor.TypeStatus.valueOf(statusName).equals(favorOutDto.getStatus())) {
                        favorOutDto.setStatus(Favor.TypeStatus.valueOf(statusName));
                        favorOutDto.setDateLastModified(LocalDateTime.now());
                    }
                    return favorRepository.save(favorOutDto);
                })).singleOrEmpty();
    }

    public void save(Request request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        favorRepository.save(
                Favor.builder()
                        .requestId(request.getId())
                        .status(Favor.TypeStatus.CREATED)
                        .dateCreation(localDateTime)
                        .dateLastModified(localDateTime)
                        .build()
        ).subscribe();
    }

    private Flux<FavorOutDto> favorToFavorOutDto(Flux<Favor> favorFlux) {
        return favorFlux.map(favorMapper::favorToFavorOutDto)
                .flatMap(favorOutDto -> requestService.getById(favorOutDto.getRequestId())
                        .map(requestOutDto -> {
                            favorOutDto.setRequest(requestOutDto);
                            return favorOutDto;
                        }))
                .flatMap(favorOutDto -> executorService.getById(favorOutDto.getExecutorId())
                        .defaultIfEmpty(new Executor(-1, ""))
                        .map(executor -> {
                            if (executor.getId() > 0)
                                favorOutDto.setExecutor(executor);
                            return favorOutDto;
                        }));
    }

}
