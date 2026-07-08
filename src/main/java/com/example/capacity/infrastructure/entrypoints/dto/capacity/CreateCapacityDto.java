package com.example.capacity.infrastructure.entrypoints.dto.capacity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Necessary fields to create a new capacity")
public record CreateCapacityDto(
        @Schema(description = "Name of the capacity", example = "Spring boot")
        @NotBlank(message = "Name must have value")
        String name,

        @Schema(description = "Description of the capacity", example = "It's a Java framework")
        @NotBlank(message = "Description must have value")
        String description,

        @Schema(description = "Ids of technologies to associate")
        @NotEmpty(message = "Technology ids cannot be empty")
        List<Long> technologyIds
) { }
