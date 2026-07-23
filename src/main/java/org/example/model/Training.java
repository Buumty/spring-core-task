package org.example.model;

import java.time.Duration;
import java.time.LocalDate;

public class Training {
    private final long traineeId;
    private final long trainerId;
    private final String trainingName;
    private final TrainingType trainingType;
    private final LocalDate trainingDate;
    private final Duration trainingDuration;

    public Training(long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Duration trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }


    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
    }
}
