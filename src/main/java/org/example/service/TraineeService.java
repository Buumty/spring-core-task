package org.example.service;

import org.example.dao.TraineeDao;
import org.example.model.Trainee;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TraineeService {
    private final TraineeDao traineeDao;

    public TraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee findById(long id) {
        return traineeDao.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    public Trainee create(Trainee trainee) {
        return traineeDao.save(trainee);
    }

    public Trainee update(Trainee newTrainee, long id) {
        Trainee traineeFromDB = traineeDao.findById(id).orElseThrow(NoSuchElementException::new);

        if (!newTrainee.getFirstName().equals(traineeFromDB.getFirstName())) {
            traineeFromDB.setFirstName(newTrainee.getFirstName());
        }
        if (!newTrainee.getLastName().equals(traineeFromDB.getLastName())) {
            traineeFromDB.setLastName(newTrainee.getLastName());
        }
        if (!newTrainee.getAddress().equals(traineeFromDB.getAddress())) {
            traineeFromDB.setAddress(newTrainee.getAddress());
        }
        if (!newTrainee)
    }
}
