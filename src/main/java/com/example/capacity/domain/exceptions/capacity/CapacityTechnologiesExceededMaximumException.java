package com.example.capacity.domain.exceptions.capacity;

import com.example.capacity.domain.exceptions.BusinessRuleException;
import com.example.capacity.domain.model.Capacity;

public class CapacityTechnologiesExceededMaximumException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "A capacity can have a maximum of %d associated technologies.";

    public CapacityTechnologiesExceededMaximumException() {
        super(String.format(ERROR_MESSAGE, Capacity.MAX_LENGTH_TECHNOLOGIES_ASSOCIATED));
    }
}
