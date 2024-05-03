package ru.mts.petprojectservices.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.entity.Executor;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.exception.RequestDoesNotExistException;
import ru.mts.petprojectservices.mapper.FavorMapperImpl;
import ru.mts.petprojectservices.service.ExecutorService;
import ru.mts.petprojectservices.service.RequestService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavorMapperTest {

    @Mock
    private RequestService requestService;
    @Mock
    private ExecutorService executorService;
    @InjectMocks
    private FavorMapperImpl favorMapper;

    @Nested
    class FavorToFavorDtoFlux {
        @Test
        void favorToFavorDtoFlux_returnFavorOutDto() {
            LocalDateTime dateCreation = LocalDateTime.now();
            LocalDateTime dateLastModified = LocalDateTime.now();
            Client client = new Client(1, "fio client");
            RequestOutDto requestOutDto = RequestOutDto.builder()
                    .id(1)
                    .clientId(client.getId())
                    .client(client)
                    .message("some msg client 1")
                    .address("address client 1")
                    .dateCreation(dateCreation)
                    .build();
            Executor executor = new Executor(1, "Fio executor 1");
            Favor favor = Favor.builder()
                    .id(1)
                    .requestId(requestOutDto.getId())
                    .executorId(executor.getId())
                    .status(Favor.TypeStatus.IN_PROGRESS)
                    .dateCreation(dateCreation)
                    .dateLastModified(dateLastModified)
                    .build();
            FavorOutDto favorOutDto = FavorOutDto.builder()
                    .id(favor.getId())
                    .request(requestOutDto)
                    .executor(executor)
                    .status(favor.getStatus())
                    .dateCreation(favor.getDateCreation())
                    .dateLastModified(favor.getDateLastModified())
                    .build();

            when(requestService.getById(requestOutDto.getId())).thenReturn(Mono.just(
                    RequestOutDto.builder()
                            .id(requestOutDto.getId())
                            .clientId(requestOutDto.getClientId())
                            .client(new Client(client.getId(), client.getFio()))
                            .message(requestOutDto.getMessage())
                            .address(requestOutDto.getAddress())
                            .dateCreation(requestOutDto.getDateCreation())
                            .build()
            ));
            when(executorService.getById(favor.getExecutorId())).thenReturn(
                    Mono.just(new Executor(executor.getId(), executor.getFio()))
            );

            StepVerifier.create(favorMapper.favorToFavorOutDto(Flux.just(favor)))
                    .assertNext(actualFavorOutDto -> Assertions.assertEquals(actualFavorOutDto, favorOutDto))
                    .verifyComplete();
        }

        @Test
        void favorToFavorDtoFlux_whenDoesNotExistClientInFavor_throwException() {
            int requestId = 1;
            when(requestService.getById(requestId)).thenReturn(Mono.empty());

            StepVerifier.create(favorMapper.favorToFavorOutDto(
                            Flux.just(Favor.builder().requestId(requestId).build())))
                    .verifyError(RequestDoesNotExistException.class);
        }

        @Test
        void favorToFavorDtoFlux_whenDoesNotExistExecutorInFavor_returnFavorOutDtoWithExecutorIsNull() {
            LocalDateTime dateCreation = LocalDateTime.now();
            LocalDateTime dateLastModified = LocalDateTime.now();
            RequestOutDto requestOutDto = RequestOutDto.builder()
                    .id(1)
                    .clientId(1)
                    .message("some msg client 1")
                    .address("address client 1")
                    .dateCreation(dateCreation)
                    .build();
            Favor favor = Favor.builder()
                    .id(1)
                    .requestId(requestOutDto.getId())
                    .status(Favor.TypeStatus.IN_PROGRESS)
                    .executorId(1)
                    .dateCreation(dateCreation)
                    .dateLastModified(dateLastModified)
                    .build();
            FavorOutDto favorOutDto = FavorOutDto.builder()
                    .id(favor.getId())
                    .request(requestOutDto)
                    .executor(null)
                    .executorId(favor.getExecutorId())
                    .status(favor.getStatus())
                    .dateCreation(favor.getDateCreation())
                    .dateLastModified(favor.getDateLastModified())
                    .build();

            when(requestService.getById(requestOutDto.getId())).thenReturn(Mono.just(
                    RequestOutDto.builder()
                            .id(requestOutDto.getId())
                            .clientId(requestOutDto.getClientId())
                            .message(requestOutDto.getMessage())
                            .address(requestOutDto.getAddress())
                            .dateCreation(dateCreation)
                            .build()
            ));
            when(executorService.getById(favorOutDto.getId())).thenReturn(Mono.empty());

            StepVerifier.create(favorMapper.favorToFavorOutDto(Flux.just(favor)))
                    .assertNext(actualFavorOutDto -> Assertions.assertEquals(favorOutDto, actualFavorOutDto))
                    .verifyComplete();
        }
    }
}
