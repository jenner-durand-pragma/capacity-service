package com.example.capacity.infrastructure.adapters.technologyservice.exceptions;

public class TechnologyServiceUnavailableException extends RuntimeException {

    private static final String ERROR_MESSAGE = "The technology service is currently unavailable";

    public TechnologyServiceUnavailableException() {
        super(ERROR_MESSAGE);
    }
}
