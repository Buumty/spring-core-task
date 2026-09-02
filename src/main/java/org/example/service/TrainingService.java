package org.example.service;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dao.TrainingTypeDao;
import org.example.model.*;
import org.example.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TrainingTypeDao trainingTypeDao;
    private final AuthenticationService authenticationService;

    public TrainingService(TrainingDao trainingDao, TraineeDao traineeDao, TrainerDao trainerDao, TrainingTypeDao trainingTypeDao, AuthenticationService authenticationService) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingTypeDao = trainingTypeDao;
        this.authenticationService = authenticationService;
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

    public List<Training> getTraineeTrainings(
            String authUsername,
            String authPassword,
            String traineeUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            TrainingTypeName trainingType
    ) {
        authenticationService.requireAuthentication(
                authUsername,
                authPassword
        );

        if (fromDate != null
                && toDate != null
                && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        return trainingDao.findTraineeTrainings(
                traineeUsername,
                fromDate,
                toDate,
                trainerName,
                trainingType
        );
    }
    public List<Training> getTrainerTrainings(
            String authUsername,
            String authPassword,
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        authenticationService.requireAuthentication(
                authUsername,
                authPassword
        );

        if (fromDate != null
                && toDate != null
                && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        return trainingDao.findTrainerTrainings(
                trainerUsername,
                fromDate,
                toDate,
                traineeName
        );
    }

    @Transactional
    public Training create(
            String authUsername,
            String authPassword,
            String traineeUsername,
            String trainerUsername,
            String trainingName,
            TrainingTypeName trainingTypeName,
            LocalDate trainingDate,
            Integer trainingDuration
    ) {
        authenticationService.requireAuthentication(
                authUsername,
                authPassword
        );

        Trainee trainee = traineeDao
                .findByUsername(traineeUsername)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Trainee not found"
                        )
                );

        Trainer trainer = trainerDao
                .findByUsername(trainerUsername)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Trainer not found"
                        )
                );

        TrainingType trainingType = trainingTypeDao
                .findByName(trainingTypeName)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Training type not found"
                        )
                );

        Training training = new Training(
                trainee,
                trainer,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        );

        return trainingDao.save(training);
    }
}
