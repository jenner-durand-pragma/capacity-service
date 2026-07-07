package com.example.capacity.domain.exceptions.capacity;

import com.example.capacity.domain.exceptions.BusinessRuleException;
import com.example.capacity.domain.model.Capacity;

public class CapacityTechnologiesBelowMinimumException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "A capacity must have at least %d associated technologies.";

    public CapacityTechnologiesBelowMinimumException() {
        super(String.format(ERROR_MESSAGE, Capacity.MIN_LENGTH_TECHNOLOGIES_ASSOCIATED));
    }
}
