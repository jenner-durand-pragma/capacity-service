package com.example.capacity.infrastructure.adapters.persistenceadapter.repository;

import com.example.capacity.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ICapacityEntityRepository extends ReactiveCrudRepository<CapacityEntity, Long> {

}
