package org.example.service;

import org.example.dao.TrainerDao;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.TrainingTypeName;
import org.example.model.User;
import org.example.service.authentication.AuthenticationService;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TrainerService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDao trainerDao;
    private final PasswordGenerator passwordGenerator;
    private final UsernameGenerator usernameGenerator;
    private final AuthenticationService authenticationService;

    public TrainerService(TrainerDao trainerDao, PasswordGenerator passwordGenerator, UsernameGenerator usernameGenerator, AuthenticationService authenticationService) {
        this.trainerDao = trainerDao;
        this.passwordGenerator = passwordGenerator;
        this.usernameGenerator = usernameGenerator;
        this.authenticationService = authenticationService;
    }

    public Trainer findById(long id) {
        log.debug("Searching for trainer with id={}", id);
        return trainerDao.findById(id).orElseThrow(() -> {
            log.warn("Trainer with id={} was not found", id);
            return new NoSuchElementException("Trainer with id " + id + " was not found");
        });
    }

    public List<Trainer> findAll() {
        log.debug("Retrieving all trainers");
        return trainerDao.findAll();
    }

    @Transactional
    public Trainer create(String firstName, String lastName, TrainingTypeName specialization) {
        User user = new User(firstName,
                lastName,
                usernameGenerator.generate(firstName, lastName),
                passwordGenerator.generate(),
                true);
        Trainer savedTrainer = trainerDao.save(new Trainer(specialization,
                user));

        log.info(
                "Created trainer id={}, username={}",
                savedTrainer.getUser().getUserId(),
                savedTrainer.getUser().getUsername()
        );

        return savedTrainer;
    }

    @Transactional
    public Trainer update(
            String firstName,
            String lastName,
            TrainingTypeName specialization,
            long id,
            String username,
            String password
    ) {
        authenticationService.requireTrainerAuthentication(username,password);
        Trainer trainerFromDB = findById(id);

        trainerFromDB.getUser().setFirstName(firstName);
        trainerFromDB.getUser().setLastName(lastName);
        trainerFromDB.setSpecialization(specialization);

        Trainer updatedTrainer = trainerDao.update(trainerFromDB);

        log.info("Updated trainer id={}", id);

        return updatedTrainer;
    }

    @Transactional
    public void changePassword(
            String username,
            String oldPassword,
            String newPassword
    ) {
        authenticationService
                .requireTrainerAuthentication(
                        username,
                        oldPassword
                );

        Trainer trainer =
                trainerDao.findByUsername(username).orElseThrow();

        trainer.getUser().setPassword(newPassword);
    }

    @Transactional
    public void activate(String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);

        Trainer trainer = trainerDao.findByUsername(username).orElseThrow();

        if (trainer.getUser().isActive()) {
            throw new IllegalStateException(
                    "Trainer is already active"
            );
        }

        trainer.getUser().setActive(true);
    }

    @Transactional
    public void deactivate(String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);

        Trainer trainer = trainerDao.findByUsername(username).orElseThrow();

        if (trainer.getUser().isActive()) {
            throw new IllegalStateException(
                    "Trainer is already inactive"
            );
        }

        trainer.getUser().setActive(false);
    }
}
