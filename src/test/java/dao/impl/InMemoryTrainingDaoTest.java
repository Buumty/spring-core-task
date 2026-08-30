package dao.impl;

import org.example.dao.impl.TrainingDaoImpl;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainingDaoTest {

    private Map<Long, Training> trainingStorage;
    private TrainingDaoImpl trainingDao;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        trainingDao = new TrainingDaoImpl(trainingStorage);
    }

    @Test
    void shouldSaveTraining() {
        Training training = createTraining(
                1L,
                10L,
                20L,
                "Strength training",
                TrainingTypeName.STRENGTH
        );

        Training savedTraining = trainingDao.save(training);

        assertSame(training, savedTraining);
        assertSame(training, trainingStorage.get(1L));
        assertEquals(1, trainingStorage.size());
    }

    @Test
    void shouldFindTrainingById() {
        Training training = createTraining(
                1L,
                10L,
                20L,
                "Strength training",
                TrainingTypeName.STRENGTH
        );

        trainingStorage.put(training.getTrainingId(), training);

        Training foundTraining = trainingDao.findById(1L)
                .orElseThrow();

        assertSame(training, foundTraining);
    }

    @Test
    void shouldReturnEmptyOptionalWhenTrainingDoesNotExist() {
        assertTrue(trainingDao.findById(999L).isEmpty());
    }

    @Test
    void shouldReturnAllTrainings() {
        Training firstTraining = createTraining(
                1L,
                10L,
                20L,
                "Strength training",
                TrainingTypeName.STRENGTH
        );

        Training secondTraining = createTraining(
                2L,
                11L,
                21L,
                "Cardio training",
                TrainingTypeName.CARDIO
        );

        trainingStorage.put(
                firstTraining.getTrainingId(),
                firstTraining
        );

        trainingStorage.put(
                secondTraining.getTrainingId(),
                secondTraining
        );

        List<Training> trainings = trainingDao.findAll();

        assertEquals(2, trainings.size());
        assertTrue(trainings.contains(firstTraining));
        assertTrue(trainings.contains(secondTraining));
    }

    @Test
    void shouldReturnEmptyListWhenStorageIsEmpty() {
        List<Training> trainings = trainingDao.findAll();

        assertTrue(trainings.isEmpty());
    }

    private Training createTraining(
            long trainingId,
            long traineeId,
            long trainerId,
            String trainingName,
            TrainingTypeName trainingType
    ) {
        return new Training(
                trainingId,
                traineeId,
                trainerId,
                trainingName,
                trainingType,
                LocalDate.of(2026, 8, 10),
                Duration.ofMinutes(60)
        );
    }
}