package com.example.capacity.infrastructure.entrypoints.presentation.capacity.integrations;

import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import com.example.capacity.infrastructure.entrypoints.dto.common.ErrorResponseDTO;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CreateCapacityDto;
import com.example.capacity.infrastructure.entrypoints.dto.capacity.CapacityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CreateCapacityIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ICapacityEntityRepository capacityEntityRepository;

    @BeforeEach
    void setUp() {
        capacityEntityRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should return a capacity saved successfully")
    void shouldReturnCapacitySavedSuccessfully_EndToEnd(){
        var requestDto = new CreateCapacityDto(
                "Spring Boot",
                "It's a Java framework"
        );

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
                "Spring boot text to validate characters limit here.",
                "This is a long description because I want to throw a validation exception, so " +
                        "I hope this work fine at the first time"
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
