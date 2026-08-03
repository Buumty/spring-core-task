package org.example.dao.impl;

import org.example.dao.TraineeDao;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTraineeDao implements TraineeDao {

    private final Map<Long, Trainee> traineeStorage;

    public InMemoryTraineeDao(@Qualifier("traineeStorage") Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }

    @Override
    public void deleteById(long id) {
        traineeStorage.remove(id);
    }

    @Override
    public Optional<Trainee> findById(long id) {
        return Optional.ofNullable(traineeStorage.get(id));
    }

    @Override
    public List<Trainee> findAll() {
        return List.copyOf(traineeStorage.values());
    }

    @Override
    public boolean existsByUsername(String username) {
        return traineeStorage.values()
                .stream()
                .anyMatch(trainee ->
                        trainee.getUsername().equals(username)
                );
    }
}
