package org.example.dao;

import org.example.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer save(Trainer trainer);
    Trainer update(Trainer trainer);
    Optional<Trainer> findById(long id);
    List<Trainer> findAll();
    boolean existsByUsername(String username);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findNotAssignedToTrainee(String traineeUsername);
}
