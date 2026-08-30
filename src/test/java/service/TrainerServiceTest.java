package service;

import org.example.dao.TrainerDao;
import org.example.model.Trainer;
import org.example.model.TrainingTypeName;
import org.example.service.TrainerService;
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
    private PasswordGenerator passwordGenerator;

    @Mock
    private UsernameGenerator usernameGenerator;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void shouldFindTrainerById() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(1L))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.findById(1L);

        assertSame(trainer, result);
        verify(trainerDao).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTrainerDoesNotExist() {
        when(trainerDao.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainerService.findById(999L)
        );

        verify(trainerDao).findById(999L);
    }

    @Test
    void shouldReturnAllTrainers() {
        Trainer first = createTrainer();

        Trainer second = new Trainer(
                2L,
                "Anna",
                "Brown",
                "Anna.Brown",
                "Password2",
                true,
                TrainingTypeName.YOGA
        );

        when(trainerDao.findAll())
                .thenReturn(List.of(first, second));

        List<Trainer> result = trainerService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainerDao).findAll();
    }

    @Test
    void shouldCreateTrainer() {
        when(idGenerator.nextUserId()).thenReturn(1L);
        when(usernameGenerator.generate("John", "Smith"))
                .thenReturn("John.Smith");
        when(passwordGenerator.generate())
                .thenReturn("Abc123xyZ9");
        when(trainerDao.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.create(
                "John",
                "Smith",
                TrainingTypeName.STRENGTH
        );

        assertEquals(1L, result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("John.Smith", result.getUsername());
        assertEquals("Abc123xyZ9", result.getPassword());
        assertEquals(
                TrainingTypeName.STRENGTH,
                result.getSpecialization()
        );
        assertTrue(result.isActive());

        verify(idGenerator).nextUserId();
        verify(usernameGenerator).generate("John", "Smith");
        verify(passwordGenerator).generate();
        verify(trainerDao).save(result);
    }

    @Test
    void shouldUpdateTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(1L))
                .thenReturn(Optional.of(trainer));
        when(trainerDao.update(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.update(
                "Jonathan",
                "Johnson",
                false,
                TrainingTypeName.CARDIO,
                1L
        );

        assertSame(trainer, result);
        assertEquals("Jonathan", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        assertFalse(result.isActive());
        assertEquals(
                TrainingTypeName.CARDIO,
                result.getSpecialization()
        );

        assertEquals("John.Smith", result.getUsername());
        assertEquals("Abc123xyZ9", result.getPassword());

        verify(trainerDao).findById(1L);
        verify(trainerDao).update(trainer);
    }

    private Trainer createTrainer() {
        return new Trainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                "Abc123xyZ9",
                true,
                TrainingTypeName.STRENGTH
        );
    }
}