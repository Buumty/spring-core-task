package org.example.service;

import org.example.dao.TraineeDao;
import org.example.model.Trainee;
import org.example.service.generator.IdGenerator;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService {
    private static final Logger log =
            LoggerFactory.getLogger(TraineeService.class);

    private final TraineeDao traineeDao;
    private final IdGenerator idGenerator;
    private final PasswordGenerator passwordGenerator;
    private final UsernameGenerator usernameGenerator;


    public TraineeService(TraineeDao traineeDao, IdGenerator idGenerator, PasswordGenerator passwordGenerator, UsernameGenerator usernameGenerator) {
        this.traineeDao = traineeDao;
        this.idGenerator = idGenerator;
        this.passwordGenerator = passwordGenerator;
        this.usernameGenerator = usernameGenerator;
    }

    public Trainee findById(long id) {
        log.debug("Searching for trainee with id={}", id);
        return traineeDao.findById(id).orElseThrow(() -> {
            log.warn("Trainee with id={} was not found", id);
            return new NoSuchElementException("Trainee with id " + id + " was not found");
        });
    }

    public List<Trainee> findAll() {
        log.debug("Retrieving all trainees");
        return traineeDao.findAll();
    }

    public Trainee create(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String address
    ) {
        Trainee trainee = new Trainee(
                idGenerator.nextUserId(),
                firstName,
                lastName,
                usernameGenerator.generate(firstName, lastName),
                passwordGenerator.generate(),
                true,
                dateOfBirth,
                address
        );

        Trainee savedTrainee = traineeDao.save(trainee);

        log.info(
                "Created trainee id={}, username={}",
                savedTrainee.getUserId(),
                savedTrainee.getUsername()
        );

        return savedTrainee;
    }

    public Trainee update(
            String firstName,
            String lastName,
            String address,
            boolean isActive,
            long id
    ) {
        Trainee traineeFromDB = findById(id);

        traineeFromDB.setFirstName(firstName);
        traineeFromDB.setLastName(lastName);
        traineeFromDB.setAddress(address);
        traineeFromDB.setActive(isActive);

        Trainee updatedTrainee = traineeDao.update(traineeFromDB);

        log.info("Updated trainee id={}", id);

        return updatedTrainee;
    }

    public void deleteById(long id) {
        findById(id);
        traineeDao.deleteById(id);

        log.info("Deleted trainee id={}", id);
    }
}
