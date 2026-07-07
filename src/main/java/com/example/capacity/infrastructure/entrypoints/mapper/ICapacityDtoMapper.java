package com.example.capacity.infrastructure.entrypoints.mapper;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CreateCapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CapacityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICapacityDtoMapper {
    @Mapping(target = "id", ignore = true)
    Capacity fromCreateToModel(CreateCapacityDto dto);
    CapacityDto fromModelToResponse(Capacity model);
}
