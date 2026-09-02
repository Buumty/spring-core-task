package org.example.facade;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    public Trainee findTraineeByUsername(
            String username,
            String password
    ) {
        return traineeService.findByUsername(
                username,
                password
        );
    }

    public Trainee findTraineeById(
            long id,
            String username,
            String password
    ) {
        return traineeService.findById(
                id,
                username,
                password
        );
    }

    public List<Trainee> findAllTrainees(
            String username,
            String password
    ) {
        return traineeService.findAll(
                username,
                password
        );
    }

    public Trainee updateTrainee(
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
                username,
                password
        );
    }

    public void deleteTrainee(
            String username,
            String password
    ) {
        traineeService.deleteByUsername(
                username,
                password
        );
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

    public Trainee updateTraineeTrainers(
            String username,
            String password,
            Set<String> trainerUsernames
    ) {
        return traineeService.updateTrainers(
                username,
                password,
                trainerUsernames
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

    public Trainer findTrainerByUsername(
            String username,
            String password
    ) {
        return trainerService.findByUsername(
                username,
                password
        );
    }

    public Trainer findTrainerById(
            long id,
            String username,
            String password
    ) {
        return trainerService.findById(
                id,
                username,
                password
        );
    }

    public List<Trainer> findAllTrainers(
            String username,
            String password
    ) {
        return trainerService.findAll(
                username,
                password
        );
    }

    public Trainer updateTrainer(
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
                username,
                password
        );
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

    public List<Trainer> findTrainersNotAssignedToTrainee(
            String traineeUsername,
            String password
    ) {
        return trainerService.findNotAssignedToTrainee(
                traineeUsername,
                password
        );
    }


    public Training createTraining(
            String authUsername,
            String authPassword,
            String traineeUsername,
            String trainerUsername,
            String trainingName,
            TrainingTypeName trainingType,
            LocalDate trainingDate,
            Integer trainingDuration
    ) {
        return trainingService.create(
                authUsername,
                authPassword,
                traineeUsername,
                trainerUsername,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        );
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
        return trainingService.getTraineeTrainings(
                authUsername,
                authPassword,
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
        return trainingService.getTrainerTrainings(
                authUsername,
                authPassword,
                trainerUsername,
                fromDate,
                toDate,
                traineeName
        );
    }
}