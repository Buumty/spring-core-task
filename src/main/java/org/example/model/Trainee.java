package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long traineeId;

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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;

    protected Trainee(){}


    public Trainee(User user, LocalDate dateOfBirth, String address) {
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
