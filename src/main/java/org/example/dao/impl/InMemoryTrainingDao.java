package org.example.dao.impl;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTrainingDao implements TrainingDao {
    private final Map<Long,Training> trainingStorage;

    public InMemoryTrainingDao(@Qualifier("trainingStorage") Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training save(Training training) {
        trainingStorage.put(training.getTrainingId(), training);
        return training;
    }

    @Override
    public Optional<Training> findById(long id) {
        return Optional.ofNullable(trainingStorage.get(id));
    }

    @Override
    public List<Training> findAll() {
        return List.copyOf(trainingStorage.values());
    }
}
