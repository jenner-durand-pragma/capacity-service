package com.example.capacity.infrastructure.entrypoints.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IRouteHandler {
    Mono<ServerResponse> handle(ServerRequest request);
}
