package com.example.capacity.domain.exceptions.capacity;

import com.example.capacity.domain.exceptions.ConflictException;

public class CapacityNameAlreadyExistsException extends ConflictException {

    private static final String ERROR_MESSAGE = "The name of the capacity already exists.";
    private static final String ERROR_FIELD = "name";

    public CapacityNameAlreadyExistsException() {
        super(ERROR_MESSAGE, ERROR_FIELD);
    }
}
