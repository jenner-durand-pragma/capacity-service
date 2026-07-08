package com.example.capacity.infrastructure.adapters.technologyservice.exceptions;

import com.example.capacity.domain.exceptions.NotFoundException;
import com.example.capacity.domain.model.Technology;

public class TechnologyNotFoundException extends NotFoundException {

    public TechnologyNotFoundException(String message) {
        super(message, Technology.class.getSimpleName());
    }
}
