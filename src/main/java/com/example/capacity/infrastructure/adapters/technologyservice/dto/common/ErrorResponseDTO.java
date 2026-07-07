package com.example.capacity.infrastructure.adapters.technologyservice.dto.common;

import java.util.Date;

public record ErrorResponseDTO(
        Date timestamp,
        String path,
        int status,
        String error,
        String requestId,
        String message
) {
}
