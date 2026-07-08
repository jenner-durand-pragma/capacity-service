package com.example.capacity.application.config;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import com.example.capacity.domain.spi.ITechnologyExternalPort;
import com.example.capacity.domain.usecase.CapacityUseCase;
import com.example.capacity.infrastructure.adapters.persistenceadapter.CapacityPersistenceAdapter;
import com.example.capacity.infrastructure.adapters.persistenceadapter.mapper.ICapacityEntityMapper;
import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import com.example.capacity.infrastructure.adapters.technologyservice.TechnologyServiceConsumerAdapter;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

        private final ICapacityEntityRepository capacityEntityRepository;
        private final ICapacityEntityMapper capacityEntityMapper;

        @Bean
        public ITechnologyExternalPort technologyExternalPort(
                @Qualifier("technologyWebClient")
                WebClient webClient,

                @Qualifier("technologyServiceRetry")
                Retry retry,

                @Qualifier("technologyServiceBulkhead")
                Bulkhead bulkhead
        ) {
                return new TechnologyServiceConsumerAdapter(
                        webClient,
                        retry,
                        bulkhead
                );
        }

        @Bean
        public ICapacityPersistencePort capacityPersistencePort() {
                return new CapacityPersistenceAdapter(
                        capacityEntityRepository,
                        capacityEntityMapper
                );
        }

        @Bean
        public ICapacityServicePort capacityServicePort(
                ICapacityPersistencePort capacityPersistencePort,
                ITechnologyExternalPort technologyExternalPort
        ) {
                return new CapacityUseCase(capacityPersistencePort, technologyExternalPort);
        }
}
