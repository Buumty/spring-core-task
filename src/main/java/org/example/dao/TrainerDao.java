package org.example.dao;

import org.example.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void save(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findById(long id);
    List<Trainer> findAll();
}
