package com.example.capacity.domain.model;

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
    public static final Integer MAX_LENGTH_TECHNOLOGIES_ASSOCIATED = 20;
    public static final Integer MIN_LENGTH_TECHNOLOGIES_ASSOCIATED = 3;

    public void checkTechnologiesNotRepeated() {
        var uniqueTechnologyIds = new HashSet<>(technologyIds);

        if (uniqueTechnologyIds.size() != technologyIds.size()) {
            throw new CapacityTechnologiesRepeatedException();
        }
    }

    public void checkMinTechnologiesAssociated() {
        var technologyAssociatedCount = technologyIds.size();

        if (technologyAssociatedCount < MIN_LENGTH_TECHNOLOGIES_ASSOCIATED) {
            throw new CapacityTechnologiesBelowMinimumException();
        }
    }

    public void checkMaxTechnologiesAssociated() {
        var technologyAssociatedCount = technologyIds.size();

        if (technologyAssociatedCount > MAX_LENGTH_TECHNOLOGIES_ASSOCIATED) {
            throw new CapacityTechnologiesExceededMaximumException();
        }
    }
}
