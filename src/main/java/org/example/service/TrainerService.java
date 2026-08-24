package org.example.service;

import org.example.dao.TrainerDao;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.service.generator.IdGenerator;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainerService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDao trainerDao;
    private final IdGenerator idGenerator;
    private final PasswordGenerator passwordGenerator;
    private final UsernameGenerator usernameGenerator;

    public TrainerService(TrainerDao trainerDao, IdGenerator idGenerator, PasswordGenerator passwordGenerator, UsernameGenerator usernameGenerator) {
        this.trainerDao = trainerDao;
        this.idGenerator = idGenerator;
        this.passwordGenerator = passwordGenerator;
        this.usernameGenerator = usernameGenerator;
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

    public Trainer create(String firstName, String lastName, TrainingType specialization) {
        Trainer savedTrainer = trainerDao.save(new Trainer(
                idGenerator.nextUserId(),
                firstName,
                lastName,
                usernameGenerator.generate(firstName, lastName),
                passwordGenerator.generate(),
                true,
                specialization));

        log.info(
                "Created trainer id={}, username={}",
                savedTrainer.getUserId(),
                savedTrainer.getUsername()
        );

        return savedTrainer;
    }

    public Trainer update(
            String firstName,
            String lastName,
            boolean isActive,
            TrainingType specialization,
            long id
    ) {
        Trainer trainerFromDB = findById(id);

        trainerFromDB.setFirstName(firstName);
        trainerFromDB.setLastName(lastName);
        trainerFromDB.setActive(isActive);
        trainerFromDB.setSpecialization(specialization);

        Trainer updatedTrainer = trainerDao.update(trainerFromDB);

        log.info("Updated trainer id={}", id);

        return updatedTrainer;
    }
}
