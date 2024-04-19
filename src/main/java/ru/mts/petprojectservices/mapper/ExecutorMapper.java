package ru.mts.petprojectservices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.mts.petprojectservices.dto.ExecutorDto;
import ru.mts.petprojectservices.entity.Executor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExecutorMapper {
    Executor executorDtoToExecutor(ExecutorDto executorDto);
    ExecutorDto executorToExecutorDto(Executor executor);
}
