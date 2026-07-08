package com.example.capacity.infrastructure.entrypoints.presentation.capacity.handlers;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CreateCapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.common.ErrorResponseDTO;
import com.example.capacity.infrastructure.entrypoints.exception.common.BodyRequiredException;
import com.example.capacity.infrastructure.entrypoints.handler.IRouteHandler;
import com.example.capacity.infrastructure.entrypoints.mapper.ICapacityDtoMapper;
import com.example.capacity.infrastructure.entrypoints.validation.dto.IDtoValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCapacityHandler implements IRouteHandler {

    private final ICapacityServicePort capacityServicePort;
    private final ICapacityDtoMapper capacityDtoMapper;
    private final IDtoValidator dtoValidator;

    @Override
    @Operation(
            tags = {"Capacity API"},
            summary = "Create a capacity",
            description = "Allow register a capacity",
            requestBody = @RequestBody(
                    description = "Capacity data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCapacityDto.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "Capacity registered successfully",
            content = @Content(schema = @Schema(implementation = CapacityDto.class)))
    @ApiResponse(responseCode = "404", description = "Technology not found",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "422", description = "Business rule error or validation error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request
                .bodyToMono(CreateCapacityDto.class)
                .switchIfEmpty(Mono.error(new BodyRequiredException()))
                .flatMap(dtoValidator::validate)
                .map(capacityDtoMapper::fromCreateToModel)
                .flatMap(capacityServicePort::createCapacity)
                .map(capacityDtoMapper::fromModelToResponse)
                .flatMap(capacityDto -> ServerResponse.ok().bodyValue(capacityDto));
    }
}
