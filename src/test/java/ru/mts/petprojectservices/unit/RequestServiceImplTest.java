package ru.mts.petprojectservices.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.mts.petprojectservices.dto.in.RequestInDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.exception.ClientDoesNotExistException;
import ru.mts.petprojectservices.repository.RequestRepository;
import ru.mts.petprojectservices.service.ClientService;
import ru.mts.petprojectservices.service.impl.RequestServiceImpl;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private RequestServiceImpl requestService;

    @Nested
    class Save {
        @Test
        void save_whenDoesNotExistClient_throwClientDoesNotException() {
            RequestInDto requestInDto = new RequestInDto(1, "msg", "address");
            when(clientService.getById(1)).thenReturn(Mono.empty());
            StepVerifier.create(requestService.save(Mono.just(requestInDto)))
                    .verifyError(ClientDoesNotExistException.class);
        }

        @Test
        void save_whenExistClient_returnRequest() throws JsonProcessingException {
            RequestInDto requestInDto = new RequestInDto(1, "msg", "address");
            Client client = new Client(1, "Fio");
            Request savedRequest = Request.builder().id(1).build();
            String serializeRequest = "Request JSON";

            when(clientService.getById(requestInDto.getClientId())).thenReturn(Mono.just(client));
            when(requestRepository.save(Mockito.any())).thenReturn(Mono.just(savedRequest));
            when(objectMapper.writeValueAsString(Mockito.any())).thenReturn(serializeRequest);

            StepVerifier.create(requestService.save(Mono.just(requestInDto)))
                    .assertNext(request -> Assertions.assertEquals(savedRequest.getId(), request.getId()))
                    .verifyComplete();

            Mockito.verify(kafkaTemplate, Mockito.times(1))
                    .send(
                            Mockito.anyString(),
                            Mockito.eq(savedRequest.getId().toString()),
                            Mockito.eq(serializeRequest)
                    );
        }
    }
}