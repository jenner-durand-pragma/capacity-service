package com.example.capacity.infrastructure.entrypoints.exception.common;

import com.example.capacity.domain.exceptions.BusinessRuleException;

public class BodyRequiredException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "Body is required.";

    public BodyRequiredException() {
        super(ERROR_MESSAGE);
    }
}
