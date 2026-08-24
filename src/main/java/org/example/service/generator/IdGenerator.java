package org.example.service.generator;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {

    private final AtomicLong userSequence;
    private final AtomicLong trainingSequence;

    public IdGenerator(
            @Qualifier("traineeStorage")
            Map<Long, Trainee> traineeStorage,

            @Qualifier("trainerStorage")
            Map<Long, Trainer> trainerStorage,

            @Qualifier("trainingStorage")
            Map<Long, Training> trainingStorage
    ) {
        long maxTraineeId = findMaxId(traineeStorage);
        long maxTrainerId = findMaxId(trainerStorage);
        long maxTrainingId = findMaxId(trainingStorage);

        long maxUserId = Math.max(maxTraineeId, maxTrainerId);

        this.userSequence = new AtomicLong(maxUserId);
        this.trainingSequence = new AtomicLong(maxTrainingId);
    }

    public long nextUserId() {
        return userSequence.incrementAndGet();
    }

    public long nextTrainingId() {
        return trainingSequence.incrementAndGet();
    }

    private long findMaxId(Map<Long, ?> storage) {
        return storage.keySet()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }
}