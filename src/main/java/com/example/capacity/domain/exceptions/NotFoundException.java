package com.example.capacity.domain.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String resource;
    private final String resourceId;

    protected NotFoundException(String message, String resource, Object resourceId) {
        super(message);
        this.resource = resource;
        this.resourceId = String.valueOf(resourceId);
    }
}
