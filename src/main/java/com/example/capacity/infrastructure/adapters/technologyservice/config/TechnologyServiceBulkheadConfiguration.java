package com.example.capacity.infrastructure.adapters.technologyservice.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TechnologyServiceBulkheadConfiguration {

    private final BulkheadRegistry bulkheadRegistry;

    @Bean
    public Bulkhead technologyServiceBulkhead() {
        return bulkheadRegistry.bulkhead("technologyServiceBulkhead");
    }
}
