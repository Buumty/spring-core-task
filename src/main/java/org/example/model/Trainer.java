package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long trainerId;

    @OneToOne(
            cascade = CascadeType.ALL,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "training_type_id",
            nullable = false
    )
    private TrainingTypeName specialization;

    protected Trainer(){}

    public Trainer(TrainingTypeName specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public User getUser() {
        return user;
    }

    public TrainingTypeName getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingTypeName specialization) {
        this.specialization = specialization;
    }
}
