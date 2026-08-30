package org.example.service;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class TrainingService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainingService.class);

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    public TrainingService(TrainingDao trainingDao, TraineeDao traineeDao, TrainerDao trainerDao) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    public Training findById(long id) {
        log.debug("Searching for training with id={}", id);
        return trainingDao.findById(id).orElseThrow(() -> {
            log.warn("Training with id={} was not found", id);
            return new NoSuchElementException("Training with id " + id + " was not found");
        });
    }

    public List<Training> findAll() {
        log.debug("Retrieving all trainings");
        return trainingDao.findAll();
    }

    @Transactional
    public Training create(long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        log.debug("Searching for trainee with id={}", traineeId);
        Trainee trainee = traineeDao.findById(traineeId).orElseThrow(() -> {
            log.warn("Trainee with id={} was not found", traineeId);
            return new NoSuchElementException("Trainee with id " + traineeId + " was not found");
        });

        log.debug("Searching for trainer with id={}", trainerId);
        Trainer trainer = trainerDao.findById(trainerId).orElseThrow(() -> {
            log.warn("Trainer with id={} was not found", trainerId);
            return new NoSuchElementException("Trainee with id " + trainerId + " was not found");
        });

        Training savedTraining = trainingDao.save(new Training(
                trainee,
                trainer,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        ));

        log.info(
                "Created training id={}, traineeId={}, trainerId={}, name={}",
                savedTraining.getTrainingId(),
                savedTraining.getTrainee().getUser().getUserId(),
                savedTraining.getTrainer().getUser().getUserId(),
                savedTraining.getTrainingName()
        );
        return savedTraining;
    }
}
