package org.example.dao;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingTypeName;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Training save(Training training);
    Optional<Training> findById(long id);
    List<Training> findAll();
    List<Training> findTraineeTrainings(
            String traineeUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            TrainingTypeName trainingType
    );
    List<Training> findTrainerTrainings(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    );
    List<Trainer> getUnassignedTrainers();
}
