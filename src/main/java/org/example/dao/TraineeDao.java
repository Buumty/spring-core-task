package org.example.dao;

import org.example.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee save(Trainee trainee);
    Trainee update(Trainee trainee);
    void deleteById(long id);
    Optional<Trainee> findById(long id);
    List<Trainee> findAll();
    boolean existsByUsername(String username);
}
