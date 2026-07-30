package org.example.service;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.service.generator.IdGenerator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.List;

@Service
public class TrainingService {
    private final TrainingDao trainingDao;
    private final IdGenerator idGenerator;

    public TrainingService(TrainingDao trainingDao, IdGenerator idGenerator) {
        this.trainingDao = trainingDao;
        this.idGenerator = idGenerator;
    }

    public Training findById(long id) {
        return trainingDao.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    public Training create(long traineeId, long trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Duration trainingDuration) {
        return trainingDao.save(new Training(
                idGenerator.nextTrainingId(),
                traineeId,
                trainerId,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        ));
    }
}
