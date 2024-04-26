package ru.mts.petprojectservices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.exception.ClientDoesNotExistException;
import ru.mts.petprojectservices.service.ClientService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RequestMapper {

    @Autowired
    protected ClientService clientService;

    public abstract RequestOutDto requestToRequestOutDto(Request request);

    public Flux<RequestOutDto> requestToRequestOutDto(Flux<Request> requestFlux) {
        return requestFlux.map(this::requestToRequestOutDto)
                .flatMap(requestOutDto -> clientService.getById(requestOutDto.getClientId())
                        .switchIfEmpty(Mono.error(ClientDoesNotExistException::new))
                        .map(client -> {
                            requestOutDto.setClient(client);
                            return requestOutDto;
                        })
                );
    }
}
