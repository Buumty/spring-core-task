package org.example.dao.impl;

import org.example.dao.TrainerDao;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTrainerStorage implements TrainerDao {

    private final Map<Long, Trainer> trainerStorage;

    public InMemoryTrainerStorage(@Qualifier("trainerStorage") Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(long id) {
        return Optional.ofNullable(trainerStorage.get(id));
    }

    @Override
    public List<Trainer> findAll() {
        return List.copyOf(trainerStorage.values());
    }
}
