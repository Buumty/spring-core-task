package org.example.model;

public class Trainer extends User {
    private TrainingType specialization;

    public Trainer(long userId, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
        super(userId, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
}
