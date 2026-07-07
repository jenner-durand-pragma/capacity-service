package com.example.capacity.infrastructure.entrypoints.validation.dto;

import reactor.core.publisher.Mono;

public interface IDtoValidator {
    <T> Mono<T> validate(T dto);
}
