package com.example.capacity.infrastructure.adapters.technologyservice;

import com.example.capacity.domain.model.Capacity;
import com.example.capacity.domain.spi.ITechnologyExternalPort;
import com.example.capacity.infrastructure.adapters.technologyservice.dto.common.ErrorResponseDTO;
import com.example.capacity.infrastructure.adapters.technologyservice.exceptions.TechnologyServiceBusinessException;
import com.example.capacity.infrastructure.adapters.technologyservice.exceptions.TechnologyServiceUnavailableException;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class TechnologyServiceConsumerAdapter implements ITechnologyExternalPort {

    @Qualifier("technologyWebClient")
    private final WebClient webClient;

    @Qualifier("technologyServiceRetry")
    private final Retry retry;

    @Qualifier("technologyServiceBulkhead")
    private final Bulkhead bulkhead;

    @Override
    @CircuitBreaker(name = "technologyService", fallbackMethod = "fallbackTechnologyServiceAssignTechnologiesToCapacity")
    public Mono<Void> assignTechnologiesToCapacity(Capacity capacity) {
        return webClient.post()
                .uri("/api/capacities/technologies")
                .bodyValue(capacity)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(ErrorResponseDTO.class)
                                .flatMap(body -> Mono.error(
                                        new TechnologyServiceBusinessException(body.message())
                                ))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(ErrorResponseDTO.class)
                                .doOnNext(body -> log.error("Server error in technology-service. status={}, body={}",
                                        response.statusCode(), body))
                                .then(Mono.error(new TechnologyServiceUnavailableException()))
                )
                .bodyToMono(Void.class)
                .transformDeferred(BulkheadOperator.of(bulkhead))
                .transformDeferred(RetryOperator.of(retry));
    }

    public Mono<Void> fallbackTechnologyServiceAssignTechnologiesToCapacity(Capacity capacity, Throwable t) {
        log.error(
                "Circuit breaker triggered for technology-service. capacityId={}, error={}",
                capacity.getId(), t.getMessage(), t
        );

        return Mono.error(t);
    }
}
