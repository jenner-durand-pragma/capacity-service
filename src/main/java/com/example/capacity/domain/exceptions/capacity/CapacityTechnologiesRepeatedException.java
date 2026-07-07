package com.example.capacity.domain.exceptions.capacity;

import com.example.capacity.domain.exceptions.BusinessRuleException;

public class CapacityTechnologiesRepeatedException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "Technologies cannot be repeated.";

    public CapacityTechnologiesRepeatedException() {
        super(ERROR_MESSAGE);
    }
}
