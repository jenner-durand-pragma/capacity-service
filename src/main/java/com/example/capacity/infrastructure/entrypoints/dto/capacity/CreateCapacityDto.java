package com.example.capacity.infrastructure.entrypoints.dto.capacity;

import com.example.capacity.domain.model.Capacity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Necessary fields to create a new capacity")
public record CreateCapacityDto(
        @Schema(description = "Name of the capacity", example = "Spring boot")
        @NotBlank(message = "Name must have value")
        @Size(
                max = 50,
                message = "Name must be lower or equals than {max} characters"
        )
        String name,

        @Schema(description = "Description of the capacity", example = "It's a Java framework")
        @NotBlank(message = "Description must have value")
        @Size(
                max = 90,
                message = "Description must be lower or equals than {max} characters"
        )
        String description
) { }
