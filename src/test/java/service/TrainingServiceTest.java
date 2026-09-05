package service;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dao.TrainingTypeDao;
import org.example.model.*;
import org.example.service.TrainingService;
import org.example.service.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void shouldCreateTraining() {
        Trainee trainee = createTrainee();
        TrainingType trainingType =
                new TrainingType(TrainingTypeName.STRENGTH);
        Trainer trainer = createTrainer(trainingType);

        LocalDate trainingDate =
                LocalDate.of(2026, 8, 10);

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsername(
                "Anna.Brown"
        )).thenReturn(Optional.of(trainer));

        when(trainingTypeDao.findByName(
                TrainingTypeName.STRENGTH
        )).thenReturn(Optional.of(trainingType));

        when(trainingDao.save(any(Training.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Training result = trainingService.create(
                "John.Smith",
                "password",
                "John.Smith",
                "Anna.Brown",
                "Strength training",
                TrainingTypeName.STRENGTH,
                trainingDate,
                60
        );

        assertSame(
                trainee,
                result.getTrainee()
        );

        assertSame(
                trainer,
                result.getTrainer()
        );

        assertSame(
                trainingType,
                result.getTrainingType()
        );

        assertEquals(
                "Strength training",
                result.getTrainingName()
        );

        assertEquals(
                trainingDate,
                result.getTrainingDate()
        );

        assertEquals(
                60,
                result.getTrainingDuration()
        );

        verify(authenticationService)
                .requireAuthentication(
                        "John.Smith",
                        "password"
                );

        verify(traineeDao)
                .findByUsername("John.Smith");

        verify(trainerDao)
                .findByUsername("Anna.Brown");

        verify(trainingTypeDao)
                .findByName(
                        TrainingTypeName.STRENGTH
                );

        verify(trainingDao)
                .save(any(Training.class));
    }

    @Test
    void shouldThrowExceptionWhenTraineeDoesNotExistDuringCreate() {
        when(traineeDao.findByUsername(
                "Unknown.Trainee"
        )).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainingService.create(
                        "John.Smith",
                        "password",
                        "Unknown.Trainee",
                        "Anna.Brown",
                        "Strength training",
                        TrainingTypeName.STRENGTH,
                        LocalDate.of(2026, 8, 10),
                        60
                )
        );

        verify(authenticationService)
                .requireAuthentication(
                        "John.Smith",
                        "password"
                );

        verify(trainerDao, never())
                .findByUsername(anyString());

        verify(trainingDao, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenTrainerDoesNotExistDuringCreate() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsername(
                "Unknown.Trainer"
        )).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainingService.create(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        "Unknown.Trainer",
                        "Strength training",
                        TrainingTypeName.STRENGTH,
                        LocalDate.of(2026, 8, 10),
                        60
                )
        );

        verify(trainingTypeDao, never())
                .findByName(any());

        verify(trainingDao, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenTrainingTypeDoesNotExistDuringCreate() {
        Trainee trainee = createTrainee();

        TrainingType trainerSpecialization =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        Trainer trainer =
                createTrainer(trainerSpecialization);

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsername(
                "Anna.Brown"
        )).thenReturn(Optional.of(trainer));

        when(trainingTypeDao.findByName(
                TrainingTypeName.STRENGTH
        )).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainingService.create(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        "Anna.Brown",
                        "Strength training",
                        TrainingTypeName.STRENGTH,
                        LocalDate.of(2026, 8, 10),
                        60
                )
        );

        verify(trainingDao, never())
                .save(any());
    }

    @Test
    void shouldReturnTraineeTrainings() {
        Training training = createTraining();

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(trainingDao.findTraineeTrainings(
                "John.Smith",
                fromDate,
                toDate,
                "Anna Brown",
                TrainingTypeName.STRENGTH
        )).thenReturn(List.of(training));

        List<Training> result =
                trainingService.getTraineeTrainings(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        fromDate,
                        toDate,
                        "Anna Brown",
                        TrainingTypeName.STRENGTH
                );

        assertEquals(1, result.size());
        assertSame(training, result.get(0));

        verify(authenticationService)
                .requireAuthentication(
                        "John.Smith",
                        "password"
                );

        verify(trainingDao)
                .findTraineeTrainings(
                        "John.Smith",
                        fromDate,
                        toDate,
                        "Anna Brown",
                        TrainingTypeName.STRENGTH
                );
    }

    @Test
    void shouldThrowExceptionWhenTraineeTrainingDateRangeIsInvalid() {
        LocalDate fromDate =
                LocalDate.of(2026, 8, 31);

        LocalDate toDate =
                LocalDate.of(2026, 8, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> trainingService.getTraineeTrainings(
                        "John.Smith",
                        "password",
                        "John.Smith",
                        fromDate,
                        toDate,
                        null,
                        null
                )
        );

        verify(authenticationService)
                .requireAuthentication(
                        "John.Smith",
                        "password"
                );

        verify(trainingDao, never())
                .findTraineeTrainings(
                        anyString(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void shouldReturnTrainerTrainings() {
        Training training = createTraining();

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(trainingDao.findTrainerTrainings(
                "Anna.Brown",
                fromDate,
                toDate,
                "John Smith"
        )).thenReturn(List.of(training));

        List<Training> result =
                trainingService.getTrainerTrainings(
                        "Anna.Brown",
                        "password",
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        "John Smith"
                );

        assertEquals(1, result.size());
        assertSame(training, result.get(0));

        verify(authenticationService)
                .requireAuthentication(
                        "Anna.Brown",
                        "password"
                );

        verify(trainingDao)
                .findTrainerTrainings(
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        "John Smith"
                );
    }

    @Test
    void shouldThrowExceptionWhenTrainerTrainingDateRangeIsInvalid() {
        LocalDate fromDate =
                LocalDate.of(2026, 8, 31);

        LocalDate toDate =
                LocalDate.of(2026, 8, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> trainingService.getTrainerTrainings(
                        "Anna.Brown",
                        "password",
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        null
                )
        );

        verify(authenticationService)
                .requireAuthentication(
                        "Anna.Brown",
                        "password"
                );

        verify(trainingDao, never())
                .findTrainerTrainings(
                        anyString(),
                        any(),
                        any(),
                        any()
                );
    }

    private Training createTraining() {
        return createTraining(
                "Strength training",
                TrainingTypeName.STRENGTH,
                LocalDate.of(2026, 8, 10),
                60
        );
    }

    private Training createTraining(
            String trainingName,
            TrainingTypeName typeName,
            LocalDate date,
            Integer duration
    ) {
        Trainee trainee = createTrainee();

        TrainingType trainingType =
                new TrainingType(typeName);

        Trainer trainer =
                createTrainer(trainingType);

        return new Training(
                trainee,
                trainer,
                trainingName,
                trainingType,
                date,
                duration
        );
    }

    private Trainee createTrainee() {
        User user = new User(
                "John",
                "Smith",
                "John.Smith",
                "Abc123xyZ9",
                true
        );

        return new Trainee(
                user,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );
    }

    private Trainer createTrainer(
            TrainingType trainingType
    ) {
        User user = new User(
                "Anna",
                "Brown",
                "Anna.Brown",
                "Trainer123",
                true
        );

        return new Trainer(
                trainingType,
                user
        );
    }
}