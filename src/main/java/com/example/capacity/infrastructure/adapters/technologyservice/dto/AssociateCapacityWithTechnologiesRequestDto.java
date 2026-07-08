package com.example.capacity.infrastructure.adapters.technologyservice.dto;

import com.example.capacity.domain.model.Capacity;

import java.util.List;

public record AssociateCapacityWithTechnologiesRequestDto(
        Long capacityId,
        List<Long> technologyIds
) {
    public static AssociateCapacityWithTechnologiesRequestDto fromCapacity(Capacity capacity) {
        return new AssociateCapacityWithTechnologiesRequestDto(capacity.getId(), capacity.getTechnologyIds());
    }
}
