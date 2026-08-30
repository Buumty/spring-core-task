package org.example.facade;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.stereotype.Component;

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

    public void deleteTrainee(
            long traineeId,
            String username,
            String password
    ) {
        traineeService.deleteById(
                traineeId,
                username,
                password
        );
    }

    public Trainee findTraineeById(long traineeId) {
        return traineeService.findById(traineeId);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public void changeTraineePassword(
            String username,
            String oldPassword,
            String newPassword
    ) {
        traineeService.changePassword(
                username,
                oldPassword,
                newPassword
        );
    }

    public void activateTrainee(
            String username,
            String password
    ) {
        traineeService.activate(
                username,
                password
        );
    }

    public void deactivateTrainee(
            String username,
            String password
    ) {
        traineeService.deactivate(
                username,
                password
        );
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
            TrainingTypeName specialization,
            String username,
            String password
    ) {
        return trainerService.update(
                firstName,
                lastName,
                specialization,
                trainerId,
                username,
                password
        );
    }

    public Trainer findTrainerById(long trainerId) {
        return trainerService.findById(trainerId);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public void changeTrainerPassword(
            String username,
            String oldPassword,
            String newPassword
    ) {
        trainerService.changePassword(
                username,
                oldPassword,
                newPassword
        );
    }

    public void activateTrainer(
            String username,
            String password
    ) {
        trainerService.activate(
                username,
                password
        );
    }

    public void deactivateTrainer(
            String username,
            String password
    ) {
        trainerService.deactivate(
                username,
                password
        );
    }

    public Training createTraining(
            long traineeId,
            long trainerId,
            String trainingName,
            TrainingType trainingType,
            LocalDate trainingDate,
            Integer trainingDuration
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