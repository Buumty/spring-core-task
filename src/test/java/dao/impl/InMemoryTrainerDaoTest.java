package dao.impl;

import org.example.dao.impl.TrainerDaoImpl;
import org.example.model.Trainer;
import org.example.model.TrainingTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainerDaoTest {

    private Map<Long, Trainer> trainerStorage;
    private TrainerDaoImpl trainerDao;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        trainerDao = new TrainerDaoImpl(trainerStorage);
    }

    @Test
    void shouldSaveTrainer() {
        Trainer trainer = createTrainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer savedTrainer = trainerDao.save(trainer);

        assertSame(trainer, savedTrainer);
        assertSame(trainer, trainerStorage.get(1L));
        assertEquals(1, trainerStorage.size());
    }

    @Test
    void shouldFindTrainerById() {
        Trainer trainer = createTrainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        trainerStorage.put(trainer.getUserId(), trainer);

        Trainer foundTrainer = trainerDao.findById(1L)
                .orElseThrow();

        assertSame(trainer, foundTrainer);
    }

    @Test
    void shouldReturnEmptyOptionalWhenTrainerDoesNotExist() {
        assertTrue(trainerDao.findById(999L).isEmpty());
    }

    @Test
    void shouldReturnAllTrainers() {
        Trainer firstTrainer = createTrainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer secondTrainer = createTrainer(
                2L,
                "Anna",
                "Brown",
                "Anna.Brown",
                TrainingTypeName.YOGA
        );

        trainerStorage.put(firstTrainer.getUserId(), firstTrainer);
        trainerStorage.put(secondTrainer.getUserId(), secondTrainer);

        List<Trainer> trainers = trainerDao.findAll();

        assertEquals(2, trainers.size());
        assertTrue(trainers.contains(firstTrainer));
        assertTrue(trainers.contains(secondTrainer));
    }

    @Test
    void shouldReturnEmptyListWhenStorageIsEmpty() {
        List<Trainer> trainers = trainerDao.findAll();

        assertTrue(trainers.isEmpty());
    }

    @Test
    void shouldUpdateExistingTrainer() {
        Trainer originalTrainer = createTrainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer updatedTrainer = new Trainer(
                1L,
                "Jonathan",
                "Smith",
                "John.Smith",
                "abcdefghij",
                false,
                TrainingTypeName.CARDIO
        );

        trainerStorage.put(
                originalTrainer.getUserId(),
                originalTrainer
        );

        Trainer result = trainerDao.update(updatedTrainer);

        assertSame(updatedTrainer, result);
        assertSame(updatedTrainer, trainerStorage.get(1L));
        assertEquals("Jonathan", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(TrainingTypeName.CARDIO, result.getSpecialization());
        assertFalse(result.isActive());
    }
    @Test
    void shouldReturnTrueWhenUsernameExists() {
        Trainer trainer = createTrainer(
                1L,
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        trainerStorage.put(trainer.getUserId(), trainer);

        assertTrue(trainerDao.existsByUsername("John.Smith"));
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {
        assertFalse(trainerDao.existsByUsername("Unknown.User"));
    }

    private Trainer createTrainer(
            long userId,
            String firstName,
            String lastName,
            String username,
            TrainingTypeName specialization
    ) {
        return new Trainer(
                userId,
                firstName,
                lastName,
                username,
                "abcdefghij",
                true,
                specialization
        );
    }
}