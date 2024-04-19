package ru.mts.petprojectservices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.mts.petprojectservices.dto.ClientDto;
import ru.mts.petprojectservices.entity.Client;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    Client clientDtoToClient(ClientDto clientDto);
    ClientDto clientToClientDto(Client client);
}
