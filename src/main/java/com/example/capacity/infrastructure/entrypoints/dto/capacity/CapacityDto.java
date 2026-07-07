package com.example.capacity.infrastructure.entrypoints.dto.capacity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Capacity DTO")
public record CapacityDto(
        @Schema(description = "ID of the capacity", example = "10")
        Long id,

        @Schema(description = "Name of the capacity", example = "Spring boot")
        String name,

        @Schema(description = "Description of the capacity", example = "It's a Java framework")
        String description
) { }
