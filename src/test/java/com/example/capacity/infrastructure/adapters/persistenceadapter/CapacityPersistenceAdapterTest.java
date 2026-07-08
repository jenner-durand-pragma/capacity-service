package com.example.capacity.infrastructure.adapters.persistenceadapter;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.infrastructure.adapters.persistenceadapter.mapper.ICapacityEntityMapper;
import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class CapacityPersistenceAdapterTest {

    @Autowired
    private ICapacityEntityRepository capacityEntityRepository;

    @Spy
    private ICapacityEntityMapper capacityEntityMapper = Mappers.getMapper(ICapacityEntityMapper.class);

    private CapacityPersistenceAdapter capacityPersistenceAdapter;

    private Capacity capacity;

    @BeforeEach
    void setUp() {
        capacityPersistenceAdapter = new CapacityPersistenceAdapter(
                capacityEntityRepository,
                capacityEntityMapper
        );

        capacity = Capacity.builder()
                .name("Backend developer")
                .description("It's a good capacity")
                .build();

        capacityEntityRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return capacity saved successfully")
    void shouldReturnCapacitySavedSuccessfully_Save() {
        StepVerifier.create(capacityPersistenceAdapter.save(capacity))
                .expectNextMatches(capacityResult ->
                        capacityResult.getId() != null
                                && capacityResult.getName().equals(capacity.getName()))
                .verifyComplete();
    }
}
