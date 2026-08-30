package dao.impl;

import org.example.dao.impl.TraineeDaoImpl;
import org.example.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTraineeDaoTest {

    private Map<Long, Trainee> traineeStorage;
    private TraineeDaoImpl traineeDao;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        traineeDao = new TraineeDaoImpl(traineeStorage);
    }

    @Test
    void shouldSaveTrainee() {
        Trainee trainee = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        Trainee savedTrainee = traineeDao.save(trainee);

        assertSame(trainee, savedTrainee);
        assertSame(trainee, traineeStorage.get(1L));
        assertEquals(1, traineeStorage.size());
    }

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        traineeStorage.put(trainee.getUserId(), trainee);

        Trainee foundTrainee = traineeDao.findById(1L)
                .orElseThrow();

        assertSame(trainee, foundTrainee);
    }

    @Test
    void shouldReturnEmptyOptionalWhenTraineeDoesNotExist() {
        assertTrue(traineeDao.findById(999L).isEmpty());
    }

    @Test
    void shouldReturnAllTrainees() {
        Trainee first = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        Trainee second = createTrainee(
                2L,
                "Anna",
                "Brown",
                "Anna.Brown"
        );

        traineeStorage.put(first.getUserId(), first);
        traineeStorage.put(second.getUserId(), second);

        List<Trainee> trainees = traineeDao.findAll();

        assertEquals(2, trainees.size());
        assertTrue(trainees.contains(first));
        assertTrue(trainees.contains(second));
    }

    @Test
    void shouldUpdateExistingTrainee() {
        Trainee originalTrainee = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        Trainee updatedTrainee = new Trainee(
                1L,
                "Jonathan",
                "Smith",
                "John.Smith",
                "abcdefghij",
                false,
                LocalDate.of(1995, 5, 10),
                "New address"
        );

        traineeStorage.put(
                originalTrainee.getUserId(),
                originalTrainee
        );

        Trainee result = traineeDao.update(updatedTrainee);

        assertSame(updatedTrainee, result);
        assertSame(updatedTrainee, traineeStorage.get(1L));
        assertEquals("Jonathan", result.getFirstName());
        assertEquals("New address", result.getAddress());
        assertFalse(result.isActive());
    }

    @Test
    void shouldDeleteTraineeById() {
        Trainee trainee = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        traineeStorage.put(trainee.getUserId(), trainee);

        traineeDao.deleteById(1L);

        assertFalse(traineeStorage.containsKey(1L));
        assertTrue(traineeDao.findById(1L).isEmpty());
    }
    @Test
    void shouldReturnTrueWhenUsernameExists() {
        Trainee trainee = createTrainee(
                1L,
                "John",
                "Smith",
                "John.Smith"
        );

        traineeStorage.put(trainee.getUserId(), trainee);

        assertTrue(traineeDao.existsByUsername("John.Smith"));
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {
        assertFalse(traineeDao.existsByUsername("Unknown.User"));
    }

    private Trainee createTrainee(
            long userId,
            String firstName,
            String lastName,
            String username
    ) {
        return new Trainee(
                userId,
                firstName,
                lastName,
                username,
                "abcdefghij",
                true,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );
    }
}