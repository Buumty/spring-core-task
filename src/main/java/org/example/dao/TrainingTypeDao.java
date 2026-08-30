package org.example.dao;

import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;

import java.util.Optional;

public interface TrainingTypeDao {

    Optional<TrainingType> findByName(
            TrainingTypeName name
    );
}