package org.example.service;

import org.example.dao.TrainerDao;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.service.generator.IdGenerator;
import org.example.service.generator.PasswordGenerator;
import org.example.service.generator.UsernameGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainerService {
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
        return trainerDao.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    public Trainer create(String firstName, String lastName, TrainingType specialization) {
        return trainerDao.save(new Trainer(
                idGenerator.nextUserId(),
                firstName,
                lastName,
                usernameGenerator.generate(firstName, lastName),
                passwordGenerator.generate(),
                true,
                specialization));
    }

    public Trainer update(String firstName, String lastName, boolean isActive, TrainingType specialization, long id) {
        Trainer trainerFromDB = findById(id);

        trainerFromDB.setFirstName(firstName);
        trainerFromDB.setLastName(lastName);
        trainerFromDB.setActive(isActive);
        trainerFromDB.setSpecialization(specialization);

        return trainerDao.update(trainerFromDB);
    }
}
