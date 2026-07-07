package com.example.capacity.infrastructure.entrypoints.presentation.capacity;

import com.example.capacity.infrastructure.entrypoints.presentation.capacity.handlers.CreateCapacityHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CapacityRouter {

    @Bean("capacityRouterFunction")
    @RouterOperations({
            @RouterOperation(
                    path = "/api/capacities",
                    beanClass = CreateCapacityHandler.class,
                    beanMethod = "handle",
                    method = RequestMethod.POST
            )
    })
    public RouterFunction<ServerResponse> routerFunction(
        CreateCapacityHandler createCapacityHandler
    ) {
        return RouterFunctions
                .route()
                .path(
                        "/capacities",
                        builder -> builder
                                .POST("", createCapacityHandler::handle)
                                .build()
                )
                .build();
    }
}
