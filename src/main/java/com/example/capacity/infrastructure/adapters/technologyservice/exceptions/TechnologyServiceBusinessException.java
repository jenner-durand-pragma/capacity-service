package com.example.capacity.infrastructure.adapters.technologyservice.exceptions;

import com.example.capacity.domain.exceptions.BusinessRuleException;

public class TechnologyServiceBusinessException extends BusinessRuleException {

    private static final String DEFAULT_ERROR_MESSAGE = "Business error in technology-service";

    public TechnologyServiceBusinessException(String message) {
        super(message != null ? message : DEFAULT_ERROR_MESSAGE);
    }
}
