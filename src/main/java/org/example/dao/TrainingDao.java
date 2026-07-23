package org.example.dao;

import org.example.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    void save(Training training);
    Optional<Training> findById(long id);
    List<Training> findAll();
}
