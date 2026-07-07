package com.example.capacity.infrastructure.entrypoints.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
public class RoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> apiPrefixRouterWrapper(
            @Qualifier("capacityRouterFunction") RouterFunction<ServerResponse> capacityRouterFunction
    ) {
        return nest(
                path("/api"),
                capacityRouterFunction
        );
    }
}
