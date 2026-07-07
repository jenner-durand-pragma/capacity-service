package com.example.capacity.domain.model;

import com.example.capacity.domain.exceptions.capacity.CapacityFieldInvalidLengthException;
import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesBelowMinimumException;
import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesExceededMaximumException;
import com.example.capacity.domain.exceptions.capacity.CapacityTechnologiesRepeatedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Capacity {

    private Long id;
    private String name;
    private String description;
    private List<Long> technologyIds = new ArrayList<>();

    public static final Integer MAX_LENGTH_NAME = 50;
    public static final Integer MAX_LENGTH_DESCRIPTION = 90;

    public void checkNameLength() {
        if (name == null || name.isBlank() || name.length() > MAX_LENGTH_NAME) {
            throw CapacityFieldInvalidLengthException.name();
        }
    }

    public void checkDescriptionLength() {
        if (description == null || description.isBlank() || description.length() > MAX_LENGTH_DESCRIPTION) {
            throw CapacityFieldInvalidLengthException.description();
        }
    }
}
