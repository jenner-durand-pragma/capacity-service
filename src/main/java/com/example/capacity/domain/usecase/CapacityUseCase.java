package com.example.capacity.domain.usecase;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.domain.exceptions.capacity.CapacityNameAlreadyExistsException;
import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;

    @Override
    public Mono<Capacity> createCapacity(Capacity capacity) {
        return Mono.just(capacity)
                .doOnNext(Capacity::checkNameLength)
                .doOnNext(Capacity::checkDescriptionLength)
                .flatMap(this::validateNameUniqueness)
                .flatMap(capacityPersistencePort::save);
    }

    private Mono<Capacity> validateNameUniqueness(Capacity capacity) {
        return capacityPersistencePort.existsByName(capacity.getName())
                .flatMap(exists -> {
                    if(Boolean.TRUE.equals(exists)) {
                        return Mono.error(new CapacityNameAlreadyExistsException());
                    }

                    return Mono.just(capacity);
                });
    }
}
