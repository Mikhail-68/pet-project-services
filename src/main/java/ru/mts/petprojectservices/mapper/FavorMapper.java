package ru.mts.petprojectservices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.mts.petprojectservices.dto.out.FavorOutDto;
import ru.mts.petprojectservices.entity.Favor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavorMapper {
    FavorOutDto favorToFavorOutDto(Favor favor);
}
