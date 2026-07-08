package com.example.capacity.infrastructure.entrypoints.presentation.capacity.handlers;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.domain.model.Capacity;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CreateCapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CapacityDto;
import com.example.capacity.infrastructure.entrypoints.mapper.ICapacityDtoMapper;
import com.example.capacity.infrastructure.entrypoints.validation.dto.IDtoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCapacityHandlerTest {

    @Mock
    private ICapacityServicePort capacityServicePort;

    @Spy
    private ICapacityDtoMapper capacityDtoMapper = Mappers.getMapper(ICapacityDtoMapper.class);

    @Mock
    private IDtoValidator dtoValidator;

    private CreateCapacityHandler createCapacityHandler;

    private CreateCapacityDto createCapacityDto;

    @BeforeEach
    void setUp() {
        createCapacityHandler = new CreateCapacityHandler(
                capacityServicePort,
                capacityDtoMapper,
                dtoValidator
        );

        createCapacityDto = new CreateCapacityDto(
                "Backend developer",
                "It's a good capacity",
                List.of(1L, 2L, 3L)
        );
    }

    @Test
    @DisplayName("Should return capacity dto mapped successfully")
    void shouldReturnCapacityDtoMappedSuccessfully_Handle() {
        var capacityId = 1L;
        var capacity = Capacity.builder()
                .name(createCapacityDto.name())
                .description(createCapacityDto.description())
                .technologyIds(List.of(1L, 2L, 3L))
                .build();

        when(dtoValidator.validate(any(CreateCapacityDto.class)))
                .thenReturn(Mono.just(createCapacityDto));

        when(capacityServicePort.createCapacity(any(Capacity.class)))
                .thenAnswer(invocation -> {
                    Capacity capacityAnswer = invocation.getArgument(0);
                    capacityAnswer.setId(capacityId);

                    return Mono.just(capacityAnswer);
                });

        var request = MockServerRequest.builder()
                .body(Mono.just(createCapacityDto));

        StepVerifier.create(createCapacityHandler.handle(request))
                .assertNext(response -> {
                    assertThat(response.statusCode().value()).isEqualTo(200);

                    @SuppressWarnings("unchecked")
                    var entityResponse = (EntityResponse<CapacityDto>) response;
                    var capacityDto = entityResponse.entity();

                    assertThat(capacityDto)
                            .isNotNull()
                            .extracting(CapacityDto::id, CapacityDto::name, CapacityDto::description, dto -> dto.technologyIds().size())
                            .containsExactly(capacityId, capacity.getName(), capacity.getDescription(), capacity.getTechnologyIds().size());
                })
                .verifyComplete();

        var capacityCaptor = ArgumentCaptor.forClass(Capacity.class);
        verify(capacityServicePort).createCapacity(capacityCaptor.capture());

        var capacityCaptured = capacityCaptor.getValue();

        assertThat(capacityCaptured)
                .isNotNull()
                .extracting(Capacity::getId, Capacity::getName, Capacity::getDescription, capacityInput -> capacityInput.getTechnologyIds().size())
                .containsExactly(capacityId, capacity.getName(), capacity.getDescription(), capacity.getTechnologyIds().size());

    }
}
