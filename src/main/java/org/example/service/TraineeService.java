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

@Service
public class TraineeService {
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
        return traineeDao.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    public Trainee create(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        Trainee trainee = new Trainee(
                idGenerator.nextUserId(),
                firstName,
                lastName,
                usernameGenerator.generate(firstName,lastName),
                passwordGenerator.generate(),
                true,
                dateOfBirth,
                address
        );


        return traineeDao.save(trainee);
    }

    public Trainee update(String firstName, String lastName, String address, boolean isActive, long id) {
        Trainee traineeFromDB = traineeDao.findById(id).orElseThrow(NoSuchElementException::new);

        traineeFromDB.setFirstName(firstName);
        traineeFromDB.setLastName(lastName);
        traineeFromDB.setAddress(address);
        traineeFromDB.setActive(isActive);

        return traineeDao.update(traineeFromDB);
    }

    public void deleteById(long id) {
        findById(id);
        traineeDao.deleteById(id);
    }
}
