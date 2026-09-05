package facade;

import org.example.facade.GymFacade;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    // =========================
    // TRAINEE
    // =========================

    @Test
    void shouldCreateTrainee() {
        Trainee trainee = mock(Trainee.class);

        LocalDate dateOfBirth =
                LocalDate.of(1995, 5, 10);

        when(traineeService.create(
                "John",
                "Smith",
                dateOfBirth,
                "Example address"
        )).thenReturn(trainee);

        Trainee result = gymFacade.createTrainee(
                "John",
                "Smith",
                dateOfBirth,
                "Example address"
        );

        assertSame(trainee, result);

        verify(traineeService).create(
                "John",
                "Smith",
                dateOfBirth,
                "Example address"
        );
    }

    @Test
    void shouldFindTraineeByUsername() {
        Trainee trainee = mock(Trainee.class);

        when(traineeService.findByUsername(
                "John.Smith",
                "password"
        )).thenReturn(trainee);

        Trainee result =
                gymFacade.findTraineeByUsername(
                        "John.Smith",
                        "password"
                );

        assertSame(trainee, result);

        verify(traineeService).findByUsername(
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = mock(Trainee.class);

        when(traineeService.findById(
                1L,
                "John.Smith",
                "password"
        )).thenReturn(trainee);

        Trainee result = gymFacade.findTraineeById(
                1L,
                "John.Smith",
                "password"
        );

        assertSame(trainee, result);

        verify(traineeService).findById(
                1L,
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldFindAllTrainees() {
        List<Trainee> trainees =
                List.of(
                        mock(Trainee.class),
                        mock(Trainee.class)
                );

        when(traineeService.findAll(
                "John.Smith",
                "password"
        )).thenReturn(trainees);

        List<Trainee> result =
                gymFacade.findAllTrainees(
                        "John.Smith",
                        "password"
                );

        assertSame(trainees, result);

        verify(traineeService).findAll(
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee trainee = mock(Trainee.class);

        LocalDate dateOfBirth =
                LocalDate.of(1996, 6, 15);

        when(traineeService.update(
                "Jonathan",
                "Johnson",
                "New address",
                dateOfBirth,
                "John.Smith",
                "password"
        )).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(
                "Jonathan",
                "Johnson",
                "New address",
                dateOfBirth,
                "John.Smith",
                "password"
        );

        assertSame(trainee, result);

        verify(traineeService).update(
                "Jonathan",
                "Johnson",
                "New address",
                dateOfBirth,
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldDeleteTrainee() {
        gymFacade.deleteTrainee(
                "John.Smith",
                "password"
        );

        verify(traineeService).deleteByUsername(
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldChangeTraineePassword() {
        gymFacade.changeTraineePassword(
                "John.Smith",
                "oldPassword",
                "newPassword"
        );

        verify(traineeService).changePassword(
                "John.Smith",
                "oldPassword",
                "newPassword"
        );
    }

    @Test
    void shouldActivateTrainee() {
        gymFacade.activateTrainee(
                "John.Smith",
                "password"
        );

        verify(traineeService).activate(
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldDeactivateTrainee() {
        gymFacade.deactivateTrainee(
                "John.Smith",
                "password"
        );

        verify(traineeService).deactivate(
                "John.Smith",
                "password"
        );
    }

    @Test
    void shouldUpdateTraineeTrainers() {
        Trainee trainee = mock(Trainee.class);

        Set<String> trainerUsernames =
                Set.of(
                        "Anna.Brown",
                        "Mike.Jones"
                );

        when(traineeService.updateTrainers(
                "John.Smith",
                "password",
                trainerUsernames
        )).thenReturn(trainee);

        Trainee result =
                gymFacade.updateTraineeTrainers(
                        "John.Smith",
                        "password",
                        trainerUsernames
                );

        assertSame(trainee, result);

        verify(traineeService).updateTrainers(
                "John.Smith",
                "password",
                trainerUsernames
        );
    }

    // =========================
    // TRAINER
    // =========================

    @Test
    void shouldCreateTrainer() {
        Trainer trainer = mock(Trainer.class);

        when(trainerService.create(
                "Anna",
                "Brown",
                TrainingTypeName.STRENGTH
        )).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(
                "Anna",
                "Brown",
                TrainingTypeName.STRENGTH
        );

        assertSame(trainer, result);

        verify(trainerService).create(
                "Anna",
                "Brown",
                TrainingTypeName.STRENGTH
        );
    }

    @Test
    void shouldFindTrainerByUsername() {
        Trainer trainer = mock(Trainer.class);

        when(trainerService.findByUsername(
                "Anna.Brown",
                "password"
        )).thenReturn(trainer);

        Trainer result =
                gymFacade.findTrainerByUsername(
                        "Anna.Brown",
                        "password"
                );

        assertSame(trainer, result);

        verify(trainerService).findByUsername(
                "Anna.Brown",
                "password"
        );
    }


    @Test
    void shouldUpdateTrainer() {
        Trainer trainer = mock(Trainer.class);

        when(trainerService.update(
                "Anna",
                "Johnson",
                TrainingTypeName.CARDIO,
                "Anna.Brown",
                "password"
        )).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(
                "Anna",
                "Johnson",
                TrainingTypeName.CARDIO,
                "Anna.Brown",
                "password"
        );

        assertSame(trainer, result);

        verify(trainerService).update(
                "Anna",
                "Johnson",
                TrainingTypeName.CARDIO,
                "Anna.Brown",
                "password"
        );
    }

    @Test
    void shouldChangeTrainerPassword() {
        gymFacade.changeTrainerPassword(
                "Anna.Brown",
                "oldPassword",
                "newPassword"
        );

        verify(trainerService).changePassword(
                "Anna.Brown",
                "oldPassword",
                "newPassword"
        );
    }

    @Test
    void shouldActivateTrainer() {
        gymFacade.activateTrainer(
                "Anna.Brown",
                "password"
        );

        verify(trainerService).activate(
                "Anna.Brown",
                "password"
        );
    }

    @Test
    void shouldDeactivateTrainer() {
        gymFacade.deactivateTrainer(
                "Anna.Brown",
                "password"
        );

        verify(trainerService).deactivate(
                "Anna.Brown",
                "password"
        );
    }

    @Test
    void shouldFindTrainersNotAssignedToTrainee() {
        List<Trainer> trainers =
                List.of(
                        mock(Trainer.class),
                        mock(Trainer.class)
                );

        when(trainerService.findNotAssignedToTrainee(
                "John.Smith",
                "password"
        )).thenReturn(trainers);

        List<Trainer> result =
                gymFacade.findTrainersNotAssignedToTrainee(
                        "John.Smith",
                        "password"
                );

        assertSame(trainers, result);

        verify(trainerService)
                .findNotAssignedToTrainee(
                        "John.Smith",
                        "password"
                );
    }

    // =========================
    // TRAINING
    // =========================

    @Test
    void shouldCreateTraining() {
        Training training = mock(Training.class);

        LocalDate trainingDate =
                LocalDate.of(2026, 8, 10);

        when(trainingService.create(
                "John.Smith",
                "password",
                "John.Smith",
                "Anna.Brown",
                "Strength training",
                TrainingTypeName.STRENGTH,
                trainingDate,
                60
        )).thenReturn(training);

        Training result = gymFacade.createTraining(
                "John.Smith",
                "password",
                "John.Smith",
                "Anna.Brown",
                "Strength training",
                TrainingTypeName.STRENGTH,
                trainingDate,
                60
        );

        assertSame(training, result);

        verify(trainingService).create(
                "John.Smith",
                "password",
                "John.Smith",
                "Anna.Brown",
                "Strength training",
                TrainingTypeName.STRENGTH,
                trainingDate,
                60
        );
    }

    @Test
    void shouldGetTraineeTrainings() {
        List<Training> trainings =
                List.of(mock(Training.class));

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(trainingService.getTraineeTrainings(
                "John.Smith",
                "password",
                "John.Smith",
                fromDate,
                toDate,
                "Anna Brown",
                TrainingTypeName.STRENGTH
        )).thenReturn(trainings);

        List<Training> result =
                gymFacade.getTraineeTrainings(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        fromDate,
                        toDate,
                        "Anna Brown",
                        TrainingTypeName.STRENGTH
                );

        assertSame(trainings, result);

        verify(trainingService)
                .getTraineeTrainings(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        fromDate,
                        toDate,
                        "Anna Brown",
                        TrainingTypeName.STRENGTH
                );
    }

    @Test
    void shouldGetTrainerTrainings() {
        List<Training> trainings =
                List.of(mock(Training.class));

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(trainingService.getTrainerTrainings(
                "Anna.Brown",
                "password",
                "Anna.Brown",
                fromDate,
                toDate,
                "John Smith"
        )).thenReturn(trainings);

        List<Training> result =
                gymFacade.getTrainerTrainings(
                        "Anna.Brown",
                        "password",
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        "John Smith"
                );

        assertSame(trainings, result);

        verify(trainingService)
                .getTrainerTrainings(
                        "Anna.Brown",
                        "password",
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        "John Smith"
                );
    }
}