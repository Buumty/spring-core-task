package org.example.service;

import org.example.dao.TrainerDao;
import org.example.dao.TrainingTypeDao;
import org.example.model.Trainer;
import org.example.model.TrainingType;
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

@Service
@Transactional(readOnly = true)
public class TrainerService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDao trainerDao;
    private final TrainingTypeDao trainingTypeDao;
    private final PasswordGenerator passwordGenerator;
    private final UsernameGenerator usernameGenerator;
    private final AuthenticationService authenticationService;

    public TrainerService(TrainerDao trainerDao, TrainingTypeDao trainingTypeDao, PasswordGenerator passwordGenerator, UsernameGenerator usernameGenerator, AuthenticationService authenticationService) {
        this.trainerDao = trainerDao;
        this.trainingTypeDao = trainingTypeDao;
        this.passwordGenerator = passwordGenerator;
        this.usernameGenerator = usernameGenerator;
        this.authenticationService = authenticationService;
    }

    public Trainer findById(long id, String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);
        log.debug("Searching for trainer with id={}", id);
        return trainerDao.findById(id).orElseThrow(() -> {
            log.warn("Trainer with id={} was not found", id);
            return new NoSuchElementException("Trainer with id " + id + " was not found");
        });
    }

    public List<Trainer> findAll(String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);
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

        TrainingType trainingType = trainingTypeDao.findByName(specialization).orElseThrow(() ->
                new NoSuchElementException(
                        "Training type " + specialization + " not found"
                ));


        Trainer savedTrainer = trainerDao.save(new Trainer(trainingType,
                user));



        log.info(
                "Created trainer id={}, username={}",
                savedTrainer.getTrainerId(),
                savedTrainer.getUser().getUsername()
        );

        return savedTrainer;
    }

    @Transactional
    public Trainer update(
            String firstName,
            String lastName,
            TrainingTypeName specialization,
            String username,
            String password
    ) {
        authenticationService.requireTrainerAuthentication(username,password);
        Trainer trainer = getByUsername(username);

        TrainingType trainingType = trainingTypeDao.findByName(specialization).orElseThrow(() ->
                new NoSuchElementException(
                        "Training type " + specialization + " not found"
                ));

        trainer.getUser().setFirstName(firstName);
        trainer.getUser().setLastName(lastName);
        trainer.setSpecialization(trainingType);

        log.info("Updated trainer username={}", username);

        return trainer;
    }

    @Transactional
    public void changePassword(
            String username,
            String oldPassword,
            String newPassword
    ) {
        authenticationService.requireTrainerAuthentication(username, oldPassword);

        Trainer trainer = getByUsername(username);

        trainer.getUser().setPassword(newPassword);
    }

    @Transactional
    public void activate(String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);

        Trainer trainer = getByUsername(username);

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

        Trainer trainer = getByUsername(username);

        if (!trainer.getUser().isActive()) {
            throw new IllegalStateException(
                    "Trainer is already inactive"
            );
        }

        trainer.getUser().setActive(false);
    }

    public Trainer findByUsername(String username, String password) {
        authenticationService.requireTrainerAuthentication(username,password);

        return getByUsername(username);
    }

    private Trainer getByUsername(String username) {
        return trainerDao.findByUsername(username)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Trainer with username " + username + " not found"
                        ));
    }
}
