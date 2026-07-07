package com.example.capacity.domain.exceptions.capacity;

import com.example.capacity.domain.exceptions.BusinessRuleException;

import static com.example.capacity.domain.model.Capacity.MAX_LENGTH_DESCRIPTION;
import static com.example.capacity.domain.model.Capacity.MAX_LENGTH_NAME;

public class CapacityFieldInvalidLengthException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "The attribute %s cannot have more characters than %d.";

    public CapacityFieldInvalidLengthException(String field, Integer length) {
        super(String.format(ERROR_MESSAGE, field, length));
    }

    public static CapacityFieldInvalidLengthException name() {
        return new CapacityFieldInvalidLengthException("name", MAX_LENGTH_NAME);
    }

    public static CapacityFieldInvalidLengthException description() {
        return new CapacityFieldInvalidLengthException("description", MAX_LENGTH_DESCRIPTION);
    }
}
