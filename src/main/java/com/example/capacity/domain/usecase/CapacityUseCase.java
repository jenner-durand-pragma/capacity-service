package com.example.capacity.domain.usecase;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import com.example.capacity.domain.spi.ITechnologyExternalPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ITechnologyExternalPort technologyPersistencePort;

    @Override
    public Mono<Capacity> createCapacity(Capacity capacity) {
        return Mono.just(capacity)
                .doOnNext(Capacity::checkTechnologiesNotRepeated)
                .doOnNext(Capacity::checkMaxTechnologiesAssociated)
                .doOnNext(Capacity::checkMinTechnologiesAssociated)
                .flatMap(capacityPersistencePort::save)
                .map(c -> {
                    c.setTechnologyIds(capacity.getTechnologyIds());

                    return c;
                })
                .flatMap(c -> technologyPersistencePort
                        .assignTechnologiesToCapacity(c)
                        .thenReturn(c)
                );
    }
}
