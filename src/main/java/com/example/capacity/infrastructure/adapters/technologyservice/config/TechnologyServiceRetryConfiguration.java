package com.example.capacity.infrastructure.adapters.technologyservice.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TechnologyServiceRetryConfiguration {

    private final RetryRegistry retryRegistry;

    @Bean
    public Retry technologyServiceRetry() {
        return retryRegistry.retry("technologyServiceRetry");
    }
}
