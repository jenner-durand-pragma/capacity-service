package com.example.capacity.domain.usecase;

import com.example.capacity.domain.exceptions.capacity.CapacityFieldInvalidLengthException;
import com.example.capacity.domain.exceptions.capacity.CapacityNameAlreadyExistsException;
import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private ICapacityPersistencePort capacityPersistencePort;

    @InjectMocks
    private CapacityUseCase capacityUseCase;

    private Capacity capacity;

    @BeforeEach
    void setUp() {
        capacity = Capacity.builder()
                .id(1L)
                .name("Spring Boot")
                .description("It's a Java framework")
                .build();
    }

    @Test
    @DisplayName(
            "Should return a CapacityFieldInvalidLengthException when " +
            "name have more characters than MAX_LENGTH_NAME"
    )
    void shouldReturnExceptionWhenNameHaveMoreCharactersThanMaxLengthName_CreateCapacity() {
        capacity.setName("Spring boot text to validate characters limit here.");

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityFieldInvalidLengthException.class)
                .verify();

        verify(capacityPersistencePort, never()).existsByName(any(String.class));
    }

    @Test
    @DisplayName(
            "Should return a CapacityFieldInvalidLengthException when " +
            "description have more characters than MAX_LENGTH_DESCRIPTION"
    )
    void shouldReturnExceptionWhenNameHaveMoreCharactersThanMaxLengthDescription_CreateCapacity() {
        capacity.setDescription(
                "This is a long description because I want to throw a validation exception, so " +
                "I hope this work fine at the first time"
        );

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityFieldInvalidLengthException.class)
                .verify();

        verify(capacityPersistencePort, never()).existsByName(any(String.class));
    }

    @Test
    @DisplayName("Should return a CapacityNameAlreadyExistsException when name already exists")
    void shouldReturnExceptionWhenNameAlreadyExists_CreateCapacity() {
        when(capacityPersistencePort.existsByName(any(String.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.createCapacity(capacity))
                .expectError(CapacityNameAlreadyExistsException.class)
                .verify();

        verify(capacityPersistencePort, never()).save(any(Capacity.class));
    }

    @Test
    @DisplayName("Should return capacity domain model when it saved successfully")
    void shouldReturnCapacityWhenItSavedSuccessfully_CreateCapacity() {
        var capacityId = 1L;
        capacity.setId(null);

        when(capacityPersistencePort.existsByName(any(String.class)))
                .thenReturn(Mono.just(false));
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

        verify(capacityPersistencePort).existsByName(any(String.class));
        verify(capacityPersistencePort).save(any(Capacity.class));
    }
}
