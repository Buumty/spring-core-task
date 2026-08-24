package service.generator;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.service.generator.IdGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    @Test
    void shouldGenerateUserIdGreaterThanExistingUserIds() {
        Map<Long, Trainee> traineeStorage = new HashMap<>();
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        Map<Long, Training> trainingStorage = new HashMap<>();

        traineeStorage.put(2L, null);
        traineeStorage.put(7L, null);

        trainerStorage.put(4L, null);
        trainerStorage.put(10L, null);

        IdGenerator idGenerator = new IdGenerator(
                traineeStorage,
                trainerStorage,
                trainingStorage
        );

        assertEquals(11L, idGenerator.nextUserId());
        assertEquals(12L, idGenerator.nextUserId());
    }

    @Test
    void shouldGenerateTrainingIdGreaterThanExistingTrainingIds() {
        Map<Long, Trainee> traineeStorage = new HashMap<>();
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        Map<Long, Training> trainingStorage = new HashMap<>();

        trainingStorage.put(3L, null);
        trainingStorage.put(9L, null);

        IdGenerator idGenerator = new IdGenerator(
                traineeStorage,
                trainerStorage,
                trainingStorage
        );

        assertEquals(10L, idGenerator.nextTrainingId());
        assertEquals(11L, idGenerator.nextTrainingId());
    }

    @Test
    void shouldStartIdsFromOneWhenStoragesAreEmpty() {
        IdGenerator idGenerator = new IdGenerator(
                new HashMap<>(),
                new HashMap<>(),
                new HashMap<>()
        );

        assertEquals(1L, idGenerator.nextUserId());
        assertEquals(1L, idGenerator.nextTrainingId());
    }
}