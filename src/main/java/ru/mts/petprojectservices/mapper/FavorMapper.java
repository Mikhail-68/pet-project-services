package ru.mts.petprojectservices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.exception.RequestDoesNotExistException;
import ru.mts.petprojectservices.service.ExecutorService;
import ru.mts.petprojectservices.service.RequestService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class FavorMapper {

    protected RequestService requestService;
    protected ExecutorService executorService;

    public abstract FavorOutDto favorToFavorOutDto(Favor favor);

    public Flux<FavorOutDto> favorToFavorOutDto(Flux<Favor> favorFlux) {
        return favorFlux.map(this::favorToFavorOutDto)
                .flatMap(favorOutDto -> requestService.getById(favorOutDto.getRequestId())
                        .switchIfEmpty(Mono.error(RequestDoesNotExistException::new))
                        .map(requestOutDto -> {
                            favorOutDto.setRequest(requestOutDto);
                            return favorOutDto;
                        })
                )
                .flatMap(favorOutDto -> executorService.getById(favorOutDto.getExecutorId())
                        .defaultIfEmpty(new Executor(-1, ""))
                        .map(executor -> {
                            if (executor.getId() > 0)
                                favorOutDto.setExecutor(executor);
                            return favorOutDto;
                        })
                );
    }

    @Autowired
    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
