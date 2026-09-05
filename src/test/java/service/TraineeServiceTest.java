package service;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.model.*;
import org.example.service.TraineeService;
import org.example.service.authentication.AuthenticationService;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        Trainee result = traineeService.findById(
                1L,
                "John.Smith",
                "Abc123xyZ9"
        );

        assertSame(trainee, result);

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(traineeDao).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTraineeDoesNotExistById() {
        when(traineeDao.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> traineeService.findById(
                        999L,
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(traineeDao).findById(999L);
    }

    @Test
    void shouldReturnAllTrainees() {
        Trainee first = createTrainee();

        Trainee second = createTrainee(
                "Anna",
                "Brown",
                "Anna.Brown",
                true
        );

        when(traineeDao.findAll())
                .thenReturn(List.of(first, second));

        List<Trainee> result =
                traineeService.findAll(
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

        verify(traineeDao).findAll();
    }

    @Test
    void shouldCreateTrainee() {
        LocalDate dateOfBirth =
                LocalDate.of(1995, 5, 10);

        when(usernameGenerator.generate(
                "John",
                "Smith"
        )).thenReturn("John.Smith");

        when(passwordGenerator.generate())
                .thenReturn("Abc123xyZ9");

        when(traineeDao.save(any(Trainee.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Trainee result = traineeService.create(
                "John",
                "Smith",
                dateOfBirth,
                "Example address"
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

        assertEquals(
                dateOfBirth,
                result.getDateOfBirth()
        );

        assertEquals(
                "Example address",
                result.getAddress()
        );

        verify(usernameGenerator)
                .generate("John", "Smith");

        verify(passwordGenerator)
                .generate();

        verify(traineeDao)
                .save(any(Trainee.class));
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee trainee = createTrainee();

        LocalDate newDateOfBirth =
                LocalDate.of(1996, 6, 15);

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.update(
                "Jonathan",
                "Johnson",
                "New address",
                newDateOfBirth,
                "John.Smith",
                "Abc123xyZ9"
        );

        assertSame(trainee, result);

        assertEquals(
                "Jonathan",
                result.getUser().getFirstName()
        );

        assertEquals(
                "Johnson",
                result.getUser().getLastName()
        );

        assertEquals(
                "New address",
                result.getAddress()
        );

        assertEquals(
                "John.Smith",
                result.getUser().getUsername()
        );

        assertEquals(
                "Abc123xyZ9",
                result.getUser().getPassword()
        );
        assertEquals(
                newDateOfBirth,
                result.getDateOfBirth()
        );

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(traineeDao)
                .findByUsername("John.Smith");

        verify(traineeDao, never())
                .update(any());
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        traineeService.deleteByUsername(
                "John.Smith",
                "Abc123xyZ9"
        );

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(traineeDao)
                .findByUsername("John.Smith");

        verify(traineeDao)
                .delete(trainee);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTrainee() {
        when(traineeDao.findByUsername(
                "Unknown.User"
        )).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> traineeService.deleteByUsername(
                        "Unknown.User",
                        "password"
                )
        );

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "Unknown.User",
                        "password"
                );

        verify(traineeDao, never())
                .delete(any());
    }

    @Test
    void shouldFindTraineeByUsername() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        Trainee result =
                traineeService.findByUsername(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        assertSame(trainee, result);

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );
    }

    @Test
    void shouldChangePassword() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        traineeService.changePassword(
                "John.Smith",
                "Abc123xyZ9",
                "NewPassword1"
        );

        assertEquals(
                "NewPassword1",
                trainee.getUser().getPassword()
        );

        verify(authenticationService)
                .requireTraineeAuthentication(
                        "John.Smith",
                        "Abc123xyZ9"
                );

        verify(traineeDao, never())
                .update(any());
    }

    @Test
    void shouldActivateInactiveTrainee() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith",
                false
        );

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        traineeService.activate(
                "John.Smith",
                "Abc123xyZ9"
        );

        assertTrue(
                trainee.getUser().isActive()
        );
    }

    @Test
    void shouldThrowExceptionWhenActivatingAlreadyActiveTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        assertThrows(
                IllegalStateException.class,
                () -> traineeService.activate(
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );
    }

    @Test
    void shouldDeactivateActiveTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        traineeService.deactivate(
                "John.Smith",
                "Abc123xyZ9"
        );

        assertFalse(
                trainee.getUser().isActive()
        );
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingAlreadyInactiveTrainee() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith",
                false
        );

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        assertThrows(
                IllegalStateException.class,
                () -> traineeService.deactivate(
                        "John.Smith",
                        "Abc123xyZ9"
                )
        );
    }

    @Test
    void shouldUpdateTrainersList() {
        Trainee trainee = createTrainee();

        Trainer trainer1 = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown"
        );

        Trainer trainer2 = createTrainer(
                "Mike",
                "Jones",
                "Mike.Jones"
        );

        Set<String> usernames = Set.of(
                "Anna.Brown",
                "Mike.Jones"
        );

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsernames(
                usernames
        )).thenReturn(
                List.of(trainer1, trainer2)
        );

        Trainee result =
                traineeService.updateTrainers(
                        "John.Smith",
                        "Abc123xyZ9",
                        usernames
                );

        assertEquals(
                2,
                result.getTrainers().size()
        );

        assertTrue(
                result.getTrainers()
                        .contains(trainer1)
        );

        assertTrue(
                result.getTrainers()
                        .contains(trainer2)
        );

        verify(trainerDao)
                .findByUsernames(usernames);
    }

    @Test
    void shouldRemoveAllTrainersWhenUsernameSetIsEmpty() {
        Trainee trainee = createTrainee();

        Trainer trainer = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown"
        );

        trainee.getTrainers().add(trainer);

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        Trainee result =
                traineeService.updateTrainers(
                        "John.Smith",
                        "Abc123xyZ9",
                        Set.of()
                );

        assertTrue(
                result.getTrainers().isEmpty()
        );

        verify(trainerDao, never())
                .findByUsernames(any());
    }

    @Test
    void shouldThrowExceptionWhenTrainerUsernamesAreNull() {
        Trainee trainee = createTrainee();

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        assertThrows(
                IllegalArgumentException.class,
                () -> traineeService.updateTrainers(
                        "John.Smith",
                        "Abc123xyZ9",
                        null
                )
        );

        verify(trainerDao, never())
                .findByUsernames(any());
    }

    @Test
    void shouldThrowExceptionWhenOneOrMoreTrainersDoNotExist() {
        Trainee trainee = createTrainee();

        Trainer trainer = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown"
        );

        Set<String> usernames = Set.of(
                "Anna.Brown",
                "Unknown.Trainer"
        );

        when(traineeDao.findByUsername(
                "John.Smith"
        )).thenReturn(Optional.of(trainee));

        when(trainerDao.findByUsernames(
                usernames
        )).thenReturn(
                List.of(trainer)
        );

        assertThrows(
                NoSuchElementException.class,
                () -> traineeService.updateTrainers(
                        "John.Smith",
                        "Abc123xyZ9",
                        usernames
                )
        );

        assertTrue(
                trainee.getTrainers().isEmpty()
        );
    }

    private Trainee createTrainee() {
        return createTrainee(
                "John",
                "Smith",
                "John.Smith",
                true
        );
    }

    private Trainee createTrainee(
            String firstName,
            String lastName,
            String username,
            boolean active
    ) {
        User user = new User(
                firstName,
                lastName,
                username,
                "Abc123xyZ9",
                active
        );

        return new Trainee(
                user,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );
    }

    private Trainer createTrainer(
            String firstName,
            String lastName,
            String username
    ) {
        User user = new User(
                firstName,
                lastName,
                username,
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