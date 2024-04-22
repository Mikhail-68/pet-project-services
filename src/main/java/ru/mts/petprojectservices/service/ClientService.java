package ru.mts.petprojectservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;
import ru.mts.petprojectservices.mapper.ClientMapper;
import ru.mts.petprojectservices.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final KafkaTemplate<String, String> kafkaTemplateString;
    private final ObjectMapper objectMapper;

    public Flux<Client> getAll() {
        return clientRepository.findAll();
    }

    public Mono<Client> getById(int id) {
        return clientRepository.findById(id);
    }

    public Mono<Void> deleteById(int id) {
        kafkaTemplateString.send("client-delete", String.valueOf(id), String.valueOf(id));
        return Mono.empty();
    }

    public Mono<Void> save(Mono<ClientDto> clientDto) {
        return clientDto.map(x -> {
            try {
                String str = objectMapper.writeValueAsString(clientMapper.clientDtoToClient(x));
                kafkaTemplateString.send("client-save", x.getFio(), str);
            } catch (JsonProcessingException e) {
                return Mono.error(e).subscribe();
            }
            return x;
        }).then();
    }

}
