package com.example.capacity.domain.usecase;

import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesBelowMinimumException;
import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesExceededMaximumException;
import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesRepeatedException;
import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import com.example.capacity.domain.spi.ITechnologyExternalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private ICapacityPersistencePort capacityPersistencePort;

    @Mock
    private ITechnologyExternalPort technologyExternalPort;

    @InjectMocks
    private CapacityUseCase capacityUseCase;

    private Capacity capacity;

    @BeforeEach
    void setUp() {
        capacity = Capacity.builder()
                .id(1L)
                .name("Backend Developer")
                .description("It's a new capacity")
                .build();
    }

    @Test
    @DisplayName(
            "Should return a CapacityTechnologiesExceededMaximumException when " +
            "technology ids have more size than MAX_LENGTH_TECHNOLOGIES_ASSOCIATED"
    )
    void shouldReturnExceptionWhenTechnologyIdsHaveMoreSizeThanMaxTechnologiesAssociated_CreateCapacity() {
        var technologyIds = List.of(
                1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L
        );
        capacity.setTechnologyIds(technologyIds);

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityTechnologiesExceededMaximumException.class)
                .verify();

        verify(capacityPersistencePort, never()).save(any(Capacity.class));
        verify(technologyExternalPort, never()).assignTechnologiesToCapacity(any(Capacity.class));
    }

    @Test
    @DisplayName(
            "Should return a CapacityTechnologiesBelowMinimumException when " +
            "technology ids have more size than MIN_LENGTH_TECHNOLOGIES_ASSOCIATED"
    )
    void shouldReturnExceptionWhenNameHaveMoreCharactersThanMaxLengthDescription_CreateCapacity() {
        var technologyIds = List.of(1L, 2L);
        capacity.setTechnologyIds(technologyIds);

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityTechnologiesBelowMinimumException.class)
                .verify();

        verify(capacityPersistencePort, never()).save(any(Capacity.class));
        verify(technologyExternalPort, never()).assignTechnologiesToCapacity(any(Capacity.class));
    }

    @Test
    @DisplayName("Should return a CapacityTechnologiesRepeatedException when technology id is repeated")
    void shouldReturnExceptionWhenTechnologyIdRepeated_CreateCapacity() {
        var technologyIds = List.of(1L, 2L, 3L, 1L);
        capacity.setTechnologyIds(technologyIds);

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityTechnologiesRepeatedException.class)
                .verify();

        verify(capacityPersistencePort, never()).save(any(Capacity.class));
        verify(technologyExternalPort, never()).assignTechnologiesToCapacity(any(Capacity.class));
    }

    @Test
    @DisplayName("Should return capacity domain model when it saved successfully")
    void shouldReturnCapacityWhenItSavedSuccessfully_CreateCapacity() {
        var capacityId = 1L;
        var technologyIds = List.of(1L, 2L, 3L);

        capacity.setId(null);
        capacity.setTechnologyIds(technologyIds);

        when(technologyExternalPort.assignTechnologiesToCapacity(any(Capacity.class)))
                .thenReturn(Mono.empty());
        when(capacityPersistencePort.save(any(Capacity.class)))
                .thenAnswer(invocation -> {
                    Capacity capacityInvocation = invocation.getArgument(0);
                    capacityInvocation.setId(capacityId);

                    return Mono.just(capacityInvocation);
                });

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectNextMatches(capacityResult ->
                        capacityResult.getId() != null && capacityResult.getId().equals(capacityId))
                .verifyComplete();

        verify(capacityPersistencePort).save(any(Capacity.class));
        verify(technologyExternalPort).assignTechnologiesToCapacity(any(Capacity.class));
    }
}
