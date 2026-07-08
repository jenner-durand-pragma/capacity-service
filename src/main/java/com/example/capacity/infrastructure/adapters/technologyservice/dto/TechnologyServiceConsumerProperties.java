package com.example.capacity.infrastructure.adapters.technologyservice.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("technology-service")
public record TechnologyServiceConsumerProperties(
        String baseUrl,
        Integer timeout
) {}
