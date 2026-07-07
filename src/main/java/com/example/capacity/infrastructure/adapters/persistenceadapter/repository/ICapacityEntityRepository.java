package com.example.capacity.infrastructure.adapters.persistenceadapter.repository;

import com.example.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ICapacityEntityRepository extends ReactiveCrudRepository<CapacityEntity, Long> {
    Mono<Boolean> existsByName(String name);
}
