package com.example.capacity.infrastructure.adapters.technologyservice;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.infrastructure.adapters.technologyservice.exceptions.TechnologyServiceBusinessException;
import com.example.capacity.infrastructure.adapters.technologyservice.exceptions.TechnologyServiceUnavailableException;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class TechnologyServiceConsumerAdapterTest {

    private MockWebServer mockWebServer;
    private TechnologyServiceConsumerAdapter adapter;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        var retryConfig = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(10))
                .ignoreExceptions(TechnologyServiceBusinessException.class)
                .build();

        var bulkheadConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(10))
                .build();

        adapter = new TechnologyServiceConsumerAdapter(
                webClient,
                Retry.of("technologyServiceRetry", retryConfig),
                Bulkhead.of("technologyServiceBulkhead", bulkheadConfig)
        );
    }

    @AfterEach
    void shutDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Should complete successfully when technology-service returns 204")
    void shouldCompleteSuccessfullyWhenNoContent_AssignTechnologiesToCapacity() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(204));

        var capacity = Capacity.builder().id(1L).build();

        StepVerifier.create(adapter.assignTechnologiesToCapacity(capacity))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw TechnologyServiceBusinessException when technology-service returns 4xx")
    void shouldThrowBusinessExceptionWhenClientError_AssignTechnologiesToCapacity() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(422)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        {
                            "timestamp": "2026-07-07T00:00:00.000Z",
                            "path": "/api/capacities/technologies",
                            "status": 422,
                            "error": "Unprocessable Entity",
                            "requestId": "abc-123",
                            "message": "Technology ids not found: 1, 2"
                        }
                        """));

        var capacity = Capacity.builder().id(1L).build();

        StepVerifier.create(adapter.assignTechnologiesToCapacity(capacity))
                .expectErrorSatisfies(error -> {
                    assertThat(error)
                            .isInstanceOf(TechnologyServiceBusinessException.class)
                            .hasMessage("Technology ids not found: 1, 2");
                })
                .verify();
    }

    @Test
    @DisplayName("Should throw TechnologyServiceUnavailableException when technology-service returns 5xx")
    void shouldThrowUnavailableExceptionWhenServerError_AssignTechnologiesToCapacity() {
        mockWebServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) {
                return new MockResponse().setResponseCode(500)
                        .setHeader("Content-Type", "application/json")
                        .setBody("""
                            {
                                "timestamp":"2026-07-07T00:00:00.000Z",
                                "path":"/api/capacities/technologies",
                                "status":500,"error":"Internal Server Error",
                                "requestId":"abc-123",
                                "message":"DB down"
                            }
                            """);
            }
        });

        var capacity = Capacity.builder().id(1L).build();

        StepVerifier.create(adapter.assignTechnologiesToCapacity(capacity))
                .expectError(TechnologyServiceUnavailableException.class)
                .verify();
    }

    @Test
    @DisplayName("Fallback should log and propagate the original error")
    void shouldPropagateErrorWhenFallbackInvoked_AssignTechnologiesToCapacity() {
        var capacity = Capacity.builder().id(1L).build();
        var originalError = new TechnologyServiceUnavailableException();

        StepVerifier.create(adapter.fallbackTechnologyServiceAssignTechnologiesToCapacity(capacity, originalError))
                .expectErrorMatches(error -> error == originalError)
                .verify();
    }
}
