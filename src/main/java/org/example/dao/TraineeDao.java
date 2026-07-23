package org.example.dao;

import org.example.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void save(Trainee trainee);
    void update(Trainee trainee);
    void deleteById(long id);
    Optional<Trainee> findById(int id);
    List<Trainee> findAll();
}
