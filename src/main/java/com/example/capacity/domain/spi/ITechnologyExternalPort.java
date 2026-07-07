package com.example.capacity.domain.spi;

import com.example.capacity.domain.model.Capacity;
import reactor.core.publisher.Mono;

public interface ITechnologyExternalPort {
    Mono<Void> assignTechnologiesToCapacity(Capacity capacity);
}
