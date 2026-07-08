package com.example.capacity.infrastructure.entrypoints.presentation.capacity.integrations;

import com.example.capacity.domain.spi.ITechnologyExternalPort;
import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CreateCapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.common.ErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CreateCapacityIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ICapacityEntityRepository capacityEntityRepository;

    @MockBean
    private ITechnologyExternalPort technologyExternalPort;

    @BeforeEach
    void setUp() {
        capacityEntityRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return a capacity saved successfully")
    void shouldReturnCapacitySavedSuccessfully_EndToEnd(){
        var requestDto = new CreateCapacityDto(
                "Backend Developer",
                "It's a good option",
                List.of(1L, 2L, 3L)
        );

        when(technologyExternalPort.assignTechnologiesToCapacity(any()))
                .thenReturn(Mono.empty());

        var capacityDto = webTestClient.post()
                .uri("/api/capacities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CapacityDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(capacityDto).isNotNull();
        assertThat(capacityDto.id()).isNotNull();
        assertThat(capacityDto.name()).isEqualTo(requestDto.name());
        assertThat(capacityDto.description()).isEqualTo(requestDto.description());
        assertThat(capacityDto.technologyIds()).hasSameSizeAs(requestDto.technologyIds());

        StepVerifier.create(capacityEntityRepository.findById(capacityDto.id()))
                .assertNext(capacity -> {
                    assertThat(capacity).isNotNull();
                    assertThat(capacity.getId()).isEqualTo(capacityDto.id());
                    assertThat(capacity.getName()).isEqualTo(capacityDto.name());
                    assertThat(capacity.getDescription()).isEqualTo(capacityDto.description());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return a validation error")
    void shouldReturnValidationError_EndToEnd() {
        var requestDto = new CreateCapacityDto(
                "Backend Developer",
                "It's a good option",
                List.of()
        );

        var errorResponseDTO = webTestClient.post()
                .uri("/api/capacities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.status()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
