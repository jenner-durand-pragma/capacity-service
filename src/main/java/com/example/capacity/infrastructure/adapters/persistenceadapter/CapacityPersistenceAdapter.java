package com.example.capacity.infrastructure.adapters.persistenceadapter;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import com.example.capacity.infrastructure.adapters.persistenceadapter.mapper.ICapacityEntityMapper;
import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements ICapacityPersistencePort {

    private final ICapacityEntityRepository capacityEntityRepository;
    private final ICapacityEntityMapper capacityEntityMapper;

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        return Mono.just(capacity)
                .map(capacityEntityMapper::toEntity)
                .flatMap(capacityEntityRepository::save)
                .map(capacityEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return Mono.just(name)
                .flatMap(capacityEntityRepository::existsByName);
    }
}
