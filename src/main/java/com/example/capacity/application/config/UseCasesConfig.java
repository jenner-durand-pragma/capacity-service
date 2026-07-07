package com.example.capacity.application.config;

import com.example.capacity.domain.api.ICapacityServicePort;
import com.example.capacity.domain.spi.ICapacityPersistencePort;
import com.example.capacity.domain.spi.ITechnologyExternalPort;
import com.example.capacity.domain.usecase.CapacityUseCase;
import com.example.capacity.infrastructure.adapters.persistenceadapter.CapacityPersistenceAdapter;
import com.example.capacity.infrastructure.adapters.persistenceadapter.mapper.ICapacityEntityMapper;
import com.example.capacity.infrastructure.adapters.persistenceadapter.repository.ICapacityEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

        private final ICapacityEntityRepository capacityEntityRepository;
        private final ICapacityEntityMapper capacityEntityMapper;
        private final ITechnologyExternalPort technologyExternalPort;

        @Bean
        public ICapacityPersistencePort capacityPersistencePort() {
                return new CapacityPersistenceAdapter(
                        capacityEntityRepository,
                        capacityEntityMapper
                );
        }

        @Bean
        public ICapacityServicePort capacityServicePort(
                ICapacityPersistencePort capacityPersistencePort
        ) {
                return new CapacityUseCase(capacityPersistencePort, technologyExternalPort);
        }
}
