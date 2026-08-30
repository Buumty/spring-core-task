package org.example.facade;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService
    ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String address
    ) {
        return traineeService.create(
                firstName,
                lastName,
                dateOfBirth,
                address
        );
    }

    public Trainee updateTrainee(
            long traineeId,
            String firstName,
            String lastName,
            String address,
            boolean active,
            String username,
            String password
    ) {
        return traineeService.update(
                firstName,
                lastName,
                address,
                traineeId,
                username,
                password
        );
    }

    public void deleteTrainee(long traineeId) {
        traineeService.deleteById(traineeId);
    }

    public Trainee findTraineeById(long traineeId) {
        return traineeService.findById(traineeId);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public Trainer createTrainer(
            String firstName,
            String lastName,
            TrainingTypeName specialization
    ) {
        return trainerService.create(
                firstName,
                lastName,
                specialization
        );
    }

    public Trainer updateTrainer(
            long trainerId,
            String firstName,
            String lastName,
            boolean active,
            TrainingTypeName specialization
    ) {
        return trainerService.update(
                firstName,
                lastName,
                active,
                specialization,
                trainerId
        );
    }

    public Trainer findTrainerById(long trainerId) {
        return trainerService.findById(trainerId);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(
            long traineeId,
            long trainerId,
            String trainingName,
            TrainingTypeName trainingType,
            LocalDate trainingDate,
            Duration trainingDuration
    ) {
        return trainingService.create(
                traineeId,
                trainerId,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        );
    }

    public Training findTrainingById(long trainingId) {
        return trainingService.findById(trainingId);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }
}