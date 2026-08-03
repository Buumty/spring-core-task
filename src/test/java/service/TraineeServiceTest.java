package service;


import org.example.dao.TraineeDao;
import org.example.model.Trainee;
import org.example.service.TraineeService;
import org.example.service.generator.IdGenerator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private UsernameGenerator usernameGenerator;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        Trainee result = traineeService.findById(1L);

        assertSame(trainee, result);
        verify(traineeDao).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTraineeDoesNotExist() {
        when(traineeDao.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> traineeService.findById(999L)
        );

        verify(traineeDao).findById(999L);
    }

    @Test
    void shouldReturnAllTrainees() {
        Trainee first = createTrainee();

        Trainee second = new Trainee(
                2L,
                "Anna",
                "Brown",
                "Anna.Brown",
                "Password2",
                true,
                LocalDate.of(2000, 10, 11),
                "Second address"
        );

        when(traineeDao.findAll())
                .thenReturn(List.of(first, second));

        List<Trainee> result = traineeService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(traineeDao).findAll();
    }

    @Test
    void shouldCreateTrainee() {
        LocalDate dateOfBirth = LocalDate.of(1995, 5, 10);

        when(idGenerator.nextUserId()).thenReturn(1L);
        when(usernameGenerator.generate("John", "Smith"))
                .thenReturn("John.Smith");
        when(passwordGenerator.generate())
                .thenReturn("Abc123xyZ9");
        when(traineeDao.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.create(
                "John",
                "Smith",
                dateOfBirth,
                "Example address"
        );

        assertEquals(1L, result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("John.Smith", result.getUsername());
        assertEquals("Abc123xyZ9", result.getPassword());
        assertEquals(dateOfBirth, result.getDateOfBirth());
        assertEquals("Example address", result.getAddress());
        assertTrue(result.isActive());

        verify(idGenerator).nextUserId();
        verify(usernameGenerator).generate("John", "Smith");
        verify(passwordGenerator).generate();
        verify(traineeDao).save(result);
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));
        when(traineeDao.update(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.update(
                "Jonathan",
                "Johnson",
                "New address",
                false,
                1L
        );

        assertSame(trainee, result);
        assertEquals("Jonathan", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        assertEquals("New address", result.getAddress());
        assertFalse(result.isActive());

        assertEquals("John.Smith", result.getUsername());
        assertEquals("Abc123xyZ9", result.getPassword());
        assertEquals(
                LocalDate.of(1995, 5, 10),
                result.getDateOfBirth()
        );

        verify(traineeDao).findById(1L);
        verify(traineeDao).update(trainee);
    }

    @Test
    void shouldDeleteTraineeById() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        traineeService.deleteById(1L);

        verify(traineeDao).findById(1L);
        verify(traineeDao).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTrainee() {
        when(traineeDao.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> traineeService.deleteById(999L)
        );

        verify(traineeDao).findById(999L);
        verify(traineeDao, never()).deleteById(anyLong());
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
}