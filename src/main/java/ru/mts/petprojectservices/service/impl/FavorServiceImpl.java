package ru.mts.petprojectservices.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.mapper.FavorMapper;
import ru.mts.petprojectservices.repository.FavorRepository;
import ru.mts.petprojectservices.service.FavorService;
import ru.mts.petprojectservices.service.RequestService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavorServiceImpl implements FavorService {
    private final FavorRepository favorRepository;
    private final FavorMapper favorMapper;
    private final RequestService requestService;

    @Override
    public Flux<FavorOutDto> getAll() {
        return favorMapper.favorToFavorOutDto(favorRepository.findAll());
    }

    @Override
    public Mono<FavorOutDto> getById(int id) {
        return favorMapper.favorToFavorOutDto(favorRepository.findById(id).flux()).singleOrEmpty();
    }

    @Override
    public Flux<FavorOutDto> getByClientId(int clientId) {
        return favorMapper.favorToFavorOutDto(favorRepository.findAll())
                .flatMap(favor -> requestService.getByClientId(clientId)
                        .filter(requestOutDto -> Objects.equals(requestOutDto.getId(), favor.getRequestId()))
                        .map(requestOutDto -> favor));
    }

    @Override
    public Flux<FavorOutDto> getByExecutorId(int executorId) {
        return favorMapper.favorToFavorOutDto(favorRepository.findByExecutorId(executorId));
    }

    @Override
    public Flux<FavorOutDto> getByStatus(String statusName) {
        try {
            return favorMapper.favorToFavorOutDto(favorRepository.findByStatus(Favor.TypeStatus.valueOf(statusName.toUpperCase())));
        } catch (IllegalArgumentException e) {
            return Flux.empty();
        }
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return favorRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteByClientId(int clientId) {
        return favorRepository.findAll()
                .flatMap(favor -> requestService.getByClientId(clientId)
                        .any(requestOutDto -> Objects.equals(requestOutDto.getId(), favor.getRequestId()))
                        .filter(aBoolean -> aBoolean)
                        .flatMap(aBoolean -> favorRepository.deleteById(favor.getId()))
                ).singleOrEmpty();
    }

    @Override
    public Mono<Void> deleteByExecutorId(int executorId) {
        return favorRepository.deleteByExecutorId(executorId);
    }

    @Override
    public Mono<FavorOutDto> updateExecutor(int favorId, int executorId) {
        return favorMapper.favorToFavorOutDto(
                favorRepository.findById(favorId).flux()
                        .flatMap(request -> {
                            request.setExecutorId(executorId);
                            return favorRepository.save(request);
                        })
        ).singleOrEmpty();
    }

    @Override
    public Mono<FavorOutDto> updateStatus(int favorId, String statusName) {
        return favorMapper.favorToFavorOutDto(favorRepository.findById(favorId).flux()
                .flatMap(favorOutDto -> {
                    if (!Favor.TypeStatus.valueOf(statusName).equals(favorOutDto.getStatus())) {
                        favorOutDto.setStatus(Favor.TypeStatus.valueOf(statusName));
                        favorOutDto.setDateLastModified(LocalDateTime.now());
                    }
                    return favorRepository.save(favorOutDto);
                })
        ).singleOrEmpty();
    }

    @Override
    public Mono<Favor> save(Request request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return favorRepository.save(
                Favor.builder()
                        .requestId(request.getId())
                        .status(Favor.TypeStatus.CREATED)
                        .dateCreation(localDateTime)
                        .dateLastModified(localDateTime)
                        .build()
        );
    }
}