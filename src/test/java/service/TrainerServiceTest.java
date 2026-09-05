package service;

import org.example.dao.TrainerDao;
import org.example.dao.TrainingTypeDao;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.example.model.User;
import org.example.service.TrainerService;
import org.example.service.authentication.AuthenticationService;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void shouldCreateTrainer() {
        TrainingType trainingType =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        when(usernameGenerator.generate(
                "John",
                "Smith"
        )).thenReturn("John.Smith");

        when(passwordGenerator.generate())
                .thenReturn("Abc123xyZ9");

        when(trainingTypeDao.findByName(
                TrainingTypeName.STRENGTH
        )).thenReturn(Optional.of(trainingType));

        when(trainerDao.save(any(Trainer.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Trainer result = trainerService.create(
                "John",
                "Smith",
                TrainingTypeName.STRENGTH
        );

        assertEquals(
                "John",
                result.getUser().getFirstName()
        );

        assertEquals(
                "Smith",
                result.getUser().getLastName()
        );

        assertEquals(
                "John.Smith",
                result.getUser().getUsername()
        );

        assertEquals(
                "Abc123xyZ9",
                result.getUser().getPassword()
        );

        assertTrue(
                result.getUser().isActive()
        );

        assertSame(
                trainingType,
                result.getSpecialization()
        );

        verify(usernameGenerator)
                .generate("John", "Smith");

        verify(passwordGenerator)
                .generate();

        verify(trainingTypeDao)
                .findByName(
                        TrainingTypeName.STRENGTH
                );

        verify(trainerDao)
                .save(any(Trainer.class));
    }

    @Test
    void shouldThrowExceptionWhenTrainingTypeDoesNotExistDuringCreate() {
        when(usernameGenerator.generate(
                "John",
                "Smith"
        )).thenReturn("John.Smith");

        when(passwordGenerator.generate())
                .thenReturn("Abc123xyZ9");

        when(trainingTypeDao.findByName(
                TrainingTypeName.STRENGTH
        )).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainerService.create(
                        "John",
                        "Smith",
                        TrainingTypeName.STRENGTH
                )
        );

        verify(trainerDao, never())
                .save(any());
    }

    @Test
    void shouldFindTrainerByUsername() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainer));

        Trainer result =
                trainerService.findByUsername(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        assertSame(trainer, result);

        verify(authenticationService)
                .requireTrainerAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(trainerDao)
                .findByUsername("John.Smith");
    }


    @Test
    void shouldActivateInactiveTrainer() {
        Trainer trainer = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH,
                false
        );

        when(trainerDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainer));

        trainerService.activate(
                "John.Smith",
                "Abc123xyZ9"
        );

        assertTrue(
                trainer.getUser().isActive()
        );
    }

    @Test
    void shouldThrowExceptionWhenActivatingAlreadyActiveTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainer));

        assertThrows(
                IllegalStateException.class,
                () -> trainerService.activate(
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );
    }

    @Test
    void shouldDeactivateActiveTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainer));

        trainerService.deactivate(
                "John.Smith",
                "Abc123xyZ9"
        );

        assertFalse(
                trainer.getUser().isActive()
        );
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingAlreadyInactiveTrainer() {
        Trainer trainer = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH,
                false
        );

        when(trainerDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainer));

        assertThrows(
                IllegalStateException.class,
                () -> trainerService.deactivate(
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );
    }

    @Test
    void shouldFindTrainersNotAssignedToTrainee() {
        Trainer first = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown",
                TrainingTypeName.YOGA,
                true
        );

        Trainer second = createTrainer(
                "Mike",
                "Jones",
                "Mike.Jones",
                TrainingTypeName.CARDIO,
                true
        );

        when(trainerDao.findNotAssignedToTrainee(
                "John.Smith"
        )).thenReturn(
                List.of(first, second)
        );

        List<Trainer> result =
                trainerService.findNotAssignedToTrainee(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(trainerDao)
                .findNotAssignedToTrainee(
                        "John.Smith"
                );
    }

    private Trainer createTrainer() {
        return createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH,
                true
        );
    }

    private Trainer createTrainer(
            String firstName,
            String lastName,
            String username,
            TrainingTypeName specialization,
            boolean active
    ) {
        User user = new User(
                firstName,
                lastName,
                username,
                "Abc123xyZ9",
                active
        );

        TrainingType trainingType =
                new TrainingType(
                        specialization
                );

        return new Trainer(
                trainingType,
                user
        );
    }
}