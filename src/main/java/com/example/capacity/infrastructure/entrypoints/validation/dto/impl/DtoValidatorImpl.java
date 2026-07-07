package com.example.capacity.infrastructure.entrypoints.validation.dto.impl;

import com.example.capacity.infrastructure.entrypoints.validation.dto.IDtoValidator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DtoValidatorImpl implements IDtoValidator {

    private final Validator validator;

    @Override
    public <T> Mono<T> validate(T dto) {
        var violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            return Mono.error(new ConstraintViolationException(violations));
        }

        return Mono.just(dto);
    }
}
