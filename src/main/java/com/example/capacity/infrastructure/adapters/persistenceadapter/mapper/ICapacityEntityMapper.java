package com.example.capacity.infrastructure.adapters.persistenceadapter.mapper;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICapacityEntityMapper {
    Capacity toModel(CapacityEntity entity);
    CapacityEntity toEntity(Capacity model);
}
