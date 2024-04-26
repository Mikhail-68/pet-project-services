package ru.mts.petprojectservices.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.exception.ClientDoesNotExistException;
import ru.mts.petprojectservices.mapper.RequestMapperImpl;
import ru.mts.petprojectservices.service.ClientService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestMapperTest {
    @Mock
    private ClientService clientService;
    @InjectMocks
    private RequestMapperImpl requestMapper;

    @Nested
    class FavorToFavorDtoFlux {
        @Test
        public void requestToRequestOutDtoFlux_returnRequestOutDtoFlux() {
            int requestId = 1, clientId = 5;
            Client client = new Client(clientId, "Fio client");
            RequestOutDto requestOutDto = RequestOutDto.builder().clientId(clientId).client(client).build();

            when(clientService.getById(requestOutDto.getClientId())).thenReturn(Mono.just(client));

            Request request = Request.builder().id(requestId).clientId(clientId).build();
            StepVerifier.create(requestMapper.requestToRequestOutDto(Flux.just(request)))
                    .assertNext(actualRequestOutDto ->
                            Assertions.assertEquals(actualRequestOutDto, requestOutDto)
                    )
                    .verifyComplete();
        }

        @Test
        public void requestToRequestOutDtoFlux_whenDoesNotExistClientInRequest_throwException() {
            when(clientService.getById(Mockito.anyInt())).thenReturn(Mono.empty());

            Request request = Request.builder().id(1).clientId(2).build();
            StepVerifier.create(requestMapper.requestToRequestOutDto(Flux.just(request)))
                    .verifyError(ClientDoesNotExistException.class);
        }
    }
}
