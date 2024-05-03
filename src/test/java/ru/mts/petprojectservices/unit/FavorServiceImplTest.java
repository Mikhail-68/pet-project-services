package ru.mts.petprojectservices.unit;

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
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.dto.out.RequestOutDto;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.mapper.FavorMapper;
import ru.mts.petprojectservices.repository.FavorRepository;
import ru.mts.petprojectservices.service.RequestService;
import ru.mts.petprojectservices.service.impl.FavorServiceImpl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavorServiceImplTest {
    @Mock
    private FavorRepository favorRepository;
    @Mock
    private FavorMapper favorMapper;
    @Mock
    private RequestService requestService;
    @InjectMocks
    private FavorServiceImpl favorService;

    @Nested
    class GetByClientId {
        @Test
        void getByClientId_whenRequestWithClientIdDoesNotExist_returnEmpty() {
            int clientId = 5;
            int requestId = 3;

            when(favorMapper.favorToFavorOutDto((Flux<Favor>) Mockito.any())).thenReturn(
                    Flux.just(FavorOutDto.builder().requestId(requestId).build()));
            when(requestService.getByClientId(clientId)).thenReturn(Flux.just(RequestOutDto.builder().id(999).build()));

            StepVerifier.create(favorService.getByClientId(clientId)).expectNextCount(0).verifyComplete();
        }
    }

    @Nested
    class DeleteByClientId {
        @Test
        void deleteByClientId_whenRequestExist_deleteClient() {
            int clientId = 3;
            int requestId = 10;
            Favor favor = Favor.builder().id(1).requestId(requestId).build();
            RequestOutDto requestOutDto = RequestOutDto.builder().id(requestId).build();

            when(favorRepository.findAll()).thenReturn(Flux.just(favor));
            when(favorRepository.deleteById(Mockito.anyInt())).thenReturn(Mono.empty());
            when(requestService.getByClientId(clientId)).thenReturn(Flux.just(requestOutDto));

            favorService.deleteByClientId(clientId).block();

            verify(favorRepository, Mockito.times(1)).deleteById(favor.getId());
        }

        @Test
        void deleteByClientId_whenRequestDoesNotExist_notDeleteClient() {
            int clientId = 3;
            int requestId = 10;
            int doesNotRequestId = 999;
            Favor favor = Favor.builder().id(1).requestId(requestId).build();
            RequestOutDto requestOutDto = RequestOutDto.builder().id(doesNotRequestId).build();

            when(favorRepository.findAll()).thenReturn(Flux.just(favor));
            when(requestService.getByClientId(clientId)).thenReturn(Flux.just(requestOutDto));

            favorService.deleteByClientId(clientId).subscribe();

            verify(favorRepository, Mockito.times(0)).deleteById(favor.getId());
        }
    }
}
