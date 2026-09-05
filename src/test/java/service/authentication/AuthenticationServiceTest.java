package service.authentication;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.example.model.User;
import org.example.service.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldValidateTraineeCredentials() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername("John.Smith"))
                .thenReturn(Optional.of(trainee));

        boolean result =
                authenticationService.traineeCredentialsValidation(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        assertTrue(result);

        verify(traineeDao)
                .findByUsername("John.Smith");
    }

    @Test
    void shouldReturnFalseWhenTraineePasswordIsInvalid() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername("John.Smith"))
                .thenReturn(Optional.of(trainee));

        boolean result =
                authenticationService.traineeCredentialsValidation(
                        "John.Smith",
                        "WrongPassword"
                );

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTraineeDoesNotExist() {
        when(traineeDao.findByUsername("Unknown.User"))
                .thenReturn(Optional.empty());

        boolean result =
                authenticationService.traineeCredentialsValidation(
                        "Unknown.User",
                        "password"
                );

        assertFalse(result);
    }

    @Test
    void shouldValidateTrainerCredentials() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.of(trainer));

        boolean result =
                authenticationService.trainerCredentialsValidation(
                        "Anna.Brown",
                        "Trainer123"
                );

        assertTrue(result);

        verify(trainerDao)
                .findByUsername("Anna.Brown");
    }

    @Test
    void shouldReturnFalseWhenTrainerPasswordIsInvalid() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.of(trainer));

        boolean result =
                authenticationService.trainerCredentialsValidation(
                        "Anna.Brown",
                        "WrongPassword"
                );

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTrainerDoesNotExist() {
        when(trainerDao.findByUsername("Unknown.Trainer"))
                .thenReturn(Optional.empty());

        boolean result =
                authenticationService.trainerCredentialsValidation(
                        "Unknown.Trainer",
                        "password"
                );

        assertFalse(result);
    }

    @Test
    void shouldAuthenticateTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername("John.Smith"))
                .thenReturn(Optional.of(trainee));

        assertDoesNotThrow(
                () -> authenticationService
                        .requireTraineeAuthentication(
                                "John.Smith",
                                "Abc123xyZ9"
                        )
        );
    }

    @Test
    void shouldThrowExceptionWhenTraineeAuthenticationFails() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername("John.Smith"))
                .thenReturn(Optional.of(trainee));

        assertThrows(
                SecurityException.class,
                () -> authenticationService
                        .requireTraineeAuthentication(
                                "John.Smith",
                                "WrongPassword"
                        )
        );
    }

    @Test
    void shouldAuthenticateTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.of(trainer));

        assertDoesNotThrow(
                () -> authenticationService
                        .requireTrainerAuthentication(
                                "Anna.Brown",
                                "Trainer123"
                        )
        );
    }

    @Test
    void shouldThrowExceptionWhenTrainerAuthenticationFails() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.of(trainer));

        assertThrows(
                SecurityException.class,
                () -> authenticationService
                        .requireTrainerAuthentication(
                                "Anna.Brown",
                                "WrongPassword"
                        )
        );
    }

    @Test
    void shouldAuthenticateWhenCredentialsBelongToTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername("John.Smith"))
                .thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsername("John.Smith"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(
                () -> authenticationService.requireAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );
    }

    @Test
    void shouldAuthenticateWhenCredentialsBelongToTrainer() {
        Trainer trainer = createTrainer();

        when(traineeDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.empty());

        when(trainerDao.findByUsername("Anna.Brown"))
                .thenReturn(Optional.of(trainer));

        assertDoesNotThrow(
                () -> authenticationService.requireAuthentication(
                        "Anna.Brown",
                        "Trainer123"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenGenericAuthenticationFails() {
        when(traineeDao.findByUsername("Unknown.User"))
                .thenReturn(Optional.empty());

        when(trainerDao.findByUsername("Unknown.User"))
                .thenReturn(Optional.empty());

        assertThrows(
                SecurityException.class,
                () -> authenticationService.requireAuthentication(
                        "Unknown.User",
                        "password"
                )
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

    private Trainer createTrainer() {
        User user = new User(
                "Anna",
                "Brown",
                "Anna.Brown",
                "Trainer123",
                true
        );

        TrainingType trainingType =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        return new Trainer(
                trainingType,
                user
        );
    }
}