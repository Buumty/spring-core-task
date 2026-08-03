package facade;
import org.example.facade.GymFacade;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldCreateTrainee() {
        LocalDate dateOfBirth = LocalDate.of(1995, 5, 10);
        Trainee trainee = createTrainee();

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
    void shouldUpdateTrainee() {
        Trainee trainee = createTrainee();

        when(traineeService.update(
                "Jonathan",
                "Johnson",
                "New address",
                false,
                1L
        )).thenReturn(trainee);

        Trainee result = gymFacade.updateTrainee(
                1L,
                "Jonathan",
                "Johnson",
                "New address",
                false
        );

        assertSame(trainee, result);

        verify(traineeService).update(
                "Jonathan",
                "Johnson",
                "New address",
                false,
                1L
        );
    }

    @Test
    void shouldDeleteTrainee() {
        gymFacade.deleteTrainee(1L);

        verify(traineeService).deleteById(1L);
    }

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = createTrainee();

        when(traineeService.findById(1L))
                .thenReturn(trainee);

        Trainee result = gymFacade.findTraineeById(1L);

        assertSame(trainee, result);
        verify(traineeService).findById(1L);
    }

    @Test
    void shouldReturnAllTrainees() {
        List<Trainee> trainees = List.of(
                createTrainee(),
                new Trainee(
                        2L,
                        "Anna",
                        "Brown",
                        "Anna.Brown",
                        "Password2",
                        true,
                        LocalDate.of(2000, 10, 11),
                        "Second address"
                )
        );

        when(traineeService.findAll())
                .thenReturn(trainees);

        List<Trainee> result = gymFacade.findAllTrainees();

        assertSame(trainees, result);
        assertEquals(2, result.size());

        verify(traineeService).findAll();
    }

    @Test
    void shouldCreateTrainer() {
        Trainer trainer = createTrainer();

        when(trainerService.create(
                "John",
                "Smith",
                TrainingType.STRENGTH
        )).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(
                "John",
                "Smith",
                TrainingType.STRENGTH
        );

        assertSame(trainer, result);

        verify(trainerService).create(
                "John",
                "Smith",
                TrainingType.STRENGTH
        );
    }

    @Test
    void shouldUpdateTrainer() {
        Trainer trainer = createTrainer();

        when(trainerService.update(
                "Jonathan",
                "Johnson",
                false,
                TrainingType.CARDIO,
                1L
        )).thenReturn(trainer);

        Trainer result = gymFacade.updateTrainer(
                1L,
                "Jonathan",
                "Johnson",
                false,
                TrainingType.CARDIO
        );

        assertSame(trainer, result);

        verify(trainerService).update(
                "Jonathan",
                "Johnson",
                false,
                TrainingType.CARDIO,
                1L
        );
    }

    @Test
    void shouldFindTrainerById() {
        Trainer trainer = createTrainer();

        when(trainerService.findById(1L))
                .thenReturn(trainer);

        Trainer result = gymFacade.findTrainerById(1L);

        assertSame(trainer, result);
        verify(trainerService).findById(1L);
    }

    @Test
    void shouldReturnAllTrainers() {
        List<Trainer> trainers = List.of(
                createTrainer(),
                new Trainer(
                        2L,
                        "Anna",
                        "Brown",
                        "Anna.Brown",
                        "Password2",
                        true,
                        TrainingType.YOGA
                )
        );

        when(trainerService.findAll())
                .thenReturn(trainers);

        List<Trainer> result = gymFacade.findAllTrainers();

        assertSame(trainers, result);
        assertEquals(2, result.size());

        verify(trainerService).findAll();
    }

    @Test
    void shouldCreateTraining() {
        LocalDate trainingDate = LocalDate.of(2026, 8, 10);
        Duration trainingDuration = Duration.ofMinutes(60);
        Training training = createTraining();

        when(trainingService.create(
                10L,
                20L,
                "Strength training",
                TrainingType.STRENGTH,
                trainingDate,
                trainingDuration
        )).thenReturn(training);

        Training result = gymFacade.createTraining(
                10L,
                20L,
                "Strength training",
                TrainingType.STRENGTH,
                trainingDate,
                trainingDuration
        );

        assertSame(training, result);

        verify(trainingService).create(
                10L,
                20L,
                "Strength training",
                TrainingType.STRENGTH,
                trainingDate,
                trainingDuration
        );
    }

    @Test
    void shouldFindTrainingById() {
        Training training = createTraining();

        when(trainingService.findById(1L))
                .thenReturn(training);

        Training result = gymFacade.findTrainingById(1L);

        assertSame(training, result);
        verify(trainingService).findById(1L);
    }

    @Test
    void shouldReturnAllTrainings() {
        List<Training> trainings = List.of(
                createTraining(),
                new Training(
                        2L,
                        11L,
                        21L,
                        "Cardio training",
                        TrainingType.CARDIO,
                        LocalDate.of(2026, 8, 11),
                        Duration.ofMinutes(45)
                )
        );

        when(trainingService.findAll())
                .thenReturn(trainings);

        List<Training> result = gymFacade.findAllTrainings();

        assertSame(trainings, result);
        assertEquals(2, result.size());

        verify(trainingService).findAll();
    }

    private Trainee createTrainee() {
        return new Trainee(
                1L,
                "John",
                "Smith",
                "John.Smith",
                "Abc123xyZ9",
                true,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );
    }

    private Trainer createTrainer() {
        return new Trainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                "Abc123xyZ9",
                true,
                TrainingType.STRENGTH
        );
    }

    private Training createTraining() {
        return new Training(
                1L,
                10L,
                20L,
                "Strength training",
                TrainingType.STRENGTH,
                LocalDate.of(2026, 8, 10),
                Duration.ofMinutes(60)
        );
    }
}